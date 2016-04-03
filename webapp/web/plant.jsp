<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>
      <c:choose>
        <c:when test="${empty id}">Create a New Plant</c:when>
        <c:otherwise>Modify an Existing Plant</c:otherwise>
      </c:choose>
    </title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <h1>
      <c:choose>
        <c:when test="${empty id}">Create a New Plant</c:when>
        <c:otherwise>Modify an Existing Plant</c:otherwise>
      </c:choose>
    </h1>

    <form action="${empty id ? 'add-plant' : 'edit-plant'}" method="post">
      <c:if test="${not empty id}"><input type="hidden" value="${id}" name="id" /></c:if>
      <table>
        <c:if test="${not empty error}"><tr><td colspan="2"><span style="color: red;">${error}</span></td></tr></c:if>
        <tr>
          <td><label for="commonName">Common name:</label></td>
          <td><input id="commonName" name="commonName" type="text" value="${commonName}" /></td>
        </tr>
        <tr>
          <td><label for="scientificName">Scientific name<sup>*</sup>:</label></td>
          <td><input id="scientificName" name="scientificName" type="text" value="${scientificName}" /></td>
        </tr>
        <tr>
          <td><label for="description">Description:</label></td>
          <td><textarea id="description" name="description" cols="80" rows="20">${description}</textarea></td>
        </tr>
        <tr>
          <td><label for="url">Wikipedia URL:</label></td>
          <td><input id="url" type="text" name="url" value="${url}" /></td>
        </tr>
        <tr>
          <td><label for="color">Color:</label></td>
          <td><input id="color" type="text" name="color" value="${color}" size="6" maxlength="6" /> RGB from 000000 (black) to FFFFFF (white)</td>
        </tr>
        <tr>
          <td><label for="height">Height:</label></td>
          <td><input id="height" type="text" name="height" value="${height}" /> in meters</td>
        </tr>
        <tr>
          <td><label for="nectar">Nectar index:</label></td>
          <td><input id="nectar" type="text" name="nectar" value="${nectar}" /> from 0.0 to 6.0</td>
        </tr>
        <tr>
          <td><label for="pollen">Pollen index:</label></td>
          <td><input id="pollen" type="text" name="pollen" value="${pollen}" /> from 0.0 to 5.0</td>
        </tr>
        <tr>
          <td><label for="start">Start flowering:</label></td>
          <td><input id="start" type="text" name="start" value="${start}" size="5" maxlength="5" /> DD-MM from 1-1 to 31-12</td>
        </tr>
        <tr>
          <td><label for="end">End flowering:</label></td>
          <td><input id="end" type="text" name="end" value="${end}" size="5" maxlength="5" /> DD-MM from 1-1 to 31-12</td>
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
      <p>Back to the <a href="manage_plants.jsp">plants list</a></p>
    </c:if>
    <p>Back to the <a href="index.jsp">main page</a></p>
  </body>
</html>
