package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.DishFlavor;
import com.sam.Reggie.dto.DishDto;
import com.sam.Reggie.entity.Dish;
import com.sam.Reggie.mapper.DishMapper;
import com.sam.Reggie.server.DishFlavorService;
import com.sam.Reggie.server.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto){
        this.save(dishDto);
        Long id = dishDto.getId();
        List<com.itheima.reggie.entity.DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(
                (item) -> {
                    item.setDishId(id);
                    return item;
                }
        ).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    @Transactional
    public DishDto findDishWithFlavor(long id) {
        Dish byId = this.getById(id);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<DishFlavor>();
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavorServiceMap = dishFlavorService.getBaseMapper().selectList(wrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);
        dishDto.setFlavors(flavorServiceMap);
        return dishDto;
    }

    @Override
    @Transactional
    public String updateDish(DishDto dishDto) {
        if(dishDto==null){
            return "修改出错";
        }
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish!=null,Dish::getId,dish.getId());
        this.update(dish,wrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        dishFlavorService.updateBatchById(flavors);


        return "修改成功";
    }

    @Override
    @Transactional
    public void updateStatus(int status, long ids) {

        Dish byId = this.getById(ids);
        byId.setStatus(status);
        this.updateById(byId);

    }
}
