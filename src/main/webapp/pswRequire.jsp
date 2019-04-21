<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>验证访问密码</title>
    <link rel="stylesheet" href="<%=basePath %>layui/css/modules/layer/default/layer.css">
    <script src="<%=basePath %>js/jquery-3.3.1.js"></script>
    <script src="<%=basePath %>layui/lay/modules/layer.js"></script>
</head>
<body>
<form id="form" action="<%=basePath %>/r">
    <input type="hidden" id="password" name="password" value="">
    <input type="submit" value="">
</form>
</body>
<script>
    layer.prompt({title: '请输入访问密码', formType: 1}, function(pass, index){
        layer.close(index);
        $('#password').val(pass);
        $('#form').submit();
    });
</script>
</html>
