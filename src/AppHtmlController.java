

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jaxen.function.ext.UpperFunction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fang.constant.Constant;
import com.fang.dao.SqlServerDao;
import com.fang.domain.CityListInfo;
import com.fang.domain.LouPanInfo;
import com.fang.util.AppHtmlUtil;
import com.fang.util.LuntanCache;
import com.fang.util.MD5;
import com.fang.util.StringUtil;
import com.sun.mail.imap.AppendUID;

/**
 * 
 * @ClassName: AppHtmlController
 * @Description: 对app提供html页面字符串
 * @author yuchao
 * @Date 2015-5-6
 * 
 */
@Controller
@RequestMapping("/wap")
public class AppHtmlController {
	
	private static final Logger logger = Logger.getLogger(AppHtmlController.class);

	@Autowired
	private SqlServerDao sqlServerDao;

	@RequestMapping("/baike")
	public void getNewsDetail(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			logger.info("进入baike");
			long inTime = System.currentTimeMillis();
			String newsId = request.getParameter("newsid");
			String key = request.getParameter("key");
			PrintWriter out = response.getWriter();

			// 加MD5验证
			 String md5Key = new MD5().getMD5(newsId + "baike");
//			 if (!md5Key.equals(key)) {
//				 out.write(createXml("failed", "MD5验证失败", null,null).asXML());
//				 out.close();
//				 return;
//			 }
			logger.info(" baike newsId :" +newsId );
			Map<String, String> result = sqlServerDao.getHtmlStringForApp(newsId);

			if (result != null) {

				// 获取html模板
				File file = new File(Constant.HTML_TEMPLATE + "htmltemplate.html");

				// 文件读成String
				String str = AppHtmlUtil.readFileToString(file);

				if (str != null) {
					if (str.contains("newstitle")) {
						str = str.replace("newstitle", result.get("newstitle"));
					}
					if (str.contains("newscontent")) {
						str = str.replace("newscontent", result.get("newscontent"));
					}
				}

				// 格式化str为html
				Document doc = Jsoup.parse(str);
				
				//格式化table中的img
				AppHtmlUtil.formatTableImgs(doc);
				// 去掉table标签
				Elements tables = doc.select("table");
				if (tables != null && tables.size() > 0) {
					tables.remove();
				}

				// 去掉相关阅读内容
				AppHtmlUtil.removeDaogouMatchTag(".*?((相关|延伸|推荐)阅读).*", doc);

				// 去掉所有的<a>但是保留a中内容
				AppHtmlUtil.removeAtag(doc);
				
				// 去掉所有的<font>但是保留font中内容
				AppHtmlUtil.removeFonttag(doc);

				// 处理图片并 保存图片的url
				List<String> urlList = AppHtmlUtil.dealImage(doc);

				// 处理视频并返回视频的url
				List<Map<String,String>> videoList = AppHtmlUtil.dealVideo(doc);

				// 生成xml文件并输出
				out.write(AppHtmlUtil.createXml("success", doc.html(), urlList,videoList).asXML());
				out.close();
			} else {
				out.write(AppHtmlUtil.createXml("failed", "无此条记录", null,null).asXML());
				out.close();
			}
			long outTime = System.currentTimeMillis();
			logger.info("baike执行结束，newsId为:" + newsId + ",耗时:" + (outTime-inTime) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取产品说明
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getProductDescriptionImgText")
	public void getProductDescriptionImgText(HttpServletRequest request, HttpServletResponse response) {
		String ret = "";
		try {
			logger.info("进入getProductDescriptionImgText");
			long inTime = System.currentTimeMillis();
			//http://home.fang.com/jiancai/InterFace/GetProductForWap.aspx?cid=75&pid=3364576&did=69657
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			String did = request.getParameter("did");
			Map<String,String> mNewNode = new HashMap<String, String>();
			//org.dom4j.Element  propertys=null;
			PrintWriter out = response.getWriter();
			String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription+"cid="+cid+"&pid="+pid+"&did="+did);
			//String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription + "cid=75&pid=3364576&did=69657");
			if (!StringUtil.isNull(result)) {
				String description = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/Description/text()","gbk");
				//List<Map<String,String>> rets = StringUtil.getListFromXmlNodeByXpath(result, "//productinfo/Propertys/Property", "gbk");
				// 获取html模板
				File file = new File(Constant.HTML_TEMPLATE +"productDescription.html");
				// 文件转成String
				ret = AppHtmlUtil.readFileToString(file);
				//拼接ret字符串
				StringBuffer sb = new StringBuffer();
				sb.append("<div class=\"pd10 pro_intro f14 bgfff\">");
				sb.append(description);
				sb.append("</div>");
				ret = ret.replace("${replaced}",sb.toString());
				//ret = ret.replace("${productparam}",sb.toString());
				// 格式化str为html
				Document doc = Jsoup.parse(ret);
				// 去掉table标签
				Elements tables = doc.select("table");
				if (tables != null && tables.size() > 0) {
					tables.remove();
				}
				//去掉p的样式
				Elements ps = doc.select("p[style]");
				if(null != ps && ps.size() >0){
					for(int i=0;i<ps.size();i++){
						ps.get(i).removeAttr("style");
					}
				}
				//修改span的样式
				Elements spans = doc.select("span[style]");
				if(null != spans && spans.size() >0){
					for(int i=0;i<spans.size();i++){
						if(null != spans.get(i) && spans.get(i).attr("style").contains("white-space")){
							String style = spans.get(i).attr("style").replaceAll("white-space(.*?);", "");
							style = style.replaceAll("(white-space:|nowrap)", "");
							spans.get(i).attr("style",style);
						}
					}
				}
				// 去掉相关阅读内容
				AppHtmlUtil.removeMatchElements(doc, "(相关|延伸)阅读.*");
				// 去掉所有的<a>但是保留a中内容
				AppHtmlUtil.removeAtag(doc);
				// 处理图片并 保存图片的url
				List<String> urlList = AppHtmlUtil.dealImage(doc);
				// 处理视频并返回视频的url
				List<Map<String,String>> videoList = AppHtmlUtil.dealVideo(doc);
				// 生成xml文件并输出

				//propertys = StringUtil.getElementFromXmlByXpathDom4J(result,"//productinfo/Propertys","gbk");
				out.write(AppHtmlUtil.createXml("success", doc.html(), urlList, videoList).asXML());
				out.close();
			} else {
				out.write(AppHtmlUtil.createXml("failed", "无此条记录", null, null).asXML());
				out.close();
			}
			long outTime = System.currentTimeMillis();
			logger.info("getProductDescriptionImgText 执行结束，耗时:" + (outTime-inTime) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取产品说明属性
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getProductDescriptionPropertys")
	public void getProductDescriptionPropertys(HttpServletRequest request, HttpServletResponse response) {
		String ret = "";
		try {
			logger.info("进入getProductDescriptionPropertys");
			long inTime = System.currentTimeMillis();
			//http://home.fang.com/jiancai/InterFace/GetProductForWap.aspx?cid=75&pid=3364576&did=69657
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			String did = request.getParameter("did");
			//Map<String,String> mNewNode = new HashMap<String, String>();
			//org.dom4j.Element  propertys=null;
			PrintWriter out = response.getWriter();
			String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription+"cid="+cid+"&pid="+pid+"&did="+did);
			logger.info(Constant.sUrlProductDescription+"cid="+cid+"&pid="+pid+"&did="+did);
			logger.info(result);
			//String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription + "cid=75&pid=3364576&did=69657");
			if (!StringUtil.isNull(result)) {
				//String description = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/Description/text()","gbk");
				List<Map<String,String>> rets = StringUtil.getListFromXmlNodeByXpath(result, "//productinfo/Propertys/Property", "gbk");

				//添加节点
				String Models = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/Model/text()","gbk");
				logger.info("Models:"+Models);
				if (!StringUtil.isNull(Models))
				{
					Map<String,String> Model = new HashMap<String, String>();
					Model.put("propertyName","型号");
					Model.put("ShowVal",Models);
					Model.put("unit","");
					rets.add(0,Model);
				}
				String Specs = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/Spec/text()","gbk");
				logger.info("Specs:"+Specs);
				if (!StringUtil.isNull(Specs))
				{
					Map<String,String> Spec = new HashMap<String, String>();
					Spec.put("propertyName","规格");
					Spec.put("ShowVal",Specs);
					Spec.put("unit","");
					rets.add(1,Spec);
				}

				Map<String,String> pidInfo = new HashMap<String, String>();
				pidInfo.put("propertyName","商品编号");
				pidInfo.put("ShowVal",pid);
				pidInfo.put("unit","");
				rets.add(2,pidInfo);


				String addTimes = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/AddedTime/text()","gbk");
				logger.info(addTimes);
				if (!StringUtil.isNull(addTimes) || addTimes.contains("0001/1/1"))
				{
					Map<String,String> addTime = new HashMap<String, String>();
					addTime.put("propertyName","上架时间");
					addTime.put("ShowVal",addTimes);
					addTime.put("unit","");
					rets.add(3,addTime);
				}

				// 获取html模板
				File file = new File(Constant.HTML_TEMPLATE +"productDescription.html");
				// 文件转成String
				ret = AppHtmlUtil.readFileToString(file);
				//拼接ret字符串
				StringBuffer sb = new StringBuffer();
				if(rets.size() > 0)
				{
					sb.append("<div class=\"mt15 pdX10 pro_intro f14 bgfff\">");
					sb.append("<ul>");
					for (Map<String,String> m :rets) {
						sb.append("<li><span>").append(m.get("propertyName")).append("</span>").append(m.get("ShowVal")).append(m.get("unit")).append("</li>");
					}
					sb.append("</ul></div>");
				}else
				{
					sb.append("").append("<div style=\"margin:auto;text-align:center\">暂无参数</div>");
				}
				ret = ret.replace("${replaced}",sb.toString());
				//mNewNode.put("Propertys",sb.toString());
				out.write(AppHtmlUtil.createProductionDescriptionXml("success", ret).asXML());
				out.close();
			} else {
				out.write(AppHtmlUtil.createProductionDescriptionXml("failed", "无此条记录").asXML());
				out.close();
			}
			long outTime = System.currentTimeMillis();
			logger.info("getProductDescriptionPropertys 执行结束，耗时:" + (outTime-inTime) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取头条详细信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/toutiao")
	public void getTouTiaoDetail(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out=null ;
		String newsId = "";
		try {
			logger.info("toutiao begin");
			long inTime = System.currentTimeMillis();

			newsId = request.getParameter("newsid");
			String key = request.getParameter("key");
			out = response.getWriter();

			// 加MD5验证
			 String md5Key = new MD5().getMD5(newsId + "toutiao");
//			 if (!md5Key.equals(key)) {
//				 out.write(createXml("failed", "MD5验证失败", null,null).asXML());
//				 out.close();
//				 return;
//			 }
			logger.info(" toutiao newsId : "+ newsId);
			if(null ==newsId || "".equals(newsId) || newsId.contains("zhishi")){
				out.write(AppHtmlUtil.createXml("failed", "无此条记录", null,null).asXML());
				out.close();
			}else{
				Map<String, String> touTiaoDetail = sqlServerDao.getTouTiaoDetail(newsId);
				long endTime1 = System.currentTimeMillis();
				logger.info("until getTouTiaoDetail use time is "+(endTime1-inTime)+" ms, toutiao newsId : "+ newsId);
				Map<String,String> params=new HashMap<String,String>();
				if (touTiaoDetail != null) {
					// 20151001start判断是否开通评论，FirstImageID=2表示开通，再根据城市获取到城市缩写，从news_comments_xx表中查询主贴id
					String FirstImageID = touTiaoDetail.get("FirstImageID");
					if(null!=FirstImageID && "2".equals(FirstImageID)){
						String newsscope = touTiaoDetail.get("newsscope");
						params.put("city", newsscope);
						String sx="";
						String citysx= "/www/3g.client.soufun.com/messageCenter/src/city_sx.txt";
//						String citysx= "D://eclipse2//messageCenter//src//city_sx.txt";
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(citysx), "utf-8"));

						String line = "";

						while((line=reader.readLine())!=null){
							String[] reslist=line.toString().split(";");
							if(reslist.length==2){
								if(newsscope.equals(reslist[0])){
									sx=reslist[1];
									break;
								}
								
							}
						}
						long endTime2 = System.currentTimeMillis();
						logger.info("TouTiao:until get city_sx use time is "+(endTime2-inTime)+" ms, toutiao newsId : "+ newsId);
						if(null!=sx && !"".equals(sx)){
							String bbs_masterid = sqlServerDao.getBbs_masterid(newsId,sx);
							params.put("FirstImageID", FirstImageID);
							params.put("newsId", newsId);
							params.put("bbs_masterid", bbs_masterid);
						}else{
							params.put("FirstImageID", FirstImageID);
							params.put("newsId", newsId);
						}
						long endTime3 = System.currentTimeMillis();
						logger.info("TouTiao:until getBbs_masterid use time is "+(endTime3-inTime)+" ms, toutiao newsId : "+ newsId);
					}else{
						params.put("FirstImageID", "");
						params.put("newsId", newsId);
					}
					// 20151001end
				

					// 引入html模板
					File file = new File(Constant.HTML_TEMPLATE + "toutiaotemplate.html");

					// 文件转成String
					String str = AppHtmlUtil.readFileToString(file);
					
					String content = touTiaoDetail.get("newscontent");
					content = AppHtmlUtil.parseXmlChar(content);
					content = content.replaceAll("(<<<|>>>|《《《|》》》|&lt;&lt;&lt;|(&gt;){3,})", "");
					//去掉需要屏蔽的内容
					if(content.contains("<!--wirelessHiddenContentBegin-->") && content.contains("<!--wirelessHiddenContentEnd-->")){
						if(content.indexOf("<!--wirelessHiddenContentEnd-->") > content.indexOf("<!--wirelessHiddenContentBegin-->")){
							content = content.replace(content.substring(content.indexOf("<!--wirelessHiddenContentBegin-->"),content.indexOf("<!--wirelessHiddenContentEnd-->")), "");
						}
					}
					
					if (str != null) {
						if (str.contains("newstitle")) {
							str = str.replace("newstitle", touTiaoDetail.get("newsline"));
						}
						if (str.contains("newscontent")) {
							str = str.replace("newscontent", content);
						}
						if (str.contains("newsday")) {
							str = str.replace("newsday", touTiaoDetail.get("newsday"));
						}
						if (str.contains("quarry")) {
							str = str.replace("quarry", touTiaoDetail.get("quarry"));
						}
					}

					// 格式化模板
					Document doc = Jsoup.parse(str);

					Elements keywords = doc.select("meta[name=keywords]");
					Elements description = doc.select("meta[name=description]");
					if (keywords != null && keywords.size() > 0) {
						keywords.get(0).attr("content", touTiaoDetail.get("newskey"));
					}
					if (description != null && description.size() > 0) {
						description.get(0).attr("content", touTiaoDetail.get("news_description"));
					}
					
					//格式化table中的img
					AppHtmlUtil.formatTableImgs(doc);
					// 去掉table标签
					Elements tables = doc.select("table");
					if (tables != null && tables.size() > 0) {
						tables.remove();
					}
					
					//去掉点击跳转
					Elements as = doc.select("a");
					if(null != as){
						for(int i = 0;i<as.size();i++){
							if(as.get(i).hasText()){
								if(as.get(i).text().contains("》") || as.get(i).text().contains(">") || as.get(i).text().contains("点击查看") || as.get(i).text().contains("<<")){
									as.get(i).remove();
								}
							}
						}
					}
					
					//去掉iframe
					Elements iframes = doc.select("iframe");
					if(null != iframes && iframes.size() > 0){
						iframes.remove();
					}
					//去掉分割线
					Elements hrs = doc.select("hr");
					if(null != hrs && hrs.size() > 0){
						hrs.remove();
					}
					
					// 去掉相关阅读内容
					AppHtmlUtil.removeDaogouMatchTag(".*?(((相关|延伸|推荐)阅读)|(相关新闻)|(往日成交)|(重点新闻推荐)"
							+ "|(往期小道)|(往期战报回顾)|(看了本文的人还看了)|(电商团购)|(今日推荐)).*", doc);
					
					//去掉“热点推荐”
					Elements strongs = doc.select("strong");
					if(null != strongs && strongs.size() > 0){
						for(int i=0;i<strongs.size();i++){
							if(null != strongs.get(i) && strongs.get(i).hasText()){
								if(strongs.get(i).text().contains("热点推荐")){
									strongs.get(i).remove();
								}
							}
						}
					}
					
					// 去掉所有的<a>但是保留a中内容
					AppHtmlUtil.removeAtag(doc);
					long endTime4 = System.currentTimeMillis();
					if(endTime4-inTime>3000){
						logger.info("TouTiao:until dealcontent use time is "+(endTime4-inTime)+" ms, toutiao newsId : "+ newsId);
					}
					// 处理图片并 保存图片的url
					List<String> urlList = AppHtmlUtil.dealImage(doc);
					long endTime5 = System.currentTimeMillis();
					if(endTime5-inTime>3000){
						logger.info("TouTiao:until dealimg use time is "+(endTime5-inTime)+" ms, toutiao newsId : "+ newsId);
					}
					// 处理视频
					List<Map<String,String>> videoList = AppHtmlUtil.dealVideo(doc);
					long endTime6 = System.currentTimeMillis();
					if(endTime6-inTime>3000){
						logger.info("TouTiao:until dealvideo use time is "+(endTime6-inTime)+" ms, toutiao newsId : "+ newsId);
					}
					//空白的p
					Elements ps = doc.select("p");
					if(null != ps && ps.size() >0){
						for(int i=0;i<ps.size();i++){
							if((ps.get(i).children() == null || ps.get(i).children().size() == 0) && !ps.get(i).hasText()){
								ps.get(i).remove();
							}
						}
					}
					
					AppHtmlUtil.changeToutiaoTel(doc);//针对有电话号码的，需要改变其样式位置
					// 生成xml文件并输出
					out.write(AppHtmlUtil.createXmlForToutiao("success", doc.html(), urlList,videoList,params).asXML());
					out.close();
				} else {
					out.write(AppHtmlUtil.createXml("failed", "无此条记录", null,null).asXML());
					out.close();
				}
			}
			long outTime = System.currentTimeMillis();
			if(outTime-inTime>3000){
				logger.info("toutiao  end time use:" + (outTime-inTime) + " ms"+" toutiao newsId : "+ newsId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception toutiao newsId : "+ newsId);
			out.write(AppHtmlUtil.createXml("failed", "无此条记录", null,null).asXML());
			out.close();
		}
	}

	/**
	 * 获取导购详情的html
	 * 
	 * @param
	 */
	@RequestMapping("/daogou")
	public void getDaoGouDetail(HttpServletRequest request, HttpServletResponse response) {
		
		try{
			logger.info("daogou begin");
			long inTime = System.currentTimeMillis();

			String newsId = request.getParameter("newsid");
			String city = request.getParameter("city");
			//String key = request.getParameter("key");
			PrintWriter out = response.getWriter();
//			// 加MD5验证
//			 String md5Key = new MD5().getMD5(newsId + "daogou");
//			 if (!md5Key.equals(key)) {
//				 out.write(AppHtmlUtil.createXml("failed", "MD5验证失败", null,null).asXML());
//				 out.close();
//				 return;
//			 }
			logger.info("newsId:" +  newsId);
			Map<String, String> daogouDetail = new HashMap<String,String>();
			daogouDetail.put("newsline", "test");
			daogouDetail.put("newsday", "test");
			daogouDetail.put("quarry", "test");
			daogouDetail.put("newskey", "test");
			daogouDetail.put("news_description", "test");
			//E:\MyWorkSpace\messageCenter\WebRoot\template
			daogouDetail.put("newscontent",AppHtmlUtil.readFileToString(new File("E:\\MyWorkSpace\\messageCenter\\WebRoot\\template\\优化导购2.html")));
//			Map<String, String> daogouDetail = sqlServerDao.getTouTiaoDetail(newsId);
			long outTime = System.currentTimeMillis();
			logger.info("newsId : "+newsId + "daogou  end time use:" + (outTime-inTime) + " ms");
			/*if (newsId.equals("18176957"))
			{
				daogouDetail = null;
			}*/
			if (daogouDetail != null) {
				// 引入html模板
				File file = new File(Constant.HTML_TEMPLATE + "daogoutemplate.html");
				
				// 文件转成String
				String str = AppHtmlUtil.readFileToString(file);
				
				if (str != null) {
					if (str.contains("newstitle")) {
						str = str.replace("newstitle", daogouDetail.get("newsline"));
					}
					if (str.contains("newsday")) {
						str = str.replace("newsday", daogouDetail.get("newsday"));
					}
					if (str.contains("quarry")) {
						str = str.replace("quarry", daogouDetail.get("quarry"));
					}
					if (str.contains("jianzhumianji")) {
						if(null!=city && "重庆".equals(city)){
							str = str.replace("jianzhumianji", "本页涉及面积，无特殊说明均指套内面积");
						}else{
							str = str.replace("jianzhumianji", "本页涉及面积，无特殊说明均指建筑面积");
						}
					}
				}
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 2time use:" + (outTime-inTime) + " ms");
				// 格式化模板
				Document doc = Jsoup.parse(str);

				Elements keywords = doc.select("meta[name=keywords]");
				Elements description = doc.select("meta[name=description]");
				if (keywords != null && keywords.size() > 0) {
					keywords.get(0).attr("content", daogouDetail.get("newskey"));
				}
				if (description != null && description.size() > 0) {
					description.get(0).attr("content", daogouDetail.get("news_description"));
				}
				
				String newscontent = daogouDetail.get("newscontent");
				//处理 iframe
				String buyForMe = "";
				//把iframe 替换掉 再诸葛添加为我买房
				/*
				 * 直接修改原来的 newscontent，只有一个
				 * */
//				if(newscontent.contains("iframe")){
////					iframes.remove();
//					newscontent=buyForMe(newscontent,city);
//				}
				//格式化newscontent，避免出现无效的xmlChar
				newscontent = AppHtmlUtil.parseXmlChar(newscontent);
				newscontent = newscontent.replaceAll("(<<<|>>>|《《《|《《|》》》|》》|》|&lt;&lt;&lt;|&gt;&gt;&gt;&gt;|&gt;&gt;&gt;|&gt;&gt;)", "");
				//去掉需要屏蔽的内容
				
				if(newscontent.contains("<!--wirelessHiddenContentBegin-->") && newscontent.contains("<!--wirelessHiddenContentEnd-->")){
					newscontent = newscontent.replace(newscontent.substring(newscontent.indexOf("<!--wirelessHiddenContentBegin-->"),newscontent.indexOf("<!--wirelessHiddenContentEnd-->")), "");
				}
				//logger.info("原始数据:" + newscontent);
				//导购头
				Element eConWord = doc.getElementById("conWord");
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 3time use:" + (outTime-inTime) + " ms");
				
				/// 为我买房   ......
				if(newscontent.contains("iframe")){
					
					Document docByForMe = Jsoup.parse(newscontent);
					Elements iframesBuyForMe = docByForMe.select("iframe");// redBagDoc = DocumentHelper.parseText(redBags[i]);
					
					System.out.println(".."+iframesBuyForMe.size());
//					iframes.remove();
					for(int i=0;i<iframesBuyForMe.size();i++){
//						iframes.remove(); 修改 iframe
							System.out.println(iframesBuyForMe.get(i));
							String urlName = iframesBuyForMe.get(i).toString();
							if(urlName.contains("HelpYouBuyHouse")||urlName.contains("pcRedBag")||urlName.contains("PingTaiRedBag")){
								newscontent = newscontent.replace(iframesBuyForMe.get(i).toString(), buyForMe(iframesBuyForMe.get(i).toString(),city));//buyForMe(iframesBuyForMe.get(i).toString(),city);
							}else{
								newscontent = newscontent.replace(iframesBuyForMe.get(i).toString(), buyForMe(iframesBuyForMe.get(i).toString(),city));//iframesBuyForMe.get(i).remove();
							}
							
						}
//					buyForMe=buyForMe(city);
				}
				//分离所有楼盘信息列表
				String[] louPanInfos = AppHtmlUtil.getLoupanList(newscontent);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 4time use:" + (outTime-inTime) + " ms");
				//判断是否房源
				boolean hasLoupan = true;
				if(null == louPanInfos || louPanInfos.length == 0 || (louPanInfos.length == 1 && null == louPanInfos[0])){
					hasLoupan = false;
				}else{
					boolean hasOne = false;
					for(int i=0;i<louPanInfos.length;i++){
						if(null != louPanInfos[i]){
							hasOne = true;
							break;
						}
					}
					if(!hasOne){
						hasLoupan = false;
					}
				}
				//没有房源
				if(!hasLoupan){
					//电话协议
					newscontent = AppHtmlUtil.addTelephoneProtocol(newscontent);
					eConWord.html(newscontent);
					
					//去掉body中的form script link 标签
					Elements bodys = doc.select("body");
					if(null != bodys && bodys.size() > 0){
						Elements links = bodys.get(0).select("link");
						if(null != links && links.size() >0){
							links.remove();
						}
						Elements scripts = bodys.get(0).select("script");
						if(null != scripts && scripts.size() >0){
							scripts.remove();
						}
						Elements forms = bodys.get(0).select("form");
						if(null != forms && forms.size() >0){
							forms.remove();
						}
					}
					
					Elements dianpingPs = doc.select("strong");
					for(int i =0;i < dianpingPs.size();i++){
						if(dianpingPs.get(i).hasText()){
							if(dianpingPs.get(i).text().contains("猛戳") 
									|| dianpingPs.get(i).text().contains("点击") 
									|| dianpingPs.get(i).text().contains("查看小区信息")
									||"房贷计算器".equals(dianpingPs.get(i).text())
									||dianpingPs.get(i).text().contains("了解更多")){
								dianpingPs.get(i).remove();
							}
						}
					}
					Elements fonts = doc.select("font");
					if(null != fonts){
						for(int i=0;i<fonts.size();i++){
							if(fonts.get(i).hasText()){
								if("查看小区信息".equals(fonts.get(i).text()) 
										|| "查房价".equals(fonts.get(i).text())
										||"房贷计算".equals(fonts.get(i).text())
										|| fonts.get(i).text().matches("更多.*?看这里")){
									fonts.get(i).remove();
									continue;
								}
							}
						}
					}
					
					//去掉iframe
					Elements iframes = doc.select("iframe");
					if(null != iframes && iframes.size() > 0){
						iframes.remove();
					}
					//去掉分割线
					Elements hrs = doc.select("hr");
					if(null != hrs && hrs.size() > 0){
						hrs.remove();
					}
					//去掉换行br
					Elements brs = doc.select("br");
					if(null != brs && brs.size() > 0){
						brs.remove();
					}
					//去掉相关阅读延伸阅读
					AppHtmlUtil.removeDaogouMatchTag(".*?(((相关|延伸|推荐)阅读)"
							+ "|(相关推荐)|(猜你感兴趣)|(猜你也感兴趣)|"
							+ "(资讯连连看)|(猜你喜欢)|(相关房源推荐)|"
							+ "(更多公园房推荐)|(热门推荐)|(系统自动推荐)|"
							+ "(今日更多特大)|(优质房源推荐)|(特别推荐)).*", doc);
					//去掉a链接
			//		AppHtmlUtil.removeDaogouAtag(doc);
					
					Elements ps = doc.select("p");
					if(null != ps){
						for(int i=0;i<ps.size();i++){
							if(ps.get(i).hasText()){
								if(ps.get(i).text().contains("点击查看") 
										|| ps.get(i).text().contains("查看房源详情")
										|| ps.get(i).text().contains("查看更多")
										|| ps.get(i).text().contains("点击链接查看")
										||ps.get(i).text().contains("房源链接")
										|| ps.get(i).text().contains("更多信息")){
									ps.get(i).remove();
								}
							}
						}
					}
					
					//处理table中的图片
//					AppHtmlUtil.formatTableImgs(doc);
					List<String> imgUrlList = AppHtmlUtil.dealDaogouImg(doc);
					// 去掉table标签
					Elements tables = doc.select("table");
					if (tables != null && tables.size() > 0) {
						tables.remove();
					}
					Elements divs = doc.select("div[style]");
					if (divs != null && divs.size() > 0) {
						for(int i=0;i<divs.size();i++){
							if(divs.get(i).attr("style").contains("width") || divs.get(i).attr("style").contains("WIDTH")){
								divs.get(i).removeAttr("style");
							}
						}
					}
					// 处理视频
					List<Map<String,String>> videoList = AppHtmlUtil.dealVideo(doc);
					outTime = System.currentTimeMillis();
					logger.info("newsId : "+newsId + "daogou  end 5time use:" + (outTime-inTime) + " ms");
					// 生成xml文件并输出
					out.write(AppHtmlUtil.createXml("success", doc.html(), imgUrlList,videoList).asXML());
					out.close();
					return ;
				}
				//有房源
				//设置导购头
				String daogouHead = AppHtmlUtil.getDaogouHead(newscontent);
				if(null != daogouHead){
					eConWord.html(AppHtmlUtil.getDaogouHead(newscontent));
				}
				
				List<LouPanInfo> louPanList = null;
				List<String> louPanNameList = new ArrayList<String>();
				//设置楼盘的信息
				//获取newcode
				StringBuffer newCode = new StringBuffer("");
				if(louPanInfos != null && louPanInfos.length > 0){
					louPanList = new ArrayList<LouPanInfo>();
					for(String louPanStr : louPanInfos){
						if(null != louPanStr){
							//处理楼盘信息和红包信息;
							//逐个检查 iframe
							if(louPanStr.contains("iframe")){
//								iframes.remove();
//								louPanStr=buyForMe(louPanStr,city);
								
								/// 为我买房   ......
								if(louPanStr.contains("iframe")){
									
									Document docByForMe = Jsoup.parse(louPanStr);
									Elements iframesBuyForMe = docByForMe.select("iframe");// redBagDoc = DocumentHelper.parseText(redBags[i]);
									
									System.out.println(".."+iframesBuyForMe.size());
//									iframes.remove();
									for(int i=0;i<iframesBuyForMe.size();i++){
//										iframes.remove(); 修改 iframe
											System.out.println(iframesBuyForMe.get(i));
											String urlName = iframesBuyForMe.get(i).toString();
											if(urlName.contains("HelpYouBuyHouse")||urlName.contains("pcRedBag")||urlName.contains("PingTaiRedBag")){
												louPanStr = louPanStr.replace(iframesBuyForMe.get(i).toString(), buyForMe(iframesBuyForMe.get(i).toString(),city));//buyForMe(iframesBuyForMe.get(i).toString(),city);
											}else{
												louPanStr = louPanStr.replace(iframesBuyForMe.get(i).toString(), buyForMe(iframesBuyForMe.get(i).toString(),city));//iframesBuyForMe.get(i).remove();
											}
											
										}
//									buyForMe=buyForMe(city);
								}
							}
							LouPanInfo louPanInfo = new LouPanInfo();
							//楼盘所有信息字符串
							louPanInfo.setNewscontent(louPanStr);
							String code = AppHtmlUtil.getLoupanNewcode(louPanStr);
							//楼盘id
							//louPanInfo.setNewcode(AppHtmlUtil.getLoupanNewcode(louPanStr));
							louPanInfo.setNewcode(code);
							newCode.append(code+",");
							Document louPanDoc = Jsoup.parse(louPanStr);
							//楼盘名称
							String loupanName = AppHtmlUtil.getLoupanName(louPanDoc);
							louPanInfo.setLouPanName(loupanName);
							louPanNameList.add(loupanName);
							//楼盘电话
							louPanInfo.setLouPanPhone(AppHtmlUtil.getLoupanTelephone(louPanDoc));
							
							//楼盘头
							louPanInfo.setLouPanHead(AppHtmlUtil.getLoupanHeader(louPanStr));
							//楼盘名片
							louPanInfo.setLouPanCard(AppHtmlUtil.getLoupanTitsFromTableText(louPanStr));
							//楼盘bodyHeader
							louPanInfo.setLouPanBodyHeader(AppHtmlUtil.getLoupanBodyHeader(louPanStr));
							//楼盘bodyItem
							louPanInfo.setLouPanBodyItem(AppHtmlUtil.getLoupanBodyItems(louPanStr));
							
							louPanList.add(louPanInfo);
						}
						
					}
				}
				
				
				//String newCode = getCodes(louPanList);
				//检查红包信息
				String redBag = checkRedBag(newCode.toString(),city);
				//查看看房团信息
				String[] kanfang = kanfang(newCode.toString());
				boolean hasKanFang = false;
				if(kanfang!=null){
					hasKanFang = true;
				}
				Element mainConBox = doc.getElementById("mainConBox");
				File louPanFile = new File(Constant.HTML_TEMPLATE + "loupan.html");
				String louPanStr = AppHtmlUtil.readFileToString(louPanFile);
				
				if(louPanList != null && louPanList.size()>0){
					//楼盘信息头div
					String louPanHtml = louPanStr.replace("louPanName", "<a href='soufun://waptoapp/{&quot;destination&quot;:&quot;xfdetail&quot;,&quot;newcode&quot;:&quot;"+louPanList.get(0).getNewcode() +"&quot;}'>" + louPanList.get(0).getLouPanName() + "</a>");
					louPanHtml = louPanHtml.replace("starNum", louPanList.get(0).getLouPanStarNum());
					louPanHtml = louPanHtml.replace("dianPing",louPanList.get(0).getLouPanDianPing());
					louPanHtml = louPanHtml.replace("telephoneNum",louPanList.get(0).getLouPanPhone());
					StringBuffer nameList = new StringBuffer();
					//楼盘名称列表
					int index = 1;
					for(String louPanName : louPanNameList){
						if(null != louPanName && louPanName.equals(louPanList.get(0).getLouPanName())){
							nameList.append("<li class='active'><a href='javascript:;' onclick=\"showLoupan('" + louPanName + "','"+index+"')\">" + louPanName + "</a></li>");
						}else{
							nameList.append("<li><a href='javascript:;' onclick=\"showLoupan('" + louPanName + "','"+index+"')\">" + louPanName + "</a></li>");
						}
						index++;
					}
					
					louPanHtml = louPanHtml.replace("nameList", nameList);
					
					
					/// 为我买房   ......
					if(newscontent.contains("iframe")){
						
						Document docByForMe = Jsoup.parse(newscontent);
						Elements iframesBuyForMe = docByForMe.select("iframe");// redBagDoc = DocumentHelper.parseText(redBags[i]);
						
						System.out.println(".."+iframesBuyForMe.size());
//						iframes.remove();
						for(int i=0;i<iframesBuyForMe.size();i++){
//							iframes.remove(); 修改 iframe
								System.out.println(iframesBuyForMe.get(i));
								String urlName = iframesBuyForMe.get(i).toString();
								if(urlName.contains("HelpYouBuyHouse")||urlName.contains("pcRedBag")||urlName.contains("PingTaiRedBag")){
									newscontent = newscontent.replace(iframesBuyForMe.get(i).toString(), buyForMe(iframesBuyForMe.get(i).toString(),city));//buyForMe(iframesBuyForMe.get(i).toString(),city);
								}else{
									iframesBuyForMe.get(i).remove();
								}
								
							}
//						buyForMe=buyForMe(city);
					}
					//为我买房
//					Elements iframes = doc.select("iframe");
//					if(null != iframes && iframes.size() > 0){
//						iframes.remove();
//					}
					
					//楼盘信息体div
					StringBuffer louPaninfoDiv = new StringBuffer();
					System.out.println(newscontent.contains("iframe")+"***********"+str.concat("iframe"));
					for(int i =0;i<louPanList.size();i++){
						LouPanInfo louPan = louPanList.get(i);
						//楼盘信息
						StringBuffer louPanInfo = new StringBuffer();
						String loupanInfoStr = null;
						//如果楼盘头，名片，地理位置，item都是空的
						if(StringUtil.isEmpty(louPan.getLouPanHead()) && StringUtil.isEmpty(louPan.getLouPanCard()) 
								&& StringUtil.isEmpty(louPan.getLouPanBodyHeader()) && (null == louPan.getLouPanBodyItem() || louPan.getLouPanBodyItem().size() == 0)){

//							//添加红包
//							addRedBag(louPan, redBagStr);
							//将所有楼盘信息加入
							louPanInfo.append(louPan.getNewscontent());
							
						}else{
							//楼盘头
							louPanInfo.append(louPan.getLouPanHead());
							
							//楼盘名片
							if(!StringUtil.isEmpty(louPan.getLouPanCard())){
								louPanInfo.append("<div class='lp-item'><div class='item-tit'>");
								louPanInfo.append("<a type='telephone' href='soufun://waptoapp/{&quot;destination&quot;:&quot;xfdetail&quot;,&quot;newcode&quot;:&quot;"+louPan.getNewcode() +"&quot;,&quot;type&quot;:&quot;2&quot;}'>"+louPan.getLouPanName());
								louPanInfo.append("名片</a></div><div class='item-intro'>");
								louPanInfo.append(louPan.getLouPanCard());
								louPanInfo.append("</div></div>");
							}
//							//加上红包属性
//							louPanInfo.append(addRedBag(louPan, redBag));
//							if(hasKanFang){
//								for(int j=0;j<kanfang.length;j++){
//									if(kanfang[j].contains(louPan.getNewcode())){
//										System.out.println("看房信息:"+kanfang[j]);
//										louPanInfo.append(kanfang[j]);
//										System.out.println("楼盘信息:"+louPanInfo);
//									}
//								}	
//							}
							//楼盘地理位置
							if(louPan.getLouPanBodyHeader() != null && louPan.getLouPanBodyHeader().trim().length() >0){
								louPanInfo.append("<div class='lp-item'><div class='item-tit'>地理位置</div><div class='mt10'>");
								louPanInfo.append(louPan.getLouPanBodyHeader());
								louPanInfo.append("</div></div>");
							}
							//楼盘item
							for(String item : louPan.getLouPanBodyItem()){
								louPanInfo.append("<div class='lp-item'><div class='item-tit'>");
								louPanInfo.append(item.substring(item.indexOf("<strong>")+8, item.indexOf("</strong>")));
								louPanInfo.append("</div><div class='mt10'>");
								louPanInfo.append(item.substring(item.indexOf("</p>")+4,item.length()));
								louPanInfo.append("</div></div>");
							}
						}
						
						
//						//加上红包属性
//						louPanInfo.append(addRedBag(louPan, redBag));
						//添加帮我买房信息
//						Elements iframes = doc.select("iframe");
//						if(null != iframes && iframes.size() > 0){
//							iframes.remove();
//							louPanInfo.append(buyForMe);
//						}
						//楼盘信息 是否包含 看房团class   hd-title hd
//						if(louPanInfo.toString().contains("hd-title hd")){
//							kanfang(louPanInfo.toString());1
////							..添加看房团信息;1
//						}
//						if(hasKanFang){
//							for(int j=0;j<kanfang.length;j++){
//								if(kanfang[j].contains(louPan.getNewcode())){
//									louPanInfo.append(kanfang[j]);
//								}
//							}	
//						}
						
						//加上红包属性  加在whitebg 下 之前加在 conWord 下
						StringBuffer bag_kanfang = new StringBuffer("");
						bag_kanfang.append(addRedBag(louPan, redBag));
						if(hasKanFang){
							for(int j=0;j<kanfang.length;j++){
								if(kanfang[j].contains(louPan.getNewcode())){
									bag_kanfang.append(kanfang[j]);
								}
							}	
						}
						//团购
						bag_kanfang.append(tuangou(city,louPan.getNewcode()));
					   //为我买房
//						bag_kanfang.append(buyForMe);
						//添加楼盘体div
						if(i == 0){
							loupanInfoStr = "<div class='whitebg' style='display:block' id='" + louPan.getLouPanName()+ "' telephone='"+louPan.getLouPanPhone() +"' newcode='"+louPan.getNewcode() +"'><div class='conWord'>" + louPanInfo + "</div>"+bag_kanfang.toString()+"</div>";
						}else{
							loupanInfoStr = "<div class='whitebg' style='display:none' id='"+louPan.getLouPanName() +"' telephone='"+louPan.getLouPanPhone() +"' newcode='"+louPan.getNewcode() +"'><div class='conWord'>" + louPanInfo + "</div>"+bag_kanfang.toString()+"</div>";
						}
						
						
						//给楼盘图片加上loupan属性
						loupanInfoStr = AppHtmlUtil.nameLoupanImg(louPan.getLouPanName(),loupanInfoStr);
						
						//给楼盘视频加上楼盘属性
						loupanInfoStr = AppHtmlUtil.nameLoupanVideo(louPan.getLouPanName(),loupanInfoStr);
						
						louPaninfoDiv.append(loupanInfoStr);
						
					}
					//添加楼盘信息
					mainConBox.append(louPanHtml+louPaninfoDiv.toString());
				}
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 6time use:" + (outTime-inTime) + " ms");
				//去掉iframe
//				Elements iframes = doc.select("iframe");
//				if(null != iframes && iframes.size() > 0){
//					iframes.remove();
//				}
				//去掉分割线
				Elements hrs = doc.select("hr");
				if(null != hrs && hrs.size() > 0){
					hrs.remove();
				}
				
				//去掉展示楼盘名字的标签
				Elements paspans = doc.select("a > span[style]");
				for(int i =0;i<paspans.size();i++){
					if(paspans.get(i).attr("style").contains("微软雅黑") && paspans.get(i).attr("style").contains("DISPLAY") && !paspans.get(i).attr("style").contains("POSITION")){
						paspans.get(i).parent().parent().remove();
					}
				}
				
				//去掉另一种展示楼盘名字的标签
				Elements loupanNameElements = doc.select("font[size=\"5\"]");
				if(null != loupanNameElements &&loupanNameElements.size()>0 ){
					loupanNameElements.remove();
				}
				
				//去掉我要点评  去掉推荐理由
				Elements dianpingPs = doc.select("strong");
				for(int i =0;i < dianpingPs.size();i++){
					if(dianpingPs.get(i).hasText()){
						if(null == dianpingPs.get(i).children() || dianpingPs.get(i).children().size() == 0){
							if(dianpingPs.get(i).text().contains("我要点评") || dianpingPs.get(i).text().contains("推荐理由") 
									|| dianpingPs.get(i).text().contains("点此报名") || dianpingPs.get(i).text().contains("点击查看")
									|| dianpingPs.get(i).text().contains("戳这里") || "您".equals(dianpingPs.get(i).text())
									|| dianpingPs.get(i).text().contains("精彩推荐") || dianpingPs.get(i).text().contains("猛戳")
									|| dianpingPs.get(i).text().contains("精彩链接") || dianpingPs.get(i).text().contains("报名")){
								dianpingPs.get(i).remove();
								continue;
							}
						}
						if(dianpingPs.get(i).text().contains("转") && !dianpingPs.get(i).text().contains("转发")){
							dianpingPs.get(i).remove();
							
						}
					}
				}
				
				//去掉TOP10
				Elements fonts = doc.select("font");
				if(null != fonts ){
					for(int i=0;i<fonts.size();i++){
						if(fonts.get(i).hasText() && (null == fonts.get(i).children() || fonts.get(i).children().size() == 0)){
							if(fonts.get(i).text().contains("TOP10") || fonts.get(i).text().contains("我要点评")
									|| fonts.get(i).text().contains(">>") || fonts.get(i).text().contains("点此查看")
									|| fonts.get(i).text().contains("<<") || fonts.get(i).text().contains("点击")){
								fonts.get(i).remove();
							}
						}
					}
				}
				
				//去掉相关阅读延伸阅读
				AppHtmlUtil.removeDaogouMatchTag("(相关|延伸|推荐)阅读.*", doc);
				
				//如果只有一个楼盘
				if(null != louPanNameList && louPanNameList.size() == 1){
					Elements tels = doc.select(".nav-icon");
					tels.remove();
				}
				//去掉链接
				AppHtmlUtil.removeDaogouAtag(doc);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 7time use:" + (outTime-inTime) + " ms");
				//处理table中的图片
				AppHtmlUtil.formatTableImgs(doc);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 8time use:" + (outTime-inTime) + " ms");
				List<String> imgUrlList = AppHtmlUtil.dealDaogouImg(doc);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 9time use:" + (outTime-inTime) + " ms");
				// 去掉table标签
				Elements tables = doc.select("table");
				if (tables != null && tables.size() > 0) {
					tables.remove();
				}
				
				//去掉style有 WHITE-SPACE的span样式
				Elements pspans = doc.select("p > span");
				for(int i=0;i<pspans.size();i++){
					if(pspans.get(i).attr("style") != null && pspans.get(i).attr("style").contains("WHITE-SPACE")){
						pspans.get(i).removeAttr("style");
					}
				}
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 10time use:" + (outTime-inTime) + " ms");
				//处理导购视频
				List<String> videoList = AppHtmlUtil.dealDaogouVideo(doc);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 11time use:" + (outTime-inTime) + " ms");
				//去掉空白p标签
				AppHtmlUtil.removeDaogouBlankPTag(doc);
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + "daogou  end 12time use:" + (outTime-inTime) + " ms");
				// 生成xml文件并输出
				out.write(AppHtmlUtil.createDaogouXml("success", doc.html(), imgUrlList,videoList,louPanNameList.get(0),louPanList.get(0).getNewcode(),"").asXML());
				outTime = System.currentTimeMillis();
				logger.info("newsId : "+newsId + " daogou  end 13time use:" + (outTime-inTime) + " ms");
				out.close();
			}else{
				out.write(AppHtmlUtil.createXml("failed", "无此条记录", null,null).asXML());
				out.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//	public String getCodes(List<LouPanInfo> louPanList){
//		StringBuffer newcodes = new StringBuffer();
//		String codeLen = null;
//		int len = 0;
//		for(int i =0;i<louPanList.size();i++){
//			LouPanInfo louPan = louPanList.get(i);
//		//拼接 newcodes . city 默认相同
//		if(louPan!=null){
//			newcodes.append(louPan.getNewcode()+",");
//		}
//		}
//		codeLen = newcodes.toString();
//		len = codeLen .length();
//		//return newcodes.toString();
//		return codeLen.substring(0,len-1);
//	}
	
	/**
	 * 处理红包
	 * */
	public String checkRedBag(String newcode,String city){
		//先检查渠道红包 
		StringBuffer urlStr = new StringBuffer("http://api.tuan.tao.test.fang.com/RedBag2/GetRedBagListByNewCodeList?");
		urlStr.append("v="+newcode);
		//查询渠道红包
		String xml = "";
		org.dom4j.Document doc;
		try {
			xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			doc = DocumentHelper.parseText(xml);
			org.dom4j.Element root = doc.getRootElement();
			//prizeListElement.elements("prizeInfo")
			org.dom4j.Element redBag = (org.dom4j.Element)root.element("RedBag");
			StringBuffer codes = new StringBuffer("");
			StringBuffer codes1 = new StringBuffer("");
			if(redBag!=null){
				//记录渠道红包存在的 newcode
				for(int i=0;i<root.elements("RedBag").size();i++){
					org.dom4j.Element redBaglement = (org.dom4j.Element) root.elements("RedBag").get(i);
					org.dom4j.Element code = redBaglement.element("NewCode");
					String newCode = "";
					if(code!=null){
						//返回渠道红包的 newCode
						newCode = code.getText();
//						if(codes.toString().contains(newCode)){
//							codes.append(newCode+",");
//						}
						//记录渠道红包存在不重复的code
						if(!codes.toString().contains(newCode)){
							codes.append(newCode+",");
						}
					}
				}
				//渠道红包存在的不重复的code个数
//				String[] codesSplit = codes.toString().split(",");
				//传过来的 code
				String[] codeSplit = newcode.trim().split(",");
				int len = codeSplit.length;
//				//比较渠道返回的 code 和 传过来的 code 个数是否相等不相等则 同时存在
//				if(len!=codesSplit.length){
					for(int i=0;i<len;i++){
						//存在渠道红包的code里面 不包含传过来的code
						if(!codes.toString().contains(codeSplit[i])){
//							if(i!=len){
								codes1.append(codeSplit[i]+",");
//							}else{
//								codes1.append(codeSplit[i]);
//							}
						}
//					}
				}
					//渠道红包存在标识
					xml = xml+"PLATFORM_BAG"+checkReadBagPlat(codes1.toString(),city);
			}else{
				//渠道红包为空
				//查询平台红包
				xml = checkReadBagPlat(newcode,city);
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		return xml;
	}
	
	public String checkReadBagPlat(String newcode,String city){
		StringBuffer urlStrPlat =  new StringBuffer("http://jkcard.test.fang.com/Other/RedBagDetail.aspx?");
		 urlStrPlat.append("newcode="+newcode);//+"1010782763");
		 urlStrPlat.append("&city="+city);
		 String xml = null;
		 org.dom4j.Document doc;
		try {
			xml = StringUtil.getUrlTxtByGBK(urlStrPlat.toString());
			//判断是否存在
			doc = DocumentHelper.parseText(xml);
			org.dom4j.Element priceInfo = doc.getRootElement();
			org.dom4j.Element price = (org.dom4j.Element) priceInfo.element("price");
			if(price!=null){
				//平台红包不为空
				return xml;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return "";
	}
	//处理查询红包返回的 xml和 楼盘信息 
	public String addRedBag(LouPanInfo louPan,String redBagXml){
		File redBagFile = null;
//		boolean hasRedBag = false;
		String redBagHtmlAll = "";
		String redBagHtml="";
		String redBagHtmlBuff="";
		String newcode = "";
		String[] redBags = null;
		org.dom4j.Document redBagDoc = null;
		if(redBagXml!=null&&redBagXml!=""){
			newcode = louPan.getNewcode();
//			hasRedBag = true;
			redBagFile = new File(Constant.HTML_TEMPLATE + "daogouredbag.html");
			try {
				redBagHtmlBuff = AppHtmlUtil.readFileToString(redBagFile);
				redBagHtml = redBagHtmlBuff;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//渠道红包存在标识,PLATFORM_BAG 拼接红包使用的参数
			if(!redBagXml.contains("PLATFORM_BAG")){
				//渠道红包不存在
				try {
					redBagDoc = DocumentHelper.parseText(redBagXml);
					return addPlatFormRedBag(newcode,redBagDoc,redBagHtml);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				 redBags = redBagXml.split("PLATFORM_BAG");
				 for(int i=0;i<redBags.length;i++){
					 try {
						 redBagDoc = DocumentHelper.parseText(redBags[i]);
						 int code_bagNum = 0;
						 if(i==0){//渠道红包
							 org.dom4j.Element root = redBagDoc.getRootElement();
							 for(int j=0;j<root.elements("RedBag").size();j++){
								 if(j!=0){
									 redBagHtml = redBagHtmlBuff;
//								 try {//重置
//										redBagHtml = AppHtmlUtil.readFileToString(redBagFile);
//									} catch (Exception e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
								 }
								 org.dom4j.Element redBaglement = (org.dom4j.Element) root.elements("RedBag").get(j);
								 org.dom4j.Element code = redBaglement.element("NewCode");
								 if(newcode.equals(code.getText())){
									 code_bagNum+=1;
									 //每个code包含的红包个数
									 if(code_bagNum>1){
										 //把 ul等截取
										 redBagHtml=redBagHtml.substring(redBagHtml.indexOf("<li"));
										 System.out.println("截取后:"+redBagHtml);
									 }
//									 if(code_bagNum<3){
//										 redBagHtml=redBagHtml.replace("更多红包", "");
//									 }
									 org.dom4j.Element aid = redBaglement.element("Aid"); 
									 String Aid = "";
									 if(aid!=null){
										 Aid = aid.getText();
									 }
									 org.dom4j.Element id = redBaglement.element("Id"); 
									 //添加 渠道红包领取  第一个 href
//									 http://api.tuan.tao.fang.com/RedBagChannel/Apply?v= F66DC417E227925C49D4E39A9F9E68FCA5CEE88828EFDAB4301A3C5D4FC8AA06
									String url = redBagHtml.substring(redBagHtml.indexOf("<li"),
												redBagHtml.indexOf("<h4"));
									String ul= "soufun://waptoapp/{&quot;destination&quot;:&quot;AddRedBag&quot;,&quot;Aid&quot;:&quot;"+Aid+"&quot;,&quot;redbagid&quot;:&quot;"+id.getText()+"&quot;}";//+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+touserId+"&quot;,&quot;username&quot;:&quot;"+touserName+"&quot;}' class='fblue'>" +tonickName+"</a>" + ":"+content+"</div>";
										String btn = url.replace("#", ul);
										//替换html 里面的 元素为 xml节点元素 <a href="#">
//									System.out.println("红包链接:"+redBagHtml.substring(redBagHtml.indexOf("<li"),redBagHtml.indexOf("<div")));
									//替换html 里面的 元素为 xml节点元素 <a href="#">
									redBagHtml=redBagHtml.replace(url, btn);
									
									 org.dom4j.Element count = redBaglement.element("ReceivedCount");
									 org.dom4j.Element money = redBaglement.element("Money"); 
									 org.dom4j.Element style = redBaglement.element("HuXing");
									 org.dom4j.Element start = redBaglement.element("StartDay");
									 org.dom4j.Element end = redBaglement.element("EndDay");
									 //判断 楼盘信息newcode是否与 xml里的匹配
									 redBagHtml=redBagHtml.replace("num", count.getText());
									 redBagHtml=redBagHtml.replace("money", money.getText());
									 redBagHtml=redBagHtml.replace("leave_house", style.getText());
									 redBagHtml=redBagHtml.replace("time", start.getText()+"-"+end.getText());
									 /*
									  * 
									  * 
									  * </ul>
</div>
 <a class="moreStyle" href="javascript:" onclick="moreBag()"><span class="btn-more">更多红包</span></a>
</div>
									  * 
									  * */
									 //单个红包拼接 在 li的 后面拼接
									 redBagHtmlAll+=redBagHtml;	 
								 }
							 }
							 if(code_bagNum>2){
								 redBagHtmlAll+="</ul></div><a class='moreStyle' href=\"javascript:\" onclick=\"moreBag()\"><span class=\"btn-more\">更多红包</span></a></div>";
							 }else if(code_bagNum>0){
								 redBagHtmlAll+="</ul></div></div>";
							 }
							 
						 }else{//平台红包
							 	redBagHtml = redBagHtmlBuff;
								redBagHtmlAll+=addPlatFormRedBag(newcode,redBagDoc,redBagHtml);
						 }
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				}	
			return redBagHtmlAll;
		}
			return "";
	}
	public String addPlatFormRedBag(String newcode,org.dom4j.Document redBagDoc,String redBagHtml){

				org.dom4j.Element priceInfo = redBagDoc.getRootElement();
				org.dom4j.Element price = (org.dom4j.Element) priceInfo.element("price");
				String redBagHtmlAll = "";
				if(price!=null){
		//			for(int i=0;i<price.elements("price").size();i++){
		//				org.dom4j.Element redBaglement = (org.dom4j.Element) root.elements("RedBag").get(i);
						org.dom4j.Element code = price.element("newcode");
						if(newcode.equals(code.getText())){
							org.dom4j.Element count = price.element("signupcount");
							org.dom4j.Element money = price.element("redmone"); 
							org.dom4j.Element style = price.element("house_level");
							org.dom4j.Element citys = price.element("city");
							org.dom4j.Element start = price.element("startday");
							org.dom4j.Element end = price.element("endday");
							//判断 楼盘信息newcode是否与 xml里的匹配
							//编码城市
							String city ="";
							if(citys!=null){
								city = citys.getText();
								if(!city.equals("")&&city!=null){
									try {
										city = (URLEncoder.encode(city, "gb2312"));
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
//							String url = redBagHtml.substring(redBagHtml.indexOf("<li"),
//									redBagHtml.indexOf("<li")+19);
							String url = redBagHtml.substring(redBagHtml.indexOf("<li"),
									redBagHtml.indexOf("<h4"));
							String ul= "soufun://waptoapp/{&quot;destination&quot;:&quot;AddPlatRedBag&quot;,&quot;city&quot;:&quot;"+city+"&quot;,&quot;newcode&quot;:&quot;"+newcode+"&quot;}";//+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+touserId+"&quot;,&quot;username&quot;:&quot;"+touserName+"&quot;}' class='fblue'>" +tonickName+"</a>" + ":"+content+"</div>";
							String btn = url.replace("#", ul);
							//替换html 里面的 元素为 xml节点元素 <a href="#">
							redBagHtml=redBagHtml.replace(url, btn);
//							redBagHtml=redBagHtml.replace(redBagHtml.substring(redBagHtml.indexOf("em>")+3,redBagHtml.indexOf("</em>")), btn);
//							redBagHtml=redBagHtml.replace("更多红包", "");
							//替换html 里面的 元素为 xml节点元素
							redBagHtml=redBagHtml.replace("num", count.getText());
							redBagHtml=redBagHtml.replace("money", money.getText());
							redBagHtml=redBagHtml.replace("style", style.getText());
			//				redBagHtml.replace("time", start.getText()+"-"+end.getText()+"暂不支持使用");
							if(start!=null&&end!=null){
								redBagHtml=redBagHtml.replace("time", start+"--"+end);
							}else{
								redBagHtml=redBagHtml.replace("time", "暂不支持使用，可先预领");
							}
							redBagHtmlAll+=redBagHtml;	
							return redBagHtmlAll+"</ul></div></div>";
			//			}
						}else{
							return "";
						}
				}else{
					return "";
				}
	
	}
	
	public String[] kanfang(String newcode){
		/**
			<div class="hd-title hd">
			<a href="#">
			<span class="btn-more">独享会员团购优惠、看房活动专车</span>
			</a>
			</div>;
		 * phone=*&name=*&email=*&code=*&codetype=*&city=*&newcode=*&projname=*&source=*
		 * &projectid=*&key=*&type=*&sex=*&word=*&hgainmeg=*&imei=*&mediumflag=*&global_cookie=*
		 * &unique_cookie=*&coordx=*&coordy=*&operatingsystem=*
		 * &model=*&channel=*&housetypeid=*&houseid=*&newsid=*&NewsEditor=* 
		 * */
		String urlStr = "http://jk.card.fang.com/NewHouse/GetSignUpLookHouseByNewCodes.aspx?projectid=1013&key=713712e9929a1431bef223f44de72105&newcodes="+newcode.substring(0,newcode.length()-1);//.replace(",","%2C");
		String xml = null;
		 org.dom4j.Document doc;
			try {
				xml = StringUtil.getUrlTxtByGBK(urlStr);
				if(xml.length()!=0){
					System.out.println(xml);
				doc = DocumentHelper.parseText(xml);
				org.dom4j.Element soufun_card = doc.getRootElement();
				org.dom4j.Element houses = soufun_card.element("Houses");
				if(houses!=null){
					org.dom4j.Element code = null;
					org.dom4j.Element signUpName = null;
					org.dom4j.Element House = soufun_card.element("Houses").element("House");
					if(House!=null){
					String[] kanfang = new String[houses.elements("House").size()];
					for(int i=0;i<houses.elements("House").size();i++){
						//获取每个房子
						org.dom4j.Element house = (org.dom4j.Element) houses.elements("House").get(i);
						code = house.element("Newcode");
						signUpName = house.element("SignUpName");
						//newcode 匹配
						if(code!=null){
							if(newcode.contains(code.getText())){
								StringBuffer button1 = new StringBuffer("<div class='kftbox mb20'><a href='soufun://waptoapp/{&quot;destination&quot;:&quot;Kanfang&quot;,");
//								StringBuffer button1 = new StringBuffer("<div class=\"kftbox mb20\"><a href='soufun://waptoapp/{&quot;destination&quot;:&quot;Kanfang&quot;,&quot;phone&quot;:&quot;"+""+"&quot;,&quot;code&quot;:&quot;");
								String signname = signUpName.getText();
									org.dom4j.Element signUpCity = house.element("SignUpCity");
									org.dom4j.Element lineID = house.element("LineID");
									org.dom4j.Element signUpCount = house.element("SignUpCount");
//									if(!signname.equals("")){
//										int start = signname.indexOf("<![CDATA[");
//										int end = signname.indexOf("]]>");
//										signname = signname.substring(start+10, end);
//									}
									String signUpNum = "0";
									if(signUpCount!=null&&!signUpCount.getText().equals("")){
										signUpNum = signUpCount.getText();
									}
									//加上看房信息
								 	button1.append("&quot;lookhouseID&quot;:&quot;713712e9929a1431bef223f44de72105");
								 	button1.append("&quot;,&quot;projectid&quot;:&quot;1013");
								 	button1.append("&quot;,&quot;city&quot;:&quot;");
								 	button1.append(URLEncoder.encode(signUpCity.getText(),"gb2312")+"&quot;,&quot;newcode&quot;:&quot;");
								 	button1.append(code.getText()+"&quot;,&quot;LineID&quot;:&quot;");
								 	button1.append(lineID.getText()+"&quot;,&quot;SignName&quot;:&quot;");
								 	button1.append(URLEncoder.encode(signname,"gb2312")+"&quot;,&quot;signUpNum&quot;:&quot;");
								 	button1.append(signUpNum+"&quot;");
								 	button1.append("}'><span class=\"font01\">");
								 	button1.append(signname);
								 	button1.append("</span>");
								 	button1.append("<em>");
								 	button1.append(signUpNum);
								 	button1.append("人已报名</em></a></div>");
								 	
								kanfang[i] = button1.toString();
							}	
						}
						}
						return kanfang;
					}
					}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//活动button
		//解析xml：使用dom4j
//		org.dom4j.Document doc = DocumentHelper.parseText(loupanInfo);  
//		org.dom4j.Element root = doc.getRootElement();
//		org.dom4j.Element needsignElement = activityElement.element("needsign");
//		StringBuffer button1 = new StringBuffer("<div class=\"hd-title hd\"><a href='soufun://waptoapp/{&quot;destination&quot;:&quot;Kanfang&quot;,&quot;phone&quot;:&quot;"+phone+"&quot;,&quot;name&quot;:&quot;");
//		 	button1.append(name+"&quot;,&quot;email&quot;:&quot;");
//		 	button1.append(email+"&quot;,&quot;code&quot;:&quot;");
//		 	button1.append(code+"&quot;,&quot;codetype&quot;:&quot;");
//		 	button1.append(codetype+"&quot;,&quot;city&quot;:&quot;");
//		 	button1.append(city+"&quot;,&quot;newcode&quot;:&quot;");
//		 	button1.append(newcode+"&quot;,&quot;projname&quot;:&quot;");
//		 	button1.append(source+"&quot;,&quot;projectid&quot;:&quot;");
//		 	button1.append(projectid+"&quot;,&quot;key&quot;:&quot;");
//		 	button1.append(key+"&quot;,&quot;type&quot;:&quot;");
//		 	button1.append(type+"&quot;,&quot;mediumflag&quot;:&quot;"+mediumflag);
//		 	button1.append("&quot;}'><span class=\"btn-more\">独享会员团购优惠、看房活动专车</span></a></div>");
//		 	if(null != needsign && "1".equals(needsign)){
//		 		activityHtml = activityHtml.replace("activity_button",button);}
			return null;
	}
	public String tuangou(String city,String newcode){
		//团购
		
		//<div class="lp-nav">
		
		StringBuffer button = new StringBuffer("<div class=\"kftbox mgY10\"><a href='soufun://waptoapp/{&quot;destination&quot;:&quot;tuangou&quot;,");
		 	button.append("&quot;city&quot;:&quot;");
		 	button.append(city+"&quot;,&quot;newcode&quot;:&quot;");
		 	button.append(newcode);
		 	button.append("&quot;}'><span class=\"font02\">楼盘动态抢先掌握 独享会员团购优惠</span></a></div>");
			return button.toString();
	}
	private  final String  NORTH_CITY="北京,长春,大连,哈尔滨,济南,青岛,石家庄,沈阳,天津,太原,郑州,烟台,潍坊,唐山,呼和浩特,吉林,保定,包头,威海,日照,邯郸,洛阳,淄博,廊坊,鞍山,菏泽,聊城,秦皇岛,东营,鄂尔多斯,晋城,德州,美国,澳大利亚,西班牙,日本,新乡,营口,衡水,临沂,大庆,运城,沧州,邢台,张家口,承德,大同,阳泉,长治,朔州,晋中,忻州,临汾,吕梁,抚顺,本溪,丹东,锦州,阜新,辽阳,盘锦,铁岭,朝阳,葫芦岛,四平,辽源,通化,白山,松原,白城,延边,齐齐哈尔,鸡西,鹤岗,双鸭山,伊春,佳木斯,七台河,牡丹江,黑河,绥化,枣庄,济宁,泰安,莱芜,德州,滨州,开封,平顶山,安阳,鹤壁,焦作,濮阳,许昌,漯河,三门峡,南阳,商丘,信阳,周口,驻马店,固安,燕郊,香河,涿州,英国,泰国,新加坡,马来西亚,加拿大,德国,韩国,新西兰,法国,塞浦路斯,菲律宾,土耳其,希腊,台湾,迪拜,意大利,巴彦淖尔,赤峰,乌兰察布,海外,汝州,高密,寿光,肥城,巩义,胶南,莱州,临猗,龙口,平度,普兰店,迁安,滕州,新泰,兴安盟,鄢陵,禹州,长葛,招远,邹城,邹平,遵化,通辽,即墨,莱西,昌邑,广饶,蓬莱,新郑,荥阳,伊川,偃师,瓦房店,东港,凤城,农安,安丘,青州,临朐,中牟,登封,肇东,宾县,安达,济阳,商河,昌乐,莱阳,新安,宜阳,新密,定州,辛集,滦县,玉田,庄河,新民,辽中,德惠,榆树,章丘,济源,呼伦贝尔,锡林郭勒盟,大兴安岭,嵩县,晋州,滦南,平阴,洛宁,孟津,汝阳,高碑店,赵县,无极,青龙,乐亭,迁西,法库,康平,五常,尚志,巴彦,依兰,清徐,临清,文安,新乐,元氏,肇州,肇源,栖霞,海阳,霸州,昌黎,平山,海林,武安,阳曲,长清,胶州,长岛,邓州,兰考,任丘,乌海";
	//帮我买房
	public String buyForMe(String frame,String city){
		/*
		 *
		 * <iframe
		style="BORDER-BOTTOM: #3983c6 0px solid; BORDER-LEFT: #3983c6 0px solid; BACKGROUND-COLOR: #ffffff; BORDER-TOP: #3983c6 0px solid; BORDER-RIGHT: #3983c6 0px solid"
		id="uploadframe" height="345" marginheight="0"
		src="http://club.fang.com/IframeSpace/HelpYouBuyHouse.aspx?newsid=19977635&newseditor=zengxiaoyan&city=%C4%CF%CD%A8"
		frameborder="0" width="635" marginwidth="2" scrolling="no"></iframe>
		 * */
		
		
		String src =frame.substring(frame.indexOf("http"),frame.indexOf("frameborder"));
		String newCity = src.substring(src.indexOf("city")+5);
		try {
			System.out.println("城市:"+newCity+","+URLDecoder.decode(newCity,"gb2312"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String button = "";
		
		if(city!=null&&city.endsWith("")){
			StringBuffer urlStr = null;
				if(NORTH_CITY.contains(newCity)){
					//北方区域
					urlStr = new StringBuffer("http://newhousejkn.fang.com/house/webinterface/multi_client_integrate.php?");
				}else{
					urlStr = new StringBuffer("http://newhousejks.fang.com/house/webinterface/multi_client_integrate.php?");
				}
			try {
				newCity = ("city="+URLEncoder.encode(city, "gb2312"));
			
			//%c9%cf%ba%a3&access_key=5033e63162ed8cb4c162441b65fecf34&districtname=
			urlStr.append("city="+newCity);
			urlStr.append("&access_key=5033e63162ed8cb4c162441b65fecf34");
			String xml = "";
			org.dom4j.Document doc;
					xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
					doc = DocumentHelper.parseText(xml);
					org.dom4j.Element root = doc.getRootElement();
					if(root!=null){
					//prizeListElement.elements("prizeInfo")
						//意向区域
					org.dom4j.Element district = (org.dom4j.Element)root.element("district");
						//意向户型
					org.dom4j.Element bedroom = (org.dom4j.Element)root.element("bedroom");
	//				File buyFile = new File(Constant.HTML_TEMPLATE + "buyForMe.html");
	//				 buyForMeHtml = AppHtmlUtil.readFileToString(buyFile);
					
	//				StringBuffer dist = new StringBuffer("");
	//				StringBuffer beedRoom = new StringBuffer("");
						if(district!=null&&bedroom!=null){
						//添加按钮
	//					 button = "<div class=\"hdBox mgY20\"><div class='hd-title fb'>" +
	//					"<a href='soufun://waptoapp/{&quot;destination&quot;:&quot;buyForMe&quot;,&quot;dist&quot;:&quot;"+district.getText()+"&quot;,&quot;beedRoom&quot;:&quot;"+bedroom.getText()+"<span class='btn-more'>一键发布您的购房需求</span></a></div></div class=\"hdBox mgY20\">";
							button ="<div class=\"kftbox mgY10\">"+
							"<a href='soufun://waptoapp/{&quot;destination&quot;:&quot;buyForMe&quot;,&quot;dist&quot;:&quot;"+district.getText()+"&quot;,&quot;beedRoom&quot;:&quot;"+bedroom.getText()+"&quot;}'><span class='btn-more'>一键发布您的购房需求</span></a></div></div>";
							frame=frame.replace(frame, button);
//							return newscontent;
						}
	//					if(district!=null){
	//					   String[] dists = district.getText().split(";");
	//						for(int i=0;i<dists.length;i++){
	//							dist.append("<option>");
	//							dist.append(dists[i]);
	//							dist.append("</option>");
	//						}
	//						buyForMeHtml=buyForMeHtml.replace("id=\"dist2015\"><option></option>", dists.toString());
	//					}
	//					if(bedroom!=null){
	//						 String[] bed = bedroom.getText().split(";");
	//							for(int i=0;i<root.elements("bedroom").size();i++){
	//								beedRoom.append("<option>");
	//								beedRoom.append(bed[i]);
	//								beedRoom.append("</option>");
	//							}
	//						 buyForMeHtml= buyForMeHtml.replace("id=\"houseType2015\"><option></option>", bed.toString());
	//					}
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//城市为空 预处理
			button ="<div class=\"kftbox mgY10\">"+
			"<a href='soufun://waptoapp/{&quot;destination&quot;:&quot;buyForMe&quot;,&quot;dist&quot;:&quot;"+""+"&quot;,&quot;beedRoom&quot;:&quot;"+"&quot;}'><span class='btn-more'>一键发布您的购房需求</span></a></div></div>";;
			frame=frame.replace(frame, button);
//			return newscontent;
		}
		return button;
	}
	
	/**
	 * 业主圈论坛信息html
	 * @param request
	 * @param response
	 */
	@RequestMapping("/luntan")
	public void getLuntanHtml(HttpServletRequest request, HttpServletResponse response){
		/*HttpServletRequest sq = (HttpServletRequest) request;
		String str = sq.getQueryString();
//		String requestURL = sq.getServerName() + ":" + sq.getServerPort()
//		+ sq.getContextPath() + sq.getServletPath() + "?" + str;
		System.out.println("str******:"+str);*/
//		city=北京&from=AndroidApp&masterId=156734919&messagename=luntanMaincontent&order=asc&page=1&pagesize=20&sender=AndroidApp&sign=1010729501
		//&userid=63937629&wirelesscode=EA147B267390634D0FB61D1B90126C5D
		try{
			logger.info("luntan begin");
			long inTime = System.currentTimeMillis();
			StringBuffer cacheKeyStr = new StringBuffer("");//为拼接wirelesscode
			StringBuffer cacheKeyStrNew = new StringBuffer("");//为拼接wirelesscode
			String urlOfCity = request.getParameter("urlOfCity");
			
			StringBuffer urlStr = new StringBuffer(urlOfCity + "api/post/getReplyPostListForApp.php?");
			
			String city = request.getParameter("city");
			/*if(!"北京".equals(city)){
				city="北京";
				
			}*/
//			city="%E5%8C%97%E4%BA%AC";
			if(null!=city){
				cacheKeyStr.append("city="+URLEncoder.encode(city, "utf-8"));
				cacheKeyStrNew.append("city="+URLEncoder.encode(city, "utf-8"));
			}
			String from = request.getParameter("from");
			if(null!=from){
				urlStr.append("from="+from);
				cacheKeyStr.append("&from="+from);
			}
			String url = request.getParameter("url");
			String masterId = request.getParameter("masterId");
			if(null!=masterId){
				cacheKeyStr.append("&masterId="+masterId);
				cacheKeyStrNew.append("&masterId="+masterId);
			}
			cacheKeyStr.append("&messagename=luntanMaincontent");
			cacheKeyStrNew.append("&messagename=luntanMaincontent");
			
			String sign = request.getParameter("sign");
			//如果参数中有url则不需要sign和masterId
			if(null != url){
				urlStr.append("&url="+url);
			}else{
				if(null != sign){
					urlStr.append("&sign="+sign);
				}
				if(null != masterId){
					urlStr.append("&masterId="+masterId);
				}
			}
			String order = request.getParameter("order");
			if(null != order){
				urlStr.append("&order="+order);
				cacheKeyStr.append("&order="+order);
				cacheKeyStrNew.append("&order="+order);
			}
			String page = request.getParameter("page");
			if(null != page){
				urlStr.append("&page="+page);
				cacheKeyStr.append("&page="+page);
				cacheKeyStrNew.append("&page="+page);
			}
			String pagesize = request.getParameter("pagesize");
			if(null != pagesize){
				urlStr.append("&pagesize="+pagesize);
				cacheKeyStr.append("&pagesize="+pagesize);
				cacheKeyStrNew.append("&pagesize="+pagesize);
			}
			String sender = request.getParameter("sender");
			if(null != sender){
				urlStr.append("&sender="+sender);
				cacheKeyStr.append("&sender="+sender);
			}
			if(null != sign){
				cacheKeyStr.append("&sign="+sign);
				cacheKeyStrNew.append("&sign="+sign);
			}
			String userId = request.getParameter("userid");
			if(null != userId){
				urlStr.append("&userid="+userId);
				cacheKeyStr.append("&userid="+userId);
				cacheKeyStrNew.append("&userid="+userId);
			}
			String user_id = request.getParameter("user_id");
			if(null != user_id){
				
				urlStr.append("&user_id="+user_id);
			}
			String postId = request.getParameter("postId");
			if(null != postId){
				urlStr.append("&postId="+postId);//从我的论坛回复处跳转时需要用到
			}
			String wirelesscode =   MD5.getMD5( (cacheKeyStr.toString() + "eO=vprX-~(b&PpUUy{)De呼c#峦tMgWQ大-!z9幢pn}V8A中Fncf匡wd2+s~L!9m63+Yq*k<").getBytes("UTF-8"));
			String wirelesscodeNew =   MD5.getMD5( (cacheKeyStrNew.toString() + "eO=vprX-~(b&PpUUy{)De呼c#峦tMgWQ大-!z9幢pn}V8A中Fncf匡wd2+s~L!9m63+Yq*k<").getBytes("UTF-8"));
			
			
			cacheKeyStr.append("&wirelesscode="+wirelesscode.toUpperCase());
			cacheKeyStrNew.append("&wirelesscode="+wirelesscodeNew.toUpperCase());
			//String key = request.getParameter("key");
			PrintWriter out = response.getWriter();
//			// 加MD5验证
//			 String md5Key = new MD5().getMD5(masterId + "luntan");
//			 if (!md5Key.equals(key)) {
//				 out.write(AppHtmlUtil.createXml("failed", "MD5验证失败", null,null).asXML());
//				 out.close();
//				 return;
//			 }

			//获取论坛数据
			String xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			logger.info("lutan url : "+ urlStr.toString());
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("sign", sign);
			parameters.put("city", city);
			parameters.put("page", page);
			parameters.put("pagesize", pagesize);
			parameters.put("postId", postId);
			parameters.put("key", cacheKeyStr.toString());
			parameters.put("keyNew", cacheKeyStrNew.toString());
			parameters.put("urlOfCity", urlOfCity);
			parameters.put("from", from);
			parameters.put("sender", sender);
			
			out.write(publicMethodForlunTan(parameters, xml, "", ""));
			out.close();
			long outTime = System.currentTimeMillis();
			logger.info("luntan  end time use:" + (outTime-inTime) + " ms"+" lutan url : "+ urlStr.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取装修论坛html
	 * @param request
	 * @param response
	 */
	@RequestMapping("/zhuangxiuluntan")
	public void getZhuangxiuLuntanHtml(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("zhuangxiuluntan begin");
			long inTime = System.currentTimeMillis();
			StringBuffer cacheKeyStr = new StringBuffer("");//为拼接wirelesscode
			StringBuffer cacheKeyStrNew = new StringBuffer("");//为拼接wirelesscode
			
			String urlOfCity = request.getParameter("urlOfCity");
			StringBuffer urlStr = new StringBuffer("http://" + urlOfCity + "/api/post/getReplyPostList.php?plnum=2");
			String city = request.getParameter("city");
			if(null!=city){
				cacheKeyStr.append("city="+URLEncoder.encode(city, "utf-8"));
				cacheKeyStrNew.append("city="+URLEncoder.encode(city, "utf-8"));
			}
			String from = request.getParameter("from");
			if(null!=from){
				urlStr.append("&from="+from);
				cacheKeyStr.append("&from="+from);
			}
			
			String masterId = request.getParameter("masterId");
			if(null != masterId){
				urlStr.append("&masterId="+masterId);
				cacheKeyStr.append("&masterId="+masterId);
				cacheKeyStrNew.append("&masterId="+masterId);
			}
			cacheKeyStr.append("&messagename=zhuangxiumaincontent");
			cacheKeyStrNew.append("&messagename=zhuangxiumaincontent");
			
			String order = request.getParameter("order");
			if(null != order){
				urlStr.append("&order="+order);
				cacheKeyStr.append("&order="+order);
				cacheKeyStrNew.append("&order="+order);
			}
			String page = request.getParameter("page");
			if(null != page){
				urlStr.append("&page="+page);
				cacheKeyStr.append("&page="+page);
				cacheKeyStrNew.append("&page="+page);
			}
			String pagesize = request.getParameter("pagesize");
			if(null != pagesize){
				urlStr.append("&pagesize="+pagesize);
				cacheKeyStr.append("&pagesize="+pagesize);
				cacheKeyStrNew.append("&pagesize="+pagesize);
			}
			String sender = request.getParameter("sender");
			if(null != sender){
				urlStr.append("&sender="+sender);
				cacheKeyStr.append("&sender="+sender);
			}
			String sign = request.getParameter("sign");
			if(null != sign){
				urlStr.append("&sign="+sign);
				cacheKeyStr.append("&sign="+sign);
				cacheKeyStrNew.append("&sign="+sign);
			}
			String userId = request.getParameter("userid");
			if(null != userId){
				urlStr.append("&userid="+userId);
				cacheKeyStr.append("&userid="+userId);
				cacheKeyStrNew.append("&userid="+userId);
			}
			String user_id = request.getParameter("user_id");
			if(null != user_id){
				urlStr.append("&user_id="+user_id);
				cacheKeyStr.append("&user_id="+user_id);
				cacheKeyStrNew.append("&user_id="+user_id);
			}
			String postId = request.getParameter("postId");
			if(null != postId){
				urlStr.append("&postId="+postId);//从我的论坛回复处跳转时需要用到
			}
			String wirelesscode =   MD5.getMD5( (cacheKeyStr.toString() + "eO=vprX-~(b&PpUUy{)De呼c#峦tMgWQ大-!z9幢pn}V8A中Fncf匡wd2+s~L!9m63+Yq*k<").getBytes("UTF-8"));
			String wirelesscodeNew =   MD5.getMD5( (cacheKeyStrNew.toString() + "eO=vprX-~(b&PpUUy{)De呼c#峦tMgWQ大-!z9幢pn}V8A中Fncf匡wd2+s~L!9m63+Yq*k<").getBytes("UTF-8"));
			
			cacheKeyStr.append("&wirelesscode="+wirelesscode.toUpperCase());
			cacheKeyStrNew.append("&wirelesscode="+wirelesscodeNew.toUpperCase());
//			String key = request.getParameter("key");
			
			PrintWriter out = response.getWriter();
			logger.info(" zhuangxiuluntan url : "+ urlStr);
			String xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("sign", sign);
			parameters.put("city", city);
			parameters.put("page", page);
			parameters.put("pagesize", pagesize);
			parameters.put("postId", postId);
			parameters.put("key", cacheKeyStr.toString());
			parameters.put("keyNew", cacheKeyStrNew.toString());
			parameters.put("urlOfCity", urlOfCity);
			parameters.put("from", from);
			parameters.put("sender", sender);
			
			out.write(publicMethodForlunTan(parameters, xml, "zhuangxiu", ""));
			out.close();
			long outTime = System.currentTimeMillis();
			logger.info("zhuangxiuluntan  end time use:" + (outTime-inTime) + " ms"+" zhuangxiuluntan url : "+ urlStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取业主圈主贴内容
	 * @param request
	 * @param response
	 */
	@RequestMapping("/luntanmaincontent")
	public void getLuntanMainHtml(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("luntanmaincontent begin");
			long inTime = System.currentTimeMillis();

			String urlOfCity = request.getParameter("urlOfCity");
			
			StringBuffer urlStr = new StringBuffer(urlOfCity + "api/post/getMasterInfoByMasterId.php?");
			String url = request.getParameter("url");
			String sign = request.getParameter("sign");
			String masterId = request.getParameter("masterId");
			String from = request.getParameter("from");
			if(null!=from){
				urlStr.append("from="+from);
			}
			String sender = request.getParameter("sender");
			if(null != sender){
				urlStr.append("&sender="+sender);
			}
			//如果参数中有url则不需要sign和masterId
			if(null != url){
				urlStr.append("&url="+url);
			}else{
				if(null != sign){
					urlStr.append("&sign="+sign);
				}
				if(null != masterId){
					urlStr.append("&masterId="+masterId);
				}
			}
			String order = request.getParameter("order");
			if(null != order){
				urlStr.append("&order="+order);
			}
			String page = request.getParameter("page");
			if(null != page){
				urlStr.append("&page="+page);
			}
			String pagesize = request.getParameter("pagesize");
			if(null != pagesize){
				urlStr.append("&pagesize="+pagesize);
			}
			String userId = request.getParameter("userid");
			if(null != userId){
				urlStr.append("&userid="+userId);
			}
			String user_id = request.getParameter("user_id");
			if(null != user_id){
				urlStr.append("&user_id="+user_id);
			}
			urlStr.append("&isMaster=1");
			String city = request.getParameter("city");
			PrintWriter out = response.getWriter();

			//获取论坛数据
			String xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			long outTime = System.currentTimeMillis();
			logger.info("luntanmaincontent  getUrlTxt time use:" + (outTime-inTime) + " ms" + "  luntanmaincontent url :  "+ urlStr);
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("sign", sign);
			parameters.put("city", city);
			parameters.put("page", page);
			parameters.put("pagesize", pagesize);
			parameters.put("urlOfCity", urlOfCity);
			parameters.put("from", from);
			parameters.put("sender", sender);
			out.write(publicMethodForlunTan(parameters, xml, "", "main"));
			out.close();
			long outTime2 = System.currentTimeMillis();
			logger.info("luntanmaincontent  end time use:" + (outTime2-inTime) + " ms ,getUrlTxt time use:"+ (outTime-inTime) + " ms "  + "  luntanmaincontent url :  "+ urlStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取装修论坛主贴html
	 * @param request
	 * @param response
	 */
	@RequestMapping("/zhuangxiumaincontent")
	public void getZhuangxiuluntanMainHtml(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("zhuangxiumaincontent begin");
			long inTime = System.currentTimeMillis();

			String urlOfCity = request.getParameter("urlOfCity");
			StringBuffer urlStr = new StringBuffer("http://" + urlOfCity + "/api/post/getReplyPostList.php?plnum=2");
			String from = request.getParameter("from");
			if(null!=from){
				urlStr.append("&from="+from);
			}
			String sign = request.getParameter("sign");
			if(null != sign){
				urlStr.append("&sign="+sign);
			}
			String masterId = request.getParameter("masterId");
			if(null != masterId){
				urlStr.append("&masterId="+masterId);
			}
			String order = request.getParameter("order");
			if(null != order){
				urlStr.append("&order="+order);
			}
			String page = request.getParameter("page");
			if(null != page){
				urlStr.append("&page="+page);
			}
			String pagesize = request.getParameter("pagesize");
			if(null != pagesize){
				urlStr.append("&pagesize="+pagesize);
			}
			String userId = request.getParameter("userid");
			if(null != userId){
				urlStr.append("&userid="+userId);
			}
			String user_id = request.getParameter("user_id");
			if(null != user_id){
				urlStr.append("&user_id="+user_id);
			}
			String sender = request.getParameter("sender");
			if(null != sender){
				urlStr.append("&sender="+sender);
			}
			String city = request.getParameter("city");
			PrintWriter out = response.getWriter();
			String xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			Map<String,String> parameters = new HashMap<String, String>();
			parameters.put("sign", sign);
			parameters.put("city", city);
			parameters.put("page", page);
			parameters.put("pagesize", pagesize);
			parameters.put("urlOfCity", urlOfCity);
			parameters.put("from", from);
			parameters.put("sender", sender);
			out.write(publicMethodForlunTan(parameters, xml, "zhuangxiu", "main"));
			out.close();
			long outTime = System.currentTimeMillis();
			logger.info("zhuangxiumaincontent  end time use:" + (outTime-inTime) + " ms"+" zhuangxiumaincontent url : "+ urlStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取业主圈论坛回复详情html
	 * @param request
	 * @param response
	 */
	@RequestMapping("/yezhuquanreply")
	public void getYezhuquanReplyHtml(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("yezhuquanreply begin");
			long inTime = System.currentTimeMillis();

			String urlOfCity = request.getParameter("urlOfCity");
			StringBuffer urlStr = new StringBuffer(urlOfCity + "api/post/getReplyPostInfo.php?");
			String sign = request.getParameter("sign");
			if(null != sign){
				urlStr.append("&sign="+sign);
			}
			String masterId = request.getParameter("masterId");
			if(null != masterId){
				urlStr.append("&masterId="+masterId);
			}
			String postId = request.getParameter("postId");
			if(null != postId){
				urlStr.append("&postId="+postId);
			}
			String page = request.getParameter("page");
			if(null != page){
				urlStr.append("&page="+page);
			}
			/*String pagesize = request.getParameter("pagesize");
			if(null != pagesize){
				urlStr.append("&pagesize="+pagesize);
			}*/
			urlStr.append("&pagesize=100");
			PrintWriter out = response.getWriter();
			logger.info(" yezhuquanreply url : "+ urlStr);
			String xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			org.dom4j.Document doc = DocumentHelper.parseText(xml);  
			org.dom4j.Element root = doc.getRootElement();
			if(null != root){
				org.dom4j.Element erorElement = root.element("errornum");
				org.dom4j.Element errorMsgElement = root.element("error");
				String errorMsg = null;
				if(null != erorElement){
					String num = erorElement.getText();
					if(null!=errorMsgElement){
						errorMsg=errorMsgElement.getText();
					}else if("1".equals(num)){
						errorMsg = "网络开小差，请稍后重试";
					}else if("2".equals(num) || "3".equals(num)){
						errorMsg = "论坛不存在或已关闭";
					}else if("4".equals(num) || "5".equals(num)){
						errorMsg = "帖子不存在或已删除";
					}else if("6".equals(num) || "7".equals(num)){
						errorMsg = "私密论坛只有通讯录用户才能访问";
					}
					out.write(AppHtmlUtil.createXml("fail", errorMsg, null, null).asXML());
				}else{
					//引入模板
					File file = new File(Constant.HTML_TEMPLATE + "luntanreplytemplate.html");
					String postHtml = AppHtmlUtil.readFileToString(file);
					
					if(!StringUtil.isEmpty(postId)){
						postHtml = postHtml.replace("reply_postid",postId);
					}
					
					org.dom4j.Element postElement = (org.dom4j.Element) root.element("post_list");
					if(null != postElement){
						//头像
						org.dom4j.Element postHeadImgElement = postElement.element("userImgsrc");
						if(null != postHeadImgElement){
							String postHeadImg = postHeadImgElement.getText().trim();
							postHtml = postHtml.replace("post_head_img",postHeadImg);
						}else{
							postHtml = postHtml.replace("post_head_img","");
						}
						
						//回复者名字
						org.dom4j.Element nickNameElement = postElement.element("nickName");
						String nickName_post="";
						if(null != nickNameElement){
							nickName_post = nickNameElement.getText();
							if(!"".equals(nickName_post)){
								postHtml = postHtml.replace("post_nickname",nickName_post);//显示昵称
							}
						}
						org.dom4j.Element postNameElement = postElement.element("userName");
						if(null != postNameElement){
							String postName = postNameElement.getText();
							postHtml = postHtml.replace("post_name",postName);
							if("".equals(nickName_post)){
								postHtml = postHtml.replace("post_nickname",postName);
							}
						}
						
						//回复者id
						org.dom4j.Element replyPostIdElement = postElement.element("userId");
						if(null != replyPostIdElement){
							String replyPostId = replyPostIdElement.getText();
							postHtml = postHtml.replace("post_userid",replyPostId);
						}
						
						//回复者content
						org.dom4j.Element postContentElement = postElement.element("content");
						if(null != postContentElement){
							postHtml = postHtml.replace("post_content",postContentElement.getText());
						}
						//回复者come_from
						org.dom4j.Element postComefromElement = postElement.element("come_from");
						if(null != postComefromElement){
							String comefrom = postComefromElement.getText();
							if("iosapp".equals(comefrom) || "AndroidApp".equals(comefrom)){
								postHtml = postHtml.replace("post_comefrom","<span>来自客户端 <img src='khd-icon.png' width='8' type='head'></span>");
							}else{
								postHtml = postHtml.replace("post_comefrom","");
							}
						}else{
							postHtml = postHtml.replace("post_comefrom","");
						}
						//创建时间
						org.dom4j.Element postCreateTimeElement = postElement.element("createTime");
						if(null != postCreateTimeElement){
							String postCreateTime = postCreateTimeElement.getText() + "000";
							String creTim=AppHtmlUtil.getNeedTimeString(postCreateTime);
							if("0分钟前".equals(creTim.trim())){
								creTim="刚刚";
							}
							postHtml = postHtml.replace("create_time",creTim);
						}
						
						//跟帖者
						org.dom4j.Element postReplyListElement = postElement.element("replyList");
						if(null != postReplyListElement){
							StringBuffer replyHtml = new StringBuffer();
							//获取节点个数，判断回复者个数
							int replyNum = postReplyListElement.nodeCount() / 9;
							for(int j=0;j<replyNum;j++){
								org.dom4j.Element  postReplyUserId = postReplyListElement.element("userId"+j);
								org.dom4j.Element  postReplyUserName = postReplyListElement.element("username"+j);
								org.dom4j.Element  postReplyContent = postReplyListElement.element("content"+j);
								org.dom4j.Element  postReplyNickName = postReplyListElement.element("nickname"+j);
								org.dom4j.Element  postReplyToUserId = postReplyListElement.element("toUserId"+j);
								org.dom4j.Element  postReplyToUserName = postReplyListElement.element("toUserName"+j);
								org.dom4j.Element  postReplyToNickName = postReplyListElement.element("toNickName"+j);
								if(null != postReplyUserName && null != postReplyContent){
									String userName = postReplyUserName.getText();
									String nickName = "";
									String userId = postReplyUserId.getText();
									String touserName = "";
									String tonickName = "";
									String touserId = "";
									String content = postReplyContent.getText();
									if(null!=postReplyNickName){
										nickName = postReplyNickName.getText();
									}
									if("".equals(nickName)){
										nickName = userName;
									}
									if(null!=postReplyToUserName && null!=postReplyToUserId){
										touserName = postReplyToUserName.getText();
										tonickName = postReplyToNickName.getText();
										if("".equals(tonickName)){
											tonickName = touserName;
										}
										touserId = postReplyToUserId.getText();
									}
									
									String divhref = "soufun://waptoapp/{&quot;destination&quot;:&quot;comment&quot;,&quot;postId&quot;:&quot;"+postId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;,&quot;nickname&quot;:&quot;"+nickName+"&quot;,&quot;userId&quot;:&quot;"+userId+"&quot;,&quot;iscenzhu&quot;:&quot;"+0+"&quot;}";
									/*if(null != content && content.contains("回复")){
										//userFrom回复userTo
										String userTo = null;
										//回复内容
										String realContent = null;
										if(content.contains("：")){
											userTo = content.substring(content.indexOf("回复")+2,content.indexOf("："));
											realContent = content.substring(content.indexOf("："),content.length());
										}else if(content.contains(":")){
											userTo = content.substring(content.indexOf("回复")+2,content.indexOf(":"));
											realContent = content.substring(content.indexOf(":"),content.length());
										}else{
											userTo = "";
											realContent = content.substring(content.indexOf("回复")+2,content.length());
										}
										replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
																"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+userName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;0&quot;,&quot;username&quot;:&quot;"+userTo+"&quot;}' class='fblue'>" +userTo+"</a>" + realContent+"</div>");
									}else{
										replyHtml.append("<div onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
																"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+userName + "</a>：" + content + "</div>");
									}*/
									if(null != touserId && !"".equals(touserId) && null != touserName && !"".equals(touserName)){
											replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
													"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+touserId+"&quot;,&quot;username&quot;:&quot;"+touserName+"&quot;}' class='fblue'>" +tonickName+"</a>" + ":"+content+"</div>");
									}else{
											replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
													"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>:"+content+"</div>");
									}
								}
								
							}
							if(replyNum>0){//如果没有跟贴，则会只显示一条空的灰色
								postHtml = postHtml.replace("reply_div", "<div class='hf-box'>" + replyHtml+"</div>");
							}else{
								postHtml = postHtml.replace("reply_div", "");
							}
						}else{
							postHtml = postHtml.replace("reply_div", "");
						}
					}
					
					//格式化论坛html模板
					Document postDoc = Jsoup.parse(postHtml);
					
					Elements ps = postDoc.select("p");
					if(null != ps && ps.size() >0){
						for(int i=0;i<ps.size();i++){
							if(ps.get(i).hasText()){
								if(ps.get(i).text().contains("===================")){
									ps.get(i).remove();
									continue;
								}
								if(null != ps.get(i).attr("style") && ps.get(i).attr("style").contains("text-indent")){
									ps.get(i).removeAttr("style");
								}
								if(ps.get(i).text().contains("http://")){
									if(ps.get(i).text().contains(".htm") || ps.get(i).text().contains(".aspx") ){
										String style = ps.get(i).attr("style");
										style = "word-break: break-all;" + style ;
										ps.get(i).attr("style",style);
									}
								}
								
							}
						}
					}
					//处理a链接
					AppHtmlUtil.romveAtagForLuntan(postDoc,"","");
					
					//处理图片
					List<String> imgUrlList = AppHtmlUtil.dealImgForLuntan(postDoc);
					
					out.write(AppHtmlUtil.createXml("success",postDoc.html(),imgUrlList,null).asXML());
				}
			}else{
				out.write(AppHtmlUtil.createXml("failed", "无此条记录", null, null).asXML());
			}
			out.close();
			long outTime = System.currentTimeMillis();
			logger.info("yezhuquanreply  end time use:" + (outTime-inTime) + " ms" +" yezhuquanreply url : "+ urlStr);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 论坛接口公用方法：生成论坛信息xml文件
	 * @param parameters：所需参数
	 * @param xml：接口返回的xml
	 * @param user:调用者
	 * @param useFor ：用于 主贴或者帖子所有内容
	 * @return
	 */
	private String publicMethodForlunTan(Map<String,String> parameters ,String xml,String user,String useFor){
		
		//返回的xmlStr
		String xmlStr = null;
		try{
			//判断原始接口是否有返回数据
			if(StringUtil.isEmpty(xml)){
				return AppHtmlUtil.createXmlForLuntan("failed", "网络超时,请重试").asXML();
			}
			long endTime1 = System.currentTimeMillis();
			String sign = parameters.get("sign");
			String masterId = parameters.get("masterId");
			String pagesize = parameters.get("pagesize");
			String page = parameters.get("page");
			String topostId = parameters.get("postId");
			String city = parameters.get("city");
			String key = parameters.get("key");
			String keyNew = parameters.get("keyNew");
			String urlOfCity = parameters.get("urlOfCity");
			String from = parameters.get("from");
			String sender = parameters.get("sender");
			//解析xml：使用dom4j
			org.dom4j.Document doc = DocumentHelper.parseText(xml);  
			org.dom4j.Element root = doc.getRootElement();
			if(null != root){
				org.dom4j.Element data = root.element("data");
				if(null != data){
					org.dom4j.Element erorElement = data.element("errornum");
					org.dom4j.Element errorMsgElement = data.element("error");
					String errorMsg = null;
					if(null != erorElement){
						String num = erorElement.getText();
						if(null!=errorMsgElement){
							errorMsg=errorMsgElement.getText();
						}else if("1".equals(num)){
							errorMsg = "网络开小差，请稍后重试";
						}else if("2".equals(num) || "3".equals(num)){
							errorMsg = "论坛不存在或已关闭";
						}else if("4".equals(num) || "5".equals(num)){
							errorMsg = "帖子不存在或已删除";
						}else if("6".equals(num) || "7".equals(num)){
							errorMsg = "私密论坛只有通讯录用户才能访问";
						}
					}else if(null!=errorMsgElement){
						errorMsg=errorMsgElement.getText();
					}
					return AppHtmlUtil.createXmlForLuntan("fail", errorMsg).asXML();
				}
			}
			
			//引入论坛模板
			String luntanTemplate = null;
			if("zhuangxiu".equals(user)){
				luntanTemplate = "zhuangxiuluntan.html";
			}else{
				luntanTemplate = "luntantemplate.html";
			}
			File file = new File(Constant.HTML_TEMPLATE + luntanTemplate);
			String luntanHtml = AppHtmlUtil.readFileToString(file);
			
			//论坛参数Map
			Map<String,String> luntanParam = new HashMap<String, String>();
			
			//论坛title
			org.dom4j.Element titleElement = root.element("title");
			//web地址
			String shareUrl = "";
			if(null != titleElement){
				org.dom4j.Element masterTitleElement = titleElement.element("masterTitle");
				if(null != masterTitleElement){
					luntanHtml = luntanHtml.replace("luntan_title", masterTitleElement.getText());
					luntanParam.put("title", masterTitleElement.getText());
				}
				//帖子城市city
				org.dom4j.Element cityElement = titleElement.element("cityName");
				if(null != cityElement){
					luntanParam.put("city", cityElement.getText());
				}
				//web地址
				org.dom4j.Element urlElement = titleElement.element("postUrl");
				if(null != urlElement){
					shareUrl = urlElement.getText();
					luntanParam.put("shareurl", urlElement.getText());
				}
			}
			
			//city
			if(!StringUtil.isEmpty(city) && !"zhuangxiu".equals(user)){
				luntanHtml = luntanHtml.replace("luntan_city", city);
			}
			//返回boardurl节点，取forumUrl节点的值20150925
			org.dom4j.Element foruminfo = root.element("forumInfo");
			if(null!=foruminfo){
				org.dom4j.Element boardUrlEle = foruminfo.element("forumUrl");
				if(null != boardUrlEle){
					luntanParam.put("boardUrl", boardUrlEle.getText());
				}
			}
			//计算pageNo
			String pageNo = "1";
			org.dom4j.Element totalCount = root.element("totalcount");
			if(null != totalCount){
				String num = "0";
				String commentCount = "0";
				org.dom4j.Element count = totalCount.element("total_count");
				if(null != count){
					num = count.getText();
				}
				org.dom4j.Element commentCountElement = totalCount.element("Comment_count");
				if(null != commentCountElement){
					commentCount = commentCountElement.getText();
				}
				//楼主占1楼
				Integer replyCount = Integer.valueOf(num) - Integer.valueOf(commentCount) + 1 ;
				if(0 != replyCount && replyCount % Integer.valueOf(pagesize) == 0){
					pageNo = (replyCount/Integer.valueOf(pagesize)) + "";
				}else{
					pageNo = (replyCount/Integer.valueOf(pagesize) + 1) + "";
				}
				luntanParam.put("pageNo", pageNo);
				
				//论坛回复数
				luntanHtml = luntanHtml.replace("poster_num",(Integer.valueOf(num) - Integer.valueOf(commentCount))+"");
			}
			
			//浏览次数
			org.dom4j.Element masterHitElement = root.element("masterHits");
			if(null != masterHitElement){
				org.dom4j.Element hitNumElement = masterHitElement.element("hits");
				if(null != hitNumElement){
					luntanHtml = luntanHtml.replace("hits_num", hitNumElement.getText());
				}
			}
			
			//论坛sign
			if(!StringUtil.isEmpty(sign)){
				if(!"zhuangxiu".equals(user)){
					luntanHtml = luntanHtml.replace("luntan_sign",sign);
				}
				luntanParam.put("sign", sign);
			}
			
			//论坛masterId
			if(!StringUtil.isEmpty(masterId)){
				luntanParam.put("masterId", masterId);
			}
			
			//当跳转指定楼层时，需要返回一些节点
			if(!"".equals(topostId)){
				org.dom4j.Element currentInfoElement = root.element("currentInfo");
				if(null!=currentInfoElement){
					org.dom4j.Element pageElement = currentInfoElement.element("page");
					if(null!=pageElement){
						luntanParam.put("page", pageElement.getText());
						page=pageElement.getText();
					}
					org.dom4j.Element postIdElement = currentInfoElement.element("postId");
					if(null!=postIdElement){
						luntanParam.put("postId", postIdElement.getText());
					}
					org.dom4j.Element isAuthElement = currentInfoElement.element("isAuth");
					if(null!=isAuthElement){
						luntanParam.put("isAuth", isAuthElement.getText());
					}
				}
				org.dom4j.Element isGoodElement = root.element("isGood");
				if(null!=isGoodElement){
					luntanParam.put("isGood", isGoodElement.getText());
				}
				org.dom4j.Element isTopElement = root.element("isTop");
				if(null!=isTopElement){
					luntanParam.put("isTop", isTopElement.getText());
				}
				org.dom4j.Element bidElement = root.element("bid");
				if(null!=bidElement){
					luntanParam.put("bid", bidElement.getText());
				}
				org.dom4j.Element headImgElement = root.element("headImg");
				if(null!=headImgElement){
					luntanParam.put("shareImgUrl", headImgElement.getText());
				}
				org.dom4j.Element louzhuNickNameElement = root.element("louzhuNickName");
				if(null!=louzhuNickNameElement){
					luntanParam.put("nickname", louzhuNickNameElement.getText());
				}
			}
			
			//forumName论坛名称
			String forumName = null;
			String cacheStr=null;
			if("1".equals(page)){
				//第一个post_list为楼主
				org.dom4j.Element louzhuElement = root.element("post_list");
				//论坛活动
				org.dom4j.Element activityElement = louzhuElement.element("activeList");
				//大转盘活动
				org.dom4j.Element luckInfoElement = louzhuElement.element("luckInfo");
				///
				org.dom4j.Element voteElement = root.element("vote");//投票贴
				String isvote="";
				if(null != voteElement){
					org.dom4j.Element isvoteElement = voteElement.element("isVote");
					if(null!=isvoteElement){
						isvote=isvoteElement.getText();
					}
				}
				//秒杀活动
				org.dom4j.Element spikeActivityElement = louzhuElement.element("spikeActivity");
				///
				
				org.dom4j.Element cacheContentElement=null;
				// 不包含活动，不包含投票信息，才会取缓存
				if(null == activityElement && null ==luckInfoElement && null ==spikeActivityElement && !"1".equals(isvote)&& null != key){
					logger.info("memCacheKey:"+key+" memCacheKeyNew:"+keyNew);
					try{
						cacheStr = (String) LuntanCache.getCache(key);
						if(null==cacheStr || "".equals(cacheStr)){
							cacheStr = (String) LuntanCache.getCache(keyNew);
						}
	//					cacheStr=AppHtmlUtil.readFileToString(new File("D:\\eclipse2\\messageCenter\\WebRoot\\template\\ceshimemcache.xml"));
						if(null !=cacheStr && !"".equals(cacheStr)){
							logger.info("cacheStr  begin:");
							org.dom4j.Document cacheDoc = DocumentHelper.parseText(cacheStr);  
							org.dom4j.Element cacheroot = cacheDoc.getRootElement();
							cacheContentElement = cacheroot.element("content");
						}
					}catch(Exception e){
						logger.info("getCache failed,key is "+key);
						e.printStackTrace();
					}
					long endTime2 = System.currentTimeMillis();
					logger.info("getCache use time is "+(endTime2-endTime1)+"ms ,key is "+key);
				}
//				
				if( null != cacheStr && null != cacheContentElement){//若主贴已有缓存,则从缓存中取出主贴部分20151019
					luntanHtml = luntanHtml.replace(luntanHtml.substring(luntanHtml.indexOf("<section"), luntanHtml.indexOf("</section>")+10),"<section></section>");
				}else{
					logger.info("getnomemCache,key is "+key+" keyNew is "+keyNew);
					//第一个post_list为楼主
//					org.dom4j.Element louzhuElement = root.element("post_list");
					if(null != louzhuElement){
						//是否加精isgood  是否固定istop,isAuth权限,boardUrl 论坛链接
						org.dom4j.Element isGoodEle = louzhuElement.element("isGood");
						if(null != isGoodEle){
							//如果是精华帖，加上精华帖的样式
							if("1".equals(isGoodEle.getText())){
								luntanHtml = luntanHtml.replace("luntan_jihua","<span class='tap-jh'></span>");
							}else{
								luntanHtml = luntanHtml.replace("luntan_jihua","");
							}
							luntanParam.put("isGood", isGoodEle.getText());
						}else{
							luntanHtml = luntanHtml.replace("luntan_jihua","");
						}
						org.dom4j.Element isTopEle = louzhuElement.element("isTop");
						if(null != isTopEle){
							luntanParam.put("isTop", isTopEle.getText());
						}
						org.dom4j.Element isAuthEle = louzhuElement.element("isAuth");
						if(null != isAuthEle){
							luntanParam.put("isAuth", isAuthEle.getText());
						}
						
						//forumName
						org.dom4j.Element forumNameElement = louzhuElement.element("forumName");
						if(null != forumNameElement){
							forumName = forumNameElement.getText();
							luntanHtml = luntanHtml.replace("forum_name", forumName);
							luntanParam.put("signName", forumName);
						}
						//论坛sign
						if(StringUtil.isEmpty(sign)){
							org.dom4j.Element signElement = louzhuElement.element("sign");
							if(null != signElement){
								sign = signElement.getText();
								luntanHtml = luntanHtml.replace("luntan_sign", sign);
								luntanParam.put("sign", sign);
							}
						}
						//论坛masterId
						if(StringUtil.isEmpty(masterId)){
							org.dom4j.Element masterIdElement = louzhuElement.element("masterId");
							if(null != masterIdElement){
								masterId = masterIdElement.getText();
								luntanParam.put("masterId", masterId);
							}
						}
						//论坛头图
						org.dom4j.Element imgsrcElement = louzhuElement.element("imgsrc");
						if(null != imgsrcElement){
							luntanParam.put("shareImgUrl", imgsrcElement.getText());
						}
						//论坛bid
						org.dom4j.Element bidElement = louzhuElement.element("bid");
						if(null != bidElement){
							luntanParam.put("bid", bidElement.getText());
						}
						
						StringBuffer luntanContentBuffer = new StringBuffer();
						//论坛活动
//						org.dom4j.Element activityElement = louzhuElement.element("activeList");
						String activeId="";//活动id,大转盘传参用
						if(null != activityElement){
							//引入活动模板
							File activityFile = new File(Constant.HTML_TEMPLATE + "luntanactivity.html");
							String activityHtml = AppHtmlUtil.readFileToString(activityFile);
							//活动名称
							org.dom4j.Element nameElement = activityElement.element("name");
							if(null != nameElement){
								String activityName = nameElement.getText();
								activityHtml = activityHtml.replace("activity_name", activityName);
							}
							//活动封面
							org.dom4j.Element faceElement = activityElement.element("facebook");
							if(null != faceElement){
								String face = faceElement.getText();
								if(!StringUtil.isEmpty(face)){
									activityHtml = activityHtml.replace("activity_face", face);
								}else{
									activityHtml = activityHtml.replace("activity_face", "http://img2.soufun.com/bbsv2/images/activeDefault.jpg");
								}
								
							}
							//开始时间
							org.dom4j.Element starttimeElement = activityElement.element("starttime");
							String starttime = "";
							if(null != starttimeElement){
								starttime = starttimeElement.getText()+"000";
								activityHtml = activityHtml.replace("activity_starttime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(starttime))));
							}
							//结束时间
							org.dom4j.Element endtimeElement = activityElement.element("endtime");
							String endtime = "";
							if(null != endtimeElement){
								endtime = endtimeElement.getText()+"000";
								activityHtml = activityHtml.replace("activity_endtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(endtime))));
							}
							//活动地点
							org.dom4j.Element zoneElement = activityElement.element("zone");
							if(null != zoneElement){
								String zone = zoneElement.getText();
								activityHtml = activityHtml.replace("activity_zone",zone);
							}
							//报名截止时间
							org.dom4j.Element signtimeElement = activityElement.element("signtime");
							if(null != signtimeElement){
								String signtime = signtimeElement.getText();
								if("0".equals(signtime) || "1".equals(signtime)){
									activityHtml = activityHtml.replace("activity_signtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(endtime))));
								}else if("2".equals(signtime)){
									activityHtml = activityHtml.replace("activity_signtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(starttime))));
								}else{
									activityHtml = activityHtml.replace("activity_signtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(signtime+"000"))));
								}
								
							}
							//报名人数上限
							org.dom4j.Element signlimitElement = activityElement.element("signlimit");
							String signlimit = "";
							if(null != signlimitElement){
								signlimit = signlimitElement.getText();
								if("0".equals(signlimit)){
									signlimit = "不限";
								}
								activityHtml = activityHtml.replace("activity_signlimit",signlimit);
							}
							//活动资格
							org.dom4j.Element signlevelElement = activityElement.element("signlevelText");
							if(null != signlevelElement){
								String signlevel = signlevelElement.getText();
								activityHtml = activityHtml.replace("activity_signlevel",signlevel);
							}else{
								activityHtml = activityHtml.replace("activity_signlevel","");
							}
							//报名填写
							org.dom4j.Element signinfoElement = activityElement.element("signinfo");
							String signinfo = null;
							if(null != signinfoElement){
								signinfo = signinfoElement.getText();
								StringBuffer info = new StringBuffer();
								if(null != signinfo){
									if(signinfo.contains("1")){
										info.append("真实姓名");
									}
									if(signinfo.contains("2")){
										info.append("&nbsp;手机");
									}
									if(signinfo.contains("3")){
										info.append("&nbsp;人数");
									}
								}
								activityHtml = activityHtml.replace("activity_signinfo",info);
							}
							//需要支付的银币
							org.dom4j.Element coinElement = activityElement.element("coin");
							if(null != coinElement){
								String coin = coinElement.getText();
								activityHtml = activityHtml.replace("activity_coin",coin);
							}
							//1时，只能报名一次，有验证码。0时可以报名多次
							org.dom4j.Element onemobileElement = activityElement.element("onemobile");
							String onemobile=null;
							if(null != onemobileElement){
								onemobile = onemobileElement.getText();
							}
							
							//论坛id
							org.dom4j.Element forumIdElement = activityElement.element("forumId");
							String forumId = "";
							if(null != forumIdElement){
								forumId = forumIdElement.getText();
							}
							//postId
							org.dom4j.Element postIdElement = activityElement.element("postId");
							String postId = "";
							if(null != postIdElement){
								postId = postIdElement.getText();
							}
							//activityNum
							org.dom4j.Element activeUserCountElement = activityElement.element("activeUserCount");
							String activeUserCount = "";
							if(null != activeUserCountElement){
								activeUserCount = activeUserCountElement.getText();
							}
							//活动button
							org.dom4j.Element needsignElement = activityElement.element("needsign");
							String needsign = null;
							if(null != needsignElement){
								needsign = needsignElement.getText();
							}
							org.dom4j.Element canjoinElement = activityElement.element("CanJoin");
							String canjoin = null;
							if(null != needsignElement){
								canjoin = canjoinElement.getText();
							}
							String button = "<a class='hd-btn'  " +
									"href='soufun://waptoapp/{&quot;destination&quot;:&quot;activity&quot;,&quot;forumId&quot;:&quot;"+forumId+"&quot;,&quot;postId&quot;:&quot;"+postId+"&quot;,&quot;signinfo&quot;:&quot;"+signinfo+"&quot;,&quot;canjoin&quot;:&quot;"+canjoin+"&quot;,&quot;onemobile&quot;:&quot;"+onemobile+"&quot;}'>我要参加 <span><i>"+
									activeUserCount+"</i>/"+signlimit+"人</span></a>";
							if(!"1".equals(canjoin) && !"0".equals(canjoin)){
								button = "<a href='javascript:;' class='hd-btn disable'>我要参加 <span><i>"+ activeUserCount+"</i>/"+signlimit+"人</span></a>";
							}
							if(null != needsign && "1".equals(needsign)){
								activityHtml = activityHtml.replace("activity_button",button);
							}else{
								activityHtml = activityHtml.replace("activity_button","");
							}
							org.dom4j.Element idElement = activityElement.element("id");
							
							if(null!=idElement){
								activeId=idElement.getText();
							}
							//添加报名活动页面
							luntanContentBuffer.append(activityHtml);
						}
						//开始大转盘活动页面20151202
						
						//关于大转盘信息
//						org.dom4j.Element luckInfoElement = louzhuElement.element("luckInfo");
						if(null != luckInfoElement){
							//引入活动模板
							File dzpactivityFile = new File(Constant.HTML_TEMPLATE + "luntanactivitydzp.html");
							String dzpactivityHtml = AppHtmlUtil.readFileToString(dzpactivityFile);
							
							org.dom4j.Element prizeListElement = luckInfoElement.element("prizeList");
							if(null !=prizeListElement){
								StringBuffer sb=new StringBuffer();
								org.dom4j.Element totalPrizeNumElement = prizeListElement.element("totalPrizeNum");
								String totalPrizeNum="0";
								if(null!=totalPrizeNumElement){
									totalPrizeNum=totalPrizeNumElement.getText();
								}
								if(null!=prizeListElement.elements("prizeInfo") && prizeListElement.elements("prizeInfo").size()>0){
									for(int i=0;i<Integer.parseInt(totalPrizeNum);i++){
										org.dom4j.Element prizeInfoElement = (org.dom4j.Element) prizeListElement.elements("prizeInfo").get(i);
										if(null!=prizeInfoElement){
											org.dom4j.Element prizeNameElement=prizeInfoElement.element("prizeName");
											String prizeName="";
											if(null!=prizeNameElement){
												prizeName=prizeNameElement.getText();
											}
											org.dom4j.Element prizePicElement=prizeInfoElement.element("prizePic");
											String prizePic="";
											if(null!=prizePicElement){
												prizePic=prizePicElement.getText();
											}
											if("".equals(prizePic)){
												prizePic="icon01.png";//设一个本地的图片
											}
											if(i<2){
												String divname="";
												 if(i==0){
													divname= "<div class='ntegral1 d20'>";
												}else{
													divname= "<div class='ntegral2 d70'>";
												}
												sb.append(divname+"<p><em>"+prizeName+"</em></p><img class='pic' src='"+prizePic+"' width='100%' type='dzp'></div>");
											        
											}else if(i<6){
												String divname="";
												if(i==2){
													divname= "<div class='ntegral3 d70f'>";
												}else if(i==3){
													divname= "<div class='ntegral4 d20f'>";
												}else if(i==4){
													divname= "<div class='ntegral5 d20'>";
												}else{
													divname= "<div class='ntegral6 d70'>";
												}
												sb.append(divname+"<img class='pic d180' src='"+prizePic+"' width='100%' type='dzp'><p class='line'><em>"+prizeName+"</em></p></div>");
												
											}else if(i==6){
												sb.append(" <div class='ntegral7 d70f'><p ><em>"+prizeName+"</em></p><img class='pic' src='"+prizePic+"' width='100%' type='dzp'></div>");
											}else{
												sb.append("<div class='ntegral d20f'><p><em>"+prizeName+"</em></p><img class='pic' src='"+prizePic+"' width='100%' type='dzp'></div>");
											     
											}
//												
											 
										}
									}
								}
								
								dzpactivityHtml=dzpactivityHtml.replace("prizeinfoList", sb.toString());
							}
							
							// 获奖名单
							org.dom4j.Element winListElement = luckInfoElement.element("winList");
							if(null !=winListElement){
								StringBuffer sbWinList=new StringBuffer();
								if(null!=winListElement.elements("winInfo") && winListElement.elements("winInfo").size()>0){
									for(int i=0;i<winListElement.elements("winInfo").size();i++){
										org.dom4j.Element winInfoElement =(org.dom4j.Element) winListElement.elements("winInfo").get(i);
										if(null!=winInfoElement){
											org.dom4j.Element nickNameElement = winInfoElement.element("nickName");
											String nickName="";
											if(null!=nickNameElement){
												nickName=nickNameElement.getText();
												if(!"".equals(nickName)){
													String nickNametemp=StringUtil.getSubString(nickName,10);
													if(!nickName.equals(nickNametemp)){
														nickName=nickNametemp+"..";
													}
												}
											}
											org.dom4j.Element winTimeElement = winInfoElement.element("winTime");
											String winTime="";
											if(null!=winTimeElement){
												winTime=winTimeElement.getText();
												if(!"".equals(winTime)){
													String wintimes=StringUtil.timeToStr(winTime, "yyyy-MM-dd HH:mm:ss");
													winTime=AppHtmlUtil.getNeedTimeString(wintimes);
													if("0分钟前".equals(winTime.trim())){
														winTime="刚刚";
													}
												}
											}
											org.dom4j.Element prizeNameElement = winInfoElement.element("prizeName");
											String prizeName="";
											if(null!=prizeNameElement){
												prizeName=prizeNameElement.getText();
											}
											sbWinList.append("<li><span class='w25'>"+nickName+"</span><span class='w30'>"+winTime+" </span><span class='w15'>抽中</span><span class='w30'>"+prizeName+"</span></li>");
										}
									}
								}
								if(winListElement.elements("winInfo").size()>3){
									dzpactivityHtml=dzpactivityHtml.replace("prizeuser_list", "<ul id='xst'>"+sbWinList.toString()+"</ul>");
								}else{
									dzpactivityHtml=dzpactivityHtml.replace("prizeuser_list", "<ul >"+sbWinList.toString()+"</ul>");
								}
								
							}
							
							//  活动规则
							org.dom4j.Element startTimeElement = luckInfoElement.element("startTime");
							String startTime="";
							if(null!=startTimeElement){
								startTime=startTimeElement.getText();
							}
							org.dom4j.Element endTimeElement = luckInfoElement.element("endTime");
							String endTime="";
							if(null!=endTimeElement){
								endTime=endTimeElement.getText();
							}
							dzpactivityHtml=dzpactivityHtml.replace("dzpactivetime", startTime+"-"+endTime);
							String activetimediff="";
							if(!"".equals(startTime) && !"".equals(endTime) ){
								activetimediff = StringUtil.dateDiff(startTime, endTime, "yyyy-MM-dd HH:mm:ss");
							}
							dzpactivityHtml=dzpactivityHtml.replace("activetimediff", activetimediff);
							
							org.dom4j.Element takeTimeElement = luckInfoElement.element("takeTime");
							if(null!=takeTimeElement){
								String takeTime=takeTimeElement.getText();
								dzpactivityHtml=dzpactivityHtml.replace("takeTime", takeTime);
							}
							
							org.dom4j.Element actDetailElement = luckInfoElement.element("actDetail");
							if(null!=actDetailElement){
								String actDetail=actDetailElement.getText();
								dzpactivityHtml=dzpactivityHtml.replace("actDetail", actDetail);
							}
							
							org.dom4j.Element luckIdElement = luckInfoElement.element("luckId");
							String luckId="";
							if(null!=luckIdElement){
								luckId=luckIdElement.getText();
							}
							
							String choujiangbutton = 
							"soufun://waptoapp/{&quot;destination&quot;:&quot;dzp&quot;,&quot;luckId&quot;:&quot;"+luckId+"&quot;,&quot;activeId&quot;:&quot;"+activeId+"&quot;}";
							dzpactivityHtml=dzpactivityHtml.replace("choujiangButton", choujiangbutton);
							
							//参与活动人
						/*	org.dom4j.Element userListElement = activityElement.element("userList");
							if(null!=userListElement){
								String nicknames="";
								org.dom4j.Element totalUserNumElement = userListElement.element("totalUserNum");
								if(null!=totalUserNumElement){
									String totalUserNum=totalUserNumElement.getText();
									dzpactivityHtml=dzpactivityHtml.replace("totalUserNum", totalUserNum);
								}
								if(userListElement.elements("userInfo").size()>0){
									for(int i=0;i<userListElement.elements("userInfo").size();i++){
										org.dom4j.Element userInfoElement = (org.dom4j.Element) userListElement.elements("userInfo").get(i);
										if(null!=userInfoElement){
											org.dom4j.Element nickNameElement = userInfoElement.element("nickName");
											if(null!=nickNameElement){
												String nickName=nickNameElement.getText();
												
												if(i==0){
													nicknames=nickName;
												}else{
													nicknames=nicknames+"、"+nickName;
												}
											}
											
										}
										
									}
								}
								
								dzpactivityHtml=dzpactivityHtml.replace("activeUsers", nicknames);
							}*/
							
							//添加大转盘活动页面
							luntanContentBuffer.append(dzpactivityHtml);
						}
						
						if(null!=spikeActivityElement){
							//引入活动模板
							File msactivityFile = new File(Constant.HTML_TEMPLATE + "luntanactivitymiaosha.html");
							String msactivityHtml = AppHtmlUtil.readFileToString(msactivityFile);
							org.dom4j.Element isSpikeActivityElement = spikeActivityElement.element("isSpikeActivity");
							if(null!=isSpikeActivityElement){
								String isSpikeActivity=isSpikeActivityElement.getText();
								luntanParam.put("isSpikeActivity", isSpikeActivity);
							}
							org.dom4j.Element msimgsrcElement = spikeActivityElement.element("imgsrc");
							if(null!=msimgsrcElement){
								String imgsrc=msimgsrcElement.getText();
								msactivityHtml=msactivityHtml.replace("ms_pic", imgsrc);
							}
							org.dom4j.Element rewardElement = spikeActivityElement.element("reward");
							if(null!=rewardElement){
								String reward=rewardElement.getText();
								msactivityHtml=msactivityHtml.replace("reward", reward);
							}
							org.dom4j.Element priceElement = spikeActivityElement.element("price");
							if(null!=priceElement){
								String price=priceElement.getText();
								msactivityHtml=msactivityHtml.replace("price", price);
							}
							org.dom4j.Element spiketimeElement = spikeActivityElement.element("spiketime");
							String spiketime="";
							if(null!=spiketimeElement){
								spiketime=spiketimeElement.getText();
								msactivityHtml=msactivityHtml.replace("spiketime", spiketime);
							}
							org.dom4j.Element spikeEndTimeElement = spikeActivityElement.element("spikeEndTime");
							String spikeEndTime="";
							if(null!=spikeEndTimeElement){
								spikeEndTime=spikeEndTimeElement.getText();
								msactivityHtml=msactivityHtml.replace("spikeEndTime", spikeEndTime);
							}
							org.dom4j.Element winnumElement = spikeActivityElement.element("winnum");
							if(null!=winnumElement){
								String winnum=winnumElement.getText();
								msactivityHtml=msactivityHtml.replace("winnum", winnum);
							}
							org.dom4j.Element getRewardClosingDateElement = spikeActivityElement.element("getRewardClosingDate");
							if(null!=getRewardClosingDateElement){
								String getRewardClosingDate=getRewardClosingDateElement.getText();
								msactivityHtml=msactivityHtml.replace("getRewardClosingDate", getRewardClosingDate);
							}
							org.dom4j.Element mempermitElement = spikeActivityElement.element("mempermit");
							if(null!=mempermitElement){
								String mempermit=mempermitElement.getText();
								msactivityHtml=msactivityHtml.replace("mempermit", mempermit);
							}
							org.dom4j.Element actplaceElement = spikeActivityElement.element("actplace");
							if(null!=actplaceElement){
								String actplace=actplaceElement.getText();
								msactivityHtml=msactivityHtml.replace("actplace", actplace);
							}
							org.dom4j.Element signupnumElement = spikeActivityElement.element("signupnum");
							if(null!=signupnumElement){
								String signupnum=signupnumElement.getText();
								msactivityHtml=msactivityHtml.replace("signupnum", signupnum);
							}
							org.dom4j.Element idElement = spikeActivityElement.element("id");//秒杀id
							String spikeId="";
							if(null!=idElement){
								spikeId=idElement.getText();
								luntanParam.put("spikeId",spikeId);
							}
							org.dom4j.Element fieldsElement = spikeActivityElement.element("fields");
							String needrealname="0";
							if(null!=fieldsElement){
								org.dom4j.Element nameElement = fieldsElement.element("name");
								if(null!=nameElement){
									needrealname="1";
								}
							}
							//处理报名和秒杀按钮
							org.dom4j.Element isSignUpElement = spikeActivityElement.element("isSignUp");//是否报名,1已报名
							String isSignUp="";
							if(null!=isSignUpElement){
								isSignUp=isSignUpElement.getText();
							}
							org.dom4j.Element spikeBeginTimeElement = spikeActivityElement.element("spikeBeginTime");
							String spikeBeginTime="";// 秒杀开始时间
							if(null!=spikeBeginTimeElement){
								spikeBeginTime=spikeBeginTimeElement.getText();
								luntanParam.put("spikeBeginTime",spikeBeginTime);
							}
							String spikeBeginTimes="";
							if(!"".equals(spikeBeginTime)){
								spikeBeginTimes=StringUtil.timeToStr(spikeBeginTime, "yyyy-MM-dd HH:mm:ss");
							}
							String spiketimes="";//秒杀时间
							if(!"".equals(spiketime)){
								spiketimes=StringUtil.timeToStr(spiketime, "yyyy-MM-dd HH:mm:ss");
							}
							String spikeEndTimes="";//秒杀结束时间
							if(!"".equals(spikeEndTime)){
								spikeEndTimes=StringUtil.timeToStr(spikeEndTime, "yyyy-MM-dd HH:mm:ss");
							}
							String nowtime=StringUtil.getNowTime();//当前时间
							String nowtimes="";
							if(!"".equals(nowtime)){
								nowtimes=StringUtil.timeToStr(nowtime, "yyyy-MM-dd HH:mm:ss");
							}
							String msbutHref = 
								"soufun://waptoapp/{&quot;destination&quot;:&quot;msBut&quot;,&quot;spikeId&quot;:&quot;"+spikeId+"&quot;}";
							String msbaomingbutHref = 
								"soufun://waptoapp/{&quot;destination&quot;:&quot;bmBut&quot;,&quot;spikeId&quot;:&quot;"+spikeId+"&quot;,&quot;needrealname&quot;:&quot;"+needrealname+"&quot;}";
									
							String baomingButton="<a  class='disable' type='miaosha'>报名已结束</a>";
							String miaoshaButton="<a  class='disable' type='miaosha' >秒杀已结束</a>";
							String msdisp1=miaoshaButton;
							//luntanParam.put("msButtontoblank", "msbuid"+"#"+msdisp1);//只会从可点状态变到置灰状态
							String msdisp2="<a href='"+msbutHref+"' type='miaosha' >秒杀</a>";
							luntanParam.put("msButton", "msbuid"+"#"+msdisp2+"#"+msdisp1);//只会从置灰状态变到可点状态
							if(null!=nowtimes && !"".equals(nowtimes)  && !"".equals(spikeBeginTimes) && nowtimes.compareTo(spikeBeginTimes)<0){//秒杀开始前
								if("1".equals(isSignUp)){
									baomingButton="<a  class='visit' type='miaosha'>您已报名</a>";
								}else{
									baomingButton="<a href='"+msbaomingbutHref+"' type='miaosha'>我要参与</a>";
								}
								 miaoshaButton="<a  class='disable' type='miaosha' >秒杀</a>";
							}else if(null!=nowtimes && !"".equals(nowtimes)&& !"".equals(spikeEndTimes) && nowtimes.compareTo(spikeEndTimes)<0){
								if("1".equals(isSignUp)){
									baomingButton="<a  class='visit' type='miaosha'>您已报名</a>";
								}
								miaoshaButton="<a href='"+msbutHref+"' type='miaosha' >秒杀</a>";
							}
							msactivityHtml=msactivityHtml.replace("baomingbutton", baomingButton);
							msactivityHtml=msactivityHtml.replace("miaoshabutton", miaoshaButton);
								
								
							//获取名单
							Map<String,String> map=new HashMap<String, String>();
							map.put("urlOfCity", urlOfCity);
							map.put("from", from);
							map.put("sender", sender);
							map.put("sign", sign);
							map.put("spikeId", spikeId);
							map.put("user", user);
							String md=getmsmd(map);
							org.dom4j.Document docmd = DocumentHelper.parseText(md);  
							org.dom4j.Element rootmd = docmd.getRootElement();
							String content="<p class='no-con'>暂无人参与</p>";
							if(null!=rootmd){
								org.dom4j.Element messElement=rootmd.element("message");
								String mess="";
								if(null!=messElement){
									mess=messElement.getText();
								}
								
								if("success".equals(mess)){
									org.dom4j.Element contentElement=rootmd.element("content");
									if(null!=contentElement){
										content=contentElement.getText();
									}
								}
							}
							msactivityHtml=msactivityHtml.replace("miaoshalist", content);
							luntanContentBuffer.append(msactivityHtml);
						}
						//论坛content
						org.dom4j.Element contentElement = louzhuElement.element("content");
						if(null != contentElement){
							String luntanContent = null;
							if("zhuangxiu".equals(user)){
								luntanContent = URLDecoder.decode(contentElement.getText(),"GBK");
							}else{
								luntanContent = contentElement.getText().replace("&nbsp;", "");
							}
							luntanContentBuffer.append(luntanContent);
						}
						
						//论坛视频，添加到content后面
						//先判断是否有视频
						org.dom4j.Element isVideoEle = louzhuElement.element("isVideo");
						if(null != isVideoEle){
							String isVideo = isVideoEle.getText();
							//有视频，审核通过
							if("1".equals(isVideo)){
								String videoPic = null;
								String videoUrl = null;
								String videoWidth = null;
								String videoHeight = null;
								org.dom4j.Element videoPicElement = louzhuElement.element("videoPic");
								if(null != videoPicElement){
									videoPic = videoPicElement.getText();
								}
								org.dom4j.Element videoUrlElement = louzhuElement.element("videoUrl");
								if(null != videoUrlElement){
									videoUrl = videoUrlElement.getText();
								}
								org.dom4j.Element vwidthElement = louzhuElement.element("vPicWidth");
								if(vwidthElement != null){
									videoWidth = vwidthElement.getText();
								}
								org.dom4j.Element vheightElement = louzhuElement.element("vPicHeight");
								if(null != vheightElement){
									videoHeight = vheightElement.getText();
								}
								//如果存在视频
								if(!StringUtil.isEmpty(videoPic) && !StringUtil.isEmpty(videoUrl)){
									//是否有宽高
									if(StringUtil.isEmpty(videoWidth) || StringUtil.isEmpty(videoHeight)){
										//如果没有，服务器取宽高
										long sta=System.currentTimeMillis();
										String []widhei=AppHtmlUtil.getImgWidthAndHeight(videoPic);
										videoWidth = widhei[0];
										videoHeight = widhei[1];
										long end=System.currentTimeMillis();
										logger.info("getImgWidthAndHeightvideoImageend width="+videoWidth+"  height:"+videoHeight+"  url:"+videoPic+" use: " + (end-sta) + " ms");
										
									}
									if(!StringUtil.isEmpty(videoWidth) && !StringUtil.isEmpty(videoHeight)){
										luntanContentBuffer.append("<p class='videoBox'><a type='video' href='soufun://waptoapp/{\"destination\":\"forumplayvideo\",\"index\":\"0\",\"videourl\":\""+videoUrl+"\"}'></a>");
										luntanContentBuffer.append("<img type='video' id='video_0' width=" + videoWidth + " height=" + videoHeight + "></p>");
										//需要生成视频节点
										luntanParam.put("video", videoPic+"#"+videoUrl);
									}
									
									
								}
							}else if("0".equals(isVideo)){
								//有视频，但还没有审核
								luntanContentBuffer.append("<p><img type='video' src='http://img.soufun.com/quanzi/2015_09/06/10/59/002260618900.png'></p>");
							}else if("2".equals(isVideo)){
								//有视频，但未审核通过
								luntanContentBuffer.append("<p><img type='video' src='http://img.soufun.com/quanzi/2015_09/06/26/99/002264052500.png'></p>");
							}
						}
						//20151008 start 论坛投票开始
//						org.dom4j.Element voteElement = root.element("vote");
						if(null != voteElement){
							org.dom4j.Element isvoteElement = voteElement.element("isVote");
							if(null!=isvoteElement){
								isvote=isvoteElement.getText();
								if("1".equals(isvote)){
									//引入活动模板
									File voteFile = new File(Constant.HTML_TEMPLATE + "luntanvote.html");
									String voteHtml = AppHtmlUtil.readFileToString(voteFile);
									//投票截止时间
									org.dom4j.Element expireTimeElement = voteElement.element("expireTime");
									String expiretime = "";
									
									org.dom4j.Element voteIdElement = voteElement.element("voteId");
									String voteId="";//提交协议的参数
									if(null !=voteIdElement){
										voteId=voteIdElement.getText();
									}
									org.dom4j.Element statusElement = voteElement.element("status");
									if(null !=statusElement){
										String status=statusElement.getText();
										org.dom4j.Element optionElement = voteElement.element("option");
										int  optionSize=0;
										if(null !=optionElement){
											 optionSize= optionElement.elements("option_list").size();
										}
										org.dom4j.Element isAllowMutiElement = voteElement.element("isAllowMuti");//0：单选投票1：多选投票
										org.dom4j.Element resultVisiableElement = voteElement.element("resultVisiable");//0：投票前结果可见1：投票后结果可见（即投票前不显示结果）

										String isAllowMuti="";
										String resultVisiable="";
										if(null !=isAllowMutiElement && null !=resultVisiableElement){
											isAllowMuti=isAllowMutiElement.getText();//0：单选投票1：多选投票
											resultVisiable=resultVisiableElement.getText();////0：投票前结果可见1：投票后结果可见（即投票前不显示结果）
										}
										org.dom4j.Element maxMutiNumElement = voteElement.element("maxMutiNum");
										String maxMutiNum="10";
										if(null!=maxMutiNumElement){
											maxMutiNum=maxMutiNumElement.getText();
										}
										org.dom4j.Element user_statusElement = voteElement.element("user_status");
										String user_status="";
										if(null != user_statusElement){
											user_status=user_statusElement.getText();
										}
										if("1".equals(status)){//status=1则投票正常
											if(null != expireTimeElement){
												expiretime = expireTimeElement.getText()+"000";
												String endt=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date(Long.valueOf(expiretime)));
												voteHtml = voteHtml.replace("voteendtime","投票截止日期："+endt);
											}
											if(null != user_statusElement){
												
												user_status=user_statusElement.getText();
												if("1".equals(user_status) || "-1".equals(user_status)){//用户有权限投票，未投票 -1：用户id出错（此时用户可能未登录）
													
													
													if(null !=isAllowMutiElement && null !=resultVisiableElement){
														isAllowMuti=isAllowMutiElement.getText();//0：单选投票1：多选投票
														resultVisiable=resultVisiableElement.getText();////0：投票前结果可见1：投票后结果可见（即投票前不显示结果）
														
														if("1".equals(resultVisiable)){//没结果时显示“投票后结果可见”
															if("0".equals(isAllowMuti)){
																voteHtml = voteHtml.replace("radiochecktitle","<h1>单选投票<i>(投票后结果可见)</i></h1>");
															}else{
																voteHtml = voteHtml.replace("radiochecktitle","<h1>多选投票<i>(最多"+maxMutiNum+"项，投票后结果可见)</i></h1>");
															}
														}else{
															if("0".equals(isAllowMuti)){
																voteHtml = voteHtml.replace("radiochecktitle","<h1>单选投票</h1>");
															}else{
																voteHtml = voteHtml.replace("radiochecktitle","<h1>多选投票<i>(最多"+maxMutiNum+"项)</i></h1>");
															}
														}
														StringBuffer sbVoteOption=new StringBuffer();
														if(("0".equals(isAllowMuti) && "1".equals(resultVisiable))){//单选没结果  
															for(int i=0;i<optionSize;i++){
																int num=i+1;
																org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
																if(null !=optionListElement){
																	org.dom4j.Element optionNameElement = optionListElement.element("optionName");
																	org.dom4j.Element optionIdElement = optionListElement.element("optionId");
																	
																	sbVoteOption.append("<li><input type='radio' class='ipt-rd' name='optionName' onclick='changeColor(&apos;"+optionSize+"&apos;,&apos;0&apos;)' value='"+optionIdElement.getText()+"' >"+num+"."+optionNameElement.getText()+"</li>");
																	
																}
																
															}
														}else if("0".equals(isAllowMuti) && "0".equals(resultVisiable)){//单选有结果
															for(int i=0;i<optionSize;i++){
																int num=i+1;
																org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
																if(null !=optionListElement){
																	org.dom4j.Element optionNameElement = optionListElement.element("optionName");
																	org.dom4j.Element optionIdElement = optionListElement.element("optionId");
																	org.dom4j.Element optionCountElement = optionListElement.element("optionCount");
																	org.dom4j.Element perElement = optionListElement.element("per");
																	
																	sbVoteOption.append("<li><input type='radio' class='ipt-rd' name='optionName'  onclick='changeColor(&apos;"+optionSize+"&apos;,&apos;0&apos;)' value='"+optionIdElement.getText()+"' >"+num+"."+optionNameElement.getText()+"<span class='co"+(num%5)+"'>"+optionCountElement.getText()+"票&nbsp;&nbsp;"+perElement.getText()+"%</span>");
																	sbVoteOption.append("<div class='jdt'><div class='Pro bg"+((i%5)+1)+"' style='width:"+perElement.getText()+"%'></div></div></li>");
																}	
																
															}
														}else if ("1".equals(isAllowMuti) && "1".equals(resultVisiable)){//多选没结果
															for(int i=0;i<optionSize;i++){
																int num=i+1;
																org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
																if(null !=optionListElement){
																	org.dom4j.Element optionNameElement = optionListElement.element("optionName");
																	org.dom4j.Element optionIdElement = optionListElement.element("optionId");
																	
																	sbVoteOption.append("<li><input type='checkbox' class='ipt-cb' onclick='changeColor(&apos;"+optionSize+"&apos;,&apos;1&apos;)' name='optionName"+num+"' value='"+optionIdElement.getText()+"' >"+num+"."+optionNameElement.getText()+"</li>");
																	
																}
																
															}
														}else if("1".equals(isAllowMuti) && "0".equals(resultVisiable)){//多选有结果
															for(int i=0;i<optionSize;i++){
																int num=i+1;
																org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
																if(null !=optionListElement){
																	org.dom4j.Element optionNameElement = optionListElement.element("optionName");
																	org.dom4j.Element optionIdElement = optionListElement.element("optionId");
																	org.dom4j.Element optionCountElement = optionListElement.element("optionCount");
																	org.dom4j.Element perElement = optionListElement.element("per");
																	
																	sbVoteOption.append("<li><input type='checkbox' class='ipt-cb'  onclick='changeColor(&apos;"+optionSize+"&apos;,&apos;1&apos;)' name='optionName"+num+"' value='"+optionIdElement.getText()+"' >"+num+"."+optionNameElement.getText()+"<span class='co"+(num%5)+"'>"+optionCountElement.getText()+"票&nbsp;&nbsp;"+perElement.getText()+"%</span>");
																	sbVoteOption.append("<div class='jdt'><div class='Pro bg"+((i%5)+1)+"' style='width:"+perElement.getText()+"%'></div></div></li>");
																}	
																
															}
															
														}
														voteHtml = voteHtml.replace("option",sbVoteOption);
														if("0".equals(isAllowMuti)){//单选
															voteHtml = voteHtml.replace("submitButton","<a type='voteSubmit' id='votesubid' onclick='getRadio(&apos;"+voteId+"&apos;);'>提交</a>");//协议在js中
														}else{//多选
															voteHtml = voteHtml.replace("submitButton","<a type='voteSubmit' id='votesubid' onclick='getCheck(&apos;"+voteId+"&apos;,&apos;"+optionSize+"&apos;);'>提交</a>");
														}
													}
													
												}else {//0：用户无权限投票  2：用户有权限投票，已投票 
													if("0".equals(isAllowMuti)){//显示标题
														voteHtml = voteHtml.replace("radiochecktitle","<h1>单选投票</h1>");
													}else{
														voteHtml = voteHtml.replace("radiochecktitle","<h1>多选投票</h1>");
													}
													StringBuffer sbVoteOption=new StringBuffer();
													for(int i=0;i<optionSize;i++){
														int num=i+1;
														org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
														if(null !=optionListElement){
															org.dom4j.Element optionNameElement = optionListElement.element("optionName");
															org.dom4j.Element optionIdElement = optionListElement.element("optionId");
															org.dom4j.Element optionCountElement = optionListElement.element("optionCount");
															org.dom4j.Element perElement = optionListElement.element("per");
															org.dom4j.Element isChoiceElement = optionListElement.element("isChoice");
															if("2".equals(user_status) || "0".equals(resultVisiable)){//结果可见，已投票必然显示结果
																sbVoteOption.append("<li>"+num+"."+optionNameElement.getText());
																if("2".equals(user_status) && "1".equals(isChoiceElement.getText())){
																	sbVoteOption.append("<em>已投</em>");
																}
																sbVoteOption.append("<span class='co"+(num%5)+"'>"+optionCountElement.getText()+"票&nbsp;&nbsp;"+perElement.getText()+"%</span>");
																sbVoteOption.append("<div class='jdt'><div class='Pro bg"+((i%5)+1)+"' style='width:"+perElement.getText()+"%'></div></div></li>");
															}else{
																sbVoteOption.append("<li>"+num+"."+optionNameElement.getText()+"</li>");
																
															}
														}	
														
													}
													voteHtml = voteHtml.replace("option",sbVoteOption);
													if("2".equals(user_status)){
														voteHtml = voteHtml.replace("submitButton","已投票");
													}else{
														voteHtml = voteHtml.replace("submitButton","很抱歉，您没有投票权限");
													}
													
												}
											}
										}else{//投票不正常
											if("0".equals(isAllowMuti)){//显示标题
												voteHtml = voteHtml.replace("radiochecktitle","<h1 class='pat10'>单选投票</h1>");
											}else{
												voteHtml = voteHtml.replace("radiochecktitle","<h1 class='pat10'>多选投票</h1>");
											}
											voteHtml = voteHtml.replace("voteendtime","");//投票已结束不显示截止日期
											StringBuffer sbVoteOption=new StringBuffer();
											for(int i=0;i<optionSize;i++){
												int num=i+1;
												org.dom4j.Element optionListElement = (org.dom4j.Element) optionElement.elements("option_list").get(i);
												if(null !=optionListElement){
													org.dom4j.Element optionNameElement = optionListElement.element("optionName");
													org.dom4j.Element optionIdElement = optionListElement.element("optionId");
													org.dom4j.Element optionCountElement = optionListElement.element("optionCount");
													org.dom4j.Element perElement = optionListElement.element("per");
													org.dom4j.Element isChoiceElement = optionListElement.element("isChoice");
													
													
													sbVoteOption.append("<li>"+num+"."+optionNameElement.getText());
													if("2".equals(user_status) && "1".equals(isChoiceElement.getText())){
														sbVoteOption.append("<em>已投</em>");
													}
													sbVoteOption.append("<span class='co"+(num%5)+"'>"+optionCountElement.getText()+"票&nbsp;&nbsp;"+perElement.getText()+"%</span>");
													sbVoteOption.append("<div class='jdt'><div class='Pro bg"+((i%5)+1)+"' style='width:"+perElement.getText()+"%'></div></div></li>");
												}	
												
											}
											voteHtml = voteHtml.replace("option",sbVoteOption);
											if("2".equals(status)){
												voteHtml = voteHtml.replace("submitButton","已结束");
											}else if("3".equals(status)){
												voteHtml = voteHtml.replace("submitButton","已关闭");
											}
										}
									}
									luntanContentBuffer.append(voteHtml);
								}
								
							}
							
						}
						//20151008  end 投票结束
						
						//添加contenthtml
						luntanHtml = luntanHtml.replace("luntan_content", luntanContentBuffer);
						
						//楼主name
						org.dom4j.Element louzhuNickNameElement = louzhuElement.element("nickName");//显示昵称
						String louzhuNickName="";
//						if(!"zhuangxiu".equals(user)){//装修论坛只使用用户名
							if(null != louzhuNickNameElement){
								louzhuNickName = louzhuNickNameElement.getText();
								if(louzhuNickName!=null && !"".equals(louzhuNickName)){
									luntanHtml = luntanHtml.replace("louzhu_nickname", louzhuNickName);
									luntanParam.put("nickname", louzhuNickName);
								}
							} 
//						}
						org.dom4j.Element louzhuNameElement = louzhuElement.element("userName");
						if(null != louzhuNameElement){
							String louzhuName = louzhuNameElement.getText();
							luntanHtml = luntanHtml.replace("louzhu_name", louzhuName);
							if("".equals(louzhuNickName)){
								luntanHtml = luntanHtml.replace("louzhu_nickname", louzhuName);
								luntanParam.put("nickname", louzhuName);
							}
						}
						
						//楼主ID
						org.dom4j.Element louzhuIdElement = louzhuElement.element("userId");
						if(null != louzhuIdElement){
							String louzhuId = louzhuIdElement.getText();
							if("0".equals(louzhuId)){
							}else{
								luntanHtml = luntanHtml.replace("louzhu_userid", louzhuId);
							}
						}
						
						//楼主头像
						org.dom4j.Element louzhuHeadImgElement = louzhuElement.element("userImgsrc");
						if(null != louzhuHeadImgElement){
							String louzhuHeadImg = louzhuHeadImgElement.getText();
							luntanHtml = luntanHtml.replace("louzhu_head_img", louzhuHeadImg);
						}
						
						//创建时间
						org.dom4j.Element createTimeElement = louzhuElement.element("createTime");
						if(null != createTimeElement){
							String createTime = createTimeElement.getText() +"000";
							String creTim=AppHtmlUtil.getNeedTimeStringToHourMin(createTime);
							luntanHtml = luntanHtml.replace("create_time", creTim);
						}
					
						//楼主身份
						org.dom4j.Element userLevelElement = louzhuElement.element("userLevel");
						if(null != userLevelElement){
							String userLevel = userLevelElement.getText();
							if(!"".equals(userLevel) && !"空".equals(userLevel)){
								if("搜房网站长".equals(userLevel)){
									userLevel="站长";
								}
								String sf="<span class='rz-icon' style='display:inline-block;color:#ff9a22;margin-left:6px!important;vertical-align:1px;line-height:17px;'>";
								luntanHtml = luntanHtml.replace("lz_shenfen", sf+"<i></i>"+userLevel+"</span>");
							}else{
								luntanHtml = luntanHtml.replace("lz_shenfen", "");
							}
							
						}else{
							luntanHtml = luntanHtml.replace("lz_shenfen", "");
						}
						
						
					}
				}
				long endTime3 = System.currentTimeMillis();
				logger.info("until getlouzhuContent use time is "+(endTime3-endTime1)+"ms");
			}else{
				luntanHtml = luntanHtml.replace("luntan_jihua","");//若不是第一页则去掉此项
			}
			
			//如果返回所有帖子内容
			StringBuffer postHtmlBuffer = new StringBuffer();
			if(!"main".equals(useFor)){
				//获取其他所有的post_list节点：有回复
				if(root.elements("post_list").size() > 0){
					//获取论坛回复模板
					File postHtmlFile = new File(Constant.HTML_TEMPLATE + "luntanpost.html");
					String postHtml = AppHtmlUtil.readFileToString(postHtmlFile);
					//第一页的时候，第一个为楼主信息
					for(int i = "1".equals(page)?1:0;i < root.elements("post_list").size();i++){
						org.dom4j.Element postElement = (org.dom4j.Element) root.elements("post_list").get(i);
						if(null != postElement){
							String singlePostHtml = postHtml;
							//forumName
							if(null == forumName){
								org.dom4j.Element forumNameElement = postElement.element("forumName");
								if(null != forumNameElement){
									forumName = forumNameElement.getText();
									luntanHtml = luntanHtml.replace("forum_name", forumName);
									luntanParam.put("signName", forumName);
								}
							}
							//sign
							if(StringUtil.isEmpty(sign)){
								org.dom4j.Element signElement = postElement.element("sign");
								if(null != signElement){
									sign = signElement.getText();
									luntanParam.put("sign", sign);
								}
							}
							//masterId
							if(StringUtil.isEmpty(masterId)){
								org.dom4j.Element masterIdElement = postElement.element("masterId");
								if(null != masterIdElement){
									masterId = masterIdElement.getText();
									luntanParam.put("masterId", masterId);
								}
							}
							
							//楼层数
							org.dom4j.Element postFloorElement = postElement.element("postFloor");
							if(null != postFloorElement){
								singlePostHtml = singlePostHtml.replace("post_num", postFloorElement.getText());
							}else{
								singlePostHtml = singlePostHtml.replace("post_num", i+1+"");
							}
							
							//回复者头像
							org.dom4j.Element postHeadImgElement = postElement.element("userImgsrc");
							if(null != postHeadImgElement){
								String postHeadImg = postHeadImgElement.getText().trim();
								singlePostHtml = singlePostHtml.replace("post_head_img",postHeadImg);
							}else{
								singlePostHtml = singlePostHtml.replace("post_head_img","");
							}
							
							//postId
							org.dom4j.Element postIdElement = postElement.element("postId");
							String postId = null;
							if(null != postIdElement){
								postId = postIdElement.getText();
								singlePostHtml = singlePostHtml.replace("relpy_postid",postId);
								singlePostHtml = singlePostHtml.replace("to_ZhiDingWeiZhi","floor_"+postId);//跳转到页面指定位置
							}
							
							//回复者名字
							org.dom4j.Element nickNameElement = postElement.element("nickName");
							String nickName_post="";
//							if(!"zhuangxiu".equals(user)){//装修论坛只使用用户名
								if(null != nickNameElement){
									nickName_post = nickNameElement.getText();
									if(!"".equals(nickName_post)){
										singlePostHtml = singlePostHtml.replace("post_nickname",nickName_post);//显示昵称
									}
								}
//							}
							org.dom4j.Element postNameElement = postElement.element("userName");
							if(null != postNameElement){
								String postName = postNameElement.getText();
								singlePostHtml = singlePostHtml.replace("post_name",postName);
								if("".equals(nickName_post)){
									singlePostHtml = singlePostHtml.replace("post_nickname",postName);
								}
							}
							
							//回复者身份
							org.dom4j.Element userLevelElement = postElement.element("userLevel");
							if(null != userLevelElement){
								String userLevel = userLevelElement.getText();
								if(!"".equals(userLevel) && !"空".equals(userLevel)){
									if("搜房网站长".equals(userLevel)){
										userLevel="站长";
									}
									String sf="<span class='rz-icon' style='display:inline-block;color:#ff9a22;margin-left:6px!important;vertical-align:1px;line-height:17px;'>";
									singlePostHtml = singlePostHtml.replace("post_shenfen", sf+"<i></i>"+userLevel+"</span>");
								}else{
									singlePostHtml = singlePostHtml.replace("post_shenfen", "");
								}
								
							}else{
								singlePostHtml = singlePostHtml.replace("post_shenfen", "");
							}
							
							//回复者id
							org.dom4j.Element replyPostIdElement = postElement.element("userId");
							if(null != replyPostIdElement){
								String replyPostId = replyPostIdElement.getText();
								if("0".equals(replyPostId)){
								}else{
									singlePostHtml = singlePostHtml.replace("post_userid",replyPostId);
								}
								
							}
							
							//回复者content
							org.dom4j.Element postContentElement = postElement.element("content");
							if(null != postContentElement){
								String postContent = null;
								if("zhuangxiu".equals(user)){
									postContent = URLDecoder.decode(postContentElement.getText(),"GBK");
								}else{
									postContent = postContentElement.getText();
								}
								
								singlePostHtml = singlePostHtml.replace("post_content",postContent);
							}
							//回复者come_from
							org.dom4j.Element postComefromElement = postElement.element("come_from");
							if(null != postComefromElement){
								String comefrom = postComefromElement.getText();
								if("iosapp".equals(comefrom) || "AndroidApp".equals(comefrom)){
									singlePostHtml = singlePostHtml.replace("post_comefrom","<span>来自客户端 <img src='khd-icon.png' width='8' type='head'></span>");
								}else{
									singlePostHtml = singlePostHtml.replace("post_comefrom","");
								}
							}else{
								singlePostHtml = singlePostHtml.replace("post_comefrom","");
							}
							//创建时间
							org.dom4j.Element postCreateTimeElement = postElement.element("createTime");
							if(null != postCreateTimeElement){
								String postCreateTime = postCreateTimeElement.getText() + "000";
								String creTim=AppHtmlUtil.getNeedTimeString(postCreateTime);
								if("0分钟前".equals(creTim.trim())){
									creTim="刚刚";
								}
								singlePostHtml = singlePostHtml.replace("create_time",creTim);
							}
							
							//跟帖者
							org.dom4j.Element postReplyListElement = postElement.element("replyList");
							if(null != postReplyListElement){
								StringBuffer replyHtml = new StringBuffer();
								//获取节点个数，判断回复者个数
								int replyNum;
								/*if("zhuangxiu".equals(user)){
									replyNum = postReplyListElement.nodeCount() / 8;//未测??????不确定会新增3个还是4个，因为本身没有Nickname
								}else{*/
									replyNum = postReplyListElement.nodeCount() / 9;
//								}
								
								for(int j=0;j<replyNum;j++){
									org.dom4j.Element  postReplyUserId = postReplyListElement.element("userId"+j);
									org.dom4j.Element  postReplyUserName = postReplyListElement.element("username"+j);
									org.dom4j.Element  postReplyNickName=null;
									if("zhuangxiu".equals(user)){
										postReplyNickName = postReplyListElement.element("nickName"+j);
									}else{
										postReplyNickName = postReplyListElement.element("nickname"+j);
									}
									org.dom4j.Element  postReplyContent = postReplyListElement.element("content"+j);
									org.dom4j.Element  postReplyToUserId = postReplyListElement.element("toUserId"+j);
									org.dom4j.Element  postReplyToUserName = postReplyListElement.element("toUserName"+j);
									org.dom4j.Element  postReplyToNickName = postReplyListElement.element("toNickName"+j);
									if(null != postReplyUserName && null != postReplyContent){
										String userName = postReplyUserName.getText();
										String nickName = "";
										String userId = postReplyUserId.getText();
										String touserName = "";
										String tonickName = "";
										String touserId = "";
										String content = postReplyContent.getText();
										if(null!=postReplyNickName){
											nickName = postReplyNickName.getText();
										}
										if("zhuangxiu".equals(user)){
											content = content.replaceAll("%(?![0-9a-fA-F]{2})", "%25"); //若中文中出现%则先转一下
											content = URLDecoder.decode(content,"GBK");
//											nickName = userName;//如果是装修论坛，强制显示username
										}
										if("".equals(nickName)){
											nickName = userName;
										}
										if(null!=postReplyToUserName && null!=postReplyToUserId){
											touserName = postReplyToUserName.getText();
											tonickName = postReplyToNickName.getText();
											if("".equals(tonickName)){
												tonickName = touserName;
											}
											touserId = postReplyToUserId.getText();
										}
										String divhref = "soufun://waptoapp/{&quot;destination&quot;:&quot;comment&quot;,&quot;postId&quot;:&quot;"+postId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;,&quot;nickname&quot;:&quot;"+nickName+"&quot;,&quot;userId&quot;:&quot;"+userId+"&quot;,&quot;iscenzhu&quot;:&quot;"+0+"&quot;}";
										/*if(null != content && content.contains("回复") && !content.contains("内回复")){
											if(content.contains("：")){
												String repostName = content.substring(content.indexOf("回复")+2,content.indexOf("："));
												if(j>2){
													replyHtml.append("<div name='"+postId+"' style='display:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;0&quot;,&quot;username&quot;:&quot;"+repostName+"&quot;}' class='fblue'>" +repostName+"</a>" + content.substring(content.indexOf("："),content.length())+"</div>");
												}else{
													replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;0&quot;,&quot;username&quot;:&quot;"+repostName+"&quot;}' class='fblue'>" +repostName+"</a>" + content.substring(content.indexOf("："),content.length())+"</div>");
												}
												
											}else if(content.contains(":")){
												String repostName = content.substring(content.indexOf("回复")+2,content.indexOf(":"));
												if(j>2){
													replyHtml.append("<div name='"+postId+"' style='display:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;0&quot;,&quot;username&quot;:&quot;"+repostName+"&quot;}' class='fblue'>" +repostName+"</a>" + content.substring(content.indexOf(":"),content.length())+"</div>");
												}else{
													replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;0&quot;,&quot;username&quot;:&quot;"+repostName+"&quot;}' class='fblue'>" +repostName+"</a>" + content.substring(content.indexOf(":"),content.length())+"</div>");
												}
												
											}else{
												if(j>2){
													replyHtml.append("<div name='"+postId+"' style='display:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>：" + content + "</div>");
												}else{
													replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
															"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>：" + content + "</div>");
												}
												
											}
											
										}else{
											if(j>2){
												replyHtml.append("<div name='"+postId+"' style='dispaly:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>：" + content + "</div>");
											}else{
												replyHtml.append("<div onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>：" + content + "</div>");
											}
											
										}*/
										/////?????????????若返回节点数不固定，则需要变动
										// 若直接回复层组则直接显示nickname:content     若有被回复节点，则显示nickname回复tonickname：content
										if(null != touserId && !"".equals(touserId) && null != touserName && !"".equals(touserName)){
											if(j>2){
												replyHtml.append("<div name='"+postId+"' style='display:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+touserId+"&quot;,&quot;username&quot;:&quot;"+touserName+"&quot;}' class='fblue'>" +tonickName+"</a>" + ":"+content+"</div>");
											}else{
												replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>回复<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+touserId+"&quot;,&quot;username&quot;:&quot;"+touserName+"&quot;}' class='fblue'>" +tonickName+"</a>" + ":"+content+"</div>");
											}
										}else{
											if(j>2){
												replyHtml.append("<div name='"+postId+"' style='display:none' onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>:"+content+"</div>");
											}else{
												replyHtml.append("<div  onclick='javascript:window.location.href=&apos;"+ divhref+"&apos;;' class='hf-con'>" +
														"<a onclick='javascript:event.stopPropagation();' href='soufun://waptoapp/{&quot;destination&quot;:&quot;GRZL&quot;,&quot;userid&quot;:&quot;"+userId+"&quot;,&quot;username&quot;:&quot;"+userName+"&quot;}' class='fblue'>"+nickName + "</a>:"+content+"</div>");
											}
										}
										
									}
								}
								if(replyNum>3){
									replyHtml.append("<div class='hf-more' id='"+postId+"'><a href='javascript:;' onclick='showPostReply(&apos;"+postId +"&apos;)' class='fblue'>还有"+(replyNum-3)+"条评论...</a></div>");
								}
								singlePostHtml = singlePostHtml.replace("reply_div", "<div class='hf-box'>" + replyHtml+"</div>");
							}else{
								singlePostHtml = singlePostHtml.replace("reply_div", "");
							}
							postHtmlBuffer.append(singlePostHtml);
						}
					}
				}
			}
			long endTime4 = System.currentTimeMillis();
			logger.info("until getpostContent use time is "+(endTime4-endTime1)+"ms");
			//替换特殊字符
			luntanHtml = luntanHtml.replace("&amp;#10084;", "♥");
			
			//格式化论坛html模板
			Document luntanDoc = Jsoup.parse(luntanHtml);
			//如果不是第一页则去掉楼主信息
			if(!"1".equals(page)){
				Element louzhuDiv = luntanDoc.getElementById("louzhuDiv");
				louzhuDiv.remove();
			}
			//获取回复section
			Element postSection = luntanDoc.getElementById("Qlistsection");
			if("main".equals(useFor)){
				//只展示主贴
				postSection.remove();
			}else{
				//将回复加入到postSection中
				postSection.append(postHtmlBuffer.toString());
			}
			
			//处理table
			Elements tables = luntanDoc.select("table");
			if(null != tables && tables.size() >0){
				for(int i =0;i<tables.size();i++){
					tables.get(i).removeAttr("width");
					tables.get(i).attr("style","max-width:100%;border-collapse: collapse;");
				}
			}
			Elements tds = luntanDoc.select("td[width]");
			if(null != tds && tds.size() >0){
				for(int i=0;i<tds.size();i++){
					tds.get(i).removeAttr("width");
					tds.get(i).attr("style","BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent;  BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: windowtext 1pt solid; PADDING-TOP: 0cm");
				}
			}
			
			//处理"==========="号
			Elements spans = luntanDoc.select("span");
			if(null != spans && spans.size() >0){
				for(int i=0;i<spans.size();i++){
					if(spans.get(i).hasText()){
						if(spans.get(i).text().contains("===========")){
							spans.get(i).remove();
						}
						
					}
				}
			}
			//处理pre标签
			Elements pres = luntanDoc.select("pre");
			if(null != pres && pres.size() > 0){
				for(int i=0;i<pres.size();i++){
					pres.get(i).after("<p>"+pres.get(i).html() + "</p>");
					pres.get(i).remove();
				}
			}
			
			Elements ps = luntanDoc.select("p");
			if(null != ps && ps.size() >0){
				for(int i=0;i<ps.size();i++){
					if(ps.get(i).hasText()){
						if(ps.get(i).text().contains("===================")){
							ps.get(i).remove();
							continue;
						}
						if(null != ps.get(i).attr("style") && ps.get(i).attr("style").contains("text-indent")){
							ps.get(i).removeAttr("style");
						}
						//让英文字母折行
						if(ps.get(i).text().contains("http://")){
							if(ps.get(i).text().contains(".htm") || ps.get(i).text().contains(".aspx") ){
								String style = ps.get(i).attr("style");
								style = "word-break: break-all;" + style ;
								ps.get(i).attr("style",style);
							}
						}
						
					}
				}
			}
			//去掉h2标签的style
			Elements h2s = luntanDoc.select("h2");
			if(null != h2s && h2s.size() >0){
				for(int i=0;i<h2s.size();i++){
					h2s.get(i).removeAttr("style");
				}
			}
			//给宽度太大的div加max-width
			Elements divs = luntanDoc.select("div[style]");
			if(null != divs && divs.size() > 0){
				for(int i=0;i<divs.size();i++){
					String style = divs.get(i).attr("style");
					if(null != style && style.contains("width")){
						divs.get(i).attr("style","max-width:100%;"+style);
					}
				}
			}
			
			//给宽度太大的iframe加max-width
			Elements iframes = luntanDoc.select("iframe");
			if(null != iframes && iframes.size() > 0){
				for(int i=0;i<iframes.size();i++){
					String style = iframes.get(i).attr("style");
					iframes.get(i).attr("style","max-width:100%;"+style);
					iframes.get(i).removeAttr("width");
				}
			}
			
			//处理a链接
			AppHtmlUtil.romveAtagForLuntan(luntanDoc,user,shareUrl);
			
			//处理图片
//			List<String> imgUrlList = AppHtmlUtil.dealImgForLuntan(luntanDoc);
			List<String> imgUrlList = new ArrayList<String>();
		///////
			org.dom4j.Element cacheContentElement=null;
			org.dom4j.Element cacheroot=null;
			if(null !=cacheStr && !"".equals(cacheStr)){
				org.dom4j.Document cacheDoc = DocumentHelper.parseText(cacheStr);  
				cacheroot = cacheDoc.getRootElement();
				cacheContentElement = cacheroot.element("content");
			}
			long endTime5 = System.currentTimeMillis();
			logger.info("until dealcontent use time is "+(endTime5-endTime1)+"ms");
			
			if(null != cacheStr && null != cacheContentElement){//若主贴已有缓存,则从缓存中取出主贴部分20151019
				
//				imgUrlList = 
				org.dom4j.Element cacheimagelistEle = cacheroot.element("imagelist");//
				if(null!=cacheimagelistEle){
					List<org.dom4j.Element> cacheImageList=cacheimagelistEle.elements("img");
					for(org.dom4j.Element ele:cacheImageList){
						org.dom4j.Element cacheUrlEle=ele.element("imageurl");
						if(null != cacheUrlEle){
							String url=cacheUrlEle.getText();
							imgUrlList.add(url);
						}
					}
				}
				AppHtmlUtil.dealImgForLuntan_V1(luntanDoc,imgUrlList);
				String cacheContent = cacheContentElement.getText();
				String cacheSection = cacheContent.substring(cacheContent.indexOf("<section"), cacheContent.lastIndexOf("</section>")+10);
				luntanHtml=luntanDoc.html();
				luntanHtml = luntanHtml.replace(luntanHtml.substring(luntanHtml.indexOf("<section"), luntanHtml.indexOf("</section>")+10),cacheSection);
				org.dom4j.Element isGoodElement = cacheroot.element("isGood");
				if(null != isGoodElement){
					luntanParam.put("isGood", isGoodElement.getText());
				}
				org.dom4j.Element isTopElement = cacheroot.element("isTop");
				if(null != isTopElement){
					luntanParam.put("isTop", isTopElement.getText());
				}
				org.dom4j.Element isAuthElement = cacheroot.element("isAuth");
				if(null != isAuthElement){
					luntanParam.put("isAuth", isAuthElement.getText());
				}
				org.dom4j.Element signNameElement = cacheroot.element("signName");
				if(null != signNameElement){
					luntanParam.put("signName", signNameElement.getText());
				}
				org.dom4j.Element signElement = cacheroot.element("sign");
				if(null != signElement){
					luntanParam.put("sign", signElement.getText());
				}
				org.dom4j.Element masterIdElement = cacheroot.element("masterId");
				if(null != masterIdElement){
					luntanParam.put("masterId", masterIdElement.getText());
				}
				org.dom4j.Element shareImgUrlElement = cacheroot.element("shareImgUrl");
				if(null != shareImgUrlElement){
					luntanParam.put("shareImgUrl", shareImgUrlElement.getText());
				}
				org.dom4j.Element bidElement = cacheroot.element("bid");
				if(null != bidElement){
					luntanParam.put("bid", bidElement.getText());

				}
				org.dom4j.Element videoElement = cacheroot.element("video");
				if(null != videoElement){
					luntanParam.put("video", videoElement.getText());

				}
				org.dom4j.Element nicknameElement = cacheroot.element("nickname");
				if(null != nicknameElement){
					luntanParam.put("nickname", nicknameElement.getText());
				}
				luntanDoc=Jsoup.parse(luntanHtml);
			}else{
				imgUrlList=AppHtmlUtil.dealImgForLuntan(luntanDoc);
			}
			///////
			long endTime6 = System.currentTimeMillis();
			logger.info("all imgs use time is "+(endTime6-endTime5)+"ms");
			//生成xml文件
			xmlStr =  AppHtmlUtil.createXmlForLuntan("success", luntanDoc.html(), imgUrlList, luntanParam).asXML();
			long endTime7 = System.currentTimeMillis();
			logger.info("until createXML use time is "+(endTime7-endTime1)+"ms");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != xmlStr){
			return xmlStr;
		}else{
			return AppHtmlUtil.createXmlForLuntan("failed", "无此条记录").asXML();
		}
		
	}
	
	
	
	/**
	 * 获取原始数据
	 */
	@RequestMapping("/getdata")
	public void getOriginalData(HttpServletRequest request, HttpServletResponse response){
		try{
			String newsId = request.getParameter("newsid");
			String flag = request.getParameter("flag");
			PrintWriter out = response.getWriter();
			if(null!=flag && "1".equals(flag)){
				Map<String, String> daogouDetail = sqlServerDao.getHtmlStringForApp(newsId);
				if(null != daogouDetail){
					String result = daogouDetail.get("newscontent");
					char [] xmlChar = result.toCharArray();
					for(int i=0;i<xmlChar.length;i++){
						if(xmlChar[i]==1 || xmlChar[i] == 20){
							xmlChar[i] = ' ';
						}
					}
					result = String.valueOf(xmlChar);
					out.write(AppHtmlUtil.createXml("suc", result, null,null).asXML());
				}else{
					out.write(AppHtmlUtil.createXml("suc", "没有记录", null,null).asXML());
				}
				
			}else{
				Map<String, String> daogouDetail = sqlServerDao.getTouTiaoDetail(newsId);
				if(null != daogouDetail){
					String result = daogouDetail.get("newscontent");
					char [] xmlChar = result.toCharArray();
					for(int i=0;i<xmlChar.length;i++){
						if(xmlChar[i]==1 || xmlChar[i] == 20){
							xmlChar[i] = ' ';
						}
					}
					result = String.valueOf(xmlChar);
					
					////////////////
					String newsscope = daogouDetail.get("newsscope");
					String sx="";
					String citysx= "/www/3g.client.soufun.com/messageCenter/src/city_sx.txt";
//					String citysx= "D://eclipse2//messageCenter//src//city_sx.txt";
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(citysx), "utf-8"));

					String line = "";
					if(null!=newsscope){
						while((line=reader.readLine())!=null){
							String[] reslist=line.toString().split(";");
							if(reslist.length==2){
								if(newsscope.equals(reslist[0])){
									sx=reslist[1];
									break;
								}
								
							}
						}
					}
					String bbs_masterid = sqlServerDao.getBbs_masterid(newsId,sx);
					result=result+" sx:"+sx;
					//////////////////
					out.write(AppHtmlUtil.createXml("suc", result, null,null).asXML());
				}else{
					out.write(AppHtmlUtil.createXml("suc", "没有记录", null,null).asXML());
				}
			}
			
			out.close();
			//logger.info(daogouDetail.get("newscontent"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取 论坛详情页秒杀活动的秒杀名单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/miaoshamingdan")
	public void getMiaoShaMingDan(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("miaoshamingdan begin");
			long inTime = System.currentTimeMillis();
			String urlOfCity = request.getParameter("urlOfCity");
			String sign = request.getParameter("sign");
			String spikeId = request.getParameter("spikeId");
			String from = request.getParameter("from");
			String sender = request.getParameter("sender");
			Map<String,String> map=new HashMap<String, String>();
			map.put("urlOfCity", urlOfCity);
			map.put("sign", sign);
			map.put("spikeId", spikeId);
			map.put("from", from);
			map.put("sender", sender);
			map.put("replaceID", "listid");
			
			PrintWriter out = response.getWriter();
			out.write(getmsmd(map));
			out.close();
			long outTime = System.currentTimeMillis();
			logger.info("miaoshamingdan  end time use:" + (outTime-inTime) + " ms" );
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public String getmsmd(Map<String,String> parameters){
		String urlOfCity=parameters.get("urlOfCity");
		String sign=parameters.get("sign");
		String spikeId=parameters.get("spikeId");
		String from=parameters.get("from");
		String sender=parameters.get("sender");
		String replaceID=parameters.get("replaceID");
		String user=parameters.get("user");
		Map<String,String> luntanParam=new HashMap<String,String>();
		StringBuffer urlStr=null;
		if(null!=replaceID && !"".equals(replaceID)){
			luntanParam.put("replaceID", replaceID);
		}
		if(null!=user && "zhuangxiu".equals(user)){
			urlStr = new StringBuffer("http://"+urlOfCity + "/api/post/getSpikeList.php?");
		}else{
			if(null!=urlOfCity && !urlOfCity.endsWith("/")){
				urlOfCity=urlOfCity+"/";
			}
			urlStr = new StringBuffer(urlOfCity + "api/post/getSpikeList.php?");
		}
		
		if(null != sign){
			urlStr.append("&sign="+sign);
		}
		if(null != spikeId){
			urlStr.append("&spikeId="+spikeId);
		}
		if(null != from){
			urlStr.append("&from="+from);
		}
		if(null != sender){
			urlStr.append("&sender="+sender);
		}
		String checkcode=StringUtil.getRandomOfSix();
		if(null!=checkcode){
			urlStr.append("&checkcode="+checkcode);
		}
		urlStr.append("&limit=20");
		logger.info(" miaoshamingdan url : "+ urlStr);
		String xml;
		String xmlStr="";
		try {
			xml = StringUtil.getUrlTxtByGBK(urlStr.toString());
			org.dom4j.Document doc = DocumentHelper.parseText(xml);  
			org.dom4j.Element root = doc.getRootElement();
			StringBuffer mingdanSB = new StringBuffer();
			String dispMD="";
			if(null != root){
				org.dom4j.Element resultElement = root.element("result");
				org.dom4j.Element errorMsgElement = root.element("mess");
				String result="";
				if(null != resultElement){
					result = resultElement.getText();
					
				}
				if( !"100".equals(result)|| root.elements("spikeList").size()==0){
					String errorMsg="";
					if(null!=errorMsgElement){
						errorMsg=errorMsgElement.getText();
					}
					if("100".equals(result) ){
						errorMsg="没有名单";
					}
					return AppHtmlUtil.createXml("fail", errorMsg, null, null).asXML();
				}else{
					//引入模板
					for(int i=0;i<root.elements("spikeList").size();i++){
						org.dom4j.Element spikeElement = (org.dom4j.Element) root.elements("spikeList").get(i);
						if(null!=spikeElement){
							////////////
							org.dom4j.Element nicknameElement = spikeElement.element("nickname");
							String nickname_ms="";
							if(null != nicknameElement){
								nickname_ms = nicknameElement.getText();
							}
							/////////
							org.dom4j.Element usernameElement =  spikeElement.element("username");
							String username="";
							if(null!=usernameElement){
								username=usernameElement.getText();
								
								if(!"".equals(username)){
									if("".equals(nickname_ms)){
										nickname_ms=username;
									}
								}
							}
							org.dom4j.Element ctimeElement =  spikeElement.element("ctime");
							String ctime="";
							if(null!=ctimeElement){
								ctime=ctimeElement.getText();
							}
							if(!"".equals(nickname_ms) && !"".equals(ctime)){
								String usernametemp=StringUtil.getSubString(nickname_ms,10);
								if(!nickname_ms.equals(usernametemp)){
									nickname_ms=usernametemp+"..";
								}
								mingdanSB.append("<li><span>"+nickname_ms+"</span>本次秒杀时间："+ctime+"</li>");
							}
							
						}
						
					}
					if(!"".equals(mingdanSB.toString())){
						dispMD="<ul id='xst'>"+mingdanSB.toString()+"</ul>";
					}
					
					
					xmlStr=AppHtmlUtil.createXmlForLuntan("success",dispMD,null,luntanParam).asXML();
				}
			}else{
				return AppHtmlUtil.createXml("failed", "无需更新名单", null, null).asXML();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null!=xmlStr && !"".equals(xmlStr)){
			return xmlStr;
		}else{
			return AppHtmlUtil.createXml("failed", "无需更新名单", null, null).asXML();
		}
		
	}

	public  static void main(String args[]) throws Exception {
		String ret = "";
		try {
			logger.info("进入getProductDescriptionPropertys");
			long inTime = System.currentTimeMillis();
			//http://home.fang.com/jiancai/InterFace/GetProductForWap.aspx?cid=75&pid=3364576&did=69657

			//Map<String,String> mNewNode = new HashMap<String, String>();
			//org.dom4j.Element  propertys=null;
			//String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription+"cid="+cid+"&pid="+pid+"&did="+did);
			String result = StringUtil.getUrlTxtByGBK(Constant.sUrlProductDescription + "cid=75&pid=3364576&did=69657");
			logger.info(result);
			if (!StringUtil.isNull(result)) {
				//String description = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/Description/text()","gbk");
				List<Map<String,String>> rets = StringUtil.getListFromXmlNodeByXpath(result, "//productinfo/Propertys/Property", "gbk");

				//添加节点
				logger.info(result);
				String addTimes = StringUtil.getContentFromXmlNodeByXpath(result,"//productinfo/AddedTime/text()","gbk");
				logger.info(addTimes);

			} else {

			}
			long outTime = System.currentTimeMillis();
			logger.info("getProductDescriptionPropertys 执行结束，耗时:" + (outTime-inTime) + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
