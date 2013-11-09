package both.calc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import both.basic.ChannelPayment;
import both.basic.ConfigUtil;
import both.basic.UserPayment;

public class Payment {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void userPayment(Date date){
		Map<Integer,Integer> payUserMap = UserPayment.getPayUser(date);
		Map<Integer,Integer> EgyptPayUserMap = UserPayment.getEgyptBilling(date);
		Map<Integer,Integer> inmobiPayUserMap = UserPayment.getPayUserByChannel("inmobi", payUserMap);
		
		Map<Integer,Integer> stcPayUserMap = new HashMap<Integer,Integer>();
		stcPayUserMap.putAll(payUserMap);
		
		for(int id :EgyptPayUserMap.keySet()){
			stcPayUserMap.remove(id);
		}
		
		for(int id :inmobiPayUserMap.keySet()){
			stcPayUserMap.remove(id);
		}
		
		int stcPayUser = stcPayUserMap.size();
		int stcUserPayment = 0;
		for(int id : stcPayUserMap.keySet()){
			stcUserPayment += stcPayUserMap.get(id)/100;
		}
		
		int inmobiPayUser = inmobiPayUserMap.size();
		int inmobiUserPayment = 0;
		for(int id : inmobiPayUserMap.keySet()){
			inmobiUserPayment += inmobiPayUserMap.get(id)/100;
		}
		
		int EgyptPayUser = EgyptPayUserMap.size();
		int EgyptUserPayment = 0;
		for(int id : EgyptPayUserMap.keySet()){
			EgyptUserPayment += EgyptPayUserMap.get(id)/100;
		}
		
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"stc_user",stcPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"stc_amount",stcUserPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"inmobi_user",inmobiPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"inmobi_amount",inmobiUserPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"Egypt_user",EgyptPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"Egypt_amount",EgyptUserPayment,sdf.format(date)});
	}
	
	
	public static void channelPayment(Date date){
		//Map<Integer,Integer> payUserMap = UserPayment.getPayUser(date);
		Map<Integer,Integer> EgyptPayUserMap = UserPayment.getEgyptBilling(date);
		Map<Integer,Integer> googleWalletPayUserMap = ChannelPayment.getGoogleWalletPayment(date);
		Map<Integer,Integer> fortumoPayUserMap = ChannelPayment.getFortumoPayment(date);
		Map<Integer,Integer> stcPayUserMap = ChannelPayment.getStcPayment(date);
		Map<Integer,Integer> payPalPayUserMap = ChannelPayment.getPayPalPayment(date);
		
		for(int id :EgyptPayUserMap.keySet()){
			stcPayUserMap.remove(id);
		}
		
		int stcPayUser = stcPayUserMap.size();
		int stcUserPayment = 0;
		for(int id : stcPayUserMap.keySet()){
			stcUserPayment += stcPayUserMap.get(id)/100;
		}
		
		int EgyptPayUser = EgyptPayUserMap.size();
		int EgyptUserPayment = 0;
		for(int id : EgyptPayUserMap.keySet()){
			EgyptUserPayment += EgyptPayUserMap.get(id);
		}
		
		int googleWalletPayUser = googleWalletPayUserMap.size();
		int googleWalletPayment = 0;
		for(int id : googleWalletPayUserMap.keySet()){
			googleWalletPayment += googleWalletPayUserMap.get(id);
		}
		
		
		int fortumoPayUser = fortumoPayUserMap.size();
		int fortumoUserPayment = 0;
		for(int id : fortumoPayUserMap.keySet()){
			fortumoUserPayment += fortumoPayUserMap.get(id)/100;
		}
		
		
		int payPalPayUser = payPalPayUserMap.size();
		int payPalUserPayment = 0;
		for(int id : payPalPayUserMap.keySet()){
			payPalUserPayment += payPalPayUserMap.get(id)/100;
		}
		
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_stc_user",stcPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_stc_amount",stcUserPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_fortumo_user",fortumoPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_fortumo_amount",fortumoUserPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_Egypt_user",EgyptPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_Egypt_amount",EgyptUserPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_googleWallet_user",googleWalletPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_googleWallet_amount",googleWalletPayment,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_payPal_user",payPalPayUser,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into userPayment(oakey,oavalue,pdate) values (?,?,?)", new Object[]{"billing_payPal_amount",payPalUserPayment,sdf.format(date)});
	}
	
	public static void main(String[] args) {
		try{
			Date d1 = sdf.parse("2013-09-20");
			Date d2 = sdf.parse("2013-10-08");
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			while(c.getTimeInMillis()< d2.getTime()){
				System.out.println(c.getTime());
				
				Payment.channelPayment(c.getTime());
				c.add(Calendar.DATE, 1);
			}
		}catch(Exception e){
			
		}
	}
}
