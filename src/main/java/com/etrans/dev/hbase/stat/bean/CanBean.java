package com.etrans.dev.hbase.stat.bean;

import java.util.Map;

import com.e_trans.hadoop.mapreduce.cotroller.annotation.hbase.HbaseStoreType;
import com.e_trans.hadoop.mapreduce.cotroller.annotation.hbase.InputTable;
import com.e_trans.hadoop.mapreduce.cotroller.annotation.hbase.Store;

@InputTable(tableName = "can", columnFamliy = "col", column = "f2", type = HbaseStoreType.JSON)
public class CanBean {

	
	public CanBean() {
	
	}

	public CanBean(Map<Integer, String> value) {
		this.value = value;
	}

	@Store
	private Map<Integer, String> value;

	public Map<Integer, String> getValue() {
		return value;
	}

	public void setValue(Map<Integer, String> value) {
		this.value = value;
	}
}
