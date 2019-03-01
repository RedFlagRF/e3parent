package com.e3.controller;

import com.e3.commons.util.FastDFSClient;
import com.e3.commons.util.JsonUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author RedFlag
 * @Description 图片处理Controller
 * @Date 13:15 2019/1/25
 */
@Controller
public class PictureController {
    @Value("${IMAGE_SERVER}")
    private String IMAGE_SERVER;

    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String  uploadFile(MultipartFile uploadFile) {
        Map<String ,Object> result = new HashMap<>();
        try {
            //图片上传图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            //得到图片地址,文件名
            String extension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extension);
            //补充完整url
            url = IMAGE_SERVER + url;
            //封装到map
            result.put("error", 0);
            result.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }
        return JsonUtils.objectToJson(result);
    }
}
