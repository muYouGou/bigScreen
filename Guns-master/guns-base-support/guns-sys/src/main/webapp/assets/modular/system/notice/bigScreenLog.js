layui.use(['layer', 'form', 'table', 'admin', 'ax', 'func'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;

    /**
     * 系统管理--消息管理
     */
    var Notice = {
        tableId: "bigScreenLogTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    Notice.initColumn = function () {
        return [[
            {type: 'numbers'},
            {field: 'bigScreenLogId', align: "center", hide: true, sort: true, title: 'id'},
            {field: 'userName', align: "center", sort: true, title: '点击大屏用户'},
            {field: 'userId', align: "center", sort: true, title: '点击大屏用户ID'},
            {field: 'screenName', align: "center", sort: true, title: '点击大屏名称'},
            {field: 'createTime', align: "center", sort: true, title: '创建时间'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Notice.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(Notice.tableId, {
            where: queryData, page: {curr: 1}
        });
    };




    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Notice.tableId,
        url: Feng.ctxPath + '/bigScreenAdmin/listOfBigScreen',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Notice.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Notice.search();
    });


});
