package tempStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class TempSkipResult {
	static Logger logger = Logger.getLogger(TempSkipResult.class);
	
	public static int getMonetid(String log){
		int monetid = 0;
		if(log.contains("monetId")){
			log = log.substring(log.indexOf("monetId")+"monetId=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			monetid = Integer.parseInt(log);
		}
		return monetid;
	}
/*	
	static public Set<Integer> getSkipList(Date date){
		//the log location
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = "/home/mozat/morange/oapro/OceanAgelogs/action.log."+ simpleDateFormat.format(date);
		Set<Integer> set = new HashSet<Integer>();
		
		BufferedReader br = null;
		try {
			String temp = null;
			String tempPath = "";
			int monetid = 0;
			
			for(int index=0;index<24;index++){
				if(index<10){
					tempPath = path +"-0"+index;
				}else{
					tempPath = path +"-"+index;
				}
				if((new File(tempPath)).exists()){
					br = new BufferedReader(new FileReader(tempPath));
					temp = br.readLine();
					while(temp!=null){
						if(temp.contains("skip")){
							monetid = getMonetid(temp);
							set.add(monetid);
						}
						temp = br.readLine();
					}
				}
			}
			if(br!=null){
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;
	}
	
	public static Set<Integer> getDAUList(Date date){
		String path = "/home/mozat/morange/OceanAgeDailyMonetidList/"+MyUtil.DateToString(date)+"_newoa.txt";
		Set<Integer> set = new HashSet<Integer>();
		if((new File(path)).isFile()){
			set = getIdList(path,set);
		}
		return set;
	}
	
	public static Set<Integer> getIdList(String path,Set<Integer> idSet){
		BufferedReader br;
//		Set<String> idSet = new java.util.HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			temp = br.readLine();
			while (temp != null) {
				if(MyUtil.isMonetid(temp)){
					idSet.add(Integer.parseInt(temp));	
				}
				temp = br.readLine();
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return idSet;
	}
*/
	
	static public void getGuideBeforeSkip(Date date){
		Map<Integer,Integer> guideMap = new HashMap<Integer,Integer>();
		
		//////////////////////////////////////////////////////
		//the log location
		
		Date fTime = new Date();
		Date tTime = new Date();
		for(long flag=0;flag<12;flag++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			gc.add(Calendar.DATE, -1);
			tTime = (Date) gc.getTime();
			System.out.println(tTime);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String path = "/home/mozat/morange/oapro/OceanAgelogs/action.log."+ simpleDateFormat.format(tTime);
			Set<Integer> skipSet = new HashSet<Integer>();
			Map<Integer,Integer> lastGuideMap = new HashMap<Integer,Integer>();
			BufferedReader br = null;
			try {
				String temp = null;
				String tempPath = "";
				int monetid = 0;
				
				for(int index=0;index<24;index++){
					if(index<10){
						tempPath = path +"-0"+index;
					}else{
						tempPath = path +"-"+index;
					}
					if((new File(tempPath)).exists()){
						br = new BufferedReader(new FileReader(tempPath));
						temp = br.readLine();
						while(temp!=null){
							if(temp.contains("skip")){
								monetid = getMonetid(temp);
								skipSet.add(monetid);
							}
							if(temp.contains("action=finishGuide")){
								int guide = Integer.parseInt(temp.substring(temp.lastIndexOf("=")+1, temp.length()));
								lastGuideMap.put(monetid,guide);
							}
							temp = br.readLine();
						}
					}
				}
				if(br!=null){
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//
			
			for(int monetid:skipSet){
				if(lastGuideMap.containsKey(monetid)){
					if(guideMap.containsKey(lastGuideMap.get(monetid))){
						guideMap.put(lastGuideMap.get(monetid), guideMap.get(lastGuideMap.get(monetid))+1);
					}else{
						guideMap.put(lastGuideMap.get(monetid), 1);
					}
				}
			}
			fTime = tTime;
		}
		
		
		///////////////////////////////////
		//output
		Set<Integer> guideSet = guideMap.keySet();
		for(Integer guide : guideSet){
			System.out.println(":"+guide+":"+guideMap.get(guide));
		}
	}
	
	static public void StartStats(Date date){
//		Set<Integer> skipSet = getSkipList(date);
//		Set<Integer> dauList = getDAUList(new Date(date.getTime()+1000*3600*24));
//		int both = 0;
//		for(int monetid : skipSet){
//			if(dauList.contains(monetid)){
//				both++;
//			}
//		}
//		System.out.println(MyUtil.DateToString(date) + ":" + skipSet.size() + ":" +  both*1.0/skipSet.size());
		getGuideBeforeSkip(date);
	}
	
	public static void main(String[] args){
		String temp = "INFO 28 May 14:00:52,618 -(StatLog.java:86) - monetId=58035,shipyardLevel=1,petLevel=0,defenseSystemLevel=1,action=finishGuide,step=2";
		System.out.println(temp.lastIndexOf("=")+1);
		System.out.println(temp.length());
		System.out.println(temp.substring(temp.lastIndexOf("=")+1, temp.length()));
	}
}
