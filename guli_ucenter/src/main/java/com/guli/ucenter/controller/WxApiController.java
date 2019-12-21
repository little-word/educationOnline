package com.guli.ucenter.controller;

import com.google.gson.Gson;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.Result;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.entity.vo.LoginInfoVo;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.utils.ConstantPropertiesUtil;
import com.guli.ucenter.utils.HttpClientUtils;
import com.guli.ucenter.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author GPX
 * @date 2019/12/18 16:10
 */
@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private MemberService memberService;

    /**
     * 微信登录跳转页面
     *
     * @param session
     * @return
     */
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

//        appid	是	应用唯一标识
//        redirect_uri	是	请使用urlEncode对链接进行处理
//        response_type	是	填code
//        scope	是	应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即
//        state	否	用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），
//        建议第三方带上该参数，可设置为简单的随机数加session进行校验
        System.out.println("sessionId = " + session.getId());

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20002, "地址解析失败");
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "gpx";//为了让大家能够使用微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("生成 state = " + state);
        session.setAttribute("wx-open-state", state);

        //生成qrcodeUrl  进行占位符填充 baseUrl中的占位符  二维码的请求地址
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }

    /**
     * 1、获取回调参数
     * 2、从redis中读取state进行比对，异常则拒绝调用
     * 3、向微信的授权服务器发起请求，使用临时票据换取access token
     * 4、使用上一步获取的openid查询数据库，判断当前用户是否已注册，如果已注册则直接进行登录操作
     * 5、如果未注册，则使用openid和access token向微信的资源服务器发起请求，请求获取微信的用户信息
     * 5.1、将获取到的用户信息存入数据库
     * 5.2、然后进行登录操作
     *
     * @param code
     * @param state
     * @return
     */

    @GetMapping("/callback")
    public String callback(String code, String state, HttpServletResponse response, HttpServletRequest request) {

        //得到授权临时票据code
        System.out.println(code);
        System.out.println(state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        //获取到唯一的Token和openid
        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }

        //解析json字符串 转换为map格式
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        //查询数据库当前用用户是否曾经使用过微信登录
        Member member = memberService.getByOpenid(openid);
        if (member == null) {
            System.out.println("新用户注册");


            //访问微信的资源服务器，获取用户信息
            //通过accessToken,OpendId  请求微信的固定地址 获取扫码人的信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            //填充baseAccessTokenUrl三个占位符（=%  " ） 拼接请求 路径 带登录信息
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.err.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(20001, "获取用户信息失败");
            }

            //解析json
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) mapUserInfo.get("nickname");
            String headimgurl = (String) mapUserInfo.get("headimgurl");

            //向数据库中插入一条记录  保存登录人的信息
            member = new Member();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }
        //TODO 登录  结合前端页面实现微信登录
        System.out.println("登录");

        // 登录
        // 生成jwt
        String guliJwtToken = JwtUtils.generateJWT(member);
        // 存入cookie，超时时间30分钟 放入session中
        request.getSession().setAttribute("guli_jwt_token", guliJwtToken);
//        CookieUtils.setCookie(request, response, "guli_jwt_token", guliJwtToken, 60 * 30);

        return "redirect:http://localhost:3000?token=" + guliJwtToken;
    }

    /**
     * 获取jwt
     * @param request
     * @return
     */
    @GetMapping("get-jwt")
    @ResponseBody
    @ApiOperation(value = "获取JWT")
    public Result getJwt(HttpServletRequest request) {
        String guliJwtToken = (String) request.getSession().getAttribute("guli_jwt_token");
//        String guliJwtToken = CookieUtils.getCookieValue(request, "guli_jwt_token");
        System.err.println(guliJwtToken);
        return Result.ok().data("guli_jwt_token", guliJwtToken);
    }

    //根据 token获取登录信息
    @PostMapping("/parse-jwt")
    @ResponseBody
    public Result getLoginInfoByJwtToken(@RequestBody String jwtToken){

        Claims claims = JwtUtils.checkJWT(jwtToken);

        String id = (String)claims.get("id");
        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");

        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setNickname(nickname);
        loginInfoVo.setAvatar(avatar);

        return Result.ok().data("loginInfo", loginInfoVo);

    }





//    @GetMapping("/callback")
//    public String callback(String code, String state) {
//
//        //得到授权临时票据code
//        System.out.println(code);
//        System.out.println(state);
//
//        //从redis中将state获取出来，和当前传入的state作比较
//        //如果一致则放行，如果不一致则抛出异常：非法访问
//
//        //向认证服务器发送请求换取access_token
//        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
//                "?appid=%s" +
//                "&secret=%s" +
//                "&code=%s" +
//                "&grant_type=authorization_code";
//
//        String accessTokenUrl = String.format(baseAccessTokenUrl,
//                ConstantPropertiesUtil.WX_OPEN_APP_ID,
//                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
//                code);
//
//        //获取到唯一的Token和openid
//        String result = null;
//        try {
//            result = HttpClientUtils.get(accessTokenUrl);
//            System.out.println("accessToken=============" + result);
//        } catch (Exception e) {
//            throw new GuliException(20001, "获取access_token失败");
//        }
//
//        //解析json字符串 转换为map格式
//        Gson gson = new Gson();
//        HashMap map = gson.fromJson(result, HashMap.class);
//        String accessToken = (String) map.get("access_token");
//        String openid = (String) map.get("openid");
//
//        //查询数据库当前用用户是否曾经使用过微信登录
//        Member member = memberService.getByOpenid(openid);
//        if (member == null) {
//            System.out.println("新用户注册");
//
//
//            //访问微信的资源服务器，获取用户信息
//            //通过accessToken,OpendId  请求微信的固定地址 获取扫码人的信息
//            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
//                    "?access_token=%s" +
//                    "&openid=%s";
//
//            //填充baseAccessTokenUrl三个占位符（=%  " ） 拼接请求 路径 带登录信息
//            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
//            String resultUserInfo = null;
//            try {
//                resultUserInfo = HttpClientUtils.get(userInfoUrl);
//                System.err.println("resultUserInfo==========" + resultUserInfo);
//            } catch (Exception e) {
//                throw new GuliException(20001, "获取用户信息失败");
//            }
//
//            //解析json
//            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
//            String nickname = (String) mapUserInfo.get("nickname");
//            String headimgurl = (String) mapUserInfo.get("headimgurl");
//
//            //向数据库中插入一条记录  保存登录人的信息
//            member = new Member();
//            member.setNickname(nickname);
//            member.setOpenid(openid);
//            member.setAvatar(headimgurl);
//            memberService.save(member);
//        }
//        //TODO 登录  结合前端页面实现微信登录
//       return "redirect:http://localhost:3000";
//    }
}
