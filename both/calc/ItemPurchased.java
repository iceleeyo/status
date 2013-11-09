package both.calc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import both.basic.ConfigUtil;
import both.basic.ItemSell;

public class ItemPurchased {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public static void itemBuyGroupUp(Date date){
		Map<String,Object[]> server1DataMap = ItemSell.getGroupUpByServer(date, "server1");
		System.out.println(server1DataMap);
		for(String item : server1DataMap.keySet()){
			ConfigUtil.myPortalDb.execSQLUpdate("insert into itemBuyGroupUp(price,users,amount,itemType,btime) values (?,?,?,?,?)", server1DataMap.get(item));
		}
		
		
	}
	
	public static void main(String[] args) {
		try{
			/*Date d1 = sdf.parse("2013-09-01");
			Date d2 = sdf.parse("2013-09-29");
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			while(c.getTimeInMillis()< d2.getTime()){
				System.out.println(c.getTime());
				calcNomal(c.getTime());
				c.add(Calendar.DATE, 1);
			}*/
			itemBuyGroupUp(sdf.parse(args[0]));
		}catch(Exception e){
			
		}
	}
}
