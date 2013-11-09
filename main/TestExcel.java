package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import both.basic.ConfigUtil;
import util.DBResultSet;
import util.MoDBRW;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;

public class TestExcel {
	static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	static DecimalFormat df = new DecimalFormat("#.00");
	static List<String> list = new ArrayList<String>();
	static int rowIndex = 0;

	static {
		list.add("billing_stc_user");
		list.add("billing_stc_amount");
		list.add("billing_fortumo_user");
		list.add("billing_fortumo_amount");
		list.add("billing_googleWallet_user");
		list.add("billing_googleWallet_amount");
		list.add("billing_Egypt_user");
		list.add("billing_Egypt_amount");
	}

	public static void writeExcel(File f,Date date) throws Exception {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -2);// 把日期往后增加一天.整数往后推,负数往前移动
		Date startTime = calendar.getTime();

		jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
		jxl.write.WritableSheet ws = wwb.createSheet("Sheet1", 0);

		setNewUser(ws, startTime);
		setUserPayment(ws, startTime);
		setKpi(ws, startTime);
		
		
		 
		// ws.addCell(labelC);
		// 写入Exel工作表
		wwb.write();
		// 关闭Excel工作薄对象
		wwb.close();
	}

	public static void readExcel(File os) throws Exception {
		Workbook wb = Workbook.getWorkbook(os);
		Sheet s = wb.getSheet("Sheet1");
		Cell c = s.getCell(0, 0);
		System.out.println(c.getContents());
	}

	//
	private static void setUserPayment(jxl.write.WritableSheet ws, Date endTime) {
		rowIndex+=2;
		try {
			ws.mergeCells(1, rowIndex, 2, rowIndex);
			ws.mergeCells(3, rowIndex, 4, rowIndex);
			ws.mergeCells(5, rowIndex, 6, rowIndex);
			ws.mergeCells(7, rowIndex, 8, rowIndex);
			ws.addCell(new jxl.write.Label(1, rowIndex, "Stc(SAR)",getContextFormat()));
			ws.addCell(new jxl.write.Label(3, rowIndex, "Fortumo(SAR)",getContextFormat()));
			ws.addCell(new jxl.write.Label(5, rowIndex, "Google wallet(USD)",getContextFormat()));
			ws.addCell(new jxl.write.Label(7, rowIndex, "Egypt(EGP)",getContextFormat()));
			rowIndex ++;
			ws.addCell(new jxl.write.Label(0, rowIndex, "日期",getContextFormat()));
			for (int i = 1; i <= list.size(); i += 2) {
				ws.addCell(new jxl.write.Label(i, rowIndex, "用户数",getContextFormat()));
				ws.addCell(new jxl.write.Label(i + 1, rowIndex, "金额",getContextFormat()));
			}
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(endTime);
			for (int j = 7; j < 98; j += 7) {
				calendar.add(Calendar.DATE, -7);
				Date startTime = calendar.getTime();
				Map<String, Integer> map3 = getUserPayment(endTime);
				rowIndex++;
				ws.addCell(new jxl.write.Label(0, rowIndex, sdf.format(startTime)
						+ "~" + sdf.format(endTime),getContextFormat()));
				endTime = startTime;
	
				for (int i = 0; i < list.size(); i++) {
					ws.addCell(new jxl.write.Number(i + 1, rowIndex, map3.get(list
							.get(i))));
				}
			}
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		
	}
	
	private static void setKpi(jxl.write.WritableSheet ws, Date endTime) {
		rowIndex+=2;
		try {
			ws.addCell(new jxl.write.Label(0, rowIndex, "沙特",getContextFormat()));
			ws.mergeCells(1, rowIndex, 7, rowIndex);
			ws.mergeCells(8, rowIndex, 14, rowIndex);
			ws.mergeCells(15, rowIndex, 21, rowIndex);
			
			ws.addCell(new jxl.write.Label(1, rowIndex, "DAU",getContextFormat()));
			ws.addCell(new jxl.write.Label(8, rowIndex, "新用户",getContextFormat()));
			ws.addCell(new jxl.write.Label(15, rowIndex, "日充值金额（SAR)",getContextFormat()));
			rowIndex ++ ;
			ws.addCell(new jxl.write.Label(0, rowIndex, "时间"));
			int fuck = 1 ;
			for (int i = 1; i <= 3; i ++) {
				for(int k = 1;k<=6;k++){
					ws.addCell(new jxl.write.Label(fuck++, rowIndex, "server"+k,getContextFormat()));
				}
				ws.addCell(new jxl.write.Label(fuck++, rowIndex, "总",getContextFormat()));
			}
			
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(endTime);
			for (int j = 7; j < 98; j += 7) {
				calendar.add(Calendar.DATE, -7);
				Date startTime = calendar.getTime();
				Map<String, Integer> map1 = getKpi(endTime,1);
				Map<String, Integer> map2 = getKpi(endTime,2);
				Map<String, Integer> map3 = getKpi(endTime,3);
				Map<String, Integer> map4 = getKpi(endTime,4);
				Map<String, Integer> map5 = getKpi(endTime,5);
				Map<String, Integer> map6 = getKpi(endTime,6);
				
				rowIndex++;
				ws.addCell(new jxl.write.Label(0, rowIndex, sdf.format(startTime)
						+ "~" + sdf.format(endTime),getContextFormat()));
				endTime = startTime;
				
				fuck = 1 ;
				int dau1 = map1.get("DAU")==null?0:map1.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau1));
				int dau2 = map2.get("DAU")==null?0:map2.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau2));
				int dau3 = map3.get("DAU")==null?0:map3.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau3));
				int dau4 = map4.get("DAU")==null?0:map4.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau4));
				int dau5 = map5.get("DAU")==null?0:map5.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau5));
				int dau6 = map6.get("DAU")==null?0:map6.get("DAU");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau6));
				int dau = dau1 + dau2 + dau3 + dau4 + dau5 +dau6 ;
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,dau));
				
				int newUserDB1 = map1.get("newUserDB")==null?0:map1.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB1));
				int newUserDB2 = map2.get("newUserDB")==null?0:map2.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB2));
				int newUserDB3 = map3.get("newUserDB")==null?0:map3.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB3));
				int newUserDB4 = map4.get("newUserDB")==null?0:map4.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB4));
				int newUserDB5 = map5.get("newUserDB")==null?0:map5.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB5));
				int newUserDB6 = map6.get("newUserDB")==null?0:map6.get("newUserDB");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB6));
				int newUserDB =   newUserDB1 + newUserDB2+ newUserDB3 +newUserDB4  + newUserDB5 +  newUserDB6; 
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,newUserDB));
				
				int topUpAmount1 = map1.get("TopUpAmount")==null?0:map1.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount1));
				int topUpAmount2 = map2.get("TopUpAmount")==null?0:map2.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount2));
				int topUpAmount3 = map3.get("TopUpAmount")==null?0:map3.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount3));
				int topUpAmount4 = map4.get("TopUpAmount")==null?0:map4.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount4));
				int topUpAmount5 = map5.get("TopUpAmount")==null?0:map5.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount5));
				int topUpAmount6 = map6.get("TopUpAmount")==null?0:map6.get("TopUpAmount");
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount6));
				int topUpAmount = topUpAmount1 + topUpAmount2 +topUpAmount3 +topUpAmount4+ topUpAmount5 +topUpAmount6;
				ws.addCell(new jxl.write.Number(fuck++,rowIndex,topUpAmount));
			}
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
	}

	private static void setNewUser(jxl.write.WritableSheet ws, Date endTime) {
		rowIndex++;
		jxl.write.Label label1 = new jxl.write.Label(0, rowIndex, "新 用 户 情 况 ",getContextFormat());
		jxl.write.Label label2 = new jxl.write.Label(1, rowIndex, "沙特进入下载站点的人数",getContextFormat());
		jxl.write.Label label3 = new jxl.write.Label(2, rowIndex, "埃及进入下载站点的人数",getContextFormat());
		jxl.write.Label label4 = new jxl.write.Label(3, rowIndex, "注册用户数",getContextFormat());
		jxl.write.Label label5 = new jxl.write.Label(4, rowIndex, "登陆用户数",getContextFormat());
		jxl.write.Label label6 = new jxl.write.Label(5, rowIndex, "新Fisher",getContextFormat());
		jxl.write.Label label7 = new jxl.write.Label(6, rowIndex, "登陆/注册",getContextFormat());
		jxl.write.Label label8 = new jxl.write.Label(7, rowIndex, "新Fisher/登陆",getContextFormat());
		jxl.write.Label label9 = new jxl.write.Label(8, rowIndex, "总转化率",getContextFormat());

		try {
			ws.addCell(label1);
			ws.addCell(label2);
			ws.addCell(label3);
			ws.addCell(label4);
			ws.addCell(label5);
			ws.addCell(label6);
			ws.addCell(label7);
			ws.addCell(label8);
			ws.addCell(label9);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endTime);
		for (int i = 7; i < 98; i += 7) {
			calendar.add(Calendar.DATE, -7);
			Date startTime = calendar.getTime();

			Map<String, Integer> map = getWebsiteDate(endTime);
			Map<String, Integer> map2 = getRegUserConvert(endTime);
			rowIndex++;
			jxl.write.Label l1 = new jxl.write.Label(0, rowIndex,
					sdf.format(startTime) + "~" + sdf.format(endTime),getContextFormat());
			
			endTime = startTime;
			
			int all_UV = map.get("all_UV")==null ? 0 : map.get("all_UV");
			int buzzall_UV = map.get("buzzall_UV")==null ? 0 : map.get("buzzall_UV");
			int all_reg = map2.get("all_reg")==null ? 0 : map2.get("all_reg");
			int all_login = map2.get("all_login")==null ? 0 : map2.get("all_login");
			int all_db = map2.get("all_db")==null ? 0 : map2.get("all_db");
			
			jxl.write.Number n1 = new Number(1, rowIndex, all_UV);
			jxl.write.Number n2 = new Number(2, rowIndex, buzzall_UV);
			jxl.write.Number n3 = new Number(3, rowIndex, all_reg);
			jxl.write.Number n4 = new Number(4, rowIndex, all_login);
			jxl.write.Number n5 = new Number(5, rowIndex, all_db);
			
			jxl.write.Label n6 = new Label(6, rowIndex, df.format(all_login * 100.0 / all_reg)+ "%");
			jxl.write.Label n7 = new Label(7, rowIndex, df.format(all_db * 100.0 / all_login)+ "%");
			jxl.write.Label n8 = new Label(8, rowIndex, df.format(all_db * 100.0/ (all_UV + buzzall_UV)) + "%");

			try {
				ws.addCell(l1);
				ws.addCell(n1);
				ws.addCell(n2);
				ws.addCell(n3);
				ws.addCell(n4);
				ws.addCell(n5);
				ws.addCell(n6);
				ws.addCell(n7);
				ws.addCell(n8);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}

	// 最好写一个这样的main方法来测试一下你的这个class是否写好了。
	public static void main(String[] args) throws Exception {
		
		File f = new File("c:\\每周周报数据.11-04.xls");
		f.createNewFile();
		writeExcel(f,new Date());
		// readExcel(f);w
	}

	private static Map<String, Integer> getWebsiteDate(Date endTime) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, -7);// 把日期往后增加一天.整数往后推,负数往前移动
		Date startTime = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		
		
		String sql = "select oakey,sum(oavalue)/7  from WebSiteData where ddate >= ? and ddate < ? and (oakey = 'all_UV' or oakey = 'buzzall_UV') group by oakey";
		DBResultSet ds = ConfigUtil.myPortalDb.execSQL(sql, new Object[] {
				sdf1.format(startTime), sdf1.format(endTime) });
		while (ds.next()) {
			try {
				map.put(ds.getString(0), ds.getInt(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private static Map<String, Integer> getRegUserConvert(Date endTime) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, -7);// 把日期往后增加一天.整数往后推,负数往前移动
		Date startTime = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		String sql = "select oakey,sum(oavalue)/7  from regUserConvert where rdate >= ? and rdate < ? and oakey like 'all_%'  group by oakey";
		DBResultSet ds = ConfigUtil.myPortalDb.execSQL(sql, new Object[] {
				sdf1.format(startTime), sdf1.format(endTime) });
		while (ds.next()) {
			try {
				map.put(ds.getString(0), ds.getInt(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private static Map<String, Integer> getUserPayment(Date endTime) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, -7);// 把日期往后增加一天.整数往后推,负数往前移动
		Date startTime = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		String sql = "select oakey,sum(oavalue)/7  from userPayment where pdate >= ? and pdate < ? and oakey like 'billing_%'   group by oakey";
		DBResultSet ds = ConfigUtil.myPortalDb.execSQL(sql, new Object[] {
				sdf1.format(startTime), sdf1.format(endTime) });
		while (ds.next()) {
			try {
				map.put(ds.getString(0), ds.getInt(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private static Map<String, Integer> getKpi(Date endTime, int server) {
		MoDBRW Db = null;
		switch (server) {
		case 1:
			Db = ConfigUtil.portal1db;
			break;
		case 2:
			Db = ConfigUtil.portal2db;
			break;
		case 3:
			Db = ConfigUtil.portal3db;
			break;
		case 4:
			Db = ConfigUtil.portal4db;
			break;
		case 5:
			Db = ConfigUtil.portal5db;
			break;
		case 6:
			Db = ConfigUtil.portal6db;
			break;
		default:
			Db = ConfigUtil.myPortalDb;
			break;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, -7);// 把日期往后增加一天.整数往后推,负数往前移动
		Date startTime = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		String sql = "select oakey,sum(oavalue)/7  from kpi where sdate >= ? and sdate < ? and (oakey = 'TopUpAmount' or oakey = 'DAU' or oakey = 'newUserDB' )   group by oakey";
		DBResultSet ds = Db.execSQL(sql, new Object[] { sdf1.format(startTime), sdf1.format(endTime) });
		while (ds.next()) {
			try {
				map.put(ds.getString(0), ds.getInt(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	private static WritableCellFormat getContextFormat(){
	    WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 10); 
	    WritableCellFormat wcfFTitle = new WritableCellFormat(wfTitle);
	    try{  
	    	wcfFTitle.setBackground(Colour.WHITE); 
	    	wcfFTitle.setBorder(Border.ALL, BorderLineStyle.THIN); 
	    	wcfFTitle.setAlignment(Alignment.CENTRE ); //这个难道不是居中？
	    	//wcfFTitle.setWrap(true); 
	    }catch(Exception e){
	    }
	    return wcfFTitle;
	} 
}