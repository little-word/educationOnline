package com.guli.edu.controller;


import com.guli.common.vo.Result;
import com.guli.edu.entity.Subject;
import com.guli.edu.entity.vo.SubjectOneVo;
import com.guli.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/edu/subject")
@CrossOrigin
@Api(description = "文件上传")
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * 文件导入
     * @param file
     * @return
     */
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/import")
    public Result fileImport(MultipartFile file){

        //返回错误的提示消息
       List<String> message =  subjectService.importFile(file);
        System.out.println(message);
        if (message.size() == 0){
            return  Result.ok();
        }else {
            return  Result.error().data("message",message);
        }

    }

    /**
     * 课程分类树形结构 所有的一二级节点
     * @return
     */
    @ApiOperation(value = "课程的树形结构")
    @GetMapping("/getTreeList")
    public Result getTreeList(){
       List<SubjectOneVo> list=subjectService.getTreeList();
        return Result.ok().data("list",list);
    }
    /**
     * 查询二级节点
     */
    @GetMapping("getSubjectListByParentId/{parentId}")
    @ApiOperation(value = "获取二级节点")
    public Result getSubjectListByParentId(
            @PathVariable("parentId")String parentId
    ){
        List<Subject> list=subjectService.getSubjectListByParentId(parentId);
        return Result.ok().data("list",list);
    }


    /**
     * 递归删除
     */
    @DeleteMapping("/removeById/{id}")
    @ApiOperation(value = "删除节点")
    public Result remove(
            @PathVariable("id")String id
    ){
        //subjectService.removeById(id);
        subjectService.removeTreeById(id);
        return Result.ok();
    }
    /**
     * 添加节点
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "添加节点")
    public Result  save(
           @RequestBody  Subject subject
    ){
        subjectService.saveSubject(subject);
        return  Result.ok();
    }


}

