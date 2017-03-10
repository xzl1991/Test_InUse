package a_小工具;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import a_生成xml.StringUtils;

public class 编码解码 {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String, String>();
		System.out.println("..."+ (map.get("sdd")==null));
//		String s="/><script >function doFind(){alert('呵呵')}</script><!-";
		String s  = "http%3A%2F%2Ftest.hbmall.rsscc.cn%2Ffe%2Fapp%2Fclient%2Fmall%2Fhtml%2Fshare-page%2Fshare.html%3Ftitle%3D%25E9%2599%2590%25E6%2597%25B6%25E7%25A7%2592%25E6%259D%2580%25EF%25BC%258C%25E7%25A6%258F%25E5%2588%25A9%25E5%25A4%25AA%25E5%25A4%259A%25E6%2580%258E%25E4%25B9%2588%25E9%2580%2589%26groupId%3D1120%26productid%3D22000031";
		String s1=null;
		s1 = URLEncoder.encode(s, "gb2312");
//		System.out.println("解码："+URLDecoder.decode(s1, "gbk"));
		System.out.println("gb 编码: "+s1);
		s1 = URLEncoder.encode(s, "utf8");
		System.out.println("utf编码: "+s1);
		s1 = URLEncoder.encode(s, "ISO-8859-1");
		System.out.println("ISO-8859-1编码: "+s1);
		s1 = URLEncoder.encode(s, "unicode");
		System.out.println("unicode编码: "+s1);
		s1 = URLEncoder.encode(s, "ascii");
		System.out.println("ascii编码: "+s1);
		s1 = URLEncoder.encode(s, "gb2312");
		System.out.println(s1);
		String[] s11 = null;
		t(s11);
		t(new String[]{"阿萨德","sdas"});
		System.out.println("解码开始");
		String code = "%c9%ee%db%da";
		String city = new String(code.toString().getBytes("UTF-8"));
		System.out.println("********"+city);
		System.out.println("utf解码:"+URLDecoder.decode(code,"UTF-8"));
		System.out.println("gbk解码:"+URLDecoder.decode(code, "gbk"));
		System.out.println("utf解码:"+URLDecoder.decode(code, "ascii"));
		System.out.println(city);
		code = "算首套房，必须算首套";
		try {
			System.out.println("decoderBase64:"+new String(decoderBase64(encoderBase64(code.getBytes()))));
			code = "%E7%AE%97%E9%A6%96%E5%A5%97%E6%88%BF%EF%BC%8C%E5%BF%85%E9%A1%BB%E7%AE%97%E9%A6%96%E5%A5%97%20";
			System.out.println("utf解码:"+ URLDecoder.decode(code,"UTF-8"));
			System.out.println("摄氏度的:"+new String(decoderBase64(code)));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Base64解密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decoderBase64(String key) throws Exception {
	    return (new BASE64Decoder()).decodeBuffer(key);
	}
	 
	/**
	 * Base64加密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encoderBase64(byte[] key) throws Exception {
	    return (new BASE64Encoder()).encodeBuffer(key);
	}
	public static void t(String...s){
		if(s==null){
			System.out.println("这是null..");
		}else{
			
			String[] s1 = null;
			if(s.length==1){
				s1 = s[0].split(",");
			}else{
				s1 = s;
			}
			for(String str:s1){
				System.out.println(str);
			}
		}
	}
}
