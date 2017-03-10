package a_生成xml;

/**
 * <p>
 * Title:  工具类
 * </p>
 * <p>
 * Description: 对基本的字符或是字符串进行操作的一个基类
 * </p>
 * 
 * <p>
 * Company: eu
 * </p>
 * <p>
 * Creat Date:2006-7-30
 * 
 * @version 1.0
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;



/**
 * Utility class to peform common String manipulation algorithms.
 */
public class StringUtils {

	/**
	 * Initialization lock for the whole class. Init's only happen once per
	 * class load so this shouldn't be a bottleneck.
	 */
	private static Object initLock = new Object();
	/**
	 * Used by the hash method.
	 */
	private static MessageDigest digest = null;
	private final static String LT = "&lt;";

	private final static String GT = "&gt;";

	private final static String D_QUTO = "&quot;";

	private final static String S_QUTO = "&#039;";

	private static final char[] BR_TAG = "<BR>".toCharArray();

	//地球半径
	private final static double EARTH_RADIUS = 6378137.0; 
	
	public static String  getUrlTxt(String strurl,String CharSet) {
		StringBuilder sb=new StringBuilder();
		BufferedReader reader=null;
		String line=null;
		try {
			URL url=new URL(strurl);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(4000);
			connection.setReadTimeout(4000);
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.connect();
			
			String encodetype = connection.getContentEncoding();
			if(encodetype!=null && encodetype.contains("gzip") ){
				GZIPInputStream gzin = new GZIPInputStream(connection.getInputStream());  
				reader=new BufferedReader(new InputStreamReader(gzin ,CharSet)); 
			}else
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),CharSet));		
			while ((line=reader.readLine())!=null) {
				sb.append(line).append("\n");
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		finally{
			try {
				reader.close();
			} catch (Exception e2) {
				e2.getStackTrace();
			}	
		}
		String result=sb.toString().trim();
		if (StringUtils.isNull(result))
		{
			result += "<?xml version=\"1.0\"?><root/>";
		}
//		if(!StringUtils.isNull(strurl.toString().trim())){
//			result=result+"\n\t"+"<!--"+strurl.toString()+"-->";
//		}
		System.out.println("request:"+strurl);
		return result;
		
	}
	
	/**
	 * Replaces all instances of oldString with newString in line.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replace(String line, String oldString,
			String newString) {
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuilder buf = new StringBuilder(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	
	
	/**
	 * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	 * &lt;table&gt',";, etc) and converts the '&lt'' and '&gt; ' \",\'
	 * characters to their HTML escape sequences.
	 * 
	 * @param input
	 *            the text to be converted.
	 * @return the input string with the characters '&lt;' and '&gt;' replaced
	 *         with their HTML escape sequences.
	 */
	public static final String escapeHTMLTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuilder buf = new StringBuilder(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append(LT);
			} else if (ch == '>') {
				buf.append(GT);
			} else if (ch == '\'') {
				buf.append(S_QUTO);
			} else if (ch == '"') {
				buf.append(D_QUTO);
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}


	/**
	 * 把 url 中要传输的等号 转换成 %3D
	 * 
	 * @param input
	 * @return
	 */
	public static final String escapeUrlTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuilder buf = new StringBuilder(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '=') {
				buf.append("%3D");
			} else if (ch == '\'') {
				buf.append(S_QUTO);
			} else if (ch == '"') {
				buf.append(D_QUTO);
			} else
				buf.append(ch);
		}
		return buf.toString();
	}

	/**
	 * 把',"转义成 \',\"
	 * 
	 * @param input
	 * @return
	 */
	public static final String escapeJScriptTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuilder buf = new StringBuilder(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '\'') {
				buf.append("\\'");
			} else if (ch == '"') {
				buf.append("\\\"");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	

	public synchronized static final String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. "
						+ "Jive will be unable to function normally.");
				nsae.printStackTrace();
			}
		}
		digest.update(data.getBytes());
		return toHex(digest.digest());
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p/>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param hash
	 *            an rray of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static final String toHex(byte hash[]) {
		StringBuilder buf = new StringBuilder(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if (((int) hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * Converts a line of text into an array of lower case words. Words are
	 * delimited by the following characters: , .\r\n:/\+
	 * <p/>
	 * In the future, this method should be changed to use a
	 * BreakIterator.wordInstance(). That class offers much more fexibility.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static final String[] toLowerCaseWordArray(String text) {
		StringTokenizer tokens = new StringTokenizer(text, " ,\r\n.:/\\+");
		String[] words = new String[tokens.countTokens()];
		for (int i = 0; i < words.length; i++) {
			words[i] = tokens.nextToken().toLowerCase();
		}
		return words;
	}

	/**
	 * A list of some of the most common words. For searching and indexing, we
	 * often want to filter out these words since they just confuse searches.
	 * The list was not created scientifically so may be incomplete :)
	 */
	private static final String[] commonWords = new String[] { "a", "and",
			"as", "at", "be", "do", "i", "if", "in", "is", "it", "so", "the",
			"to" };

	private static Map commonWordsMap = null;

	/**
	 * Returns a new String array with some of the most common English words
	 * removed. The specific words removed are: a, and, as, at, be, do, i, if,
	 * in, is, it, so, the, to
	 */
	public static final String[] removeCommonWords(String[] words) {
		if (commonWordsMap == null) {
			synchronized (initLock) {
				if (commonWordsMap == null) {
					commonWordsMap = new HashMap();
					for (int i = 0; i < commonWords.length; i++) {
						commonWordsMap.put(commonWords[i], commonWords[i]);
					}
				}
			}
		}
		ArrayList results = new ArrayList(words.length);
		for (int i = 0; i < words.length; i++) {
			if (!commonWordsMap.containsKey(words[i])) {
				results.add(words[i]);
			}
		}
		return (String[]) results.toArray(new String[results.size()]);
	}

	public static final String parseNormal(double in) {
		String s;
		int ilen, dlen, index;
		char ch[];
		s = Double.toString(in);
		index = s.indexOf('E');
		if (index >= 0) {
			ilen = Integer.parseInt(s.substring(index + 1));
			dlen = index - ilen - 2;
			ch = s.toCharArray();
			s = s.copyValueOf(ch, 0, 1);
			if (ilen < index - 2) {
				s += s.copyValueOf(ch, 2, ilen);
			} else {
				s += s.copyValueOf(ch, 2, index - 2);
				for (int i = 0; i < ilen - index + 2; i++) {
					s += "0";
				}
			}
			if (dlen > 0) {
				s += ".";
				s += s.copyValueOf(ch, 2 + ilen, dlen);
			}
		}
		return s;
	}

	public static final String parseNormal(int in) {
		String s = "";
		s = Integer.toString(in);
		return s;
	}

	public static final String parseNormal(long in) {
		String s = "";
		int ilen, index;
		char ch[];
		s = Long.toString(in);
		index = s.indexOf('E');
		if (index >= 0) {
			ilen = Integer.parseInt(s.substring(index + 1));
			ch = s.toCharArray();
			s = s.copyValueOf(ch, 0, 1);
			if (ilen < index - 2) {
				s += s.copyValueOf(ch, 2, ilen);
			} else {
				s += s.copyValueOf(ch, 2, index - 2);
				for (int i = 0; i < ilen - index + 2; i++) {
					s += "0";
				}
			}
		}
		return s;
	}

	/**
	 * 按分隔符号读出字符串的内容
	 * 
	 * @param strlist
	 *            含有分隔符号的字符串
	 * @param ken
	 *            分隔符号
	 * @return 列表
	 */
	public static final Vector parseStringToVector(String strlist, String ken) {
		StringTokenizer st = new StringTokenizer(strlist, ken);

		if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
			return new Vector();
		}

		int size = st.countTokens();
		Vector strv = new Vector();

		for (int i = 0; i < size; i++) {
			String nextstr = st.nextToken();
			if (!nextstr.equals("")) {
				strv.add(nextstr);
			}
		}
		return strv;
	}

	/**
	 * 按分隔符号读出字符串的内容
	 * 
	 * @param strlist
	 *            含有分隔符号的字符串
	 * @param ken
	 *            分隔符号
	 * @return 列表
	 */
	public static final ArrayList parseStringToArrayList(String strlist,
			String ken) {
		StringTokenizer st = new StringTokenizer(strlist, ken);

		if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
			return new ArrayList();
		}

		int size = st.countTokens();
		ArrayList strv = new ArrayList();

		for (int i = 0; i < size; i++) {
			String nextstr = st.nextToken();
			if (!nextstr.equals("")) {
				strv.add(nextstr);
			}
		}
		return strv;
	}
	public static boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String URLEncode_GBK(String s) throws Exception{
		if(!StringUtils.isNull(s))
			s = URLEncoder.encode(s, "GBK");
		else
			s = "";
		return s;
	}
	
	public static String URLEncode_UTF8(String s) throws Exception{
		if(!StringUtils.isNull(s))
			s = URLEncoder.encode(s, "UTF-8");
		else
			s = "";
		return s;
	}
	
	/**
	 * @param map	参数集合，请用LinkedHashMap初始化，保证拼接出来的参数顺序一致
	 * @param headurl	原始接口前缀
	 * @param encodeType	编码类型
	 */
	public static String getUrlTextByMap(Map map ,String headurl ,String encodeType) throws Exception{
		String result = "";
		map.remove("messagename");
		map.remove("wirelesscode");
		Set set = map.keySet();
		Iterator it = set.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(headurl);
		//区分编码类型
		if(encodeType.toLowerCase().equals("utf-8")){
			while(it.hasNext()){
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
					.append(URLEncode_UTF8( (String)map.get(t) ));
			}
		}else{
			while(it.hasNext()){
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
					.append(URLEncode_GBK( (String)map.get(t) ));
			}
		}
		
		String url = sb.toString().replace("?&", "?");
		result = getUrlTxt(url, encodeType);
		return result;
	}
	
	/**
	 * 参数编码和返回解码分开进行
	 * @param map
	 * @param headurl
	 * @param paramEncodeType   参数的编码方式
	 * @param returnDecodeType  返回的解码编码
	 * @return
	 * @throws Exception
	 */
	public static String getUrlTextByMap(Map map ,String headurl ,String paramEncodeType,String returnDecodeType) throws Exception{
		String result = "";
		map.remove("messagename");
		map.remove("wirelesscode");
		Set set = map.keySet();
		Iterator it = set.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(headurl);
		//区分编码类型
		if(paramEncodeType.toLowerCase().equals("utf-8")){
			while(it.hasNext()){
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
					.append(URLEncode_UTF8( (String)map.get(t) ));
			}
		}else{
			while(it.hasNext()){
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
					.append(URLEncode_GBK( (String)map.get(t) ));
			}
		}
		
		String url = sb.toString().replace("?&", "?");
		result = getUrlTxt(url, returnDecodeType);
		return result;
	}
	
	/**
	 * 根据不同的请求方式获取参数信息:当不是多层次的信息时
	 * @since 2016-04-13
	 * @param str 参数地址
	 * @return 参数的key-value值
	 * @throws Exception 
	 */
	public static Map<String, String> getMapOfParas(HttpServletRequest request)
			throws Exception {
		String getmethod = request.getMethod().toLowerCase();
		Map<String, String> map = new LinkedHashMap<String, String>();
		if ("get".equals(getmethod)) {
			String[] parameters = request.getQueryString().split("&");
			for (String parameter : parameters) {
				String[] keyValue = parameter.split("=");
				if (keyValue.length != 1) {
					String key = keyValue[0];
					String value = URLDecoder.decode(keyValue[1], "GBK");
					map.put(key, value);
				}
			}
		} else if ("post".equals(getmethod)) {
			Map<String, String[]> params = request.getParameterMap();
			for (String key : params.keySet()) {
				String[] values = params.get(key);
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					value = URLDecoder.decode(value, "GBK");
					map.put(key, value);
				}
			}
		}
		return map;
	}
	 /**
	 * 
	 * @author 许泽龙 
	 *  当需要返回简单的错误信息时
	 * 生成文档  
	 * @throws Exception 
	 * */
	public static  String createXmlForError(String codes,String messages) throws Exception{
		Element root=new Element("root");
		// 生成文档
		Document doc=new Document(root);
		// 创建userId节点
		Element code=new Element("code");
		// 设置节点值
		code.addContent(new CDATA(codes));
		// 创建date节点
		Element message=new Element("message"); 
		message.addContent(new CDATA(messages));
		// 添加节点到根节点
		root.addContent(code);
		root.addContent(message);
		String result=StringUtils.xmltostring(doc);
		return result;
	} 
	
	/**
	 * 从request获取ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 ip = request.getHeader("Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 ip = request.getHeader("WL-Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 ip = request.getRemoteAddr();  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 ip = request.getHeader("http_client_ip");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		 }  
		 // 如果是多级代理，那么取第一个ip为客户ip  
		 if (ip != null && ip.indexOf(",") != -1) {  
			 ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();  
		 }  
		 return ip;
    }
	/**
	 * 获取参数时忽略大小写
	 * @param request
	 * @param param
	 * @return param对应的参数值
	 */
	public static String getParameter(HttpServletRequest request,String param){
		Enumeration<String> params = request.getParameterNames();
	    while (params.hasMoreElements()){
	    	String str = params.nextElement();
	    	if (str.equalsIgnoreCase(param)){
	    		return request.getParameter(str);
	    	}
	    }
	    return "";
	}
	
	/**
	 * 获取头部参数时忽略大小写
	 * @param request
	 * @param param
	 * @return param对应的参数值
	 */
	public static String getHeader(HttpServletRequest request,String param){
		Enumeration<String> params = request.getHeaderNames();
		while (params.hasMoreElements()){
			String str = params.nextElement();
			if (str.equalsIgnoreCase(param)){
				return request.getHeader(str);
			}
		}
		return "";
	}
	
	public static String replaceNull(String res) {
		if (isNull(res)) {
			res = "";
		}
		return res;
	}
	
	/**
	 * json字符串转换成Map
	 * @param str
	 * @return
	 */
	public static Map<String,String> jsonToMap(String str){
        Map<String, String> map = (Map) JSONObject.toBean(JSONObject.fromObject(str), Map.class);
        return map;
	}
	
	/**
	 * Map转换成json字符串
	 * @param str
	 * @return
	 */
	public static String mapToJson(Map<String, String> map){
		JSONObject jsonObject = new JSONObject();   
		 for (Entry<String, String> entry : map.entrySet()) {
			 jsonObject.put(entry.getKey(), entry.getValue());  
	     }
		return jsonObject.toString();
	}
	
	/**
	 * 获取原始接口
	 * 可匹配到格式为<!--urlurlurlurlurl-->的串
	 * 返回值格式为(包含0个或多个)：<!--urlurlurlurlurl-->...<!--urlurlurlurlurl-->
	 * 1.在调用方法中初始化StringBuilder ysUrl = new StringBuilder();
	 * 2.调用result=getUrlTextXXXX(xxx)后，增加ysUrl.append(StringUtils.getYsUrl(result));
	 * 3.将调用方法的return result修改为result+ysUrl.toString()。
	 * 4.如果调用方法调用的其他方法functions(...)中有result=getUrlTextXXXX(xxx)，
	 *   在调用方法function之后需要做处理ysUrl.append(StringUtils.getYsUrl(result))
	 * 5.在doPost(...)方法中对result重新组装过的，
	 *   需要先保存处理前的url=StringUtils.getYsUrl(result)，
	 *   然后将url重新拼接到处理后的result
	 * @param result
	 * @return
	 */
	public static String getYsUrl(String result){
		if(result == null){
			return "";
		}
		String regex = "<!--.*-->";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(result);
		StringBuilder url = new StringBuilder();
		if(matcher.find()) {
			url.append(matcher.group());
		}
		return url.toString();
	}
	
	
	/**
	 * 根据指定节点按大小逆序调整
	 * @param eList Element队列
	 * @param elementName 节点名
	 * @return 新的队列
	 */
	public static List<Element> createTimeReverseSort(List<Element> eList, String elementName) {
		Element[] a = (Element[]) eList.toArray(new Element[eList.size()]);
		int d = a.length/2;
		while (d>0) {
			for (int i = d; i < a.length; i++) {
				int j = i-d ;
				while (j>=0&&a[j].getChildText(elementName).compareTo(a[j+d].getChildText(elementName))>0) {
					Element temp = a[j] ;
					a[j] = a[j+d] ;
					a[j+d] = temp ;
					j-=d;
				}
			}
			d /=2;
		}
		List<Element> rList = new ArrayList<Element>();
		for (int i = a.length-1; i >=0; i--) {
			rList.add(a[i]);
		}
		return rList;
	}

	/**
	 * post提交xml文件
	 */
	public static String sendPost(String xml,String headUrl) throws IOException{
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer buffer = null;
		try {
			URL url = new URL(headUrl);  
			URLConnection con = url.openConnection();  
			//发送post请求
			con.setDoOutput(true);  
			con.setDoInput(true);  
			con.setRequestProperty("accept", "*/*");  
			con.setRequestProperty("connection", "Keep-Alive"); 
			con.setRequestProperty("Pragma:", "no-cache");  
			con.setRequestProperty("Cache-Control", "no-cache");  
			con.setRequestProperty("Content-Type", "text/xml");  
			
			OutputStream outputStream = con.getOutputStream();
			outputStream.write(xml.getBytes());
			outputStream.flush();
			outputStream.close();
			
			inputStream = con.getInputStream();  
			inputStreamReader = new InputStreamReader(inputStream);  
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;  
			buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {  
			    buffer.append(new String(str.getBytes(), "GBK"));  
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			bufferedReader.close();  
		    inputStreamReader.close();  
		    // 释放资源  
		    inputStream.close();  
		    inputStream = null;
		}
		
		return buffer.toString();
	}

	/**
	 * 把xml文档转换成字符串
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String xmltostring(Document doc) throws Exception{
		if (doc == null)
			return "" ;
		// 格式化xml文件
		Format format=Format.getCompactFormat();
		format.setEncoding("GB2312");   // 设置编码
		format.setIndent(" ");
		
		XMLOutputter out =new XMLOutputter();
		out.setFormat(format);
		String result = out.outputString(doc);
		return result;
	}
	/**
	 * 获取六位随机数
	 * */
	public static String getRandomOfSix(){
		Random random = new Random();
        StringBuilder bud = new StringBuilder();
        for (int i = 0; i < 6; i++) {
           bud.append(random.nextInt(10));
        }
		return bud.toString();
	}
	
	
	
	/**
	 * 获取当天是周几
	 * @return
	 */
	public static int getWeekDay(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date()); 
		int week=cal.get(Calendar.DAY_OF_WEEK)-1;
		return week;
	}
	
	
	/**
	 * @author 许泽龙
	 * 20160331
	 *检查参数是否数字
	 * */
	public static boolean checkParamIsInt(String str) {
		boolean result=str.matches("[0-9]+");
		if(result==true){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 获取周一0点时间
	 * */
	 public static String getMondayOfThisWeek() throws ParseException {
		  Calendar c = Calendar.getInstance();
		  SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		  if (day_of_week == 0)
		   day_of_week = 7;
		  c.add(Calendar.DATE, -day_of_week + 1);
		  Date dat0 = new SimpleDateFormat("yyyy-MM-dd").parse(df2.format(c.getTime()));
		  return df2.format(dat0);
		 }
	 /**
		 * 
		 * @author 许泽龙
		 * 生成xml 通用方法
		 * @throws Exception 
		 * */
		public static  String createXml(Map<String,String> args) throws Exception{
			Element root=new Element("root");
			// 生成文档
			Document doc=new Document(root);
				// 创建userId节点
				 for(Map.Entry<String, String> entry:args.entrySet()){
					 System.out.print(entry.getKey()+"--->" +entry.getValue());
		             Element node=new Element(entry.getKey());
		 			// 设置节点值
		 			node.addContent(new CDATA(entry.getValue()));
		 			// 添加节点到根节点
		 			root.addContent(node);
				 }
			
			return xmltostring(doc);
		}
		/**
		 * 
		 * @author 许泽龙
		 * 生成xml 通用方法
		 * @throws Exception 
		 * */
		public static  String createXml(String roots,Map<String,String> args) throws Exception{
			Element root=new Element(roots);
			// 生成文档
			Document doc=new Document(root);
				// 创建userId节点
				 for(Map.Entry<String, String> entry:args.entrySet()){
					 System.out.print(entry.getKey()+"--->" +entry.getValue());
		             Element node=new Element(entry.getKey());
		 			// 设置节点值
		 			node.addContent(new CDATA(entry.getValue()));
		 			// 添加节点到根节点
		 			root.addContent(node);
				 }
			
			return xmltostring(doc);
		}
		public static  Document createXmlDoc(String roots,Map<String,String> args) throws Exception{
			Element root=new Element(roots);
			// 生成文档
			Document doc=new Document(root);
				// 创建userId节点
				 for(Map.Entry<String, String> entry:args.entrySet()){
					 System.out.print(entry.getKey()+"--->" +entry.getValue());
		             Element node=new Element(entry.getKey());
		 			// 设置节点值
		 			node.addContent(new CDATA(entry.getValue()));
		 			// 添加节点到根节点
		 			root.addContent(node);
				 }
			
			return doc;
		}
		/**
		 * 
		 * 
		 * 生成xml中间层 方法
		 * @throws Exception 
		 * */
		public static  List<Element> createXmlnew(Map<String, Integer> args) throws Exception{
			List<Element> childlist=new LinkedList<Element>();
				// 创建userId节点
				 for(Map.Entry<String, Integer> entry:args.entrySet()){
					 for(int i=0;i<entry.getValue();i++){
						 Element node=new Element(entry.getKey());
						 childlist.add(node);
					 }
				 }
			
			return childlist;
		}
		
		/**
		 * 
		 * 
		 * 生成xml叶子节点 方法
		 * @throws Exception 
		 * */
		public static  List<Element> lastcreateXml(Map<String, String> args) throws Exception{
			List<Element> childlist=new LinkedList<Element>();
				// 创建userId节点
				 for(Map.Entry<String, String> entry:args.entrySet()){
						 Element node=new Element(entry.getKey());
						 node.addContent(new CDATA(entry.getValue()));
						 childlist.add(node);
				 }
			
			return childlist;
		}
		
		public static String URLDecoder_GBK(String s) throws Exception{
			if(!StringUtils.isNull(s))
				s = URLDecoder.decode(s, "GBK");
			else
				s = "";
			return s;
		}
		
		public static String URLDecoder_UTF8(String s) throws Exception{
			if(!StringUtils.isNull(s))
				s = URLDecoder.decode(s, "UTF-8");
			else
				s = "";
			return s;
		}

		public static String getUrlTxtByGBK(String strurl) throws Exception {
		StringBuffer sb=new StringBuffer();
		BufferedReader reader=null;
		System.out.println("request url: "+strurl);
		String line=null;
		try {
			URL url=new URL(strurl);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.connect();
			System.out.println("connection time out:"+connection.getConnectTimeout());
			System.out.println("read time out:"+connection.getReadTimeout());
			String encodetype = connection.getContentEncoding();
			if(encodetype!=null && encodetype.contains("gzip") ){
				GZIPInputStream gzin = new GZIPInputStream(connection.getInputStream());  
				reader=new BufferedReader(new InputStreamReader(gzin ,"GBK")); 
			}else
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"gbk"));
			while ((line=reader.readLine())!=null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		finally{
			try {
				reader.close();
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
		return sb.toString();
	}
		/**
		 * @author xzl
		 * 
		 * */
		public String checkContent(String content){
			content = content.toLowerCase();
			content = content.replace("\r", "");
			content = content.replace("\n", "");
			content = content.replace("\0", "");
			content = content.replace(" ", "");
			System.out.println("****:"+content);
			String post = "CheckPost~全国~ask~"+content+"\0";
			System.out.println(post);
			return post;
		}
		
}















