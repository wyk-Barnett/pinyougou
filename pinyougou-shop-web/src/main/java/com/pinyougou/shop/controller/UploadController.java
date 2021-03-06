package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * @author wangyangkun
 * @date 2019/7/14 0014 21:00
 */
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String file_server_url;

    @RequestMapping("/upload.do")
    public Result upload(MultipartFile file){
        if (file!=null){
            String fileOriginalFilename = file.getOriginalFilename();
            String extName = fileOriginalFilename.substring(fileOriginalFilename.lastIndexOf(".") + 1);
            try {
                FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
                String fileId = client.uploadFile(file.getBytes(), extName);
                //图片完整地址
                String url = file_server_url+fileId;
                return new Result(true,url);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"上传失败");
            }
        }else {
            return new Result(false,"上传失败");
        }
    }
}
