package both.calc;

import both.basic.ConfigUtil;
import both.basic.Pet;

public class PetCalc {
	private static void calcPetLvUser(){
		int startLevel[]={1,21,26,31,36,41,46,61};
		int endLevel[]={20,30,35,40,45,50,70,100};
		
		String server[]={"Server1","Server2","Server3","Server4","Server5","Server6"};
		
		for(String s :server){
			ConfigUtil.myPortalDb.execSQLUpdate("delete petLvUser where server = ? ", new Object[]{s});
			for(int i=0;i<8;i++){
				int _n = Pet.calcPetLvUser(startLevel[i],endLevel[i],s);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into petLvUser(arenaId,users,server) values (?,?,?)", new Object[]{i+1,_n,s});
			}
		}
	}
	
	
	public static void main(String args[]){
		PetCalc.calcPetLvUser();
	}
}
