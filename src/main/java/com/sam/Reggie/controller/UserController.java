package com.sam.Reggie.controller;

import com.sam.Reggie.common.R;
import com.sam.Reggie.entity.User;
import com.sam.Reggie.server.impl.MailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MailServiceImpl mailService;

    @PostMapping("/login")
    public R<String> userLogin(String email ,int code){
        log.info("");

        return R.success("666");
    }
//930691434@qq.com
    @PostMapping("/sendMsg")
    public R<String> userMsg(String email, HttpSession session){
        log.info(email);
        if(email!=null){
            Random random = new Random();
            String s = "";
            for(int i=0;i<5;i++){
                int nextInt = random.nextInt(10);
                String s1 = String.valueOf(nextInt);
                s = s+s1;
            }
            int parseInt = Integer.parseInt(s);
            session.setAttribute("yzm",parseInt);
            String yanzm = "你的验证码为："+s+"请在一分钟内输入";
            mailService.sendSimpleMail(email,"验证码",yanzm);
            return R.success("success");
        }
        return R.error("失败了");
    }

}
