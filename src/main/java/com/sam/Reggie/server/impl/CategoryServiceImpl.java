package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.entity.Category;
import com.sam.Reggie.mapper.CategoryMapper;
import com.sam.Reggie.server.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategroyServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}