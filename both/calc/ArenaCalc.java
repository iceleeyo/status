package both.calc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBResultSet;
import both.basic.Arena;
import both.basic.ConfigUtil;

public class ArenaCalc {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static void calcDailyChallange(Date stime){
		try{
			Map<Integer,Integer[]> serverDataMap = Arena.getDailyArenaChallangeByServer(stime);
			
			StringBuffer sb = new StringBuffer("");
			for(int id : serverDataMap.keySet()){
				sb.append(id+",");
			}
			
			String s1UserList = sb.toString();
			s1UserList = s1UserList.substring(0, s1UserList.length()-1);
			
			
			Map<Integer,List<Integer>> sLevelMap = new HashMap<Integer, List<Integer>>();
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			c.set(Calendar.HOUR, 5);
			
			DBResultSet ds = ConfigUtil.myPortalDb.execSQL("select max(petlevel) petlevel,monetid  from petupgrade_new where  utime < ? and monetid in ("+s1UserList+") group by monetid", new Object[]{c.getTime()});
			while(ds.next()){
				int _pl = ds.getInt("petlevel");
				int _monetid = ds.getInt("monetid");
				
				List<Integer> _temp = sLevelMap.get(_pl);
				if(_temp==null){
					_temp = new ArrayList<Integer>();
				}
				_temp.add(_monetid);
				sLevelMap.put(_pl, _temp);
			}
			
			
			
			for(int level : sLevelMap.keySet()){
				List<Integer> _lv = sLevelMap.get(level);
				Integer[] _winTimes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				Integer[] _userCount = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				Integer[] _challengeTimes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				for(int id:_lv){
					Integer[] _userData = serverDataMap.get(id);
					if(_userData!=null&&_userData.length==3){
						int _ctime = _userData[2];
						_userCount[0]+=1;
						_winTimes[0]+=_userData[1];
						_challengeTimes[0]+=_ctime;
						_winTimes[_userData[2]]+=_userData[1];
						_userCount[_userData[2]]+=1;
						_challengeTimes[_userData[2]]+=_ctime;
					}
				}
				
				for(int i=1;i<_winTimes.length;i++){
					if(_challengeTimes[i]>0)
						ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaChallenge(petLevel,challengeCount,users,winTime,challengeTime,dtime) values(?,?,?,?,?,?)", new Object[]{level,i,_userCount[i],_winTimes[i],_challengeTimes[i],stime});
				}
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void generateTop100Detail(Date date){
		try{
			DBResultSet ds = ConfigUtil.myServerDb.execSQL("select distinct arenaTypeid from ArenaRanking", new Object[]{});
			List<Integer> arenaList = new ArrayList<Integer>();
			while(ds.next()){
				arenaList.add(ds.getInt("arenaTypeid"));
			}
			
			String[] oakey = {"PetLevel","StarLevel","PetAttack","PetDefense","StarAttack","StarDefense","ShipLife","EquipmentAttack","EquipmentDefense","EquipmentLife","ReinforceAttack","ReinforceDefense","ReinforceLife"};
			
			
			double [] tribeConfig = {0.05, 0.07, 0.1, 0.15, 0.25, 0.4};
			Map<Integer,Integer> shipConfig = new HashMap<Integer,Integer>();
			Map<Integer,Integer[]> equipmentConfig = new HashMap<Integer,Integer[]>();
			Map<Integer,Integer[]> reinforceConfig = new HashMap<Integer,Integer[]>();
			
			//配置信息
			ds = ConfigUtil.myConfigDb.execSQLQuery("select id,hp from shiptype", new Object[]{});
			while(ds.next()){
				shipConfig.put(ds.getInt("id"), ds.getInt("hp"));
			}
			
			ds = ConfigUtil.myConfigDb.execSQLQuery("select id type,attack,defense,life from EquipmentType", new Object[]{});
			while(ds.next()){
				Integer[] _temp = {ds.getInt("attack"),ds.getInt("defense"),ds.getInt("life")};
				equipmentConfig.put(ds.getInt("type"), _temp);
			}
			
			
			ds = ConfigUtil.myConfigDb.execSQLQuery("select equipment_type_id type,attack,defence defense,life from EquipmentReinforce", new Object[]{});
			while(ds.next()){
				Integer[] _temp = {ds.getInt("attack"),ds.getInt("defense"),ds.getInt("life")};
				reinforceConfig.put(ds.getInt("type"), _temp);
			}
			
			//具体数据
			for(int _arenaid : arenaList){
				System.out.println("[ArenaID]"+_arenaid);
				Map<Integer,Integer> posMap = new HashMap<Integer,Integer>();
				Map<String,Double> dataMap = new HashMap<String,Double>(); 
				
				ds = ConfigUtil.myServerDb.execSQL("select TOP 100 userid,position FROM ArenaRanking where arenaTypeid = ? order by position", new Object[]{_arenaid});
				while(ds.next()){
					posMap.put(ds.getInt("userid"),ds.getInt("position"));
				}
				
				StringBuffer sb = new StringBuffer("");
				for(int id : posMap.keySet()){
					sb.append(id+",");
				}
				
				String idList = sb.toString();
				if(idList.length()>=1){
					idList = idList.substring(0,idList.length()-1);
				}
				if(idList.length()>0){
					
					//宠物等级/星等/宠物攻击/防御/星等攻击/防御
					ds = ConfigUtil.myServerDb.execSQLQuery("select ownerid monetid,petLevel,starLevel,attack,defense,starAttack,starDefense from pet where ownerid in ("+idList+")", new Object[]{});
					while(ds.next()){
						int _monetid = ds.getInt("monetid");
						dataMap.put(_monetid+"_PetLevel",new Double(ds.getInt("petLevel")));
						dataMap.put(_monetid+"_StarLevel",new Double(ds.getInt("starLevel")));
						dataMap.put(_monetid+"_PetAttack",new Double(ds.getInt("attack")));
						dataMap.put(_monetid+"_PetDefense",new Double(ds.getInt("defense")));
						dataMap.put(_monetid+"_StarAttack",new Double(ds.getInt("starAttack")));
						dataMap.put(_monetid+"_StarDefense",new Double(ds.getInt("starDefense")));
					}
					
					//船的生命值 ,最后要*家族的加成
					ds = ConfigUtil.myServerDb.execSQLQuery("select type,ownerid monetid from ship where ownerid in("+idList+")", new Object[]{});
					
					while(ds.next()){
						int _monetid = ds.getInt("monetid");
						int _type = ds.getInt("type");
						
						if(!dataMap.containsKey(_monetid+"_ShipLife")){
							dataMap.put(_monetid+"_ShipLife",new Double(0));
						}
						
						double _life = dataMap.get(_monetid+"_ShipLife");
						_life += shipConfig.get(_type);
						
						dataMap.put(_monetid+"_ShipLife",_life);
					}
					
					ds = ConfigUtil.myServerDb.execSQLQuery("select monetid,tribelevel from (select * from member where tid  >0 and title > 0 and monetid in("+idList+"))  m left join tribe on m.tid = tribe.id", new Object[]{});
					while(ds.next()){
						int _monetid = ds.getInt("monetid");
						int _tribelevel = ds.getInt("tribelevel");
						if(dataMap.containsKey(_monetid+"_ShipLife")){
							double _life = dataMap.get(_monetid+"_ShipLife");
							_life = _life*(1+tribeConfig[(_tribelevel-1)]);
							dataMap.put(_monetid+"_ShipLife",_life);
						}
					}
					
					
					//装备和装备强化
					ds = ConfigUtil.myServerDb.execSQLQuery("select monet_id monetid,type_id type,reinforce_count reinforceCount  from equipment where status =1 and type_id >=5 and monet_id in ("+idList+")", new Object[]{});
					
					while(ds.next()){
						int _monetid = ds.getInt("monetid");
						int _type = ds.getInt("type");
						int _reinforceCount = ds.getInt("reinforceCount");
						
						if(!dataMap.containsKey(_monetid+"_EquipmentAttack")){
							dataMap.put(_monetid+"_EquipmentAttack",new Double(0));
						}
						double _EquipmentAttack = dataMap.get(_monetid+"_EquipmentAttack");
						
						if(!dataMap.containsKey(_monetid+"_EquipmentDefense")){
							dataMap.put(_monetid+"_EquipmentDefense",new Double(0));
						}
						double _EquipmentDefense = dataMap.get(_monetid+"_EquipmentDefense");
						
						if(!dataMap.containsKey(_monetid+"_EquipmentLife")){
							dataMap.put(_monetid+"_EquipmentLife",new Double(0));
						}
						double _EquipmentLife = dataMap.get(_monetid+"_EquipmentLife");
						
						if(!dataMap.containsKey(_monetid+"_ReinforceAttack")){
							dataMap.put(_monetid+"_ReinforceAttack",new Double(0));
						}
						double _ReinforceAttack = dataMap.get(_monetid+"_ReinforceAttack");
						
						if(!dataMap.containsKey(_monetid+"_ReinforceDefense")){
							dataMap.put(_monetid+"_ReinforceDefense",new Double(0));
						}
						double _ReinforceDefense = dataMap.get(_monetid+"_ReinforceDefense");
						
						if(!dataMap.containsKey(_monetid+"_ReinforceLife")){
							dataMap.put(_monetid+"_ReinforceLife",new Double(0));
						}
						double _ReinforceLife = dataMap.get(_monetid+"_ReinforceLife");
						_EquipmentAttack +=equipmentConfig.get(_type)[0];
						_EquipmentDefense +=equipmentConfig.get(_type)[1];
						_EquipmentLife +=equipmentConfig.get(_type)[2];
						
						_ReinforceAttack +=reinforceConfig.get(_type)[0]*_reinforceCount;
						_ReinforceDefense +=reinforceConfig.get(_type)[1]*_reinforceCount;
						_ReinforceLife +=reinforceConfig.get(_type)[2]*_reinforceCount;
						
						dataMap.put(_monetid+"_EquipmentAttack",_EquipmentAttack);
						dataMap.put(_monetid+"_EquipmentDefense",_EquipmentDefense);
						dataMap.put(_monetid+"_EquipmentLife",_EquipmentLife);
						dataMap.put(_monetid+"_ReinforceAttack",_ReinforceAttack);
						dataMap.put(_monetid+"_ReinforceDefense",_ReinforceDefense);
						dataMap.put(_monetid+"_ReinforceLife",_ReinforceLife);
					}
				}
				
				for(int _monetid:posMap.keySet()){
					for(String _oakey :oakey){
						ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaTop100Detail (monetid,position,arenaid,dtime,oakey,oavalue) values (?,?,?,?,?,?)", new Object[]{_monetid,posMap.get(_monetid),_arenaid,date,_oakey,dataMap.get(_monetid+"_"+_oakey)});
					}
				}
				
				ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaTop100Detail(oavalue,monetid,oakey,position,arenaid,dtime) select sum(oavalue),monetid,'Defense',max(position),"+_arenaid+",'"+sdf.format(date)+"' from arenaTop100Detail where arenaid = "+_arenaid+" and dtime = '"+sdf.format(date)+"' and charindex('Defense',oakey) > 0 group by monetid",new Object[]{});
				ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaTop100Detail(oavalue,monetid,oakey,position,arenaid,dtime) select sum(oavalue),monetid,'Attack',max(position),"+_arenaid+",'"+sdf.format(date)+"' from arenaTop100Detail where arenaid = "+_arenaid+" and dtime = '"+sdf.format(date)+"' and charindex('Attack',oakey) > 0 group by monetid",new Object[]{});
				ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaTop100Detail(oavalue,monetid,oakey,position,arenaid,dtime) select sum(oavalue),monetid,'Life',max(position),"+_arenaid+",'"+sdf.format(date)+"' from arenaTop100Detail where arenaid = "+_arenaid+" and dtime = '"+sdf.format(date)+"' and charindex('Life',oakey) > 0 group by monetid",new Object[]{});
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void generateTop20UserEquipment(Date date){
		try{
			DBResultSet ds = ConfigUtil.myServerDb.execSQL("select distinct arenaTypeid from ArenaRanking order by arenaTypeid desc", new Object[]{});
			List<Integer> arenaList = new ArrayList<Integer>();
			List<Object[]> ojList =  new ArrayList<Object[]>();
			int count = 0;
			while(ds.next()&&count<2){
				arenaList.add(ds.getInt("arenaTypeid"));
				count ++;
			}
			
			for(int _arenaid : arenaList){
				System.out.println("[ArenaID]"+_arenaid);
				ds = ConfigUtil.myServerDb.execSQL("select count(*) num,monet_id,type_id from equipment where type_id > 5 and monet_id in (select TOP 20 userid FROM ArenaRanking where arenaTypeid = ? order by position) group by monet_id,type_id", new Object[]{_arenaid});
				while(ds.next()){
					ojList.add(new Object[]{ds.getInt("num"),ds.getInt("monet_id"),ds.getInt("type_id"),_arenaid,sdf.format(date)});
				}
			}
			
			for(Object[] o:ojList){
				ConfigUtil.myPortalDb.execSQLUpdate("insert into arenaTop20Equipment(amount,monetid,typeid,arenaId,ddate) values (?,?,?,?,?) ", o);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		generateTop20UserEquipment(new Date());
	}
}
