package com.test.TreeSort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class 二叉树遍历 {
	private static List<String> allElement = new ArrayList<String>();

	public static void setElement() {
		allElement.add("A");
		allElement.add("A1");
		allElement.add("A2");
		allElement.add("A3");
		allElement.add("A4");
		allElement.add("A11");
		allElement.add("A21");
		allElement.add("A22");
		allElement.add("A41");
		allElement.add("A42");
		allElement.add("A111");
		allElement.add("A421");
	}

	public static void main(String[] args) {
		setElement();
		System.out.println("深度遍历:");
		deepOrder("A");
		System.out.println("广度遍历:");
		broadOrder("A");
	}

	// 深度遍历
	public static void deepOrder(String oneElement) {

		if (allElement.contains(oneElement)) {
			Stack<String> s = new Stack<String>();
			s.push(oneElement);
			while (!s.isEmpty()) {
				String now = s.pop();
				StringBuffer t = getSpace(now);
				System.out.println(t.toString() + now);
				s.addAll(getChild("deep", now));
			}
		}

	}

	// 根据传入的string元素来返回需要的空格
	private static StringBuffer getSpace(String now) {
		StringBuffer t = new StringBuffer("");
		for (int i = 0; i < now.length(); i++) {
			t.append(" ");
		}
		return t;
	}

	// 获取子元素
	private static Collection<String> getChild(String mode, String oneElement) {
		List<String> childs = new ArrayList<String>();
		for (int i = 0; i < allElement.size(); i++) {
			if (allElement.get(i).toString().length() == oneElement.length() + 1
					&& (allElement.get(i).toString()
							.substring(0, oneElement.length())
							.equals(oneElement))) {
				if (mode.equals("deep")) {
					// 此处保证集合中最后一个元素是需要显示在当前层级中第一个展示的子节点（因为堆栈中是最后一个元素先出）
					if (childs != null
							&& childs.size() != 0
							&& Integer.valueOf(allElement.get(i).toString()
									.substring(1)) > Integer.valueOf(childs
									.get(0).toString().substring(1))) {
						childs.add(0, allElement.get(i));
					} else {
						childs.add(allElement.get(i));
					}
				} else {
					if (childs != null
							&& childs.size() != 0
							&& Integer.valueOf(allElement.get(i).toString()
									.substring(1)) < Integer.valueOf(childs
									.get(0).toString().substring(1))) {
						childs.add(0, allElement.get(i));
					} else {
						childs.add(allElement.get(i));
					}
				}
			}
		}
		return childs;
	}

	// 广度遍历
	private static void broadOrder(String oneElement) {
		if (allElement.contains(oneElement)) {
			List<String> l = new ArrayList<String>();
			l.add(oneElement);
			while (!l.isEmpty()) {
				String now = l.get(0);
				l.remove(0);
				StringBuffer t = getSpace(now);
				System.out.println(t.toString() + now);
				l.addAll(getChild("broad", now));
			}
		}
	}
}
