package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.Result;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.Teacher;
import com.guli.edu.entity.vo.TeacherQuery;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/edu/teacher")
@Api(description = "讲师管理")
@CrossOrigin //跨域访问
@Slf4j
public class TeacherController {


    @Autowired
    private TeacherService teacherService;

    @Autowired

    private CourseService courseService;

    //element-ui 修改成本地登录
//    @PostMapping("/login")
//    @ApiOperation(value = "vsc element-ui 登录入口")
//    public Result login(){
//        //{"code":20000,"data":{"token":"admin"}}
//        return Result.ok().data("token","admin");
//
//    }
//    //{"code":20000,"data":{"roles":["admin"],"name":"admin","avatar":
//    // "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
//    //Info
//    @GetMapping("/info")
//    @ApiOperation(value = "element-ui 登录信息")
//    public  Result info(){
//
//        return Result.ok().data("roles","[admin]").data("name","admin")
//                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//    }

    //获取所有讲师列表
    @GetMapping("/list")
    @ApiOperation(value = "讲师列表")
    public Result teacherList() {
        List<Teacher> list = teacherService.list(null);
        return Result.ok().data("items", list);
    }

    //根据ID删除讲师---- 逻辑删除 在entity-teacher 和application.properties 配置文件中 有配置
    @DeleteMapping("/removeById/{id}")
    @ApiOperation(value = "根据Id删除讲师")
    public Result removeTeacherById(
            @ApiParam(name = "id", value = "讲师Id", required = true)
            @PathVariable("id") String id) {

        try {
            //sql语句 执行成功 就不会报错
            // teacherService.removeById(id);
            //自己先写一个方法
            Boolean b = teacherService.deleteById(id);
            if (b) {
                return Result.ok();
            } else {
                throw new RuntimeException("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    //添加数据
    //name;intro;career;level;avatar;
    @PostMapping("/save")
    @ApiOperation(value = "添加讲师")
    public Result saveTeacher(
            @ApiParam(name = "teacher", value = "添加讲师", required = true)
            // Teacher teacher
            @RequestBody Teacher teacher) {

        teacherService.save(teacher);

        return Result.ok();
    }
    //修改讲师数据

    @PutMapping(value = "/update/{id}")
    @ApiOperation(value = "修改讲师数据")
    public Result updateTeacher(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id,
            @RequestBody Teacher teacher) {
        log.info(teacher + " " + id);
        teacher.setId(id);
        teacherService.updateById(teacher);
        return Result.ok();

    }
//    @ApiOperation(value = "修改讲师信息")
//    @PutMapping("update")
//    public Result updateById(@RequestBody Teacher teacher){
//        teacherService.updateById(teacher);
//        return Result.ok();
//    }

    //根据讲师Id查询讲师信息
    @ApiOperation(value = "根据Id查询讲师信息")
    @GetMapping("/getTeacherById/{id}")
    public Result getTeacherById(
            @ApiParam(name = "id", value = "讲师Id", required = true)
            @PathVariable("id") String id) {

        //异常测试
        //int i = 1/0;
        Teacher teacher = null;
        try {
            teacher = teacherService.getById(id);
            if (teacher != null) {
                return Result.ok().data("item", teacher);
            } else {
                throw new GuliException(20002, "没有此讲师信息");
                //throw new  RuntimeException("没有此讲师信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("没有此讲师信息");
        }

    }

    //分页查询  无参数

    @ApiOperation(value = "分页查询讲师信息- 无参数")
    @GetMapping("/queryTeacherByPage/{page}/{limit}")
    public Result queryTeacherByPage(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable long page,
            @ApiParam(name = "limit", value = "每页数据", required = true)
            @PathVariable long limit
    ) {

//        Page<Teacher> pages = new Page<>(page, limit);
//
//        teacherService.page(pages, null);
//        List<Teacher> records = pages.getRecords();
//        long total = pages.getTotal();
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("items", records);
//        map.put("total", total);
//        //return  Result.ok().data("数据",map);
//        return Result.ok().data("items", records).data("total", total);
        Page<Teacher> pageParam = new Page<>(page, limit);

        if (page <= 0 || limit <= 0) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        } else {
            teacherService.page(pageParam, null);

            Map<String, Object> map = new HashMap<>();
            map.put("total", pageParam.getTotal());
            map.put("records", pageParam.getRecords());
            map.put("current", pageParam.getCurrent());
            return Result.ok().data("items", map);
        }
    }

    //分页查询  带条件
    @ApiOperation(value = "分页查询讲师信息- 带参数")
    @PostMapping("/queryTeacherByPageAndParam/{page}/{limit}")
    public Result queryTeacherByPageAndParam(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable long page,
            @ApiParam(name = "limit", value = "每页数据", required = true)
            @PathVariable long limit,
            @ApiParam(name = "teacherQuery", value = "输入查询的条件")
            @RequestBody TeacherQuery teacherQuery
    ) {
//        Page<Teacher> pageParam = new Page<>(page,limit);
//        teacherService.queryTeacherByPageAndParam(pageParam,teacherQuery);
//
//        List<Teacher> records = pageParam.getRecords();
//        long total = pageParam.getTotal();
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("records",records);
//        map.put("total",total);
//        return Result.ok().data("Items", map);
        Page<Teacher> pageParam = new Page<>(page, limit);
        if (page <= 0 || limit <= 0) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        } else {
            teacherService.queryTeacherByPageAndParam(pageParam, teacherQuery);
            List<Teacher> records = pageParam.getRecords();
            long total = pageParam.getTotal();

            return Result.ok().data("total", total).data("rows", records);
        }

    }

    /**
     * NUXT前端页面的讲师展示
     */

    @GetMapping("/getPageList/{page}/{limit}")
    @ApiOperation(value = "NUXT前端页面的讲师展示")
    public Result getPageListWeb(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit
            ) {

        Page<Teacher> pageParam = new Page<>(page, limit);
        Map<String,Object> map= teacherService.pageListWeb(pageParam, null);

//        Map map = new HashMap<String, Object>();
//        map.put("total", pageParam.getTotal());
//        map.put("records", pageParam.getRecords());
//        map.put("current", pageParam.getCurrent());
//        map.put("hasPrevious", pageParam.hasPrevious());
//        map.put("hasNext", pageParam.hasNext());
//        map.put("pages", pageParam.getPages());

        return Result.ok().data(map);
    }

    /**
     * 根据讲师id查询讲师所讲课程列表
     */
   // selectByTeacherId
    @ApiOperation(value = "查询讲师所讲课程列表")
    @GetMapping(value = "{id}")
    public Result selectByTeacherId(@PathVariable("id") String id){

        //查询讲师信息
        Teacher teacher = teacherService.getById(id);

        //根据讲师id查询这个讲师的课程列表
       List<Course> courseList= courseService.selectByTeacherId(id);
        return  Result.ok().data("teacher", teacher).data("courseList", courseList);
    }

}
