package cn.stylefeng.guns.sys.modular.system.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.sys.core.constant.factory.ConstantFactory;
import cn.stylefeng.guns.sys.core.constant.state.MenuStatus;
import cn.stylefeng.guns.sys.core.exception.enums.BizExceptionEnum;
import cn.stylefeng.guns.sys.modular.system.entity.BigScrennAdmin;
import cn.stylefeng.guns.sys.modular.system.entity.SysBigScreenLog;
import cn.stylefeng.guns.sys.modular.system.mapper.SysBigScreenLogMapper;
import cn.stylefeng.guns.sys.modular.system.model.BigScrennAdminDto;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 日志业务类
 * @date 2020-06-30 23:06
 */
@Service
public class SysBigScreenLogService extends ServiceImpl<SysBigScreenLogMapper, SysBigScreenLog> {

    @Resource
    private SysBigScreenLogMapper sysBigScreenLogMapper;

    /**
     * 添加大屏管理页面
     *
     * @author guope
     * @Date 2018/12/23 5:59 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSysBigScreen(SysBigScreenLog sysBigScreenLog) {
        this.save(sysBigScreenLog);
    }


    /**
     *
     *
     * @author guope
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }
}
