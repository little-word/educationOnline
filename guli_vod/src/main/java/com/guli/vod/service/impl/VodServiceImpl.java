package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.guli.vod.service.VodService;
import com.guli.vod.utils.AliyunVodSDKUtils;
import com.guli.vod.utils.ConstantPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.guli.vod.utils.AliyunVodSDKUtils.initVodClient;

/**
 * @author GPX
 * @date 2019/12/16 18:05
 */
@Service
public class VodServiceImpl implements VodService {

    /**
     * 视频上传 流式上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadVod(MultipartFile file) {
        try {
            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")),
                    file.getOriginalFilename(), file.getInputStream());
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                return response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
//                System.out.print("VideoId=" + response.getVideoId() + "\n");
//                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据VideoId云端删除视频
     * @param videoId
     * @throws Exception
     */
    @Override
    public void removeVod(String videoId) throws Exception {

        DefaultAcsClient client = null;
        try {
            client = initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);

            DeleteVideoResponse response = client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    //批量删除
    @Override
    public void removeBatch(List<String> ids) throws Exception {
        try {
            DefaultAcsClient client = initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            //批量删除，将集合拆分的工具类
            String join = StringUtils.join(ids, ",");
            //方式二
            String array = org.springframework.util.StringUtils.arrayToDelimitedString(ids.toArray(), ",");
            request.setVideoIds(join);

            DeleteVideoResponse response = client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取播放凭证
     * @param videoSourceId
     * @return
     * @throws Exception
     */
    @Override
    public String getPlayAuthById(String videoSourceId) throws Exception {


        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //请求
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoSourceId);

            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //得到播放凭证
            String playAuth = response.getPlayAuth();
            System.err.println("playAuth:"+playAuth);

            return playAuth;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
