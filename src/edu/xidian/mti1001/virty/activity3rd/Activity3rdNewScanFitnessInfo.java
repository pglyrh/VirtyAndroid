package edu.xidian.mti1001.virty.activity3rd;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class Activity3rdNewScanFitnessInfo extends Activity implements
		OnGestureListener, OnTouchListener {
	// 上部两个“返回”按钮
	private Button buttonBack;
	// 显示用户名
	private TextView textViewNumber, textViewInfo;
	// 识别手势操作的类
	private GestureDetector gestureDetector;
	// 每页显示的内容
	private ArrayList<String> arrayList;
	// 每页显示的内容颜色
	private int []arrayListColor;
	//内容大小
	int size;
	//当前页码
	int currentNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3rd_news_scan_fitness_info);
		//初始化detector
		gestureDetector = new GestureDetector(
				Activity3rdNewScanFitnessInfo.this, this);
		// 获得控件
		buttonBack = (Button) findViewById(R.id.button3rdFitnessInfoBack);
		textViewNumber = (TextView) findViewById(R.id.textView3rdFitnessInfoNumber);
		textViewInfo = (TextView) findViewById(R.id.viewPageFitnessInfo);
		// 添加监听器
		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到主3“健康时讯”页面
				MainTabs.mTabHost.setCurrentTab(2);
			}
		});
		
		//初始化显示的内容
		initArrayList();
		SysApplication.getInstance().addActivity(this); 
	}

	//初始化TextView中显示的内容
	//可以考虑以后用文件来实现（保存内容）
	private void initArrayList() {
		arrayList = new ArrayList<String>();
		arrayList.add("适量锻炼。参加锻炼不仅可以使骨骼、肌肉强壮发达，亦能促进大脑和各内脏器官的发育。");
		arrayList.add("骨量是指单位体积内骨组织的含量，代表骨骼健康的情况。注意补充流失的钙质，加强骨骼密度，是必须长期坚持的健康之道。"
		+"由于骨骼和肌肉关系密切，过瘦或运动不足都会令骨量减少。");
		arrayList.add("吃好早餐，不吃早餐造成人体血糖低下，对大脑的营养供应不足。早餐中鲜牛奶最为适宜，它不仅含有优质的蛋白质，"
		+"而且还含有大脑发育所必须的卵磷脂。");
		arrayList.add("关注自己的体重。科学家研究表明，人的体重在短期内有显著的减少或者增加，暗示了某种疾病的悄然袭来，" +
				"因此平时一定要重视自己的体重变化。");
		arrayList.add("保证充足的睡眠。睡眠是大脑休息和调整的阶段，睡眠不仅能保持大脑皮层细胞免于衰竭。良好的睡眠有增进记忆力的作用。");
		arrayList.add("饮水充足。水是人体的最主要的组成部分，研究发现，饮水不足时大脑衰老加快的一个重要原因。"
				+"人每天至少要饮用8杯水，以保证身体的需要。");
		arrayList.add("体水分是指体内的水分，包括血液、淋巴液、细胞外液、细胞内液等成分，有输送养分、回收废物、保持体温等重要技能。" +
				"体水分率的正常范围：女性：45%-60%；男性：50%-65%。");
		arrayList.add("不要带病用脑。在身体欠佳或患各种急性病的时候，就应该休息。这时如果仍然坚持学习用脑，" +
				"不仅效率低下，而且容易造成大脑的损失。");
		arrayList.add("体重与寿命成反比。人类现在和将来的大敌之一是肥胖。很多人不是死于饥饿、战争、灾难，" +
				"而是死于肥胖以及肥胖所引起的各种疾病，包括身体和心里的疾病。");
		arrayList.add("a) 研究表明，脂肪超标对人体的危害非同小可，脂肪超标引起的肥胖可增加心血管疾病的危险、" +
				"影响消化系统和内分泌的功能、增加癌症发生和危险性。"+'\n'+
				"b) 脂肪超标对女性健康有很严重的影响，可导致内分泌失调、妇科癌症的几率增加、糖尿病、高血压、心血管疾病、关节疾病等。");
		arrayList.add("健康美丽的身体 = 合理的运动+平衡膳食+积极心态+良好的生活习惯。");
		arrayList.add("饮用干净的水，并且不要用饮料代替白开水。不要等到口渴才喝水，水要一口一口的经常喝。" +
				"运动前2小时先喝两杯，运动时每15分钟最少喝半杯。");
		arrayList.add("饮水要注意最佳时间，早上起床后要适量多喝水，可补偿一夜之间失去的水分，上午10点和下午3点左右饮水可以补充" +
				"因工作流汗和排尿失去的水分，晚上8点左右饮水杯认为是最适宜的时间，可以冲淡血液，加速血液循环。");
		arrayList.add("保持充足睡眠："+'\n'+
				"a) 大多数人睡觉时间超过生命的1/3，充足的水面可以缓解人体在白天产生的压力，促进各个器官的新陈代谢、放松、排毒，还可以美容。"+'\n'+
				"b) 能取得较好睡眠质量的入睡时间是晚上9点到11点，中午12点到1点半，凌晨2点到3点。这时利于人体转入慢波水面。");
		arrayList.add("在户外，特别是夏天，剧烈运动90分钟以上，可在水中加入稀释的鲜果汁，加快补充电解质、糖分以及水分，" +
				"但避免高糖分的饮料，因为高糖分会影响身体对水分的吸收。");
		arrayList.add("坚持适度运动：运动应遵循因人而异、循序渐进的原则。针对不同的对象，不同的肥胖情况，应区别对待。" +
				"循序渐进即逐步增加运动负荷的原则，有一定强度刺激才能使机体的适应性改变。");
		arrayList.add("蔬菜五颜六色，每天吃全各种颜色的蔬果，营养价值非常全面。红色——令人振奋补铁养血；紫色——含碘丰富；" +
				"黑色——富含硒，抗衰老；绿色——含纤维素肠胃“清道夫”，保持身体酸碱平衡，减少癌症发病率；白色——蛋白质和钙质的源泉；" +
				"黄色——极高的维生素C含量，是最好的抗氧化剂。");
		arrayList.add("中华传统饮食文化鼓励吃五谷杂粮，多吃苹果、豆腐、辅之肉蛋，讲究食补、食疗，营养比较全面，但饮食结构中钙、" +
				"铁、蛋白质略显不足，可通过加奶补钙和蛋白质，适当加些瘦肉，并多用铁锅少用不粘锅补铁。规范饮食结构，为健康护航！");
		arrayList.add("生活中多注意以下几点可有效控制脂肪的增多：" +'\n'+
				"a) 控制蛋白质的摄入，摄入量过多会以脂肪形式存储起来；" +'\n'+
				"b) 少吃加工甜品，多食水果蔬菜的糖分；" +'\n'+
				"c) 少吃油炸食品；" +'\n'+
				"d) 控制饮酒，1克酒精能产7千卡热量，仅次于脂肪产的热量。");
		arrayList.add("两款室内健身良方" +'\n'+
				"a) 颤抖健身：先喝一杯凉开水，仰卧在床上，静止一分钟之后，双手缓缓向上举起，双脚竖起，四肢与身体形成90度角。" +
				"然后四肢同时轻轻抖动，每次3到5分钟，早晚各一次。" +'\n'+
				"b) 下蹲健身：双手叉腰、双脚与肩同宽，两眼平视，屈膝缓缓下蹲，脚跟离地，重心落在脚尖上，同时口念“哈”，" +
				"将腹中浊气吐出；起立时吸气，意守丹田，把新鲜空气吸入丹田。运动宜缓。");				
		size = arrayList.size();
		currentNum = 1;
		
		//颜色数组
		arrayListColor = new int[]{0xffff0000,0xffff6666,0xff8e7cc3,0xff3d85c6,0xff6aa84f}; 
		
		refreshText();
	}

	//刷新文本
	@SuppressLint("ResourceAsColor") 
	void refreshText(){
//		textViewInfo.setTextColor(R.color.blue);
//		textViewInfo.setTextColor(Color.YELLOW);
		textViewInfo.setTextColor(arrayListColor[(currentNum-1)%5]);
		textViewInfo.setText(arrayList.get(currentNum-1));
		textViewNumber.setText(currentNum+"/"+size);
	}
	
	// 使其可以相应滑动事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(event);
		return true;
	}

	// OnGestureListener中的函数
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		// System.out.println("onScroll");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	// 手指滑动事件
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
//		System.out.println("onFling");
		if (e1.getX() - e2.getX() > 0) {
			// Fling left
//			System.out.println("left");
			//页码加1
			currentNum ++;
			if (currentNum >= size) {
				currentNum = 1;
			}		
		} else if (e2.getX() - e1.getX() > 0) {
			// Fling right
//			System.out.println("right");
			//页码减1
			currentNum --;
			if (currentNum <= 0) {
				currentNum = size;
			}	
		}
		refreshText();
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		// 跳转到主3“健康时讯”页面
		MainTabs.mTabHost.setCurrentTab(2);
	}
}