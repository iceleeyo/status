package main;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MyUtil;
import both.basic.ConfigUtil;

public class Mail {
	static Logger logger = Logger.getLogger(Main.class);
	
	
	static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
	static DecimalFormat df =  new DecimalFormat("####.##");
	
	public static void main(String[] args) {
		
		Calendar c = Calendar.getInstance();
		c.add( Calendar.DATE,-1);
		Date dataTime = c.getTime();
		int dau = 0,mau= 0,hau= 0,pau= 0,withdraw = 0,cau=0,clau=0;
		
		if(args.length == 7){
			dau = new Integer(args[0]);
			mau = new Integer(args[1]);
			hau = new Integer(args[2]);
			pau = new Integer(args[3]);
			withdraw = new Integer(args[4]);
			cau = new Integer(args[5]);
			clau = new Integer(args[6]);
		}else if(args.length == 8){
			dau = new Integer(args[0]);
			mau = new Integer(args[1]);
			hau = new Integer(args[2]);
			pau = new Integer(args[3]);
			withdraw = new Integer(args[4]);
			cau = new Integer(args[5]);
			clau = new Integer(args[6]);
			dataTime = MyUtil.StringToDate(args[7]);
		}else{
			logger.error("input error"+new Date());
		}
		
		Map<Integer, String> newUserMap  = new HashMap<Integer, String>();
		Map<Integer, String> topUpUserMap  = new HashMap<Integer, String>();
		Map<Integer, String> mTopUpUserMap  = new HashMap<Integer, String>();
		int topup= 0;
		int mTopUp=0;
		//int MACU=0;
		//int DACU=0;
		try {
			
			//----- 新用户
			DBResultSet rs =  ConfigUtil.server1db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server2db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			
			rs =  ConfigUtil.server3db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			
			rs =  ConfigUtil.server4db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server5db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			
			rs =  ConfigUtil.server6db.execSQLQuery("select distinct monetid from fisher where convert(varchar(10),newuserflag,20) = ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			
			
			//---- 充值
			rs =  ConfigUtil.server1db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server2db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server3db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server4db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			rs =  ConfigUtil.server5db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server6db.execSQLQuery("select sum(amount)/100 money from callbackstc where convert(varchar(10),date,20) = ?",new Object[]{sdf.format(dataTime)});
			if(rs.next()){
				topup += rs.getInt("money");
			}
			
			//---- 充值用户
			
			rs =  ConfigUtil.server1db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server2db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server3db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			
			rs =  ConfigUtil.server4db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server5db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server6db.execSQLQuery("select distinct monet_id monetid from callbackstc where convert(varchar(10),date,20) =  ?",new Object[]{sdf.format(dataTime)});
			while(rs.next()){
				topUpUserMap.put(rs.getInt("monetid"), "");
			}
			
			
			
			//---- 月充值金额
			Calendar c2 = Calendar.getInstance();
			c2.setTime(dataTime);
			c2.add(Calendar.DATE, -30);
			
			rs =  ConfigUtil.server1db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server2db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			
			rs =  ConfigUtil.server3db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			rs =  ConfigUtil.server4db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			rs =  ConfigUtil.server5db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			rs =  ConfigUtil.server6db.execSQLQuery("select sum(amount)/100 money from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				mTopUp += rs.getInt("money");
			}
			
			//---- 月充值人数
			
			rs =  ConfigUtil.server1db.execSQLQuery("select distinct monet_id monetid from callbackstc where date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server2db.execSQLQuery("select distinct monet_id monetid from callbackstc where  date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server3db.execSQLQuery("select distinct monet_id monetid from callbackstc where  date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server4db.execSQLQuery("select distinct monet_id monetid from callbackstc where  date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server5db.execSQLQuery("select distinct monet_id monetid from callbackstc where  date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			rs =  ConfigUtil.server6db.execSQLQuery("select distinct monet_id monetid from callbackstc where  date >= ? and date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			while(rs.next()){
				mTopUpUserMap.put(rs.getInt("monetid"), "");
			}
			
			
			/*//---- 月活跃人数
			
			rs =  server1db.execSQLQuery("select  count(distinct monetid) acuser from user_event where server_date >= ? and server_date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				MACU += rs.getInt("acuser");
			}
			rs =  server2db.execSQLQuery("select  count(distinct monetid) acuser from user_event where server_date >= ? and server_date < ?",new Object[]{sdf.format(c2.getTime()),sdf.format(Calendar.getInstance().getTime())});
			if(rs.next()){
				MACU += rs.getInt("acuser");
			}
			System.out.println(sdf.format(c2.getTime()));
			//---- 日活跃人数
			
			rs =  server1db.execSQLQuery("select  count(distinct monetid) acuser from user_event where convert(varchar(10),server_date,20) = ?",new Object[]{sdf.format(c2.getTime())});
			if(rs.next()){
				DACU += rs.getInt("acuser");
			}
			rs =  server2db.execSQLQuery("select  count(distinct monetid) acuser from user_event where convert(varchar(10),server_date,20) = ?",new Object[]{sdf.format(c2.getTime())});
			if(rs.next()){
				DACU += rs.getInt("acuser");
			}*/
			
			
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"DAU",dau,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"MAU",mau,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"PAU",pau,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"HAU",hau,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"NAU",newUserMap.size(),sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"TAU",topUpUserMap.size(),sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"WithDraw",withdraw,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"TopUp",topup,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"MTopUp",mTopUp,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"MTAU",mTopUpUserMap.size(),sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"ARPPU",df.format((double)mTopUp/mTopUpUserMap.size()),sdf.format(dataTime)});			
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"ARPU",df.format((double)mTopUp/mau),sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"CAU",cau,sdf.format(dataTime)});
			ConfigUtil.portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"CLAU",clau,sdf.format(dataTime)});
			
			
			//portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"MACU",MACU,sdf.format(dataTime)});
			//portal1db.execSQLUpdate("insert into mailData(oakey,oavalue,mdate) values (?,?,?)", new Object[]{"DACU",DACU,sdf.format(dataTime)});
			
