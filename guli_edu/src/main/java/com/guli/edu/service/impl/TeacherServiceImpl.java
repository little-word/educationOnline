package com.guli.edu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Teacher;
import com.guli.edu.entity.vo.TeacherQuery;
import com.guli.edu.mapper.TeacherMapper;
import com.guli.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Override
    public Boolean deleteById(String id) {
        int delete = baseMapper.deleteById(id);
        //1 删除 0正常
        return delete == 1;
    }

    /**
     * 分页的模糊查询
     * @param pageParam
     * @param teacherQuery
     */

    @Override
    public void queryTeacherByPageAndParam(Page<Teacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        //升序
        wrapper.orderByAsc("sort");
        if (teacherQuery == null) {
            baseMapper.selectPage(pageParam, wrapper);
            return;
        }
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Map<String, Object> pageListWeb(Page<Teacher> pageParam, Object o) {



        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        baseMapper.selectPage(pageParam, wrapper);

        List<Teacher> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

}
