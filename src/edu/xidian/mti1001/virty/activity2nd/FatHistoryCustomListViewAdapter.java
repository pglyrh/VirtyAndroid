package edu.xidian.mti1001.virty.activity2nd;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;


public class FatHistoryCustomListViewAdapter extends BaseAdapter {
	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	//数据库指针
	private Cursor cursor;
	private String name;
	private Context context;
	
	//默认单位
	private boolean unit;
//	private String unitOfWeight;
	
	//数据
	public ArrayList<FatHistoryListCellData> data = new ArrayList<FatHistoryListCellData>();
	
	public FatHistoryCustomListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		db = new UserDatabaseHelper(context, "VirtyDB", null, 1);
//		System.out.println("..................db: "+db.toString());		
		initRecord();
	}
	
	public Context getContext() {
		return context;
	}
	
	// 自定义函数，用户通过更改adapter（通过cursor对数据库查询），进而刷新listView
	public void initRecord() {
		name = Activity1stUserList.CHOOSE_USER_NAME;
		dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,
		// String groupBy, String having,String orderBy)
		cursor = dbReadDatabase.query("userHealthRecord", null,
				"name=?", new String[] { name + "" }, null, null, null);
		data.clear();
		if (cursor.getCount()>0) {
//			System.out.println("............................cursor: "+cursor.getCount());
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
/*				public FatHistoryListCellData(String date, String time, float weight, float fat, 
						float water, float muscle, float bone, int metabolism)*/
				FatHistoryListCellData history = new FatHistoryListCellData(
						cursor.getString(cursor.getColumnIndex("date")),
						cursor.getString(cursor.getColumnIndex("time")),
						cursor.getFloat(cursor.getColumnIndex("weight")),
						cursor.getFloat(cursor.getColumnIndex("fat")),
						cursor.getFloat(cursor.getColumnIndex("water")),
						cursor.getFloat(cursor.getColumnIndex("muscle")),
						cursor.getFloat(cursor.getColumnIndex("bone")),
						cursor.getInt(cursor.getColumnIndex("metabolism"))
						);
//				System.out.println("............................date: "+history.date);
//				System.out.println("............................week: "+history.week);
				data.add(0, history);
//				data.add(history);
				cursor.moveToNext();
			}
		}

		// 关闭数据库
		dbReadDatabase.close();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout linearLayout = null;
		if (convertView != null) {
			linearLayout = (LinearLayout) convertView;
		}else {
			linearLayout = (LinearLayout) LayoutInflater.from(
					getContext()).inflate(R.layout.activity_2nd_weight_record_fat_history_cell, null);
//			System.out.println("................linearLayout: "+linearLayout.getId());
		}
		
		FatHistoryListCellData dataCell = getItem(position);
		
		//listCell更改
		TextView dateView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryDate);  
        TextView timeView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryTime);  
        TextView weightView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryWeight); 
        TextView fatView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryFat);  
        TextView waterView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryWater);  
        TextView muscleView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryMuscle); 
        TextView boneView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryBone);  
        TextView metabolismView = (TextView) linearLayout.findViewById(R.id.textView2ndFatHistoryMetabolism); 
		
		dateView.setText(dataCell.date);
		timeView.setText(dataCell.time);
		//根据单位处理一下体重的显示		
		unit = Activity4thSettingDefaultUnit.unit;
		if (unit) {
			weightView.setText(String.format("%.1f%s", dataCell.weight,"kg"));
		}else {
			// lb，转换体重
			float weightTrans = (float) (dataCell.weight * 2.20462);
//			BigDecimal b = new BigDecimal(
//					weightTrans);
//			weightTrans = b.setScale(2,
//					BigDecimal.ROUND_HALF_UP)
//					.floatValue();
			weightView.setText(String.format("%.1f%s", weightTrans,"lb"));
		}
		fatView.setText(String.format("%.1f%s", dataCell.fat,"%"));
		waterView.setText(String.format("%.1f%s", dataCell.water,"%"));
		muscleView.setText(String.format("%.1f%s", dataCell.muscle,"%"));
		boneView.setText(String.format("%.1f%s", dataCell.bone,"kg"));
		metabolismView.setText(dataCell.metabolism+"");
		
		return linearLayout;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	//public Object getItem(int position)
	public FatHistoryListCellData getItem(int position) {
		// TODO Auto-generated method stub
		//return null;
		return data.get(position);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return 0;
		return data.size();
	}
}
