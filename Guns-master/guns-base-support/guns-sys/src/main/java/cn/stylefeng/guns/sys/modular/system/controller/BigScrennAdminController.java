package cn.stylefeng.guns.sys.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.base.auth.annotion.Permission;
import cn.stylefeng.guns.base.auth.context.LoginContextHolder;
import cn.stylefeng.guns.base.auth.model.LoginUser;
import cn.stylefeng.guns.base.pojo.node.ZTreeNode;
import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.sys.core.constant.Const;
import cn.stylefeng.guns.sys.core.constant.factory.ConstantFactory;
import cn.stylefeng.guns.sys.core.exception.enums.BizExceptionEnum;
import cn.stylefeng.guns.sys.core.log.LogObjectHolder;
import cn.stylefeng.guns.sys.modular.system.entity.BigScrennAdmin;
import cn.stylefeng.guns.sys.modular.system.entity.Menu;
import cn.stylefeng.guns.sys.modular.system.entity.SysBigScreenLog;
import cn.stylefeng.guns.sys.modular.system.model.BigScrennAdminDto;
import cn.stylefeng.guns.sys.modular.system.model.MenuDto;
import cn.stylefeng.guns.sys.modular.system.model.UploadResult;
import cn.stylefeng.guns.sys.modular.system.service.BigScreenAdminService;
import cn.stylefeng.guns.sys.modular.system.service.FileInfoService;
import cn.stylefeng.guns.sys.modular.system.service.SysBigScreenLogService;
import cn.stylefeng.guns.sys.modular.system.warpper.BigScrennAdminWrapper;
import cn.stylefeng.guns.sys.modular.system.warpper.BigScrennLogWrapper;
import cn.stylefeng.guns.sys.modular.system.warpper.NoticeWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏维护管理页面
 * @date 2020-06-29 09:40
 */
@Controller
@RequestMapping("/bigScreenAdmin")
public class BigScrennAdminController extends BaseController {

    private static String PREFIX = "/modular/system/bigScreen/";
    private static String PREFIXONE = "/modular/system/bigIndex/";

    @Autowired
    private BigScreenAdminService bigScreenAdminService;

    @Autowired
    private SysBigScreenLogService sysBigScreenLogService;


    @Autowired
    private FileInfoService fileInfoService;


    /**
     * 跳转到大屏维护管理列表列表页面
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bigScreenAdmin.html";
    }




    /**
     * 获取大屏管理列表（s树形）
     *
     * @author guope
     * @Date 2019年2月23日22:01:47
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/listTree")
    @ResponseBody
    public Object listTree(@RequestParam(required = false) String menuName) {
        List<Map<String, Object>> menus = this.bigScreenAdminService.selectBigScreenTree(menuName);
        List<Map<String, Object>> menusWrap = new BigScrennAdminWrapper(menus).wrap();

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menusWrap);
        return result;
    }


    /**
     * 新增菜单
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseData add(BigScrennAdminDto menu) {
        this.bigScreenAdminService.addMenu(menu);
        return SUCCESS_TIP;
    }


    /**
     * 跳转到菜单列表列表页面
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/bigScreenAdmin_add")
    public String menuAdd() {
        return PREFIX + "bigScreenAdmin_add.html";
    }

    @RequestMapping(value = "/k9nf0bV8M0n7f")
    public String menuOpen() {
        return PREFIXONE + "index.html";
    }



    /**
     * 修该菜单
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseData edit(BigScrennAdminDto menu) {

        //如果修改了编号，则该菜单的子菜单也要修改对应编号
        this.bigScreenAdminService.updateMenu(menu);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到菜单详情列表页面
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/menu_edit")
    public String menuEdit(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //获取菜单当前信息，记录日志用
        BigScrennAdmin menu = this.bigScreenAdminService.getById(menuId);
        LogObjectHolder.me().set(menu);

        return PREFIX + "bigScreenAdmin_edit.html";
    }



    /**
     * 获取菜单信息
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/getMenuInfo")
    @ResponseBody
    public ResponseData getMenuInfo(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        BigScrennAdmin menu = this.bigScreenAdminService.getById(menuId);

        BigScrennAdminDto menuDto = new BigScrennAdminDto();
        BeanUtil.copyProperties(menu, menuDto);

        //设置pid和父级名称
        menuDto.setPid(ConstantFactory.me().getBigScreenIdByCode(menuDto.getPcode()));
        menuDto.setPcodeName(ConstantFactory.me().getBigScreenNameByCode(menuDto.getPcode()));
        return ResponseData.success(menuDto);
    }





    /**
     * 删除菜单
     *
     * @author guope
     * @Date 2018/12/23 5:53 PM
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/remove")
    @ResponseBody
    public ResponseData remove(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        this.bigScreenAdminService.delMenuContainSubMenus(menuId);

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     *
     * @author guope
     * @Date 2018/12/23 5:54 PM
     */
    @RequestMapping(value = "/selectMenuTreeList")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = this.bigScreenAdminService.menuTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    @RequestMapping(value = "/getAllBigScreen")
    @ResponseBody
    public  Object getAllBigScreenInfo(){
        List<Map<String, Object>> menus = this.bigScreenAdminService.selectBigScreenTree(null);
        List<Map<String, Object>> menusWrap = new BigScrennAdminWrapper(menus).wrap();

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menusWrap);
        return result;
    }

    /**
     * 记录日志
     * @param title
     */
    @RequestMapping(value = "/clickInfoLog")
    @ResponseBody
    public void clickInfoLog(@RequestParam String title){
        final LoginUser user = LoginContextHolder.getContext().getUser();
        SysBigScreenLog s = new SysBigScreenLog();
        s.setScreenName(title);
        s.setUserId(user.getId());
        s.setUserName(user.getName());
        sysBigScreenLogService.save(s);

    }



    /**
     * layui上传组件 通用文件上传接口
     *
     * @author guope
     * @Date 2019-2-23 10:48:29
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public ResponseData layuiUpload(@RequestPart("file") MultipartFile file) {

        UploadResult uploadResult = this.fileInfoService.uploadFileByBigScreen(file);
        String fileId = uploadResult.getFileSavePath();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fileId", fileId);

        return ResponseData.success(0, "上传成功", map);
    }

    @RequestMapping(value = "returnBigScreen")
    public String indexOfBigScreenLog() {
        return "/modular/system/notice/bigScreenLog.html";
    }

    @RequestMapping(value = "/listOfBigScreen")
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.sysBigScreenLogService.list(condition);
        Page<Map<String, Object>> wrap = new BigScrennLogWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }
}
