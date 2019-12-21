package com.guli.ucenter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
public interface MemberMapper extends BaseMapper<Member> {

    Integer getRegisterCountByDay(String day);
}
