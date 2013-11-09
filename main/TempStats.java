package main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import basicStats.BusinessModel;

import util.MyUtil;

public class TempStats {
	static Logger logger = Logger.getLogger(TempStats.class);
	final static String oauid = "OaDownUID";

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class);
		Date fTime = new Date();
		Date tTime = new Date();
		int high = 50;
		int mid = 5;
		if(args.length == 4){
			fTime = MyUtil.StringToDate(args[0]);
			tTime = MyUtil.StringToDate(args[1]);
			high = Integer.parseInt(args[2]);
			mid = Integer.parseInt(args[3]);
			
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
		
//		long flag =  (tTime.getTime()-fTime.getTime())/1000/3600/24;
//		for(long temp=0;temp<flag;temp++){
//			Calendar fromWhen = Calendar.getInstance();
//			fromWhen.setTime(fTime);
//			GregorianCalendar gc = new GregorianCalendar(fromWhen
//					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
//					.get(Calendar.DAY_OF_MONTH));
//			gc.add(Calendar.DATE, 1);
//			tTime = (Date) gc.getTime();
			
//			System.out.print(fTime);
//			System.out.print("----");
//			System.out.println(tTime);
			
//			TempDownloadStats.StartStats(tTime);
//			TempNewUserStats.StartStats(fTime);
//			TempSkipResult.StartStats(fTime);
			
			BusinessModel.test(fTime,tTime,high,mid);
			
			fTime = tTime;
//		}
		logger.info("Stats process end "+new Date());
	}
}
