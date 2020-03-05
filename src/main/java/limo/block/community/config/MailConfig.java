package limo.block.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 邮件配置类
 */
@Configuration
public class MailConfig {
    /**
     * 获取邮件发送实例
     */
    @Bean
    public MailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        /*指定用来发送Email的邮件服务器主机名*/
        mailSender.setHost("smtp.qq.com");
        /*设置默认端口，标准的SMTP端口*/
        mailSender.setPort(587);
        /*用户名*/
        mailSender.setUsername("1073472234@qq.com");
        /*授权码*/
        mailSender.setPassword("vpfdjlkzhxfkbcfa");

        return mailSender;
    }
}
