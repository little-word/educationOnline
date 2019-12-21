package com.guli.edu.controller;


import com.guli.common.vo.Result;
import com.guli.edu.entity.Chapter;
import com.guli.edu.service.ChapterService;
import com.sun.org.apache.regexp.internal.RE;
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
@RequestMapping("/edu/chapter")
@CrossOrigin
@Api(description = "章节列表")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    /**
     * 获取课程大纲
     * getChapterList
     */
    @ApiOperation(value = "获取课程大纲")
    @GetMapping("/getChapterList/{courseId}")
    public Result getChapterList(@PathVariable("courseId") String courseId) {
        List<Map<String, Object>> mapList = chapterService.getChapterListByCourseId(courseId);
        return Result.ok().data("list", mapList);
    }

    /**
     * 添加章节信息
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加章节信息")
    public Result save( @RequestBody Chapter chapter ) {
        chapterService.save(chapter);
        return Result.ok();
    }
    /**
     * 获取章节信息 用于回显数据
     */
    @GetMapping("/getChapterById/{chapterId}")
    @ApiOperation(value = "获取章节信息")
    public Result getChapterById(@PathVariable("chapterId") String chapterId){

        Chapter chapter = chapterService.getById(chapterId);
        return Result.ok().data("item",chapter);
    }
    /**
     * 删除章节信息  写错了
     */
    @DeleteMapping("/removeByChapterId/{chapterId}")
    @ApiOperation(value = "删除章节Id")
    public Result removeByChapterId(@PathVariable("chapterId")String chapterId){
        chapterService.removeById(chapterId);
        return Result.ok();
    }
    /**
     * 修改章节信息
     */
    @ApiOperation(value = "修改章节信息")
    @PostMapping("update")
    public Result updateById(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.ok();
    }

}

