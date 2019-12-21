package com.guli.edu.service;

import com.guli.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface VideoService extends IService<Video> {

    void removeVideoById(String videoId);
    //根据章节id删除小节信息
    void removeVideoByChapterId(String chapterId);

}
