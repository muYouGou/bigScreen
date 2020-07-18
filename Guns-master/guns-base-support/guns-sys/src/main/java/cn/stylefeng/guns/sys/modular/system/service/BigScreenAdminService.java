package cn.stylefeng.guns.sys.modular.system.service;

import ch.qos.logback.classic.jmx.MBeanUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.base.pojo.node.ZTreeNode;
import cn.stylefeng.guns.sys.core.constant.factory.ConstantFactory;
import cn.stylefeng.guns.sys.core.constant.state.MenuStatus;
import cn.stylefeng.guns.sys.core.exception.enums.BizExceptionEnum;
import cn.stylefeng.guns.sys.modular.system.entity.BigScrennAdmin;
import cn.stylefeng.guns.sys.modular.system.entity.Menu;
import cn.stylefeng.guns.sys.modular.system.mapper.BigScreenAdminMapper;
import cn.stylefeng.guns.sys.modular.system.model.BigScrennAdminDto;
import cn.stylefeng.guns.sys.modular.system.model.MenuDto;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏管理用户
 * @date 2020-06-29 09:44
 */
@Service
public class BigScreenAdminService extends ServiceImpl<BigScreenAdminMapper, BigScrennAdmin> {

    @Resource
    private BigScreenAdminMapper bigScreenAdminMapper;


    /**
     * 首页
     * @param condition
     * @return
     */
    public List<Map<String, Object>> selectBigScreenTree(String condition) {
        List<Map<String, Object>> maps = this.baseMapper.selectMenuTree(condition);

        if (maps == null) {
            maps = new ArrayList<>();
        }

        if ( ToolUtil.isNotEmpty(condition) ) {
            if (maps.size() > 0) {
                for (Map<String, Object> menu : maps) {
                    menu.put("pcode", "0");
                }
            }
        }
        BigScrennAdmin b =  new BigScrennAdmin();
        b.setBigScreenId(-1L);
        b.setTitle("根节点");
        b.setCode("0");
        b.setPcode("-2");
        maps.add(BeanUtil.beanToMap(b));
        return maps;
    }

    /**
     * 添加大屏管理页面
     *
     * @author guope
     * @Date 2018/12/23 5:59 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(BigScrennAdminDto bigScrennAdminDto) {
        if ( ToolUtil.isOneEmpty( bigScrennAdminDto, bigScrennAdminDto.getCode(), bigScrennAdminDto.getTitle(),
                bigScrennAdminDto.getPid(),  bigScrennAdminDto.getUrl() ) ) {
            throw new RequestEmptyException();
        }



        //判断是否已经存在该编号
        String existedMenuName = ConstantFactory.me().getMenuNameByCode(bigScrennAdminDto.getCode());
        if (ToolUtil.isNotEmpty(existedMenuName)) {
            throw new ServiceException(BizExceptionEnum.EXISTED_THE_MENU);
        }

        //组装属性，设置父级菜单编号 getBigScreenNameByCode
        BigScrennAdmin resultMenu = this.menuSetPcode(bigScrennAdminDto);

        resultMenu.setStatus(MenuStatus.ENABLE.getCode());

        this.save(resultMenu);
    }



    /**
     * 更新大屏
     *
     * @author guope
     * @Date 2019/2/27 4:09 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(BigScrennAdminDto menuDto) {

        //如果菜单为空
        if ( ToolUtil.isOneEmpty( menuDto, menuDto.getCode(), menuDto.getTitle(),
                menuDto.getPid(),  menuDto.getUrl() ) ) {
            throw new RequestEmptyException();
        }


        //获取旧的菜单
        Long id = menuDto.getBigScreenId();

        final BigScrennAdmin menu = this.getById(id);

        if (menu == null) {
            throw new RequestEmptyException();
        }

        //设置父级菜单编号
        final BigScrennAdmin resultMenu = this.menuSetPcode(menuDto);

        //查找该节点的子集合,并修改相应的pcodes和level(因为修改菜单后,层级可能变化了)
        updateSubMenuLevels(menu, resultMenu);

        this.updateById(resultMenu);
    }

    /**
     * 更新所有子菜单的结构
     *
     * @param oldMenu 原来的菜单
     * @param newMenu 新菜单
     * @author guope
     * @Date 2019/2/27 4:25 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSubMenuLevels(BigScrennAdmin oldMenu, BigScrennAdmin newMenu) {

        final List<BigScrennAdmin> menus = bigScreenAdminMapper.getMenusLikePcodes(oldMenu.getCode());

          for (BigScrennAdmin menu : menus) {
            //更新pcode
            if (oldMenu.getCode().equals(menu.getPcode())) {
                menu.setPcode(newMenu.getCode());
            }

            //更新pcodes
            String oldPcodesPrefix = oldMenu.getPcodes() + "[" + oldMenu.getCode() + "],";
            String oldPcodesSuffix = menu.getPcodes().substring(oldPcodesPrefix.length());
            String menuPcodes = newMenu.getPcodes() + "[" + newMenu.getCode() + "]," + oldPcodesSuffix;
            menu.setPcodes(menuPcodes);

            //更新levels
            int level = StrUtil.count(menuPcodes, "[");
            menu.setLevels(level);


            this.updateById(menu);
        }

    }




    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    public List<ZTreeNode> menuTreeList() {
        return this.baseMapper.menuTreeList();
    }



    /**
     * 删除菜单包含所有子菜单
     *
     * @author stylefeng
     * @Date 2017/6/13 22:02
     */
    @Transactional
    public void delMenuContainSubMenus(Long menuId) {

        BigScrennAdmin menu = bigScreenAdminMapper.selectById(menuId);

        //删除当前菜单
        delMenu(menuId);

        //删除所有子菜单
        List<BigScrennAdmin> menus = bigScreenAdminMapper.getMenusLikePcodes(menu.getCode());

        for (BigScrennAdmin temp : menus) {
            delMenu(temp.getBigScreenId());
        }
    }

    /**
     * 删除菜单
     *
     * @author stylefeng
     * @Date 2017/5/5 22:20
     */
    @Transactional
    public void delMenu(Long menuId) {

        //删除菜单
        this.bigScreenAdminMapper.deleteById(menuId);


    }

    /**
     * 根据请求的父级菜单编号设置pcode和层级
     *
     * @author guope
     * @Date 2018/12/23 5:54 PM
     */
    public BigScrennAdmin menuSetPcode(BigScrennAdminDto bigScrennAdminDto) {

        BigScrennAdmin resultMenu = new BigScrennAdmin();
        BeanUtil.copyProperties(bigScrennAdminDto, resultMenu);

        if (ToolUtil.isEmpty(bigScrennAdminDto.getPid()) || bigScrennAdminDto.getPid().equals(0L)) {
            resultMenu.setPcode("0");
            resultMenu.setPcodes("[0],");
            resultMenu.setLevels(1);
        } else {
            Long pid = bigScrennAdminDto.getPid();
            final BigScrennAdmin byId = this.getById(pid);
            Integer pLevels = byId.getLevels();
            resultMenu.setPcode(byId.getCode());

            //如果编号和父编号一致会导致无限递归
            if (bigScrennAdminDto.getCode().equals(bigScrennAdminDto.getPcode())) {
                throw new ServiceException(BizExceptionEnum.MENU_PCODE_COINCIDENCE);
            }
            pLevels =  pLevels + 1;
            if (pLevels >= 3) {
                throw new ServiceException(400,"大屏层级不能超过2级");
            }
            resultMenu.setLevels(pLevels);
            resultMenu.setPcodes(byId.getPcodes() + "[" + byId.getCode() + "],");
        }

        return resultMenu;
    }




















}
