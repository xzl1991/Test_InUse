package a_发送邮件;

import java.util.Date;

public class emailTest {  
    public static void main(String[] args) {  
        MailInfo mailInfo = new MailInfo();  
        mailInfo.setMailServerHost("smtp.163.com");  
        mailInfo.setMailServerPort("25");  
        mailInfo.setValidate(true);  
        mailInfo.setUsername("mobile");  
        mailInfo.setPassword("qVVBBBBK2dB");// 您的邮箱密码  
        mailInfo.setFromAddress("xxxx@163.com");  
        mailInfo.setToAddress("xuzelong@fang.com");  
        mailInfo.setSubject("设置邮箱标题");  
                  
        //附件  
        String[] attachFileNames={"d:/Sunset.jpg"};  
        mailInfo.setAttachFileNames(attachFileNames);  
          
        // 这个类主要来发送邮件  
        //mailInfo.setContent("设置邮箱内容");  
        //SimpleMail.sendTextMail(mailInfo);// 发送文体格式  
        StringBuffer demo = new StringBuffer();  
        demo
//        .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
//        .append("<html>")  
//        .append("<head>")  
//        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
//        .append("<title>测试邮件</title>")  
//        .append("<style type=\"text/css\">")  
//        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
//        .append("</style>")  
//        .append("</head>")  
        
        .append("<br>&nbsp;&nbsp;&nbsp;&nbsp;您在房天下问答提出的\"")
		.append("已经有人回答，请您点击以下链接查看详细情况。\n<br/><a target=_blank href='")
		.append("'>")
		.append("</a><br/><br/>注：此邮件为系统提示邮件，")

				.append("请勿答复此邮件，如有问题请到如下链接反馈<a target=_blank href='")
				.append("http://www.fang.com/ask/feedback.aspx'>")
				.append("http://www.fang.com/ask/feedback.aspx</a>")
		.append("。感谢您使用房天下问答。<br/>")
				.append("<div style='text-align:right;padding-right:20px;'>")
		 .append(new Date()).append(
				"</div>")
				
        .append("<body>")  
        .append("<span class=\"test\">大家好，这里是测试Demo</span>")  ;
//        .append("</body>")  
//        .append("</html>");  
//        .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
//        .append("<html>")  
//        .append("<head>")  
//        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
//        .append("<title>测试邮件</title>")  
//        .append("<style type=\"text/css\">")  
//        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
//        .append("</style>")  
//        .append("</head>")  
//        .append("<body>")  
//        .append("<span class=\"test\">大家好，这里是测试Demo</span>")  
//        .append("</body>")  
//        .append("</html>");  
        mailInfo.setContent(demo.toString());  
        SimpleMail.sendHtmlMail(mailInfo);// 发送html格式  
    }  
}  
