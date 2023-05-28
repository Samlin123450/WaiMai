package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.dto.MealDto;
import com.sam.Reggie.entity.Category;
import com.sam.Reggie.entity.Setmeal;
import com.sam.Reggie.entity.SetmealDish;
import com.sam.Reggie.mapper.SetmealMapper;
import com.sam.Reggie.server.CategoryService;
import com.sam.Reggie.server.SetmealDishService;
import com.sam.Reggie.server.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService   {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;



    @Override
    @Transactional
    public void addMeal(MealDto mealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(mealDto,setmeal);
        this.save(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = mealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map(
                (item) -> {
                    item.setSetmealId(id);
                    return item;
                }
        ).collect(Collectors.toList());
        setmealDishService.saveBatch(collect);


    }

    @Transactional
    @Override
    public MealDto showModifyMeal(long id) {
        Setmeal byId = this.getById(id);
        Long setmealId = byId.getId();
        Long categoryId = byId.getCategoryId();
        Category categoryById = categoryService.getById(categoryId);
        String name = categoryById.getName();
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        MealDto mealDto = new MealDto();
        BeanUtils.copyProperties(byId,mealDto);
        mealDto.setSetmealDishes(list);
        mealDto.setCategoryName(name);
        return mealDto;
    }

    @Transactional
    @Override
    public void updateMeal (MealDto mealDto){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(mealDto,setmeal);
        this.updateById(setmeal);
        //删除旧的所有菜品
        Long setmealId = setmeal.getId();
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(wrapper);
        for(SetmealDish dish:mealDto.getSetmealDishes()){
            dish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(mealDto.getSetmealDishes());

//        List<SetmealDish> dishList = mealDto.getSetmealDishes();
//        setmealDishService.updateBatchById(dishList);



    }

}
