<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery-2.1.1.min.js"></script>
</head>
<body>
    hello world!!!
    name=${name } welcome!
 	
      <form action="/upload" 
      	enctype="multipart/form-data" method="post">
         	上传文件1：<input type="file" name="file1"><br/>
         	<!-- 上传文件2：<input type="file" name="file2"><br/> -->
         <input type="submit" value="提交">
     </form>
     <form action="/download" method="post" >
	 	<input type="submit" value="下载">
	</form>
</body>
</html>