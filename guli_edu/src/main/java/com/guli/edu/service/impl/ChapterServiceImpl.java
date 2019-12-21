package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.ChapterMapper;
import com.guli.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    //获取课程大纲
    @Override
    public List<Map<String, Object>> getChapterListByCourseId(String courseId) {
        //将数据封装到一个集合中
        List<Map<String, Object>> mapList = new ArrayList<>();
        //1、根据课程的Id查询章节列表  获取的一级节点
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.orderByAsc("sort");
        List<Chapter> chapterList = baseMapper.selectList(wrapper);
        //2、遍历章节列表
        for (Chapter chapter : chapterList) {
            //3、获取每一个章节信息放在Map中 id title 父节点Id
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", chapter.getId());
            map.put("title", chapter.getTitle());

            //4、根据每一个章节的Id查询小节的列表 视频表
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId);
            queryWrapper.eq("chapter_id", chapter.getId());
            queryWrapper.orderByAsc("sort");
            //查询二级分类节点 视频节点 子节点
            List<Video> videoList = videoService.list(queryWrapper);
            //5、把小节的集合对象放在章节的Map对象中
            map.put("children", videoList);
            //6、把章节的Map对象放在集合中
            mapList.add(map);
        }
       

        return mapList;
    }
}
