<html>
<title>student</title>
<head>
    <meta charset="utf-8">
</head>
<body>
学生信息<br>
姓名;&nbsp;&nbsp; ${student.name}<br>
ID:&nbsp;&nbsp; ${student.id}<br>
年龄:&nbsp;&nbsp; ${student.age}<br>
地址:&nbsp;&nbsp; ${student.addr}<br>
当前日期:${data?string("yyyy年MM月dd日 HH:mm:ss")}<br/>
null值处理: ${val!"空值"}
判断是否为null<br/>
<#if va??>
    val非空
<#else >
    val空
</#if>
<br/>
引用模版测试<br/>
<#include "hello.ftl">
<hr/>
<table border="1">
    <tr>
        <th>序号</th>
        <th>姓名</th>
        <th>ID</th>
        <th>年龄</th>
        <th>地址</th>
    </tr>
    <#list stuList as stu >
        <#if stu_index %2==0>
            <tr bgcolor="#90ee90">
        <#else>
            <tr bgcolor="#00bfff">
        </#if>
        <td>${stu_index}</td>
        <td>${stu.name}</td>
        <td>${stu.id}</td>
        <td>${stu.age}</td>
        <td>${stu.addr}</td>
        </tr>
    </#list>

</table>
</body>
</html>