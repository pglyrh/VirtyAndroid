package edu.xidian.mti1001.virty.activity2nd;

import java.util.ArrayList;

import android.widget.Button;

//脂肪秤的趋势
public class TrendFatCellData {
	// 成员属性，日期、体重（kg记录）、脂肪、水分、肌肉、骨量、基础代谢
	public String date = "";
	public float weight = 0;
	public float fat = 0;
	public float water = 0;
	public float muscle = 0;
	public float bone = 0;
	public int metabolism = 0;
	
	// 将各项指标保存为一个数组
//	private ArrayList healthItems;

	public TrendFatCellData(String date, float weight, float fat, float water,
			float muscle, float bone, int metabolism) {
		this.date = date.substring(5);
		this.weight = weight;
		this.fat = fat;
		this.water = water;
		this.muscle = muscle;
		this.bone = bone;
		this.metabolism = metabolism;
	}

	//根据传入的数值决定返回的成员属性
	public float getValue(int i){
		switch (i) {
		case 0:
			return weight;
		case 1:
			return fat;
		case 2:
			return water;
		case 3:
			return muscle;
		case 4:
			return bone;
		case 5:
			return metabolism;
		default:
			return 0;
		}
	}
}
