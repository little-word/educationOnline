package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.exception.GuliException;
import com.guli.edu.entity.Subject;
import com.guli.edu.entity.vo.SubjectOneVo;
import com.guli.edu.entity.vo.SubjectTwoVo;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-12-04
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public List<String> importFile(MultipartFile file) {
        //上传Excel 要有excel 表格对象
        //上传的格式为 03
        //获取上传流
        //读取上传的内容
        // 判断有无文件信息
        //判断第一节点是否存在
        //判断第二节点是否存在
        //判断节点下面数据是否存在
        //成功上传 返回message(是否有错的提示)

        List<String> message = new ArrayList<>();

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            //获取最后一行的索引值
            int lastRowNumows = sheet.getLastRowNum();
            //判断是否有数据
            if (lastRowNumows < 1) {
                message.add("文件没有数据");
                return message;
            }
            //有数据遍历获取数据
            for (int row = 1; row <= lastRowNumows; row++) {
                //获取第一行
                Row row1 = sheet.getRow(row);
                //判断有否有第一行
                if (row1 != null) {
                    //有数据 获取第一列的数据
                    Cell cell = row1.getCell(0);//获取第一列的值 cellnum 不能变化
                    // 变化就是 第1行 第row列 ，与表格的格式不匹配 不是一个节点下的子节点
                    // ，获取值应该为第一列对应的每一行的值 第一行第三列没有值
                    //判断第一列是否有值
                    if (cell == null) {
                        message.add("第" + (row + 1) + "行第1列为空");
                        continue;
                    }
                    //如果第一列有数据 获取其值
                    String stringCellValue = null;//列值
                    if (cell != null) {
                        stringCellValue = cell.getStringCellValue();
                        //判断值是否存在
                        if (StringUtils.isEmpty(stringCellValue)) {
                            message.add("第" + (row + 1) + "行第1列为空");
                            continue;
                        }

                    }
                    //14、如果存在：根据一级分类名称和parentId = 0 查询数据库是否存在这个分类名称--二级分类（集合）
                    Subject subject = this.getSubjectByTitleAndParentId(stringCellValue, "0");
                    //15、如果存在获取Id
                    String subjectOneId = null; // 一级分类
                    if (subject == null) {
                        //16、如果不存在：添加一级分类；获取Id
                        subject = new Subject();
                        //自定义设置
                        subject.setTitle(stringCellValue);
                        subject.setSort(row);
                        subject.setParentId("0");
                        baseMapper.insert(subject);
                        subjectOneId = subject.getId();
                    } else {
                        subjectOneId = subject.getId();
                    }
                    //----------------------------------获取第二列的数据
                    //17、获取第二列
                    Cell cellTwo = row1.getCell(1);
                    //18、判断第二列是否存在
                    if (cellTwo == null) {
                        message.add("第" + (row + 1) + "行第2列为空");
                        continue;
                    }
                    //19、如果存在获取第二列的值
                    String stringCellValue1 = null;
                    if (cellTwo != null) {
                        stringCellValue1 = cellTwo.getStringCellValue();
                        //20、判断值是否存在；
                        if (StringUtils.isEmpty(stringCellValue1)) {

                            message.add("第" + (row + 1) + "行第2列为空");
                            continue;
                        }

                    }
                    //21、如果存在：根据第二级分类的名称和刚添加进去的一级分类Id为ParentId 查询是否有此二级分类名称存在
                    Subject subjectTwo = this.getSubjectByTitleAndParentId(stringCellValue1, subjectOneId);
                    //22、如果不存在直接添加
                    if (subjectTwo == null) {
                        subjectTwo = new Subject();
                        subjectTwo.setTitle(stringCellValue1);
                        subjectTwo.setSort(row);
                        subjectTwo.setParentId(subjectOneId);
                        baseMapper.insert(subjectTwo);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 树形结构
     *获取一级二级 节点
     * @return
     */
    @Override
    public List<SubjectOneVo> getTreeList() {
        //创建一个 所有节点的集合
        ArrayList<SubjectOneVo> subjectOneList = new ArrayList<>();
        //1、先获取一级分类：集合
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", "0");
        wrapper.orderByAsc("sort");
        //获取到所有的父节点  所有节点集合
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        //2、遍历每一个父节点
        for (Subject subject : subjectList) {
            //将数据拷贝到新的对象中，此对象包含 children
            SubjectOneVo subjectOneVo = new SubjectOneVo();
            //3、获取每一个一级分类下的二级分类集合
            BeanUtils.copyProperties(subject, subjectOneVo);
            //4、把每一个二级分类数据添加到一级分类的children中
            //4.1获取所有的二级节点，将二级节点复制到新的对象中
            QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
            //获取到二级节点
            queryWrapper.eq("parent_id", subjectOneVo.getId());
            wrapper.orderByAsc("sort");
            List<Subject> list = baseMapper.selectList(queryWrapper);
            for (Subject subject1 : list) {
                //将子节点拷贝到新的对象中
                SubjectTwoVo subjectTwoVo = new SubjectTwoVo();
                BeanUtils.copyProperties(subject1, subjectTwoVo);
                //将子节点 添加到父节点中
                subjectOneVo.getChildren().add(subjectTwoVo);
            }
            //5、把每一个一级分类添加到总集合中
            subjectOneList.add(subjectOneVo);

        }

        return subjectOneList;
    }

    /**
     * 递归删除
     *
     * @param id
     */
    @Override
    public void removeTreeById(String id) {
        //通过法节点的Id获取其对应的子节点的Id创建集合递归删除
        //创建一个集合保存所有节点信息
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        this.getIds(ids, id);
        baseMapper.deleteBatchIds(ids);
    }

    private void getIds(ArrayList<String> ids, String id) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        for (Subject subject : subjectList) {
            ids.add(subject.getId());
            //递归添加
            this.getIds(ids, subject.getId());
        }
    }

    @Override
    public void saveSubject(Subject subject) {
        //判断是否是一级节点
        //默认的父节点就有
//        if(subject.getTitle() !=null){
//            throw new GuliException(20002,"此课程已存在");
//        }

        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", subject.getTitle());
        Subject selectOne = baseMapper.selectOne(wrapper);
        if (selectOne != null) {
            throw new GuliException(20002, "此课程已存在");
        }

        Subject sub = this.getSubjectByTitleAndParentId(subject.getParentId(), subject.getTitle());
        //一级节点 如果没有title 就是二级节点，有就是一级节点
        if (sub == null) {
            baseMapper.insert(subject);
        } else {
            throw new GuliException(20002, "此节点已存在");
        }

        //判断课程分类是否存在
        //保存
    }

    //查询二级节点
    @Override
    public List<Subject> getSubjectListByParentId(String parentId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        wrapper.orderByAsc("sort");
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        return subjectList;
    }


    /**
     * 根据课程分类的title和ParentId查询课程分类数据
     *
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubjectByTitleAndParentId(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_Id", parentId);
        Subject subject = baseMapper.selectOne(queryWrapper);
        return subject;
    }
}
