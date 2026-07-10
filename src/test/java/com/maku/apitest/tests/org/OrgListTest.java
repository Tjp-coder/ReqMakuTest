package com.maku.apitest.tests.org;

import com.maku.apitest.api.OrgApi;
import com.maku.apitest.model.common.CommonResp;
import com.maku.apitest.model.org.SysOrgVO;
import com.maku.apitest.tests.base.BaseTest;
import com.maku.apitest.utils.JsonTemplateUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * 机构列表查询接口测试：GET /sys/org/list
 *
 * 该接口无请求参数（暂无 query 参数），但服务端有几个真实存在的行为分支：
 * 1. TreeUtils.build() 按 pid 把扁平列表组装成树（children 嵌套）
 * 2. SQL 里 order by sort asc，同级机构按 sort 升序排列
 * 3. SQL 里 where deleted = 0，软删除的机构不会出现在列表里
 * 这几点都是"服务端真的会基于字段做不同行为"，所以分别建了独立用例覆盖。
 */
@Feature("机构管理模块")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrgListTest extends BaseTest {
    private OrgApi orgApi;

    private static final String ROOT_ORG = "test_v3_org_list_001";
    private static final String CHILD_ORG = "test_v3_org_list_001_child";
    private static final String SORT_ORG_LOW = "test_v3_org_list_sort_low";
    private static final String SORT_ORG_HIGH = "test_v3_org_list_sort_high";
    private static final String DELETED_ORG = "test_v3_org_list_deleted";

    private Long rootOrgId;

    @BeforeAll
    void setupTestData() {
        orgApi = new OrgApi(specFactory);

        cleanupTestData();

        // 根机构：用作正向断言的锚点，同时是子机构/排序机构的父级
        createOrg(ROOT_ORG, 0L, 999);
        rootOrgId = queryOrgId(ROOT_ORG);

        // 子机构：挂在根机构下，验证 children 嵌套
        createOrg(CHILD_ORG, rootOrgId, 1);

        // 两个同级机构，sort 故意反着建，验证服务端按 sort 升序返回
        createOrg(SORT_ORG_HIGH, rootOrgId, 20);
        createOrg(SORT_ORG_LOW, rootOrgId, 10);
    }

    @AfterAll
    void tearDown() {
        cleanupTestData();
    }

    // ──────────────────────── 测试数据准备/清理 ────────────────────────

    private void cleanupTestData() {
        // 子机构在前、根机构在后：避免依赖删除顺序（sys_org 无外键约束，这里只是保持语义清晰）
        jdbcTemplate.update("DELETE FROM sys_org WHERE name IN (?,?,?,?,?)",
                CHILD_ORG, SORT_ORG_LOW, SORT_ORG_HIGH, DELETED_ORG, ROOT_ORG);
    }

    private void createOrg(String name, Long pid, int sort) {
        String body = JsonTemplateUtil.load("template/org/save.json")
                .set("$.pid", pid)
                .set("$.name", name)
                .set("$.sort", sort)
                .toJson();
        Response response = orgApi.save(token, body);
        assertThat((Integer) response.path("code"))
                .as("@BeforeAll: 创建机构 " + name + " 应成功").isEqualTo(0);
    }

    private Long queryOrgId(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM sys_org WHERE name = ? AND deleted = 0", Long.class, name);
    }

    // 把树状结构拍平成一维列表，方便断言"某机构是否存在于任意层级"
    private List<SysOrgVO> flatten(List<SysOrgVO> orgs) {
        List<SysOrgVO> result = new ArrayList<>();
        for (SysOrgVO org : orgs) {
            result.add(org);
            if (org.getChildren() != null) {
                result.addAll(flatten(org.getChildren()));
            }
        }
        return result;
    }

    private SysOrgVO findByName(List<SysOrgVO> orgs, String name) {
        return flatten(orgs).stream()
                .filter(org -> name.equals(org.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("列表中未找到机构: " + name));
    }

    // ──────────────────────── 正向用例 ────────────────────────

    @Test
    @Story("列表查询")
    @DisplayName("列表查询-正向：返回全部机构列表，结构符合 Schema")
    void should_return_org_list_when_default_call() {
        Response response = orgApi.list(token);
        CommonResp<List<SysOrgVO>> resp = response.as(new TypeRef<CommonResp<List<SysOrgVO>>>() {
        });

        response.then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/org/list.json"));
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isNotNull();
        assertThat(flatten(resp.getData()))
                .as("列表中应包含预置的测试机构")
                .extracting(SysOrgVO::getName)
                .contains(ROOT_ORG);
    }

    @Test
    @Story("列表查询")
    @DisplayName("列表查询-正向：子机构应嵌套在父机构的 children 中")
    void should_nest_child_org_under_parent_when_pid_matches() {
        Response response = orgApi.list(token);
        CommonResp<List<SysOrgVO>> resp = response.as(new TypeRef<CommonResp<List<SysOrgVO>>>() {
        });

        SysOrgVO root = findByName(resp.getData(), ROOT_ORG);

        assertThat(root.getChildren())
                .as("根机构的 children 中应包含子机构")
                .extracting(SysOrgVO::getName)
                .contains(CHILD_ORG);
    }

    @Test
    @Story("列表查询")
    @DisplayName("列表查询-正向：同级机构按 sort 升序排列")
    void should_order_children_by_sort_ascending() {
        Response response = orgApi.list(token);
        CommonResp<List<SysOrgVO>> resp = response.as(new TypeRef<CommonResp<List<SysOrgVO>>>() {
        });

        SysOrgVO root = findByName(resp.getData(), ROOT_ORG);

        List<String> sortedChildNames = root.getChildren().stream()
                .map(SysOrgVO::getName)
                .filter(name -> name.equals(SORT_ORG_LOW) || name.equals(SORT_ORG_HIGH))
                .collect(Collectors.toList());

        assertThat(sortedChildNames)
                .as("sort 值小的机构应排在前面")
                .containsExactly(SORT_ORG_LOW, SORT_ORG_HIGH);
    }

    // ──────────────────────── 异常/边界用例 ────────────────────────

    @Test
    @Story("列表查询")
    @DisplayName("列表查询-异常：已删除的机构不应出现在列表中")
    void should_exclude_org_when_org_is_deleted() {
        // ① 准备：额外建一个机构，立即软删除
        createOrg(DELETED_ORG, rootOrgId, 999);
        Long deletedId = queryOrgId(DELETED_ORG);
        Response deleteResponse = orgApi.delete(token, deletedId);
        assertThat((Integer) deleteResponse.path("code"))
                .as("@Given: 软删除机构应成功").isEqualTo(0);

        // ② 调用
        Response response = orgApi.list(token);
        CommonResp<List<SysOrgVO>> resp = response.as(new TypeRef<CommonResp<List<SysOrgVO>>>() {
        });

        // ④ 断言：deleted=0 是 SQL 里的真实过滤条件，软删除后不应再出现在任何层级
        assertThat(flatten(resp.getData()))
                .as("软删除的机构不应出现在列表中")
                .extracting(SysOrgVO::getName)
                .doesNotContain(DELETED_ORG);
    }

    @Test
    @Story("列表查询")
    @DisplayName("列表查询-未授权：无 token 返回 code 401")
    void should_return_401_when_no_token() {
        Response response = orgApi.list("");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((Integer) response.path("code")).isEqualTo(401);
        assertThat((String) response.path("msg")).isEqualTo("还未授权，不能访问");
    }
}
