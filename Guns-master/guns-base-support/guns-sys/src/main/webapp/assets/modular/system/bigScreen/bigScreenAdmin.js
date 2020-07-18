layui.use(['layer', 'form', 'ztree', 'laydate', 'admin', 'ax', 'table', 'treetable', 'func'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;
    var table = layui.table;
    var treetable = layui.treetable;
    var func = layui.func;

    /**
     * 大屏管理--大屏管理
     */
    var Menu = {
        tableId: "bigSceenAdminTable",    //表格id
        condition: {
            bigScreenId: "",
            menuName: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Menu.initColumn = function () {
        return [[
            {type: 'numbers'},
            {field: 'bigScreenId', hide: true, sort: true, title: 'id'},
            {field: 'title', align: "left", sort: true, title: '大屏名称', minWidth: 240},
            {field: 'assistantTitle', align: "left", sort: true, title: '大屏副名称', minWidth: 240},
            {field: 'code', align: "center", sort: true, title: '大屏编号', minWidth: 120},
            {field: 'pcode', align: "center", sort: true, title: '大屏父编号'},
            {field: 'url', align: "center", sort: true, title: '请求地址'},
            {field: 'sort', align: "center", sort: true, title: '排序'},
            {field: 'levels', align: "center", sort: true, title: '层级'},
            {field: 'statusName', align: "center", sort: true, title: '状态'},
            {field: 'avatar', title: '上传图片', templet: '<div><img src="{{ d.avatar }}" style="width:30px; height:30px;"></div>'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


    /**
     * 初始化表格
     */
    Menu.initTable = function (menuId, data) {
        return treetable.render({
            elem: '#' + menuId,
            url: Feng.ctxPath + '/bigScreenAdmin/listTree',
            where: data,
            page: false,
            height: "full-98",
            cellMinWidth: 100,
            cols: Menu.initColumn(),
            treeColIndex: 2,
            treeSpid: "0",
            treeIdName: 'code',
            treePidName: 'pcode'
        });
    };

    // 渲染表格, 展开和这点所有
    var tableResult = Menu.initTable(Menu.tableId);
    $('#expandAll').click(function () {
        treetable.expandAll('#' + Menu.tableId);
    });
    $('#foldAll').click(function () {
        treetable.foldAll('#' + Menu.tableId);
    });



    /**
     * 点击菜单树时
     */
    Menu.onClickMenu = function (e, treeId, treeNode) {
        Menu.condition.menuId = treeNode.id;
        Menu.search();
    };

    /**
     * 点击查询按钮
     */
    Menu.search = function () {
        var queryData = {};
        queryData['menuName'] = $("#menuName").val();
        Menu.initTable(Menu.tableId, queryData);
    };

    /**
     * 弹出添加菜单对话框
     */
    Menu.openAddMenu = function () {
        func.open({
            height: 720,
            title: '添加大屏',
            content: Feng.ctxPath + '/bigScreenAdmin/bigScreenAdmin_add',
            tableId: Menu.tableId,
            endCallback: function () {
                Menu.initTable(Menu.tableId);
            }
        });
    };



    /**
     * 点击编辑菜单按钮时
     *
     * @param data 点击按钮时候的行数据
     */
    Menu.onEditMenu = function (data) {

        if(data.levels == 1) {
            Feng.error("基础配置数据不允许修改!");
            return false;
        }
        func.open({
            height: 720,
            title: '修改大屏',
            content: Feng.ctxPath + "/bigScreenAdmin/menu_edit?menuId=" + data.bigScreenId,
            tableId: Menu.tableId,
            endCallback: function () {
                Menu.initTable(Menu.tableId);
            }
        });
    };

    /**
     * 点击删除菜单按钮
     *
     * @param data 点击按钮时候的行数据
     */
    Menu.onDeleteMenu = function (data) {

        if(data.levels == 1) {
            Feng.error("基础配置数据不允许删除!");
            return false;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bigScreenAdmin/remove", function () {
                Feng.success("删除成功!");
                Menu.condition.menuId = "";
                Menu.initTable(Menu.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("menuId", data.bigScreenId);
            ajax.start();
        };
        Feng.confirm("是否删除菜单" + data.title + "?", operation);
    };



    //初始化左侧部门树
    var ztree = new $ZTree("menuTree", "/menu/selectMenuTreeList");
    ztree.bindOnClick(Menu.onClickMenu);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Menu.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Menu.openAddMenu();
    });

    $('#btnOpen').click(function () {
        window.open(Feng.ctxPath + '/bigScreenAdmin/k9nf0bV8M0n7f');

    });

    // 工具条点击事件
    table.on('tool(' + Menu.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Menu.onEditMenu(data);
        } else if (layEvent === 'delete') {
            Menu.onDeleteMenu(data);
        } else if (layEvent === 'roleAssign') {
            Menu.roleAssign(data);
        } else if (layEvent === 'reset') {
            Menu.resetPassword(data);
        }
    });

});
