<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
  <c:choose>
    <c:when test="${empty id}">Create a New F4BI Plant image</c:when>
    <c:otherwise>Modify an Existing F4BI Plant image</c:otherwise>
  </c:choose>
</title>
</head>
<body>
  <h1>
    <c:choose>
      <c:when test="${empty id}">Create a New F4BI Plant image</c:when>
      <c:otherwise>Modify an Existing F4BI Plant image</c:otherwise>
    </c:choose>
  </h1>
        
  <form action="${empty id ? 'add-plant-image' : 'edit-plant-image'}" method="post" enctype="multipart/form-data">
  <c:if test="${not empty id}">
    <input type="hidden" name="id" value="${id}" />
  </c:if>
  <c:set var="plantid" value="${not empty plantid ? plantid : param.plantid}" />
  <c:if test="${not empty plantid}">
    <input type="hidden" name="plantid" value="${plantid}" />
  </c:if>
        
  <table>
    <c:if test="${not empty error}"><tr><td colspan="2"><span style="color: red;">${error}</span></td></tr></c:if>
    <c:if test="${not empty size}">
      <tr>
        <td>Database image size:</td>
        <td><c:out value="${size} bytes" /></td>
      </tr>
    </c:if>
    <tr>
      <td><label for="file">Image<sup>*</sup>:</label></td>
      <td><input id="file" type="file" name="file" /></td>
    </tr>
    <tr>
      <td><label for="caption">Image caption<sup>*</sup>:</label></td>
      <td><input id="caption" type="text" name="caption" value="${caption}" /></td>
    </tr>
    <tr>
      <td colspan="2"><sup>*</sup>Mandatory field</td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" name="submit" /> <input type="reset" name="reset" /></td>
    </tr>
  </table>
  </form>
  <c:if test="${not empty id}">
    Back to the <a href="manage_plant_images.jsp?plantid=${plantid}">plant images list</a><br/>
  </c:if>
  Back to the <a href="manage_plants.jsp">plants list</a><br/>
  Back to the <a href="index.jsp">administrative console</a>
</body>
</html>
