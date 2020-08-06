<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>我的报销单</title>
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="js/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function deleteBaoxiaoBill(id) {
		if (confirm("是否确定删除该流程？")) {
			location = "${pageContext.request.contextPath}/deleteBaoxiaoBill/" + id;
		}
	}
</script>
</head>
<body>
	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>报销管理</li>
		<li class="active">我的报销单</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<form class="form-inline">
			<div class="panel panel-default">
				<div class="panel-heading">报销单列表</div>

				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th width="10%">ID</th>
								<th width="15%">报销金额</th>
								<th width="15%">标题</th>
								<th width="25%">备注</th>
								<th width="25%">时间</th>
								<th width="15%">状态</th>
								<th width="30%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="baoxiao" items="${baoxiaoList}">
								<tr>
									<td>${baoxiao.id}</td>
									<td>${baoxiao.money}</td>
									<td>${baoxiao.title}</td>
									<td>${baoxiao.remark}</td>
									<td><fmt:formatDate value="${baoxiao.creatdate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
							 <c:if test="${baoxiao.state==2}">
							 		<td>审核完成</td>
									<td><a href="javascript:deleteBaoxiaoBill(${baoxiao.id})"
										class="btn btn-danger btn-xs"><span
											class="glyphicon glyphicon-remove"></span> 删除</a></td>
									<td><a href="${pageContext.request.contextPath}/findInMyBaoxiaoBill?id=${baoxiao.id}" class="btn btn-success btn-xs"><span
											class="glyphicon glyphicon-eye-open"></span> 查看审核记录</a></td>
							  </c:if>	
							  <c:if test="${baoxiao.state==1}">
							  		<td>审核中</td>
							  		<td><a href="${pageContext.request.contextPath}/findInMyBaoxiaoBill?id=${baoxiao.id}" class="btn btn-success btn-xs"><span
											class="glyphicon glyphicon-eye-open"></span> 查看审核记录</a></td>
									<td><a href="${pageContext.request.contextPath}/viewCurrentImageByBill?id=${baoxiao.id}" class="btn btn-success btn-xs"><span
											class="glyphicon glyphicon-eye-open"></span> 查看流程图</a></td>
							  </c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>
	<div id="page">
		【当前第${pageInfo.pageNum}页，总共${pageInfo.pages}页，总共${pageInfo.total}条记录】
		<c:choose>
			<c:when test="${pageInfo.pageNum==1}">
				             首页 上一页
				     <a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.nextPage}">下一页</a>
					 <a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.pages}">尾页</a>
			</c:when>

			<c:when test="${pageInfo.pageNum==pageInfo.pages}">
				<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=1">首页</a>
				 <a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.prePage}">上一页</a> 
				           下一页 尾页
				   </c:when>
			<c:otherwise>
				<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=1">首页</a> 
				<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.prePage}">上一页</a> 
				<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.nextPage}">下一页</a> 
				<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${pageInfo.pages}">尾页</a>
			</c:otherwise>
		</c:choose>	
	</div>
	</div>
</body>
</html>