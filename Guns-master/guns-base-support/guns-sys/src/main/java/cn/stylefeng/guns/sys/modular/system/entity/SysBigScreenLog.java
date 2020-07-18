package cn.stylefeng.guns.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏管理
 * @date 2020-06-29 09:21
 */
@TableName("sys_big_screen_log")
@Data
public class SysBigScreenLog implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "big_screen_log_id", type = IdType.ID_WORKER)
    private Long bigScreenLogId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 大屏名称
     */
    @TableField("screen_name")
    private String screenName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
