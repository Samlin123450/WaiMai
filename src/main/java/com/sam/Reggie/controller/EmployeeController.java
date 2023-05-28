package com.sam.Reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sam.Reggie.common.R;
import com.sam.Reggie.entity.Employee;
import com.sam.Reggie.server.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService EmployeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = EmployeeService.getOne(wrapper);

        if (employee1==null){
            return R.error("登录失败");
        }

        if (!password.equals(employee1.getPassword())){
            return R.error("密码错误");
        }
        if(employee1.getStatus().equals(1)){
            request.getSession().setAttribute("employeeId",employee1.getId());
            return R.success(employee1);
        }else{
            return R.error("状态不可用");
        }
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employeeId");
        return R.success("退出成功！");
    }

    @PostMapping()
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工{}",employee.toString());
        //设置默认密码
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        //设置创建时间与更新时间 拦截器统一处理
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        long employeeId = (long) request.getSession().getAttribute("employeeId");
//        employee.setCreateUser(employeeId);
//        employee.setUpdateUser(employeeId);
//        try {   //直接全局处理
            EmployeeService.save(employee);
//        }catch (Exception e){
//            R.error("添加失败");
//        }


        return R.success("添加成功！");
    }
    @GetMapping("/page")
    public R<Page> allEmployee(int page,int pageSize,String name){
//        log.info("{}+{}+{}",page,pageSize,name); //查看是否传参成功   1+10+null
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        EmployeeService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    @PutMapping()
    public R<String> modifyStatus(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        long employeeId = (long)request.getSession().getAttribute("employeeId");
        employee.setUpdateUser(employeeId);
//        employee.setUpdateTime(LocalDateTime.now());

        log.info("用户：{}修改{}的权限",employeeId,employee.getId());

        EmployeeService.updateById(employee);
        return R.success("修改权限成功");
    }

    @GetMapping("/{id}")
    public R<Employee> modifyEmployee(@PathVariable("id") long id){
        log.info("{}",id);

        Employee byId = EmployeeService.getById(id);
        if(byId!=null){
            return R.success(byId);
        }
        return R.error("无该用户");
    }

}
