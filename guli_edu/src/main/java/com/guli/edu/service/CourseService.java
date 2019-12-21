package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.vo.CourseInfoVo;
import com.guli.edu.entity.vo.CourseQuery;
import com.guli.edu.entity.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface CourseService extends IService<Course> {

    // String saveCourse(CourseInfoVo courseInfoVo);
    String saveCourse(CourseInfoVo courseInfoVo);

    //发布项目列表搜索
    void getPageByQuery(Page<Course> pageParam, CourseQuery courseQuery);

    CourseInfoVo getCourseInfoById(String courseId);

    void updateCourseInfo(CourseInfoVo courseQuery);

    //根据课程的Id，获取课程最终发布信息
    Map<String, Object> getPublishInfoByCourseId(String courseId);

    //NUXT获取讲师课程展示
    List<Course> selectByTeacherId(String teacherId);

    //NUXT前端课程分页展示
    Map<String, Object> pageListWeb(Page<Course> pageParam, Object o);

    CourseWebVo getCourseInfoWebByCourseId(String courseId);

    //NUXT前端课程详情展示

}
