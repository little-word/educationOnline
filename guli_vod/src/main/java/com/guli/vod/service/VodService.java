package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List; /**
 * @author GPX
 * @date 2019/12/16 18:03
 */
public interface VodService {
    //视频上传
    String uploadVod(MultipartFile file);

    //单个删除
    void removeVod(String videoId) throws Exception;

    //批量删除
    void removeBatch(List<String> ids) throws Exception;

    //获取播放凭证
    String getPlayAuthById(String videoSourceId) throws Exception;
}
