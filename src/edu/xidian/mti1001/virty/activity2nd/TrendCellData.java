package edu.xidian.mti1001.virty.activity2nd;

public class TrendCellData {
	// 成员属性，日期和体重（kg记录）
	public String date = "";
	public String originDate = "";
	public float weight = 0;

	public TrendCellData(String date, float weight) {
		this.originDate = date;
		this.date = date.substring(5);
		this.weight = weight;
	}

}
