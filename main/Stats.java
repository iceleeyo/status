package main;

import item.DAULevelResource;
import item.DAUStocks;
import item.GoldStats;
import item.ShipDistribution;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import luckydraw.LuckDraw;

import org.apache.log4j.Logger;

import payment.UserPayment;
import petEquiment.Equipment;
import petEquiment.Pet;
import shipyard.Shipyard;
import tribe.MonsterAttackCalc;
import tribe.MonsterCalc;
import tribe.TribeLevel;
import user.DAUFor5to5;
import user.LoginServiceDAU;
import user.NewUserGuide;
import user.RegLogNewUser;
import user.ReturnRate;
import user.ShipyardLevelWPD;
import user.UserPhone;
import user.UserPlatformCount;
import util.MyUtil;
import action.AllAction;
import basicStats.CreditStats;
import basicStats.GoldBuyStats;
import basicStats.ItemUsing;
import basicStats.OAproKPI;
import basicStats.TopUp;
import both.calc.ArenaCalc;
import both.calc.ItemPurchased;
import both.calc.NewUser;
import both.calc.Payment;
import download.DownloadFile;
import download.DownloadFileForResumeBrokenDownload;
import download.DownloadWebsite;
import download.PromotionChannel;
import egypt.EgyptUser;

public class Stats {
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class);
		Date fTime = new Date();
		Date tTime = new Date();
		if(args.length == 2){
			fTime = MyUtil.StringToDate(args[0]);
			tTime = MyUtil.StringToDate(args[1]);
		}
		if(args.length == 0){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			tTime = (Date) gc.getTime();
			gc.add(Calendar.DATE, -1);
			fTime = (Date) gc.getTime();
		}
		logger.info("Stats process start "+new Date());
		logger.info("Stats process start "+fTime);
		long flag =  (tTime.getTime()-fTime.getTime())/1000/3600/24;
		for(long temp=0;temp<flag;temp++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			gc.add(Calendar.DATE, 1);
			tTime = (Date) gc.getTime();
			
			System.out.print(fTime);
			System.out.print("----");
			System.out.println(tTime);

			//session2
			//道具购买 - gold
			/*GoldBuyStats.StartStats(fTime);*/
			GoldBuyStats.StartStats(fTime);
			//道具购买 - 蓝宝石
			CreditStats.StartStats(fTime);
			//道具使用
			ItemUsing.StartStats(fTime);
			
			
			
			//session3
			//下载站点 - 访问和下载的手机
			DownloadWebsite.startStats(fTime);
			//android和j2me的下载情况
			DownloadFileForResumeBrokenDownload.StartStats(fTime);
			//BB的下载情况
			DownloadFile.StartStats(fTime);
			//充值情况
			TopUp.StartStats(fTime, tTime);
			
			//用户手机平台统计
			UserPhone.StartStats(fTime);
			//session1
			//5点到5点的DAU记录
			DAUFor5to5.StartStats(fTime);
			//登录日志的记录
			LoginServiceDAU.startStats(fTime);
			//注册log的新用户
			RegLogNewUser.StartStats(fTime);
			//所有用户的行为记录
			AllAction.StartStats(fTime);
			//记录关键性指标
			OAproKPI.StartStats(fTime);
			//留存率
			ReturnRate.StartStats(fTime);
			//每个船厂等级的留存率
			ReturnRate.getEachShipyardLevelReturnRate(fTime);
			
			//session4  蓝宝石  珍珠   金币    统计
			GoldStats.countGoldProduce(fTime);
			GoldStats.countPearlProduce(fTime);
			GoldStats.countSapphireProduce(fTime);
			//每日活跃用户的库存情况
			DAUStocks.StartStats(fTime);
			//每日活跃用户的资源获得
			DAULevelResource.StartStats(fTime);
			//记录新手导航完成情况
			NewUserGuide.StartStats(fTime);
			//用户的船类型的分布
			ShipDistribution.StartStats(fTime);
			
			//充值前十用户
			UserPayment.calcTop10Payment(fTime);
			
			//
			ShipyardLevelWPD.StartStats(fTime);
			//推广渠道的统计
			PromotionChannel.StartStats(fTime);

			
			UserPlatformCount.startCount(fTime);
			
			DAUFor5to5.userTopup5To5(fTime);
			
			NewUser.userLogin(fTime);
			
			Payment.userPayment(fTime);

	
			Payment.channelPayment(fTime);


			//GetEgyptUserInfo.insertToEgypt(fTime);
			TribeLevel.getAllTribeLevel(fTime);
			
			EgyptUser.calcEgyptUser(fTime);


			
			
			ArenaCalc.calcDailyChallange(fTime);
			//ArenaCalc.arenaTop100bak(fTime);
			ArenaCalc.generateTop100Detail(fTime);
			Equipment.calcActivityUserEquipmentReinforce(fTime);
			Equipment.calcEquipmentReinforce(fTime);
			Equipment.calcReinforceConsume(fTime);
			//新强化系统
			Equipment.calcReinforceConsume2(fTime);
			Pet.calcPetUpgradTime(fTime);
			Shipyard.calcShipyardUpgradTime(fTime);
			
			//家族打怪
			MonsterCalc.calcCreateTribeMonster(fTime);
			MonsterCalc.calcKillTribeMonster(fTime);
			MonsterAttackCalc.calcAttackTime(fTime);
			
///			ItemSource.StartStatsFromDB(fTime, tTime);
			//抽奖
			LuckDraw.calcNomal(fTime);
			LuckDraw.calcEquipment(fTime);
			
			ItemPurchased.itemBuyGroupUp(fTime);
			//资源更新
			resource.resourceUpdate.resouceUpdateCalc(fTime);
			
			ArenaCalc.generateTop20UserEquipment(fTime);
			
			//send email
			//DailyReportEmail.StartSend();
			//炸弹的使用加得到
			weapon.UseWeapon.WeaponInAndOut(fTime);
			
			
//			//统计  珍珠...测试暂时不上线
//			AmountPearlCredit.countPearlProduce(fTime);
			
			fTime = tTime;
		}
		logger.info("Stats process end "+new Date());
	}
}
