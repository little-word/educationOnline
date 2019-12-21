package com.guli.common.Handler;

import com.guli.common.exception.GuliException;
import com.guli.common.utils.ExceptionUtil;
import com.guli.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 统一处理异常
 * @author GPX
 * @date 2019/12/4 19:25
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody//自定义异常处理
    @ExceptionHandler( GuliException.class)
    public Result GuliException(GuliException e) {
        //e.printStackTrace();
        //把异常信息封到栈中
        //2.输出异常
        log.error(ExceptionUtil.getMessage(e));
        return Result.error().message(e.getMessage()).code(e.getCode());
    }
    @ResponseBody//特殊异常处理
    @ExceptionHandler( ArithmeticException.class)
    public  Result ArithmeticException(Exception e){
      //  e.printStackTrace();
        log.error(e.getMessage());
        return Result.error().message("算数异常");

    }


    @ExceptionHandler(Exception.class)//全局异常处理
    @ResponseBody
    public Result error(Exception e){
        //e.printStackTrace();
        log.error(e.getMessage());
        return Result.error();
    }
}
