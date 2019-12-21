package com.guli.oss.controller;

import com.guli.common.vo.Result;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author GPX
 * @date 2019/12/9 15:02
 */
@Api(description="阿里云文件管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/oss")
public class FileController {
    //注入Bean
    @Autowired
    private FileService fileService;

    //文件上传 返回一个URL
//    @PostMapping("file/upload")
//    @ApiOperation(value = "文件上传")
//    public Result upload(MultipartFile file){
//       String url= fileService.upload(file);
//        System.out.println(url);
//        return Result.ok().data("url",url);
//    }
    //上传封面
    @PostMapping("file/upload")
    @ApiOperation(value = "文件上传")
    public Result upload(MultipartFile file,
                         @RequestParam(required =false)String host){//cover封面
        if(host != null){
            ConstantPropertiesUtil.FILE_HOST=host;
        }
        String url= fileService.upload(file);

        System.out.println(url);
        return Result.ok().data("url",url);
    }
}
