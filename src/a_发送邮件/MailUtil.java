package a_发送邮件;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {

	private static final String username = "mobile";

	private static final String password = "qVVBBBBK2dB";

	private static String smtp_server = "smtp.staff.soufun.com";

	private static int COUNTER = 0;
	private static int COUNTER_timedOut = 0;
	private static int COUNTER_notFound = 0;
	private static int COUNTER_conFail = 0;
	
	private static Map mailMap = new HashMap<Integer, String>(50);
	private static Map mailMap_timedOut = new HashMap<Integer, String>(50);
	private static Map mailMap_notFound = new HashMap<Integer, String>(50);
	private static Map mailMap_conFail = new HashMap<Integer, String>(50);

	public static void main(String[] args) {
		String[] tos = {  "malin.bj@fang.com" };
			MailUtil.sendMail("阿抵抗力将发生劳动局发生的", "xuzelong@fang.com","服务器报错邮件测试","");
			System.out.println(".......");
	}

	/**
	 * 相关业务发送异常URL
	 * */
	public static void sendMailOfErrorUrl(String url,String type){
		// 抄送者
		String cc =  "";
		
	}
	/**
	 * 异常邮件内容设置
	 * */
	public static void sendPatchMail(String content) {
		//区别邮件类型
		if(content.contains("timed out")){
			COUNTER_timedOut++;
			mailMap_timedOut.put(COUNTER_timedOut, content);
		}else if(content.contains("连接")){
			COUNTER_conFail++;
			mailMap_conFail.put(COUNTER_conFail, content);
		}else if(content.contains("FileNotFoundException")){
			COUNTER_notFound++;
			mailMap_notFound.put(COUNTER_notFound, content);
		}else{
			if(content.contains("index out of range")){
				return ;
			}
			COUNTER++;
			mailMap.put(COUNTER, content);
		}
		//判断是否该发邮件
		String subject = "";
		if(COUNTER == 50){
			subject = "无线部服务器监控报告";
			sendMail_batch(mailMap,subject,50);
			//初始化
			COUNTER = 0;
			mailMap.clear();
		}
		if(COUNTER_timedOut == 100){
			subject = "无线部连接或读取超时报告";
			sendMail_batch(mailMap_timedOut,subject,100);
			COUNTER_timedOut = 0;
			mailMap_timedOut.clear();
		}
		if(COUNTER_conFail == 50){
			subject = "无线部数据库连接失败警告";
			sendMail_batch(mailMap_conFail,subject,50);
			COUNTER_conFail = 0;
			mailMap_conFail.clear();
		}
		if(COUNTER_notFound == 50){
			subject = "无线部文件读取失败警告";
			sendMail_batch(mailMap_notFound,subject,50);
			COUNTER_notFound = 0;
			mailMap_notFound.clear();
		}
		
	}

	public static void sendMail_batch(Map map,String subject,int MAXCOUNT){
		StringBuilder sb = new StringBuilder(1024);
		for(int i=1;i<=MAXCOUNT;i++){
			sb.append("\n\n").append("NO.").append(i).append("\n");
			sb.append(map.get(i));
		}
		
		String tos = "malin.bj@fang.com" ;
		String content = sb.toString();
		MailUtil.sendMail(content, tos,subject,"");
	}
	
	public static void sendMail(String content, String to,String subject,String cc) {
		try {
			smtp_server = "smtp.staff.soufun.com";
			String from_mail_address = username;

			Authenticator auth = new PopupAuthenticator(username, password);

			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.auth", "true");

			mailProps.put("username", username);

			mailProps.put("password", password);

			mailProps.put("mail.smtp.host", smtp_server);
			Session mailSession = Session.getDefaultInstance(mailProps, auth);
			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from_mail_address));
			//设置收件人
//			message.setRecipients(Message.RecipientType.TO, new InternetAddress(to));
			message.setRecipients(Message.RecipientType.TO, to);
			//设置抄送人
			message.addRecipients(Message.RecipientType.CC, cc);
			
			//设置邮件标题
			message.setSubject(subject, "GBK");
			
			MimeMultipart multi = new MimeMultipart();
			BodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(content);
			multi.addBodyPart(textBodyPart);
			message.setContent(multi);
			message.saveChanges();
			Transport.send(message);
		} catch (Exception ex) {
			System.err.println("邮件发送失败的原因是：" + ex.getMessage());
			System.err.println("具体错误原因：");
			ex.printStackTrace(System.err);
		}
	}
}

class PopupAuthenticator extends Authenticator {
	private String username;

	private String password;

	public PopupAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}
