package edu.xidian.mti1001.virty.activity1st;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

//进入编辑用户列表后，选择某一用户，将可修改其属性，进入本页面
public class Activity1stUserListEdit extends Activity implements
		OnClickListener {
	// 上部两个“返回”、“添加按钮”
	private Button buttonUserEditCancel, buttonUserEditSure;

	// 获得修改List每一项的布局，实现点击，修改相应条目
	private LinearLayout zoneEditName, zoneEditSex, zoneEditAge,
			zoneEditHeight, zoneEditWeight;

	// textView
	private TextView textViewUserName, textViewUserEditName,
			textViewUserEditSex, textViewUserEditAge, textViewUserEditHeight,
			textViewUserEditWeight;

	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	private SQLiteDatabase dbWriteDatabase;

	// 此处应该想办法获得上一个Activity选择的用户值
	// 静态变量
	private int id;

	// 设置的数据
	private String originName, name, sex;
//	private int chooseSex;
	private int age, height;
	private float weight;

	// 定义身高、体重选择器
	private NumberPicker numberPickerHeight;
	private NumberPicker numberPickerWeight1;
	private NumberPicker numberPickerWeight2;

	// 体重单位
	private String unitOfWeight;

	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_1st_user_list_edit);
		id = Activity1stUserListEditList.chooseId;
//		System.out.println("edit id: "+id);
		// 获得控件
		buttonUserEditCancel = (Button) findViewById(R.id.buttonUserEditCancel);
		buttonUserEditSure = (Button) findViewById(R.id.buttonUserEditSure);

		// 获得TextView
		textViewUserName = (TextView) findViewById(R.id.textViewUserName);
		// 姓名
		textViewUserEditName = (TextView) findViewById(R.id.textViewUserEditName);
		// 性别
		textViewUserEditSex = (TextView) findViewById(R.id.textViewUserEditSex);
		// 年龄
		textViewUserEditAge = (TextView) findViewById(R.id.textViewUserEditAge);
		// 身高
		textViewUserEditHeight = (TextView) findViewById(R.id.textViewUserEditHeight);
		// 目标体重
		textViewUserEditWeight = (TextView) findViewById(R.id.textViewUserEditWeight);

		// 获得修改类目布局
		zoneEditName = (LinearLayout) findViewById(R.id.zoneEditName);
		zoneEditSex = (LinearLayout) findViewById(R.id.zoneEditSex);
		zoneEditAge = (LinearLayout) findViewById(R.id.zoneEditAge);
		zoneEditHeight = (LinearLayout) findViewById(R.id.zoneEditHeight);
		zoneEditWeight = (LinearLayout) findViewById(R.id.zoneEditWeight);

		// 监听器
		buttonUserEditCancel.setOnClickListener(this);
		buttonUserEditSure.setOnClickListener(this);
		// 修改类目
		zoneEditName.setOnClickListener(this);
		zoneEditSex.setOnClickListener(this);
		zoneEditAge.setOnClickListener(this);
		zoneEditHeight.setOnClickListener(this);
		zoneEditWeight.setOnClickListener(this);

		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);

		// 显示数据
//		refreshInfo();
		
		SysApplication.getInstance().addActivity(this); 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resources = getResources();
		id = Activity1stUserListEditList.chooseId;
