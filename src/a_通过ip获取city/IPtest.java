package a_通过ip获取city;

import junit.framework.TestCase;  

public class IPtest extends TestCase {  
      
    public void testIp(){  
                //指定纯真数据库的文件名，所在文件夹  
        IPSeeker ip=new IPSeeker("QQWry.Dat","E:/qqwry");  
         //测试IP 58.20.43.13  
        System.out.println(ip.getIPLocation("74.125.37.147").getCountry()+":"+ip.getIPLocation("58.20.43.13").getArea());  
        //192.168.114.226
        System.out.println(ip.getIPLocation("106.39.78.17").getCountry()+":"+ip.getIPLocation("106.39.78.17").getArea());
        System.out.println(ip.getIPLocation("202.199.047.255").getCountry()+":"+ip.getIPLocation("202.199.047.255").getArea());
        String city = ip.getIPLocation("202.199.047.255").getCountry();
        System.out.println(city.substring(city.indexOf("省")+1));
    
    } 
    public static void main(String[] args){
        //指定纯真数据库的文件名，所在文件夹  
    	IPSeeker ip=new IPSeeker("qqwry.dat","E:\\WorkSoft\\MyWorkspace\\MyWorkSpaces\\FangAskService\\src\\com\\ip\\city");  
   //测试IP 58.20.43.13  
  System.out.println(ip.getIPLocation("74.125.37.147").getCountry()+":"+ip.getIPLocation("58.20.43.13").getArea());  
  //192.168.114.226
  System.out.println(ip.getIPLocation("106.39.78.17").getCountry()+":"+ip.getIPLocation("106.39.78.17").getArea());
  System.out.println(ip.getIPLocation("202.199.047.255").getCountry()+":"+ip.getIPLocation("202.199.047.255").getArea());
  String city = ip.getIPLocation("202.199.047.255").getCountry();
  System.out.println(city.substring(city.indexOf("省")+1));

    }
}
