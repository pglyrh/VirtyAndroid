package edu.xidian.mti1001.virty.activity3rd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity3rdNewScanTodayRecipeRecommend extends Activity implements OnClickListener{
	// 上部“返回”按钮
	private Button buttonBack;
	//图片、名字、成分、做法
	private ImageView imageViewFood;
	private TextView textViewName, textViewMaterials, textViewMethod;	
	
	//food
	private FoodRecipe foodRecipe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan_today_recipe_recommend);
				
		//获得控件
		buttonBack = (Button) findViewById(R.id.button3rdTodayRecipeRecommendBack);
		imageViewFood = (ImageView) findViewById(R.id.imageView3rdTodayRecipeRecommend);
		textViewName = (TextView) findViewById(R.id.textViewTodayRecipeRecommendName);
		textViewMaterials = (TextView) findViewById(R.id.textViewTodayRecipeRecommendMaterials);
		textViewMethod = (TextView) findViewById(R.id.textViewTodayRecipeRecommendMethod);
		//TextView滚动条
		textViewMethod.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		//获得开启这个页面的Activity所传来的对象
		Intent intent = this.getIntent(); 
		foodRecipe=(FoodRecipe)intent.getSerializableExtra("food");
//		System.out.println("foodRecipe: "+foodRecipe.getFoodName());
		showFood();
		
		//添加监听器
		buttonBack.setOnClickListener(this);
		
		SysApplication.getInstance().addActivity(this); 
	}

	
	
	public FoodRecipe getFoodRecipe() {
		return foodRecipe;
	}


	public void setFoodRecipe(FoodRecipe foodRecipe) {
		this.foodRecipe = foodRecipe;
	}

	//显示相应的食物
//	String []recipes = new String[]{"牛肉山药粥","桂圆粥","皮蛋瘦肉粥","营养咸豆花",
//	"蘑菇蔬菜意面","海蛎烧豆腐","蟹黄银杏汤"};
	public void showFood(){
		//设置
		imageViewFood.setImageResource(foodRecipe.getImageId());
		textViewName.setText(foodRecipe.getFoodName());
		textViewMaterials.setText(foodRecipe.getMaterial());
		textViewMethod.setText(foodRecipe.getMethod());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//今日食谱
		case R.id.button3rdTodayRecipeRecommendBack:
			onBackPressed();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
