package main;

import item.GoldStats;
import item.StockStats;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;


import util.MyUtil;

import basicStats.TribeStats;


public class Main {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class);
		Date fTime = new Date();
		Date tTime = new Date();
		if(args.length == 2){
			fTime = MyUtil.StringToDate(args[0]);
			tTime = MyUtil.StringToDate(args[1]);
		}
		if(args.length == 0){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			tTime = (Date) gc.getTime();
			gc.add(Calendar.DATE, -1);
			fTime = (Date) gc.getTime();
		}
		logger.info("Stats process start "+new Date());
		
		long flag =  (tTime.getTime()-fTime.getTime())/1000/3600/24;
		for(long temp=0;temp<flag;temp++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			gc.add(Calendar.DATE, 1);
			tTime = (Date) gc.getTime();
			
			System.out.print(fTime);
			System.out.print("----");
			System.out.println(tTime);
			
			GoldStats.setAllGold(fTime);
			GoldStats.setAllPearl(fTime);
			GoldStats.setAllCredit(fTime);
			StockStats.StartStats(fTime);
			//家族等级分布
			TribeStats.setAllTribeLevel(fTime);
			
			fTime = tTime;
		}
		logger.info("Stats process end "+new Date());
	}
	
}
