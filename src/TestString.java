import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import a_小工具.DES;
import a_小工具.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class TestString {
	public static void main(String[] args){
		String s11 = null;
//		String[] a = s11.split(",");
		String askid = null;
		String usertypeParam = null;
		String askClassId = null;
		String theme = null;
		String addDate = null;
		String pubDate = null;
		String askTitle = null;
		String userAdd = null;
		String pubLish = null;
		String picturl = null;
		String askContent = null;
		String url = null;
		
		
//		System.out.println("#####################"+a.length+a[0]+"."+a[1]);
		/*
		 * 参数配置：
				AccreditID = f77c2bbd4b4f489a951895c310c21c37,固定
                CallTime = DateTime.Now.ToString(),当前时间
                PassportID = userId.ToString(),
                Type = “500021”,
                Quantity = 100,求悬赏的积分
                CityName = cityName,
                Title = “系统悬赏问题”,
                Description = “回答被采纳并通过审核”,
                ExtOrderID = "",为空
                IsExtOrderIDUnique = false,
                RedirectUrl = "",为空
                NotifyUrl = "",为空
                Source = source 来源
           	将以上参数 已 放到json数组中然后进行 通行证加密方法  加密【积分加密的key=”582acd52”】
 		最后解析参数返回信息 将数据作为参数 传给【param参数】
		 * */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONArray js = new JSONArray();
		 js.add(0,  "f77c2bbd4b4f489a951895c310c21c37");
		 js.add(1,  "500021");
		 System.out.println("****"+js);
		StringBuffer sb = new StringBuffer("http://jk.jifen.fang.com/PresentApply_V1.api?returntype=2&amp;param=");
		String str = "[{AccreditID = 'f77c2bbd4b4f489a951895c310c21c37',CallTime ='"+sdf.format(new Date())+"'," +
				"PassportID = '28943132',Type = '500021',CityName = '北京', Title = '系统悬赏问题',Description = '回答被采纳并通过审核',ExtOrderID ='',"   // 一个未转化的字符串
		+"IsExtOrderIDUnique = 'false',RedirectUrl = '',NotifyUrl ='',Source ='1'}]" ;  // 一个未转化的字符串
		JSONArray json = JSONArray.fromObject(str ); // 首先把字符串转成 JSONArray  对象
		System.out.println(json.toString()+".....");
		String name =null;
		String name1=json.toString().substring(1, json.toString().length()-1);
		System.out.println("*******"+name1);
		try {
			name1 = DES.encodeDES(name1, "582acd52", "582acd52");
			System.out.println(name1.length()+"*****加密后:"+name1);
			name = DES.encodeDES(json.toString(), "582acd52", "582acd52");
			System.out.println(name.length()+"加密后:"+name);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(json.size()>0){
		  for(int i=0;i<json.size();i++){
		    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
		    System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
		  }
		}
//		System.out.println(str);
		JSONObject jsonObject = null;
//		System.out.println(jsonObject.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", 1001);
		map.put("userName", "张三");
		map.put("userSex", "男");
		jsonObject = JSONObject.fromObject(map);
		System.out.println(jsonObject.toString());
		
		String s = "123";
		String[] s1 = new String[1];
		s1[0]="0";
		test(s);
		s=null;
		test(s);
	}
	public static void test(String s){
		System.out.println(s+"....");
	}
}
