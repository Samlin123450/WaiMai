package com.sam.Reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sam.Reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
