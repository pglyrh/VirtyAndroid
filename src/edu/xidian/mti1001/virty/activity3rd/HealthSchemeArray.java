package edu.xidian.mti1001.virty.activity3rd;

import java.util.ArrayList;
import java.util.Locale;

//健康方案数组，在此进行一些初始化
public class HealthSchemeArray {
	// 数组
	private ArrayList<HealthScheme> gymMaleSchemes;
	private ArrayList<HealthScheme> homeMaleSchemes;
	private ArrayList<HealthScheme> gymFemaleSchemes;
	private ArrayList<HealthScheme> homeFemaleSchemes;
	private HealthScheme healthScheme;
	private boolean flag = true;

	// 构造函数
	public HealthSchemeArray() {		
		String l = Locale.getDefault().getLanguage().toLowerCase();
		if (l.contains("zh")) {
			// 简体中文
			flag = true;
		}else if (l.contains("en")) {
			// 英文
			flag = false;
		}
		
		// 对数组进行一些初始化
		initHealthSchemes();
	}

	private void initHealthSchemes() {
		gymMaleSchemes = new ArrayList<HealthScheme>();
		homeMaleSchemes = new ArrayList<HealthScheme>();
		gymFemaleSchemes = new ArrayList<HealthScheme>();
		homeFemaleSchemes = new ArrayList<HealthScheme>();

		if (flag) {
			//女性健身房健身方案
			healthScheme = new HealthScheme(0, 1, "摇摆机", "6次/周", "30分钟");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "跑步机", "3次/周", "40分钟");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "腰腹肌", "3次/周", "30分钟");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "动感单车", "2次/周", "25分钟");
			gymFemaleSchemes.add(healthScheme);
			
			//男性健身房健身方案
			healthScheme = new HealthScheme(0, 0, "动感单车", "2次/周", "30分钟");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "哑铃推举", "2次/周", "3×30个");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "跑步机", "3次/周", "60分钟");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "引体向上", "3次/周", "10个");
			gymMaleSchemes.add(healthScheme);
			
			
			//女性居家健身方案
			healthScheme = new HealthScheme(1, 1, "健身球", "3次/周", "45分钟");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "仰卧起坐", "6次/周", "30个");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "健身操", "2次/周", "45分钟");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "瑜伽", "3次/周", "45分钟");
			homeFemaleSchemes.add(healthScheme);
			
			//男性居家健身方案
			healthScheme = new HealthScheme(1, 0, "扶墙半蹲", "3次/周", "3×15个");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "仰卧起坐", "6次/周", "2×30个");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "健腹轮", "3次/周", "30分钟");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "俯卧撑", "3次/周", "3×20个");
			homeMaleSchemes.add(healthScheme);
		}else {
			//女性健身房健身方案
			healthScheme = new HealthScheme(0, 1, "Wave", "6/w", "30mins");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "Treadmill", "3t/w", "40mins");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "Easy-line", "3t/w", "30mins");
			gymFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 1, "Spinning", "2t/w", "25mins");
			gymFemaleSchemes.add(healthScheme);
			
			//男性健身房健身方案
			healthScheme = new HealthScheme(0, 0, "Spinning", "2t/w", "30mins");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "Dumbbell", "2t/w", "3×30times");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "Treadmill", "3t/w", "60mins");
			gymMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(0, 0, "Pull-up", "3t/w", "10times");
			gymMaleSchemes.add(healthScheme);
			
			
			//女性居家健身方案
			healthScheme = new HealthScheme(1, 1, "Fit-ball", "3t/w", "45mins");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "Sit-up", "6t/w", "30times");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "Aerobics", "2t/w", "45mins");
			homeFemaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 1, "Yoga", "3t/w", "45mins");
			homeFemaleSchemes.add(healthScheme);
			
			//男性居家健身方案
			healthScheme = new HealthScheme(1, 0, "Squats", "3t/w", "3×15times");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "Sit-up", "6t/w", "2×30times");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "Roller", "3t/w", "30mins");
			homeMaleSchemes.add(healthScheme);
			healthScheme = new HealthScheme(1, 0, "Push-up", "3t/w", "3×20times");
			homeMaleSchemes.add(healthScheme);
		}
		
	}
	
	//根据性别、类型返回列表
	public HealthScheme getGymMaleItem(int position){
		return gymMaleSchemes.get(position);
	}
	public HealthScheme getHomeMaleItem(int position){
		return homeMaleSchemes.get(position);
	}
	public HealthScheme getGymFemaleItem(int position){
		return gymFemaleSchemes.get(position);
	}
	public HealthScheme getHomeFemaleItem(int position){
		return homeFemaleSchemes.get(position);
	}
	// 传入list和position
	public HealthScheme getItem(ArrayList<HealthScheme> schemes, int position){
		return schemes.get(position);
	}
	
	public ArrayList<HealthScheme> getGymMaleSchemes(){
		return this.gymMaleSchemes;
	}
	
	public ArrayList<HealthScheme> getHomeMaleSchemes(){
		return this.homeMaleSchemes;
	}
	public ArrayList<HealthScheme> getGymFemaleSchemes(){
		return this.gymFemaleSchemes;
	}
	
	public ArrayList<HealthScheme> getHomeFemaleSchemes(){
		return this.homeFemaleSchemes;
	}
	
	// 根据性别和category返回列表
	public ArrayList<HealthScheme> getSchemes(int sex, int category){
		if (sex == 0) {
			//男性
			if (category == 0) {
				//健身房
				return this.gymMaleSchemes;
			}else {
				return this.homeMaleSchemes;
			}
		}else {
			//女性
			if (category == 0) {
				//健身房
				return this.gymFemaleSchemes;
			}else {
				return this.homeFemaleSchemes;
			}
		}
	}
}
