<%--
  Created by IntelliJ IDEA.
  User: Flare576
  Date: 1/19/2016
  Time: 1:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="countries">
<head>
    <title>Nations of the World</title>
    <base href="<%=request.getContextPath()%>/">
    <link rel="stylesheet" type="text/css" href="css/lib/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="css/lib/bootstrap-theme.min.css" />
    <script type="text/javascript" src="js/lib/angular.min.js"></script>
    <script type="text/javascript" src="js/lib/angular-animate.min.js"></script>
    <script type="text/javascript" src="js/lib/ui-bootstrap-tpls-1.1.0.min.js"></script>

    <link rel="stylesheet" type="text/css" href="css/index.css" />
    <script type="text/javascript" src="js/app.js"></script>
    <script type="text/javascript" src="js/countryQuery.js"></script>
    <script type="text/javascript" src="js/borders.js"></script>
    <script type="text/javascript" src="js/timezones.js"></script>
</head>
<body>
    <h1>Nations of the World</h1>
    <h2>Brought to you by <a href="https://www.youtube.com/watch?v=x88Z5txBc7w"><span style="text-decoration: line-through;">Yakko Warner</span></a> Jeremy Scherer</h2>
    <stats-tabs></stats-tabs>
</body>
</html>
