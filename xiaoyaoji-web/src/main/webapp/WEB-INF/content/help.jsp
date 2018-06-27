<%--
  User: zhoujingjie
  Date: 17/4/4
  Time: 13:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--[if lt IE 9]> <script>location.href='unsupport.html?refer='+encodeURIComponent(location.href)</script> <![endif]-->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>在线帮助 - 小幺鸡</title>
    <jsp:include page="/WEB-INF/includes/meta.jsp"/>
    <link rel="stylesheet" href="${assets}/css/index.css?v=${v}">
</head>
<body>
<div id="app">
<jsp:include page="/WEB-INF/includes/header.jsp"/>
</div>
<div class="help-bg"></div>
<div class="mc help-content">
    <br/><br/>
    <h3>如果解决跨域访问？</h3>
    <div>
        <p>1.服务器提供<a href="http://baike.baidu.com/item/jsonp" target="_blank">JSONP</a> 支持</p>
        <p>2.服务器提供<a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS" target="_blank">CORS</a> 支持</p>
        <p>3.下载由小幺鸡提供的Chrome浏览器插件。
            <a href="https://chrome.google.com/webstore/detail/%E5%B0%8F%E5%B9%BA%E9%B8%A1/omohfhadnbkakganodaofplinheljnbd" target="_blank">chrome应用商店下载</a>
        </p>
    </div>
    <br/>
    <h3>无法访问谷歌应用商店?</h3>
    <div>
        <p>1.下载 <a href="https://s3.amazonaws.com/lantern/lantern-installer-beta.exe" target="_blank">lantern</a> 并安装 。然后打开即可正常访问。</p>
        <p>2.下载<a href="http://www.ishadowsocks.org/" target="_blank"> shadowsocks </a>  然后配置服务器。
        </p>
    </div>
    <br/>
</div>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

<jsp:include page="/WEB-INF/includes/footer.jsp"/>

</body>
</html>