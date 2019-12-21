package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.vo.TeacherQuery;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface TeacherService extends IService<Teacher> {

    Boolean deleteById(String id);


    void queryTeacherByPageAndParam(Page<Teacher> pageParam,  TeacherQuery teacherQuery);

    //NUXT 前端分页展示
    Map<String,Object> pageListWeb(Page<Teacher> pageParam, Object o);
}
