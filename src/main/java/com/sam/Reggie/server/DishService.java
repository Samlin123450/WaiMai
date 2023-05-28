package com.sam.Reggie.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.Reggie.common.R;
import com.sam.Reggie.dto.DishDto;
import com.sam.Reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto findDishWithFlavor(long id);

    String updateDish(DishDto dishDto);

    void updateStatus(int status,long ids);
}
