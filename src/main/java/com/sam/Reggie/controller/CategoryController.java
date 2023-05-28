package com.sam.Reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sam.Reggie.common.R;
import com.sam.Reggie.entity.Category;
import com.sam.Reggie.entity.Employee;
import com.sam.Reggie.server.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping("")
    public R<String> addCategoryFood(@RequestBody Category category){
        if(category.getType()==1){
            service.save(category);
            return R.success("添加菜品成功！");
        }else if(category.getType()==2){
            service.save(category);
            return R.success("添加套餐成功！");
        }
        return R.error("添加失败");
    }
    @GetMapping("/page")
    public R<Page> showCategory(int page,int pageSize){
        Page pageInfo = new Page(page,pageSize);
//        log.info("{}+{}+{}",page,pageSize,name);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);
        service.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    @PutMapping("")
    public R<String> modifyCategory(@RequestBody Category category){
        if (category==null){
            return R.error("修改出现错误");
        }
        service.updateById(category);
        return R.success("修改成功");
    }
    @DeleteMapping
    public R<String> delCategory(long ids){
        service.removeById(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> CategorySelect( Category category,int type){
        category.setType(type);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = service.list(wrapper);

        return R.success(list);
    }


}
