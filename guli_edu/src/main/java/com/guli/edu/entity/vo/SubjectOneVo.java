package com.guli.edu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GPX
 * @date 2019/12/10 17:56
 */
@Data
public class SubjectOneVo {
    private String id;
    private  String title;

    //把子节点和父节点连接起来
    private List<SubjectTwoVo> children = new ArrayList<>();
}
