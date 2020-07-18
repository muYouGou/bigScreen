/**
 * 详情对话框
 */
var MenuInfoDlg = {
    data: {
        pid: "",
        pcodeName: ""
    }
};

layui.use(['layer', 'form' , 'upload' , 'admin', 'laydate', 'ax', 'iconPicker'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var upload = layui.upload;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var iconPicker = layui.iconPicker;



    //获取菜单信息
    var ajax = new $ax(Feng.ctxPath + "/bigScreenAdmin/getMenuInfo?menuId=" + Feng.getUrlParam("menuId"));
    var result = ajax.start();
    form.val('BigScrennEditForm', result.data);
    if (result.data.levels == 2) {
        $("#imgDiv").css("display","block");
        // $("#avatarPreview").attr("src","localhost:8011"+result.data.avatar);
        $("#avatarPreview").attr("src",result.data.avatar);
    }

    // 点击父级菜单
    $('#pcodeName').click(function () {
        var formName = encodeURIComponent("parent.MenuInfoDlg.data.pcodeName");
        var formId = encodeURIComponent("parent.MenuInfoDlg.data.pid");
        var treeUrl = encodeURIComponent("/bigScreenAdmin/selectMenuTreeList");

        layer.open({
            type: 2,
            title: '父级菜单',
            area: ['300px', '400px'],
            content: Feng.ctxPath + '/system/commonTree?formName=' + formName + "&formId=" + formId + "&treeUrl=" + treeUrl,
            end: function () {
                $("#pid").val(MenuInfoDlg.data.pid);
                $("#pcodeName").val(MenuInfoDlg.data.pcodeName);
            }
        });
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/bigScreenAdmin/edit", function (data) {
            Feng.success("修改成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();

        }, function (data) {
            Feng.error("修改失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        //添加 return false 可成功跳转页面
        return false;
    });

    upload.render({
        elem: '#imgHead'
        , url: Feng.ctxPath + '/bigScreenAdmin/upload'
        , before: function (obj) {
            obj.preview(function (index, file, result) {
                // alert(result.fi)
                $('#avatarPreview').attr('src', result);
            });
        }
        , done: function (res) {
            $("#avatar").val(res.data.fileId);

        }
        , error: function () {
            Feng.error("上传头像失败！");
        }
    });

});