package com.maku.apitest.model.org;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysOrgVO {
    private Long id;
    private Long pid;
    private List<SysOrgVO> children;
    private String name;
    private Integer sort;

    private String createTime;
    private String parentName;
}
