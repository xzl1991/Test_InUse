package a_常用算法.最短路径;
/**
 * 
 * 

Dijkstra对象用于计算起始节点到所有其他节点的最短路径
 * 
 * 		
 * 
 * */
public class Main {  
    public static void main(String[] args) {  
        Dijkstra test=new Dijkstra();  
        Node start=test.init();  
        test.computePath(start);  
        test.printPathInfo();  
    }  
}  
