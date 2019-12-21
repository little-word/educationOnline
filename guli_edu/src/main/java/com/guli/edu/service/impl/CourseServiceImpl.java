package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import com.guli.edu.entity.vo.CourseInfoVo;
import com.guli.edu.entity.vo.CourseQuery;
import com.guli.edu.entity.vo.CourseWebVo;
import com.guli.edu.mapper.CourseMapper;
import com.guli.edu.service.CourseDescriptionService;
import com.guli.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Override
    public String saveCourse(CourseInfoVo courseInfoVo) {

        //添加课程基本信息
        baseMapper.insert(courseInfoVo.getCourse());

        //2、获取Id
        String courseId = courseInfoVo.getCourse().getId();
        //3、设置描述表中的id
        courseInfoVo.getCourseDescription().setId(courseId);
        //4、插入课程描述对象
        courseDescriptionService.save(courseInfoVo.getCourseDescription());

        return courseId;
    }

    //发布项目列表搜索
    @Override
    public void getPageByQuery(Page<Course> pageParam, CourseQuery courseQuery) {

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, wrapper);
            return;
        }
        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.like("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.ge("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id", teacherId);
        }
        baseMapper.selectPage(pageParam,wrapper);
    }

    @Override
    public CourseInfoVo getCourseInfoById(String courseId) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //1、获取Course
        Course course = baseMapper.selectById(courseId);
        courseInfoVo.setCourse(course);
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setCourseDescription(courseDescription);

        return courseInfoVo;
    }

    /**
     * 修改发布课程信息
     */
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {

        //两张表分别保存
        //保存课程基本信息
        baseMapper.updateById(courseInfoVo.getCourse());

        //保存课程详情信息
        //courseDescription.setDescription(courseQuery.getDescription());
        courseDescriptionService.updateById(courseInfoVo.getCourseDescription());
    }

    @Override
    public Map<String, Object> getPublishInfoByCourseId(String courseId) {

        return baseMapper.getPublishInfoByCourseId(courseId);
    }

    /**
     * NUXT根据
     * 讲师id查询当前
     * 讲师的课程列表
     * @param teacherId
     * @return
     */
    @Override
    public List<Course> selectByTeacherId(String teacherId) {

        //讲师的课程列表
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        //按照最后更新时间倒序排列
        wrapper.orderByDesc("gmt_modified");
        List<Course> courses =  baseMapper.selectList(wrapper);
        return courses;
    }

    @Override
    public Map<String, Object> pageListWeb(Page<Course> pageParam, Object o) {

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_modified");
        baseMapper.selectPage(pageParam,wrapper);

        List<Course> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getCourseInfoWebByCourseId(String courseId) {

        //更新课程浏览数
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        ////NUXT前端课程详情展示
        return baseMapper.getCourseInfoWebByCourseId(courseId);
    }
}
