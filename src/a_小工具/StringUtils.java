package a_小工具;

/**
 * <p>
 * Title:  ������
 * </p>
 * <p>
 * Description: �Ի���ַ�����ַ���в�����һ������
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

	public static String getUrlTxt(String strurl, String CharSet) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		String line = null;
		try {
			URL url = new URL(strurl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(4000);
			connection.setReadTimeout(4000);
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.connect();

			String encodetype = connection.getContentEncoding();
			if (encodetype != null && encodetype.contains("gzip")) {
				GZIPInputStream gzin = new GZIPInputStream(
						connection.getInputStream());
				reader = new BufferedReader(
						new InputStreamReader(gzin, CharSet));
			} else
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), CharSet));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
		String result = sb.toString().trim();
		if (StringUtils.isNull(result)) {
			result += "<?xml version=\"1.0\"?><root/>";
		}
		if (!StringUtils.isNull(strurl.toString().trim())) {
			result = result + "\n\t" + "<!--" + strurl.toString() + "-->";
		}
		System.out.println("request:" + strurl);
		return result;

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
	 * �ж��ַ��Ƿ�Ϊ��
	 * */
	public static boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String URLEncode_GBK(String s) throws Exception {
		if (!StringUtils.isNull(s))
			s = URLEncoder.encode(s, "GBK");
		else
			s = "";
		return s;
	}

	public static String URLEncode_UTF8(String s) throws Exception {
		if (!StringUtils.isNull(s))
			s = URLEncoder.encode(s, "UTF-8");
		else
			s = "";
		return s;
	}

	/**
	 * @param map
	 *            ����ϣ�����LinkedHashMap��ʼ������֤ƴ�ӳ����Ĳ���˳��һ��
	 * @param headurl
	 *            ԭʼ�ӿ�ǰ׺
	 * @param encodeType
	 *            ��������
	 */
	public static String getUrlTextByMap(Map map, String headurl,
			String encodeType) throws Exception {
		String result = "";
		map.remove("messagename");
		map.remove("wirelesscode");
		Set set = map.keySet();
		Iterator it = set.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(headurl);
		// ��ֱ�������
		if (encodeType.toLowerCase().equals("utf-8")) {
			while (it.hasNext()) {
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
						.append(URLEncode_UTF8((String) map.get(t)));
			}
		} else {
			while (it.hasNext()) {
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
						.append(URLEncode_GBK((String) map.get(t)));
			}
		}

		String url = sb.toString().replace("?&", "?");
		result = getUrlTxt(url, encodeType);
		return result;
	}

	/**
	 * �������ͷ��ؽ���ֿ�����
	 * 
	 * @param map
	 * @param headurl
	 * @param paramEncodeType
	 *            ����ı��뷽ʽ
	 * @param returnDecodeType
	 *            ���صĽ������
	 * @return
	 * @throws Exception
	 */
	public static String getUrlTextByMap(Map map, String headurl,
			String paramEncodeType, String returnDecodeType) throws Exception {
		String result = "";
		map.remove("messagename");
		map.remove("wirelesscode");
		Set set = map.keySet();
		Iterator it = set.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(headurl);
		// ��ֱ�������
		if (paramEncodeType.toLowerCase().equals("utf-8")) {
			while (it.hasNext()) {
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
						.append(URLEncode_UTF8((String) map.get(t)));
			}
		} else {
			while (it.hasNext()) {
				String t = it.next().toString();
				sb.append("&").append(t).append("=")
						.append(URLEncode_GBK((String) map.get(t)));
			}
		}

		String url = sb.toString().replace("?&", "?");
		result = getUrlTxt(url, returnDecodeType);
		return result;
	}

	/**
	 * ��ݲ�ͬ������ʽ��ȡ������Ϣ:�����Ƕ��ε���Ϣʱ
	 * 
	 * @since 2016-04-13
	 * @param str
	 *            �����ַ
	 * @return �����key-valueֵ
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
					String key = keyValue[0].toLowerCase();
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
					key = key.toLowerCase();
					map.put(key, value);
				}
			}
		}
		return map;
	}

	/**
	 * 
	 * @author ������ ����Ҫ���ؼ򵥵Ĵ�����Ϣʱ ����ĵ�
	 * @throws Exception
	 * */
	public static String createXmlForError(String codes, String messages)
			throws Exception {
		Element root = new Element("root");
		// ����ĵ�
		Document doc = new Document(root);
		// ����userId�ڵ�
		Element timecode = new Element("TimeUse");
		timecode.addContent("0");
		Element code = new Element("code");
		// ���ýڵ�ֵ
		code.addContent(new CDATA(codes));
		// ����date�ڵ�
		Element message = new Element("message");
		message.addContent(new CDATA(messages));
		// ��ӽڵ㵽��ڵ�
		root.addContent(timecode);
		root.addContent(code);
		root.addContent(message);
		String result = StringUtils.xmltostring(doc);
		return result;
	}

	/**
	 * ��request��ȡip
	 * 
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
		// ����Ƕ༶���?��ôȡ��һ��ipΪ�ͻ�ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

	/**
	 * post�ύxml�ļ�
	 */
	public static String sendPost(String xml, String headUrl)
			throws IOException {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer buffer = null;
		try {
			URL url = new URL(headUrl);
			URLConnection con = url.openConnection();
			// ����post����
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
		} finally {
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
		}

		return buffer.toString();
	}

	/**
	 * ��xml�ĵ�ת�����ַ�
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String xmltostring(Document doc) throws Exception {
		if (doc == null)
			return "";
		// ��ʽ��xml�ļ�
		Format format = Format.getCompactFormat();
		format.setEncoding("GBK"); // ���ñ���
		format.setIndent(" ");

		XMLOutputter out = new XMLOutputter();
		out.setFormat(format);
		String result = out.outputString(doc);
		return result;
	}

	/**
	 * ��ȡ�������ܼ�
	 * 
	 * @return
	 */
	public static int getWeekDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return week;
	}

	/**
	 * @author ������ 20160331 �������Ƿ�����
	 * */
	public static boolean checkParamIsInt(String str) {
		boolean result = str.matches("[0-9]+");
		if (result == true) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @author ������ 20160331 �������Ƿ�����(����)
	 * */
	public static boolean checkParam(String... str) {
		String[] args = null;
		if (str == null) {
			return false;
		}
		if (str.length == 1) {
			args = str[0].split("-");
		} else {
			args = str;
		}
		boolean result = true;
		for (String st : args) {
			if (st.equals("")) {

			} else {
				result = st.matches("[0-9]+");
				if (result == true) {
				} else {
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * ��ȡ��һ0��ʱ��
	 * */
	public static String getMondayOfThisWeek() throws ParseException {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		Date dat0 = new SimpleDateFormat("yyyy-MM-dd").parse(df2.format(c
				.getTime()));
		return df2.format(dat0);
	}

	public static Date getYesterDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * ��ȡ��ǰΪ���� i �� ���� ��ǰ֮ǰ����
	 * */
	public static Date getDayFromToday(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, i);
		date = calendar.getTime();
		return date;
	}

	/**
	 * ��ȡ��ǰΪ���� i Сʱǰ ���� ��ǰ֮ǰ����
	 * */
	public static Date getDateByHours(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, i);
		date = calendar.getTime();
		return date;
	}

	/**
	 * @throws ParseException
	 * @category ����ʱ���
	 * 
	 * */
	public static int difTime(String from, String to) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		Date toDate = null;
		try {
			date = sdf.parse(from);
			toDate = sdf.parse(to);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long diff = toDate.getTime() - date.getTime();
		return (int) (diff / 60 / 1000);
	}

	/**
	 * 
	 * @author ������ ���xml ͨ�÷���
	 * @throws Exception
	 * */
	public static String createXml(Map<String, String> args) throws Exception {
		Element root = new Element("root");
		// ����ĵ�
		Document doc = new Document(root);
		// ����userId�ڵ�
		for (Map.Entry<String, String> entry : args.entrySet()) {
			Element node = new Element(entry.getKey());
			// ���ýڵ�ֵ
			node.addContent(new CDATA(entry.getValue()));
			// ��ӽڵ㵽��ڵ�
			root.addContent(node);
		}

		return xmltostring(doc);
	}

	/**
	 * 
	 * @author ������ ���xml ͨ�÷���
	 * @throws Exception
	 * */
	public static String createXml(String roots, Map<String, String> args)
			throws Exception {
		Element root = new Element(roots);
		// ����ĵ�
		Document doc = new Document(root);
		// ����userId�ڵ�
		for (Map.Entry<String, String> entry : args.entrySet()) {
			Element node = new Element(entry.getKey());
			// ���ýڵ�ֵ
			node.addContent(new CDATA(entry.getValue()));
			// ��ӽڵ㵽��ڵ�
			root.addContent(node);
		}

		return xmltostring(doc);
	}

	/**
	 * 
	 * 
	 * ���xml�м�� ����
	 * 
	 * @throws Exception
	 * */
	public static List<Element> createXmlnew(Map<String, Integer> args)
			throws Exception {
		List<Element> childlist = new LinkedList<Element>();
		// ����userId�ڵ�
		for (Map.Entry<String, Integer> entry : args.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				Element node = new Element(entry.getKey());
				childlist.add(node);
			}
		}

		return childlist;
	}

	/**
	 * 
	 * 
	 * ���xmlҶ�ӽڵ� ����
	 * 
	 * @throws Exception
	 * */
	public static List<Element> lastcreateXml(Map<String, String> args)
			throws Exception {
		List<Element> childlist = new LinkedList<Element>();
		// ����userId�ڵ�
		for (Map.Entry<String, String> entry : args.entrySet()) {
			Element node = new Element(entry.getKey());
			node.addContent(new CDATA(entry.getValue()));
			childlist.add(node);
		}

		return childlist;
	}

	public static String URLDecoder_GBK(String s) throws Exception {
		if (!StringUtils.isNull(s))
			s = URLDecoder.decode(s, "GBK");
		else
			s = "";
		return s;
	}

	public static String URLDecoder_UTF8(String s) throws Exception {
		if (!StringUtils.isNull(s))
			s = URLDecoder.decode(s, "UTF-8");
		else
			s = "";
		return s;
	}

	public static String getUrlTxtByGBK(String strurl) throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		System.out.println("request url: " + strurl);
		String line = null;
		try {
			URL url = new URL(strurl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.connect();
			String encodetype = connection.getContentEncoding();
			if (encodetype != null && encodetype.contains("gzip")) {
				GZIPInputStream gzin = new GZIPInputStream(
						connection.getInputStream());
				reader = new BufferedReader(new InputStreamReader(gzin, "GBK"));
			} else
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "gbk"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
		return sb.toString();
	}

	/*
	 * ���ָ����root ���doc �ĵ� *
	 */
	public static Document createXmlDoc(String roots, Map<String, String> args)
			throws Exception {
		if (roots == null || roots.equals("")) {
			roots = "root";
		}
		Element root = new Element(roots);
		// ����ĵ�
		Document doc = new Document(root);
		// ����userId�ڵ�
		for (Map.Entry<String, String> entry : args.entrySet()) {
			Element node = new Element(entry.getKey());
			// ���ýڵ�ֵ
			node.addContent(new CDATA(entry.getValue()));
			// ��ӽڵ㵽��ڵ�
			root.addContent(node);
		}

		return doc;
	}
	/**
	 * ��ȡ �ӵ�ǰ�·ݿ�ʼָ���·ݵ�һ�� 
	 * �ڵ�ǰ�·� + i ����
	 * �����ʾǰi����
	 * */
	public static String getFirstDayOfMonth(int next) {
		String firstday = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, next);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		firstday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return firstday;
	}
	/**
	 * ��ȡ �ӵ�ǰ�·ݿ�ʼָ���·����һ��
	 * �ڵ�ǰ�·� + i ����
	 * �����ʾǰi����
	 * */
	public static String getLastDayOfMonth(int next) {
		String firstday = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, next);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		firstday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return firstday;
	}
	/**
	 * ��ȡ���µ�һ��
	 * */
	public static String getFirstDayOfMonth() {
		String firstday = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		firstday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return firstday;
	}
}
