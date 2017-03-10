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
public class GetHtmlDealForbidId {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String GetPageCode(String PageURL)
    {
		String Charset = "gb2312";
        try
        {
            //存放目标网页的html
            String strHtml = "";
            //连接到目标网页
            HttpWebRequest wreq = (HttpWebRequest)WebRequest.Create(PageURL);
            wreq.Headers.Add("X_FORWARDED_FOR", "101.0.0.11"); //发送X_FORWARDED_FOR头(若是用取源IP的方式，可以用这个来造假IP,对日志的记录无效)  

           wreq.Method = "Get";
            wreq.KeepAlive = true;
            wreq.ContentType = "application/x-www-form-urlencoded";
            wreq.AllowAutoRedirect = true;
            wreq.Accept = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*";
            wreq.UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)";

           CookieContainer cookieCon = new CookieContainer();
            wreq.CookieContainer = cookieCon;

           HttpWebResponse wresp = (HttpWebResponse)wreq.GetResponse();

           //采用流读取，并确定编码方式
            Stream s = wresp.GetResponseStream();
            StreamReader objReader = new StreamReader(s, System.Text.Encoding.GetEncoding(Charset));

           string strLine = "";
            //读取
            while (strLine != null)
            {
                strLine = objReader.ReadLine();
                if (strLine != null)
                {
                    strHtml += strLine.Trim();
                }
            }
            strHtml = strHtml.Replace("<br />", "\r\n");

           return strHtml;
        }
        catch (Exception n) //遇到错误，打印错误
        {
            return n.Message;
        }
    }
}
