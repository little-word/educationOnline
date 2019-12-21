package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.edu.client.VodClient;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodClient vodClient;
    @Override
    public void removeVideoById(String videoId) {
        //先根据课时Id查询课时信息
        Video video = baseMapper.selectById(videoId);
        //TODO 2、删除云端视频
        vodClient.removeVod(video.getVideoSourceId());

        //3、删除数据库记录
        baseMapper.deleteById(videoId);
    }

    /**
     * 根据章节ID删除小节信息
     * @param chapterId
     */

    @Override
    public void removeVideoByChapterId(String chapterId) {

        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        List<Video> videoList = baseMapper.selectList(wrapper);
        for(Video video : videoList){
            this.removeVideoById(video.getId());
        }
    }
}
