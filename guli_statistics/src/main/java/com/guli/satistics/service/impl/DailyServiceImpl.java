package com.guli.satistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.satistics.client.UcenterClient;
import com.guli.satistics.service.DailyService;
import com.guli.satistics.entity.Daily;
import com.guli.satistics.mapper.DailyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createDailyByDay(String day) {
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        //判断daily是否有值
        Daily daily = baseMapper.selectOne(wrapper);
        if (daily != null) {
            //有值删除
            baseMapper.deleteById(daily.getId());
        }
        //无值添加---> 登录人数 注册人数 视频播放量 课程数
        //调用Feign 获取数据
        Integer rejisterNum = (Integer) ucenterClient.registerCount(day).getData().get("count");

        Integer loginNum = RandomUtils.nextInt(100, 200);//登录人数
        //Integer rejisterNum = RandomUtils.nextInt(100, 200);// 注册人数
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//视频播放量
        Integer courseNum = RandomUtils.nextInt(100, 200);//课程数


        daily = new Daily();
        daily.setLoginNum(loginNum);
        daily.setRegisterNum(rejisterNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> getShow(String type, String begin, String end) {
        //分别获取对应的数据
        //学员登录数统计
        //学员注册数统计
        //课程播放数统计
        //每日课程数统计
        //type 传入需要查询的--->字段名  "register_num":  this.title = "学员注册数统计";
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.select("date_Calculated",type);
        wrapper.between("date_calculated",begin,end);
        List<Daily> dailyList = baseMapper.selectList(wrapper);
        Map<String, Object> map = new HashMap<>();

        ArrayList<Integer> dataList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();

        map.put("dataList",dataList);
        map.put("dateList",dateList);
        for (Daily daily : dailyList) {
            //获取被统计的时间  每个字段都有时间
            dateList.add(daily.getDateCalculated());
            switch (type) { // 获取查询的字段
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        return map;
    }
}
