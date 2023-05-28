package com.sam.Reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Supplier;
//处理器处理公共字段
@Slf4j
@Component
public class myMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】");
        log.info(metaObject.toString());
        long id = threadValue.getId();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】");
        log.info(metaObject.toString());

        long id = threadValue.getId();
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("updateUser", id);
    }


}
