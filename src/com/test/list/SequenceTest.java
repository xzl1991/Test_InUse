package com.test.list;

public class SequenceTest {
	public static void main(String[] args){
		SequenceList<String> ls = new SequenceList<String>();
		System.out.println(ls.isEmpty());
		for(int i=0;i<5;i++){
			ls.add("元素"+i);
		}
		System.out.println(ls.indexOf("元素3"));
		ls.add(4, "最后位置");
		System.out.println(ls.isEmpty()+","+ls.size());
		ls.delete();
		System.out.println("元素位置:"+ls.indexOf("最后位置"));
		System.out.println("删除前:"+ls.get(4));
//		ls.delete(5);
		System.out.println(ls.toString());
		ls.delete("最后位置");
//		System.out.println(ls.get(4));
//		System.out.println(ls.get(5));
		System.out.println(ls.isEmpty()+","+ls.size());
		ls.clear();
	}
}
