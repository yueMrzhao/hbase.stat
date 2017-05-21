package com.etrans.dev.hbase.stat.map.can;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

import com.e_tran.data.hadoop.mapreduce.hbase.task.AAutoHbaseMapperTask2;
import com.e_trans.hadoop.mapreduce.cotroller.writable.ETListWritable;
import com.etrans.dev.hbase.stat.bean.CanBean;

public class CanTask extends AAutoHbaseMapperTask2<CanBean> {
	@Override
	public void init(Configuration arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Map<Writable, ETListWritable> call(Configuration configuration, byte[] rowkey, LinkedHashMap<Long, CanBean> datas)
			throws Exception {
		CanAnalyse canAnalyse = new CanAnalyse();
		return canAnalyse.analyse(configuration, rowkey, datas);
	}
}