//		System.out.println("edit resume");
		refreshInfo();
	}
	
	// 自定义函数，用户通过更改adapter（通过cursor对数据库查询），进而刷新listView
	private void refreshInfo(){	
//		System.out.println("refreshInfo..................");
    	dbReadDatabase = db.getReadableDatabase();
//    	query(String table, String[] columns, String selection,String[] selectionArgs,
//    			String groupBy, String having,String orderBy)
        Cursor cursor = dbReadDatabase.query("user", null, "_id=?", new String[]{id+""}, null, null, null);
//        System.out.println("cursor count: "+cursor.getCount());
//        System.out.println("id: "+id);
        cursor.moveToLast();
//        cursor.moveToPosition(0);

        //保存原来的名字
        originName = cursor.getString(cursor.getColumnIndex("name"));
        //将数据库中的值赋给本地变量
        name = cursor.getString(cursor.getColumnIndex("name"));
        sex = cursor.getString(cursor.getColumnIndex("sex"));
        age = cursor.getInt(cursor.getColumnIndex("age"));
        height = cursor.getInt(cursor.getColumnIndex("height"));
        weight = cursor.getFloat(cursor.getColumnIndex("targetWeight"));
        
//		//姓名
        textViewUserName.setText(name+"");
        textViewUserEditName.setText(name+"");
//		//性别
        if (sex.equals("男")) {
			textViewUserEditSex.setText(resources.getString(R.string.DialogMale));
		}else {
			textViewUserEditSex.setText(resources.getString(R.string.DialogFemale));
		}
//        textViewUserEditSex.setText(sex+"");

        //		//年龄
        textViewUserEditAge.setText(age+"");
//		//身高
        textViewUserEditHeight.setText(height+"cm");
//		//目标体重
        //获得体重单位
		float weightTrans = weight;
		if (Activity4thSettingDefaultUnit.unit) {
			unitOfWeight = "kg";
		} else {
			unitOfWeight = "lb";
			// 转换体重
			weightTrans = (float) (weightTrans * 2.20462);
			BigDecimal b = new BigDecimal(weightTrans);
			weightTrans = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		textViewUserEditWeight.setText(weightTrans + unitOfWeight);
//        //关闭数据库
        dbReadDatabase.close();
    }
	
	//将更改的数据保存到数据库中
	private void saveEditUser(){
    	//可写入的数据库
        dbWriteDatabase = db.getWritableDatabase();
        //插入数据
        //构建数据
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("sex", sex);
        contentValues.put("age", age);
        contentValues.put("height", height);
        contentValues.put("targetWeight", weight);
        //更改数据 dbWriteDatabase.update(table, values, whereClause, whereArgs)
        dbWriteDatabase.update("user", contentValues, "_id=?", new String[]{id+""});        
        //关闭数据库
        dbWriteDatabase.close();
    	
        //若名字变更，则体重记录数据也需要更新
        if (!originName.equals(name)) {
//        	System.out.println("。。。。。。。。。。。。。。更改名字");
        	dbWriteDatabase = db.getWritableDatabase();
        	//更改数据 dbWriteDatabase.update(table, values, whereClause, whereArgs)
        	contentValues = new ContentValues();
            contentValues.put("name", name);
            dbWriteDatabase.update("userHealthRecord", contentValues, "name=?", new String[]{originName});        
            //关闭数据库
            dbWriteDatabase.close();
		}
//        System.out.println("。。。。。。。。。。。。。。Activity1stUserList.CHOOSE_USER_NAME"+Activity1stUserList.CHOOSE_USER_NAME);
        //第一页选中的是否为更改项
        if (Activity1stUserList.CHOOSE_USER_NAME.equals(originName)) {
//        	System.out.println("。。。。。。。。。。。。。。第一页选中的是否为更改项");
        	Activity1stUserList.CHOOSE_USER_NAME = name;
		}
        
    	//保存失败的情况
    	//Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();  	
    }
	
	//返回按钮，返回“用户页面”
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		MainTabs.mTabHost.setCurrentTab(4);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonUserEditCancel:
			//跳转到编辑用户列表页面
			MainTabs.mTabHost.setCurrentTab(4);
			break;

		case R.id.buttonUserEditSure:
			//将更改的数据保存到数据库中，改进方案->设立标志位，如果又改才将此项写入数据库中
			saveEditUser();			
			//跳转到编辑用户列表页面
			MainTabs.mTabHost.setCurrentTab(4);
			break;
			
		case R.id.zoneEditName:
			//弹出对话框，输入新用户名
			//输入框初始内容为原用户名
			final EditText editText = new EditText(this);
			editText.setText(textViewUserEditName.getText());
			//弹出对话框，提醒输入新名称
			//.setIcon(android.R.drawable.ic_dialog_info).
			new AlertDialog.Builder(Activity1stUserListEdit.this).setTitle(resources.getString(R.string.DialogInputName)).
			setView(editText).setPositiveButton(resources.getString(R.string.DialogSure), 
					new DialogInterface.OnClickListener() {  
		                    @Override  
		                    public void onClick(DialogInterface arg0, int arg1) {  
		                        //数据获取   
		                    	if (name != null && !name.isEmpty()) {
		                    		//保存新name
		                    		name = editText.getText().toString();  
		                    		textViewUserEditName.setText(name);  
								}
		                    }  
		                })
				     .setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;
		
		case R.id.zoneEditSex:
			final String l = Locale.getDefault().getLanguage().toLowerCase();

			// 弹出导出对话框
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(
					R.layout.activity_1st_user_list_add_dialog_gender,
					(ViewGroup) findViewById(R.id.dialog_fat_manual));
			// 弹出对话框
			final AlertDialog alertDialogInput = new AlertDialog.Builder(
					getParent()).setView(layout).show();
			// 获得dialog中的控件
			ImageView imageViewFemale = (ImageView) layout
					.findViewById(R.id.imageViewGenderDialogFemale);
			ImageView imageViewMale = (ImageView) layout
					.findViewById(R.id.imageViewGenderDialogMale);
			// 选择女
			imageViewFemale.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 保存sex
					sex = "女";
					// 更改标志位
					// 设置sex
					textViewUserEditSex.setText(resources.getString(R.string.DialogFemale));
					
					// 关闭对话框
					alertDialogInput.dismiss();
				}
			});
			// 选择男
			imageViewMale.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sex = "男";
					// 更改标志位
					// 设置sex
					textViewUserEditSex.setText(resources.getString(R.string.DialogMale));
					// 关闭对话框
					alertDialogInput.dismiss();
				}
			});
