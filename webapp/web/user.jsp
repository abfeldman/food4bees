<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>
      <c:choose>
        <c:when test="${empty id}">Create a New User</c:when>
        <c:otherwise>Modify an Existing User</c:otherwise>
      </c:choose>
    </title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <h1>
      <c:choose>
        <c:when test="${empty id}">Create a New User</c:when>
        <c:otherwise>Modify an Existing User</c:otherwise>
      </c:choose>
    </h1>

    <form action="${empty id ? 'add-user' : 'edit-user'}" method="post">
      <c:if test="${not empty id}"><input type="hidden" value="${id}" name="id" /></c:if>
      <table class="editor">
        <f4bi:groups />
        <c:if test="${not empty error}">
          <tr><td colspan="2"><span style="color: red;">${error}</span></td></tr>
        </c:if>
        <tr>
          <td><label for="name">Name<sup>*</sup>:</label></td>
          <td><input id="name" type="text" name="name" value="${name}" /></td>
        </tr>
        <tr>
          <td><label for="email">Email<sup>*</sup>:</label></td>
          <td><input id="email" type="text" name="email" value="${email}" /></td>
        </tr>
        <tr>
          <td><label for="password">Password<sup>*</sup>:</label></td>
          <td><input id="password" type="password" name="password" value="${password}" /></td>
        </tr>
        <tr>
          <td><label for="password">Confirm password<sup>*</sup>:</label></td>
          <td><input id="password" type="password" name="confirmation" value="${confirmation}" /></td>
        </tr>
        <tr>
          <td><label for="group">Group<sup>*</sup>:</label></td>
          <td>
            <select name="group">
              <c:forEach var="g" items="${groups}">
                <c:choose>
                  <c:when test="${empty group ? g.name == 'User' : g.id == group}">
                    <option value="${g.id}" selected>${g.name}</option>
                  </c:when>
                  <c:otherwise>
                    <option value="${g.id}">${g.name}</option>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="2"><sup>*</sup>Mandatory fields</td>
        </tr>
        <tr>
          <td colspan="2"><input type="submit" name="submit" /> <input type="reset" name="reset" /></td>
        </tr>
      </table>
    </form>
    <c:if test="${not empty id}">
      <p>Back to the <a href="manage_users.jsp">users list</a></p>
    </c:if>
    <p>Back to the <a href="index.jsp">main page</a></p>
  </body>
</html>
