package com.guli.edu.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author GPX
 * @date 2019/12/12 14:05
 */
@Data
@ApiModel(value = "课程基本信息", description = "编辑课程基本信息的表单对象")
public class CourseInfoVo implements Serializable {

//    @ApiModelProperty(value = "课程ID")
//    private String id;
//
//    @ApiModelProperty(value = "课程讲师ID")
//    private String teacherId;
//
//    @ApiModelProperty(value = "课程专业ID")
//    private String subjectId;
//
//    @ApiModelProperty(value = "课程专业父级ID")
//    private String subjectParentId;
//
//    @ApiModelProperty(value = "课程标题")
//    private String title;
//
//    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
//    //@TableId(type = IdType.INPUT) //自己修改
//    private BigDecimal price;
//
//    @ApiModelProperty(value = "总课时")
//    private Integer lessonNum;
//
//    @ApiModelProperty(value = "课程封面图片路径")
//    private String cover;
//
//    @ApiModelProperty(value = "课程简介")
//    private String description;


    private Course course;
    private CourseDescription courseDescription;
}
