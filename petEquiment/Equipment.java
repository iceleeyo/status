package petEquiment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBResultSet;
import both.basic.ConfigUtil;
import both.basic.Users;

public class Equipment {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @desc 活跃用户强化武器分布
	 * 
	 */
	public static void calcActivityUserEquipmentReinforce(Date dataTime){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(dataTime);
			c.set(Calendar.HOUR,5);
			
			Date stime = c.getTime();
			
			c.add(Calendar.DATE, 1);
			
			Date etime = c.getTime();
			
			Map<Integer,Integer> activityUserMap = Users.getTraceUser(stime, etime);
			
			StringBuffer sb = new StringBuffer("");
			for(int id : activityUserMap.keySet()){
				sb.append(id+",");
			}
			
			String idList = sb.toString();
			idList = idList.substring(0,idList.length()-1);
			
			Map<String,Integer> eqMap = new HashMap<String,Integer>();
			
			DBResultSet ds = ConfigUtil.myServerDb.execSQLQuery("select * from equipment where monet_id in("+idList+") and type_id >=5 ", new Object[]{});
			while(ds.next()){
				int _typeId = ds.getInt("type_id");
				int reinforce_count = ds.getInt("reinforce_count");
				if(eqMap.get(_typeId+"_"+reinforce_count)==null){
					eqMap.put(_typeId+"_"+reinforce_count,0);
				}
				
				int _num = eqMap.get(_typeId+"_"+reinforce_count);
				_num += 1;
				eqMap.put(_typeId+"_"+reinforce_count,_num);
				
				
				if(eqMap.get(_typeId+"_ALL")==null){
					eqMap.put(_typeId+"_ALL",0);
				}
				
				int _all = eqMap.get(_typeId+"_ALL");
				_all += 1;
				eqMap.put(_typeId+"_ALL",_all);
				
				if(reinforce_count>0){
					if(eqMap.get(_typeId+"_GT")==null){
						eqMap.put(_typeId+"_GT",0);
					}
					
					int _gt = eqMap.get(_typeId+"_GT");
					_gt += 1;
					
					eqMap.put(_typeId+"_GT",_gt);
				}
			}
			
			for(String id:eqMap.keySet()){
				String key[] = id.split("_");
				int _value = eqMap.get(id);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into EquipmentActivityUserReinforce(typeid,reinforce,oaValue,dtime) values (?,?,?,?)", new Object[]{key[0],key[1],_value,dataTime});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @desc 库存强化武器分布
	 * 
	 */
	public static void calcEquipmentReinforce(Date dataTime){
		try{
			
			Map<String,Integer> eqMap = new HashMap<String,Integer>();
			
			DBResultSet ds = ConfigUtil.myServerDb.execSQLQuery("select * from equipment where  type_id >=5 ", new Object[]{});
			while(ds.next()){
				int _typeId = ds.getInt("type_id");
				int reinforce_count = ds.getInt("reinforce_count");
				if(eqMap.get(_typeId+"_"+reinforce_count)==null){
					eqMap.put(_typeId+"_"+reinforce_count,0);
				}
				
				int _num = eqMap.get(_typeId+"_"+reinforce_count);
				_num += 1;
				eqMap.put(_typeId+"_"+reinforce_count,_num);
				
				
				if(eqMap.get(_typeId+"_ALL")==null){
					eqMap.put(_typeId+"_ALL",0);
				}
				
				int _all = eqMap.get(_typeId+"_ALL");
				_all += 1;
				eqMap.put(_typeId+"_ALL",_all);
				
				if(reinforce_count>0){
					if(eqMap.get(_typeId+"_GT")==null){
						eqMap.put(_typeId+"_GT",0);
					}
					
					int _gt = eqMap.get(_typeId+"_GT");
					_gt += 1;
					
					eqMap.put(_typeId+"_GT",_gt);
				}
			}
			
			for(String id:eqMap.keySet()){
				String key[] = id.split("_");
				int _value = eqMap.get(id);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into EquipmentReinforce(typeid,reinforce,oaValue,dtime) values (?,?,?,?)", new Object[]{key[0],key[1],_value,dataTime});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @desc 强化次数和强化消耗
	 * 
	 */
	public static void calcReinforceConsume(Date dataTime){
		BufferedReader br = null;
		try{
			String fileUrl = ConfigUtil.OaLogPath+"trace.log."+sdf.format(dataTime)+"*";
			Map<String,Map<Integer,Integer>> userTimeMap = new HashMap<String,Map<Integer,Integer>>();
			Map<String,Map<Integer,Integer>> userCreditTimeMap =new HashMap<String,Map<Integer,Integer>>();
			Map<String,Integer> levelTimeMap = new HashMap<String,Integer>();
			
			levelTimeMap.put("ALL", 0);
			levelTimeMap.put("ALL_Success", 0);
			levelTimeMap.put("ALL_Sapphire", 0);
			
			userTimeMap.put("ALL", new HashMap<Integer,Integer>());
			userCreditTimeMap.put("ALL", new HashMap<Integer,Integer>());
			
			Map<Integer,Integer[]> equipmentMap = new HashMap<Integer,Integer[]>();
			
			if(fileUrl!=null){
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=Reinforce, "+fileUrl+"|awk -F '[=,]' '{print $3\",\"$7\",\"$11\",\"$13\",\"$17\",\"$21\",\"$23\",\"$25}'"};
				//monetId,equipmentTypeId,quality,beforeLevel,result,pearlCount,stoneCount,sapphireCount
				System.out.println("---------------------------startProcess:"+"grep action=Reinforce, "+fileUrl+"|awk -F '[=,]' '{print $3\",\"$7\",\"$11\",\"$13\",\"$17\",\"$21\",\"$23\",\"$25}'");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	String[] _temp = result.split(",");
	            	
	            	//------------每个用户的强化次数&&总强化人数
	            	int _monetid = new Integer(_temp[0]);
	            	int _equipmentid = new Integer(_temp[1]);
	            	if(userTimeMap.get("ALL").containsKey(_monetid)){
	            		int _time = userTimeMap.get("ALL").get(_monetid);
	            		_time +=1;
	            		userTimeMap.get("ALL").put(_monetid, _time);
	            	}else{
	            		userTimeMap.get("ALL").put(_monetid, 1);
	            	}
	            	
	            	//------------各等级强化次数(强化前等级)&&总强化次数
	            	int _tt = levelTimeMap.get("ALL");
	            	_tt +=1;
	            	levelTimeMap.put("ALL",_tt);
	            	if(levelTimeMap.containsKey(_temp[3])){
	            		int _time = levelTimeMap.get(_temp[3]);
	            		_time +=1;
	            		levelTimeMap.put(_temp[3], _time);
	            	}else{
	            		levelTimeMap.put(_temp[3],1);
	            	}
	            	
	            	
	            	if(!userTimeMap.containsKey(_temp[3])){
	            		userTimeMap.put(_temp[3],new HashMap<Integer,Integer>());
	            	}
	            	
	            	Map<Integer,Integer> _tempMap = userTimeMap.get(_temp[3]);
	            	if(_tempMap.containsKey(_monetid)){
	            		int _time = _tempMap.get(_monetid);
	            		_time +=1;
	            		_tempMap.put(_monetid, _time);
	            	}else{
	            		_tempMap.put(_monetid, 1);
	            	}
	            	userTimeMap.put(_temp[3],_tempMap);
	            	
	            	//------------各等级强化成功次数(强化前等级)&&总强化成功次数
	            	if("suc".equals(_temp[4])){
	            		int _tts = levelTimeMap.get("ALL_Success");
	            		_tts +=1;
		            	levelTimeMap.put("ALL_Success",_tts);
		            	if(levelTimeMap.containsKey(_temp[3]+"_Success")){
		            		int _time = levelTimeMap.get(_temp[3]+"_Success");
		            		_time +=1;
		            		levelTimeMap.put(_temp[3]+"_Success", _time);
		            	}else{
		            		levelTimeMap.put(_temp[3]+"_Success",1);
		            	}
	            	}
	            	
	            	//------------各等级蓝宝石强化(强化前等级)&&总蓝宝石强化次数&&总蓝宝石强化人数
	            	if(!"0".equals(_temp[7])){
	            		userCreditTimeMap.get("ALL").put(_monetid, 0);
	            		int _tts1 = levelTimeMap.get("ALL_Sapphire");
	            		_tts1 +=1;
		            	levelTimeMap.put("ALL_Sapphire",_tts1);
		            	if(levelTimeMap.containsKey(_temp[3]+"_Sapphire")){
		            		int _time = levelTimeMap.get(_temp[3]+"_Sapphire");
		            		_time +=1;
		            		levelTimeMap.put(_temp[3]+"_Sapphire", _time);
		            	}else{
		            		levelTimeMap.put(_temp[3]+"_Sapphire",1);
		            	}
		            	
		            	if(!userCreditTimeMap.containsKey(_temp[3])){
		            		userCreditTimeMap.put(_temp[3],new HashMap<Integer,Integer>());
		            	}
		            	
		            	Map<Integer,Integer> _tempMap1 = userCreditTimeMap.get(_temp[3]);
		            	_tempMap1.put(_monetid, 0);
		            	userCreditTimeMap.put(_temp[3],_tempMap1);
		            	
	            	}
	            	
	            	//---------根据装备ID统计:强化次数，蓝宝石强化次数，消耗珍珠数，消耗强化石数，消耗蓝宝石数
	            	if(!equipmentMap.containsKey(_equipmentid)){
	            		equipmentMap.put(_equipmentid, new Integer[]{0,0,0,0,0});
	            	}
	            	
	            	Integer[] _tempI = equipmentMap.get(_equipmentid);
	            	//强化次数
	            	_tempI[0] += 1;
	            	
	            	if(!"0".equals(_temp[7])){
	            		//蓝宝石强化次数
	            		_tempI[1] += 1;
	            		
	            		//消耗蓝宝石数
	            		int _s = new Integer(_temp[7]);
	            		_tempI[4] += _s; 
	            	}
	            	
	            	int _p = new Integer(_temp[5]);
	            	int _st = new Integer(_temp[6]);
	            	
	            	//消耗珍珠数
	            	_tempI[2] += _p;
	            	//消耗强化石数
	            	_tempI[3] += _st; 
	            	
	                result = read.readLine();
	            }
	            
	            
	            
	            String s1 = "INSERT INTO ReinforceCalc(WeaponLevel,Users,Count,SucCount,MaxUserCount,MaxUserId,SapphireCount,SapphireUser,dtime) VALUES(?,?,?,?,?,?,?,?,?)";
	            for(String key:levelTimeMap.keySet()){
	            	if(key.indexOf("_")<0){
	            		int mmid=0;
	    	            int mmc=0;
	    	            
	    	            for(int k1:userTimeMap.get(key).keySet()){
	    	            	if(userTimeMap.get(key).get(k1)>mmc){
	    	            		mmc=userTimeMap.get(key).get(k1);
	    	            		mmid=k1;
	    	            	}
	    	            }
	    	            
	            		ConfigUtil.myPortalDb.execSQLUpdate(s1, new Object[]{key,userTimeMap.get(key)==null?0:userTimeMap.get(key).size(),levelTimeMap.get(key),levelTimeMap.get(key+"_Success")==null?0:levelTimeMap.get(key+"_Success"),mmc,mmid,levelTimeMap.get(key+"_Sapphire")==null?0:levelTimeMap.get(key+"_Sapphire"),userCreditTimeMap.get(key)==null?0:userCreditTimeMap.get(key).size(),dataTime});
	            	}
	            }
	            
	           String s2 = "INSERT INTO EquipmentConsume(EquipmentID,ReinforceCount,SapphireCount,PearlNum,StoneNum,SapphireNum,dtime) VALUES (?,?,?,?,?,?,?)";
	          
	           for(int eqid:equipmentMap.keySet()){
	        	   Integer[] _tempeq = equipmentMap.get(eqid);
	        	   ConfigUtil.myPortalDb.execSQLUpdate(s2, new Object[]{eqid,_tempeq[0],_tempeq[1],_tempeq[2],_tempeq[3],_tempeq[4],dataTime});
	           }
			}
		} catch (Exception e) {	
			e.printStackTrace();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	
	}
	
	public static void calcReinforceConsume2(Date dataTime){
		BufferedReader br = null;
		List<String[]> allList = new ArrayList<String[]>();
		List<String[]> comList = new ArrayList<String[]>();
		List<String[]> advList = new ArrayList<String[]>();
		List<String[]> supList = new ArrayList<String[]>();
		try{
			String fileUrl = ConfigUtil.OaLogPath+"trace.log."+sdf.format(dataTime)+"*";
			if(fileUrl!=null){
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=Reinforce, "+fileUrl+"|awk -F '[=,]' '{print $3\",\"$7\",\"$11\",\"$13\",\"$17\",\"$21\",\"$23\",\"$25\",\"$NF}'"};
				//monetId,equipmentTypeId,quality,beforeLevel,result,pearlCount,stoneCount,sapphireCount
				System.out.println("---------------------------startProcess:"+"grep action=Reinforce, "+fileUrl+"|awk -F '[=,]' '{print $3\",\"$7\",\"$11\",\"$13\",\"$17\",\"$21\",\"$23\",\"$25\",\"$NF}'");
				Process process = Runtime.getRuntime().exec(cmdArray);
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	String[] _temp = result.split(",");
	            	allList.add(_temp);
	            	if("commonReinforceStone".equals(_temp[8])){
	            		comList.add(_temp);
	            	}else if("advancedReinforceStone".equals(_temp[8])){
	            		advList .add(_temp);
	            	}else if("superReinforceStone".equals(_temp[8])){
	            		supList.add(_temp);
	            	}
	                result = read.readLine();
	            }
			}
		} catch (Exception e) {	
			e.printStackTrace();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
			}
		}
		
		insertDB(allList,"all",dataTime);
		insertDB(comList,"commonReinforceStone",dataTime);
		insertDB(advList,"advancedReinforceStone",dataTime);
		insertDB(supList,"superReinforceStone",dataTime);
	}
	
	public static void insertDB(List<String[]> list,String type,Date dataTime){
			Map<String,Map<Integer,Integer>> userTimeMap = new HashMap<String,Map<Integer,Integer>>();
			Map<String,Map<Integer,Integer>> userCreditTimeMap =new HashMap<String,Map<Integer,Integer>>();
			Map<String,Integer> levelTimeMap = new HashMap<String,Integer>();
			
			levelTimeMap.put("ALL", 0);
			levelTimeMap.put("ALL_Success", 0);
			levelTimeMap.put("ALL_Sapphire", 0);
			
			userTimeMap.put("ALL", new HashMap<Integer,Integer>());
			userCreditTimeMap.put("ALL", new HashMap<Integer,Integer>());
			
			Map<Integer,Integer[]> equipmentMap = new HashMap<Integer,Integer[]>();
			
				
				
	            if(list != null && list.size() > 0) {
	            	for(String[] _temp : list ){
	            	
	            	//------------每个用户的强化次数&&总强化人数
	            	int _monetid = new Integer(_temp[0]);
	            	int _equipmentid = new Integer(_temp[1]);
	            	if(userTimeMap.get("ALL").containsKey(_monetid)){
	            		int _time = userTimeMap.get("ALL").get(_monetid);
	            		_time +=1;
	            		userTimeMap.get("ALL").put(_monetid, _time);
	            	}else{
	            		userTimeMap.get("ALL").put(_monetid, 1);
	            	}
	            	
	            	//------------各等级强化次数(强化前等级)&&总强化次数
	            	int _tt = levelTimeMap.get("ALL");
	            	_tt +=1;
	            	levelTimeMap.put("ALL",_tt);
	            	if(levelTimeMap.containsKey(_temp[3])){
	            		int _time = levelTimeMap.get(_temp[3]);
	            		_time +=1;
	            		levelTimeMap.put(_temp[3], _time);
	            	}else{
	            		levelTimeMap.put(_temp[3],1);
	            	}
	            	
	            	
	            	if(!userTimeMap.containsKey(_temp[3])){
	            		userTimeMap.put(_temp[3],new HashMap<Integer,Integer>());
	            	}
	            	
	            	Map<Integer,Integer> _tempMap = userTimeMap.get(_temp[3]);
	            	if(_tempMap.containsKey(_monetid)){
	            		int _time = _tempMap.get(_monetid);
	            		_time +=1;
	            		_tempMap.put(_monetid, _time);
	            	}else{
	            		_tempMap.put(_monetid, 1);
	            	}
	            	userTimeMap.put(_temp[3],_tempMap);
	            	
	            	//------------各等级强化成功次数(强化前等级)&&总强化成功次数
	            	if("suc".equals(_temp[4])){
	            		int _tts = levelTimeMap.get("ALL_Success");
	            		_tts +=1;
		            	levelTimeMap.put("ALL_Success",_tts);
		            	if(levelTimeMap.containsKey(_temp[3]+"_Success")){
		            		int _time = levelTimeMap.get(_temp[3]+"_Success");
		            		_time +=1;
		            		levelTimeMap.put(_temp[3]+"_Success", _time);
		            	}else{
		            		levelTimeMap.put(_temp[3]+"_Success",1);
		            	}
	            	}
	            	
	            	//------------各等级蓝宝石强化(强化前等级)&&总蓝宝石强化次数&&总蓝宝石强化人数
	            	if(!"0".equals(_temp[7])){
	            		userCreditTimeMap.get("ALL").put(_monetid, 0);
	            		int _tts1 = levelTimeMap.get("ALL_Sapphire");
	            		_tts1 +=1;
		            	levelTimeMap.put("ALL_Sapphire",_tts1);
		            	if(levelTimeMap.containsKey(_temp[3]+"_Sapphire")){
		            		int _time = levelTimeMap.get(_temp[3]+"_Sapphire");
		            		_time +=1;
		            		levelTimeMap.put(_temp[3]+"_Sapphire", _time);
		            	}else{
		            		levelTimeMap.put(_temp[3]+"_Sapphire",1);
		            	}
		            	
		            	if(!userCreditTimeMap.containsKey(_temp[3])){
		            		userCreditTimeMap.put(_temp[3],new HashMap<Integer,Integer>());
		            	}
		            	
		            	Map<Integer,Integer> _tempMap1 = userCreditTimeMap.get(_temp[3]);
		            	_tempMap1.put(_monetid, 0);
		            	userCreditTimeMap.put(_temp[3],_tempMap1);
		            	
	            	}
	            	
	            	//---------根据装备ID统计:强化次数，蓝宝石强化次数，消耗珍珠数，消耗强化石数，消耗蓝宝石数
	            	if(!equipmentMap.containsKey(_equipmentid)){
	            		equipmentMap.put(_equipmentid, new Integer[]{0,0,0,0,0});
	            	}
	            	
	            	Integer[] _tempI = equipmentMap.get(_equipmentid);
	            	//强化次数
	            	_tempI[0] += 1;
	            	
	            	if(!"0".equals(_temp[7])){
	            		//蓝宝石强化次数
	            		_tempI[1] += 1;
	            		
	            		//消耗蓝宝石数
	            		int _s = new Integer(_temp[7]);
	            		_tempI[4] += _s; 
	            	}
	            	
	            	int _p = new Integer(_temp[5]);
	            	int _st = new Integer(_temp[6]);
	            	
	            	//消耗珍珠数
	            	_tempI[2] += _p;
	            	//消耗强化石数
	            	_tempI[3] += _st; 
	            	
	            }
	            
	            }
	            
	            String s1 = "INSERT INTO ReinforceCalc_new(WeaponLevel,Users,Count,SucCount,MaxUserCount,MaxUserId,SapphireCount,SapphireUser,dtime,type) VALUES(?,?,?,?,?,?,?,?,?,?)";
	            for(String key:levelTimeMap.keySet()){
	            	if(key.indexOf("_")<0){
	            		int mmid=0;
	    	            int mmc=0;
	    	            
	    	            for(int k1:userTimeMap.get(key).keySet()){
	    	            	if(userTimeMap.get(key).get(k1)>mmc){
	    	            		mmc=userTimeMap.get(key).get(k1);
	    	            		mmid=k1;
	    	            	}
	    	            }
	    	            
	            		ConfigUtil.myPortalDb.execSQLUpdate(s1, new Object[]{key,userTimeMap.get(key)==null?0:userTimeMap.get(key).size(),levelTimeMap.get(key),levelTimeMap.get(key+"_Success")==null?0:levelTimeMap.get(key+"_Success"),mmc,mmid,levelTimeMap.get(key+"_Sapphire")==null?0:levelTimeMap.get(key+"_Sapphire"),userCreditTimeMap.get(key)==null?0:userCreditTimeMap.get(key).size(),dataTime,type});
	            	}
	            }
	            
	           String s2 = "INSERT INTO EquipmentConsume_new(EquipmentID,ReinforceCount,SapphireCount,PearlNum,StoneNum,SapphireNum,dtime,type) VALUES (?,?,?,?,?,?,?,?)";
	          
	           for(int eqid:equipmentMap.keySet()){
	        	   Integer[] _tempeq = equipmentMap.get(eqid);
	        	   ConfigUtil.myPortalDb.execSQLUpdate(s2, new Object[]{eqid,_tempeq[0],_tempeq[1],_tempeq[2],_tempeq[3],_tempeq[4],dataTime,type});
	           }
	
	}
	
	public static void main(String[] args){
		try{
			Date stime = sdf.parse("2013-08-28");
			calcReinforceConsume2(stime);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
