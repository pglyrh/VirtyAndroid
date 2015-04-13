package edu.xidian.mti1001.virtyandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
	//context为调用数据库的环境，name为数据库名，factory通常为null，version可设为1
	public UserDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//创建数据库的结构，例如新建表
		//db.execSQL("CREATE TABLE user("+"name TEXT DEFAULT NONE,"+"sex TEXT DEFAULT NONE)");
		
		//用户表包括的信息为，用户名、性别、年龄、身高、目标体重
		db.execSQL("CREATE TABLE user("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT DEFAULT NONE," + "sex TEXT DEFAULT NONE,"+"age INTEGER DEFAULT NONE,"
				+"height INTEGER DEFAULT NONE,"+"targetWeight FLOAT DEFAULT NONE)");
		
		//建立表单，保存计步器内容，初级版本不分用户，高级版本有区分
		db.execSQL("CREATE TABLE pedo("
				+ "target INTEGER DEFAULT NONE," + "current INTEGER DEFAULT NONE,"
					+"percent FLOAT DEFAULT NONE)");
		
		//建立表单，保存用户的各项健康指标（名、日期、事件、体重、脂肪率、水分率、肌肉率、骨量、基础代谢）
		db.execSQL("CREATE TABLE userHealthRecord("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT DEFAULT NONE,"
				+ "date TEXT DEFAULT NONE,"
				+ "time TEXT DEFAULT NONE,"				
				+ "weight FLOAT DEFAULT 0.0,"
				+ "fat FLOAT DEFAULT 0.0,"
				+ "water FLOAT DEFAULT 0.0,"				
				+ "muscle FLOAT DEFAULT 0.0,"
				+ "bone FLOAT DEFAULT 0.0,"			
				+ "metabolism INTEGER DEFAULT 0)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
