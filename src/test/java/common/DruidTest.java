package common;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.List;
import java.util.Map;

public class DruidTest {
    //通过数据库连接池对象获取JdbcTemplate对象
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @SneakyThrows
    @Test
    public void test1(){
        String sql = "select * from sys_org where id = ? ";
        Map<String,Object> map = jdbcTemplate.queryForMap(sql,3);
        List list= jdbcTemplate.queryForList(sql,3);
        System.out.println(map);
        System.out.println(list);

        //如果想断言数据库的值是否删除怎么实现
        //断言map就行
        //看看是否包含某个id

    }

}
