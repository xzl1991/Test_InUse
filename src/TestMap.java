import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TestMap {
public static void main(String args[]){
	String name1 ="_1";
	System.out.println(name1.contains("-")+"------"+name1.compareTo("-2")+"*****"+name1.compareTo("0"));
     Map<String,String> pa= new HashMap<String,String>();
     pa.put("name", "姓名");
     pa.put("id", "姓名1");
     pa.put("id", "姓名id...");
     pa.put("add", "姓名");
//   Map set = pa.entrySet();
     Iterator it=pa.keySet().iterator();
     
     String name="姓名";
     Map<String,String> pa1= new HashMap<String,String>();
     pa1 = pa;
     System.out.println( "第一种遍历" );
     while(it.hasNext()){   
          String key;   
          String value;   
          key=it.next().toString();   
          value=pa.get(key);  
          //通过 value 找 key
          if(value.equals(name)){
        	  System. out.print("通过value找Key:"+key+"--" +value+"\n");   
          }
     } 
//   22222222
     System.out.println( "");
     System.out.println( "第2种遍历" );
     Set set = pa.entrySet();        
     Iterator i = set.iterator();        
     while(i.hasNext()){     
          Map.Entry<String, String> entry1=(Map.Entry<String, String>)i.next();   
          System.out.print(entry1.getKey()+"==" +entry1.getValue());   
     }  
//   Iterator
     System.out.println( "");
     System.out.println( "第3种遍历" );
     it=pa.entrySet().iterator();          
//   System.out.println( pa.entrySet().size());   
     String key;          
     String value;   
     while(it.hasNext()){   
             Map.Entry entry = (Map.Entry )it.next();          
             key=entry.getKey().toString();          
             value=entry.getValue().toString();          
             System. out.print(key+"====" +value+"\n");                    
     }  
     
     
     System.out.println( "");
     System.out.println( "第4种遍历" );
     for(Map.Entry<String, String> entry:pa.entrySet()){   
          System.out.print(entry.getKey()+"--->" +entry.getValue()+"\n");   
     }  
     for(Map.Entry<String, String> entry:pa.entrySet()){
               System.out.print(entry.getKey()+"---111--->" +entry.getValue());
               System.out.print(entry.getKey()+"---111--->" +entry.getValue());
     }
}
}