//			// 弹出性别单选框
//			new AlertDialog.Builder(this)
//					.setTitle("请选择性别")
//					.setSingleChoiceItems(new String[] { "男", "女" }, 0, new DialogInterface.OnClickListener(){
//						@Override
//						public void onClick(DialogInterface arg0,
//								int arg1) {
//							// TODO Auto-generated method stub
////							System.out.println("arg1: " + arg1);
//							//保存选择项
//							chooseSex = arg1;
//						}
//				
//			}).setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
////									System.out.println("which: "+ which);
//									if (chooseSex == 0) {
//										sex = "男";
//									} else {
//										sex = "女";
//									}
////									System.out.println("sex: "+sex);
//									textViewUserEditSex.setText(sex);
//								}
//							}).setNegativeButton("取消", null).show();
			break;
			
		case R.id.zoneEditAge:
			//获得当前日期
			Calendar c = Calendar.getInstance();
			final int yearNow = c.get(Calendar.YEAR);
//			final int monthNow = c.get(Calendar.MONTH);
//			final int dayNow = c.get(Calendar.DAY_OF_MONTH);
			//弹出日期选择器，获得用户选择的数据，修改年纪
			new DatePickerDialog(Activity1stUserListEdit.this, new DatePickerDialog.OnDateSetListener() {				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					//修改相应的年龄
					//根据选择的出生日期算出周岁	
//					age = yearNow - year;
//					textViewUserEditAge.setText(age+"");
					//应在数据库中保存出生年份，显示的时候为当前年岁
					age = year;
					textViewUserEditAge.setText((yearNow-year) + "");
					//String.format("%d:%d:%d", year,monthOfYear,dayOfMonth)
				}
			}, 1991, 1, 3).show();
			break;
		
		case R.id.zoneEditHeight:
			//滚轮选择身高
			//弹出选择身高的对话框
			//获得对话框布局
			LayoutInflater inflater1 = getLayoutInflater();
			View layout1 = inflater1.inflate(
					R.layout.activity_1st_user_list_number_picker_height,
					(ViewGroup) findViewById(R.id.dialog_number_picker_height));
			//获得对话框中的数字选择器
			numberPickerHeight = (NumberPicker)layout1.findViewById(R.id.numberPickerHeight);
			//指定最大值和最小值
	        numberPickerHeight.setMinValue(130);
	        numberPickerHeight.setMaxValue(210);
	        numberPickerHeight.setValue(height);
	        //弹出对话框
			new AlertDialog.Builder(Activity1stUserListEdit.this)
					.setTitle(resources.getString(R.string.DialogSelectHeight))
					.setView(layout1)
					.setPositiveButton(resources.getString(R.string.DialogSure),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
//									//保存值
									height = numberPickerHeight.getValue();
									textViewUserEditHeight.setText(height+"cm");
								}
							}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;
			
		case R.id.zoneEditWeight:
			//滚轮显示体重
			//获得对话框布局
			LayoutInflater inflater2 = getLayoutInflater();
			View layout2 = inflater2.inflate(
					R.layout.activity_1st_user_list_number_picker_weight,
					(ViewGroup) findViewById(R.id.dialog_number_picker_weight));
			//获得对话框中的数字选择器
			numberPickerWeight1 = (NumberPicker)layout2.findViewById(R.id.numberPickerWeight1);
			numberPickerWeight2 = (NumberPicker)layout2.findViewById(R.id.numberPickerWeight2);
			//指定最大值和最小值
	        numberPickerWeight1.setMinValue(20);
	        numberPickerWeight1.setMaxValue(100);
	        numberPickerWeight1.setValue((int)weight);
	        numberPickerWeight2.setMinValue(0);
	        numberPickerWeight2.setMaxValue(9);

	        //弹出对话框
			new AlertDialog.Builder(Activity1stUserListEdit.this)
					.setTitle(resources.getString(R.string.DialogSelectWeight))
					.setView(layout2)
					.setPositiveButton(resources.getString(R.string.DialogSure),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									int w1 = numberPickerWeight1.getValue();
									int w2 = numberPickerWeight2.getValue();
									String w = ""+w1+"."+w2;
//									System.out.println("weight string: "+w);
									weight = Float.parseFloat(w);
									//获得体重单位
			                		float weightTrans = weight;
			                		if (Activity4thSettingDefaultUnit.unit) {
			                			unitOfWeight = "kg";
			                		} else {
			                			unitOfWeight = "lb";
			                			// 转换体重
			                			weightTrans = (float) (weightTrans * 2.20462);
			                			BigDecimal b = new BigDecimal(weightTrans);
			                			weightTrans = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
			                		}
			                		textViewUserEditWeight.setText(weightTrans + unitOfWeight);
								}
							}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;
			
		default:
			break;
		}
	}
}
