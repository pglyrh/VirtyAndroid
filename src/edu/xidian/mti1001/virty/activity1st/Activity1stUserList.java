package edu.xidian.mti1001.virty.activity1st;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity3rd.Activity3rdNewScanFitnessScheme;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

//第一主页面（用户）
public class Activity1stUserList extends Activity implements OnClickListener,
		OnItemClickListener, OnQueryTextListener, OnScrollListener {
	// 上部两个“修改”、“添加”按钮
	private Button buttonUserEdit, buttonUserAdd;
	// “进入健康工具”按钮
	private Button buttonEnterHealthTools;
	// 用户列表
	private ListView listViewUser;
	// 上一次点击的View
	private View preView;
	private int scrolledY = 0;
//	int mycount = 0;
	// 搜索框
	private SearchView searchView;
	// 搜索框内容
	private String searchString;
	//搜索栏提示用
	private ArrayList<String> searchStrs = new ArrayList<String>();
//	private SearchListViewAdapter searchAdapter;
	private ArrayAdapter<String> searchAdapter;
	private ListView searchListView;
	// View popupWindow;
	private PopupWindow mPopupWindow;
	
	//TextView
	private TextView textViewName, textViewAge, textViewSex, textViewHeight, textViewWeight;
	
	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	// 数据库行指针
	private SimpleCursorAdapter adapter;
	// SimpleCursorAdapter adapter2;

	//静态数据，获得选择的用户
	public static String CHOOSE_USER_NAME = null;
	//目标体重
	public static float TARGET_WEIGHT;
	//身高
	public static int HEIGHT;
	//性别
	public static String SEX;
	
	//体重单位
	private String unitOfWeight;
	
	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_1st_user_list);

		// 获得按钮控件
		buttonUserEdit = (Button) findViewById(R.id.buttonUserEdit);
		buttonUserAdd = (Button) findViewById(R.id.buttonUserAdd);
		buttonEnterHealthTools = (Button) findViewById(R.id.buttonUserEnterHealthTools);
		searchView = (SearchView) findViewById(R.id.searchViewUser);

		// 添加点击事件监听器
		buttonUserEdit.setOnClickListener(this);
		buttonUserAdd.setOnClickListener(this);
		buttonEnterHealthTools.setOnClickListener(this);

		// 获得用户列表
		listViewUser = (ListView) findViewById(R.id.listViewUser);
		// 为listViewUser添加每项点击事件相应，用于一旁的具体内容显示
		listViewUser.setOnItemClickListener(this);
		// 滚动事件
		listViewUser.setOnScrollListener(this);

		//显示textview
		textViewName = (TextView) findViewById(R.id.textViewUserName);
		// 性别
		textViewSex = (TextView) findViewById(R.id.textViewUserSex);
		// 年龄
		textViewAge = (TextView) findViewById(R.id.textViewUserAge);
		// 身高
		textViewHeight = (TextView) findViewById(R.id.textViewUserHeight);
		// 目标体重
		textViewWeight = (TextView) findViewById(R.id.textViewUserWeight);
		
		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
		
		// cursor将在initListView中更改
		// 只显示用户名称
		// public SimpleCursorAdapter(Context context, int layout, Cursor c,
		// String[] from, int[] to)
		adapter = new SimpleCursorAdapter(this,
				R.layout.activity_1st_user_list_cell, null,
				new String[] { "name" },
				new int[] { R.id.textViewUserListName });
		listViewUser.setAdapter(adapter);
//		for (int i = 0; i < adapter.getCount(); i++) {
//			Cursor c = adapter.getCursor();
//			c.moveToPosition(i);
//			searchStrs.add(c.getString(c.getColumnIndex("name")));
//		}
//		System.out.println("............"+searchStrs.size());
		// 搜索框属性
