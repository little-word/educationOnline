package com.guli.ucenter.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author GPX
 * @date 2019/12/19 11:28
 */

@Data
@ApiModel(value="用户基本信息")
public class LoginInfoVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
