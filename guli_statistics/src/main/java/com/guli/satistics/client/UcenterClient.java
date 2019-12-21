package com.guli.satistics.client;

import com.baomidou.mybatisplus.extension.api.R;
import com.guli.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author GPX
 * @date 2019/12/11 18:42
 */
@Component
@FeignClient("guli-ucenter")
public interface UcenterClient {
    /**
     * 注意：一定要写成 @PathVariable("day")，圆括号中的"day"不能少
     * @param day
     * @return
     */
    @GetMapping(value = "/ucenter/member/registerCount/{day}")
    public Result registerCount(@PathVariable("day") String day);
}
