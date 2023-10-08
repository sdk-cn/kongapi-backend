package com.dsk.kongapiinterface.controller;


import com.dsk.kongapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//
//        //这里应该从用户数据库里查询是否有此ak的用户
//        if(!accessKey.equals("wangliangxi")){
//            throw new RuntimeException("无权限");
//        }
//
//        //todo 判断该请求里携带的时间戳跟当前时间的间隔是否在5分钟或者10分钟,防止重放攻击，可以为随机数判断拦住一部分请求
//        //if(timestamp)
//
//
//        //从内存里的HashMap或者redis里查询随机数是否存在,这里随便判断一下
//        if(Long.parseLong(nonce)>1000){
//            throw new RuntimeException("无权限");
//        }
//
//        //从数据库里查出ak对应的sk，并计算sign，然后比较
//        String serverSign= SignUtils.genSign(body,"loveu");
//        if(!sign.equals(serverSign)){
//            throw new RuntimeException("无权限");
//        }


        return "POST 用户名字是" + user.getUserName();
    }
}
