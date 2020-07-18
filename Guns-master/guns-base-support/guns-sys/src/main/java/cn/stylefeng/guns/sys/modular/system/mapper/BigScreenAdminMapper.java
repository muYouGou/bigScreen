package cn.stylefeng.guns.sys.modular.system.mapper;

import cn.stylefeng.guns.base.pojo.node.ZTreeNode;
import cn.stylefeng.guns.sys.modular.system.entity.BigScrennAdmin;
import cn.stylefeng.guns.sys.modular.system.entity.Menu;
import cn.stylefeng.guns.sys.modular.system.model.BigScrennAdminDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏管理mapper
 * @date 2020-06-29 09:47
 */
public interface BigScreenAdminMapper extends BaseMapper<BigScrennAdmin> {



    /**
     * 查询大屏树形列表
     *
     * @author guope
     * @Date 2019/2/23 22:03
     */
    List<Map<String, Object>> selectMenuTree(@Param("condition") String condition);

    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<ZTreeNode> menuTreeList();


    /**
     * 获取pcodes like某个code的菜单列表
     *
     * @author guope
     * @Date 2019/3/31 15:51
     */
    List<BigScrennAdmin> getMenusLikePcodes(@Param("code") String code);

}
