<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Manage Plant Images</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="edit_delete">
      <h1>Manage Plant Images</h1>
      <table>
        <c:set var="plantid" value="${not empty plantid ? plantid : param.plantid}" />
        <f4bi:plant_images plant="${plantid}"/>
        <c:if test="${plant_images == null}">
          <tr><td colspan="4">Internal error</td></tr>
        </c:if>
        <c:if test="${plant_images != null && empty plant_images}">
          <tr><td colspan="4">No plants images</td></tr>
        </c:if>
        <c:if test="${not empty plant_images}">
          <tr><th>Size</th><th>Caption</th><th></th><th></th></tr>
          <c:forEach var="plant_image" items="${plant_images}">
            <tr>
              <td>${plant_image.size} bytes</td>
              <td>${plant_image.caption}</td>
              <td><a href="edit-plant-image?plantid=${plantid}&id=${plant_image.id}">Edit</a></td>
              <td><a href="delete-plant-image?plantid=${plantid}&id=${plant_image.id}">Delete</a></td>
            </tr>
          </c:forEach>
        </c:if>
      </table>
      <p>Back to the <a href="manage_plants.jsp">plants list</a></p>
      <p>Back to the <a href="index.jsp">main page</a></p>
    </div>
  </body>
</html>
