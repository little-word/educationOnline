package com.guli.edu.controller;


import com.guli.common.vo.Result;
import com.guli.edu.entity.Video;
import com.guli.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@RestController
@CrossOrigin
@RequestMapping("/edu/video")
@Api(description = "视频管理")
public class VideoController {

    @Autowired
    private VideoService videoService;
    /**
     * 添加视频信息
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加视频")
    public Result save(@RequestBody Video video){
        videoService.save(video);
        return Result.ok();
    }
    /**
     * 查询视频 数据回显
     */
    @GetMapping("/getVideoById/{videoId}")
    @ApiOperation(value = "查询视频")
    public Result getVideoById(@PathVariable("videoId") String videoId){

        Video video = videoService.getById(videoId);
        return  Result.ok().data("video",video);
    }

    /**
     * 修改课时
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改视频")
    public Result udpateById(@RequestBody Video video){
        videoService.updateById(video);
        return Result.ok();
    }

    /**
     * 根据课时Id删除课时信息
     */
    @DeleteMapping("/removeById/{videoId}")
    @ApiOperation(value = "删除视频")
    public Result removeById(@PathVariable("videoId") String videoId){
        videoService.removeVideoById(videoId);
        return  Result.ok();
    }
}

