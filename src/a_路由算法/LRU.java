package a_路由算法;

public class LRU {  
    /** 
     * 内存块的个数 
     */  
    public static final int N = 5;  
    /** 
     * 内存块数组 
     */  
    Object[] array = new Object[N];  
    private int size;  
      
    public LRU() {  
    }  
    /** 
     * 判断内存区是否为空 
     * @return 
     */  
    public boolean isEmpty() {  
        if(size == 0) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    /** 
     * 判断内存区是否达到最大值 
     * @return 
     */  
    public boolean isOutOfBoundary() {  
        if(size >=N) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    /** 
     * 查找元素o在数组中的位置 
     * @param o 
     * @return 
     */  
    public int indexOfElement(Object o) {  
        for(int i=0; i<N; i++) {   
            if(o == array[i]) {  
                return i;  
            }  
        }  
        return -1;  
    }     
    /** 
     * 有新的数据o需要申请内存 
     * @param o 
     * @return 移出内存区的数据 
     */  
    public Object push(Object o) {  
        int t = -1;  
        if(!isOutOfBoundary() && indexOfElement(o) == -1){  
            array[size] = o;  
            size ++;  
        } else if(isOutOfBoundary() && indexOfElement(o) == -1){  
            for(int i=0; i<size-1; i++) {  
                array[i] = array[i+1];                
            }  
            array[size-1] = o;  
        } else {  
            t = indexOfElement(o);  
            for(int i=t; i<size-1; i++) {  
                array[i] = array[i+1];  
            }  
            array[size-1] = o;  
        }  
        if( -1 == t) {  
            return null;  
        } else {  
            return array[t];  
        }  
    }  
    /** 
     * 输出内存区中的各数据 
     */  
    public void showMemoryBlock() {  
        for(int i=0; i<size; i++) {  
            System.out.print(array[i] + "\t");  
        }  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        Integer iter[] = {4,7,0,7,1,0,1,2,1,2,6};  
        LRU lru = new LRU();  
        for(int i=0; i<iter.length; i++) {  
            lru.push(iter[i]);  
            lru.showMemoryBlock();  
            System.out.println();  
        }  
    }  
  
} 
