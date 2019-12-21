package com.guli.edu.client;

import com.guli.common.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author GPX
 * @date 2019/12/17 12:40
 */
@Component
@FeignClient("guli-vod")
public interface VodClient {

    @DeleteMapping("vod/remove/{videoSourceId}")
    @ApiOperation(value = "删除单个视频")
    public Result removeVod(@PathVariable("videoSourceId") String videoSourceId);

}
