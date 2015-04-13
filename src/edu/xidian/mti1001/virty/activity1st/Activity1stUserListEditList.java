package edu.xidian.mti1001.virty.activity1st;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

//从主Tab1点击“编辑”按钮，进入编辑用户列表，显示数据库中已存在的用户，选择某以用户，将可修改其属性
public class Activity1stUserListEditList extends Activity implements
		OnItemClickListener, OnItemLongClickListener {
	// 静态数据，用于获得操作者选的用户id
	public static int chooseId;
	public String chooseName;

	// 上部两个“返回”、“添加按钮”
	private Button buttonUserBack, buttonUserAdd;
	// 用户列表
	private ListView listViewUser;
	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	private SQLiteDatabase dbWriteDatabase;
	private SimpleCursorAdapter adapter;

	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_1st_user_list_edit_list);

//		System.out.println("Activity1stUserListEditList oncreate");

		// 获得控件
		buttonUserBack = (Button) findViewById(R.id.buttonUserEditBack);
		buttonUserAdd = (Button) findViewById(R.id.buttonUserEditAdd);

		// 监听器
		buttonUserBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到用户页面
				MainTabs.mTabHost.setCurrentTab(0);
			}
		});
		buttonUserAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到添加页面
				MainTabs.mTabHost.setCurrentTab(5);
			}
		});

		// 新建一个数据库Helper
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);

		// 获得编辑用户的列表
		listViewUser = (ListView) findViewById(R.id.listViewUserEditList);
		// cursor将在refreshListView中更改
		// 只显示用户名称
		// public SimpleCursorAdapter(Context context, int layout, Cursor c,
		// String[] from, int[] to)
		adapter = new SimpleCursorAdapter(this,
				R.layout.activity_1st_user_list_cell, null,
				new String[] { "name" },
				new int[] { R.id.textViewUserListName });
		listViewUser.setAdapter(adapter);
		// 为listViewUser添加每项点击事件，用于进入某一用户的具体编辑
		listViewUser.setOnItemClickListener(this);
		// 长按事件，可删除用户
		listViewUser.setOnItemLongClickListener(this);
		// 第一次显示
		refreshListView();
		
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		resources = getResources();
		
		refreshListView();
	}

	// 自定义函数，用户通过更改adapter（通过cursor对数据库查询），进而刷新listView
	private void refreshListView() {
		dbReadDatabase = db.getReadableDatabase();
		// query(String table, String[] columns, String selection,String[]
		// selectionArgs,
		// String groupBy, String having,String orderBy)
		Cursor cursor = dbReadDatabase.query("user", null, null, null, null,
				null, null);
		// 刷新cursor
		adapter.changeCursor(cursor);
		// 关闭数据库
		dbReadDatabase.close();
	}

	// 返回按钮，返回“用户页面”
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		MainTabs.mTabHost.setCurrentTab(0);
	}

	// 长按，删除用户
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// 长度大于1才能进行删除操作
		if (parent.getCount() > 1) {		
			// 弹出提示框，提示用户是否确定要删除
			new AlertDialog.Builder(Activity1stUserListEditList.this)
			.setTitle(resources.getString(R.string.DialogPrompt))
			.setMessage(resources.getString(R.string.DialogConfirmDeleteUser))
			.setPositiveButton(resources.getString(R.string.DialogSure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// 获得cursor
					Cursor cursor = adapter.getCursor();
					// 利用cursor获得项
					cursor.moveToPosition(position);
					
					// 获得id，列表项在数据库中的id，(_id)
					int itemId = cursor.getInt(cursor.getColumnIndex("_id"));
					chooseName = cursor.getString(cursor.getColumnIndex("name"));
					// 删除
					dbWriteDatabase = db.getWritableDatabase();
					// delete(String table, String whereClause, String[]
					// whereArgs)
					dbWriteDatabase.delete("user", "_id=?",
							new String[] { itemId + "" });
					// 更新列表
					refreshListView();
					dbWriteDatabase.close();
					dbWriteDatabase = db.getWritableDatabase();
					//删除所有与用户相关的记录，例如体重记录
					dbWriteDatabase.delete("userHealthRecord", "name=?", new String[] {chooseName});
					dbWriteDatabase.close();
					//处理一下第一页的问题
					if (Activity1stUserList.CHOOSE_USER_NAME.equals(chooseName)) {
						System.out.println("choose name");
						Activity1stUserList.CHOOSE_USER_NAME = null;
					} 
				}
			}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();
			// 响应长按事件，返回值为true，操作系统将为用户提供一个反馈（震动、声音），若为false，则表明长按不成功
			return true;
		}else {
			return false;
		}
	}

	// 短按，进入用户具体编辑页面
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获得cursor
		Cursor cursor = adapter.getCursor();
		// 利用cursor获得项
		cursor.moveToPosition(position);
		// //获得id，列表项在数据库中的id，(_id)
		chooseId = cursor.getInt(cursor.getColumnIndex("_id"));
//		System.out.println("id: "+chooseId);
		chooseName = cursor.getString(cursor.getColumnIndex("name"));
//		System.out.println("i: "+chooseName);
		// //将页面替换为编辑
		MainTabs.mTabHost.setCurrentTab(6);
	}
}
