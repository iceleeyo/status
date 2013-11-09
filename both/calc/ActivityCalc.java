package both.calc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;

import both.basic.ChannelPayment;
import both.basic.ConfigUtil;
import both.basic.ReportPayment;
import both.basic.Users;

public class ActivityCalc {
	static double usdTosar=3.745;
	static double egpTosar=0.538;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void calcRamadan(Date stime,Date etime){
		try{
			Map<Integer,Integer> s1UEFisher=Users.getTraceUserByServerHour(stime, etime, "Server2");
			setData("ActivityUser", s1UEFisher.size()+"");
			Map<Integer,Integer> _s1StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server2");
			Map<Integer,Integer> _s1EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server2");
			Map<Integer,Integer> _s1GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server2");
			
			Map<Integer,Integer> _temp = new HashMap<Integer,Integer>();
			_temp.putAll(_s1StcPay);
			for(int id :_s1GooglePay.keySet()){
				_temp.remove(id);
			}
			
			for(int id :_s1EgyptPay.keySet()){
				_temp.remove(id);
			}
			
			int s1PayUser=_temp.size()+_s1GooglePay.size()+_s1EgyptPay.size();
			
			setData("TopupUser",s1PayUser+"");
			
			double s1StcPay=getPaymentInteger(_s1StcPay)/100;
			double s1GooglePay=getPaymentInteger(_s1GooglePay)*usdTosar;
			double s1EgyptPay=getPaymentInteger(_s1EgyptPay)/100*egpTosar;
			double s1AllPay=s1GooglePay+s1StcPay+s1EgyptPay;
			
			setData("Topup",s1AllPay+"");
			
			DBResultSet rs = ConfigUtil.portal1db.execSQLQuery("select sum(price) price,count(distinct monetid) users,count(*) amount,itemtype from(select i.*,i1.itemtype from itembuy i,itemconfig i1 where i.item=i1.itemname and btime >= ? and btime < ? and server = ?) t group by itemtype",new Object[]{stime,etime,"server2"}) ;
			while(rs.next()){
				setData(rs.getString("itemtype"),rs.getInt("price")+"");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void setData(String key,String value){
		System.out.println("["+key+"]:\t"+value);
	}
	
	static double getPaymentInteger(Map<Integer,Integer> dataMap){
		double result = 0;
		for(int id :dataMap.keySet()){
			result +=dataMap.get(id);
		}
		return result;
	}
	
	public static void main(String[] args){
		try{
			
			//String stime[] ={"2013-07-12 15:00:00","2013-07-16 15:00:00","2013-07-20 15:00:00","2013-07-24 15:00:00","2013-07-28 15:00:00","2013-08-01 15:00:00","2013-08-05 15:00:00"};
			//String etime[] ={"2013-07-15 17:00:00","2013-07-19 17:00:00","2013-07-23 17:00:00","2013-07-28 05:00:00","2013-08-01 05:00:00","2013-08-05 05:00:00","2013-08-09 05:00:00"};
			
			String stime[] ={"2013-07-12 15:00:00","2013-07-18 15:00:00","2013-07-24 15:00:00","2013-07-30 15:00:00","2013-08-05 15:00:00"};
			String etime[] ={"2013-07-16 17:00:00","2013-07-22 17:00:00","2013-07-29 05:00:00","2013-08-04 05:00:00","2013-08-10 05:00:00"};
			for(int i = 0;i<stime.length;i++){
				System.out.println("---------------------------------------------------------------");
				System.out.println(stime[i]);
				System.out.println(etime[i]);
				calcRamadan(sdf.parse(stime[i]), sdf.parse(etime[i]));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
