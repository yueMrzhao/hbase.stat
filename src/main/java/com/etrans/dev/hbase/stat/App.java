package com.etrans.dev.hbase.stat;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.e_tran.data.hadoop.mapreduce.hbase.job.impl.InitHbaseMapperSupperManyTable2;
import com.e_tran.data.hadoop.mapreduce.hbase.job.manytable.et.ET2TableInputFormat;
import com.e_tran.data.hadoop.mapreduce.hbase.job.manytable.et.ET2TableRecordReaderImpl;
import com.e_tran.data.hadoop.mapreduce.hbase.job.manytable.et.ETROWRang;
import com.e_tran.data.hadoop.mapreduce.hbase.job.util.ScanUtil;
import com.e_trans.data.hadoop.mapreduce.output.ETDBAutoOutputFormat;
import com.e_trans.hadoop.mapreduce.cotroller.interfaces.mr.IMapperInit;
import com.e_trans.hadoop.mapreduce.cotroller.interfaces.output.IETOutputFormat;
import com.e_trans.hadoop.mapreduce.cotroller.job.EtJob;
import com.e_trans.hadoop.mapreduce.cotroller.job.RUNTYPE;
import com.et.develop.common.datasource.hbase.HbaseDataSourceUtil;
import com.etrans.dev.hbase.stat.map.can.CanMapper;

/**
 * Hello world!
 *
 */
public class App {
	static String protoPath;
	// 这个是配置log 的
	static {
		Logger.getRootLogger().setLevel(Level.INFO);
		ConsoleAppender appender = new ConsoleAppender();
		appender.setWriter(new PrintWriter(System.out));
		appender.setLayout(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN));
		appender.setName("stdout");
		Logger.getRootLogger().addAppender(appender);
	}

	static Logger logger = Logger.getLogger(App.class);

	//
	public static void main2(String[] args) {

		// byte[] bs = Bytes.add(Bytes.toBytes(IntUtil.intRevert(1)),
		// Bytes.toBytes(HbaseDataSourceUtil.getETBaseTime("20170120000000")));
		// System.out.println(bs.length);
		// // Bytes.copy(bs, 0, 4);
		// System.out.println(IntUtil.intRevert(Bytes.toInt(Bytes.copy(bs, 0,
		// 4))));
		// System.out.println(Bytes.toInt(Bytes.copy(bs, 0, 4)));

	}

	public static void main(String[] args) {
		// System.out.println(System.getProperty("java.class.path"));
		// System.out.println(System.getProperty("user.dir"));

		// 这个是找出 hadoop map-reduce调用的4 个配置文件

		// local 执行的时候需要
		File file = new File(System.getProperty("user.dir") + "/src/resource");
		if (!file.isDirectory()) {
			System.out.println("it must is dir");
		}
		File[] files = file.listFiles();
		String[] paths = new String[files.length];
		int i = 0;
		for (File file2 : files) {
			paths[i++] = file2.getAbsolutePath();
		}

		// RUNTYPE 支持三种类型 ，分别是 本地 ，远程 ，平台（平台还在测试中）
		EtJob etJob = new EtJob("hbaseStat", paths, RUNTYPE.REMOTE) {

			@Override
			public IMapperInit regMapperInit() {

				return new InitHbaseMapperSupperManyTable2() {

					@Override
					public Map<String, ETROWRang> tableAndScan() {
						Map<String, ETROWRang> map = new HashMap<>();
						map.put("can", new ETROWRang(0, 10000000));
						return map;
					}

					@Override
					public Date workDate() {
						return null;
					}

					@Override
					public String zookeeperPort() {
						return "2181";
					}

					@Override
					public String zookeeperQuorum() {
						return "10.10.1.101,10.10.1.103";
					}

					// public Scan canHbaseScan() {
					// String regex = ".{1}20161101$";
					// Filter rowFilter = new RowFilter(CompareOp.EQUAL, new
					// RegexStringComparator(regex));
					// Scan scan = new Scan();
					// scan.setMaxVersions();
					// scan.setCaching(20);
					// scan.addColumn(Bytes.toBytes("col"),
					// Bytes.toBytes("f2"));
					// scan.setCacheBlocks(false);
					// scan.setFilter(rowFilter);

					// return ScanUtil.getETOneDayScan("20170117000000", 4);

					// }

					@SuppressWarnings("rawtypes")
					@Override
					public TableMapper regHbaseMapper() {
						return new CanMapper();
					}

				};
			}

			@SuppressWarnings("rawtypes")
			@Override
			public IETOutputFormat[] regOutputFormats() {
				return new IETOutputFormat[] { new ETDBAutoOutputFormat("com.mysql.jdbc.Driver",
						"jdbc:mysql://10.10.1.155:3314/cardb?useUnicode=true&characterEncoding=utf-8", "root",
						"ycadmin_1001") };
			}
		};
		// hbase.mapper.max.item
		etJob.setRunParameter(ET2TableInputFormat.INPUT_TABLE_BASETIME,
				HbaseDataSourceUtil.getETBaseTime("20170117000000") + "");
		// 要 能本地启动 就需要先编译一个jar ,然后在这里填写这个jar 包 ，而 在linux 下启动是不需要的
		etJob.setRunParameter("hbase.mapper.max.item", String.valueOf(50000));
		etJob.setRunParameter("hbase.read.batch.size", args[0]);
		// 远程执行的时候 将就可以 注释掉
		// etJob.setJar(jarPath);
		// etJob.setJar(System.getProperty("user.dir") +
		// "\\target\\hbase.stat-0.0.1-SNAPSHOT.jar");
		// etJob.setJar("hdfs://master:9000/hbase.stat-0.0.1-SNAPSHOT.jar");
		//
		// System.out.println("hdfs://master:9000/hbase.stat-0.0.1-SNAPSHOT.jar");
		etJob.setJarByClass(App.class);
		etJob.setThirdPartyJar("hbase.custom.comparator-1.0.0.jar");

		// 这里设置 hadoop 的用户名，就可以解决权限问题了
		etJob.setHadoopUser("hadoop1");
		etJob.start();
	}
}