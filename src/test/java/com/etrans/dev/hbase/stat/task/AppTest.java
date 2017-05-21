package com.etrans.dev.hbase.stat.task;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) {
		ArrayList<String> arrayList=new ArrayList<>();
		arrayList.add("1");
		arrayList.add("1");
		arrayList.add("1");
		arrayList.add("1");
		arrayList.add("1");
		for(String s:arrayList.toArray(new String[0]))
		{
			System.out.println(s);
		}
		
		
	}
}
