package com.guli.vod.controller;

import com.guli.common.vo.Result;
import com.guli.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import java.util.List;

/**
 * @author GPX
 * @date 2019/12/16 17:59
 */
@RestController
@CrossOrigin
@RequestMapping("/vod")
@Api(description = "视频上传")
public class UploadVodController {
    @Autowired
    private VodService vodService;

    /**
     * 上传视频
     */
    @PostMapping("/upload")
    @ApiOperation(value = "视频上传")
    public Result upload(MultipartFile file){
       String videoSourceId = vodService.uploadVod(file);
        return  Result.ok().data("videoSourceId",videoSourceId);

    }
    /**
     * 云端视频删除
     */

    @DeleteMapping("/remove/{videoSourceId}")
    @ApiOperation(value = "删除单个视频")
    public Result removeVod(@PathVariable("videoSourceId") String videoSourceId) throws Exception {
        vodService.removeVod(videoSourceId);
        return Result.ok();
    }
    /**
     * 批量删除
     */
    @DeleteMapping("/removeBatch")
    @ApiOperation(value = "批量删除视频")
    public Result removeBatch(@RequestParam("ids") List<String> ids) throws Exception {

        vodService.removeBatch(ids);
        return  Result.ok();
    }

    /**
     * 获取播放凭证
     */
    @ApiOperation(value = "获取播放凭证")
    @GetMapping("/getPlayAuth/{videoSourceId}")
    public Result getPlayAuth(@PathVariable("videoSourceId") String videoSourceId) throws Exception {

         String playAuth=vodService.getPlayAuthById(videoSourceId);
        return  Result.ok().data("playAuth",playAuth);
    }
}
