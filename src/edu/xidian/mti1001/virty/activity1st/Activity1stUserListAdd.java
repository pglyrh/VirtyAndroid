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
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

//添加用户页面
public class Activity1stUserListAdd extends Activity implements OnClickListener {
	// 上部两个“返回”、“确认”
	private Button buttonUserAddCancel, buttonUserAddSure;

	// 获得修改List每一项的布局，实现点击，修改相应条目
	private LinearLayout zoneAddName, zoneAddSex, zoneAddAge, zoneAddHeight,
			zoneAddWeight;

	// textView
	private TextView textViewUserAddName, textViewUserAddSex,
			textViewUserAddAge, textViewUserAddHeight, textViewUserAddWeight;

	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbWriteDatabase, dbReadDatabase;

	// 设置的数据
	private String name, sex;
	private int chooseSex;
	private int age, height;
	private float weight;

	// 定义身高、体重选择器
	private NumberPicker numberPickerHeight;
	private NumberPicker numberPickerWeight1;
	private NumberPicker numberPickerWeight2;

	// 标志位，标识每一项是否都填写，1代表完成，0代表未完成
	private int[] flag = new int[] { 0, 0, 0, 0, 0 };
	
	// 语言
//	private boolean lflag = true;

	// 体重单位
	private String unitOfWeight;

	Resources resources;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_1st_user_list_add);

		// 获得控件
		buttonUserAddCancel = (Button) findViewById(R.id.buttonUserAddCancel);
		buttonUserAddSure = (Button) findViewById(R.id.buttonUserAddSure);
		// 获得TextView
		// 姓名
		textViewUserAddName = (TextView) findViewById(R.id.textViewUserAddName);
		// 性别
		textViewUserAddSex = (TextView) findViewById(R.id.textViewUserAddSex);
		// 年龄
		textViewUserAddAge = (TextView) findViewById(R.id.textViewUserAddAge);
		// 身高
		textViewUserAddHeight = (TextView) findViewById(R.id.textViewUserAddHeight);
		// 目标体重
		textViewUserAddWeight = (TextView) findViewById(R.id.textViewUserAddWeight);
		// 获得修改类目布局
		zoneAddName = (LinearLayout) findViewById(R.id.zoneAddName);
		zoneAddSex = (LinearLayout) findViewById(R.id.zoneAddSex);
		zoneAddAge = (LinearLayout) findViewById(R.id.zoneAddAge);
		zoneAddHeight = (LinearLayout) findViewById(R.id.zoneAddHeight);
		zoneAddWeight = (LinearLayout) findViewById(R.id.zoneAddWeight);

		// 监听器
		buttonUserAddCancel.setOnClickListener(this);
		buttonUserAddSure.setOnClickListener(this);
		// 添加类目
		zoneAddName.setOnClickListener(this);
		zoneAddSex.setOnClickListener(this);
		zoneAddAge.setOnClickListener(this);
		zoneAddHeight.setOnClickListener(this);
		zoneAddWeight.setOnClickListener(this);

		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);

		SysApplication.getInstance().addActivity(this);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resources = getResources();
		//判断语言
