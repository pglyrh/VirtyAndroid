package edu.xidian.mti1001.virty.activity2nd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BmiHistoryListCellData {

	public BmiHistoryListCellData(String date, String time, float weight) {
		this.date = date;
		this.time = time;
		this.weight = weight;
		this.week = DateToWeek(date);
	}

	public String date = "";
	public String time = "";
	public float weight = 0;
	public String week = "";

	// 将日期转成星期
	public static final int WEEKDAYS = 7;
	// 星期的定义
	public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	public static String[] WEEKEN = { "Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};

	/**
	 * 日期变量转成对应的星期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String DateToWeek(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
			int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayIndex < 1 || dayIndex > WEEKDAYS) {
				return null;
			}
			
//			System.out.println("dayIndex: "+dayIndex);
			
			if (Locale.getDefault().getLanguage().toLowerCase().contains("zh")) {
				return WEEK[(dayIndex+6)%7];
			}else {
				return WEEKEN[(dayIndex+6)%7];				
			}
		}
		return null;
	}
}
