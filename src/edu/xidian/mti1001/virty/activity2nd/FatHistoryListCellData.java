package edu.xidian.mti1001.virty.activity2nd;

public class FatHistoryListCellData {

/*	db.execSQL("CREATE TABLE userHealthRecord("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "name TEXT DEFAULT NONE,"
			+ "date TEXT DEFAULT NONE,"
			+ "time TEXT DEFAULT NONE,"				
			+ "weight FLOAT DEFAULT 0.0,"
			+ "fat FLOAT DEFAULT 0.0,"
			+ "water FLOAT DEFAULT 0.0,"				
			+ "muscle FLOAT DEFAULT 0.0,"
			+ "bone FLOAT DEFAULT 0.0,"			
			+ "metabolism INTEGER DEFAULT 0)");*/
	public FatHistoryListCellData(String date, String time, float weight, float fat, 
			float water, float muscle, float bone, int metabolism) {
		this.date = date;
		this.time = time;
		this.weight = weight;
		this.fat = fat;
		this.water = water;
		this.muscle = muscle;
		this.bone = bone;
		this.metabolism = metabolism;
	}

	public String date = "";
	public String time = "";
	public float weight = 0;
	public float fat = 0;
	public float water = 0;
	public float muscle = 0;
	public float bone = 0;
	public int metabolism = 0;

}
