<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"     "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<title>[bizwork]给分平台</title>
<style type="text/css">
body{background:#fefefe; margin:20px; font-family:Georgia}
span{font-size:55px; font-weight:bold; color:#900}
p{margin:20px 0; font-size:40px; font-weight:bold; font-family:"微软雅黑"; color:#ccc}
div{height:80px; line-height:25px; color:#333; font-size:12px}
div a{color:#333}
</style>
</head>
<body style="color: #4169E1" align="center">
		<br><br><br><br>
	<%  
		String taskId = request.getParameter("taskId");
		int status = Integer.parseInt(request.getParameter("status"));
	    if(status == 1) {%>
		<p><font size="8" face="arial" color="red"><i><a href="http://api.task.bizwork.sogou/task/confirmScore.action?status=6&taskId=<%=taskId%>">确认</a>给分</i></font></p>
		<%} else if (status == 2) {%>
		<p><font size="30" face="verdana" align="center">已给分成功，无法再次给分！</font></p>
		<hr>
		<p><font size="8" face="arial" color="red"><i><a href="http://api.task.bizwork.sogou/task/cancelScore.action?status=5&taskId=<%=taskId%>">撤销</a>给分</i></font></p>
		<%} else if (status == -1) {%>
		<p><font size="10" face="verdana" align="center">给分失败，因为该积分任务已被删除！</font></p>
		<%} else if (status == 0) {%>
		<p><font size="10" face="verdana" align="center">给分失败，因为该积分任务未完成！</font></p>
		<%} else if (status == 5) {%>
		<p><font size="10" face="verdana" align="center">积分已成功撤回！</font></p>
		<%} else if (status == 6) {%>
		<p><font size="10" face="verdana" align="center">恭喜您，给分成功！</font></p>
		<hr>
		<p><font size="8" face="arial" color="red"><i><a href="http://api.task.bizwork.sogou/task/cancelScore.action?status=5&taskId=<%=taskId%>">撤销</a>给分</i></font></p>
		<%} %>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
<p><font size="4" face="arial" color="blue">进入：<a href="http://bizwork.sogou-inc.com/home/" target="_blank">bizwork办公平台</a></font></p>
<p><font size="4" face="arial" color="blue">进入：<a href="http://task.bizwork.sogou-inc.com/" target="_blank">biztask办公平台</a></font></p>
</body>
</html>