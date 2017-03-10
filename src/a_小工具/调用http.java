package a_小工具;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class 调用http {
	public static void main(String[] args) throws Exception{
		String s  = "黑";
		String s1=null;
		s1 = URLEncoder.encode(s, "utf-8");
		String code = "6A586341697750737755554A522F694576674D66704A657076774770496C47794C597164467770774C675446617849735375514B335132427147794949597A39434133773270534272496F5A47704942722B6A3049413D3D";
		StringBuilder sb = new StringBuilder(20);
		sb.append("http://captcha.fang.com/Verify?Answer=").append(s1);
		sb.append("&Captcha=").append(code);
		String value = StringUtils.getUrlTxt(sb.toString(), "utf8");
		System.out.println(value+"..."+value.startsWith("f"));
		System.out.println(value.startsWith("f"));
	}
}