//		String l = Locale.getDefault().getLanguage().toLowerCase();
//		if (l.contains("zh")) {
//			// 简体中文
//			lflag = true;
//		}else if (l.contains("en")) {
//			// 英文
//			lflag = false;
//		}
	}
	
	// 将添加的数据保存到数据库中
	private void saveEditUser() {
		// 判断是否所有数据都完成填写
		int flags = 1;
		for (int i = 0; i < flag.length; i++) {
			flags *= flag[i];
		}
		if (flags != 0) {
			// 可写入的数据库
			dbWriteDatabase = db.getWritableDatabase();
			// 插入数据
			// 构建数据
			ContentValues contentValues = new ContentValues();
			contentValues.put("name", name);
			contentValues.put("sex", sex);
			// 有一些的存入数据不为String，需要进行处理
			// put的第二个参数可以不为string，为其相应的类型
			contentValues.put("age", age);
			contentValues.put("height", height);
			contentValues.put("targetWeight", weight);
			// 更改数据 dbWriteDatabase.insert(table, nullColumnHack, values)
			// 插入表中
			dbWriteDatabase.insert("user", null, contentValues);

			// 关闭数据库
			dbWriteDatabase.close();
			// 跳转到主1用户页面
			MainTabs.mTabHost.setCurrentTab(0);
		} else {
			// 保存失败的情况
			Toast.makeText(this, resources.getString(R.string.ToastHavenotDone), Toast.LENGTH_SHORT).show();
		}
	}

	// 清空所有数据
	private void clearAll() {
		// 数据清零（变量）
		name = "";
		sex = "";
		chooseSex = 0;
		age = 0;
		height = 0;
		weight = 0;
		// flag置零
		for (int i = 0; i < flag.length; i++) {
			flag[i] = 0;
		}
		
		// view更新
		textViewUserAddName.setText(resources.getString(R.string.Activity1UserListAddName));
		textViewUserAddSex.setText(resources.getString(R.string.Activity1UserListAddGender));
		textViewUserAddAge.setText(resources.getString(R.string.Activity1UserListAddAge));
		textViewUserAddHeight.setText(resources.getString(R.string.Activity1UserListAddHeight));
		textViewUserAddWeight.setText(resources.getString(R.string.Activity1UserListAddTargetWeight));
//		if (lflag) {
//			// 简体中文
//			textViewUserAddName.setText("用户名");
//			textViewUserAddSex.setText("性别");
//			textViewUserAddAge.setText("年龄");
//			textViewUserAddHeight.setText("身高");
//			textViewUserAddWeight.setText("目标体重");
//		}else {
//			textViewUserAddName.setText("name");
//			textViewUserAddSex.setText("gender");
//			textViewUserAddAge.setText("age");
//			textViewUserAddHeight.setText("height");
//			textViewUserAddWeight.setText("target weight");
//		}
	}

	// 返回按钮，返回“用户页面”
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		MainTabs.mTabHost.setCurrentTab(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		// 取消添加
		case R.id.buttonUserAddCancel:
			// 清空所有数据
			clearAll();
			// 跳转到用户页面
			MainTabs.mTabHost.setCurrentTab(0);
			break;

		// 确定添加
		case R.id.buttonUserAddSure:
			// 将添加数据保存到数据库中
			saveEditUser();
			// 清空所有数据
			clearAll();
			break;

		// 点击名字区域
		case R.id.zoneAddName:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(Activity1stUserListAdd.this)
					.setTitle(resources.getString(R.string.DialogInputName))
					.setView(editText)
					.setPositiveButton(resources.getString(R.string.DialogSure),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// 判断是否此人已存在
									dbReadDatabase = db.getReadableDatabase();
									Cursor cursorExist = dbReadDatabase.query(
											"user", null, "name=?",
											new String[] { editText.getText()
													.toString() }, null, null,
											null);
									if (cursorExist.getCount() == 0) {
										// 保存新name
										name = editText.getText().toString();
										// 判断是否为空
										if (name != null && !name.isEmpty()) {
											// 更改标志位
											flag[0] = 1;
											// 显示数据
											textViewUserAddName.setText(name);
										}
									} else {
										Toast.makeText(
												Activity1stUserListAdd.this,
												resources.getString(R.string.ToastHaveUser), Toast.LENGTH_SHORT)
												.show();
									}
									// cursorExist = null;
									dbReadDatabase.close();
								}
							}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;

		// 点击性别区域
		case R.id.zoneAddSex:
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
					flag[1] = 1;
					// 设置sex
					textViewUserAddSex.setText(resources.getString(R.string.DialogFemale));
					
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
					flag[1] = 1;
					// 设置sex
					textViewUserAddSex.setText(resources.getString(R.string.DialogMale));
					// 关闭对话框
					alertDialogInput.dismiss();
				}
			});
			
			// // 弹出性别单选框
			// new AlertDialog.Builder(this)
			// .setTitle("请选择您的性别")
			// .setSingleChoiceItems(new String[] { "男", "女" }, 0,
			// new DialogInterface.OnClickListener(){
			// @Override
			// public void onClick(DialogInterface arg0,
			// int arg1) {
			// // TODO Auto-generated method stub
			// // System.out.println("arg1: " + arg1);
			// chooseSex = arg1;
			// }
			//
			// }
			// )
			// .setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int which) {
			// // // 打印一下which
			// // System.out.println("which: " + which);
			// if (chooseSex == 0) {
			// sex = "男";
			// } else {
			// sex = "女";
			// }
			// //更改标志位
			// flag[1] = 1;
			// //保存sex
			// textViewUserAddSex.setText(sex);
			// }
			// }).setNegativeButton("取消", null).show();
			break;

		// 点击年龄区域
		case R.id.zoneAddAge:
			// 获得当前日期
			Calendar c = Calendar.getInstance();
			final int yearNow = c.get(Calendar.YEAR);
