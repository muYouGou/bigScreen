package cn.stylefeng.guns.sys.modular.system.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guope
 * @version V1.0
 * @description: 大屏维护表
 * @date 2020-06-29 11:26
 */
@Data
public class BigScrennAdminDto implements Serializable {

    /**
     * 主键id
     */
    private Long bigScreenId;

    /**
     * 菜单编号
     */
    private String code;

    /**
     * 菜单父编号
     */
    private String pcode;

    /**
     * 菜单父级id
     */
    private Long pid;

    /**
     * 当前菜单的所有父菜单编号
     */
    private String pcodes;

    /**
     * 菜单父级名称
     */
    private String pcodeName;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单副名称
     */
    private String assistantTitle;

    /**
     * url地址
     */
    private String url;

    /**
     * 菜单排序号
     */
    private Integer sort;

    /**
     * 菜单层级
     */
    private Integer levels;

    /**
     * 是否是2级标题(字典)
     */
    private String menuFlag;

    /**
     * 备注
     */
    private String description;


    /**
     * 头像
     */
    private String avatar;

}
