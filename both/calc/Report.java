package both.calc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import both.basic.ChannelPayment;
import both.basic.ConfigUtil;
import both.basic.NewUsers;
import both.basic.ReportPayment;
import both.basic.Users;

public class Report {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// static SimpleDateFormat sdf = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static double usdTosar = 3.745;
	static double egpTosar = 0.538;
	static int highPay = 25;
	static int lowPay = 5;

	public static void main(String args[]) {
		Date stime = null;
		Date etime = null;
		/*
		 * double sarTousd=0.267; double egpTousd=0.145;
		 */

		try {
			stime = sdf.parse(args[0]);
			etime = sdf.parse(args[1]);

			// stime = sdf.parse("2013-09-05 00:00:00");
			// etime = sdf.parse("2013-09-12 00:00:00");

			// 活跃用户数
			/* to do 改成对文件获取 */
			// User_event User
			/*
			 * Map<Integer,Integer> s1UEFisher=Users.getUEUserByServer(stime,
			 * etime, "Server1"); Map<Integer,Integer>
			 * s2UEFisher=Users.getUEUserByServer(stime, etime, "Server2");
			 */

			Map<Integer, Integer> s1UEFisher = Users.getLoginUserByServer(stime, etime, "Server1");
			Map<Integer, Integer> s2UEFisher = Users.getLoginUserByServer(stime, etime, "Server2");
			Map<Integer, Integer> s3UEFisher = Users.getLoginUserByServer(stime, etime, "Server3");
			Map<Integer, Integer> s4UEFisher = Users.getLoginUserByServer(stime, etime, "Server4");
			Map<Integer, Integer> s5UEFisher = Users.getLoginUserByServer(stime, etime, "Server5");
			Map<Integer, Integer> s6UEFisher = Users.getLoginUserByServer(stime, etime, "Server6");

			Map<Integer, Integer> allUEFisher = new HashMap<Integer, Integer>();
			allUEFisher.putAll(s1UEFisher);
			allUEFisher.putAll(s2UEFisher);
			allUEFisher.putAll(s3UEFisher);
			allUEFisher.putAll(s4UEFisher);
			allUEFisher.putAll(s5UEFisher);
			allUEFisher.putAll(s6UEFisher);

			setReportData("ActivityFisher", allUEFisher.size(), "ALL", etime, "-");
			setReportData("ActivityFisher", s1UEFisher.size(), "Server1", etime, "-");
			setReportData("ActivityFisher", s2UEFisher.size(), "Server2", etime, "-");
			setReportData("ActivityFisher", s3UEFisher.size(), "Server3", etime, "-");
			setReportData("ActivityFisher", s4UEFisher.size(), "Server4", etime, "-");
			setReportData("ActivityFisher", s5UEFisher.size(), "Server5", etime, "-");
			setReportData("ActivityFisher", s6UEFisher.size(), "Server6", etime, "-");

			Map<Integer, Integer> _allPay = ReportPayment.getPayUser(stime, etime);

			clearId(_allPay);

			Map<Integer, Integer> allRegEgyptNewFisher = NewUsers.getRegNewUsersByBilling("Egypt");
			Map<Integer, Integer> allRegInmobiNewFisher = NewUsers.getRegNewUsersByChannel("inmobi");

			Map<Integer, Integer> _allEgyptPay = ReportPayment.getEgyptPayUser(stime, etime);
			Map<Integer, Integer> _allStcPay = ReportPayment.getSTCPayUser(stime, etime);

			Map<Integer, Integer> _s1StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server1");
			clearId(_s1StcPay);
			Map<Integer, Integer> _s2StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server2");
			clearId(_s2StcPay);

			Map<Integer, Integer> _s3StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server3");
			clearId(_s3StcPay);

			Map<Integer, Integer> _s4StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server4");
			clearId(_s4StcPay);

			Map<Integer, Integer> _s5StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server5");
			clearId(_s5StcPay);

			Map<Integer, Integer> _s6StcPay = ReportPayment.getSTCPayUserByServer(stime, etime, "server6");
			clearId(_s6StcPay);

			/*
			 * Map<Integer,Integer> _s1Pay =
			 * UserPayment.getServer1PayUser(stime, etime); clearId(_s1Pay);
			 * 
			 * Map<Integer,Integer> _s2Pay =
			 * UserPayment.getServer2PayUser(stime, etime); clearId(_s2Pay);
			 */

			Map<Integer, Integer> _s1EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server1");
			Map<Integer, Integer> _s2EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server2");
			Map<Integer, Integer> _s3EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server3");
			Map<Integer, Integer> _s4EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server4");
			Map<Integer, Integer> _s5EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server5");
			Map<Integer, Integer> _s6EgyptPay = ReportPayment.getEgyptPayUserByServer(stime, etime, "server6");

			Map<Integer, Integer> _s1GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server1");
			clearId(_s1GooglePay);
			Map<Integer, Integer> _s2GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server2");
			clearId(_s2GooglePay);
			Map<Integer, Integer> _s3GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server3");
			clearId(_s3GooglePay);
			Map<Integer, Integer> _s4GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server4");
			clearId(_s4GooglePay);
			Map<Integer, Integer> _s5GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server5");
			clearId(_s5GooglePay);

			Map<Integer, Integer> _s6GooglePay = ChannelPayment.getGoogleWalletPaymentByServer(stime, etime, "Server6");
			clearId(_s6GooglePay);

			// 所有付费用户
			Map<Integer, Integer> _temp = new HashMap<Integer, Integer>();
			_temp.putAll(_allPay);
			for (int id : _s1GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s2GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s3GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s4GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s5GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s6GooglePay.keySet()) {
				_temp.remove(id);
			}

			int allPayUser = _temp.size() + _s1GooglePay.size() + _s2GooglePay.size() + _s3GooglePay.size() + _s4GooglePay.size() + _s5GooglePay.size() + _s6GooglePay.size();

			// google 付费用户
			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s1GooglePay);
			for (int id : _s2GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s3GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s4GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s5GooglePay.keySet()) {
				_temp.remove(id);
			}
			for (int id : _s6GooglePay.keySet()) {
				_temp.remove(id);
			}

			int googlePayUser = _temp.size() + _s2GooglePay.size() + _s3GooglePay.size() + _s4GooglePay.size() + _s5GooglePay.size() + _s6GooglePay.size();

			setReportData("TopupUser", allPayUser, "ALL", etime, "-");
			setReportData("TopupUser", _allStcPay.size(), "ALL", etime, "STC");
			setReportData("TopupUser", _allEgyptPay.size(), "ALL", etime, "EGYPT");
			setReportData("TopupUser", googlePayUser, "ALL", etime, "GOOGLE");

			// 所有用户付费(SAR)
			double stcPay = getPaymentInteger(_allStcPay) / 100;

			double googlePay = getPaymentInteger(_s1GooglePay) * usdTosar + getPaymentInteger(_s2GooglePay) * usdTosar + getPaymentInteger(_s3GooglePay) * usdTosar + getPaymentInteger(_s4GooglePay) * usdTosar + getPaymentInteger(_s5GooglePay) * usdTosar + getPaymentInteger(_s6GooglePay) * usdTosar;

			double egyptPay = getPaymentInteger(_allEgyptPay) / 100 * egpTosar;
			double allPay = stcPay + egyptPay + googlePay;

			setReportData("Topup", allPay, "ALL", etime, "-");
			setReportData("Topup", stcPay, "ALL", etime, "STC");
			setReportData("Topup", egyptPay, "ALL", etime, "EGYPT");
			setReportData("Topup", googlePay, "ALL", etime, "GOOGLE");

			// 所有用户ARPPU
			setReportData("ARPU", (allUEFisher.size() == 0 ? 0 : allPay / allUEFisher.size()), "ALL", etime, "-");
			setReportData("ARPPU", (allPayUser == 0 ? 0 : allPay / allPayUser), "ALL", etime, "-");
			setReportData("Permeability", (allUEFisher.size() == 0 ? 0 : (double) allPayUser / allUEFisher.size()), "ALL", etime, "-");

			System.out.println("---------------------------------------------------");
			// Server1 用户付费
			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s1StcPay);
			for (int id : _s1GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s1EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s1PayUser = _temp.size() + _s1GooglePay.size() + _s1EgyptPay.size();

			setReportData("TopupUser", s1PayUser, "Server1", etime, "-");
			setReportData("TopupUser", _s1StcPay.size(), "Server1", etime, "STC");
			setReportData("TopupUser", _s1GooglePay.size(), "Server1", etime, "GOOGLE");
			setReportData("TopupUser", _s1EgyptPay.size(), "Server1", etime, "EGYPT");

			double s1StcPay = getPaymentInteger(_s1StcPay) / 100;
			double s1GooglePay = getPaymentInteger(_s1GooglePay) * usdTosar;
			double s1EgyptPay = getPaymentInteger(_s1EgyptPay) / 100 * egpTosar;
			double s1AllPay = s1GooglePay + s1StcPay + s1EgyptPay;

			setReportData("Topup", s1AllPay, "Server1", etime, "-");
			setReportData("Topup", s1StcPay, "Server1", etime, "STC");
			setReportData("Topup", s1GooglePay, "Server1", etime, "GOOGLE");
			setReportData("Topup", s1EgyptPay, "Server1", etime, "EGYPT");

			setReportData("ARPU", (s1UEFisher.size() == 0 ? 0 : s1AllPay / s1UEFisher.size()), "Server1", etime, "-");
			setReportData("ARPPU", (s1PayUser == 0 ? 0 : s1AllPay / s1PayUser), "Server1", etime, "-");
			setReportData("Permeability", (s1UEFisher.size() == 0 ? 0 : (double) s1PayUser / s1UEFisher.size()), "Server1", etime, "-");
			System.out.println("---------------------------------------------------");

			// Server2 用户付费

			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s2StcPay);
			for (int id : _s2GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s2EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s2PayUser = _temp.size() + _s2GooglePay.size() + _s2EgyptPay.size();
			setReportData("TopupUser", s2PayUser, "Server2", etime, "-");
			setReportData("TopupUser", _s2StcPay.size(), "Server2", etime, "STC");
			setReportData("TopupUser", _s2EgyptPay.size(), "Server2", etime, "EGYPT");
			setReportData("TopupUser", _s2GooglePay.size(), "Server2", etime, "GOOGLE");

			double s2StcPay = getPaymentInteger(_s2StcPay) / 100;
			double s2GooglePay = getPaymentInteger(_s2GooglePay) * usdTosar;
			double s2EgyptPay = getPaymentInteger(_s2EgyptPay) / 100 * egpTosar;
			double s2AllPay = s2GooglePay + s2StcPay + s2EgyptPay;

			setReportData("Topup", s2AllPay, "Server2", etime, "-");
			setReportData("Topup", s2StcPay, "Server2", etime, "STC");
			setReportData("Topup", s2GooglePay, "Server2", etime, "GOOGLE");
			setReportData("Topup", s2EgyptPay, "Server2", etime, "EGYPT");

			setReportData("ARPU", (s2UEFisher.size() == 0 ? 0 : s2AllPay / s2UEFisher.size()), "Server2", etime, "-");
			setReportData("ARPPU", (s2PayUser == 0 ? 0 : s2AllPay / s2PayUser), "Server2", etime, "-");
			setReportData("Permeability", (s2UEFisher.size() == 0 ? 0 : (double) s2PayUser / s2UEFisher.size()), "Server2", etime, "-");
			System.out.println("---------------------------------------------------");

			// Server3 用户付费

			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s3StcPay);
			for (int id : _s3GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s3EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s3PayUser = _temp.size() + _s3GooglePay.size() + _s3EgyptPay.size();
			setReportData("TopupUser", s3PayUser, "Server3", etime, "-");
			setReportData("TopupUser", _s3StcPay.size(), "Server3", etime, "STC");
			setReportData("TopupUser", _s3EgyptPay.size(), "Server3", etime, "EGYPT");
			setReportData("TopupUser", _s3GooglePay.size(), "Server3", etime, "GOOGLE");

			double s3StcPay = getPaymentInteger(_s3StcPay) / 100;
			double s3GooglePay = getPaymentInteger(_s3GooglePay) * usdTosar;
			double s3EgyptPay = getPaymentInteger(_s3EgyptPay) / 100 * egpTosar;
			double s3AllPay = s3GooglePay + s3StcPay + s3EgyptPay;

			setReportData("Topup", s3AllPay, "Server3", etime, "-");
			setReportData("Topup", s3StcPay, "Server3", etime, "STC");
			setReportData("Topup", s3GooglePay, "Server3", etime, "GOOGLE");
			setReportData("Topup", s3EgyptPay, "Server3", etime, "EGYPT");

			setReportData("ARPU", (s3UEFisher.size() == 0 ? 0 : s3AllPay / s3UEFisher.size()), "Server3", etime, "-");
			setReportData("ARPPU", (s3PayUser == 0 ? 0 : s3AllPay / s3PayUser), "Server3", etime, "-");
			setReportData("Permeability", (s3UEFisher.size() == 0 ? 0 : (double) s3PayUser / s3UEFisher.size()), "Server3", etime, "-");
			System.out.println("---------------------------------------------------");

			// Server4 用户付费

			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s4StcPay);
			for (int id : _s4GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s4EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s4PayUser = _temp.size() + _s4GooglePay.size() + _s4EgyptPay.size();
			setReportData("TopupUser", s4PayUser, "Server4", etime, "-");
			setReportData("TopupUser", _s4StcPay.size(), "Server4", etime, "STC");
			setReportData("TopupUser", _s4EgyptPay.size(), "Server4", etime, "EGYPT");
			setReportData("TopupUser", _s4GooglePay.size(), "Server4", etime, "GOOGLE");

			double s4StcPay = getPaymentInteger(_s4StcPay) / 100;
			double s4GooglePay = getPaymentInteger(_s4GooglePay) * usdTosar;
			double s4EgyptPay = getPaymentInteger(_s4EgyptPay) / 100 * egpTosar;
			double s4AllPay = s4GooglePay + s4StcPay + s4EgyptPay;

			setReportData("Topup", s4AllPay, "Server4", etime, "-");
			setReportData("Topup", s4StcPay, "Server4", etime, "STC");
			setReportData("Topup", s4GooglePay, "Server4", etime, "GOOGLE");
			setReportData("Topup", s4EgyptPay, "Server4", etime, "EGYPT");

			setReportData("ARPU", (s4UEFisher.size() == 0 ? 0 : s4AllPay / s4UEFisher.size()), "Server4", etime, "-");
			setReportData("ARPPU", (s4PayUser == 0 ? 0 : s4AllPay / s4PayUser), "Server4", etime, "-");
			setReportData("Permeability", (s4UEFisher.size() == 0 ? 0 : (double) s4PayUser / s4UEFisher.size()), "Server4", etime, "-");
			System.out.println("---------------------------------------------------");

			// Server5 用户付费
			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s5StcPay);
			for (int id : _s5GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s5EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s5PayUser = _temp.size() + _s5GooglePay.size() + _s5EgyptPay.size();
			setReportData("TopupUser", s5PayUser, "Server5", etime, "-");
			setReportData("TopupUser", _s5StcPay.size(), "Server5", etime, "STC");
			setReportData("TopupUser", _s5EgyptPay.size(), "Server5", etime, "EGYPT");
			setReportData("TopupUser", _s5GooglePay.size(), "Server5", etime, "GOOGLE");

			double s5StcPay = getPaymentInteger(_s5StcPay) / 100;
			double s5GooglePay = getPaymentInteger(_s5GooglePay) * usdTosar;
			double s5EgyptPay = getPaymentInteger(_s5EgyptPay) / 100 * egpTosar;
			double s5AllPay = s5GooglePay + s5StcPay + s5EgyptPay;

			setReportData("Topup", s5AllPay, "Server5", etime, "-");
			setReportData("Topup", s5StcPay, "Server5", etime, "STC");
			setReportData("Topup", s5GooglePay, "Server5", etime, "GOOGLE");
			setReportData("Topup", s5EgyptPay, "Server5", etime, "EGYPT");

			setReportData("ARPU", (s5UEFisher.size() == 0 ? 0 : s5AllPay / s5UEFisher.size()), "Server5", etime, "-");
			setReportData("ARPPU", (s5PayUser == 0 ? 0 : s5AllPay / s5PayUser), "Server5", etime, "-");
			setReportData("Permeability", (s5UEFisher.size() == 0 ? 0 : (double) s5PayUser / s5UEFisher.size()), "Server5", etime, "-");
			System.out.println("---------------------------------------------------");

			// Server6 用户付费
			_temp = new HashMap<Integer, Integer>();
			_temp.putAll(_s6StcPay);
			for (int id : _s6GooglePay.keySet()) {
				_temp.remove(id);
			}

			for (int id : _s6EgyptPay.keySet()) {
				_temp.remove(id);
			}

			int s6PayUser = _temp.size() + _s6GooglePay.size() + _s6EgyptPay.size();
			setReportData("TopupUser", s6PayUser, "Server6", etime, "-");
			setReportData("TopupUser", _s6StcPay.size(), "Server6", etime, "STC");
			setReportData("TopupUser", _s6EgyptPay.size(), "Server6", etime, "EGYPT");
			setReportData("TopupUser", _s6GooglePay.size(), "Server6", etime, "GOOGLE");

			double s6StcPay = getPaymentInteger(_s6StcPay) / 100;
			double s6GooglePay = getPaymentInteger(_s6GooglePay) * usdTosar;
			double s6EgyptPay = getPaymentInteger(_s6EgyptPay) / 100 * egpTosar;
			double s6AllPay = s6GooglePay + s6StcPay + s6EgyptPay;

			setReportData("Topup", s6AllPay, "Server6", etime, "-");
			setReportData("Topup", s6StcPay, "Server6", etime, "STC");
			setReportData("Topup", s6GooglePay, "Server6", etime, "GOOGLE");
			setReportData("Topup", s6EgyptPay, "Server6", etime, "EGYPT");

			setReportData("ARPU", (s6UEFisher.size() == 0 ? 0 : s6AllPay / s6UEFisher.size()), "Server6", etime, "-");
			setReportData("ARPPU", (s6PayUser == 0 ? 0 : s6AllPay / s6PayUser), "Server6", etime, "-");
			setReportData("Permeability", (s6UEFisher.size() == 0 ? 0 : (double) s6PayUser / s6UEFisher.size()), "Server6", etime, "-");
			System.out.println("---------------------------------------------------");

			// 用户付费汇总_ALL
			Map<Integer, Double> allUserPay = new HashMap<Integer, Double>();
			for (int id : _allStcPay.keySet()) {
				allUserPay.put(id, new Double(_allStcPay.get(id)) / 100);
			}

			for (int id : _allEgyptPay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _allEgyptPay.get(id) / 100 * egpTosar);
				} else {
					allUserPay.put(id, _allEgyptPay.get(id) / 100 * egpTosar);
				}
			}

			for (int id : _s1GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s1GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s1GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s2GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s2GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s2GooglePay.get(id) * usdTosar);
				}
			}
			for (int id : _s3GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s3GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s3GooglePay.get(id) * usdTosar);
				}
			}
			for (int id : _s4GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s4GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s4GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s5GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s5GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s5GooglePay.get(id) * usdTosar);
				}
			}
			for (int id : _s6GooglePay.keySet()) {
				if (allUserPay.containsKey(id)) {
					allUserPay.put(id, allUserPay.get(id) + _s6GooglePay.get(id) * usdTosar);
				} else {
					allUserPay.put(id, _s6GooglePay.get(id) * usdTosar);
				}
			}

			// 用户付费汇总_S1
			Map<Integer, Double> s1UserPay = new HashMap<Integer, Double>();
			for (int id : _s1StcPay.keySet()) {
				s1UserPay.put(id, new Double(_s1StcPay.get(id)) / 100);
			}

			for (int id : _s1GooglePay.keySet()) {
				if (s1UserPay.containsKey(id)) {
					s1UserPay.put(id, s1UserPay.get(id) + _s1GooglePay.get(id) * usdTosar);
				} else {
					s1UserPay.put(id, _s1GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s1EgyptPay.keySet()) {
				if (s1UserPay.containsKey(id)) {
					s1UserPay.put(id, s1UserPay.get(id) + _s1EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s1UserPay.put(id, _s1EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 用户付费汇总_S2
			Map<Integer, Double> s2UserPay = new HashMap<Integer, Double>();
			for (int id : _s2StcPay.keySet()) {
				s2UserPay.put(id, new Double(_s2StcPay.get(id)) / 100);
			}

			for (int id : _s2GooglePay.keySet()) {
				if (s2UserPay.containsKey(id)) {
					s2UserPay.put(id, s2UserPay.get(id) + _s2GooglePay.get(id) * usdTosar);
				} else {
					s2UserPay.put(id, _s2GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s2EgyptPay.keySet()) {
				if (s2UserPay.containsKey(id)) {
					s2UserPay.put(id, s2UserPay.get(id) + _s2EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s2UserPay.put(id, _s2EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 用户付费汇总_S3
			Map<Integer, Double> s3UserPay = new HashMap<Integer, Double>();
			for (int id : _s3StcPay.keySet()) {
				s3UserPay.put(id, new Double(_s3StcPay.get(id)) / 100);
			}

			for (int id : _s3GooglePay.keySet()) {
				if (s3UserPay.containsKey(id)) {
					s3UserPay.put(id, s3UserPay.get(id) + _s3GooglePay.get(id) * usdTosar);
				} else {
					s3UserPay.put(id, _s3GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s3EgyptPay.keySet()) {
				if (s3UserPay.containsKey(id)) {
					s3UserPay.put(id, s3UserPay.get(id) + _s3EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s3UserPay.put(id, _s3EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 用户付费汇总_S4
			Map<Integer, Double> s4UserPay = new HashMap<Integer, Double>();
			for (int id : _s4StcPay.keySet()) {
				s4UserPay.put(id, new Double(_s4StcPay.get(id)) / 100);
			}

			for (int id : _s4GooglePay.keySet()) {
				if (s4UserPay.containsKey(id)) {
					s4UserPay.put(id, s4UserPay.get(id) + _s4GooglePay.get(id) * usdTosar);
				} else {
					s4UserPay.put(id, _s4GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s4EgyptPay.keySet()) {
				if (s4UserPay.containsKey(id)) {
					s4UserPay.put(id, s4UserPay.get(id) + _s4EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s4UserPay.put(id, _s4EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 用户付费汇总_S5
			Map<Integer, Double> s5UserPay = new HashMap<Integer, Double>();
			for (int id : _s5StcPay.keySet()) {
				s5UserPay.put(id, new Double(_s5StcPay.get(id)) / 100);
			}

			for (int id : _s5GooglePay.keySet()) {
				if (s5UserPay.containsKey(id)) {
					s5UserPay.put(id, s5UserPay.get(id) + _s5GooglePay.get(id) * usdTosar);
				} else {
					s5UserPay.put(id, _s5GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s5EgyptPay.keySet()) {
				if (s5UserPay.containsKey(id)) {
					s5UserPay.put(id, s5UserPay.get(id) + _s5EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s5UserPay.put(id, _s5EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 用户付费汇总_S6
			Map<Integer, Double> s6UserPay = new HashMap<Integer, Double>();
			for (int id : _s6StcPay.keySet()) {
				s6UserPay.put(id, new Double(_s6StcPay.get(id)) / 100);
			}

			for (int id : _s6GooglePay.keySet()) {
				if (s6UserPay.containsKey(id)) {
					s6UserPay.put(id, s6UserPay.get(id) + _s6GooglePay.get(id) * usdTosar);
				} else {
					s6UserPay.put(id, _s6GooglePay.get(id) * usdTosar);
				}
			}

			for (int id : _s6EgyptPay.keySet()) {
				if (s6UserPay.containsKey(id)) {
					s6UserPay.put(id, s6UserPay.get(id) + _s6EgyptPay.get(id) / 100 * egpTosar);
				} else {
					s6UserPay.put(id, _s6EgyptPay.get(id) / 100 * egpTosar);
				}
			}

			// 高中低付费用户计算
			setUserPayLevelData(etime, allUserPay, "ALL", "-");
			setUserPayLevelData(etime, s1UserPay, "Server1", "-");
			setUserPayLevelData(etime, s2UserPay, "Server2", "-");
			setUserPayLevelData(etime, s3UserPay, "Server3", "-");
			setUserPayLevelData(etime, s4UserPay, "Server4", "-");
			setUserPayLevelData(etime, s5UserPay, "Server5", "-");
			setUserPayLevelData(etime, s6UserPay, "Server6", "-");
			// 新用户

			Map<Integer, Integer> s1NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server1");
			Map<Integer, Integer> s2NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server2");
			Map<Integer, Integer> s3NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server3");
			Map<Integer, Integer> s4NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server4");
			Map<Integer, Integer> s5NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server5");
			Map<Integer, Integer> s6NewFisher = NewUsers.getDbNewUsersByServer(stime, etime, "Server6");

			Map<Integer, Integer> allNewFisher = new HashMap<Integer, Integer>();
			allNewFisher.putAll(s1NewFisher);
			allNewFisher.putAll(s2NewFisher);
			allNewFisher.putAll(s3NewFisher);
			allNewFisher.putAll(s4NewFisher);
			allNewFisher.putAll(s5NewFisher);
			allNewFisher.putAll(s6NewFisher);

			Map<Integer, Integer> allEgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> allInmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> allStcNewFisher = new HashMap<Integer, Integer>();

			for (int id : allNewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					allInmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					allEgyptNewFisher.put(id, 0);
				} else {
					allStcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", allNewFisher.size(), "ALL", etime, "-");
			setReportData("NewFisher", allStcNewFisher.size(), "ALL", etime, "STC");
			setReportData("NewFisher", allInmobiNewFisher.size(), "ALL", etime, "INMOBI");
			setReportData("NewFisher", allEgyptNewFisher.size(), "ALL", etime, "EGYPT");
			setReportData("NewFisher", s1NewFisher.size(), "Server1", etime, "-");
			setReportData("NewFisher", s2NewFisher.size(), "Server2", etime, "-");
			setReportData("NewFisher", s3NewFisher.size(), "Server3", etime, "-");
			setReportData("NewFisher", s4NewFisher.size(), "Server4", etime, "-");
			setReportData("NewFisher", s5NewFisher.size(), "Server5", etime, "-");
			setReportData("NewFisher", s6NewFisher.size(), "Server6", etime, "-");

			Map<Integer, Integer> s2EgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s2InmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s2StcNewFisher = new HashMap<Integer, Integer>();
			for (int id : s2NewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					s2InmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					s2EgyptNewFisher.put(id, 0);
				} else {
					s2StcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", s2EgyptNewFisher.size(), "Server2", etime, "EGYPT");
			setReportData("NewFisher", s2InmobiNewFisher.size(), "Server2", etime, "INMOBI");
			setReportData("NewFisher", s2StcNewFisher.size(), "Server2", etime, "STC");

			Map<Integer, Integer> s3EgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s3InmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s3StcNewFisher = new HashMap<Integer, Integer>();
			for (int id : s3NewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					s3InmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					s3EgyptNewFisher.put(id, 0);
				} else {
					s3StcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", s3EgyptNewFisher.size(), "Server3", etime, "EGYPT");
			setReportData("NewFisher", s3InmobiNewFisher.size(), "Server3", etime, "INMOBI");
			setReportData("NewFisher", s3StcNewFisher.size(), "Server3", etime, "STC");

			Map<Integer, Integer> s4EgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s4InmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s4StcNewFisher = new HashMap<Integer, Integer>();
			for (int id : s4NewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					s4InmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					s4EgyptNewFisher.put(id, 0);
				} else {
					s4StcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", s4EgyptNewFisher.size(), "Server4", etime, "EGYPT");
			setReportData("NewFisher", s4InmobiNewFisher.size(), "Server4", etime, "INMOBI");
			setReportData("NewFisher", s4StcNewFisher.size(), "Server4", etime, "STC");

			Map<Integer, Integer> s5EgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s5InmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s5StcNewFisher = new HashMap<Integer, Integer>();
			for (int id : s5NewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					s5InmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					s5EgyptNewFisher.put(id, 0);
				} else {
					s5StcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", s5EgyptNewFisher.size(), "Server5", etime, "EGYPT");
			setReportData("NewFisher", s5InmobiNewFisher.size(), "Server5", etime, "INMOBI");
			setReportData("NewFisher", s5StcNewFisher.size(), "Server5", etime, "STC");

			Map<Integer, Integer> s6EgyptNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s6InmobiNewFisher = new HashMap<Integer, Integer>();
			Map<Integer, Integer> s6StcNewFisher = new HashMap<Integer, Integer>();
			for (int id : s6NewFisher.keySet()) {
				if (allRegInmobiNewFisher.containsKey(id)) {
					s6InmobiNewFisher.put(id, 0);
				} else if (allRegEgyptNewFisher.containsKey(id)) {
					s6EgyptNewFisher.put(id, 0);
				} else {
					s6StcNewFisher.put(id, 0);
				}
			}

			setReportData("NewFisher", s6EgyptNewFisher.size(), "Server6", etime, "EGYPT");
			setReportData("NewFisher", s6InmobiNewFisher.size(), "Server6", etime, "INMOBI");
			setReportData("NewFisher", s6StcNewFisher.size(), "Server6", etime, "STC");

			setReportData("OldFisher", allUEFisher.size() - allNewFisher.size(), "ALL", etime, "-");
			setReportData("OldFisher", s1UEFisher.size() - s1NewFisher.size(), "Server1", etime, "-");
			setReportData("OldFisher", s2UEFisher.size() - s2NewFisher.size(), "Server2", etime, "-");
			setReportData("OldFisher", s3UEFisher.size() - s3NewFisher.size(), "Server3", etime, "-");
			setReportData("OldFisher", s4UEFisher.size() - s4NewFisher.size(), "Server4", etime, "-");
			setReportData("OldFisher", s5UEFisher.size() - s5NewFisher.size(), "Server5", etime, "-");
			setReportData("OldFisher", s6UEFisher.size() - s6NewFisher.size(), "Server6", etime, "-");
			System.out.println("---------------------------------------------------");

			// 新旧用户付费
			setNOPayData("-", "ALL", etime, allUEFisher, allUserPay, allNewFisher);
			setNOPayData("-", "Server1", etime, s1UEFisher, s1UserPay, s1NewFisher);
			setNOPayData("-", "Server2", etime, s2UEFisher, s2UserPay, s2NewFisher);
			setNOPayData("-", "Server3", etime, s3UEFisher, s3UserPay, s3NewFisher);
			setNOPayData("-", "Server4", etime, s4UEFisher, s4UserPay, s4NewFisher);
			setNOPayData("-", "Server5", etime, s5UEFisher, s5UserPay, s5NewFisher);
			setNOPayData("-", "Server6", etime, s6UEFisher, s6UserPay, s6NewFisher);
			// 分平台统计
			// 活跃用户
			setPlatFormData("Android", "Server2", stime, etime, s2UEFisher, s2UserPay, s2NewFisher);
			setPlatFormData("BB", "Server2", stime, etime, s2UEFisher, s2UserPay, s2NewFisher);
			setPlatFormData("J2ME", "Server2", stime, etime, s2UEFisher, s2UserPay, s2NewFisher);

			setPlatFormData("Android", "Server3", stime, etime, s3UEFisher, s3UserPay, s3NewFisher);
			setPlatFormData("BB", "Server3", stime, etime, s3UEFisher, s3UserPay, s3NewFisher);
			setPlatFormData("J2ME", "Server3", stime, etime, s3UEFisher, s3UserPay, s3NewFisher);

			setPlatFormData("Android", "Server4", stime, etime, s4UEFisher, s4UserPay, s4NewFisher);
			setPlatFormData("BB", "Server4", stime, etime, s4UEFisher, s4UserPay, s4NewFisher);
			setPlatFormData("J2ME", "Server4", stime, etime, s4UEFisher, s4UserPay, s4NewFisher);

			setPlatFormData("Android", "Server5", stime, etime, s5UEFisher, s5UserPay, s5NewFisher);
			setPlatFormData("BB", "Server5", stime, etime, s5UEFisher, s5UserPay, s5NewFisher);
			setPlatFormData("J2ME", "Server5", stime, etime, s5UEFisher, s5UserPay, s5NewFisher);

			setPlatFormData("Android", "Server6", stime, etime, s6UEFisher, s6UserPay, s6NewFisher);
			setPlatFormData("BB", "Server6", stime, etime, s6UEFisher, s6UserPay, s6NewFisher);
			setPlatFormData("J2ME", "Server6", stime, etime, s6UEFisher, s6UserPay, s6NewFisher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 新旧用户付费
	private static void setNOPayData(String platform, String server, Date etime, Map<Integer, Integer> activityFisher, Map<Integer, Double> userPay, Map<Integer, Integer> newFisher) {
		Map<Integer, Double> _newUserPay = new HashMap<Integer, Double>();
		Map<Integer, Double> _oldUserPay = new HashMap<Integer, Double>();
		for (int id : userPay.keySet()) {
			if (newFisher.containsKey(id)) {
				_newUserPay.put(id, userPay.get(id));
			} else {
				_oldUserPay.put(id, userPay.get(id));
			}
		}
		double _oldPay = getPaymentDouble(_oldUserPay);
		setReportData("TopupOld", _oldPay, server, etime, platform);
		setReportData("TopupOldFisher", _oldUserPay.size(), server, etime, platform);
		setReportData("TopupOldARPPU", (_oldUserPay.size() == 0 ? 0 : _oldPay / _oldUserPay.size()), server, etime, platform);
		setReportData("TopupOldARPU", ((activityFisher.size() - newFisher.size()) == 0 ? 0 : _oldPay / (activityFisher.size() - newFisher.size())), server, etime, platform);
		setReportData("TopupOldPermeability", ((activityFisher.size() - newFisher.size()) == 0 ? 0 : (double) _oldUserPay.size() / (activityFisher.size() - newFisher.size())), server, etime, platform);

		double _newPay = getPaymentDouble(_newUserPay);
		setReportData("TopupNew", _newPay, server, etime, platform);
		setReportData("TopupNewFisher", _newUserPay.size(), server, etime, platform);
		setReportData("TopupNewARPPU", (_newUserPay.size() == 0 ? 0 : _newPay / _newUserPay.size()), server, etime, platform);
		setReportData("TopupNewARPU", (_newUserPay.size() == 0 ? 0 : _newPay / newFisher.size()), server, etime, platform);
		setReportData("TopupNewPermeability", (newFisher.size() == 0 ? 0 : (double) _newUserPay.size() / newFisher.size()), server, etime, platform);
		System.out.println("---------------------------------------------------");
	}

	// 高中低付费用户计算
	private static void setUserPayLevelData(Date etime, Map<Integer, Double> userPay, String server, String platform) {
		int hcount;
		double hsum;
		int lcount;
		double lsum;
		int mcount;
		double msum;
		hcount = 0;
		hsum = 0;
		lcount = 0;
		lsum = 0;
		mcount = 0;
		msum = 0;
		for (int id : userPay.keySet()) {
			if (userPay.get(id) >= highPay) {
				hcount++;
				hsum += userPay.get(id);
			} else if (userPay.get(id) <= lowPay) {
				lcount++;
				lsum += userPay.get(id);
			} else {
				mcount++;
				msum += userPay.get(id);
			}
		}

		setReportData("TopupHigh", hsum, server, etime, platform);
		setReportData("TopupHighUser", hcount, server, etime, platform);
		setReportData("TopupHighARPPU", (hcount == 0 ? 0 : hsum / hcount), server, etime, platform);
		setReportData("TopupMiddle", msum, server, etime, platform);
		setReportData("TopupMiddleUser", mcount, server, etime, platform);
		setReportData("TopupMiddleARPPU", (mcount == 0 ? 0 : msum / mcount), server, etime, platform);
		setReportData("TopupLow", lsum, server, etime, platform);
		setReportData("TopupLowUser", lcount, server, etime, platform);
		setReportData("TopupLowARPPU", (lcount == 0 ? 0 : lsum / lcount), server, etime, platform);
		System.out.println("---------------------------------------------------");
	}

	// 分平台统计
	private static void setPlatFormData(String platform, String server, Date stime, Date etime, Map<Integer, Integer> activityFisher, Map<Integer, Double> userPay, Map<Integer, Integer> newFisher) {
		Map<Integer, Integer> _PlatformUserMap = Users.getPlatformUserByServer(stime, server, platform);

		Map<Integer, Integer> plagformActivityFisherMap = new HashMap<Integer, Integer>();

		for (int id : activityFisher.keySet()) {
			if (_PlatformUserMap.containsKey(id)) {
				plagformActivityFisherMap.put(id, 0);
			}
		}

		setReportData("ActivityFisher", plagformActivityFisherMap.size(), server, etime, platform);
		// 新旧用户
		Map<Integer, Integer> platformNewFisherMap = new HashMap<Integer, Integer>();
		for (int id : newFisher.keySet()) {
			if (_PlatformUserMap.containsKey(id)) {
				platformNewFisherMap.put(id, 0);
			}
		}

		setReportData("NewFisher", platformNewFisherMap.size(), server, etime, platform);
		setReportData("OldFisher", plagformActivityFisherMap.size() - platformNewFisherMap.size(), server, etime, platform);

		// 付费用户

		Map<Integer, Double> platformPayFisherMap = new HashMap<Integer, Double>();
		for (int id : userPay.keySet()) {
			if (_PlatformUserMap.containsKey(id)) {
				platformPayFisherMap.put(id, userPay.get(id));
			}
		}

		double _topupPlatform = getPaymentDouble(platformPayFisherMap);
		setReportData("Topup", _topupPlatform, server, etime, platform);
		setReportData("TopupUser", platformPayFisherMap.size(), server, etime, platform);
		setReportData("ARPU", (plagformActivityFisherMap.size() == 0 ? 0 : _topupPlatform / plagformActivityFisherMap.size()), server, etime, platform);
		setReportData("ARPPU", (platformPayFisherMap.size() == 0 ? 0 : _topupPlatform / platformPayFisherMap.size()), server, etime, platform);
		setReportData("Permeability", (plagformActivityFisherMap.size() == 0 ? 0 : (double) platformPayFisherMap.size() / plagformActivityFisherMap.size()), server, etime, platform);
	}

	public static void setReportData(String key, int value, String server, Date etime, String channel) {
		System.out.println("Set " + server + " " + channel + " " + key + ":" + value);

		Calendar c = Calendar.getInstance();
		c.setTime(etime);
		c.add(Calendar.DATE, -1);
		ConfigUtil.portal1db.execSQLUpdate("insert into weekReport(oakey,oavalue,server,channel,dtime) values (?,?,?,?,?)", new Object[] { key, value, server, channel, c.getTime() });
	}

	public static void setReportData(String key, double value, String server, Date etime, String channel) {
		System.out.println("Set " + server + " " + channel + " " + key + ":" + value);

		Calendar c = Calendar.getInstance();
		c.setTime(etime);
		c.add(Calendar.DATE, -1);
		ConfigUtil.portal1db.execSQLUpdate("insert into weekReport(oakey,oavalue,server,channel,dtime) values (?,?,?,?,?)", new Object[] { key, value, server, channel, c.getTime() });
	}

	static double getPaymentInteger(Map<Integer, Integer> dataMap) {
		double result = 0;
		for (int id : dataMap.keySet()) {
			result += dataMap.get(id);
		}
		return result;
	}

	static double getPaymentDouble(Map<Integer, Double> dataMap) {
		double result = 0;
		for (int id : dataMap.keySet()) {
			result += dataMap.get(id);
		}
		return result;
	}

	static void clearId(Map<Integer, Integer> dataMap) {
		int ids[] = { 4164, 7001, 7803, 83885, 13793, 28268, 105158, 105529, 4164 };
		for (int id : ids) {
			dataMap.remove(id);
		}
	}

}
