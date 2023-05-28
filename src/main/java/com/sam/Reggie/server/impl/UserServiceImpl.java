package com.sam.Reggie.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.Reggie.entity.User;
import com.sam.Reggie.mapper.UserMapper;
import com.sam.Reggie.server.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
