package com.guli.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.mapper.MemberMapper;
import com.guli.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author huaan
 * @since 2019-12-11
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    /**
     * 根据统计时间 注册人数
     * @param day
     * @return
     */
    @Override
    public Integer getRegisterCountByDay(String day) {

        //QueryWrapper<Member> wrapper = new QueryWrapper<>();

        //效率低
       // wrapper.like("gmt_create",day);
       // Integer count = baseMapper.selectCount(wrapper);
      // Integer count = baseMapper.selectCount(wrapper);/
        Integer count =baseMapper.getRegisterCountByDay(day);
        return count;
    }

    /**
     * 获取OpenID 微信登录使用
     * 并查询登录信息是否存在数据库中
     * @param openid
     * @return
     */
    @Override
    public Member getByOpenid(String openid) {

       
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        Member member = baseMapper.selectOne(wrapper);
        return member;
    }
}
