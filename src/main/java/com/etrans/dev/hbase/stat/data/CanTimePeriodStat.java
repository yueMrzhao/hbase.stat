package com.etrans.dev.hbase.stat.data;

import java.util.Date;

import com.e_trans.hadoop.mapreduce.cotroller.annotation.OutPut;
import com.e_trans.hadoop.mapreduce.cotroller.annotation.OutPutType;
import com.e_trans.hadoop.mapreduce.cotroller.annotation.db.Column;
import com.e_trans.hadoop.mapreduce.cotroller.annotation.db.Table;
import com.e_trans.hadoop.mapreduce.cotroller.bean.ETWrite;
import com.etrans.dev.hbase.stat.Utils;

@OutPut(type = OutPutType.DB)
@Table(tableName="vehicle_time_period_stat", dbName = "cardb")
public class CanTimePeriodStat extends ETWrite {
//	static Logger logger = org.slf4j.LoggerFactory.getLogger(TimePeriodStat.class);
	/**
	 * 车辆
	 */
	@Column
	private int vehicle_id;
	/**
	 * 分析日期
	 */
	@Column
	private Date analyse_date;
	/**
	 * 行驶里程(0.1km)
	 */
	@Column
	private int mileage = 0;
	/**
	 * 总油耗(0.1L)
	 */
	@Column
	private double total_oil_consumption = 0;
	/**
	 * 平均油耗(0.01L)
	 */
	@Column
	private double avg_oil_consumption = 0;
	/**
	 * 发动机最高转速(rpm)
	 */
	@Column
	private int max_revs = 0;
	/**
	 * 发动机最低转速(rpm)
	 */
	@Column
	private int min_revs = 0;
	/**
	 * 发动机平均转速(rpm)
	 */
	@Column
	private int avg_revs = 0;
	/**
	 * 最高速度(实际速度需要除以10)
	 */
	@Column
	private int max_speed = 0;
	/**
	 * 最低速度(实际速度需要除以10)
	 */
	@Column
	private int min_speed = 0;
	/**
	 * 平均速度(实际速度需要除以10)
	 */
	@Column
	private int avg_speed = 0;
	/**
	 * 开始时间
	 */
	@Column
	private Date start_time;
	/**
	 * 结束时间
	 */
	@Column
	private Date end_time;
	/**
	 * 持续时长(秒)
	 */
	@Column
	private int persist_time;
	/**
	 * 开始经度/100000
	 */
	@Column
	private int start_lon = 0;
	/**
	 * 开始纬度/100000
	 */
	@Column
	private int start_lat = 0;
	/**
	 * 结束经度/100000
	 */
	@Column
	private int end_lon = 0;
	/**
	 * 结束纬度/100000
	 */
	@Column
	private int end_lat = 0;
	/**
	 * 时间周期类型(0-停车, 1-开车)
	 */
	@Column
	private int time_period_type;
	/**
	 * 开始里程
	 */
	private int start_mileage = 0;
	/**
	 * 结束里程
	 */
	private int end_mileage = 0;
	/**
	 * 记录条数
	 */
	private int recordCount = 0;

	public CanTimePeriodStat(int _time_period_type, int _vehicle_id, Date _startTime, int _revs, int _mileage, int _canSpeed, double _oil) {
		this.time_period_type = _time_period_type;
		this.vehicle_id = _vehicle_id;
		this.analyse_date = Utils.getDate(_startTime);
		this.start_time = _startTime;
		this.end_time = _startTime;
		this.max_revs = _revs;
		this.min_revs = _revs;
		this.avg_revs = _revs;
		this.start_mileage = _mileage;
		this.end_mileage = _mileage;
		this.max_speed = _canSpeed;
		this.min_speed = _canSpeed;
		this.avg_revs = _canSpeed;
		if ((!Double.isNaN(_oil)) && (!Double.isInfinite(_oil)))
			this.total_oil_consumption = _oil;
		this.recordCount = 1;
		
	}

	public void calc() {
		try {
			// 持续时长(秒)
			this.persist_time = (int) ((this.end_time.getTime() - this.start_time.getTime()) / 1000);
			// 行驶里程
			this.mileage = this.end_mileage - this.start_mileage;
			// 发动机平均转速
			this.avg_revs = this.avg_revs / this.recordCount;
			// 平均速度
			this.avg_speed = this.avg_speed / this.recordCount;
		} catch (Exception e) {
//			logger.error("TimePeriodStat error - calc: {}", e);
			e.printStackTrace();
		}
	}

	public int getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(int vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public Date getAnalyse_date() {
		return analyse_date;
	}

	public void setAnalyse_date(Date analyse_date) {
		this.analyse_date = analyse_date;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public double getTotal_oil_consumption() {
		return total_oil_consumption;
	}

	public void setTotal_oil_consumption(double total_oil_consumption) {
		if ((Double.isNaN(total_oil_consumption)) || (Double.isInfinite(total_oil_consumption)))
			this.total_oil_consumption = 0;
		else
			this.total_oil_consumption = total_oil_consumption;
	}

	public double getAvg_oil_consumption() {
		return avg_oil_consumption;
	}

	public void setAvg_oil_consumption(double avg_oil_consumption) {
		this.avg_oil_consumption = avg_oil_consumption;
	}

	public int getMax_revs() {
		return max_revs;
	}

	public void setMax_revs(int max_revs) {
		this.max_revs = max_revs;
	}

	public int getMin_revs() {
		return min_revs;
	}

	public void setMin_revs(int min_revs) {
		this.min_revs = min_revs;
	}

	public int getAvg_revs() {
		return avg_revs;
	}

	public void setAvg_revs(int avg_revs) {
		this.avg_revs = avg_revs;
	}

	public int getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(int max_speed) {
		this.max_speed = max_speed;
	}

	public int getMin_speed() {
		return min_speed;
	}

	public void setMin_speed(int min_speed) {
		this.min_speed = min_speed;
	}

	public int getAvg_speed() {
		return avg_speed;
	}

	public void setAvg_speed(int avg_speed) {
		this.avg_speed = avg_speed;
	}

	public Date getStart_time() {
		return start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public int getPersist_time() {
		return persist_time;
	}

	public int getStart_lon() {
		return start_lon;
	}

	public void setStart_lon(int start_lon) {
		this.start_lon = start_lon;
	}

	public int getStart_lat() {
		return start_lat;
	}

	public void setStart_lat(int start_lat) {
		this.start_lat = start_lat;
	}

	public int getEnd_lon() {
		return end_lon;
	}

	public void setEnd_lon(int end_lon) {
		this.end_lon = end_lon;
	}

	public int getEnd_lat() {
		return end_lat;
	}

	public void setEnd_lat(int end_lat) {
		this.end_lat = end_lat;
	}

	public int getTime_period_type() {
		return time_period_type;
	}

	public void setEnd_mileage(int end_mileage) {
		this.end_mileage = end_mileage;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
}
