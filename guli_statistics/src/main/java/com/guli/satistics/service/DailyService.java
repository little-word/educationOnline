package com.guli.satistics.service;

import com.guli.satistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
public interface DailyService extends IService<Daily> {

    void createDailyByDay(String day);

    Map<String, Object> getShow(String type, String begin, String end);
}
