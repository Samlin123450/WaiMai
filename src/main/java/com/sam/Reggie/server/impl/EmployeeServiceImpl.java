package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.entity.Employee;
import com.sam.Reggie.mapper.EmployeeMapper;
import com.sam.Reggie.server.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService  {

}
