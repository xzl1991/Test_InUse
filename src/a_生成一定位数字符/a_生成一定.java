package a_生成一定位数字符;

public class a_生成一定 {
	protected String getSerial()   {  
	    long sn = serial.getAndIncrement();  
	    if (sn + 1 > this.getLimit()) {// 如果达到上限，则更新上限  
	        synchronized (serial) {// serial是AtomicLong类型  
	            if (sn + 1 > this.getLimit()) {// 如果仍然达到上限，则更新上限  
	                serial.set(this.allocate());// 更新上限  
	                sn = serial.getAndIncrement();  
	            }  
	        }  
	    }  
	    return seiralFormat(sn, length);  
	}  
}
