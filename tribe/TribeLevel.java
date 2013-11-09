package tribe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class TribeLevel {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat format = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

	public static void getAllTribeLevel(Date dataTime) {
		BufferedReader br = null;
		List<String[]> allList = new ArrayList<String[]>();
		try {
			String fileUrl = ConfigUtil.OaLogPath + "trace.log."
					+ sdf.format(dataTime) + "*";
			if (fileUrl != null) {
				String[] cmdArray = new String[] {
						"/bin/sh",
						"-c",
						"grep action=createTribe, "
								+ fileUrl
								+ "|awk -F '[=,]' 'BEGIN{OFS=\",\"}{print $3,$7,\"1\",$9}'" };
				// 62637,1591,1,Mon Sep 02 01:02:05 SGT 2013
				// 创建者id，家族id，时间
				System.out
						.println("---------------------------startProcess:"
								+ "grep action=createTribe, "
								+ fileUrl
								+ "|awk -F '[=,]' 'BEGIN{OFS=\",\"}{print $3,$7,\"1\",$9}'");
				Process process = Runtime.getRuntime().exec(cmdArray);
				InputStream in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(
						in));
				String result = read.readLine();
				while (result != null) {
					String[] _temp = result.split(",");
					allList.add(_temp);
					result = read.readLine();
				}

				cmdArray = new String[] {
						"/bin/sh",
						"-c",
						"grep action=TribeUpgrade , "
								+ fileUrl
								+ "|awk -F '[=,]' 'BEGIN{OFS=\",\"}{print $3,$7,substr($9,0,length($9)-10),$10}'" };
				// 29139,465,2,Mon Sep 02 05:13:19 SGT 2013
				// 贡献者id,家族id，等级，时间
				System.out
						.println("---------------------------startProcess:"
								+ "grep action=TribeUpgrade, "
								+ fileUrl
								+ "|awk -F '[=,]' 'BEGIN{OFS=\",\"}{print $3,$7,substr($9,0,length($9)-10),$10}'");
				process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				read = new BufferedReader(new InputStreamReader(in));
				result = read.readLine();
				while (result != null) {
					String[] _temp = result.split(",");
					allList.add(_temp);
					result = read.readLine();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		insertDB(allList, dataTime);
		getTribeUpgradeTime(dataTime);
	}

	private static void insertDB(List<String[]> allList, Date dataTime) {
		String s1 = "INSERT INTO tribelevel(monetid,tribeid,tlevel,tdate) VALUES(?,?,?,?)";
		for (String[] s : allList) {
			System.out.println(s[0] + s[1] + s[2] + s[3]);
			try {
				int monetid = Integer.parseInt(s[0]);
				int tribeid = Integer.parseInt(s[1]);
				int level = Integer.parseInt(s[2]);
				Date date = format.parse(s[3]);
				ConfigUtil.myPortalDb.execSQLUpdate(s1, new Object[] { monetid,
						tribeid, level, date });
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		
		if (args.length > 0) {
			String dateStr = args[0];
			try {
				Date date = sdf.parse(dateStr);
				getAllTribeLevel(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	

	private static void getTribeUpgradeTime(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, +1);// 把日期往后增加一天.整数往后推,负数往前移动
		Date tDate = calendar.getTime();
		String s = "select max(tlevel) from tribelevel";
		DBResultSet ds = ConfigUtil.myPortalDb.execSQLQuery(s, new Object[] {});
		int maxlevel = 0;
		if (ds.next()) {
			try {
				maxlevel = ds.getInt(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String sql = "select  b5.tribeid,cast(datediff(ss,b1.tdate,b5.tdate) as float) dfs "
				+ "	from ( "
				+ "		select tribeid, tdate  "
				+ "			from tribeLevel  "
				+ "			where "
				+ "			tlevel = ? "
				+ "			and tdate between ? and ? "
				+ "		) b5 "
				+ "		join ( "
				+ "		select tribeid, tdate  "
				+ "			from tribeLevel  "
				+ "			where tlevel = ?  "
				+ "		) b1  "
				+ "		on b5.tribeid = b1.tribeid ";

		String insertSql = "insert into tribeleveltime(tribeid,time,tlevel,tdate) values(?,?,?,?)";
		for (int i = 1; i < maxlevel; i++) {
			try {
				ds = ConfigUtil.myPortalDb.execSQLQuery(sql, new Object[] {
						i + 1, date, tDate, i });
				while (ds.next()) {
					ConfigUtil.myPortalDb.execSQLUpdate(insertSql,
							new Object[] { ds.getInt(0), ds.getFloat(1), i + 1,
									date });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
