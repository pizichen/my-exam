package com.merlin.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.merlin.FastDFSClientUtil;
import com.merlin.entity.FastDFSFile;

@CrossOrigin
@Controller("/")
public class FileController {
	
	public static Log logger = LogFactory.getLog(FileController.class);
	
	@RequestMapping(value="/home")
	public String home(){
		System.out.println("redirect to home page!");
		return "index";
	}
	
	@PostMapping(value="/upload")
	@ResponseBody
	public String upload(@RequestParam("file1") MultipartFile file) throws IOException{
		
		//判断文件是否为空
        if (file.isEmpty()) {
            return "-1";
        }
        String path=saveFile(file);
		return path;
	}
	
	
	@PostMapping(value="/download")
	@ResponseBody
	public static ResponseEntity<byte[]> download(String groupName,
	        String remoteFileName,String specFileName) {
		specFileName = "下载文件.png";
	    byte[] content = null;
	    HttpHeaders headers = new HttpHeaders();
	    try {
	    	InputStream inputStream = FastDFSClientUtil.downFile("group1", "M00/00/00/CgEFyluXkKuAc97PAAJYEDxIU1o443.png");
	        headers.setContentDispositionFormData("attachment",  new String(specFileName.getBytes("UTF-8"),"iso-8859-1"));
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		    if(inputStream!=null){
		        int len1 = inputStream.available();
		        content = new byte[len1];
		        inputStream.read(content);
		        inputStream.close();
		    }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
	}
	
	
	/**
	 * 	拼装 上传文件对象
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private String saveFile(MultipartFile multipartFile) throws IOException {
	    String[] fileAbsolutePath = {};
	    String fileName = multipartFile.getOriginalFilename();
	    String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
	    byte[] file_buff = null;
	    InputStream inputStream = multipartFile.getInputStream();
	    if(inputStream != null){
	        int len1 = inputStream.available();
	        file_buff = new byte[len1];
	        inputStream.read(file_buff);
	    }
	    inputStream.close();
	    FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
	    try {
	        fileAbsolutePath = FastDFSClientUtil.upload(file,null);  //upload to fastdfs
	    } catch (Exception e) {
	        logger.error("upload file Exception!",e);
	    }
	    if (fileAbsolutePath==null) {
	        logger.error("upload file failed,please upload again!");
	    }
//	    String path=FastDFSFileUtil.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
	    String path = fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
	    // group1/M00/00/00/CgEFyluXUiiAZZX9AAD5aJ3CBtA485.png
	    return path;
	}
}
