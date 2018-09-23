package com.merlin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import com.merlin.entity.FastDFSFile;

public class FastDFSClientUtil {

	public static Log logger = LogFactory.getLog(FastDFSClientUtil.class);

	private static StorageClient storageClient = null;

	static {
		try {
			String filePath = new ClassPathResource("fdfs/fdfs_client.conf")
					.getFile().getAbsolutePath();
			;
			ClientGlobal.init(filePath);
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = trackerClient
					.getStoreStorage(trackerServer);
			storageClient = new StorageClient(trackerServer, storageServer);
		} catch (Exception e) {
			logger.error("FastDFS Client Init Fail!", e);
		}
	}

	/**
	 *  文件上传
	 * @param file
	 * @param metaList 文件参数对
	 * @return
	 */

	public static String[] upload(FastDFSFile file, Map<String, String> metaList) {
		logger.info("File Name: " + file.getName() + "File Length:"
				+ file.getContent().length);
		if(MapUtils.isEmpty(metaList)){
			metaList = new HashMap<String, String>();
		}

		NameValuePair[] nameValuePairs = null;
		if (metaList != null) {
			nameValuePairs = new NameValuePair[metaList.size()];
			int index = 0;
			for (Iterator<Map.Entry<String, String>> iterator = metaList
					.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = iterator.next();
				String name = entry.getKey();
				String value = entry.getValue();
				nameValuePairs[index++] = new NameValuePair(name, value);
			}
		}
		
		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		try {
			uploadResults = storageClient.upload_file(file.getContent(),
					file.getExt(), nameValuePairs);
		} catch (IOException e) {
			logger.error(
					"IO Exception when uploadind the file:" + file.getName(), e);
		} catch (Exception e) {
			logger.error(
					"Non IO Exception when uploadind the file:"
							+ file.getName(), e);
		}
		logger.info("upload_file time used:"
				+ (System.currentTimeMillis() - startTime) + " ms");

		if (uploadResults == null) {
			logger.error("upload file fail, error code:"
					+ storageClient.getErrorCode());
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];

		logger.info("upload file successfully!!!" + "group_name:" + groupName
				+ ", remoteFileName:" + " " + remoteFileName);
		return uploadResults;
	}

	/**
	 * 使用FastDFS提供的客户端storageClient来进行文件上传，最后将上传结果返回。 根据groupName和文件名获取文件信息。
	 */
	public static FileInfo getFile(String groupName, String remoteFileName) {
		try {
			return storageClient.get_file_info(groupName, remoteFileName);
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}
	
	
	/**
	 * 通过对应的组名和上传文件的全名，获取上传文件的相关参数
	 * @param groupName 上传文件分组名
	 * @param remoteFilename 上传文件的远端全路径名
	 * @return
	 */
	public static Map<String, String> getFileMetadata(String groupName, String remoteFilename) {
		try {
			NameValuePair[] metaList = storageClient.get_metadata(groupName, remoteFilename);
			if (metaList != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (NameValuePair metaItem : metaList) {
					map.put(metaItem.getName(), metaItem.getValue());
				}
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 文件下载
	 * @param groupName 上传文件分组名
	 * @param remoteFilename 上传文件的远端全路径名
	 * @return
	 */
	public static InputStream downFile(String groupName, String remoteFileName) {
		try {
			byte[] fileByte = storageClient.download_file(groupName,
					remoteFileName);
			if(fileByte == null || fileByte.length == 0){
				System.out.println("获取文件已经被删除！");
				return null;
			}
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	// 删除文件
	public static int deleteFile(String groupName, String remoteFileName)
			throws Exception {
		int i = storageClient.delete_file(groupName, remoteFileName);
		logger.info("delete file successfully!!!" + i);
		return i;
	}
}
