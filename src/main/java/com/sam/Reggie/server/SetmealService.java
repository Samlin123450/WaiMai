package com.sam.Reggie.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.Reggie.dto.MealDto;
import com.sam.Reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

void addMeal(MealDto mealDto);

MealDto showModifyMeal(long id);

void updateMeal(MealDto mealDto);
}
