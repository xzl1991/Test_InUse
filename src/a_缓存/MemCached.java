package a_缓存;

import java.util.Date;
import java.util.Properties;

import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.transcoders.SerializingTranscoder;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
* 使用memcached的缓存实用类.
* 
* @author 铁木箱子
*
*/
public class MemCached
{
    // 创建全局的唯一实例
    protected static MemCachedClient mcc = new MemCachedClient();
    //createMemcachedClientBuilder("127.0.0.1:11211");
    
    protected static MemCached memCached = new MemCached();
    
    // 设置与缓存服务器的连接池
    static {
        // 服务器列表和其权重
        String[] servers = {"127.0.0.1:11211"};
        Integer[] weights = {3};

        // 获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();

        // 设置服务器信息
        pool.setServers( servers );
        pool.setWeights( weights );

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn( 5 );
        pool.setMinConn( 5 );
        pool.setMaxConn( 250 );
        pool.setMaxIdle( 1000 * 60 * 60 * 6 );

        // 设置主线程的睡眠时间
        pool.setMaintSleep( 30 );

        // 设置TCP的参数，连接超时等
        pool.setNagle( false );
        pool.setSocketTO( 3000 );
        pool.setSocketConnectTO( 0 );

        // 初始化连接池
        pool.initialize();

        // 压缩设置，超过指定大小（单位为K）的数据都会被压缩
//        mcc.setCompressEnable( true );
//        mcc.setCompressThreshold( 64 * 1024 );
//        mcc.setTransCoder(arg0)
    }
    
    /**
     * 保护型构造方法，不允许实例化！
     *
     */
    protected MemCached()
    {
        
    }
    
    /**
     * 获取唯一实例.
     * @return
     */
    public static MemCached getInstance()
    {
        return memCached;
    }
    
    /**
     * 添加一个指定的值到缓存中.
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value)
    {
        return mcc.add(key, value);
    }
    
    public boolean add(String key, Object value, Date expiry)
    {
        return mcc.add(key, value, expiry);
    }
    
    public boolean replace(String key, Object value)
    {
        return mcc.replace(key, value);
    }
    
    public boolean replace(String key, Object value, Date expiry)
    {
        return mcc.replace(key, value, expiry);
    }
    public  boolean deleteCache(String key){
		return mcc.delete(key);
	}
    /**
     * 根据指定的关键字获取对象.
     * @param key
     * @return
     */
    public Object get(String key)
    {
        return mcc.get(key);
    }
    private static  MemcachedClientBuilder createMemcachedClientBuilder(  
            String properties) {  
        String addresses = properties.trim();  
      
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(  
                AddrUtil.getAddresses(addresses));  
      
        // 使用二进制文件  
        builder.setCommandFactory(new BinaryCommandFactory());  
        // 使用一致性哈希算法（Consistent Hash Strategy）  
        builder.setSessionLocator(new KetamaMemcachedSessionLocator());  
        // 使用序列化传输编码  
        builder.setTranscoder((Transcoder) new SerializingTranscoder());  
        // 进行数据压缩，大于1KB时进行压缩  
        builder.getTranscoder().setCompressionThreshold(1024);  
      
        return builder;  
    }  
    public static void main(String[] args)
    { 
    	MemCached cache = MemCached.getInstance();
    	System.out.println(cache.get("zlex")+".....");
    	System.out.println("开始.......");
        System.out.println("设置前 : " + cache.get("hello"));
        cache.add("hello", 124);
        cache.deleteCache("hello");
        System.out.println("删除缓存 : " + cache.get("hello"));
        cache.replace("hello", 234);
        System.out.println("get value : " + cache.get("hello"));
        System.out.println("get value1 : " + cache.get("sss"));
        TBean tb = new TBean();
        tb.setName("铁木箱子");
        cache.add("bean", tb);
//        cache.replace("bean", tb);
        System.out.println(cache.get("bean"));
        TBean tb1 = (TBean)cache.get("bean");
        System.out.println("name=" + tb1.getName());
        tb1.setName("铁木箱子_修改的");
        tb1 = (TBean)cache.get("bean");
        System.out.println("set修改=" + tb1.getName());
        
        tb1.setName("铁木箱子_修改的");
        cache.replace("bean", tb1);//.get("bean");
        tb1 = (TBean) cache.get("bean");
        System.out.println("cache.replace修改=" + tb1.getName());
    }
}

class TBean implements java.io.Serializable
{
    private static final long serialVersionUID = 1945562032261336919L;
    
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
