//		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(false);
//		searchView.setQueryHint("请输入用户名");
		searchView.setOnQueryTextListener(this);
		
		// 无法解决suggestion（自动补全提示）
		// Cursor cursor = getTestCursor();
		// System.out.println("..........................count: "+cursor.getCount());
		// adapter2 = new
		// SimpleCursorAdapter(this,R.layout.activity_1st_user_list_cell,
		// cursor,
		// new String[] { "name" },new int[] { R.id.textViewUserListName });
		// System.out.println("..........................adapter: "+adapter2.getCount());
		// //搜索框提示
		// searchView.setSuggestionsAdapter(adapter2);

//		System.out.println("oncreate...........");
		
		// 第一次显示
//		initListView();
//		Toast.makeText(this, "oncreate", Toast.LENGTH_SHORT).show();
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resources = getResources();
		initListView();
	}
	
	// MainTabs.mTabHost.setCurrentTab(int index); 跳转到指定页面，定义在MainTabs.java中
	// 跳转页面：0 （主1用户） 1 （主2称重） 2 （主3健康时讯）3（主4设置） 5 （主1的编辑用户） 0 （主1的添加用户）

	@Override
	public void onClick(View v) {
		// 根据按钮按钮id
		// System.out.println("id:"+v.getId());
		switch (v.getId()) {
		case R.id.buttonUserEdit:
			// 跳转到编辑页面
			MainTabs.mTabHost.setCurrentTab(4);
			break;
		case R.id.buttonUserAdd:
			// 跳转到添加页面
			MainTabs.mTabHost.setCurrentTab(5);
			// startActivity(new Intent(this,ActivityNumberPicker.class));
			break;
		case R.id.buttonUserEnterHealthTools:
			// 跳转到Tab2（称重）
			MainTabs.setButtonSelected(1);
			break;
		default:
			break;
		}
	}

	// 自定义函数，用户通过更改adapter（通过cursor对数据库查询），进而刷新listView
	private void initListView() {
		dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,
		// String groupBy, String having,String orderBy)
		Cursor cursor = dbReadDatabase.query("user", null, null, null, null,
				null, null);
		// 刷新cursor
		adapter.changeCursor(cursor);
		searchStrs.clear();
		for (int i = 0; i < adapter.getCount(); i++) {
			Cursor c = adapter.getCursor();
			c.moveToPosition(i);
			searchStrs.add(c.getString(c.getColumnIndex("name")));
		}
//		System.out.println("............"+searchStrs.size());
/*		原來setAdapter是非同步(asynchronous)。		 
		只要在setAdapter()之后，加入post函式去更新ListView的ChildView即可。 */
//		//解决第一次载入，第一条项目变色
//		listViewUser.post(new Runnable(){
//		    public void run(){
//		        // fileList为与adapter做连结的list总数             
//		        if (adapter.getCount()==listViewUser.getChildCount()){  
////		            //对ListView中的ChildView进行操作。。。  
//		        		listViewUser.getChildAt(0).setBackgroundColor(0xff6fa8dc);
//		        }   
//		    }
//		});
		if (cursor.getCount()>0) {
//			Toast.makeText(this, "chooseUser"+this.CHOOSE_USER_NAME, Toast.LENGTH_SHORT).show();
			if (CHOOSE_USER_NAME == null) {
				//第一次启动
				// 总是显示第一个数据
				cursor.moveToFirst();
//				System.out.println("...............first");
				/*		原來setAdapter是非同步(asynchronous)。		 
				只要在setAdapter()之后，加入post函式去更新ListView的ChildView即可。 */
				CHOOSE_USER_NAME = cursor.getString(cursor.getColumnIndex("name"));
				
				//解决第一次载入，第一条项目变色
//				listViewUser.post(new Runnable(){
//				    public void run(){
////				    	System.out.println("...............run");
//				        // fileList为与adapter做连结的list总数 
////				    	System.out.println("...............adapter.getCount()"+adapter.getCount());
////				    	System.out.println("...............listViewUser.getChildCount()"+listViewUser.getChildCount());
////				        if (adapter.getCount()==listViewUser.getChildCount()){  
//////				            //对ListView中的ChildView进行操作。。。  
//////				        	System.out.println("...............adapter.getCount()==listViewUser.getChildCount()");
////				        		listViewUser.getChildAt(0).setBackgroundColor(0xff96c4ef);
////				        		for (int i = 1; i < listViewUser.getCount(); i++) {
////				        			listViewUser.getChildAt(i).setBackgroundColor(0x00000000);
////								}
////				        }else if (adapter.getCount()>=listViewUser.getChildCount()) {
////				        	listViewUser.getChildAt(0).setBackgroundColor(0xff96c4ef);
////						}
////				    	mycount++;
////				    	System.out.println(".......mycount"+mycount);
//				        //设置preView为第一个项目
//				        preView = listViewUser.getChildAt(0);
//				        listViewUser.getChildAt(0).setBackgroundColor(0xff96c4ef);
//				        
//				    }
//				});
			}else {
				cursor = dbReadDatabase.query("user", null, "name=?",
						new String[] { CHOOSE_USER_NAME }, null, null, null);
				cursor.moveToFirst();
//				listViewUser.post(new Runnable(){
//				    public void run(){
//				        // fileList为与adapter做连结的list总数             
//				        if (adapter.getCount()==listViewUser.getChildCount()){  
////				            //对ListView中的ChildView进行操作。。。 
//				        		listViewUser.getChildAt(0).setBackgroundColor(0xff96c4ef);
//				        		for (int i = 0; i < listViewUser.getCount(); i++) {
//				        			System.out.println(".............");
////				        			if (adapter.getItem(i).toString())) {
////										
////									}
////				        			listViewUser.getChildAt(i).setBackgroundColor(0x00000000);
//								}
//				        }   
//				    }
//				});
			}
			refreshTextView(cursor);			
		}else {
			refreshTextView(null);
		}

		// 关闭数据库
		dbReadDatabase.close();
	}

	// 更新右边的TextView
	private void refreshTextView(Cursor cursor) {
		//判断
		if (cursor == null) {
			// 姓名
			textViewName.setText("User");
			// 更改静态变量UserName
			CHOOSE_USER_NAME = null;
			// 性别
			textViewSex.setText("");
			// 年龄
			textViewAge.setText("");
			// 身高
			textViewHeight.setText("");
			// 目标体重
			//获得体重单位
			textViewWeight.setText("");
		}else {			
			// 读取用户项
			// _id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT DEFAULT NONE,
			// sex TEXT DEFAULT NONE,age INTEGER DEFAULT NONE,height INTEGER DEFAULT
			// NONE,
			// targetWeight FLOAT DEFAULT NONE
			//id
			Activity3rdNewScanFitnessScheme.ID = cursor.getInt(cursor.getColumnIndex("_id"));
			// 姓名
			textViewName.setText(cursor.getString(cursor.getColumnIndex("name")));
			// 更改静态变量UserName
			CHOOSE_USER_NAME = cursor.getString(cursor.getColumnIndex("name"));
//		Toast.makeText(this, "CHOOSE_USER_NAME"+CHOOSE_USER_NAME, Toast.LENGTH_SHORT).show();
			// 性别
			SEX = cursor.getString(cursor.getColumnIndex("sex"));
			String l = Locale.getDefault().getLanguage().toLowerCase();
//			System.out.println("Locale.getDefault().getLanguage().........."+Locale.getDefault().getLanguage());
			
			if (SEX.equals("男")) {
				textViewSex.setText(resources.getString(R.string.DialogMale));
			}else {
				textViewSex.setText(resources.getString(R.string.DialogFemale));
			}
			
/*			if (l.contains("zh")) {
				// 简体中文
				textViewSex.setText(SEX);
			}else if (l.contains("en")) {
				if (SEX.equals("男")) {
					textViewSex.setText("male");
				}else {
					textViewSex.setText("female");
				}
			}*/
			// 年龄
			textViewAge.setText(Calendar.getInstance().get(Calendar.YEAR)-cursor.getInt(cursor.getColumnIndex("age")) + "");
			// 身高
			HEIGHT = cursor.getInt(cursor.getColumnIndex("height"));
			textViewHeight.setText(cursor.getInt(cursor.getColumnIndex("height")) + "cm");
			// 目标体重
			//获得体重单位
			float weight = cursor.getFloat(cursor.getColumnIndex("targetWeight"));
			TARGET_WEIGHT = weight;
			if (Activity4thSettingDefaultUnit.unit) {
				unitOfWeight = "kg";
			}else {
				unitOfWeight = "lb";
				//转换体重
				weight = (float) (weight*2.20462);
				BigDecimal b = new BigDecimal(weight); 
				weight = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
			}
			textViewWeight.setText(weight + unitOfWeight);
		}
	}

	// 用户点击列表项，在旁边的页面显示每个用户的详细信息
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		System.out.println(".............................parent"+parent.toString());
//		System.out.println(".............................position"+position);
//		// 清空之前选中的
//		preView.setBackgroundColor(0x00000000);
//		//点击项目变色
//		if (parent.getChildCount()>0) {
////			System.out.println(".............................parent.getChildCount()"+parent.getChildCount());
//			//未翻页的情况下
//			if (scrolledY == 0) {
//				for (int i = 0; i < parent.getChildCount(); i++) {
//					View childview = parent.getChildAt(i);
//					if (i == position) {
//						childview.setBackgroundColor(0xff96c4ef);
//						preView = childview;
//					}else {					
//						childview.setBackgroundColor(0x00000000);
//					}
//				}
//			}else if (scrolledY > 0) {
//				//翻页
//				for (int i = 0; i < parent.getChildCount(); i++) {
//					View childview = parent.getChildAt(i);
//					if (i == position-scrolledY) {
//						childview.setBackgroundColor(0xff96c4ef);
//						preView = childview;
//					}else {					
//						childview.setBackgroundColor(0x00000000);
//					}
//				}
//			}			
//		}
		
		//问题是如何获得子View, 或者可以利用BaseAdapter中的ListView
		

		
