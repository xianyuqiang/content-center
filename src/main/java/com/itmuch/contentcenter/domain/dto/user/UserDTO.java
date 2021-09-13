package com.itmuch.contentcenter.domain.dto.user;

import java.util.Date;

import lombok.Data;

/**
 * @program: content-center
 * @description:
 * @author: xianyuqiang
 * @create: 2021-09-03 17:00
 **/

@Data
public class UserDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 微信id
     */
    private String wxId;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 角色
     */
    private String roles;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 积分
     */
    private Integer bonus;

}
