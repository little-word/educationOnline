package com.guli.edu.mapper;

import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.edu.entity.vo.CourseWebVo;

import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface CourseMapper extends BaseMapper<Course> {

    Map<String,Object> getPublishInfoByCourseId(String courseId);

    CourseWebVo getCourseInfoWebByCourseId(String courseId);
}
