<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="bean" class="service.bean.EditBean" scope="request" />
<%@ page import="domain.Point"%>
<%@ page import="domain.Config"%>
<%@ page import="domain.Draft"%>
<%@ page import="domain.Template"%>
<%@ page import="java.util.List"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<link rel="stylesheet"
		href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css">
	<link rel="stylesheet" href="/css/demo.css">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script
		src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
	<script src="/js/demo.js"></script>


	<title>編集jsp</title>


	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="CSS.css">
</head>

<body>
	<h1></h1>

	<table border="1" width="100%" height="100%" bgcolor="#F2F2F2"
		style="table-layout: fixed; position: relative;">
		<tbody>
			<tr>
			<td rowspan=1>
				<table border="1" width="100%" height="100%">
					<tr>
						<td rowspan=1 width="5px" height="5px">
							<%String templateID = bean.getDrafts().get(0).getTemplateID();%>
							<%String selected = "selected";%>
							<form method="POST" action="EditSelectTemplateServlet">
								<select name="templateID">
									<% for(Template template: bean.getTemplates()) {%>
										<option value="<%=template.getTEMPLATEID() %>" <% if(template.getTEMPLATEID().equals(templateID)){ %>selected<%} %>><%=template.getNAME() %></option>
									<% } %>
								</select>
								<input type="submit" value="変更"><br>
								</form>
							<form method="POST" action="EditPreviewServlet">
								<input type="submit" value="プレビュー">
						</td>
					</tr>
					<tr>
						<td rowspan=1 height="5px">日報：<%= bean.getDrafts().get(0).getTemplateName() %></td>
					</tr>
					<tr>
						<td rowspan=1 width="5px" height="5px">送信者：<%=bean.getName().getName() %>
						<input type = hidden name = "sender" value = "<%=bean.getName().getName() %>" >
						</td>
					</tr>
					<tr>
						<td rowspan=1 width="5px" height="5px">あて先：CSS_2016trainer@central-soft.co.jp
						<input type = hidden name = "destination" value = "CSS_2016trainer@central-soft.co.jp" >
						</td>
					</tr>
					<tr>
						<td rowspan=1 height="5px">件名：<%= bean.getTitle().getTitle() %>
						<input type = hidden name = "title" value = "<%= bean.getTitle().getTitle() %>" >
						</td>
					</tr>
					<tr>
						<td rowspan=1 width="100px" height="50px"><br>
							<table id="table" border="0" width="100%" height="100%">
								<tbody id = "list">
									<%int i = 0;%>
									<%String textareatag = "<textarea" ;%>
									<%String input1 = "<input type=\"";%>
									<%String input2 = "\" name=\"";%>
									<%String input3 = "\" value=\"";%>
									<%String input4 = "\" style=\"width: 100%; height: 50px; white-space: normal; wrap=hard; \">";%>
									<%String input5 = "\" >";%>
									<%List<Draft> drafts = bean.getDrafts();%>
									<%String temp = drafts.get(0).getTemplateID();%>

									<% for(Draft draft: drafts) {%>
									<% i++;%>
									<tr class="sortable-tr" height="10%">
									<td>
									<%-- アイテムテキスト --%>
									<%=draft.getItemName() %><br>
									<%=textareatag%> name="text<%=i %>" <%=input4 %><%=draft.getText() %>
									</textarea>
									</td>

									</tr>
									<%-- ドラフトID --%>
									<%=input1 %>hidden<%=input2 %>draftID<%=i %><%=input3 %><%=draft.getDraftID() %><%=input5 %>
									<% } %>
									<%--アイテム個数 --%>
									<%=input1 %>hidden<%=input2 %>cnt<%=input3 %><%=i %><%=input5 %>
									<%--テンプレID --%>
									<%=input1 %>hidden<%=input2 %>templateID<%=input3 %><%=temp %><%=input5 %>
								</form>
							</tbody>
							</table>
						</td>
					</tr>
				</table>
			</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
