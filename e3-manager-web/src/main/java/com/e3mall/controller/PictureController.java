package com.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3mall.common.utils.JsonUtils;
import com.e3mall.utils.FastDFSClient;

/*
 * 图片上传
 */
@Controller
public class PictureController {
	
	//读取配置文件中数据
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	//指定响应结果的content-type
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		Map<String,Object> map = new HashMap<>();
		try {
			//1.接收上传的文件  
            //2.获取扩展名 
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			//3.图片上传到服务器
			FastDFSClient fastDFSClient = new FastDFSClient("F:/study/e3mall/e3mall/git/e3-manager-web/src/main/resources/resource/client.conf");
			//返回图片路径
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url=IMAGE_SERVER_URL+url;
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.ObjectToJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", 0);
			map.put("messger", "图片上传失败!");
			return JsonUtils.ObjectToJson(map);
		}
	}
}
