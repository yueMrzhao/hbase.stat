package com.etrans.dev.hbase.stat.map.can;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import com.e_trans.data.common.bytes.IntUtil;
import com.e_trans.hadoop.mapreduce.cotroller.writable.ETListWritable;
import com.etrans.dev.hbase.stat.bean.CanBean;
import com.etrans.dev.hbase.stat.data.CanTimePeriodStat;

public class CanAnalyse {

	public Map<Writable, ETListWritable> analyse(Configuration configuration, byte[] rowkey,
			LinkedHashMap<Long, CanBean> datas) {

		Map<Writable, ETListWritable> map = new HashMap<>();
		ETListWritable etListWritable = new ETListWritable();

		// String key = Bytes.toString(rowkey);
		// System.out.println(String.format("rowKey = %s", key));
		// int rid = (int) (Bytes.toLong(rowkey) >>> 4);
		// int vehicle_id = IntUtil.intRevert(rid);
		int vehicle_id = IntUtil.intRevert(Bytes.toInt(Bytes.copy(rowkey, 0, 4)));
		// System.out.println(vehicle_id);
		// int vehicle_id = Integer.valueOf(key.substring(0, key.length() - 8));
		String temp = "";
		Date canTime = null;
		CanTimePeriodStat timePeriodStat = null;
		int i = 0;
		Set<Entry<Long, CanBean>> entrySet = datas.entrySet();
		for (Entry<Long, CanBean> entry : entrySet) {
			i++;
			canTime = new Date(entry.getKey());
			CanBean info = entry.getValue();
			// 发动机转速
			temp = info.getValue().get("11");
			if (temp == null)
				continue;
			int revs = (int) Math.floor(Float.valueOf(temp));

			// 粗略总行驶里程
			temp = info.getValue().get("18");
			if (temp == null)
				continue;
			int mileage = (int) Math.floor(Float.valueOf(temp) * 10);

			// 发动机瞬时油耗率
			temp = info.getValue().get("20");
			if (temp == null)
				continue;
			int oil = (int) Math.floor(Float.valueOf(temp) * 10);

			// CAN速度
			temp = info.getValue().get("17");
			if (temp == null)
				continue;
			int canSpeed = (int) Math.floor(Float.valueOf(temp) * 10);

			// 消耗油量(L)
			double curroil = 1 / (oil / 10.0) * 100 * (canSpeed / 10.0) / 120;

			// 转速大于0
			if (revs > 0) {
				if (timePeriodStat == null) {
					// 开始发动机转速大于0的时间区间
					timePeriodStat = new CanTimePeriodStat(1, vehicle_id, canTime, revs, mileage, canSpeed, curroil);
				} else {
					if (timePeriodStat.getTime_period_type() == 0) {
						// 结束发动机转速为0的时间区间
						saveTimePeriodStat(etListWritable, timePeriodStat);
						// 开始发动机转速大于0的时间区间
						timePeriodStat = new CanTimePeriodStat(1, vehicle_id, canTime, revs, mileage, canSpeed,
								curroil);
					} else {
						// 平均转速
						timePeriodStat.setAvg_revs(timePeriodStat.getAvg_revs() + revs);
						// 最高转速
						if (revs > timePeriodStat.getMax_revs())
							timePeriodStat.setMax_revs(revs);
						// 最低转速
						if (revs < timePeriodStat.getMin_revs())
							timePeriodStat.setMin_revs(revs);
						// 平均速度
						timePeriodStat.setAvg_speed(timePeriodStat.getAvg_speed() + canSpeed);
						// 最高速度
						if (canSpeed > timePeriodStat.getMax_speed())
							timePeriodStat.setMax_speed(canSpeed);
						// 最低速度
						if (canSpeed < timePeriodStat.getMin_speed())
							timePeriodStat.setMin_speed(canSpeed);
						// 总油耗
						timePeriodStat.setTotal_oil_consumption(timePeriodStat.getTotal_oil_consumption() + curroil);
						// 行驶里程
						timePeriodStat.setEnd_mileage(mileage);
						// can时间
						timePeriodStat.setEnd_time(canTime);
						// 记录条数
						timePeriodStat.setRecordCount(timePeriodStat.getRecordCount() + 1);
					}
				}
			} else {
				if (timePeriodStat == null) {
					// 开始发动机转速为0的时间区间
					timePeriodStat = new CanTimePeriodStat(0, vehicle_id, canTime, revs, mileage, canSpeed, curroil);
				} else {
					if (timePeriodStat.getTime_period_type() == 1) {
						// 结束发动机转速大于0的时间区间
						saveTimePeriodStat(etListWritable, timePeriodStat);
						// 开始发动机转速为0的时间区间
						timePeriodStat = new CanTimePeriodStat(0, vehicle_id, canTime, revs, mileage, canSpeed,
								curroil);
					} else {// 暂时试试，实际应用需要删除
						// 平均转速
						timePeriodStat.setAvg_revs(timePeriodStat.getAvg_revs() + revs);
						// 最高转速
						if (revs > timePeriodStat.getMax_revs())
							timePeriodStat.setMax_revs(revs);
						// 最低转速
						if (revs < timePeriodStat.getMin_revs())
							timePeriodStat.setMin_revs(revs);
						// 平均速度
						timePeriodStat.setAvg_speed(timePeriodStat.getAvg_speed() + canSpeed);
						// 最高速度
						if (canSpeed > timePeriodStat.getMax_speed())
							timePeriodStat.setMax_speed(canSpeed);
						// 最低速度
						if (canSpeed < timePeriodStat.getMin_speed())
							timePeriodStat.setMin_speed(canSpeed);
						// 总油耗
						timePeriodStat.setTotal_oil_consumption(timePeriodStat.getTotal_oil_consumption() + curroil);
						// 行驶里程
						timePeriodStat.setEnd_mileage(mileage);
						// can时间
						timePeriodStat.setEnd_time(canTime);
						// 记录条数
						timePeriodStat.setRecordCount(timePeriodStat.getRecordCount() + 1);
					}
				}
			}
			// 正在分析的车辆最后一条记录
			if (i == datas.size()) {
				// 结束时间区间
				saveTimePeriodStat(etListWritable, timePeriodStat);
			}
		}
		map.put(new IntWritable(vehicle_id), etListWritable);

		return map;
	}

	private void saveTimePeriodStat(ETListWritable _etListWritable, CanTimePeriodStat _timePeriodStat) {
		_timePeriodStat.calc();
		_etListWritable.add(_timePeriodStat);
	}

}
