package edu.xidian.mti1001.virty.activity2nd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import edu.xidian.mti1001.virty.welcome.R;

public class BmiHistorySimpleCursorAdapter extends SimpleCursorAdapter {
	private Cursor m_cursor;  
	private Context m_context;  

	public BmiHistorySimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
		m_cursor = c;  
        m_context = context; 
	}
  
    @Override  
    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
        final View view = super.newView(context, cursor, parent);  
        ViewHolder holder = new ViewHolder();  
        holder.dateView = (TextView) view.findViewById(R.id.textView2ndBmiHistoryDate);  
        holder.timeView = (TextView) view.findViewById(R.id.textView2ndBmiHistoryTime);  
        holder.weekView = (TextView) view.findViewById(R.id.textView2ndBmiHistoryWeek);  
        holder.weightView = (TextView) view.findViewById(R.id.textView2ndBmiHistoryWeight);  
  
        view.setTag(holder);  
        return view;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        return super.getView(position, convertView, parent);  
    }  

//	//建立表单，保存用户的各项健康指标（名、日期、事件、体重、脂肪率、水分率、肌肉率、骨量、基础代谢）
//	db.execSQL("CREATE TABLE userHealthRecord("
//			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
//			+ "name TEXT DEFAULT NONE,"
//			+ "date TEXT DEFAULT NONE,"
//			+ "time TEXT DEFAULT NONE,"				
//			+ "weight FLOAT DEFAULT 0.0,"
//			+ "fat FLOAT DEFAULT 0.0,"
//			+ "water FLOAT DEFAULT 0.0,"				
//			+ "muscle FLOAT DEFAULT 0.0,"
//			+ "bone FLOAT DEFAULT 0.0,"			
//			+ "metabolism INTEGER DEFAULT 0)");
    @Override  
    public void bindView(View view, Context context, Cursor cursor) {  
        ViewHolder holder = (ViewHolder) view.getTag();  
        setViewText(holder.dateView, cursor.getString(cursor  
                .getColumnIndex("date")));  
        setViewText(holder.timeView, cursor.getString(cursor  
                .getColumnIndex("time")));  
        setViewText(holder.weekView, DateToWeek(cursor.getString(cursor  
                .getColumnIndex("date"))));  
        setViewText(holder.weightView, cursor.getString(cursor  
                .getColumnIndex("weight")));  

        super.bindView(view, context, cursor);  
    }  
  
    final static class ViewHolder {  
        public TextView dateView;  
        public TextView timeView;  
        public TextView weekView;  
        public TextView weightView;  
    } 
    
    // 将日期转成星期
	public static final int WEEKDAYS = 7;
 	// 星期的定义
 	public static String[] WEEK = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
 			"星期天" };

 	/**
 	 * 日期变量转成对应的星期字符串
 	 * 
 	 * @param date
 	 * @return
 	 */
 	public static String DateToWeek(String dateString) {
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ");  
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
			int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayIndex < 1 || dayIndex > WEEKDAYS) {
				return null;
			}
			
			return WEEK[dayIndex - 1];
		}
		return null;
 	}
}
