package com.guli.ucenter.service;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
public interface MemberService extends IService<Member> {

    /**
     * 统计时间
     * @param day
     * @return
     */
    Integer getRegisterCountByDay(String day);

    /**
     * 获取openid
     * @param openid
     * @return
     */
    Member getByOpenid(String openid);
}
