package com.sam.Reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class globalExceptionhandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> ExceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.info("数据库异常,数据重复");
        if (exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");

            return R.error("用户名重复了"+split[2]);
        }
        return R.error("失败了^.^");
    }
}
