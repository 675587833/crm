<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link href="../../jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css"
          rel="stylesheet"/>
    <link href="../../jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"
          type="text/css" rel="stylesheet"/>
    <link href="../../jquery/bs_pagination/jquery.bs_pagination.min.css"
          type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="../../jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript"
            src="../../jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="../../jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="../../jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript"
            src="../../jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript"
            src="../../jquery/bs_pagination/en.js"></script>
    <script type="text/javascript">

        $(function () {

            $(".time").datetimepicker({
                minView: "month",
                language: "zh-CN",
                format: "yyyy-mm-dd",
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            })

            pageList(1, 5);

            $("#addBtn").click(function () {

                $.ajax({
                    type: "get",
                    url: "getCreateOwner.do",
                    data: {},
                    success: function (data) {
                        $("#createOwner").empty();
                        $.each(data, function (i, n) {
                            $("#createOwner").append("<option value='" + n.id + "'>" + n.name + "</option>");
                        })
                        $("#createOwner").val("${user.id}");
                    },
                    dataType: "json"
                })

                $("#createActivity").modal("show");
            })

            $("#saveBtn").click(function () {

                $.ajax({
                    type: "post",
                    url: "save.do",
                    data: {
                        owner: $.trim($("#createOwner").val()),
                        name: $.trim($("#createName").val()),
                        startDate: $.trim($("#createStartTime").val()),
                        endDate: $.trim($("#createEndTime").val()),
                        cost: $.trim($("#createCost").val()),
                        description: $.trim($("#createDescription").val())
                    },
                    success: function (data) {
                        if (data.success) {
                            pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#createActivity").modal("hide");
                            $("#createActivityForm")[0].reset();
                        } else {
                            alert("添加市场活动失败");
                        }

                    },
                    dataType: "json"
                })
            })

            $("#searchBtn").click(function () {

                $("#hideName").val($("#searchName").val());
                $("#hideOwner").val($("#searchOwner").val());
                $("#hideStartDate").val($("#searchStartDate").val());
                $("#hideEndDate").val($("#searchEndDate").val());
                pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

            })

            $("#allCheckbox").click(function () {

                $("input[name=comCheckbox]").prop("checked", this.checked);

            })

            $("#activityBody").on("click", $("input[name=comCheckbox]"), function () {

                $("#allCheckbox").prop("checked", $("input[name=comCheckbox]").length == $("input[name=comCheckbox]:checked").length)

            })

            $("#deleteBtn").click(function () {

                var $xz = $("input[name=comCheckbox]:checked");
                if ($xz.length == 0) {
                    alert("请选择需要删除的记录")
                }else{
                    if (window.confirm("确定删除所选择的记录吗")) {
                        var param = "";
                        for (var i = 0; i < $xz.length; i++) {
                            param += "&id=" + $xz[i].value;
                        }
                        param = param.substring(1);

                        $.ajax({
                            type: "post",
                            url: "delete.do",
                            data: param,
                            success: function (data) {
                                if (data.success) {
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                                } else {
                                    alert("删除市场活动失败");
                                }
                            },
                            dataType: "json"
                        })
                    }


                }
            })

            $("#editBtn").click(function () {
                var $xz = $("input[name=comCheckbox]:checked");
                if ($xz.length == 0) {
                    alert("请选择一条需要修改的市场活动")
                } else if ($xz.length > 1) {
                    alert("只能选择一条需要修改的市场活动")
                } else {
                    $.ajax({
                        type: "get",
                        url: "getUserListAndActivity.do",
                        data: {
                            id: $xz.val(),
                        },
                        success: function (data) {
                            $("#editOwner").empty();
                            $.each(data.userList, function (i, n) {
                                $("#editOwner").append("<option value='" + n.id + "'>" + n.name + "</option>");
                            })
                            $("#editId").val(data.activity.id);
                            $("#editOwner").val(data.activity.owner);
                            $("#editName").val(data.activity.name);
                            $("#editStartDate").val(data.activity.startDate);
                            $("#editEndDate").val(data.activity.endDate);
                            $("#editCost").val(data.activity.cost);
                            $("#editDescription").val(data.activity.description);
                            $("#editActivityModal").modal("show");
                        },
                        dataType: "json"
                    })
                }
            })

            $("#updateBtn").click(function () {

                $.ajax({
                    type: "post",
                    url: "update.do",
                    data: {
                        "id": $("#editId").val(),
                        "owner": $("#editOwner").val(),
                        "name": $("#editName").val(),
                        "startDate": $("#editStartDate").val(),
                        "endDate": $("#editEndDate").val(),
                        "cost": $("#editCost").val(),
                        "description": $("#editDescription").val()
                    },
                    success: function (data) {
                        if (data.success) {
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                , $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            $("#editActivityModal").modal("hide");
                        } else {
                            alert("修改市场活动失败")
                        }
                    },
                    dataType: "json"
                })
            })


        })

        function pageList(pageNo, pageSize) {

            $("#allCheckbox").prop("checked", false);

            $("#searchName").val($("#hideName").val());
            $("#searchOwner").val($("#hideOwner").val());
            $("#searchStartDate").val($("#hideStartDate").val());
            $("#searchEndDate").val($("#hideEndDate").val());

            $.ajax({
                type: "get",
                url: "pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#searchName").val()),
                    "owner": $.trim($("#searchOwner").val()),
                    "startDate": $.trim($("#searchStartDate").val()),
                    "endDate": $.trim($("#searchEndDate").val())
                },
                success: function (data) {
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                        html += '<tr class="active">';
                        html += '    <td><input type="checkbox"  name="comCheckbox" value="' + n.id + '"/></td>';
                        html += '    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.do?id=' + n.id + '\';">' + n.name + '</a></td>';
                        html += '    <td>' + n.owner + '</td>';
                        html += '    <td>' + n.startDate + '</td>';
                        html += '    <td>' + n.endDate + '</td>';
                        html += '</tr>';
                    })
                    $("#activityBody").html(html);

                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                    $("#activityPage").bs_pagination({
                        currentPage: pageNo,
                        rowsPerPage: pageSize,
                        maxRowsPerPage: 10,
                        totalPages: totalPages,
                        totalRows: data.total,
                        visiblePageLinks: 3,
                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    })
                },
                dataType: "json"
            })

        }

    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="createActivityForm" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="createOwner"></select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="createName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="createStartTime">
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="createEndTime">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="createCost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="createDescription"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <span><input type="hidden" id="editId"></span>

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="editOwner">
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="editName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="editStartDate">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="editEndDate">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="editCost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="editDescription"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="searchName">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="searchOwner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon ">开始日期</div>
                        <input class="form-control time" type="text" id="searchStartDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="searchEndDate">
                    </div>
                </div>
                <span>
                    <input type="hidden" id="hideName">
                    <input type="hidden" id="hideOwner">
                    <input type="hidden" id="hideStartDate">
                    <input type="hidden" id="hideEndDate">
                </span>
                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="allCheckbox"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">

                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage">

            </div>
        </div>

    </div>

</div>
</body>
</html>