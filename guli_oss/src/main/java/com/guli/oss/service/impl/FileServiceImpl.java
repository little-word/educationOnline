package com.guli.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author GPX
 * @date 2019/12/9 15:09
 */
@Service
public class FileServiceImpl implements FileService {
    //上传文件校验
    public static final String[] TYPE = {".jpg", ".png", ".txt"};

    @Override
    public String upload(MultipartFile file) {


        //获取阿里云存储相关常量
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String fileHost = ConstantPropertiesUtil.FILE_HOST;

        OSSClient ossClient = null;
        boolean flag = false;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            //校验文件类型
            //获取文件类型
            for (String type : TYPE) {
                if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                System.err.println("文件类型正确");
            } else {
                throw new GuliException(20002, "文件类型不正确");
            }
            //校验内容
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null) {
                throw new GuliException(20002, "文件内容不符合规定");
            } else {
                System.err.println("文件的高度" + read.getHeight());
                System.err.println("文件的宽度" + read.getWidth());
            }


            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            //用作拼接唯一路径
            String filePath = new DateTime().toString("yyyy/MM/dd");
            String fileName = file.getOriginalFilename();
            //获取文件后缀  可有可无 但是 文件名 必须是唯一的UUid
            String ext = fileName.substring(fileName.lastIndexOf("."));
           // String newFileName = UUID.randomUUID().toString().replaceAll("-","") + ext;
            String newFileName = UUID.randomUUID()+ ext;

            //object name  拼接成唯一的路径
            String path = fileHost + "/" + filePath + "/" + newFileName; // avatar/2019/12/09/234567435673456.png
            //参数说明：1、buckName; 2、是保存的文件、里面也包含着保存的文件路径; 3、文件流
            ossClient.putObject(bucketName, path, inputStream);
            // https://oss-cn-beijing.aliyuncs.comgugulili-edu./avatar/2019/12/09/ba5d56f4-8635-4b65-bea4-1eb9bae20836.png
            // 错误 String url = "https://" + endpoint+"." + bucketName + "/" + path;

            String url = "https://" + bucketName + "." + endpoint + "/" + path;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return null;
    }
}
