/**
 * 
 */
package com.test.多线程;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;




/**
 * @ClassName: HtmlUtil
 * @Description: 为app生成html的工具类
 * @author yuchao
 * @param <T>
 * @Date 2015-5-20
 * 
 */
public class AppHtmlUtil<T> implements Callable<T>{
	private static final Logger logger = Logger.getLogger(AppHtmlUtil.class);	/**
	 * 将文件转换成str
	 * 
	 * @param file
	 * @return urlList 所有图片url
	 * @throws Exception
	 * @throws IOException
	 */
	private  static ExecutorService pool = Executors.newCachedThreadPool();
	private String url ;
	private String  imgNO;
	//Future 相当于是用来存放Executor执行的结果的一种容器
	public AppHtmlUtil(String src,int i) {
		// TODO Auto-generated constructor stub
		this.url = src;
		this.imgNO =Integer.toString(i);
	}

	public static String readFileToString(File file) throws Exception {

		FileInputStream in;
		String str = null;
		try {
			if (file != null) {
				in = new FileInputStream(file);
				int size = in.available();
				byte[] buffer = new byte[size];
				in.read(buffer);
				in.close();
				str = new String(buffer, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return str;
	}

	/**
	 * 处理图片并返回所有图片的url
	 * 
	 * @param imgs
	 * @return
	 * @throws Exception
	 */
	public static List<String> dealImage(Document doc) throws Exception {

		List<String> imgUrlList = new ArrayList<String>();
		List<Future<String[]>> results = new ArrayList<Future<String[]>>();
		try {
			
			if (doc != null) {
				Elements imgs = doc.select("img");
				if (imgs != null && imgs.size() > 0) {
					for (int i = 0,j=0; i < imgs.size(); i++) {

						// 给图片添加js
						// imgs.get(i).attr("onclick", "showBigPic(" + i + ")");
						// 获取图片的url
						String url = imgs.get(i).attr("src");
						if(null != url && url.contains("%")){
							url = java.net.URLDecoder.decode(url,"GBK");
						}
						
						if((null !=url && url.contains("qqface")) || "http://wpa\\.qq\\.com/pa?p=2:1130463441:52".equals(url)
								||"https://mmbiz.qlogo.cn/mmbiz/6xsuhALdNEr0ibLDATPbiaQoI0OyJzZP81jDYTckoPrfeX088lekl55f4B43DyAGgGfvtCXmEmg8KtLjtQ7yMLiaw/0".equals(url)){
							continue;
						}
						if(null != url && url.trim().endsWith(".gif")){
							continue;
						}
						
						
						// 去掉src属性
						imgs.get(i).removeAttr("src");
						// 去掉图片标签中的 alt 和 title 属性 ljj add
						imgs.get(i).removeAttr("title");
						imgs.get(i).removeAttr("alt");

						// 去掉图片的style属性
						imgs.get(i).removeAttr("style");

						// 为图片增加id属性
						imgs.get(i).attr("id", "fimg_" + j);
						
						// 获取图片的width和heigh
						String width = imgs.get(i).attr("width");
						String height = imgs.get(i).attr("height");
						
						// 如果没有width或heigh，则为其加上
						if (null!=width &&null!= height ) {
							if(!StringUtil.isEmpty(width) && !StringUtil.isEmpty(height)){
								imgUrlList.add(url);
								// 为图片添加父节点<a>
								String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
								imgs.get(i).after("<a href=" + href + ">" + imgs.get(i).toString() + "</a>");
								imgs.get(i).remove();
								j++;
							}else{
								//宽或高是空的获取宽高
								//获取宽高
								results.add(pool.submit(new AppHtmlUtil(url,i)));
							}
						}else{
							//宽或高是空的获取宽高
								//获取宽高
								results.add(pool.submit(new AppHtmlUtil(url,i)));
							
						}
					}
					//处理线程返回的结果
					move(imgs,results,imgUrlList,1);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		j=0;
		return imgUrlList;
	}

	/**
	 * 去掉所有的 a 标签 ，并保留a中的内容
	 * 
	 * @param doc
	 * @throws Exception 
	 */
	public static void removeAtag(Document doc) throws Exception {

		if (doc != null) {
			Elements alinks = doc.select("a");
			int count=0;
			if (alinks != null && alinks.size() > 0) {
				for (int i = 0; i < alinks.size(); i++) {
					//以news.开头，包含有.fang.com/，并且以id.htm结尾.则保留。最多保留5个。例子:http://news.sh.fang.com/2015-09-29/17532753.htm
					if(alinks.get(i).attr("href").trim().startsWith("http://news") && 
							alinks.get(i).attr("href").trim().contains("fang.com") &&
							alinks.get(i).attr("href").trim().endsWith(".htm") && count<5){
						//不处理
						if( !alinks.get(i).attr("href").contains("soufun://waptoapp/")){
							//编码href
							String href;
							href = java.net.URLEncoder.encode(alinks.get(i).attr("href"),"utf-8");
							alinks.get(i).attr("href","soufun://waptoapp/{\"destination\":\"webview\",\"url\":\"" + href + "\"}");
						}
						count++;
						continue;
					}else if(alinks.get(i).attr("href").trim().contains("http:") && //http://{项目域名}.fang.com/	http://shihualongyuebjcj.fang.com/ 
							alinks.get(i).attr("href").trim().endsWith("fang.com/") &&
							alinks.get(i).attr("href").trim().matches("http://([0-9a-zA-Z]*).fang.com/")
							&& count<5){
						//不处理
						if( !alinks.get(i).attr("href").contains("soufun://waptoapp/")){
							//编码href
							String href;
							href = java.net.URLEncoder.encode(alinks.get(i).attr("href"),"utf-8");
							alinks.get(i).attr("href","soufun://waptoapp/{\"destination\":\"webview\",\"url\":\"" + href + "\"}");
						}
						count++;
						continue;
					}else if(alinks.get(i).attr("href").trim().contains("www.fang.com") && count<5){//www.fang.com开头的
						//不处理
						if( !alinks.get(i).attr("href").contains("soufun://waptoapp/")){
							//编码href
							String href;
							href = java.net.URLEncoder.encode(alinks.get(i).attr("href"),"utf-8");
							alinks.get(i).attr("href","soufun://waptoapp/{\"destination\":\"webview\",\"url\":\"" + href + "\"}");
						}
						count++;
						continue;
					}
					if("p".equals(alinks.get(i).parent().tagName()) && alinks.get(i).parent().children().size() == 1 
							&& alinks.get(i).attr("href").trim().endsWith(".htm")){
						alinks.get(i).remove();
						continue;
					}
					
					String alinkHtml = alinks.get(i).toString();
					if ((alinkHtml.indexOf(">") + 1) != alinkHtml.lastIndexOf("</a>")) {
						String innerHtml = alinkHtml.substring(alinkHtml.indexOf(">") + 1, alinkHtml.lastIndexOf("</a>"));
						alinks.get(i).after(innerHtml);
					}
					alinks.get(i).remove();
				}
			}
		}

	}

	/**
	 * 去掉 html中 reg匹配的元素
	 * 
	 * @param doc : html
	 * @param regString : 正则表达式
	 */
	public static void removeMatchElements(Document doc, String regString) {

		if (doc != null) {
			Elements s_filter = doc.getAllElements();
			List<org.jsoup.nodes.Element> deleteList = new ArrayList<org.jsoup.nodes.Element>();
			int iDeleteFlag = 0;
			for (int i = 0; i < s_filter.size(); i++) {
				if (s_filter.get(i).text().matches(regString)) {
					iDeleteFlag = 1;
				}
				if (s_filter.get(i).tagName().equals("strong") && !s_filter.get(i).text().matches(regString)) {
					iDeleteFlag = 0;
				}
				if (iDeleteFlag == 1) {
					if (!s_filter.get(i).tagName().equals("strong")) {
						deleteList.add(s_filter.get(i));
					}
				}
			}
			for (org.jsoup.nodes.Element t : deleteList) {
				Elements tmp_strongs = t.select("strong");
				if (tmp_strongs == null || tmp_strongs.size() == 0) {
					t.remove();
				} else {
					for (org.jsoup.nodes.Element t_strong : tmp_strongs) {
						if (t_strong.text().matches(regString)) {
							t.remove();
						}
					}
				}
			}
			deleteList.clear();
		}
	}

	/**
	 * 处理视频并返回视频url
	 * 
	 * @param doc
	 * @throws Exception
	 * @throws MalformedURLException
	 */
	public static List<Map<String, String>> dealVideo(Document doc) throws Exception {

		List<Map<String, String>> videoList = null;
		if (doc != null) {
			Elements embeds = doc.select("embed");
			if (embeds != null && embeds.size() > 0) {
				videoList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < embeds.size(); i++) {
					Map<String, String> urlMap = new HashMap<String, String>();
					// 拿到视视频缩略图url
					String flashvars = embeds.get(i).attr("flashvars");
					String videoXml = StringUtil.getUrlTxtByUTF("http://jks.v.soufun.com/Interface/GetVideoInfoByIds.aspx?"
							+ flashvars);
					String videoImgUrl = null;
					int width = 0;
					int heigth = 0;
					if (videoXml != null && videoXml.contains("ImageSrc")) {
						videoImgUrl = videoXml.substring(videoXml.indexOf("<ImageSrc>") + 10,
								videoXml.indexOf("</ImageSrc>"));
						InputStream is = new URL(videoImgUrl).openStream();
//						BufferedImage sourceImg = ImageIO.read(is);
//						width = sourceImg.getWidth();
//						heigth = sourceImg.getHeight();
						Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(videoImgUrl.substring(videoImgUrl.lastIndexOf(".")+1,videoImgUrl.length()));
						ImageReader reader = (ImageReader) readers.next();
						ImageInputStream iis = ImageIO.createImageInputStream(is);
						reader.setInput(iis, true);
						width = reader.getWidth(0);
						heigth = reader.getHeight(0);
						is.close();
					}

					org.jsoup.nodes.Element parent = embeds.get(i).parent();
					embeds.get(i).remove();
					String parentHtml = parent.html();
					String src = "";
					if (parentHtml != null && parentHtml.trim().length() > 0) {
						if (parentHtml.contains("<!--") && parentHtml.contains("-->")) {
							src = parentHtml.substring(parentHtml.indexOf("<!--") + 4, parentHtml.indexOf("-->"));
							String href = "soufun://waptoapp/{\"destination\":\"playvideo\",\"index\":\"" + i + "\"}";
							parent.append("<a href=" + href + "></a><img type='image' id='video_" + i + "' width=" + width
									+ " height=" + heigth + ">");
							parent.attr("class", "videoBox");
						}
					}
					if(!StringUtil.isEmpty(src)){
						urlMap.put(src, videoImgUrl);
						videoList.add(urlMap);
					}
					
				}

			}
		}
		return videoList;
	}

	/**
	 * 生成固定结构的xml文件
	 * 
	 * @param msg ： 处理结果
	 * @param newscontent ： 信息
	 * @param urlList ： 图片url列表
	 * @param videoList ： 视频信息列表
	 * @return
	 */
	public static org.dom4j.Document createXml(String msg, String newscontent, List<String> urlList,
			List<Map<String, String>> videoList) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				Element imgurl = img.addElement("imageurl");
				imgurl.setText(url);
			}
		}
		// 增加视频list节点
		if (videoList != null && videoList.size() > 0) {
			Element videolist = root.addElement("videolist");
			for (Map<String, String> urlMap : videoList) {
				Element video = videolist.addElement("video");
				Element videourl = video.addElement("videourl");
				Element thumburl = video.addElement("thumburl");
				for (String key : urlMap.keySet())
				{
					videourl.setText(key.trim());
					thumburl.setText(urlMap.get(key).trim());
				}
			}
		}
		return document;
	}

	/**
	 * 生成固定结构的xml文件
	 *
	 * @param msg ： 处理结果
	 * @param newscontent ： 信息
	 * @param urlList ： 图片url列表
	 * @param videoList ： 视频信息列表
	 * @return
	 */
	public static org.dom4j.Document createXmlExtend(String msg, String newscontent, List<String> urlList,
											   List<Map<String, String>> videoList,Element nodeElement  ) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				Element imgurl = img.addElement("imageurl");
				imgurl.setText(url);
			}
		}
		// 增加视频list节点
		if (videoList != null && videoList.size() > 0) {
			Element videolist = root.addElement("videolist");
			for (Map<String, String> urlMap : videoList) {
				Element video = videolist.addElement("video");
				Element videourl = video.addElement("videourl");
				Element thumburl = video.addElement("thumburl");
				for (String key : urlMap.keySet())
				{
					videourl.setText(key.trim());
					thumburl.setText(urlMap.get(key).trim());
				}
			}
		}
		if (nodeElement != null)
		{
			root.add((Element)nodeElement.clone());
		}
		return document;
	}
	public static org.dom4j.Document createXmlExtend(String msg, String newscontent, List<String> urlList,
													 List<Map<String, String>> videoList,Map<String,String> mElement  ) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				Element imgurl = img.addElement("imageurl");
				imgurl.setText(url);
			}
		}
		// 增加视频list节点
		if (videoList != null && videoList.size() > 0) {
			Element videolist = root.addElement("videolist");
			for (Map<String, String> urlMap : videoList) {
				Element video = videolist.addElement("video");
				Element videourl = video.addElement("videourl");
				Element thumburl = video.addElement("thumburl");
				for (String key : urlMap.keySet())
				{
					videourl.setText(key.trim());
					thumburl.setText(urlMap.get(key).trim());
				}
			}
		}
		for (String key : mElement.keySet())
		{
			Element nodename = root.addElement(key);
			nodename.setText(mElement.get(key));
		}
		return document;
	}

	/**
	 * 格式化table标签中的img
	 * 
	 * @param doc
	 */
	public static void formatTableImgs(Document doc) {

		Elements tableImgs = doc.select("table");
		for (int i = 0; i < tableImgs.size(); i++) {
			Elements tmp = tableImgs.get(i).select("img");
			if(tmp.size() ==1){
				StringBuffer sb = new StringBuffer();
				sb.append("<p align='center'>");
				sb.append(tmp.get(0));
				sb.append("</p>");
				tableImgs.get(i).after(sb.toString());
				tableImgs.get(i).remove();
			}
			else if (tmp.size() == 2)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("<div class=\"imgGrid\">");
				sb.append("<ul class=\"pic-2\">");
				sb.append("<li>" + tmp.get(0) + "</li>");
				sb.append("<li>" + tmp.get(1) + "</li>");
				sb.append("</ul></div>");
				tableImgs.get(i).after(sb.toString());
				tableImgs.get(i).remove();
			} else if (tmp.size() == 3)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("<div class=\"imgGrid\">");
				sb.append("<ul class=\"pic-3\">");
				sb.append("<li>");
				sb.append("<div>" + tmp.get(0) + "</div>");
				sb.append("<div>" + tmp.get(1) + "</div>");
				sb.append("</li>");
				sb.append("<li>" + tmp.get(2) + "</li>");
				sb.append("</ul></div>");
				tableImgs.get(i).after(sb.toString());
				tableImgs.get(i).remove();
			} else if (tmp.size() == 4)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("<div class=\"imgGrid\">");
				sb.append("<ul class=\"pic-4\">");
				sb.append("<li>" + tmp.get(0) + "</li>");
				sb.append("<li>" + tmp.get(1) + "</li>");
				sb.append("<li>" + tmp.get(2) + "</li>");
				sb.append("<li>" + tmp.get(3) + "</li>");
				sb.append("</ul></div>");
				tableImgs.get(i).after(sb.toString());
				tableImgs.get(i).remove();
			}
		}

	}
	
	/**
	 * 取得导购头
	 * @param content
	 * @return
	 */
	public static String getDaogouHead(String content){
		String head = null;
		if(null != content){
			Document doc = Jsoup.parse(content);
			Elements pASpans = doc.select("a > span[style]");
			if(null != pASpans && pASpans.size() >0){
				for(int i=0;i<pASpans.size();i++){
					if(pASpans.get(i).attr("style").toString().contains("微软雅黑")){
						if("p".equals(pASpans.get(i).parent().parent().tagName())){
							head = "<p>" + doc.toString().substring(doc.toString().indexOf("<body>")+6,doc.toString().indexOf(pASpans.get(i).parent().toString()));
						}else{
							head = doc.toString().substring(doc.toString().indexOf("<body>")+6,doc.toString().indexOf(pASpans.get(i).parent().toString()));
						}
						break;
					}
				}
			}
			
			//另一种格式
			if(head == null){
				Elements loupanEs = doc.select("font[size=\"5\"]");
				if(null != loupanEs && loupanEs.size() > 0){
					for(int i=0;i<loupanEs.size();i++){
						if(loupanEs.get(i).hasText() && loupanEs.get(i).text().contains("NO.")){
							if("p".equals(loupanEs.get(i).parent().tagName())){
								head = doc.toString().substring(doc.toString().indexOf("<body>")+6,doc.toString().indexOf(loupanEs.get(i).parent().toString()));
							}else{
								head = doc.toString().substring(doc.toString().indexOf("<body>")+6,doc.toString().indexOf(loupanEs.get(i).toString()));
							}
							break;
						}
					}
					
				}
			}
			if(null != head && head.contains("<hr")){
				head = head.substring(0,head.lastIndexOf("<hr"));
			}else if(null != head && head.contains("<!--subtitle")){
				head = head.substring(0,head.indexOf("<!--subtitle"));
			}
			
		}
		return head;
	}
	/**
	 * 获取楼盘newcode
	 * @param loupan
	 * @return
	 */
	public static String getLoupanNewcode(String loupan){
		return StringUtil.getRegGroupContentByIndex("<!--newcode:([\\d]*?)-->", loupan, 1);
	}
	
	/**
	 * 获取所有楼盘信息
	 * @param sContent
	 * @return
	 */
	public static String[] getLoupanList(String sContent){
		//判断是否有房源
		if(null != sContent){
			Document content = Jsoup.parse(sContent);
			Elements checkLoupans = content.select("p > a > span[style]");
			if(null == checkLoupans || checkLoupans.size() == 0){
				checkLoupans = content.select("a > font > font[face]");
			}
			if(null == checkLoupans || checkLoupans.size() == 0){
				return null;
			}
		}
		String[] loupans =  sContent.split("<hr[\\s]{0,}class=\"cms_pager_tag\"[\\s]{0,}[/]{0,1}>");
		
		//判断是否是楼盘的信息
		if(loupans != null && loupans.length >0){
			for(int i =0;i<loupans.length;i++){
				Document loupanDoc = Jsoup.parse(loupans[i]);
				Elements loupanFlag = loupanDoc.select("a > span[style]");
				//分离第一个房源与导购头
				boolean hasLoupan = false;
				if(null != loupanFlag && loupanFlag.size() >0){
					for(int j=0;j<loupanFlag.size();j++){
						if(loupanFlag.get(j).attr("style").toString().contains("微软雅黑")){
							if(null != loupans[0] && i== 0){
								//获取楼盘id
								String newcode = null;
								if(loupans[0].contains("<!--newcode")){
									newcode = loupans[0].substring(loupans[0].indexOf("<!--newcode"),loupans[0].indexOf("<!--newcode")+25);
								}else if(loupans[0].contains("<!--tuangoubaoming")){
									newcode = "<!--newcode:" + loupans[0].substring(loupans[0].indexOf("<!--tuangoubaoming")+19,loupans[0].indexOf("<!--tuangoubaoming")+29) + "-->";
								}
								if(loupans[0].contains("<!--subtitle")){
									loupans[0] = "<p>" + newcode + loupans[0].substring(loupans[0].indexOf("<!--subtitle"),loupans[0].length());
								}else{
									if("p".equals(loupanFlag.get(j).parent().parent().tagName())){
										loupans[0] =newcode + "<p>" + loupanDoc.toString().substring(loupanDoc.toString().indexOf(loupanFlag.get(j).parent().toString()),loupanDoc.toString().lastIndexOf("</body>"));
									}else{
										loupans[0] = newcode + loupanDoc.toString().substring(loupanDoc.toString().indexOf(loupanFlag.get(j).parent().toString()),loupanDoc.toString().lastIndexOf("</body>"));
									}
								}
							}
							hasLoupan = true;
							break;
						}
					}
				}else{
					loupanFlag = loupanDoc.select("font[size=\"5\"]");
					if(null != loupanFlag && loupanFlag.size()>0){
						for(int j=0;j<loupanFlag.size();j++){
							if(loupanFlag.get(j).hasText() && loupanFlag.get(j).text().contains("NO.")){
								//如果是第一个是房源的需要分离楼盘头与房源信息
								if(null != loupans[0] && i == 0){
									if(loupans[0].contains("<!--subtitle")){
										loupans[0] = "<p>" + loupans[0].substring(loupans[0].indexOf("<!--subtitle"),loupans[0].length());
									}else{
										String newcode = "";
										if(loupans[0].contains("<!--newcode")){
											newcode = loupans[0].substring(loupans[0].indexOf("<!--newcode"),loupans[0].indexOf("-->")+3);
										}
										if("p".equals(loupanFlag.get(j).parent().tagName())){
											loupans[0] = newcode + "<p>"+loupanDoc.toString().substring(loupanDoc.toString().indexOf(loupanFlag.get(j).toString()),loupanDoc.toString().lastIndexOf("</body>"));
										}else{
											loupans[0] = newcode + loupanDoc.toString().substring(loupanDoc.toString().indexOf(loupanFlag.get(j).toString()),loupanDoc.toString().lastIndexOf("</body>"));
										}
									}
								}
								hasLoupan = true;
								break;
							}
						}
						
					}else {
						loupans[i] = null;
					}
				}
				if(!hasLoupan){
					loupans[i] = null;
				}
			}
		}
		return loupans;
	}
	
	/**
	 * 获取楼盘名称
	 * @param loupan
	 * @return
	 */
	public static String getLoupanName(Document louPanDoc){
		String loupanName = null;
		if(null != louPanDoc){
			Elements pASpans = louPanDoc.select("a > span[style]");
			//楼盘名称
			if(null != pASpans && pASpans.size() >0){
				for(int i = 0;i<pASpans.size();i++){
					if(null != pASpans.get(i).attr("style") && pASpans.get(i).attr("style").toString().contains("微软雅黑")){
						loupanName = pASpans.get(i).text();
						break;
					}
				}
			}
			//另一种格式
			if(null == loupanName){
				Elements loupanEs = louPanDoc.select("p > a > font[face]");
				if(null == loupanEs || loupanEs.size() == 0){
					loupanEs = louPanDoc.select("p > font > a > font[face]");
				}
				if(null == loupanEs || loupanEs.size() == 0){
					loupanEs = louPanDoc.select("a > font > font[face]");
				}
				if(null != loupanEs && loupanEs.size() > 0){
					for(int i =0;i<loupanEs.size();i++){
						if(null != loupanEs.get(i).attr("face") && loupanEs.get(i).attr("face").toString().contains("微软雅黑")){
							loupanName = loupanEs.get(i).text();
							break;
						}
					}
				}
			}
		}
		return loupanName;
		
	}
	
	/**
	 * 获取楼盘电话
	 * @param louPanDoc
	 * @return
	 */
	public static String getLoupanTelephone(Document louPanDoc){
		String loupanTelephone = null;
		if(null != louPanDoc){
			Elements pASpans = louPanDoc.select("a > span[style]");
			//楼盘名称
			if(null != pASpans && pASpans.size() >0){
				for(int i = 0;i<pASpans.size();i++){
					//找到楼盘名称的span
					if(null != pASpans.get(i).attr("style") && pASpans.get(i).attr("style").toString().contains("微软雅黑")){
						if(null != pASpans.get(i).parent().nextElementSibling() && pASpans.get(i).parent().nextElementSibling().hasText()){
							loupanTelephone = pASpans.get(i).parent().nextElementSibling().text();
							break;
						}
					}
				}
			}
			
			//另一种电话格式
			if(null == loupanTelephone){
				Elements telephoneEs = louPanDoc.select("strong");
				if(null != telephoneEs && telephoneEs.size() > 0){
					for(int i = 0;i<telephoneEs.size();i++){
						if(telephoneEs.get(i).hasText() && telephoneEs.get(i).text().contains("转")){
							loupanTelephone = telephoneEs.get(i).text();
							break;
						}
					}
				}
			}
			
			if(null != loupanTelephone && loupanTelephone.trim().length() > 0){
				if(loupanTelephone.contains("转")){
					loupanTelephone = loupanTelephone.replace("转", ",");
				}
				loupanTelephone = loupanTelephone.replaceAll(" ", "");
			}
		}
		return loupanTelephone;
		
	}
	/**
	 * 获取楼盘头信息
	 * @param doc
	 * @return
	 */
	public static String getLoupanHeader(String loupan){
		String header = null;
		if(null != loupan){
			if(loupan.contains("<!--mingpian sta-->")){
				header = StringUtil.getRegGroupContentByIndex("([\\s\\S]*?)(?=<!--mingpian sta-->)", loupan, 1);
			}else{
				Document doc = Jsoup.parse(loupan);
				Elements tables = doc.select("table");
				if(null != tables){
					for(int i=0;i<tables.size();i++){
						if(tables.get(i).toString().contains("基本信息")){
							header = doc.toString().substring(doc.toString().indexOf("<body>")+6,doc.toString().indexOf(tables.get(i).toString().substring(0, tables.get(i).toString().indexOf(">"))));
						}
					}
				}
			}
		}
		return header;
	}
	
	/**
	 * 获取楼盘名片
	 * @param doc
	 * @return
	 */
	public static String getLoupanTitsFromTableText(String louPan){
		String loupanMingPian = StringUtil.getRegGroupContentByIndex("(<!--mingpian sta-->[\\s\\S]*?<!--mingpian end-->)",louPan,1);
		if(null == loupanMingPian || loupanMingPian.trim().length() == 0){
			Document louPanDoc = Jsoup.parse(louPan);
			Elements tables = louPanDoc.select("table");
			for(org.jsoup.nodes.Element table : tables){
				Elements trs = table.select("tr");
				if(trs != null && trs.size() >0){
					if(trs.get(0).select("td > a").text().contains("基本信息")){
						loupanMingPian = table.toString();
					}
				}
				
			}
		}
		Document doc = Jsoup.parse(loupanMingPian);
		Elements es = doc.select("table");
		StringBuffer sb = new StringBuffer();
		List<String> lRet = new ArrayList<String>();
		StringBuffer louPanTit = new StringBuffer();
		for (org.jsoup.nodes.Element e : es){
			Elements trs = e.select("tr");
			//第一个tr里面是"XXX基本信息"
			for (int j = 1;j< trs.size();j++){
				Elements tds = trs.get(j).select("td");
				for (int i = 1; i < tds.size(); i = i + 2){
					if ((i + 1) % 2 == 0){
						sb.append(tds.get(i - 1).text());
						sb.append(":");
						sb.append(tds.get(i).text());
						lRet.add("<p>" + sb.toString() + "</p>");
						sb.setLength(0);
					}
				}
			}
		}
		if(lRet != null && lRet.size() > 0){
			for(String p : lRet){
				louPanTit.append(p);
			}
		}
		return louPanTit.toString();
	}
	
	/**
	 * 获取楼盘body信息
	 * @param loupan
	 * @return
	 */
	private static String getLoupanBodyInfos(String loupan){
		return StringUtil.getRegGroupContentByIndex("<!--mingpian end-->([\\s\\S]*)", loupan, 1);
	}
	
	/**
	 * 获取楼盘bodyHeader信息
	 * @param loupan
	 * @return
	 */
	
	public static String getLoupanBodyHeader(String loupan){
		String loupanBodyInfos = getLoupanBodyInfos(loupan);
		if(null!=loupanBodyInfos && loupanBodyInfos.length()>0 && loupanBodyInfos.contains("<strong>")){
			return StringUtil.getRegGroupContentByIndex("([\\s\\S]*?)(?=<p>[\\s]{0,}<strong>)", loupanBodyInfos, 1);
		}else{
			return "";
		}
		
	}
	
	/**
	 * 获取楼盘bodyItem列表
	 * @param loupan
	 * @return
	 */
	public static  List<String> getLoupanBodyItems(String loupan){
		String loupanBodyInfos = getLoupanBodyInfos(loupan);
		loupanBodyInfos = loupanBodyInfos+"<p><strong>11111</strong></p>";
		return  StringUtil.getRegGroupContents("(<p>[\\s]{0,}<strong>.*</strong>[\\s]{0,}</p>[\\s\\S]*?)(?=<p>[\\s]{0,}<strong>.*</strong>[\\s]{0,}</p>)",loupanBodyInfos,1);
	}
	
	
	/**
	 * 给楼盘图片加上 loupan属性
	 * @param info
	 * @param loupanHtml
	 * @return
	 */
	public static String nameLoupanImg(String louPanName,String loupanHtml){
		if(loupanHtml != null && louPanName != null){
			Document loupanDoc = Jsoup.parse(loupanHtml);
			Elements imgs = loupanDoc.select("img");
			if(imgs != null){
				for(int i = 0;i<imgs.size();i++){
					imgs.get(i).attr("loupan",louPanName);
				}
			}
			loupanHtml = loupanDoc.toString().substring(loupanDoc.toString().indexOf("<body>"),loupanDoc.toString().indexOf("</body>"));
		}
		
		return loupanHtml;
		
		
	}
	
	/**
	 * 处理导购图片
	 * @param loupanHtml
	 * @return
	 * @throws Exception 
	 */
	public static List<String> dealDaogouImg(Document doc) throws Exception{
		long start = System.currentTimeMillis();
		List<String> imgUrlList = new ArrayList<String>();
		List<Future<String[]>> results = new ArrayList<Future<String[]>>(); 
		try {
			if (doc != null) {
				Elements imgs = doc.select("img");
				if (imgs != null && imgs.size() > 0) {
					for (int i = 0,j=0; i < imgs.size(); i++) {
						
						// 获取图片的url
						String url = imgs.get(i).attr("src");
						
						if(null != imgs.get(i).attr("alt") && imgs.get(i).attr("alt").contains("点评")){
							imgs.get(i).remove();
							continue;
						}
						// 获取图片的width和heigh
						String width = imgs.get(i).attr("width");
						String height = imgs.get(i).attr("height");
						
						
						//获取楼盘属性
						String loupan = imgs.get(i).attr("loupan");
						if(loupan != null && loupan.trim().length() != 0){
							url = url + "#" + loupan;
						}
						
						// 去掉src属性
						imgs.get(i).removeAttr("src");
						// 去掉图片标签中的 alt 和 title 属性 
						imgs.get(i).removeAttr("title");
						imgs.get(i).removeAttr("alt");

						// 去掉图片的style属性
						imgs.get(i).removeAttr("style");
						
						// 为图片增加id属性
						imgs.get(i).attr("id", "fimg_" + j);
						

						// 如果没有width或heigh，则为其加上
						if (null!=width  && null!=height ) {
							if(!StringUtil.isEmpty(width)&&!StringUtil.isEmpty(height)){
								if(!"0".equals(height) && !"NaN".equals(height) && Integer.valueOf(width)/Integer.valueOf(height) > 3){
									imgs.get(i).remove();
									continue;
								}
								imgUrlList.add(url);
								// 为图片添加父节点<a>
								String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
								imgs.get(i).after("<a href=" + href + ">" + imgs.get(i).toString() + "</a>");
								imgs.get(i).remove();
								j++;
							}else{
								//宽或高是空的获取宽高
								//获取宽高
								results.add(pool.submit(new AppHtmlUtil(url,i)));
							}
						}else{
							//宽或高是空的获取宽高
							//获取宽高
							results.add(pool.submit(new AppHtmlUtil(url,i)));
						}
						
					}
					
					//处理线程返回的结果
					move(imgs,results,imgUrlList,1);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		long endtime = System.currentTimeMillis();
		logger.info("daogou  end imgtime use:" + (endtime-start) + " ms");
		j=0;
		return imgUrlList;
		
	}
	
	/**
	 * 给楼盘视频加上楼盘属性
	 * @param loupan
	 * @param loupanName
	 * @return
	 * @throws Exception
	 */
	public static String nameLoupanVideo(String loupanName,String loupan) throws Exception{
		if(null != loupan && null != loupanName){
			Pattern p = Pattern.compile("<!--vimgurl.*-->([\\s\\S]*?)<!--hqVedioFileUrl_wap.*-->");
			Matcher m = p.matcher(loupan);
			while(m.find()){
				String videoStr = m.group();
				String imgUrl = videoStr.substring(videoStr.indexOf("<!--vimgurl") +12, videoStr.indexOf("-->"));
				String videoUrl = videoStr.substring(videoStr.indexOf("<!--hqVedioFileUrl_wap") +23, videoStr.lastIndexOf("-->"));
				
				//获取图片宽高
				InputStream is = new URL(imgUrl).openStream();
				Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imgUrl.substring(imgUrl.lastIndexOf(".")+1,imgUrl.length()));
				ImageReader reader = (ImageReader) readers.next();
				ImageInputStream iis = ImageIO.createImageInputStream(is);
				reader.setInput(iis, true);
				int width = reader.getWidth(0);
				int heigth = reader.getHeight(0);
				is.close();
				//换成视频标签
				loupan = loupan.replace(videoStr, "<p align='center' class='videoBox'><video videourl='" +videoUrl+ "' imgurl='"+imgUrl + "' width=" +width+ " height=" + heigth +" loupan='" + loupanName+"'>");
			}
		}
		
		return loupan;
		
	}
	
	/**
	 * 处理导购视频并返回视频的相关信息列表
	 * @param doc
	 * @return
	 */
	public static List<String> dealDaogouVideo(Document doc){
		List<String> videoList = null;
		if(null != doc){
			videoList = new ArrayList<String>();
			Elements videos = doc.select("video");
			if(null != videos){
				for(int i =0;i <videos.size();i++ ){
					String videoUrl = videos.get(i).attr("videourl");
					String imgUrl = videos.get(i).attr("imgurl");
					String loupan = videos.get(i).attr("loupan");
					String width = videos.get(i).attr("width");
					String height = videos.get(i).attr("height");
					String href = "soufun://waptoapp/{\"destination\":\"playvideo\",\"index\":\"" + i + "\"}";
					videos.get(i).after("<a href=" + href + "></a><img type='image' id='video_" + i + "' width=" + width
							+ " height=" + height + ">");
					videos.get(i).remove();
					if(null != loupan && loupan.trim().length() > 0){
						videoList.add(videoUrl + "#" + imgUrl + "#" + loupan);
					}else{
						videoList.add(videoUrl + "#" + imgUrl);
					}
				}
			}
		}
		return videoList;
		
	}
	
	/**
	 * 去掉导购的链接
	 * @param doc
	 */
	public static void removeDaogouAtag(Document doc){
		if(null != doc){
			Elements as = doc.select("a");
			List listRemoveParent=new ArrayList();
			if(null != as){
				for(int i=0;i<as.size();i++){
					if(("p".equals(as.get(i).parent().tagName()) || "strong".equals(as.get(i).parent().tagName()) || "font".equals(as.get(i).parent().tagName()))
							&& as.get(i).attr("href").trim().endsWith(".htm")){
						if(as.get(i).hasText() && as.get(i).text().contains(".htm")){
							if(as.get(i).parent()!=null){
								if(listRemoveParent.contains(as.get(i).parent())){
									continue;
								}
								listRemoveParent.add(as.get(i).parent());
								as.get(i).parent().remove();
							}else{
								as.get(i).remove();
							}
							continue;
						}
					}
					
					if(as.get(i).hasText()){
						if(as.get(i).text().contains("点击") || as.get(i).text().contains("查看") 
								|| as.get(i).text().contains("点此") || as.get(i).text().contains("报名") 
								|| as.get(i).text().contains("我要算房贷") || as.get(i).text().contains("更多在售二手房")
								|| as.get(i).text().contains("http://") || as.get(i).text().matches("更多.*?推荐")){
							as.get(i).remove();
							continue;
						}
					}
					
					if("li".equals(as.get(i).parent().tagName()) || (null != as.get(i).children() && as.get(i).children().size() ==1 && "i".equals(as.get(i).child(0).tagName())) || "h2".equals(as.get(i).parent().tagName())){
						//不处理
					}else if("telephone".equals(as.get(i).attr("type"))){
						//不处理
					}else{
						String alinkHtml = as.get(i).toString();
						if ((alinkHtml.indexOf(">") + 1) != alinkHtml.lastIndexOf("</a>")) {
							String innerHtml = alinkHtml.substring(alinkHtml.indexOf(">") + 1, alinkHtml.lastIndexOf("</a>"));
							as.get(i).after(innerHtml);
						}
						as.get(i).remove();
					}
				}
			}
			
		}
	}
	
	/**
	 * 将所有400-800-9000转1234换成电话协议
	 * @param content
	 */
	public static String addTelephoneProtocol(String content){
		if(null != content){
//			Pattern p = Pattern.compile("(\\d{3}-\\d{3}-\\d{4})([\\s\\S]*?)(转)([\\s\\S]*?)(\\d{6})");
			Pattern p = Pattern.compile("(\\d{3}-\\d{3}-\\d{4})([\\s]*?)(转)([\\s]*?)(\\d{6})");

			Matcher m = p.matcher(content);
			while(m.find()){
				String telephone = null;
				if(m.groupCount() == 1){
					telephone = m.group(1).replace("转", ",").trim();
				}else{
					telephone = m.group(1) +","+ m.group(m.groupCount());
				}
				String showTel = m.group();
				content = content.replace(showTel, "<a type='telephone' href='soufun://waptoapp/{&quot;destination&quot;:&quot;openDial&quot;,&quot;phoneNum&quot;:&quot;"+telephone+"&quot;}'>"+showTel+"</a>");
			}
			
		}
		return content;
	}
	
	/**
	 * 去掉导购的空白p标签
	 * @param doc
	 */
	public static void removeDaogouBlankPTag(Document doc){
		Elements fonts = doc.select("font");
		if(null != fonts && fonts.size() >0){
			for(int i =0;i<fonts.size();i++){
				if(fonts.get(i).html().contains("<!--subtitle") || fonts.get(i).html().contains("<!--newcode")){
					if(StringUtil.isEmpty(fonts.get(i).text())){
						fonts.get(i).remove();
						continue;
					}
				}
				if(null != fonts.get(i).children() && fonts.get(i).children().size()>0){
					if("br".equals(fonts.get(i).children().get(0).tagName())){
						fonts.get(i).children().get(0).remove();
					}
				}
			}
		}
		//去掉空白的p标签
		Elements ps = doc.select("p");
		for(int i = 0;i<ps.size();i++){
			//将所有的&nbsp;替换掉
			ps.get(i).html(ps.get(i).html().replace("&nbsp;", ""));
			
			//去掉p的首位的<br>
			if(null != ps.get(i).children() && ps.get(i).children().size() >0){
				if(ps.get(i).children().size() == 1 && "br".equals(ps.get(i).children().get(0).tagName())){
					ps.get(i).children().get(0).remove();
				}else if(ps.get(i).children().size() > 1){
					if("br".equals(ps.get(i).children().get(0).tagName())){
						ps.get(i).children().get(0).remove();
					}
					if("br".equals(ps.get(i).children().get(ps.get(i).children().size()-1).tagName())){
						ps.get(i).children().get(ps.get(i).children().size()-1).remove();
					}
				}
			}
			if((ps.get(i).children() == null || ps.get(i).children().size() == 0) && ps.get(i).hasText()){
				if(":".equals(ps.get(i).text().trim()) || "：".equals(ps.get(i).text().trim())){
					ps.get(i).remove();
					continue;
				}
				//只有空格和换行的p
				ps.get(i).html(ps.get(i).html().replaceAll("\\s\\r\\n", ""));
			}
			//空白的p
			if((ps.get(i).children() == null || ps.get(i).children().size() == 0) && !ps.get(i).hasText()){
				ps.get(i).remove();
				continue;
			}
			if(ps.get(i).html().contains("<!--") && StringUtil.isEmpty(ps.get(i).text()) && !ps.get(i).html().contains("<img")){
				ps.get(i).remove();
			}
		}
		
	}
	
	/**
	 * 去掉导购中正则匹配的标签
	 * @param reg
	 * @param doc
	 */
	public static void removeDaogouMatchTag(String reg,Document doc){
		if(null != doc){
			Elements fonts = doc.select("p > strong > font");
			if(null != fonts && fonts.size() > 0){
				for(int i=0;i<fonts.size();i++){
					if(fonts.get(i).hasText() && fonts.get(i).text().matches(reg)){
						while(null != fonts.get(i).parent().parent().nextElementSibling() && "p".equals(fonts.get(i).parent().parent().nextElementSibling().tagName())
								&& null != fonts.get(i).parent().parent().nextElementSibling().children()
								&& fonts.get(i).parent().parent().nextElementSibling().children().size() == 1
								&& "a".equals(fonts.get(i).parent().parent().nextElementSibling().children().get(0).tagName())){
							fonts.get(i).parent().parent().nextElementSibling().remove();
						}
						fonts.get(i).parent().parent().remove();
					}
				}
			}
			
			Elements ps = doc.select("p");
			if(null != ps && ps.size() > 0){
				for(int i=0;i<ps.size();i++){
					if(ps.get(i).hasText() && ps.get(i).text().matches(reg)){
						while(null != ps.get(i).nextElementSibling() && "p".equals(ps.get(i).nextElementSibling().tagName())
								&& null != ps.get(i).nextElementSibling().children() && ps.get(i).nextElementSibling().children().size() > 0 
								&& "a".equals(ps.get(i).nextElementSibling().children().get(0).tagName()) && !ps.get(i).nextElementSibling().html().contains("<img")){
							ps.get(i).nextElementSibling().remove();
						}
						ps.get(i).remove();
					}
				}
			}
		}
	}
	
	/**
	 * 生成导购xml文件
	 * 
	 * @param msg ： 处理结果
	 * @param newscontent ： 信息
	 * @param urlList ： 图片url列表
	 * @param videoList ： 视频信息列表
	 * @return
	 */
	public static org.dom4j.Document createDaogouXml(String msg, String newscontent, List<String> urlList,
			List<String> videoList,String curLoupan,String curLoupanNewcode,String city) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				String[] urlAndName = url.split("#");
				if(urlAndName.length == 1){
					Element imgurl = img.addElement("imageurl");
					imgurl.setText(url);
				}else{
					Element imgurl = img.addElement("imageurl");
					Element loupan = img.addElement("loupanname");
					loupan.setText(urlAndName[1]);
					imgurl.setText(urlAndName[0]);
				}
				
				
			}
		}
		// 增加视频list节点
		if (videoList != null && videoList.size() > 0) {
			Element videolist = root.addElement("videolist");
			for(String videoStr : videoList){
				Element video = videolist.addElement("video");
				Element videourl = video.addElement("videourl");
				Element thumburl = video.addElement("thumburl");
				String[] videoArg = videoStr.split("#");
				if(videoArg.length > 1){
					videourl.setText(videoArg[0]);
					thumburl.setText(videoArg[1]);
				}
			}
		}
		//当前楼盘节点
		if(null != curLoupan){
			Element curloupan = root.addElement("curloupan");
			curloupan.setText(curLoupan);
		}
		//newcode
		Element newcode = root.addElement("newcode");
		newcode.setText(curLoupanNewcode);
		Element cityNode = root.addElement("city");
		cityNode.setText(city);
		
		return document;
	}
	public static org.dom4j.Document createProductionDescriptionXml(String msg, String newscontent) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);
		return document;
	}
	
	/**
	 * 生成论坛xml
	 * @param msg
	 * @param newscontent
	 * @param urlList
	 * @param pageNo
	 * @return
	 */
	public static org.dom4j.Document createXmlForLuntan(String msg, String newscontent, List<String> urlList,Map<String,String> luntanParam){
		
		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				Element imgurl = img.addElement("imageurl");
				imgurl.setText(url);
			}
		}
		
		String pageNo = luntanParam.get("pageNo");
		//生成pageno节点
		if(null != pageNo){
			Element pageno = root.addElement("pagenum");
			pageno.setText(pageNo);
		}
		
		String topic = luntanParam.get("topic");
		if(null != topic){
			Element topicEle = root.addElement("topic");
			topicEle.setText(topic);
		}
		
		String sign = luntanParam.get("sign");
		if(null != sign){
			Element signEle = root.addElement("sign");
			signEle.setText(sign);
		}
		
		String masterId = luntanParam.get("masterId");
		if(null != masterId){
			Element masterIdEle = root.addElement("masterId");
			masterIdEle.setText(masterId);
		}
		String title = luntanParam.get("title");
		if(null != title){
			Element titleEle = root.addElement("title");
			titleEle.setText(title);
		}
		String signName = luntanParam.get("signName");
		if(null != signName){
			Element signNameEle = root.addElement("signName");
			signNameEle.setText(signName);
		}
		
		String city = luntanParam.get("city");
		if(null != city){
			Element cityEle = root.addElement("city");
			cityEle.setText(city);
		}
		
		String shareImgUrl = luntanParam.get("shareImgUrl");
		if(null != shareImgUrl){
			Element shareImgUrlEle = root.addElement("shareImgUrl");
			shareImgUrlEle.setText(shareImgUrl);
		}
		
		String shareurl = luntanParam.get("shareurl");
		if(null != shareurl){
			Element shareurlEle = root.addElement("shareurl");
			shareurlEle.setText(shareurl);
		}
		
		String bid = luntanParam.get("bid");
		if(null != bid){
			Element bidEle = root.addElement("bid");
			bidEle.setText(bid);
		}
		String isGood = luntanParam.get("isGood");
		if(null != isGood){
			Element isGoodEle = root.addElement("isGood");
			isGoodEle.setText(isGood);
		}
		String isTop = luntanParam.get("isTop");
		if(null != isGood){
			Element isTopEle = root.addElement("isTop");
			isTopEle.setText(isTop);
		}
		//权限
		String isAuth = luntanParam.get("isAuth");
		if(null != isAuth){
			Element isAuthEle = root.addElement("isAuth");
			isAuthEle.setText(isAuth);
		}
		//<boardUrl> 论坛链接
		String boardUrl = luntanParam.get("boardUrl");
		if(null != boardUrl){
			Element boardUrlEle = root.addElement("boardUrl");
			boardUrlEle.setText(boardUrl);
		}
		//楼主昵称
		String nickname = luntanParam.get("nickname");
		if(null != nickname){
			Element nicknameEle = root.addElement("nickname");
			nicknameEle.setText(nickname);
		}
		//跳转楼层所需要 page:当前第几页  postid:跳转楼层号
		String page = luntanParam.get("page");
		if(null != page){
			Element pageEle = root.addElement("page");
			pageEle.setText(page);
		}
		String postId = luntanParam.get("postId");
		if(null != postId){
			Element postIdEle = root.addElement("postId");
			postIdEle.setText(postId);
		}
		//返回当前系统时间
		String nowtime=StringUtil.getNowTime();
		if(null!=nowtime){
			Element nowTimeEle = root.addElement("nowTime");
			nowTimeEle.setText(nowtime);
		}
		//秒杀替换名单时的id
		String replaceID = luntanParam.get("replaceID");
		if(null != replaceID){
			Element replaceIDEle = root.addElement("replaceID");
			replaceIDEle.setText(replaceID);
		}
		//是否秒杀活动
		String isSpikeActivity = luntanParam.get("isSpikeActivity");
		if(null != isSpikeActivity){
			Element isSpikeActivityEle = root.addElement("isSpikeActivity");
			isSpikeActivityEle.setText(isSpikeActivity);
		}
		//秒杀id
		String spikeId = luntanParam.get("spikeId");
		if(null != spikeId){
			Element spikeIdEle = root.addElement("spikeId");
			spikeIdEle.setText(spikeId);
		}
		//秒杀开始时间
		String spikeBeginTime = luntanParam.get("spikeBeginTime");
		if(null != spikeBeginTime){
			Element spikeBeginTimeEle = root.addElement("spikeBeginTime");
			spikeBeginTimeEle.setText(spikeBeginTime);
		}
		
		
		//视频节点
		String video = luntanParam.get("video");
		if(null != video){
			String[] videoInfo = video.split("#");
			Element videoListEle = root.addElement("videolist");
			Element videoEle = videoListEle.addElement("video");
			Element videourlEle = videoEle.addElement("videourl");
			Element thumburlEle = videoEle.addElement("thumburl");
			thumburlEle.setText(videoInfo[0]);
			videourlEle.setText(videoInfo[1]);
		}
		//替换秒杀按钮节点
		String msButton = luntanParam.get("msButton");
		if(null != msButton){
			String[] msButtonInfo = msButton.split("#");
			Element replacelistEle = root.addElement("replacelist");
			Element replaceEle = replacelistEle.addElement("replace");
			Element idEle = replaceEle.addElement("repid");
			Element repcontentEle = replaceEle.addElement("repcontent");
			idEle.setText(msButtonInfo[0]);
			repcontentEle.setText(msButtonInfo[1]);
		}
		
		return document;
		
	}
	
	/**
	 * (重载方法)生成论坛xml ：错误信息的论坛xml
	 * @param msg
	 * @param errorMsg
	 * @return
	 */
	public static org.dom4j.Document createXmlForLuntan(String msg,String errorMsg){
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element erromessage = root.addElement("errormessage");
		message.setText(msg);
		erromessage.setText(errorMsg);
		return document;
		
	}
	/**
	 * 根据给定时间返回所需要的时间：1小时内显示N分钟前，当天只显示时间，当年只显示日期，非当年显示年月日
	 * @param timeStr:时间毫秒数
	 * @return
	 */
	public static String getNeedTimeString(String timeStr){
		String needStr = null;
		if(null != timeStr){
			//当前时间
			Calendar nowDate = Calendar.getInstance();
			long nowMillis = nowDate.getTimeInMillis();
			//给定时间
			Calendar paramDate = Calendar.getInstance();
			paramDate.setTimeInMillis(Long.valueOf(timeStr));
			long paramMillis = Long.valueOf(timeStr);
			if(nowMillis - paramMillis <= 3600000){
				needStr = (nowMillis - paramMillis)/60000 + "分钟前";
			}else if(nowDate.get(Calendar.YEAR) == paramDate.get(Calendar.YEAR) 
					&& nowDate.get(Calendar.MONTH) == paramDate.get(Calendar.MONTH)
					&& nowDate.get(Calendar.DATE) == paramDate.get(Calendar.DATE)){
				needStr = (nowMillis - paramMillis)/3600000 + "小时前";
//				needStr =  new SimpleDateFormat("HH:mm").format(new Date(paramMillis));
			}else if(nowDate.get(Calendar.YEAR) == paramDate.get(Calendar.YEAR)){
				needStr =  new SimpleDateFormat("MM-dd").format(new Date(paramMillis));
			}else{
				needStr =  new SimpleDateFormat("yyyy-MM-dd").format(new Date(paramMillis));
			}
		}
		return needStr;
		
	}
	
	/**
	 * 处理论坛图片
	 * @param doc
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private static int j=0;
	public static List<String> dealImgForLuntan(Document luntanDoc) throws InterruptedException, ExecutionException{
		//处理图片
		List<String> imgUrlList = null;
		if(null != luntanDoc){
			Elements imgs = luntanDoc.select("img");
			if(null != imgs && imgs.size() > 0){
				imgUrlList = new ArrayList<String>();
				List<Future<String[]>> results = new ArrayList<Future<String[]>>();
				
				
				for(int i = 0;i<imgs.size();i++){
					//这个图片标签有误
					if("http://www.028cdbz.cn/SS49800.gif".equals(imgs.get(i).attr("src"))){
						imgs.get(i).after("<img style='max-width:100%;' src='http://www.028cdbz.cn/SS49800.gif'>");
						imgs.get(i).remove();
						continue;
					}
					//qq的图片不处理
					if(imgs.get(i).attr("src").contains("qq.com")){
						continue;
					}
					if("http://wpa.qq.com/pa?p=2:2090383169:41&r=0.35315199465694036".equals(imgs.get(i).attr("src"))
							|| "http://wpa.qq.com/pa?p=2:1130463441:52".equals(imgs.get(i).attr("src"))){
						continue;
					}
					if(!"head".equals(imgs.get(i).attr("type")) && !"video".equals(imgs.get(i).attr("type")) && !imgs.get(i).attr("src").endsWith("gif")
							&& !"dzp".equals(imgs.get(i).attr("type")) && !"miaosha".equals(imgs.get(i).attr("type"))){
						String src = imgs.get(i).attr("src");
						
						String width = imgs.get(i).attr("picwidth") + "";
						String height = imgs.get(i).attr("pichigh") + "";
						//如果图片宽高不是空
						if (StringUtil.isEmpty(width)||StringUtil.isEmpty(height)){
							//宽或高是空的获取宽高
							if(!StringUtil.isEmpty(src) && !src.trim().contains("http://p1.gexing.com/")){
								//获取宽高
								results.add(pool.submit(new AppHtmlUtil(src,i)));
							}
						}else {
							imgUrlList.add(src);
							String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
							if("dl".equals(imgs.get(i).parent().parent().parent().tagName())){
								imgs.get(i).parent().parent().parent().removeAttr("style");
							}
							if("p".equals(imgs.get(i).parent().tagName())){
								imgs.get(i).after("<span class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></span>");
							}else{
								imgs.get(i).after("<div class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></div>");
							}
							imgs.get(i).remove();
							j++;
						}
						
					}else if(!StringUtil.isEmpty(imgs.get(i).attr("style")) && imgs.get(i).attr("style").contains("max-width")){
						imgs.get(i).attr("style","outline: none; vertical-align: middle; max-width: 100%;");
					}
				}
				
				move(imgs,results,imgUrlList,1);
			}
		}
				j=0;
			return imgUrlList;
		
	}
	
	public static void move(Elements imgs, List<Future<String[]>> results, List<String> imgUrlList, int methodNO) throws InterruptedException, ExecutionException{
			//处理结果..........等待任务结束
				for (Future<String[]> fs : results) {
					int k=0;
					 while(!fs.isDone()){
				         	try {
								Thread.sleep(2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				         	k++;
				         	if(k==100){
				         		break;
				         		}
				         }
					 long sta=System.currentTimeMillis();
						String width =  fs.get()[0];
						String height =  fs.get()[1];
						int i =  Integer.parseInt(fs.get()[2]);
						String src =  fs.get()[3];
		                long end=System.currentTimeMillis();
						//返回 第 i 个照片 是否空 
						logger.info("getImgWidthAndHeightLuntanend width="+width+"  height:"+height+" i="+i+"  url:"+src+" use: " + (end-sta) + " ms");
						//如果图片宽高不是空  单独处理
						//第一个方法调用的
						if(methodNO==1){
						
							if (!StringUtil.isEmpty(width)&&!StringUtil.isEmpty(height)){
								imgUrlList.add(src);
								String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
								if("dl".equals(imgs.get(i).parent().parent().parent().tagName())){
									imgs.get(i).parent().parent().parent().removeAttr("style");
								}
								if("p".equals(imgs.get(i).parent().tagName())){
									imgs.get(i).after("<span class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></span>");
								}else{
									imgs.get(i).after("<div class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></div>");
								}
								imgs.get(i).remove();
								j++;
							}else{
								imgs.get(i).remove();
							}
						}else if(methodNO==2){
							//如果获取的图片宽高不是空  单独处理
							if(!StringUtil.isEmpty(width)&&!StringUtil.isEmpty(height)){
								if(!"0".equals(height) && !"NaN".equals(height) && Integer.valueOf(width)/Integer.valueOf(height) > 3){
									imgs.get(i).remove();
									continue;
								}
								imgUrlList.add(src);
								// 为图片添加父节点<a>
								String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
								imgs.get(i).after("<a href=" + href + ">" + imgs.get(i).toString() + "</a>");
								imgs.get(i).remove();
								j++;
							}else{
								imgs.get(i).remove();
							}
					        }
		}
	}
	
	/**
	 * 处理论坛图片涉及缓存
	 * @param doc
	 * @return
	 */
	public static List<String> dealImgForLuntan_V1(Document luntanDoc,List<String> imgUrlList){
		//处理图片
		if(null != luntanDoc){
			Elements imgs = luntanDoc.select("img");
			if(null != imgs && imgs.size() > 0){
				//imgUrlList = new ArrayList<String>();
				for(int i = 0,j=imgUrlList.size();i<imgs.size();i++){
					//这个图片标签有误
					if("http://www.028cdbz.cn/SS49800.gif".equals(imgs.get(i).attr("src"))){
						imgs.get(i).after("<img style='max-width:100%;' src='http://www.028cdbz.cn/SS49800.gif'>");
						imgs.get(i).remove();
						continue;
					}
					//qq的图片不处理
					if(imgs.get(i).attr("src").contains("qq.com")){
						continue;
					}
					if("http://wpa.qq.com/pa?p=2:2090383169:41&r=0.35315199465694036".equals(imgs.get(i).attr("src"))
							|| "http://wpa.qq.com/pa?p=2:1130463441:52".equals(imgs.get(i).attr("src"))){
						continue;
					}
					if(!"head".equals(imgs.get(i).attr("type")) && !"video".equals(imgs.get(i).attr("type")) && !imgs.get(i).attr("src").endsWith("gif")){
						String src = imgs.get(i).attr("src");
						
						String width = imgs.get(i).attr("picwidth") + "";
						String height = imgs.get(i).attr("pichigh") + "";
						if (StringUtil.isEmpty(width) || StringUtil.isEmpty(height)) {
							if(!StringUtil.isEmpty(src) && !src.trim().contains("http://p1.gexing.com/")){
								String[] widthHright = getImgWidthAndHeight(src);
								width = widthHright[0];
								height = widthHright[1];
								
							}
						}
						//如果图片宽高不是空
						if (!StringUtil.isEmpty(width)&&!StringUtil.isEmpty(height)){
							imgUrlList.add(src);
							String href = "soufun://waptoapp/{\"destination\":\"showBigPic\",\"index\":\"" + j + "\"}";
							if("dl".equals(imgs.get(i).parent().parent().parent().tagName())){
								imgs.get(i).parent().parent().parent().removeAttr("style");
							}
							if("p".equals(imgs.get(i).parent().tagName())){
								imgs.get(i).after("<span class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></span>");
							}else{
								imgs.get(i).after("<div class='img-box'><a href=" + href + ">" + "<img type='image' id='fimg_" +j + "' width='"+width+"' height='"+height + "'></a></div>");
							}
							imgs.get(i).remove();
							j++;
						}else{
							imgs.get(i).remove();
						}
					}else if(!StringUtil.isEmpty(imgs.get(i).attr("style")) && imgs.get(i).attr("style").contains("max-width")){
						imgs.get(i).attr("style","outline: none; vertical-align: middle; max-width: 100%;");
					}
				}
			}
		}
		return imgUrlList;
		
	}
	
	public static void romveAtagForLuntan(Document luntanDoc,String user,String shareUrl) throws Exception{
		if(null != luntanDoc){
			//去一下shareUrl的空格
			if(!StringUtil.isEmpty(shareUrl)){
				shareUrl = shareUrl.trim();
			}
			Elements alinks = luntanDoc.select("a");
			if(null != alinks && alinks.size() > 0){
				for(int i =0;i<alinks.size();i++){
					//视频的链接不处理
					if("video".equals(alinks.get(i).attr("type"))){
						continue;
					}
					//投票的链接不处理
					if("voteSubmit".equals(alinks.get(i).attr("type"))){
						continue;
					}
					//大转盘的链接不处理
					if("dzp".equals(alinks.get(i).attr("type"))){
						continue;
					}
					//大转盘的链接不处理
					if("miaosha".equals(alinks.get(i).attr("type"))){
						continue;
					}
					
					//去掉查看更多
					if(alinks.get(i).hasText() && alinks.get(i).text().contains("查看更多")){
						alinks.get(i).remove();
					}else if(!StringUtil.isEmpty(alinks.get(i).attr("href")) && alinks.get(i).attr("href").matches("http.*/([0-9]{9}_[0-9]{9}).htm") 
							&& !"zhuangxiu".equals(user)
							&& !alinks.get(i).attr("href").trim().equals(shareUrl)){
						//家居论坛暂不支持 ; 不能自己跳自己
						alinks.get(i).removeAttr("target");
						//编码href
						String href = java.net.URLEncoder.encode(alinks.get(i).attr("href"),"utf-8");
						alinks.get(i).attr("href","soufun://waptoapp/{\"destination\":\"forumUrl\",\"url\":\"" + href + "\"}");
					}else if("fblue".equals(alinks.get(i).attr("class")) && alinks.get(i).hasText() && alinks.get(i).text().contains("(过客)")){
						alinks.get(i).attr("href","javascript:;");
					}else if(!"fblue".equals(alinks.get(i).attr("class")) && !"hf-btn".equals(alinks.get(i).attr("class")) 
							&& !"up-icon".equals(alinks.get(i).attr("class")) 
							&& !"hd-btn".equals(alinks.get(i).attr("class"))
							&& !"hd-btn disable".equals(alinks.get(i).attr("class")) ){
						//如果不是自己添加的链接则去掉
						String alinkHtml = alinks.get(i).toString();
						if ((alinkHtml.indexOf(">") + 1) != alinkHtml.lastIndexOf("</a>")) {
							String innerHtml = alinkHtml.substring(alinkHtml.indexOf(">") + 1, alinkHtml.lastIndexOf("</a>"));
							alinks.get(i).after(innerHtml);
						}
						alinks.get(i).remove();
					}
				}
			}
		}
	}
	
	/**
	 * 过滤str中无效的xmlChar
	 * @param xmlStr
	 * @return
	 */
	public static String parseXmlChar(String xmlStr){
		char [] xmlChar = xmlStr.toCharArray();
		for(int i=0;i<xmlChar.length;i++){
			if(xmlChar[i] == 1 || xmlChar[i] == 20){
				xmlChar[i] = ' ';
			}
		}
		xmlStr = String.valueOf(xmlChar);
		return xmlStr;
	}
	
	/**
	 * 获取图片的宽高
	 * @param imgSrc
	 * @return
	 */
	public static String[] getImgWidthAndHeight(String src){
		String[] widthHeight = {"","","",""};
		InputStream is=null ;
		ImageInputStream iis=null;
		try{
			if(!(src.substring(src.lastIndexOf("/"))).contains(".")
					|| (!src.contains("soufun.com")
							&& !src.contains("soufunimg.com"))
							){
				
			}else if(!StringUtil.isEmpty(src) 
					&& !src.contains("http://spfmmbj.hrbszfc.com:5399/mmbjquery/images/22.png")//过滤此url
							&& !src.contains("farm7.static.flickr.com")//过滤此url,此域名不存在
			){
				URL realUrl = new URL(src);
				if(realUrl.getProtocol().toLowerCase().equals("https")) {
					 HttpsURLConnection conn = null;  
			         TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){  
			            public X509Certificate[] getAcceptedIssuers(){return null;}  
			            public void checkClientTrusted(X509Certificate[] certs, String authType){}  
			            public void checkServerTrusted(X509Certificate[] certs, String authType){}  
			        }};  
	                conn = (HttpsURLConnection) realUrl.openConnection();
	                HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);  
	                SSLContext sc = SSLContext.getInstance("TLS");  
	                sc.init(null, trustAllCerts, new SecureRandom());  
	                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
	                conn.setConnectTimeout(3000);
	                conn.setReadTimeout(3000);
	                conn.connect();  
	                is = conn.getInputStream();
	    			iis = ImageIO.createImageInputStream(is);
	    			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
	    			ImageReader imgReader = (ImageReader) readers.next();
	    			imgReader.setInput(iis, true);
	    			String width = String.valueOf(imgReader.getWidth(0));
	    			String height = String.valueOf(imgReader.getHeight(0));
	    			widthHeight[0] = width;
					widthHeight[1] = height;
				}else {
						 
			        // 打开和URL之间的连接
					HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			        connection.setRequestProperty("Referer", src);
			        connection.setConnectTimeout(3000);
			        connection.setReadTimeout(3000);
//					is = realUrl.openStream();
			        is = connection.getInputStream();
					long endTime1 = System.currentTimeMillis();
					iis = ImageIO.createImageInputStream(is);
					Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
					ImageReader imgReader = (ImageReader) readers.next();
					imgReader.setInput(iis, true);
					String width = String.valueOf(imgReader.getWidth(0));
					String height = String.valueOf(imgReader.getHeight(0));
					widthHeight[0] = width;
					widthHeight[1] = height;
					long endTime2 = System.currentTimeMillis();
					logger.info("until dealimg  InputStream is "+(endTime2-endTime1)+"ms");
				}
			}
		
		}catch(Exception e){
			System.out.println("错误的图片地址：" + src);
			logger.error(e.getMessage(), e);
		}finally{
				try {
					if(null!=iis){
						iis.close();
					}
					if(null!=is){
						is.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		return widthHeight;
		
		
	}
	
	/**
	 * 屏蔽二手房导购链接
	 * @param doc
	 */
	public static void removeErshoufangDaogouATag(Document doc){
		if(null != doc){
			Elements as = doc.select("a");
			if(null != as){
				for(int i=0;i<as.size();i++){
					//二手房导购跳转小区详情链接处理
					if("#".equals(as.get(i).attr("href"))){
						if(as.get(i).toString().contains("<!--") 
								&& as.get(i).toString().contains("soufun://waptoapp")
								&& as.get(i).toString().contains("-->")){
							String href = "";
							if(as.get(i).toString().indexOf("<!--")+4 < as.get(i).toString().indexOf("-->")){
								href = as.get(i).toString().substring(as.get(i).toString().indexOf("<!--")+4,as.get(i).toString().indexOf("-->"));
							}
							as.get(i).attr("href",href);
						}
						continue;
					}
					
					if(("p".equals(as.get(i).parent().tagName()) || "strong".equals(as.get(i).parent().tagName()) || "font".equals(as.get(i).parent().tagName()))
							&& as.get(i).attr("href").trim().endsWith(".htm")){
						if(as.get(i).hasText() && as.get(i).text().contains(".htm")){
							as.get(i).parent().remove();
							continue;
						}
					}
					
					if(as.get(i).hasText()){
						if(as.get(i).text().contains("点击") || as.get(i).text().contains("查看") 
								|| as.get(i).text().contains("点此") || as.get(i).text().contains("报名") 
								|| as.get(i).text().contains("我要算房贷") || as.get(i).text().contains("更多在售二手房")
								|| as.get(i).text().contains("http://") || as.get(i).text().matches("更多.*?推荐")){
							as.get(i).remove();
							continue;
						}
					}
					
					if("li".equals(as.get(i).parent().tagName()) || (null != as.get(i).children() && as.get(i).children().size() ==1 && "i".equals(as.get(i).child(0).tagName())) || "h2".equals(as.get(i).parent().tagName())){
						//不处理
					}else if("telephone".equals(as.get(i).attr("type"))){
						//不处理
					}else if(as.get(i).attr("href").contains("soufun://waptoapp/")){
						//不处理
					}
					else{
						String alinkHtml = as.get(i).toString();
						if ((alinkHtml.indexOf(">") + 1) != alinkHtml.lastIndexOf("</a>")) {
							String innerHtml = alinkHtml.substring(alinkHtml.indexOf(">") + 1, alinkHtml.lastIndexOf("</a>"));
							as.get(i).after(innerHtml);
						}
						as.get(i).remove();
					}
				}
			}
			
		}
	}
	/**
	 * toutiao新增返回节点，生成固定结构的xml文件
	 * 
	 * @param msg ： 处理结果
	 * @param newscontent ： 信息
	 * @param urlList ： 图片url列表
	 * @param videoList ： 视频信息列表
	 * @param toutiaoparam ：  新增的返回节点
	 * @return
	 */
	public static org.dom4j.Document createXmlForToutiao(String msg, String newscontent, List<String> urlList,
			List<Map<String, String>> videoList,Map<String,String> toutiaoparam) {

		// 生成xml文件
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element root = document.addElement("result");
		Element message = root.addElement("message");
		Element content = root.addElement("content");
		message.setText(msg);
		content.setText(newscontent);

		// 增加图片list节点
		if (urlList != null && urlList.size() > 0) {
			Element imglist = root.addElement("imagelist");
			for (String url : urlList) {
				Element img = imglist.addElement("img");
				Element imgurl = img.addElement("imageurl");
				imgurl.setText(url);
			}
		}
		// 增加视频list节点
		if (videoList != null && videoList.size() > 0) {
			Element videolist = root.addElement("videolist");
			for (Map<String, String> urlMap : videoList) {
				Element video = videolist.addElement("video");
				Element videourl = video.addElement("videourl");
				Element thumburl = video.addElement("thumburl");
				for (String key : urlMap.keySet())
				{
					videourl.setText(key.trim());
					thumburl.setText(urlMap.get(key).trim());
				}
			}
		}
		String FirstImageID = toutiaoparam.get("FirstImageID");//是否开通评论，2为开通，”“为非开通
		if(null != FirstImageID){
			Element firstImageIdEle = root.addElement("FirstImageID");
			firstImageIdEle.setText(FirstImageID);
		}
		String newsId = toutiaoparam.get("newsId");
		if(null != newsId){
			Element newsIdEle = root.addElement("newsId");
			newsIdEle.setText(newsId);
		}
		String bbs_masterid = toutiaoparam.get("bbs_masterid");//主贴id
		if(null != bbs_masterid){
			Element bbs_masteridEle = root.addElement("bbs_masterid");
			bbs_masteridEle.setText(bbs_masterid);
		}
		String city = toutiaoparam.get("city");//帖子所在城市
		if(null != city){
			Element cityEle = root.addElement("city");
			cityEle.setText(city);
		}
		return document;
	}
	
	public static void main(String[] args) {
		

		try {
			for(int i=0;i<20;i++){
				
				getImgWidthAndHeight("http://img1.soufun.com/album/2013_10/25/1382705805785_000.jpg");
			}
			
//			logger.info(getImgWidthAndHeight("https://mmbiz.qlogo.cn/mmbiz/x0iadIz14MetqAKxEZxUSD3g5rf4YETbhICLPneIbIvU0KsXibJibtg3npmr9vW3ibicbh2jNZ5ldU7HBYEg8rf72bA/0?wx_fmt=jpeg")[0]);
			/*File file = new File("D:\\work\\workspace\\messageCenter\\WebRoot\\template\\优化导购.html");
			String loupan = readFileToString(file);
//			for(String str : getLoupanList(loupan)){
//				System.out.println(str);
//				System.out.println("===============================");
//			}
//			System.out.println(getLoupanList(loupan).length);
			Document louPanDoc = Jsoup.parse(loupan);
			//System.out.println(getLoupanTelephone(louPanDoc));
			//System.out.println(getDaogouHead(loupan));
			//System.out.println(getLoupanName(louPanDoc));
			removeDaogouMatchTag(".*?(看了本文的人还看了).*", louPanDoc);
			System.out.println(louPanDoc);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	    };
	    
	    /**
		 * 将头条资讯中含有电话的span移动位置
		 * @param reg
		 * @param doc
		 */
		public static void changeToutiaoTel(Document doc){
			if(null != doc){
				Elements telSpans = doc.select("p > span[style]");
				Pattern p = Pattern.compile("(\\d{3}-\\d{3}-\\d{4})([\\s]*?)(转)([\\s]*?)(\\d{6})");

				
				if(null != telSpans && telSpans.size() >0){
					for(int i=0;i<telSpans.size();i++){
						Matcher m = p.matcher(telSpans.get(i).text());
						
						if(telSpans.get(i).attr("style").toString().contains("http://img1.soufun.com/house/admin/tel.png")
						 || 	m.find()	
						){
							if(null != telSpans.get(i).parent().nextElementSibling() 
									&& ("p".equals(telSpans.get(i).parent().nextElementSibling().tagName())
									|| "strong".equals(telSpans.get(i).parent().nextElementSibling().tagName()))
								&& null != telSpans.get(i).parent().children()	&& "a".equals(telSpans.get(i).parent().children().get(0).tagName())
								&& telSpans.get(i).parent().children().get(0).attr("href").contains("webview")
								&& null!=telSpans.get(i).parent().children().get(0).children() && telSpans.get(i).parent().children().get(0).children().size()==1
								&& "span".equals(telSpans.get(i).parent().children().get(0).children().get(0).tagName())
							
							){
								telSpans.get(i).parent().children().get(0).children().get(0).removeAttr("style");
								telSpans.get(i).parent().children().get(0).children().get(0).attr("style","font-size:22px;color:#507fbd;font-weight:bold;");
								telSpans.get(i).removeAttr("style");
								telSpans.get(i).attr("style", "font-size:18px;color:#f00;background: url(http://img1.soufun.com/house/admin/tel.png) no-repeat left center;padding-left:20px;");
								telSpans.get(i).parent().after( "<p>"+telSpans.get(i).toString()+"</p>");
								telSpans.get(i).remove();
							}else if(null != telSpans.get(i).parent().nextElementSibling() 
									&& ("p".equals(telSpans.get(i).parent().nextElementSibling().tagName())
											|| "strong".equals(telSpans.get(i).parent().nextElementSibling().tagName()))
									&& null != telSpans.get(i).parent().children()	&& telSpans.get(i).parent().children().size()==2
									&& "span".equals(telSpans.get(i).parent().children().get(0).tagName())
									&& telSpans.get(i).parent().children().get(0).attr("style").contains("微软雅黑")
								
								){
									telSpans.get(i).parent().children().get(0).removeAttr("style");
									telSpans.get(i).parent().children().get(0).attr("style","font-size:22px;color:#507fbd;font-weight:bold;");
									telSpans.get(i).removeAttr("style");
									telSpans.get(i).attr("style", "font-size:18px;color:#f00;background: url(http://img1.soufun.com/house/admin/tel.png) no-repeat left center;padding-left:20px;");
									telSpans.get(i).parent().after( "<p>"+telSpans.get(i).toString()+"</p>");
									telSpans.get(i).remove();
								}
						}
					}
				}
				
				
			}
		}
		
		@SuppressWarnings("unchecked")
		public  T call() throws Exception {
			String[] widthAndHeigth = {"","",imgNO,url};
			widthAndHeigth = AppHtmlUtil.getImgWidthAndHeight(url);
			widthAndHeigth[2]= imgNO;
			widthAndHeigth[3]= url;
	        return (T) widthAndHeigth;
	        
	    }
}
