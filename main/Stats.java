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
			//���߹��� - gold
			/*GoldBuyStats.StartStats(fTime);*/
			GoldBuyStats.StartStats(fTime);
			//���߹��� - ����ʯ
			CreditStats.StartStats(fTime);
			//����ʹ��
			ItemUsing.StartStats(fTime);
			
			
			
			//session3
			//����վ�� - ���ʺ����ص��ֻ�
			DownloadWebsite.startStats(fTime);
			//android��j2me���������
			DownloadFileForResumeBrokenDownload.StartStats(fTime);
			//BB���������
			DownloadFile.StartStats(fTime);
			//��ֵ���
			TopUp.StartStats(fTime, tTime);
			
			//�û��ֻ�ƽ̨ͳ��
			UserPhone.StartStats(fTime);
			//session1
			//5�㵽5���DAU��¼
			DAUFor5to5.StartStats(fTime);
			//��¼��־�ļ�¼
			LoginServiceDAU.startStats(fTime);
			//ע��log�����û�
			RegLogNewUser.StartStats(fTime);
			//�����û�����Ϊ��¼
			AllAction.StartStats(fTime);
			//��¼�ؼ���ָ��
			OAproKPI.StartStats(fTime);
			//������
			ReturnRate.StartStats(fTime);
			//ÿ�������ȼ���������
			ReturnRate.getEachShipyardLevelReturnRate(fTime);
			
			//session4  ����ʯ  ����   ���    ͳ��
			GoldStats.countGoldProduce(fTime);
			GoldStats.countPearlProduce(fTime);
			GoldStats.countSapphireProduce(fTime);
			//ÿ�ջ�Ծ�û��Ŀ�����
			DAUStocks.StartStats(fTime);
			//ÿ�ջ�Ծ�û�����Դ���
			DAULevelResource.StartStats(fTime);
			//��¼���ֵ���������
			NewUserGuide.StartStats(fTime);
			//�û��Ĵ����͵ķֲ�
			ShipDistribution.StartStats(fTime);
			
			//��ֵǰʮ�û�
			UserPayment.calcTop10Payment(fTime);
			
			//
			ShipyardLevelWPD.StartStats(fTime);
			//�ƹ�������ͳ��
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
			//��ǿ��ϵͳ
			Equipment.calcReinforceConsume2(fTime);
			Pet.calcPetUpgradTime(fTime);
			Shipyard.calcShipyardUpgradTime(fTime);
			
			//������
			MonsterCalc.calcCreateTribeMonster(fTime);
			MonsterCalc.calcKillTribeMonster(fTime);
			MonsterAttackCalc.calcAttackTime(fTime);
			
///			ItemSource.StartStatsFromDB(fTime, tTime);
			//�齱
			LuckDraw.calcNomal(fTime);
			LuckDraw.calcEquipment(fTime);
			
			ItemPurchased.itemBuyGroupUp(fTime);
			//��Դ����
			resource.resourceUpdate.resouceUpdateCalc(fTime);
			
			ArenaCalc.generateTop20UserEquipment(fTime);
			
			//send email
			//DailyReportEmail.StartSend();
			//ը����ʹ�üӵõ�
			weapon.UseWeapon.WeaponInAndOut(fTime);
			
			
//			//ͳ��  ����...������ʱ������
//			AmountPearlCredit.countPearlProduce(fTime);
			
			fTime = tTime;
		}
		logger.info("Stats process end "+new Date());
	}
}