//			final int monthNow = c.get(Calendar.MONTH);
//			final int dayNow = c.get(Calendar.DAY_OF_MONTH);
			// 弹出日期选择器，获得用户选择的数据，修改年纪
			new DatePickerDialog(Activity1stUserListAdd.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// 修改相应的年龄
							// 根据选择的出生日期算出周岁
//							age = yearNow - year;
//							textViewUserAddAge.setText(age + "");
							//应在数据库中保存出生年份，显示的时候为当前年岁
							age = year;
							textViewUserAddAge.setText((yearNow-year) + "");
							// String.format("%d:%d:%d",
							// year,monthOfYear,dayOfMonth)
							// 更改标志位
							flag[2] = 1;
						}
					}, 1991, 1, 3).show();
			break;

		// 点击身高区域
		case R.id.zoneAddHeight:
			// 弹出选择身高的对话框
			// LayoutInflater inflater = getLayoutInflater();
			// View layout = inflater.inflate(
			// R.layout.activity_1st_user_list_number_picker_height,
			// (ViewGroup) findViewById(R.id.dialog_number_picker_height));
			// numberPickerHeight =
			// (NumberPicker)layout.findViewById(R.id.numberPickerHeight);
			// // np2 =
			// (NumberPicker)inflater.inflate(R.layout.activity_1st_user_list_number_picker,
			// // (ViewGroup) findViewById(R.id.np2));
			// numberPickerHeight.setMinValue(80);
			// numberPickerHeight.setMaxValue(90);
			//
			// new AlertDialog.Builder(Activity1stUserListAdd.this)
			// .setTitle("自定义布局").setView(layout)
			// .setPositiveButton("确定", null)
			// .setNegativeButton("取消", null).show();
			// 获得对话框布局
			LayoutInflater inflater1 = getLayoutInflater();
			View layout1 = inflater1.inflate(
					R.layout.activity_1st_user_list_number_picker_height,
					(ViewGroup) findViewById(R.id.dialog_number_picker_height));
			// 获得对话框中的数字选择器
			numberPickerHeight = (NumberPicker) layout1
					.findViewById(R.id.numberPickerHeight);
			// 指定最大值和最小值
			numberPickerHeight.setMinValue(130);
			numberPickerHeight.setMaxValue(210);
			numberPickerHeight.setValue(170);
			// 弹出对话框
			new AlertDialog.Builder(Activity1stUserListAdd.this)
					.setTitle(resources.getString(R.string.DialogSelectHeight))
					.setView(layout1)
					.setPositiveButton(resources.getString(R.string.DialogSure),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									height = numberPickerHeight.getValue();
									// 更改标志位
									flag[3] = 1;
									// System.out.println("height: "+height);
									// //保存值
									// height = chooseHeight;
									textViewUserAddHeight
											.setText(height + "cm");
								}
							}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;

		// 点击目标体重区域
		case R.id.zoneAddWeight:
			// 弹出选择身高的对话框
			// 获得对话框布局
			LayoutInflater inflater2 = getLayoutInflater();
			View layout2 = inflater2.inflate(
					R.layout.activity_1st_user_list_number_picker_weight,
					(ViewGroup) findViewById(R.id.dialog_number_picker_weight));
			// 获得对话框中的数字选择器
			numberPickerWeight1 = (NumberPicker) layout2
					.findViewById(R.id.numberPickerWeight1);
			numberPickerWeight2 = (NumberPicker) layout2
					.findViewById(R.id.numberPickerWeight2);
			// 指定最大值和最小值
			numberPickerWeight1.setMinValue(20);
			numberPickerWeight1.setMaxValue(100);
			numberPickerWeight1.setValue(55);
			numberPickerWeight2.setMinValue(0);
			numberPickerWeight2.setMaxValue(9);

			//

			// 弹出对话框
			new AlertDialog.Builder(Activity1stUserListAdd.this)
					.setTitle(resources.getString(R.string.DialogSelectWeight))
					.setView(layout2)
					.setPositiveButton(resources.getString(R.string.DialogSure),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									int w1 = numberPickerWeight1.getValue();
									int w2 = numberPickerWeight2.getValue();
									String w = "" + w1 + "." + w2;
									System.out.println("weight string: " + w);
									weight = Float.parseFloat(w);
									// 更改标志位
									flag[4] = 1;
									// 获得体重单位
									float weightTrans = weight;
									if (Activity4thSettingDefaultUnit.unit) {
										unitOfWeight = "kg";
									} else {
										unitOfWeight = "lb";
										// 转换体重
										weightTrans = (float) (weightTrans * 2.20462);
										BigDecimal b = new BigDecimal(
												weightTrans);
										weightTrans = b.setScale(1,
												BigDecimal.ROUND_HALF_UP)
												.floatValue();
									}
									textViewUserAddWeight.setText(weightTrans
											+ unitOfWeight);
								}
							}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			break;

		default:
			break;
		}
	}

}
