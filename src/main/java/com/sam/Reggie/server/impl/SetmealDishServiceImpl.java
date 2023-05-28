package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.entity.Setmeal;
import com.sam.Reggie.entity.SetmealDish;
import com.sam.Reggie.mapper.SetmealDishMapper;
import com.sam.Reggie.mapper.SetmealMapper;
import com.sam.Reggie.server.SetmealDishService;
import com.sam.Reggie.server.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
