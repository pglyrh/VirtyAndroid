package edu.xidian.mti1001.virty.activity3rd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity3rdNewScanTodayRecipe extends Activity implements OnClickListener, OnItemClickListener {
	// 上部“返回”按钮
	private Button buttonBack;
	//食谱列表
	private ListView listViewRecipe;
	private ArrayAdapter<FoodRecipe> adapter;
//	private ArrayAdapter<String> adapter;
	FoodRecipeArray foodRecipeArray;
	//选择的食物
	FoodRecipe foodRecipe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan_today_recipe);
				
		//获得控件
		buttonBack = (Button) findViewById(R.id.button3rdTodayRecipeBack);
		listViewRecipe = (ListView) findViewById(R.id.listView3rdTodayRecipeList);
		
		//添加监听器
		buttonBack.setOnClickListener(this);
		//为listViewRecipe添加食谱列表，点击相应项目，显示对应食物的食谱
		listViewRecipe.setOnItemClickListener(this);
		
		//列表适配器
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//		//添加数据
//		String []recipes = new String[]{"牛肉山药粥","桂圆粥","皮蛋瘦肉粥","营养咸豆花",
//				"蘑菇蔬菜意面","海蛎烧豆腐","蟹黄银杏汤"};
//		adapter.addAll(recipes);
////		adapter.add("aaa");
//		listViewRecipe.setAdapter(adapter);
        //添加数据
		//列表适配器
		adapter = new ArrayAdapter<FoodRecipe>(this, android.R.layout.simple_list_item_1);
        foodRecipeArray = new FoodRecipeArray();
        adapter.addAll(foodRecipeArray.getRecipes());
        listViewRecipe.setAdapter(adapter);
        
        SysApplication.getInstance().addActivity(this); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//今日食谱
		case R.id.button3rdTodayRecipeBack:
			//返回主3页面
			MainTabs.mTabHost.setCurrentTab(2);
			break;
		default:
			break;
		}
	}

	//点击列表项中的食物，显示相应的食谱
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//获得选择的食物
		foodRecipe = foodRecipeArray.getItem(position);	
//		System.out.println("name: "+foodRecipe.getFoodName());
		//跳转到显示选择的食物详细食谱的页面
		Intent intent = new Intent(this, Activity3rdNewScanTodayRecipeRecommend.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("food", foodRecipe);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主3“健康时讯”页面
		MainTabs.mTabHost.setCurrentTab(2);
	}
}
