<%--&lt;%&ndash;--%>
<%--  Created by IntelliJ IDEA.--%>
<%--  User: sarker--%>
<%--  Date: 9/5/19--%>
<%--  Time: 7:08 PM--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%--&ndash;%&gt;--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>


<%--<head>--%>
<%--    <link rel="stylesheet" href="../css/bootstrap.min.css">--%>
<%--    <script src="../js/bootstrap.min.js"></script>--%>
<%--</head>--%>
<%--<body>--%>


<%--<% out.println("ip: " + request.getRemoteAddr()); %>--%>

<%--&lt;%&ndash;<div class="container">&ndash;%&gt;--%>
<%--&lt;%&ndash;    <h2>Employees</h2>&ndash;%&gt;--%>
<%--&lt;%&ndash;    <!--Search Form -->&ndash;%&gt;--%>
<%--&lt;%&ndash;    <form action="/employee" method="get" id="seachEmployeeForm" role="form" >&ndash;%&gt;--%>
<%--&lt;%&ndash;        <input type="hidden" id="searchAction" name="searchAction" value="searchByName"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;        <div class="form-group col-xs-5">&ndash;%&gt;--%>
<%--&lt;%&ndash;            <input type="text" name="employeeName" id="employeeName" class="form-control" required="true"&ndash;%&gt;--%>
<%--&lt;%&ndash;                   placeholder="Type the Name or Last Name of the employee"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </div>&ndash;%&gt;--%>
<%--&lt;%&ndash;        <button type="submit" class="btn btn-info">&ndash;%&gt;--%>
<%--&lt;%&ndash;            <span class="glyphicon glyphicon-search"></span> Search&ndash;%&gt;--%>
<%--&lt;%&ndash;        </button>&ndash;%&gt;--%>
<%--&lt;%&ndash;        <br></br>&ndash;%&gt;--%>
<%--&lt;%&ndash;        <br></br>&ndash;%&gt;--%>
<%--&lt;%&ndash;    </form>&ndash;%&gt;--%>


<%--&lt;%&ndash;    <form action="/employee" method="post" id="employeeForm" role="form" >&ndash;%&gt;--%>
<%--&lt;%&ndash;        <c:choose>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <c:when test="${not empty employeeList}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                <table  class="table table-striped">&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <thead>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>#</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>Name</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>Last name</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>Birth date</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>Role</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>Department</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <td>E-mail</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    </tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    </thead>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <c:forEach var="employee" items="${employeeList}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <c:set var="classSucess" value=""/>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <c:if test ="${idEmployee == employee.id}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <c:set var="classSucess" value="info"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        </c:if>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        <tr class="${classSucess}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.id}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.name}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.lastName}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.birthDate}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.role}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.department}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <td>${employee.email}</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        </tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    </c:forEach>&ndash;%&gt;--%>
<%--&lt;%&ndash;                </table>&ndash;%&gt;--%>
<%--&lt;%&ndash;            </c:when>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <c:otherwise>&ndash;%&gt;--%>
<%--&lt;%&ndash;                <br>  </br>&ndash;%&gt;--%>
<%--&lt;%&ndash;                <div class="alert alert-info">&ndash;%&gt;--%>
<%--&lt;%&ndash;                    No people found matching your search criteria&ndash;%&gt;--%>
<%--&lt;%&ndash;                </div>&ndash;%&gt;--%>
<%--&lt;%&ndash;            </c:otherwise>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </c:choose>&ndash;%&gt;--%>
<%--&lt;%&ndash;    </form>&ndash;%&gt;--%>

<%--&lt;%&ndash;</div>&ndash;%&gt;--%>

<%--</body>--%>
<%--</html>--%>
