package com.sam.Reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sam.Reggie.common.R;
import com.sam.Reggie.dto.MealDto;
import com.sam.Reggie.entity.Category;
import com.sam.Reggie.entity.Setmeal;
import com.sam.Reggie.entity.SetmealDish;
import com.sam.Reggie.server.CategoryService;
import com.sam.Reggie.server.SetmealDishService;
import com.sam.Reggie.server.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class setmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page> showSetmeal(int page, int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>();
        Page<MealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
        Wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, Wrapper);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<MealDto> collect = records.stream().map(
                (item) -> {
                    MealDto mealDto = new MealDto();
                    BeanUtils.copyProperties(item, mealDto);
                    Long categoryId = item.getCategoryId();
                    Category byId = categoryService.getById(categoryId);
                    String idName = byId.getName();
                    mealDto.setCategoryName(idName);
                    Long setMealId = item.getId();
                    LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SetmealDish::getSetmealId,setMealId);
                    List<SetmealDish> list = setmealDishService.list(wrapper);
                    mealDto.setSetmealDishes(list);
                    return mealDto;
                }
        ).collect(Collectors.toList());
        dtoPage.setRecords(collect);


        return R.success(dtoPage);
    }

    @PostMapping("")
    public R<String> addSetMeal(@RequestBody MealDto mealDto){
        setmealService.addMeal(mealDto);

        return R.success("保存成功");
    }

    @GetMapping("/{id}")
    public R<MealDto> modifyMeal(@PathVariable("id") long id){
        MealDto mealDto = setmealService.showModifyMeal(id);
        return R.success(mealDto);
    }

    @PutMapping("")
    public R<String> updateMeal(@RequestBody MealDto mealDto){
        setmealService.updateMeal(mealDto);
        return R.success("成功更新");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status , long[] ids){
        for(long i:ids){

            Setmeal byId = setmealService.getById(i);
            byId.setStatus(status);
            setmealService.updateById(byId);
        }
        return R.success("success");
    }

    @DeleteMapping("")
    public R<String> delMeal(long[] ids){
        for(long id: ids){
            Setmeal byId = setmealService.getById(id);
            if(byId.getStatus()==0){
                setmealService.removeById(id);
                LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SetmealDish::getSetmealId,id);
                setmealDishService.remove(wrapper);
            }else{
                return R.error("请停售后再删除");
            }

        }
        return R.success("success");
    }


}
