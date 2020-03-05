package limo.block.community.controller;

import limo.block.community.entity.User;
import limo.block.community.service.UserService;
import limo.block.community.util.Consts;
import limo.block.community.util.MD5Util;
import limo.block.community.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @Resource
    private JavaMailSender mailSender;

    @ResponseBody
    @PostMapping("/register")
    public Map<String, Object> register(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
        } else if (userService.findByUserName(user.getUserName()) != null) {
            map.put("success", false);
            map.put("errorInfo", "用户名已存在");
        } else if (userService.findByEmail(user.getEmail()) != null) {
            map.put("success", false);
            map.put("errorInfo", "邮箱已存在");
        } else {
            user.setPassWord(MD5Util.md5(user.getPassWord(), MD5Util.SALT));
            userService.save(user);
            map.put("success", true);
        }
        return map;
    }

    /**
     * 用户登录
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> login(User user, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(user.getUserName())) {
            map.put("success", false);
            map.put("errorInfo", "请输入用户名!");
        } else if (StringUtil.isEmpty(user.getPassWord())) {
            map.put("success", false);
            map.put("errorInfo", "请输入密码!");
        } else {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), MD5Util.md5(user.getPassWord(), MD5Util.SALT));
            try {
                /*登录验证*/
                subject.login(token);
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                User currentUser = userService.findByUserName(userName);
                if (currentUser.isOff()) {
                    map.put("success", false);
                    map.put("errorInfo", "该用户已被封禁，请联系管理员");
                    subject.logout();
                } else {
                    session.setAttribute(Consts.CURRENT_USER,currentUser);
                    map.put("success", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "用户名或密码错误");
            }
        }
        return map;
    }

    /**
     * 发送邮件
     */
    @ResponseBody
    @PostMapping("/sendEmail")
    public Map<String, Object> sendEmail(String email, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(email)) {
            map.put("success", false);
            map.put("errorInfo", "邮箱不能为空!");
            return map;
        }
        /*验证邮箱是否存在*/
        User u = userService.findByEmail(email);
        if(u==null){
            map.put("success", false);
            map.put("errorInfo", "邮箱填写错误!");
            return map;
        }
        String mailCode = StringUtil.getSixRandom();
        /*发邮件*/
        /*消息构造器*/
        SimpleMailMessage message = new SimpleMailMessage();
        /*发件人*/
        message.setFrom("1073472234@qq.com");
        /*收件人*/
        message.setTo(email);
        /*主题*/
        message.setSubject("博客用户找回密码");
        /*正文内容*/
        message.setText("您本次的验证码是"+ mailCode);
        mailSender.send(message);
        /*验证码存入session*/
        session.setAttribute(Consts.MAIL_CODE_NAME,mailCode);
        session.setAttribute(Consts.USER_ID_NAME,u.getUserId());

        map.put("success",true);
        return map;
    }

    /**
     * 邮件验证码判断
     */
    @ResponseBody
    @PostMapping("/checkYzm")
    public Map<String, Object> checkYzm(String yzm, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(yzm)) {
            map.put("success", false);
            map.put("errorInfo", "验证码不能为空!");
            return map;
        }
        String mailCode = (String) session.getAttribute(Consts.MAIL_CODE_NAME);
        Integer userId = (Integer) session.getAttribute(Consts.USER_ID_NAME);

        if(!mailCode.equals(yzm)){
            map.put("success", false);
            map.put("errorInfo", "验证码错误!");
            return map;
        }
        /*给用户重置密码为123456*/
        User user = userService.getUserById(userId);
        user.setPassWord(MD5Util.md5(Consts.PASSWARD,MD5Util.SALT));
        userService.save(user);
        map.put("success",true);
        return map;
    }


}
