package com.merlin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.springframework.core.annotation.Order;

import com.merlin.entity.FastDFSFile;

public class FastDFSClientTest {
	
	static String groupName = "group1";
	static String remoteFileName = "M00/00/00/CgEFyluXkZSAHDXbAAJYEO4iHGU313.png";
	
	/**
     * 文件上传测试
	 * @throws IOException 
     */
//    @Test
    public void testUpload() throws IOException {
        File file = new File("C:\\Users\\aspire\\Desktop\\image\\3.jpg");
        Map<String,String> metaList = new HashMap<String, String>();
        metaList.put("width","220");
        metaList.put("height","285");
        metaList.put("author","merlin");
        metaList.put("date","20180911");
        byte[] fileBuff = null;
        InputStream inputStream = new FileInputStream(file);
        if(inputStream != null){
	        int len1 = inputStream.available();
	        fileBuff = new byte[len1];
	        inputStream.read(fileBuff);
	    }
        inputStream.close();
        FastDFSFile dfsFile = new FastDFSFile(file.getName(), fileBuff, ".jpg");
        String[] paths = FastDFSClientUtil.upload(dfsFile, metaList);
        System.out.println("upload local file " + file.getPath() + " ok, path=" + paths[0] +"/"+paths[1]);
        //上传成功返回的文件ID： /group1/M00/00/00/CgEFylttBNGABBE1AAC1Qaj4zoI9.3.jpg
    }

    /**
     * 文件下载测试
     * @throws IOException 
     */
    @Test
    public void testDownload() throws IOException {
        InputStream is = FastDFSClientUtil.downFile(groupName, remoteFileName);
        File file = new File("DownloadFile_fid.png");
        FileOutputStream os = new FileOutputStream(file);
        
        
        byte[] bt = new byte[1024];
        int len = 0;
        while((len = is.read(bt)) != -1){
        	os.write(bt, 0, len);
        }
        os.flush();
        is.close();
        os.close();
    }

    /**
     * 获取文件元数据测试
     */
//    @Test
    public void testGetFileMetadata() {
        Map<String,String> metaList = FastDFSClientUtil.getFileMetadata(groupName, remoteFileName);
        if(MapUtils.isEmpty(metaList)){
        	return;
        }
        for (Iterator<Map.Entry<String,String>>  iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,String> entry = iterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            System.out.println(name + " = " + value );
        }
    }

    /**
     * 文件删除测试
     * @throws Exception 
     */
//    @Test
    public void testDelete() throws Exception {
        int i = FastDFSClientUtil.deleteFile(groupName, remoteFileName);
        System.out.println(i == 0 ? "删除成功" : "删除失败");
    }
}
