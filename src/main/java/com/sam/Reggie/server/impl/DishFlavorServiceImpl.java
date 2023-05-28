package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.mapper.DishFlavorMapper;
import com.sam.Reggie.server.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, com.itheima.reggie.entity.DishFlavor>implements DishFlavorService {
}
