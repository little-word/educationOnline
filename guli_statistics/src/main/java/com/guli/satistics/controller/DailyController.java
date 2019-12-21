package com.guli.satistics.controller;


import com.guli.common.vo.Result;
import com.guli.satistics.entity.Daily;
import com.guli.satistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
@RestController
@RequestMapping("/statistics/daily")
@CrossOrigin
@Api(description = "统计表")
public class DailyController {

    @Autowired
    private DailyService dailyService;
    /**
     * 生成统计
     */
    @GetMapping("/createDaily/{day}")
    @ApiOperation(value = "生成统计")
    public Result createDailyByDay(
            @ApiParam(name = "day",value = "",required = false)
            @PathVariable("day") String day){
        dailyService.createDailyByDay(day);
        return  Result.ok();
    }

    /**
     *根据查询的类型和时间查询注册人数的个数和时间的集合
     */
    @GetMapping("/show/{type}/{begin}/{end}")
    public Result show(
            @PathVariable("type") String type,
            @PathVariable("begin") String begin,
            @PathVariable("end") String end
    ){
      Map<String,Object> map =  dailyService.getShow(type,begin,end);
        return  Result.ok().data(map);
    }
}

