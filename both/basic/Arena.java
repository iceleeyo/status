package both.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.MyUtil;

public class Arena {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

	public static Map<Integer, Integer[]> getDailyArenaChallangeByServer(Date stime) {
		BufferedReader br = null;
		try {
			Map<Integer, Integer[]> resultMap = new HashMap<Integer, Integer[]>();
			ArrayList<Integer> testList = MyUtil.getTestAccount();
			String fileUrl = null;
			fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/action.log";

			if (fileUrl != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(stime);

				c.set(Calendar.HOUR, 5);
				StringBuffer fileList = new StringBuffer("");
				System.out.println(stime);
				System.out.println(c.getTime());

				for (int i = 1; i <= 24; i++) {
					String _fileName = fileUrl + "." + sdf.format(c.getTime());
					fileList.append(_fileName + " ");
					c.add(Calendar.HOUR, 1);
				}

				String[] cmdArray = new String[] { "/bin/sh", "-c", "grep reportBattle " + fileList.toString() + "|awk -F '[=,]' '{print $3,$NF}'|awk '{tt[$1]+=$2;cc[$1]+=1}END{for(i in tt){print i\",\"tt[i]\",\"cc[i]};}'" };

				System.out.println("---------------------------startProcess:" + "grep reportBattle " + fileList.toString() + "|awk -F '[=,]' '{print $3,$NF}'|awk '{tt[$1]+=$2;cc[$1]+=1}END{for(i in tt){print i\",\"tt[i]\",\"cc[i]};}'");
				Process process = Runtime.getRuntime().exec(cmdArray);
				// process.waitFor();
				InputStream in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();
				while (result != null) {
					String a[] = result.split(",");
					if (a.length == 3 && !testList.contains(a[0])) {
						Integer _temp[] = { new Integer(a[0]), new Integer(a[1]), new Integer(a[2]) };
						resultMap.put(new Integer(a[0]), _temp);
					}
					result = read.readLine();
				}
			}
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<Integer, Integer[]>();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	
}
