package both.basic;

import util.DBResultSet;

public class Pet {
	
	public static int calcPetLvUser(int startLevel,int endLevel,String server){
		try{
			System.out.println(startLevel+"/"+endLevel+"/"+server);
			int returnValue = 0;
			if("Server1".equals(server)){
				DBResultSet rs = ConfigUtil.portal1db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
					System.out.println(returnValue);
				}
			}else if("Server2".equals(server)){
				DBResultSet rs = ConfigUtil.portal2db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
				}
			}else if("Server3".equals(server)){
				DBResultSet rs = ConfigUtil.portal3db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
				}
			}else if("Server4".equals(server)){
				DBResultSet rs = ConfigUtil.portal4db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
				}
			}else if("Server5".equals(server)){
				DBResultSet rs = ConfigUtil.portal5db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
				}
			}else if("Server6".equals(server)){
				DBResultSet rs = ConfigUtil.portal6db.execSQLQuery("select count(*) num from (select max(petlevel) pl,monetid from petupgrade_new group by monetid) p where pl >= ? and pl <= ?",new Object[]{startLevel,endLevel}) ;
				if(rs.next()){
					returnValue = rs.getInt("num");
				}
			}
			return returnValue;
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
