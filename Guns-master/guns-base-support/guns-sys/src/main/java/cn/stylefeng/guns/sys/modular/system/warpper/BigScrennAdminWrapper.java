package cn.stylefeng.guns.sys.modular.system.warpper;

import cn.stylefeng.guns.sys.core.constant.factory.ConstantFactory;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏菜单管理
 * @date 2020-06-29 09:31
 */
public class BigScrennAdminWrapper extends BaseControllerWrapper {


    public BigScrennAdminWrapper(final List<Map<String, Object>> multi) {
        super(multi);
    }

    public BigScrennAdminWrapper(final Page<Map<String, Object>> page) {
        super(page);
    }

    @Override
    protected void wrapTheMap(final Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getMenuStatusName((String) map.get("status")));
    }

}
