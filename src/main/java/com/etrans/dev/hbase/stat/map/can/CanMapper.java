package com.etrans.dev.hbase.stat.map.can;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;

import com.e_tran.data.hadoop.mapreduce.hbase.mapper.CommonManyTableAutoHbaseMapper;
import com.e_tran.data.hadoop.mapreduce.hbase.task.AHbaseMapperTask;

public class CanMapper extends CommonManyTableAutoHbaseMapper<IntWritable> {

	@Override
	public List<AHbaseMapperTask> tasks() {
		List<AHbaseMapperTask> listTasks = new ArrayList<>();

		listTasks.add(new CanTask());
		return listTasks;
	}
}
