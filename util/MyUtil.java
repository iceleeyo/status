package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

public class MyUtil {
	static final Logger logger = Logger.getLogger(MyUtil.class);
	static ArrayList<String> testAccount = new ArrayList<String>();
	static{
	}
	
	public static Connection getConnection(){
		Connection con = null; 
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			String db81Url = serverConf.getString("dbReadUrls");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = db81Url;
			con = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void toDo(ArrayList<String> sqlList){
		Connection con = getConnection();  
        Statement stmt = null;  
        ResultSet rs = null;  
        try {  
            stmt = con.createStatement();  
            int num = 0;
            con.setAutoCommit(false);
            for(String sql:sqlList){
                 stmt.addBatch(sql); 
                 num++;
                 if(num==1000){
                	 stmt.executeBatch();
                	 con.commit();    
                     num = 0;
                     stmt.clearBatch();
                 }
            }
            if(num>0){
            	stmt.executeBatch();
           	 con.commit();    
                num = 0;
                stmt.clearBatch();
            }
           
        }catch (SQLException se) {  
            logger.error("",se);
        } finally {  
            try {  
                if (rs != null) {  
                    rs.close();  
                    rs = null;  
                }  
                if (stmt != null) {  
                    stmt.close();  
                    stmt = null;  
                }  
                if (con != null) {  
                    con.close();  
                    con = null;  
                }  
            } catch (SQLException se) {  
                se.printStackTrace();  
            }  
        }  
    }
	
	static public boolean isTestAccount(String monetid){
		boolean flag = false;
		if(testAccount.contains(monetid)){
			flag = true;
		}else if(Integer.parseInt(monetid)<50000){
			flag = true;
		}
		return flag;
	}
	
	public static Date StringToDate(String dateString){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String DateToString(Date date){
		String datestring = "";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		datestring = df.format(date);
		return datestring;
	}
	
	public static ArrayList<Integer> getIdList(String path){
		BufferedReader br;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			temp = br.readLine();
			while (temp != null) {
				if(MyUtil.isMonetid(temp)){
					if(!idList.contains(Integer.parseInt(temp))){
						idList.add(Integer.parseInt(temp));
					}
				}
				temp = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idList;
	}
	
	public static ArrayList<Integer> getTestAccount(){
		String path = "data/SubsidiaryAccounts.txt";
		BufferedReader br;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			temp = br.readLine();
			while (temp != null) {
				if(MyUtil.isMonetid(temp)){
					if(!idList.contains(Integer.parseInt(temp))){
						idList.add(Integer.parseInt(temp));
					}
				}
				temp = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idList;
	}
	
	static public String getValueOfKey(String log,String key,String sign) {
		if(log!=null&&key!=null){
			if(log.length()>0&&key.length()>0){
				if(log.contains(key)){
					int start = 0;
					int end = 0;
					start = log.indexOf(key) + key.length()+1;
					log = log.substring(start);
					if(log.indexOf(sign) != -1){
						end = log.indexOf(sign);
						log = log.substring(0,end);
					}
					return log.replace("\t","");
				}
			}
		}
		return null;
	}
	
	/**
     * to get the mid value of the list<Double>
     */
	public static Double getMid(List<Integer> list2){
		try{
			ArrayList<Double> list = new ArrayList<Double>();
			for(int i = 0;i<list2.size();i++){
				double num = Double.parseDouble(list2.get(i).toString());
				list.add(num);
			}
			if(list.size()>0){
				if(list.size()%2==0){
					if(((list.get(list.size()/2)+list.get(list.size()/2-1))/2)%2==1){
						return ((list.get(list.size()/2)+list.get(list.size()/2-1))/2);
					}else{
						return ((list.get(list.size()/2)+list.get(list.size()/2-1))/2);
					}
								
				}else{
					return list.get(list.size()/2);					
				}
			}else{
				return null;
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * to get the avg value of the list
     */
	public static Double getAvg(List<Integer> list){
		if(list!=null){
			try{
				if(list.size()>0){
					int amount = 0;
					for(int i = 0;i<list.size();i++){
						double num = Double.parseDouble(list.get(i).toString());
						amount += num;
					}
					return amount*1.0/list.size();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return 0.0;
	}
	
	public static void sendEmail(String title,String msg){
		HtmlEmail email = new HtmlEmail();
	    try {
	    	email.setHostName("smtp.163.com");
	    	email.setCharset("gbk");
			// 收件人的邮箱
			email.addTo("game_operating@mozat.com<game_operating@mozat.com>");
			// 发送人的邮箱
			email.setFrom("oaprosa@163.com");
			email.setAuthentication("oaprosa@163.com", "asorpao");
			email.setSubject(title);
			// 要发送的信息
			email.setMsg(msg);
			// 发送
			email.send();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	}
	
	public static void sendEmailToPC(String title,String msg){
		HtmlEmail email = new HtmlEmail();
	    try {
	    	email.setHostName("smtp.163.com");
	    	email.setCharset("gbk");
			// 收件人的邮箱
			email.addTo("huangjiangping@mozat.com");
			// 发送人的邮箱
			email.setFrom("oaprosa@163.com");
			email.setAuthentication("oaprosa@163.com", "asorpao");
			email.setSubject(title);
			// 要发送的信息
			email.setMsg(msg);
			// 发送
			email.send();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	}
	
	public static boolean isMonetid(String monetid){
		Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence)monetid);
        if(matcher.matches()){
        	return true;
        }else{
        	return false;
        }
	}
}