			sendMail(dataTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void sendMail(Date dataTime) throws Exception {
		DBResultSet rs;
		String style="<HEAD><STYLE TYPE='TEXT/CSS'>div.table {float:left;position:relative;width:614px;margin:0 300px 37px 0;}table.listing {text-align: center;font-family: Arial;font-weight: normal;font-size: 11px;width: 900px;background-color:#fafafa;border: 1px #AE0000 solid;border-collapse: collapse;border-spacing: 0px;}table.listing td,table.listing th{border: 1px #AE0000 solid;padding:3px;}</STYLE></HEAD>";
		
		String tableHeadAll="<h1>OA PRO Daily Report<h1><table></table><table><div class='table'><table class='listing' cellpadding='0' cellspacing='0'>";
		String tableEndAll="</table></div><br><br>";
		String thAll = "<tr class='first' width='177'><th nowrap='nowrap'>日期</th><th nowrap='nowrap'>DAU</th><th nowrap='nowrap'>新用户数</th><th nowrap='nowrap'>MAU</th><th nowrap='nowrap'>历史用户</th><th nowrap='nowrap'>销售(蓝宝石)</th><th nowrap='nowrap'>付费人数</th><th nowrap='nowrap'>日充值人数</th><th nowrap='nowrap'>月充值人数</th><th nowrap='nowrap'>日充值金额</th><th nowrap='nowrap'>月充值金额</th><th nowrap='nowrap'>ARPU</th><th nowrap='nowrap'>ARPPU</th>";
		String tdAll = "<tr class='bg'><td>@dateTime</td><td><font color='@fcolor1'>@dau</font></td><td><font color='@fcolor2'>@nau</font></td><td>@mau</td><td>@hau</td><td><font color='@fcolor3'>@withdraw</font></td><td>@pau</td><td>@tau</td><td>@mtau</td><td><font color='@fcolor4'>@topup</font></td><td>@mtopup</td><td>@arpu</td><td>@arppu</td></tr>";
			
		
		String tableHead="<h1>@PROTAL Daily Report<h1><table></table><table><div class='table'><table class='listing' cellpadding='0' cellspacing='0'>";
		String tableEnd="</table></div><br><br>";
		String th = "<tr class='first' width='177'><th nowrap='nowrap'>日期</th><th nowrap='nowrap'>DAU</th><th nowrap='nowrap'>登陆成功用户</th><th nowrap='nowrap'>新用户数</th><th nowrap='nowrap'>MAU</th><th nowrap='nowrap'>历史用户</th><th nowrap='nowrap'>销售(蓝宝石)</th><th nowrap='nowrap'>付费人数</th><th nowrap='nowrap'>日充值人数</th><th nowrap='nowrap'>月充值人数</th><th nowrap='nowrap'>日充值金额</th><th nowrap='nowrap'>月充值金额</th><th nowrap='nowrap'>ARPU</th><th nowrap='nowrap'>ARPPU</th><th nowrap='nowrap'>平均在线人数</th><th nowrap='nowrap'>最高在线人数</th><th nowrap='nowrap'>用户在线时长</th><th nowrap='nowrap'>渗透率</th></tr>";
		String td = "<tr class='bg'><td>@dateTime</td><td><font color='@fcolor1'>@dau</font></td><td>@rdau</td><td><font color='@fcolor2'>@nau</font></td><td>@mau</td><td>@hau</td><td><font color='@fcolor3'>@withdraw</font></td><td>@pau</td><td>@tau</td><td>@mtau</td><td><font color='@fcolor4'>@topup</font></td><td>@mtopup</td><td>@arpu</td><td>@arppu</td><td>@aoau</td><td>@toau</td><td>@uot</td><td class='last'>@pmb</td></tr>";

		
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataTime);
		String dateStr[] = {"","","","",""};
		dateStr[0]=sdf.format(c1.getTime());
		for(int i = 1;i<5;i++){
			c1.add( Calendar.DATE,-1);
			dateStr[i]=sdf.format(c1.getTime());
		}
		
		Map<String,Object> mailDataMap = new HashMap<String,Object>();
		
		//---------- get portal1
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal1db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER1_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER1_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER1_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER1_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER1_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER1_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER1_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER1_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER1_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER1_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER1_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER1_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER1_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER1_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER1_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER1_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER1_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		
		//---------- get portal2
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal2db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER2_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER2_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER2_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER2_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER2_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER2_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER2_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER2_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER2_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER2_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER2_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER2_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER2_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER2_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER2_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER2_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER2_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		
		
		//---------- get portal3
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal3db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER3_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER3_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER3_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER3_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER3_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER3_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER3_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER3_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER3_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER3_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER3_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER3_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER3_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER3_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER3_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER3_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER3_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		
		//---------- get portal4
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal4db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER4_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER4_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER4_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER4_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER4_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER4_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER4_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER4_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER4_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER4_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER4_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER4_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER4_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER4_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER4_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER4_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER4_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		//---------- get portal5
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal5db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER5_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER5_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER5_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER5_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER5_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER5_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER5_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER5_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER5_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER5_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER5_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER5_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER5_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER5_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER5_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER5_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER5_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		

		//---------- get portal6
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal6db.execSQL("Select * from kpi where sdate = ?", new Object[]{dateStr[i]});
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("SERVER6_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("newUserDB".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("SERVER6_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("SERVER6_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("SERVER6_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("SERVER6_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("FisherAmount".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("SERVER6_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("SERVER6_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("SERVER6_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("DailySelling".equalsIgnoreCase(rs.getString("oakey"))){//销售(蓝宝石)
					mailDataMap.put("SERVER6_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PaymentUser".equalsIgnoreCase(rs.getString("oakey"))){//购买人数
					mailDataMap.put("SERVER6_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpUsers30".equalsIgnoreCase(rs.getString("oakey"))){//月充值人数
					mailDataMap.put("SERVER6_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUpAmount30".equalsIgnoreCase(rs.getString("oakey"))){//月付费金额
					mailDataMap.put("SERVER6_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("avrgUsers".equalsIgnoreCase(rs.getString("oakey"))){//平均在线人数
					mailDataMap.put("SERVER6_AOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("maxUsers".equalsIgnoreCase(rs.getString("oakey"))){//最高在线人数
					mailDataMap.put("SERVER6_TOAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("userTime".equalsIgnoreCase(rs.getString("oakey"))){//用户在线时长
					mailDataMap.put("SERVER6_UOT_"+dateStr[i], rs.getInt("oavalue"));
				}else if("Permeability".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER6_PMB_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("RDAU".equalsIgnoreCase(rs.getString("oakey"))){//渗透率
					mailDataMap.put("SERVER6_RDAU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
			
		}
		
		for(int i = 0;i<dateStr.length;i++){
			rs = ConfigUtil.portal1db.execSQL("Select * from mailData where mdate = ?", new Object[]{dateStr[i]});	
			while (rs.next()){
				if("DAU".equalsIgnoreCase(rs.getString("oakey"))){//dau
					mailDataMap.put("ALL_DAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MAU".equalsIgnoreCase(rs.getString("oakey"))){//新用户
					mailDataMap.put("ALL_MAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("PAU".equalsIgnoreCase(rs.getString("oakey"))){//日充值人数
					mailDataMap.put("ALL_PAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("HAU".equalsIgnoreCase(rs.getString("oakey"))){//日充值金额
					mailDataMap.put("ALL_HAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("NAU".equalsIgnoreCase(rs.getString("oakey"))){//mau
					mailDataMap.put("ALL_NAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TAU".equalsIgnoreCase(rs.getString("oakey"))){//历史用户
					mailDataMap.put("ALL_TAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("WithDraw".equalsIgnoreCase(rs.getString("oakey"))){//ARPU
					mailDataMap.put("ALL_WITHDRAW_"+dateStr[i], rs.getInt("oavalue"));
				}else if("TopUp".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("ALL_TOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MTopUp".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("ALL_MTOPUP_"+dateStr[i], rs.getInt("oavalue"));
				}else if("MTAU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("ALL_MTAU_"+dateStr[i], rs.getInt("oavalue"));
				}else if("ARPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("ALL_ARPU_"+dateStr[i], rs.getDouble("oavalue"));
				}else if("ARPPU".equalsIgnoreCase(rs.getString("oakey"))){//ARPPU
					mailDataMap.put("ALL_ARPPU_"+dateStr[i], rs.getDouble("oavalue"));
				}
			}
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(style);
		
		//---------- show All
		sb.append(tableHeadAll);
		sb.append(thAll);
			for(int i = 0;i<dateStr.length-1;i++){
				String temp = String.valueOf(tdAll);
				temp = temp.replace("@dateTime", dateStr[i]);
				if(mailDataMap.get("ALL_DAU_"+dateStr[i])!=null&&mailDataMap.get("ALL_DAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("ALL_DAU_"+dateStr[i])-(Integer)mailDataMap.get("ALL_DAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor1","#FF0000");
					}else{
						temp = temp.replace("@fcolor1","#00FF00");
					}
				}
				temp = temp.replace("@dau", mailDataMap.get("ALL_DAU_"+dateStr[i])+"");
				if(mailDataMap.get("ALL_NAU_"+dateStr[i])!=null&&mailDataMap.get("ALL_NAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("ALL_NAU_"+dateStr[i])-(Integer)mailDataMap.get("ALL_NAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor2","#FF0000");
					}else{
						temp = temp.replace("@fcolor2","#00FF00");
					}
				}
				temp =temp.replace("@nau", mailDataMap.get("ALL_NAU_"+dateStr[i])+"");
				temp =temp.replace("@tau", mailDataMap.get("ALL_TAU_"+dateStr[i])+"");
				temp =temp.replace("@mau", mailDataMap.get("ALL_MAU_"+dateStr[i])+"");
				temp =temp.replace("@hau", mailDataMap.get("ALL_HAU_"+dateStr[i])+"");
				temp =temp.replace("@pau", mailDataMap.get("ALL_PAU_"+dateStr[i])+"");
				temp =temp.replace("@mtau", mailDataMap.get("ALL_MTAU_"+dateStr[i])+"");
				temp =temp.replace("@mtopup", mailDataMap.get("ALL_MTOPUP_"+dateStr[i])+"");
				temp =temp.replace("@arpu", mailDataMap.get("ALL_ARPU_"+dateStr[i])+"");
				temp =temp.replace("@arppu", mailDataMap.get("ALL_ARPPU_"+dateStr[i])+"");
				
				
				if(mailDataMap.get("ALL_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("ALL_WITHDRAW_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("ALL_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("ALL_WITHDRAW_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor3","#FF0000");
					}else{
						temp = temp.replace("@fcolor3","#00FF00");
					}
				}
				temp =temp.replace("@withdraw", mailDataMap.get("ALL_WITHDRAW_"+dateStr[i])+"");
				if(mailDataMap.get("ALL_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("ALL_TOPUP_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("ALL_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("ALL_TOPUP_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor4","#FF0000");
					}else{
						temp = temp.replace("@fcolor4","#00FF00");
					}
				}
				temp =temp.replace("@topup", mailDataMap.get("ALL_TOPUP_"+dateStr[i])+"");
				sb.append(temp);
			}
		sb.append(tableEndAll);
		
		
		//---------- show portal 1 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 1"));
		sb.append(th);
			for(int i = 0;i<dateStr.length-1;i++){
				String temp = String.valueOf(td);
				temp = temp.replace("@dateTime", dateStr[i]);
				if(mailDataMap.get("SERVER1_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER1_DAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER1_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER1_DAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor1","#FF0000");
					}else{
						temp = temp.replace("@fcolor1","#00FF00");
					}
				}
				temp = temp.replace("@dau", mailDataMap.get("SERVER1_DAU_"+dateStr[i])+"");
				temp = temp.replace("@rdau", mailDataMap.get("SERVER1_RDAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER1_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER1_NAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER1_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER1_NAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor2","#FF0000");
					}else{
						temp = temp.replace("@fcolor2","#00FF00");
					}
				}
				temp =temp.replace("@nau", mailDataMap.get("SERVER1_NAU_"+dateStr[i])+"");
				temp =temp.replace("@tau", mailDataMap.get("SERVER1_TAU_"+dateStr[i])+"");
				temp =temp.replace("@mau", mailDataMap.get("SERVER1_MAU_"+dateStr[i])+"");
				temp =temp.replace("@hau", mailDataMap.get("SERVER1_HAU_"+dateStr[i])+"");
				temp =temp.replace("@pau", mailDataMap.get("SERVER1_PAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER1_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER1_WITHDRAW_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER1_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER1_WITHDRAW_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor3","#FF0000");
					}else{
						temp = temp.replace("@fcolor3","#00FF00");
					}
				}
				temp =temp.replace("@withdraw", mailDataMap.get("SERVER1_WITHDRAW_"+dateStr[i])+"");
				temp =temp.replace("@mtau", mailDataMap.get("SERVER1_MTAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER1_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER1_TOPUP_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER1_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER1_TOPUP_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor4","#FF0000");
					}else{
						temp = temp.replace("@fcolor4","#00FF00");
					}
				}
				temp =temp.replace("@topup", mailDataMap.get("SERVER1_TOPUP_"+dateStr[i])+"");
				temp =temp.replace("@mtopup", mailDataMap.get("SERVER1_MTOPUP_"+dateStr[i])+"");
				temp =temp.replace("@arpu", mailDataMap.get("SERVER1_ARPU_"+dateStr[i])+"");
				temp =temp.replace("@arppu", mailDataMap.get("SERVER1_ARPPU_"+dateStr[i])+"");
				temp =temp.replace("@pmb", mailDataMap.get("SERVER1_PMB_"+dateStr[i])+"");
				temp =temp.replace("@toau", mailDataMap.get("SERVER1_TOAU_"+dateStr[i])+"");
				temp =temp.replace("@aoau", mailDataMap.get("SERVER1_AOAU_"+dateStr[i])+"");
				temp =temp.replace("@uot", mailDataMap.get("SERVER1_UOT_"+dateStr[i])+"");
				sb.append(temp);
			}
		sb.append(tableEnd);
		
		//---------- show portal 2 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 2"));
		sb.append(th);
			for(int i = 0;i<dateStr.length-1;i++){
				String temp = String.valueOf(td);
				temp = temp.replace("@dateTime", dateStr[i]);
				if(mailDataMap.get("SERVER2_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER2_DAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER2_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER2_DAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor1","#FF0000");
					}else{
						temp = temp.replace("@fcolor1","#00FF00");
					}
				}
				temp = temp.replace("@dau", mailDataMap.get("SERVER2_DAU_"+dateStr[i])+"");
				temp = temp.replace("@rdau", mailDataMap.get("SERVER2_RDAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER2_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER2_NAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER2_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER2_NAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor2","#FF0000");
					}else{
						temp = temp.replace("@fcolor2","#00FF00");
					}
				}
				temp =temp.replace("@nau", mailDataMap.get("SERVER2_NAU_"+dateStr[i])+"");
				temp =temp.replace("@tau", mailDataMap.get("SERVER2_TAU_"+dateStr[i])+"");
				temp =temp.replace("@mau", mailDataMap.get("SERVER2_MAU_"+dateStr[i])+"");
				temp =temp.replace("@hau", mailDataMap.get("SERVER2_HAU_"+dateStr[i])+"");
				temp =temp.replace("@pau", mailDataMap.get("SERVER2_PAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER2_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER2_WITHDRAW_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER2_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER2_WITHDRAW_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor3","#FF0000");
					}else{
						temp = temp.replace("@fcolor3","#00FF00");
					}
				}
				temp =temp.replace("@withdraw", mailDataMap.get("SERVER2_WITHDRAW_"+dateStr[i])+"");
				temp =temp.replace("@mtau", mailDataMap.get("SERVER2_MTAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER2_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER2_TOPUP_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER2_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER2_TOPUP_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor4","#FF0000");
					}else{
						temp = temp.replace("@fcolor4","#00FF00");
					}
				}
				temp =temp.replace("@topup", mailDataMap.get("SERVER2_TOPUP_"+dateStr[i])+"");
				temp =temp.replace("@mtopup", mailDataMap.get("SERVER2_MTOPUP_"+dateStr[i])+"");
				temp =temp.replace("@arpu", mailDataMap.get("SERVER2_ARPU_"+dateStr[i])+"");
				temp =temp.replace("@arppu", mailDataMap.get("SERVER2_ARPPU_"+dateStr[i])+"");
				temp =temp.replace("@pmb", mailDataMap.get("SERVER2_PMB_"+dateStr[i])+"");
				temp =temp.replace("@toau", mailDataMap.get("SERVER2_TOAU_"+dateStr[i])+"");
				temp =temp.replace("@aoau", mailDataMap.get("SERVER2_AOAU_"+dateStr[i])+"");
				temp =temp.replace("@uot", mailDataMap.get("SERVER2_UOT_"+dateStr[i])+"");
				sb.append(temp);
			}
		sb.append(tableEnd);
		
		
		//---------- show portal 3 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 3"));
		sb.append(th);
			for(int i = 0;i<dateStr.length-1;i++){
				String temp = String.valueOf(td);
				temp = temp.replace("@dateTime", dateStr[i]);
				if(mailDataMap.get("SERVER3_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER3_DAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER3_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER3_DAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor1","#FF0000");
					}else{
						temp = temp.replace("@fcolor1","#00FF00");
					}
				}
				temp = temp.replace("@dau", mailDataMap.get("SERVER3_DAU_"+dateStr[i])+"");
				temp = temp.replace("@rdau", mailDataMap.get("SERVER3_RDAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER3_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER3_NAU_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER3_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER3_NAU_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor2","#FF0000");
					}else{
						temp = temp.replace("@fcolor2","#00FF00");
					}
				}
				temp =temp.replace("@nau", mailDataMap.get("SERVER3_NAU_"+dateStr[i])+"");
				temp =temp.replace("@tau", mailDataMap.get("SERVER3_TAU_"+dateStr[i])+"");
				temp =temp.replace("@mau", mailDataMap.get("SERVER3_MAU_"+dateStr[i])+"");
				temp =temp.replace("@hau", mailDataMap.get("SERVER3_HAU_"+dateStr[i])+"");
				temp =temp.replace("@pau", mailDataMap.get("SERVER3_PAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER3_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER3_WITHDRAW_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER3_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER3_WITHDRAW_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor3","#FF0000");
					}else{
						temp = temp.replace("@fcolor3","#00FF00");
					}
				}
				temp =temp.replace("@withdraw", mailDataMap.get("SERVER3_WITHDRAW_"+dateStr[i])+"");
				temp =temp.replace("@mtau", mailDataMap.get("SERVER3_MTAU_"+dateStr[i])+"");
				if(mailDataMap.get("SERVER3_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER3_TOPUP_"+dateStr[i+1])!=null){
					if((Integer)mailDataMap.get("SERVER3_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER3_TOPUP_"+dateStr[i+1]) > 0 ){
						temp = temp.replace("@fcolor4","#FF0000");
					}else{
						temp = temp.replace("@fcolor4","#00FF00");
					}
				}
				temp =temp.replace("@topup", mailDataMap.get("SERVER3_TOPUP_"+dateStr[i])+"");
				temp =temp.replace("@mtopup", mailDataMap.get("SERVER3_MTOPUP_"+dateStr[i])+"");
				temp =temp.replace("@arpu", mailDataMap.get("SERVER3_ARPU_"+dateStr[i])+"");
				temp =temp.replace("@arppu", mailDataMap.get("SERVER3_ARPPU_"+dateStr[i])+"");
				temp =temp.replace("@pmb", mailDataMap.get("SERVER3_PMB_"+dateStr[i])+"");
				temp =temp.replace("@toau", mailDataMap.get("SERVER3_TOAU_"+dateStr[i])+"");
				temp =temp.replace("@aoau", mailDataMap.get("SERVER3_AOAU_"+dateStr[i])+"");
				temp =temp.replace("@uot", mailDataMap.get("SERVER3_UOT_"+dateStr[i])+"");
				sb.append(temp);
			}
		sb.append(tableEnd);
		
		//---------- show portal 4 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 4"));
		sb.append(th);
		for(int i = 0;i<dateStr.length-1;i++){
			String temp = String.valueOf(td);
			temp = temp.replace("@dateTime", dateStr[i]);
			if(mailDataMap.get("SERVER4_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER4_DAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER4_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER4_DAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor1","#FF0000");
				}else{
					temp = temp.replace("@fcolor1","#00FF00");
				}
			}
			temp = temp.replace("@dau", mailDataMap.get("SERVER4_DAU_"+dateStr[i])+"");
			temp = temp.replace("@rdau", mailDataMap.get("SERVER4_RDAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER4_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER4_NAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER4_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER4_NAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor2","#FF0000");
				}else{
					temp = temp.replace("@fcolor2","#00FF00");
				}
			}
			temp =temp.replace("@nau", mailDataMap.get("SERVER4_NAU_"+dateStr[i])+"");
			temp =temp.replace("@tau", mailDataMap.get("SERVER4_TAU_"+dateStr[i])+"");
			temp =temp.replace("@mau", mailDataMap.get("SERVER4_MAU_"+dateStr[i])+"");
			temp =temp.replace("@hau", mailDataMap.get("SERVER4_HAU_"+dateStr[i])+"");
			temp =temp.replace("@pau", mailDataMap.get("SERVER4_PAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER4_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER4_WITHDRAW_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER4_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER4_WITHDRAW_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor3","#FF0000");
				}else{
					temp = temp.replace("@fcolor3","#00FF00");
				}
			}
			temp =temp.replace("@withdraw", mailDataMap.get("SERVER4_WITHDRAW_"+dateStr[i])+"");
			temp =temp.replace("@mtau", mailDataMap.get("SERVER4_MTAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER4_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER4_TOPUP_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER4_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER4_TOPUP_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor4","#FF0000");
				}else{
					temp = temp.replace("@fcolor4","#00FF00");
				}
			}
			temp =temp.replace("@topup", mailDataMap.get("SERVER4_TOPUP_"+dateStr[i])+"");
			temp =temp.replace("@mtopup", mailDataMap.get("SERVER4_MTOPUP_"+dateStr[i])+"");
			temp =temp.replace("@arpu", mailDataMap.get("SERVER4_ARPU_"+dateStr[i])+"");
			temp =temp.replace("@arppu", mailDataMap.get("SERVER4_ARPPU_"+dateStr[i])+"");
			temp =temp.replace("@pmb", mailDataMap.get("SERVER4_PMB_"+dateStr[i])+"");
			temp =temp.replace("@toau", mailDataMap.get("SERVER4_TOAU_"+dateStr[i])+"");
			temp =temp.replace("@aoau", mailDataMap.get("SERVER4_AOAU_"+dateStr[i])+"");
			temp =temp.replace("@uot", mailDataMap.get("SERVER4_UOT_"+dateStr[i])+"");
			sb.append(temp);
		}
		sb.append(tableEnd);
		
		//---------- show portal 5 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 5"));
		sb.append(th);
		for(int i = 0;i<dateStr.length-1;i++){
			String temp = String.valueOf(td);
			temp = temp.replace("@dateTime", dateStr[i]);
			if(mailDataMap.get("SERVER5_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER5_DAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER5_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER5_DAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor1","#FF0000");
				}else{
					temp = temp.replace("@fcolor1","#00FF00");
				}
			}
			temp = temp.replace("@dau", mailDataMap.get("SERVER5_DAU_"+dateStr[i])+"");
			temp = temp.replace("@rdau", mailDataMap.get("SERVER5_RDAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER5_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER5_NAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER5_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER5_NAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor2","#FF0000");
				}else{
					temp = temp.replace("@fcolor2","#00FF00");
				}
			}
			temp =temp.replace("@nau", mailDataMap.get("SERVER5_NAU_"+dateStr[i])+"");
			temp =temp.replace("@tau", mailDataMap.get("SERVER5_TAU_"+dateStr[i])+"");
			temp =temp.replace("@mau", mailDataMap.get("SERVER5_MAU_"+dateStr[i])+"");
			temp =temp.replace("@hau", mailDataMap.get("SERVER5_HAU_"+dateStr[i])+"");
			temp =temp.replace("@pau", mailDataMap.get("SERVER5_PAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER5_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER5_WITHDRAW_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER5_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER5_WITHDRAW_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor3","#FF0000");
				}else{
					temp = temp.replace("@fcolor3","#00FF00");
				}
			}
			temp =temp.replace("@withdraw", mailDataMap.get("SERVER5_WITHDRAW_"+dateStr[i])+"");
			temp =temp.replace("@mtau", mailDataMap.get("SERVER5_MTAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER5_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER5_TOPUP_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER5_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER5_TOPUP_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor5","#FF0000");
				}else{
					temp = temp.replace("@fcolor5","#00FF00");
				}
			}
			temp =temp.replace("@topup", mailDataMap.get("SERVER5_TOPUP_"+dateStr[i])+"");
			temp =temp.replace("@mtopup", mailDataMap.get("SERVER5_MTOPUP_"+dateStr[i])+"");
			temp =temp.replace("@arpu", mailDataMap.get("SERVER5_ARPU_"+dateStr[i])+"");
			temp =temp.replace("@arppu", mailDataMap.get("SERVER5_ARPPU_"+dateStr[i])+"");
			temp =temp.replace("@pmb", mailDataMap.get("SERVER5_PMB_"+dateStr[i])+"");
			temp =temp.replace("@toau", mailDataMap.get("SERVER5_TOAU_"+dateStr[i])+"");
			temp =temp.replace("@aoau", mailDataMap.get("SERVER5_AOAU_"+dateStr[i])+"");
			temp =temp.replace("@uot", mailDataMap.get("SERVER5_UOT_"+dateStr[i])+"");
			sb.append(temp);
		}
		sb.append(tableEnd);
		//---------- show portal 6 
		sb.append(tableHead.replace("@PROTAL", "OA PRO SERVER 6"));
		sb.append(th);
		for(int i = 0;i<dateStr.length-1;i++){
			String temp = String.valueOf(td);
			temp = temp.replace("@dateTime", dateStr[i]);
			if(mailDataMap.get("SERVER6_DAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER6_DAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER6_DAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER6_DAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor1","#FF0000");
				}else{
					temp = temp.replace("@fcolor1","#00FF00");
				}
			}
			temp = temp.replace("@dau", mailDataMap.get("SERVER6_DAU_"+dateStr[i])+"");
			temp = temp.replace("@rdau", mailDataMap.get("SERVER6_RDAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER6_NAU_"+dateStr[i])!=null&&mailDataMap.get("SERVER6_NAU_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER6_NAU_"+dateStr[i])-(Integer)mailDataMap.get("SERVER6_NAU_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor2","#FF0000");
				}else{
					temp = temp.replace("@fcolor2","#00FF00");
				}
			}
			temp =temp.replace("@nau", mailDataMap.get("SERVER6_NAU_"+dateStr[i])+"");
			temp =temp.replace("@tau", mailDataMap.get("SERVER6_TAU_"+dateStr[i])+"");
			temp =temp.replace("@mau", mailDataMap.get("SERVER6_MAU_"+dateStr[i])+"");
			temp =temp.replace("@hau", mailDataMap.get("SERVER6_HAU_"+dateStr[i])+"");
			temp =temp.replace("@pau", mailDataMap.get("SERVER6_PAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER6_WITHDRAW_"+dateStr[i])!=null&&mailDataMap.get("SERVER6_WITHDRAW_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER6_WITHDRAW_"+dateStr[i])-(Integer)mailDataMap.get("SERVER6_WITHDRAW_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor3","#FF0000");
				}else{
					temp = temp.replace("@fcolor3","#00FF00");
				}
			}
			temp =temp.replace("@withdraw", mailDataMap.get("SERVER6_WITHDRAW_"+dateStr[i])+"");
			temp =temp.replace("@mtau", mailDataMap.get("SERVER6_MTAU_"+dateStr[i])+"");
			if(mailDataMap.get("SERVER6_TOPUP_"+dateStr[i])!=null&&mailDataMap.get("SERVER6_TOPUP_"+dateStr[i+1])!=null){
				if((Integer)mailDataMap.get("SERVER6_TOPUP_"+dateStr[i])-(Integer)mailDataMap.get("SERVER6_TOPUP_"+dateStr[i+1]) > 0 ){
					temp = temp.replace("@fcolor6","#FF0000");
				}else{
					temp = temp.replace("@fcolor6","#00FF00");
				}
			}
			temp =temp.replace("@topup", mailDataMap.get("SERVER6_TOPUP_"+dateStr[i])+"");
			temp =temp.replace("@mtopup", mailDataMap.get("SERVER6_MTOPUP_"+dateStr[i])+"");
			temp =temp.replace("@arpu", mailDataMap.get("SERVER6_ARPU_"+dateStr[i])+"");
			temp =temp.replace("@arppu", mailDataMap.get("SERVER6_ARPPU_"+dateStr[i])+"");
			temp =temp.replace("@pmb", mailDataMap.get("SERVER6_PMB_"+dateStr[i])+"");
			temp =temp.replace("@toau", mailDataMap.get("SERVER6_TOAU_"+dateStr[i])+"");
			temp =temp.replace("@aoau", mailDataMap.get("SERVER6_AOAU_"+dateStr[i])+"");
			temp =temp.replace("@uot", mailDataMap.get("SERVER6_UOT_"+dateStr[i])+"");
			sb.append(temp);
		}
		
		//-------------------------- end 
		sb.append(tableEnd);
		
		MyUtil.sendEmail("Daily Report - "+dateStr[0], sb.toString().replace("null", "-"));
	}
	
	
	/*public static void main(String args[]){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		String dateStr[] = {"","","",""};
		dateStr[0]=sdf.format(c1.getTime());
		for(int i = 1;i<4;i++){
			c1.add( Calendar.DATE,-1);
			dateStr[i]=sdf.format(c1.getTime());
		}
		
		for(int i = 0;i<4;i++){
			System.out.println(dateStr[i]);
		}
	}*/
	
}
