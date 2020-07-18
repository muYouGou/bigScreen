package cn.stylefeng.guns.sys.modular.system.mapper;

import cn.stylefeng.guns.sys.modular.system.entity.BigScrennAdmin;
import cn.stylefeng.guns.sys.modular.system.entity.SysBigScreenLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏日志mapper
 * @date 2020-06-30 23:07
 */
public interface SysBigScreenLogMapper extends BaseMapper<SysBigScreenLog> {


    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition);


}
