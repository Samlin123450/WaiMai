package com.sam.Reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.DishFlavor;
import com.sam.Reggie.common.R;
import com.sam.Reggie.dto.DishDto;
import com.sam.Reggie.entity.Category;
import com.sam.Reggie.entity.Dish;
import com.sam.Reggie.entity.Employee;
import com.sam.Reggie.server.CategoryService;
import com.sam.Reggie.server.DishFlavorService;
import com.sam.Reggie.server.DishService;
import com.sam.Reggie.server.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.WebFilter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> showDish(int page,int pageSize,String name){
        Page<Dish> pageinfo = new Page<>(page,pageSize);
        Page<DishDto> pageinfo1 = new Page<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), Dish::getName,name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageinfo);
        BeanUtils.copyProperties(pageinfo,pageinfo1,"records");
        List<Dish> records = pageinfo.getRecords();
        List<DishDto> collect = records.stream().map(
                (item) -> {
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    Long id = item.getCategoryId();
                    Category byId = categoryService.getById(id);
                    if(byId!=null){
                        String name1 = byId.getName();
                        dishDto.setCategoryName(name1);
                    }

                    return dishDto;
                }
        ).collect(Collectors.toList());
        pageinfo1.setRecords(collect);
        return R.success(pageinfo1);

    }

    @PostMapping("")
    public R<String> addDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> modifyDish(@PathVariable long id){
        DishDto dishDto = dishService.findDishWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto){
        String s = dishService.updateDish(dishDto);

        return R.success(s);
    }

    @PostMapping("/status/{status}")
    public R<String> updateDishStatus(@PathVariable("status") int status,String ids){
//        log.info(ids);
        String[] split = ids.split(",");
        for(String id:split){
            long idL = Long.parseLong(id);
            dishService.updateStatus(status, idL);
        }

        return R.success("update success");
    }

//    @PostMapping("/status/{status}")
//    public R<String> updateBatchDishStatus(@PathVariable("status") int status,long[] ids){
//        log.info("{},{}",status,ids);
//        return null;
//    }

    @DeleteMapping("")
    public R<String> delDish(long[] ids){
        for(long i:ids){
            dishService.removeById(i);
        }
        return R.success("sucess for delate");
    }

    @GetMapping("/list")
    public R<List<Dish>> addMeal(long categoryId){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(wrapper);
        return R.success(list);
    }


}
