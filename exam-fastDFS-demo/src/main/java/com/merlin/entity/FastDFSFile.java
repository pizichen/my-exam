package com.merlin.entity;

import lombok.Data;

/**
 * 上传文件对象
 * @author aspire
 */
@Data
public class FastDFSFile {
	
	public FastDFSFile(String fileName, byte[] file_buff, String ext){
		this.name = fileName;
		this.content = file_buff;
		this.ext = ext;
	}
	// 文件名
	private String name;
	
	// 输入流二进制数组
	private byte[] content;
	
	// 文件后缀
	private String ext;
	
	// 加密方式
	private String md5;
	
	// 原文件作者
	private String author;
}
