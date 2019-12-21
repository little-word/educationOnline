package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.Result;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.vo.CourseInfoVo;
import com.guli.edu.entity.vo.CourseQuery;
import com.guli.edu.entity.vo.CourseWebVo;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/edu/course")
@CrossOrigin
@Api(description = "课程发布管理")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    //新增课程
    //操作两张表 单张表的查询 语句不能用
    @ApiOperation(value = "添加发布课程信息")
    @PostMapping("/addCourse")
    public Result addCourse(
            @RequestBody CourseInfoVo courseInfoVo) {

//        BigDecimal price = courseInfoVo.getCourse().getPrice();
//        String s = String.valueOf(price);
//        if("0".equals(s)){
//            return Result.error().message("价格不能为空");
//        }else {
//
//        }
        String courseId=courseService.saveCourse(courseInfoVo);
        return Result.ok().data("courseId",courseId);
    }

    //课程发布搜索对象
    @PostMapping("{page}/{limit}")
    @ApiOperation(value = "发布课程列表查询")
    public Result pageQuery(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit,
            @RequestBody CourseQuery courseQuery
    ) {
        Page<Course> pageParam = new Page<>(page,limit);
        courseService.getPageByQuery(pageParam, courseQuery);
        return Result.ok().data("total",pageParam.getTotal()).data("rows",pageParam.getRecords());
    }


    /**
     * 根据课程Id获取课程的详细信息
     */
    @GetMapping("/getCourseInfoById/{courseId}")
    @ApiOperation(value = "获取课程详情")
    public Result getCourseInfoById(@PathVariable("courseId") String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfoById(courseId);
        return Result.ok().data("courseInfo", courseInfoVo);
    }

    @PostMapping("/updateCourseInfo")
    @ApiOperation(value = "修改发布课程信息")
    public Result updateCourseInfo(
            @RequestBody CourseInfoVo courseInfoVo
    ){
       courseService.updateCourseInfo(courseInfoVo);
        return  Result.ok();
    }

    /**
     * 课程发布信息
     */
    @GetMapping("/publish/{courseId}")
    @ApiOperation(value = "根据课程的Id，获取课程最终发布信息")
    public Result getPublishInfoByCourseId(@PathVariable("courseId") String courseId){

        Map<String, Object> map= courseService.getPublishInfoByCourseId(courseId);
        return Result.ok().data(map);
    }

    /**
     * 项目发布课程删除
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "项目发布课程删除")
    public Result removeBySubjectId(@PathVariable("id") String id){
        courseService.removeById(id);
        return Result.ok();
    }
    /**
     * 显示发布成功
     */
    @PutMapping("/changeStatus/{courseId}")
    @ApiOperation(value = "显示已发布")
    public Result changeStatus(@PathVariable("courseId") String courseId){

        Course course = courseService.getById(courseId);
        course.setStatus("Normal");

        courseService.updateById(course);
        return Result.ok();
    }
    /**
     * NUXT前端课程详情展示
     */
    @GetMapping("{page}/{limit}")
    @ApiOperation(value ="NUXT前端课程详情展示" )

    public Result pageList(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit){

        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.pageListWeb(pageParam,null);
        return  Result.ok().data(map);
    }
    /**
     * NUXT获取课程详情页
     */
    @ApiOperation(value = "NUXT获取课程详情页")
    @GetMapping("/getCourseInfoWeb/{courseId}")
    public Result getCourseInfoWebByCourseId(@PathVariable("courseId") String courseId){

        //课程详情
        //查询课程信息和讲师信息
        CourseWebVo dataList = courseService.getCourseInfoWebByCourseId(courseId);

        //课程大纲详情
        List<Map<String, Object>> chapterList = chapterService.getChapterListByCourseId(courseId);
        return  Result.ok().data("dataList",dataList).data("chapterList",chapterList);
    }
}