//		if (parent.getCount()>0) {
//			for (int i = 0; i < listViewUser.getCount(); i++) {
//				listViewUser.getChildAt(i).setBackgroundColor(0x00000000);
//			}
////			view.setBackgroundColor(0xff96c4ef);
//		}
		
		
		//把view清理一下
//		view.clearFocus();
		//arg1=arg0.getChildAt(arg2) ;
//		view = parent.getChildAt(position);
//		view.setBackgroundColor(0xffff0000);
//		view.setSelected(true);
		
		// 获得cursor
		Cursor cursor = adapter.getCursor();
		// 利用cursor获得项
		cursor.moveToPosition(position);
	
		// 更改UserName
//		Activity3rdNewScanFitnessScheme.chooseUserName = cursor
//				.getString(cursor.getColumnIndex("name"));

		// 更新TextView
		refreshTextView(cursor);
	}

	//判断是否有指定字符串并返回字符串
	private ArrayList<String> getCertainStrs(String subString){
		ArrayList<String> arrayList= new ArrayList<String>();
		
		for (int i = 0; i < searchStrs.size(); i++) {
			if (searchStrs.get(i).contains(subString)) {
				arrayList.add(searchStrs.get(i));
			}
		}		
//		return (String[]) arrayList.toArray();
//		System.out.println("..........."+arrayList.size());
		return arrayList;		
	}
	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow(String str) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.popup_window, null);
		// radioGroup.setOnCheckedChangeListener(this);

		searchListView = (ListView) popupWindow.findViewById(R.id.listViewPopSearch);
		searchAdapter = new ArrayAdapter<String>(this,
				 R.layout.popup_window_list_cell);
