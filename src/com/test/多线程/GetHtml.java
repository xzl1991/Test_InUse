package com.test.多线程;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.dom4j.DocumentHelper;

public class GetHtml {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		GetHtml g = new GetHtml();
		long start = System.currentTimeMillis();
		String contents = g.getHtml("http://www.google.com/");
		long end = System.currentTimeMillis();
		System.out.println("总时间:"+(end-start));
		System.out.println("读取的内容是:"+contents);
		OutputStreamWriter wr = null;
		//输出位置
		wr = new OutputStreamWriter(new FileOutputStream(new File("E:"+File.separator+"get.html")));
		wr.write(contents);
		wr.close();
		
		
		//使用dom4j 处理  获取的网页信息 
		org.dom4j.Document doc = DocumentHelper.parseText(contents);
		//获取根元素 
		org.dom4j.Element root = doc.getRootElement();
		
	}
	
	//获取网页信息
	public String getHtml(String urls) throws Exception{
		StringBuffer sb = new StringBuffer();
		BufferedReader reader=null;
		String line = null;
		
//		if(urls.startsWith("file")){
//			System.out.println(urls.contains("c"));
//			int begin = urls.indexOf("C:");
//			if(begin!=-1){
//				
//			}
//			System.out.println(begin);
//			String[] url = urls.split("C:", begin-1);
//			String s = urls.substring(begin);
//			System.out.println(s);
//			System.out.println(url[0]);
//			System.out.println(url[1]);
//		}
		
		try {
			// 获取url
			URL url = new URL(urls);
			// 建立连接  打开连接
			HttpURLConnection coon = (HttpURLConnection) url.openConnection();
			//设置连接参数
			coon.setConnectTimeout(5000);
			coon.setReadTimeout(5000);
			coon.addRequestProperty("Accept-Encoding", "gzip");
			coon.connect();
			
			//处理数据
			String encodeType = coon.getContentEncoding();//请求类型
			if(encodeType!=null&&encodeType.contains("gzip")){
				//如果有编码格式 解码参数
				GZIPInputStream gip = new GZIPInputStream(coon.getInputStream());
				reader = new BufferedReader(new InputStreamReader(gip,"utf-8"));
			}else
				reader = new BufferedReader(new InputStreamReader(coon.getInputStream(),"UTF-8"));
			
			 //拼接获取的内容
			while((line = reader.readLine())!=null){
				sb.append(line);
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (Exception e2) {
				e2.getStackTrace();
			}
		}
		return sb.toString();
	}
}
