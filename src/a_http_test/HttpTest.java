package a_http_test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class HttpTest {
	public static void main(String[] args) throws ClientProtocolException, IOException{
		File file =new File("E:/MyWorkSpace/Test/WebContent/test.html");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		String str = http();
		out.write(str);
		out.flush();
	}
	public static String http() throws ClientProtocolException, IOException{

		String url = "https://daohang.qq.com/";
		//组装请求
		HttpClient httpClient = new DefaultHttpClient(); 
		HttpGet httpGet = new HttpGet(url);
		//接收响应
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		byte[] bytes = EntityUtils.toByteArray(entity);
		String result = new String(bytes,"utf-8");
		/**
		 * 向目标服务器发送了 HttpGet 请求
		 * 将服务器的返回 解码得到 html 文档
		 * */
		return result;
	}
}



































