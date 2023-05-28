package com.sam.Reggie.controller;

import com.sam.Reggie.common.R;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Configuration
@RestController
@Slf4j
@RequestMapping("/common")
public class commonController {
    @Value("${riggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
//        原始文件名
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        int length = split.length;


        String filename = UUID.randomUUID().toString()+"."+split[length-1];

        try{

            file.transferTo(new File(basePath+"\\"+filename));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


        return R.success(filename);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            if(!new File(basePath+name).exists()){
                return;
            }
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = inputStream.read(bytes))!=-1){
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
