package resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class resourceUpdate_Time {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH");
	static StringBuffer fileList = new StringBuffer("");
	static StringBuffer fileList2 = new StringBuffer("");
	static{
		try {
			String fileUrl = "/var/log/gamefeedback/gameServerSys.log";
			String fileUrl2 = "/home/mozat/morange/oapro/OceanAgelogs/newOA_Request.log";
			Date date1 = sdf1.parse("2013-11-06-12");
			Date date2 = sdf1.parse("2013-11-07-10");
			
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			
			
			
			while(c.getTimeInMillis()<date2.getTime()){
				String _fileName = fileUrl+"."+sdf1.format(c.getTime());
				System.out.println(_fileName);
				fileList.append(_fileName+"* ");
				
				_fileName = fileUrl2+"."+sdf1.format(c.getTime());
				System.out.println(_fileName);
				fileList2.append(_fileName+"* ");
				
				c.add(Calendar.HOUR, 1);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//登陆的用户
	private static List<String> getLoginMonetid(Date stime){
		BufferedReader br = null;
		String date = sdf.format(stime);
		try{
			List<String> list = new ArrayList<String>();
			
			
			String cmd = "grep Action=ResourceFile "+fileList.toString()+ "|awk '{print $13}'|sort -b|uniq -c|sed -e 's/[]]//g'|sed -e 's/[[]//g'|awk '{print $2}'";
			String []cmdArray = new String[]{"/bin/sh", "-c", cmd};
			
			System.out.println("---------------------------startProcess:"+cmd);
			Process process = Runtime.getRuntime().exec(cmdArray);
			
			//process.waitFor();  
			InputStream  in = process.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String result = read.readLine(); 
			while (result != null) {
				list.add(result);
				result = read.readLine();
			}
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//请求支援更新>0的用户
	private static List<String> getUpdateResourceMonetid(Date stime){
		BufferedReader br = null;
		String date = sdf.format(stime);
		try{
			List<String> list = new ArrayList<String>();
			
				
				String cmd = "grep Action=ResourceFile "+fileList.toString()+ "|grep -v  ResourceNumber=0|awk '{print $13}'|sort -b|uniq -c|sed -e 's/[]]//g'|sed -e 's/[[]//g'|awk '{print $2}'";
				String []cmdArray = new String[]{ "/bin/sh", "-c", cmd};
				
				System.out.println("---------------------------startProcess:"+cmd);
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	list.add(result);
	                result = read.readLine();
	            }
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//资源更新成功的用户
	private static List<String> getUpdateResourceSuccessMonetid(Date stime){
		BufferedReader br = null;
		String date = sdf.format(stime);
		try{
			List<String> list = new ArrayList<String>();
			
				
				String cmd = "grep Action=UpdateResourceFile "+fileList.toString()+ "|grep -v  ResourceNumber=0|awk '{print $13}'|sort -b|uniq -c|sed -e 's/[]]//g'|sed -e 's/[[]//g'|awk '{print $2}'";
				String []cmdArray = new String[]{ "/bin/sh", "-c", cmd};
				
				System.out.println("---------------------------startProcess:"+cmd);
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	list.add(result);
	                result = read.readLine();
	            }
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//登陆成功的用户
	private static List<String> getLoginSuccessMonetid(Date stime){
		BufferedReader br = null;
		String date = sdf.format(stime);
		try{
			List<String> list = new ArrayList<String>();
			
				
				String cmd = "grep -v Realm.QueryAction "+fileList2.toString()+ "|awk '{print $8}'|sort -u ";
				String []cmdArray = new String[]{ "/bin/sh", "-c", cmd};
				
				System.out.println("---------------------------startProcess:"+cmd);
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	list.add(result);
	                result = read.readLine();
	            }
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//所有的fisher
	private static List<String> getFisher(){
		try{
			List<String> list = new ArrayList<String>();
				String sql = "select monetid from fisher";
				DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{});
				while(ds.next()){
					try {
						list.add(ds.getInt("monetid")+"");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}
	//每个平台对应的用户
	private static Map<String,List<String>> getmonetidAndPlatfrom(){
		Map<String,List<String>>  map = new HashMap<String,List<String>>();
		try{
			String sql = "select monetid,platform from All_Users";
			List<String> bbList = new ArrayList<String>();
			List<String> j2meList = new ArrayList<String>();
			List<String> androidList = new ArrayList<String>();
			DBResultSet ds = ConfigUtil.myPortalDb.execSQL(sql, new Object[]{});
			while(ds.next()){
				try {
					String platform = ds.getString("platform");
					if("BB".equals(platform)){
						bbList.add(ds.getInt("monetid")+"");
					}else if("J2ME".equals(platform)){
						j2meList.add(ds.getInt("monetid")+"");
					}else if("ANDROID".equals(platform)){
						androidList.add(ds.getInt("monetid")+"");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			map.put("BB", bbList);
			map.put("J2ME", j2meList);
			map.put("Android", androidList);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}
	public static void main(String[] args) {
		
		Date date  = null;;
		try {
			date = sdf.parse(args[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> loginList = getLoginMonetid(date);
		List<String> updateResource = getUpdateResourceMonetid(date);
		List<String> updateResourceSuccess =  getUpdateResourceSuccessMonetid(date);
		List<String> loginSuccess = getLoginSuccessMonetid(date);
		
		System.out.println("-----1111----");
		System.out.println(loginList.size());
		System.out.println(updateResource.size());
		System.out.println(updateResourceSuccess.size());
		System.out.println(loginSuccess.size());
		
		List<String> fisher = getFisher();
		Map<String,List<String>>  map = getmonetidAndPlatfrom();
		List<String> bbList = map.get("BB");
		List<String> j2meList = map.get("J2ME");
		List<String> androidList = map.get("Android");
		
		System.out.println("----begin---");
		System.out.println(fisher.size());
		System.out.println(bbList.size());
		System.out.println(j2meList.size());
		System.out.println(androidList.size());
		
		bbList.retainAll(fisher);
		j2meList.retainAll(fisher);
		androidList.retainAll(fisher);
		
		List<String>  bbUpdate  = new ArrayList<String>(bbList);
		bbUpdate.retainAll(updateResource);
		
		List<String>  bbUpdateSuccess  = new ArrayList<String>(bbList);
		bbUpdateSuccess.retainAll(updateResource);
		bbUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  bbUpdateLoginSuccess  = new ArrayList<String>(bbList);
		bbUpdateLoginSuccess.retainAll(updateResource);
		bbUpdateLoginSuccess.retainAll(updateResourceSuccess);
		bbUpdateLoginSuccess.retainAll(loginList);
		bbUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  bblogin  = new ArrayList<String>(bbList);
		bblogin.retainAll(loginList);
		
		List<String>  bbloginSuccess  = new ArrayList<String>(bbList);
		bbloginSuccess.retainAll(loginList);
		bbloginSuccess.retainAll(loginSuccess);
		
		System.out.println("---bb----");
		System.out.println(bbUpdate.size());
		System.out.println(bbUpdateSuccess.size());
		System.out.println(bbUpdateLoginSuccess.size());
		System.out.println(bblogin.size());
		System.out.println(bbloginSuccess.size());
		
		insertDB(bbUpdate.size(),bbUpdateSuccess.size(),bbUpdateLoginSuccess.size(),bblogin.size(),bbloginSuccess.size(),"BB",date);
		
		List<String>  j2meUpdate  = new ArrayList<String>(j2meList);
		j2meUpdate.retainAll(updateResource);
		
		List<String>  j2meUpdateSuccess  = new ArrayList<String>(j2meList);
		j2meUpdateSuccess.retainAll(updateResource);
		j2meUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  j2meUpdateLoginSuccess  = new ArrayList<String>(j2meList);
		j2meUpdateLoginSuccess.retainAll(updateResource);
		j2meUpdateLoginSuccess.retainAll(updateResourceSuccess);
		j2meUpdateLoginSuccess.retainAll(loginList);
		j2meUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  j2melogin  = new ArrayList<String>(j2meList);
		j2melogin.retainAll(loginList);
		
		List<String>  j2meloginSuccess  = new ArrayList<String>(j2meList);
		j2meloginSuccess.retainAll(loginList);
		j2meloginSuccess.retainAll(loginSuccess);
		
		System.out.println("---jeme----");
		System.out.println(j2meUpdate.size());
		System.out.println(j2meUpdateSuccess.size());
		System.out.println(j2meUpdateLoginSuccess.size());
		System.out.println(j2melogin.size());
		System.out.println(j2meloginSuccess.size());
		insertDB(j2meUpdate.size(),j2meUpdateSuccess.size(),j2meUpdateLoginSuccess.size(),j2melogin.size(),j2meloginSuccess.size(),"J2ME",date);
		
		List<String>  androidUpdate  = new ArrayList<String>(androidList);
		androidUpdate.retainAll(updateResource);
		
		List<String>  androidUpdateSuccess  = new ArrayList<String>(androidList);
		androidUpdateSuccess.retainAll(updateResource);
		androidUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  androidUpdateLoginSuccess  = new ArrayList<String>(androidList);
		androidUpdateLoginSuccess.retainAll(updateResource);
		androidUpdateLoginSuccess.retainAll(updateResourceSuccess);
		androidUpdateLoginSuccess.retainAll(loginList);
		androidUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  androidlogin  = new ArrayList<String>(androidList);
		androidlogin.retainAll(loginList);
		
		List<String>  androidloginSuccess  = new ArrayList<String>(androidList);
		androidloginSuccess.retainAll(loginList);
		androidloginSuccess.retainAll(loginSuccess);
		System.out.println("---android----");
		System.out.println(androidUpdate.size());
		System.out.println(androidUpdateSuccess.size());
		System.out.println(androidUpdateLoginSuccess.size());
		System.out.println(androidlogin.size());
		System.out.println(androidloginSuccess.size());
		insertDB(androidUpdate.size(),androidUpdateSuccess.size(),androidUpdateLoginSuccess.size(),androidlogin.size(),androidloginSuccess.size(),"Android",date);
		
	}
	public static void resouceUpdateCalc(Date date) {
		
		List<String> loginList = getLoginMonetid(date);
		List<String> updateResource = getUpdateResourceMonetid(date);
		List<String> updateResourceSuccess =  getUpdateResourceSuccessMonetid(date);
		List<String> loginSuccess = getLoginSuccessMonetid(date);
		List<String> fisher = getFisher();
		
		Map<String,List<String>>  map = getmonetidAndPlatfrom();
		List<String> bbList = map.get("BB");
		List<String> j2meList = map.get("J2ME");
		List<String> androidList = map.get("Android");
		
		bbList.retainAll(fisher);
		j2meList.retainAll(fisher);
		androidList.retainAll(fisher);
		
		List<String>  bbUpdate  = new ArrayList<String>(bbList);
		bbUpdate.retainAll(updateResource);
		
		List<String>  bbUpdateSuccess  = new ArrayList<String>(bbList);
		bbUpdateSuccess.retainAll(updateResource);
		bbUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  bbUpdateLoginSuccess  = new ArrayList<String>(bbList);
		bbUpdateLoginSuccess.retainAll(updateResource);
		bbUpdateLoginSuccess.retainAll(updateResourceSuccess);
		bbUpdateLoginSuccess.retainAll(loginList);
		bbUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  bblogin  = new ArrayList<String>(bbList);
		bblogin.retainAll(loginList);
		
		List<String>  bbloginSuccess  = new ArrayList<String>(bbList);
		bbloginSuccess.retainAll(loginList);
		bbloginSuccess.retainAll(loginSuccess);
		
		System.out.println(bbUpdate.size());
		System.out.println(bbUpdateSuccess.size());
		System.out.println(bbUpdateLoginSuccess.size());
		System.out.println(bblogin.size());
		System.out.println(bbloginSuccess.size());
		
		insertDB(bbUpdate.size(),bbUpdateSuccess.size(),bbUpdateLoginSuccess.size(),bblogin.size(),bbloginSuccess.size(),"BB",date);
		
		List<String>  j2meUpdate  = new ArrayList<String>(j2meList);
		j2meUpdate.retainAll(updateResource);
		
		List<String>  j2meUpdateSuccess  = new ArrayList<String>(j2meList);
		j2meUpdateSuccess.retainAll(updateResource);
		j2meUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  j2meUpdateLoginSuccess  = new ArrayList<String>(j2meList);
		j2meUpdateLoginSuccess.retainAll(updateResource);
		j2meUpdateLoginSuccess.retainAll(updateResourceSuccess);
		j2meUpdateLoginSuccess.retainAll(loginList);
		j2meUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  j2melogin  = new ArrayList<String>(j2meList);
		j2melogin.retainAll(loginList);
		
		List<String>  j2meloginSuccess  = new ArrayList<String>(j2meList);
		j2meloginSuccess.retainAll(loginList);
		j2meloginSuccess.retainAll(loginSuccess);
		
		System.out.println(j2meUpdate.size());
		System.out.println(j2meUpdateSuccess.size());
		System.out.println(j2meUpdateLoginSuccess.size());
		System.out.println(j2melogin.size());
		System.out.println(j2meloginSuccess.size());
		
		insertDB(j2meUpdate.size(),j2meUpdateSuccess.size(),j2meUpdateLoginSuccess.size(),j2melogin.size(),j2meloginSuccess.size(),"J2ME",date);
		
		List<String>  androidUpdate  = new ArrayList<String>(androidList);
		androidUpdate.retainAll(updateResource);
		
		List<String>  androidUpdateSuccess  = new ArrayList<String>(androidList);
		androidUpdateSuccess.retainAll(updateResource);
		androidUpdateSuccess.retainAll(updateResourceSuccess);
		
		List<String>  androidUpdateLoginSuccess  = new ArrayList<String>(androidList);
		androidUpdateLoginSuccess.retainAll(updateResource);
		androidUpdateLoginSuccess.retainAll(updateResourceSuccess);
		androidUpdateLoginSuccess.retainAll(loginList);
		androidUpdateLoginSuccess.retainAll(loginSuccess);
		
		List<String>  androidlogin  = new ArrayList<String>(androidList);
		androidlogin.retainAll(loginList);
		
		List<String>  androidloginSuccess  = new ArrayList<String>(androidList);
		androidloginSuccess.retainAll(loginList);
		androidloginSuccess.retainAll(loginSuccess);
		
		System.out.println(androidUpdate.size());
		System.out.println(androidUpdateSuccess.size());
		System.out.println(androidUpdateLoginSuccess.size());
		System.out.println(androidlogin.size());
		System.out.println(androidloginSuccess.size());
		
		insertDB(androidUpdate.size(),androidUpdateSuccess.size(),androidUpdateLoginSuccess.size(),androidlogin.size(),androidloginSuccess.size(),"Android",date);
		
	}
	
	private static void insertDB(int resouceUpdate ,int resouceUpdateSuccess ,int UpdateLoginSuccess,int login,int loginSeccess,String from,Date date) {
		String s1 = "INSERT INTO resouceUpdate([resouceUpdate],[resouceUpdateSuccess],[updateLoginSuccess],[login],[loginSuccess],[platFrom],[rdate]) VALUES(?,?,?,?,?,?,?)";
			try {
				//ConfigUtil.myPortalDb.execSQLUpdate(s1, new Object[]{resouceUpdate,resouceUpdateSuccess,UpdateLoginSuccess,login,loginSeccess,from,date});
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
