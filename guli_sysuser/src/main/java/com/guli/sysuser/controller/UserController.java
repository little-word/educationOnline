package com.guli.sysuser.controller;

import com.guli.common.vo.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    /**
     * 1、请求登陆的login
     */
    @PostMapping("login")
    public Result login(){
        return Result.ok().data("token","admin");
    }

    /**
     * 2、根据token获取用户信息
     */
    @GetMapping("info")
    public Result info(String token){
        /**
         * {"code":20000,
         * "data":
         *  {
         *      "roles":["admin"],
         *      "name":"admin",
         *      "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
         */
        return Result.ok()
                    .data("roles","[\"admin\"]")
                    .data("name","admin")
                    .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}
