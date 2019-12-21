package com.guli.edu.service;

import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.vo.SubjectOneVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
public interface SubjectService extends IService<Subject> {

    List<String> importFile(MultipartFile file);

    List<SubjectOneVo> getTreeList();

    void removeTreeById(String id);

    void saveSubject(Subject subject);

    List<Subject> getSubjectListByParentId(String parentId);
}
