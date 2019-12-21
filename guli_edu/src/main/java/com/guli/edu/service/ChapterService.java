package com.guli.edu.service;

import com.guli.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface ChapterService extends IService<Chapter> {

    //获取课程大纲 需要查询一级 二级 列表 不用Vo
    List<Map<String, Object>> getChapterListByCourseId(String courseId);
}
