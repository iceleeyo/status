package both.basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dau {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Map<Integer, String> getSever2Dau(Date date) {
		try {
			Map<Integer, String> resultMap = new HashMap<Integer, String>();

			String s2DauPath = ConfigUtil.server2DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br1 = new BufferedReader(new FileReader(s2DauPath));
			String temp = br1.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br1.readLine();
			}
			br1.close();

			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, String> getSever1Dau(Date date, Map<Integer, String> server2DauMap) {
		try {
			Map<Integer, String> resultMap = new HashMap<Integer, String>();

			String s1DauPath = ConfigUtil.server1DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br1 = new BufferedReader(new FileReader(s1DauPath));
			String temp = br1.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br1.readLine();
			}
			br1.close();
			if (server2DauMap != null) {
				Set<Integer> _s = server2DauMap.keySet();
				for (int k : _s) {
					resultMap.remove(new Integer(k));
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, String> getSever3Dau(Date date, Map<Integer, String> server2DauMap) {
		try {
			Map<Integer, String> resultMap = new HashMap<Integer, String>();

			String s3DauPath = ConfigUtil.server3DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br3 = new BufferedReader(new FileReader(s3DauPath));
			String temp = br3.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br3.readLine();
			}
			br3.close();
			if (server2DauMap != null) {
				Set<Integer> _s = server2DauMap.keySet();
				for (int k : _s) {
					resultMap.remove(new Integer(k));
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, String> getAllDau(Date date) {
		try {
			Map<Integer, String> resultMap = new HashMap<Integer, String>();

			String s1DauPath = ConfigUtil.server1DauPath + "/" + sdf.format(date) + "_newoa.txt";
			BufferedReader br1 = new BufferedReader(new FileReader(s1DauPath));
			String temp = br1.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br1.readLine();
			}
			br1.close();
			String s2DauPath = ConfigUtil.server2DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br2 = new BufferedReader(new FileReader(s2DauPath));
			temp = br2.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br2.readLine();
			}
			br2.close();

			String s3DauPath = ConfigUtil.server3DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br3 = new BufferedReader(new FileReader(s3DauPath));
			temp = br3.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br3.readLine();
			}
			br3.close();

			String s4DauPath = ConfigUtil.server4DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br4 = new BufferedReader(new FileReader(s4DauPath));
			temp = br4.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br4.readLine();
			}
			br4.close();

			String s5DauPath = ConfigUtil.server5DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br5 = new BufferedReader(new FileReader(s5DauPath));
			temp = br5.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br5.readLine();
			}
			br5.close();

			String s6DauPath = ConfigUtil.server6DauPath + "/" + sdf.format(date) + "_newoa.txt";

			BufferedReader br6 = new BufferedReader(new FileReader(s6DauPath));
			temp = br6.readLine();
			while (temp != null) {
				resultMap.put(Integer.parseInt(temp), "");
				temp = br6.readLine();
			}
			br6.close();

			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

}