//		searchAdapter = new SearchListViewAdapter(Activity1stUserList.this);
		searchAdapter.clear();
		if (!TextUtils.isEmpty(str)) {			
			searchAdapter.addAll(getCertainStrs(str));
		}else {
			searchAdapter.addAll(searchStrs);
		}
		searchListView.setAdapter(searchAdapter);
		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow, searchView.getWidth(), LayoutParams.WRAP_CONTENT);
//		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(false);
		// 需要设置一下此参数，点击外边可消失 
		//设置点击窗口外边窗口消失 
		mPopupWindow.setOutsideTouchable(true); 
		//设置弹出窗体需要软键盘，
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		//再设置模式，和Activity的一样，覆盖，调整大小。
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		//listview点击事件
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//获得选择的食物
				String choose = searchAdapter.getItem(position);
				searchView.setQuery(choose, false);
				mPopupWindow.dismiss();
			}
		});
	}
	@Override
	// 用户输入字符时激发该方法
	public boolean onQueryTextSubmit(String query) {
//		Toast.makeText(this, "您选择的是：" + query, Toast.LENGTH_SHORT).show();
		if (TextUtils.isEmpty(query)) {
			// 未输入任何信息，重启列表项内容
			initListView();
		} else {
			searchString = query;
			// 输入了查询条件，向数据库进行查询
			dbReadDatabase = db.getReadableDatabase();
			// query(String table, String[] columns, String selection,String[]
			// selectionArgs,
			// String groupBy, String having,String orderBy)
			//查询条件为name = query
			Cursor cursor = dbReadDatabase.query("user", null, "name=?",
					new String[] { searchString }, null, null, null);
			//查询成功
			if (cursor != null && cursor.getCount() > 0) {
				// 刷新cursor
				adapter.changeCursor(cursor);
				// 利用cursor获得项
				cursor.moveToFirst();
//				// 更改UserName
				CHOOSE_USER_NAME = cursor.getString(cursor.getColumnIndex("name"));
				// 更新TextView
				refreshTextView(cursor);
			} else {
				// 查询条件不正确
				// 弹出提示框，提示用户不存在
				Toast.makeText(this, resources.getString(R.string.ToastNotHaveUser), Toast.LENGTH_SHORT).show();
				searchString = "";
				initListView();
			}
			dbReadDatabase.close();
		}
		return true;
	}

	@Override
	// 用户提交时激发
	public boolean onQueryTextChange(String newText) {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss(); 				
		}
		if (TextUtils.isEmpty(newText)) {
			// 未输入任何信息
			CHOOSE_USER_NAME = null;
			initListView();
		} else {
			// 使用用户输入的内容对ListView的列表项进行过滤
			// listView.setFilterText(newText);
//			initPopuptWindow(str);
			initPopuptWindow(newText);
			mPopupWindow.showAsDropDown(searchView);
			return true;
		}
		return true;
	}
	// private Cursor getTestCursor() {
	// dbReadDatabase = db.getReadableDatabase();
	// Cursor cursor = null;
	// cursor = dbReadDatabase.query("user", null, null, null, null, null,
	// null);
	// System.out.println("..........................get: "+cursor.getCount());
	// dbReadDatabase.close();
	// return cursor;
	// }
	
	//记录上一次按下的时间，若连续按下则退出程序
	private long lastClickTime = 0;
	@Override
	public void onBackPressed() {
		if (lastClickTime <= 0) {
			Toast.makeText(this, resources.getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
			lastClickTime = System.currentTimeMillis();
		} else {
			long currentClickTime = System.currentTimeMillis();
			if ((currentClickTime - lastClickTime) < 1000) {
				// 退出
				// finish();
				// 关闭整个程序
				SysApplication.getInstance().exit();
			} else {
				Toast.makeText(this, resources.getString(R.string.ToastPressAgain), Toast.LENGTH_SHORT).show();
				lastClickTime = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 不滚动时保存当前滚动到的位置   
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {      
                scrolledY = listViewUser.getFirstVisiblePosition();      
        }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
}
