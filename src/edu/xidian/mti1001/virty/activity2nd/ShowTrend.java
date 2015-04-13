package edu.xidian.mti1001.virty.activity2nd;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.welcome.R;

public class ShowTrend extends View implements OnTouchListener{

	// 长、宽
	private int height, width;
	//y轴跨度
	private int scale = 5;
	//图片区域
	private Rect dst;
	//记录数组
	private ArrayList<TrendCellData> datas = new ArrayList<TrendCellData>(); 
	//周期（0代表周，1代表月，2代表季，3代表年）
	private int period = 0;
	//选中的circle
	private TrendCircle chooseCircle = null;
	//单位
	private boolean unit = true; 
	//最大值、最小值、均值
	private float max, min, avg;
	// target，画线基准
	private float target;
	private final double trans = 2.20462;
	//记录圆心坐标的数组，响应点击事件
	private ArrayList<TrendCircle> circles = new ArrayList<ShowTrend.TrendCircle>();
	
	public ShowTrend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//为记录数组赋值
	public void setDatas(ArrayList<TrendCellData> datas){
		this.datas = datas;
	}
	public ArrayList<TrendCellData> getDatas(){
		return datas;
	}
	//为周期赋值
	public void setPeriod(int period){
		this.period = period;
	}
	//单位
	public void setUnit(boolean unit){
		this.unit = unit;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		this.canvas = canvas;
		// 获取长、宽
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.AT_MOST);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.AT_MOST);
		height = this.getMeasuredHeight();
		width = this.getMeasuredWidth();
//		System.out.println("show trend: height: "+height+" width: "+width);
		
//		Paint p = new Paint();
//		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), 
//				R.drawable.weight_recorde_trend_trand_line),
//				0, 0, p);
		
		//绘制图片
		drawBackgroundPicture(canvas);
//		for (int i = 0; i < 7; i++) {			
//			drawATrend((width*(i+1)+158)/8, height/9, canvas);
//		}
//		drawATrend((width+158)/8, height/9, canvas);
		
		//计算scale
		computeValue();
		//绘制y轴
		drawOrdinateY(canvas);
		//绘制x轴
		drawOrdinateX(canvas);
		// 绘制趋势
		drawTrends(canvas);
		
		if (chooseCircle != null) {
			drawWeight(canvas, chooseCircle);
		}
		chooseCircle = null;
	}

	// 绘制图片
	public void drawBackgroundPicture(Canvas canvas){
		Paint p = new Paint();
		dst = new Rect();// 屏幕 >>目标矩形
		dst.left = 0;
		dst.top = 15;
		dst.right = width;
		dst.bottom = height-30;
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), 
				R.drawable.weight_record_trend_trand_line), null, dst, p);
	}
	// 绘制纵坐标（体重）
	public void drawOrdinateY(Canvas canvas) {
		int targetWeight = (int) Activity1stUserList.TARGET_WEIGHT;
		// 单位转换
		if (unit) {
			//kg
		}else {
			//lb
			targetWeight = (int) (Activity1stUserList.TARGET_WEIGHT*trans);
		}
		int x=0, y=0;
		Paint p = new Paint();
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		p.setTextSize(16);
		for (int i = 0; i < 9; i++) {			
			canvas.drawText((targetWeight+4*scale-i*scale)+"", 
					x+10, y+((height-45)*i+180)/9, p);
		}

	}
	
	// 绘制横坐标（日期）
	public void drawOrdinateX(Canvas canvas) {
		
		int x=0, y=0;
		Paint p = new Paint();
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		p.setTextSize(16);
		int count = datas.size();	
		
		//判断记录的数量是否大于7，不大于7则按实际数量绘图，大于9的都只
		if (count > 7) {
			
			//数量大于7
			for (int i = 0; i < 7; i++) {
				canvas.drawText(datas.get(count-7+i).date, x+width*(i+1)/8, y+height-10, p);				
			}			
		}else {
			//数量小于7
			for (int i = 0; i < count; i++) {
				canvas.drawText(datas.get(i).date, x+width*(i+1)/8, y+height-10, p);
			}
		}
		
		// 周期为“周”
//		if (period == 0) {
//			//判断记录的数量是否大于7，不大于7则按实际数量绘图，大于9的都只
//			if (count > 7) {
//				
//				//数量大于7
//				for (int i = 0; i < 7; i++) {
//					canvas.drawText(datas.get(count-7+i).date, x+width*(i+1)/8, y+height-10, p);				
//				}			
//			}else {
//				//数量小于7
//				for (int i = 0; i < count; i++) {
//					canvas.drawText(datas.get(i).date, x+width*(i+1)/8, y+height-10, p);
//				}
//			}			
//		}else if (period == 1){
//			ArrayList<TrendCellData> monthDatas = new ArrayList<TrendCellData>(); 
//			// 周期为“月”，每5天统计一次
//			String lastDay = datas.get(count-1).originDate;
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
//	        try {
////	        	System.out.println("变化");
//	            Date date = sdf.parse(lastDay);  
//	            Calendar calendar   = Calendar.getInstance();   
//	            calendar.setTime(date);   
//	            calendar.add(calendar.DATE,-5);//把日期往后增加一天.整数往后推,负数往前移动   
//	            date=calendar.getTime();   //这个时间就是日期往后推一天的结果   
//	            String putDate = sdf.format(date); //增加一天后的日期  
////	            System.out.println("putdate........."+putDate);
//	            
//	        } catch (ParseException e) {  
//	            e.printStackTrace();  
//	        }
//	        
//		}
		
	}

	
	// 绘制一个数据
	public void drawATrend(float x, float y, Canvas canvas) {
		// 画点
		Paint p = new Paint();
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		canvas.drawCircle(x, y, 5, p);// 小圆
		
		// 画直线 (startX, startY, stopX, stopY, paint)
		p.setStrokeWidth(2);	//宽度
		canvas.drawLine(x, y, x, height-31, p);				
	}
	
	// 绘制一组数据
	public void drawTrends(Canvas canvas){
		//先将保存圆心的数组清空
		circles.clear();
		int count = datas.size();
		// 判断记录的数量是否大于7，不大于7则按实际数量绘图，大于9的都只
		// drawATrend((width * (i + 1) + 158) / 8, (16+(height-45)*(i+1)/9),
		// canvas);
		// 单位转换
		int targetWeight = (int) Activity1stUserList.TARGET_WEIGHT;
		// 单位转换
		if (unit) {
			//kg
		}else {
			//lb
			targetWeight = (int) (Activity1stUserList.TARGET_WEIGHT*2.20462);
		}
		// 计算式子
		if (count > 7) {
			// 数量大于7
			for (int i = 0; i < 7; i++) {
				float x = (width * (i + 1) + 158) / 8;
				float y;
				if (unit) {
					y = 16 + (targetWeight + scale * 4 - datas.get(count - 7 + i).weight)
							* (height - 45) / (scale * 9);					
				}else {
					y = (float) (16 + (targetWeight + scale * 4 - datas.get(count - 7 + i).weight*trans)
							* (height - 45) / (scale * 9));	
				}
				drawATrend(x, y, canvas);
				//将圆心坐标保存到数组中
				circles.add(new TrendCircle(x, y, datas.get(count - 7 + i).weight));
			}
		} else {
			// 数量小于7
			for (int i = 0; i < count; i++) {
				float x = (width * (i + 1) + 158) / 8;
				float y;
				if (unit) {
					y = 16 + (targetWeight + scale * 4 - datas.get(i).weight)
							* (height - 45) / (scale * 9);					
				}else {
					y = (float) (16 + (targetWeight + scale * 4 - datas.get(i).weight*trans)
							* (height - 45) / (scale * 9));	
				}
				drawATrend(x, y, canvas);
				//将圆心坐标保存到数组中
				circles.add(new TrendCircle(x, y, datas.get(i).weight));
			}
		}
	}
	
	// 点击原点绘制体重
	public void drawWeight(Canvas canvas, TrendCircle circle){
		Paint p = new Paint();
		p.setTextSize(16);
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		// 判断单位
		if (unit) {			
			canvas.drawText(""+circle.getWeight(),
					circle.getCircleX()-17,circle.getCircleY()-10,p);
		}else {
			canvas.drawText(String.format("%.1f", circle.getWeight()*trans),
					circle.getCircleX()-20,circle.getCircleY()-10,p);
		}
	}
	
	//计算最大值、最小值、均值
	public void computeValue(){
		int count = datas.size();
		float sum = 0;
		if (count > 0) {
			max = datas.get(count-1).weight;
			min = datas.get(count-1).weight;
			avg = datas.get(count-1).weight;
			sum = datas.get(count-1).weight;
		}
		//判断记录的数量是否大于7，不大于7则按实际数量绘图，大于7的都只算最后7组
		if (count > 7) {
			// 数量大于7
			for (int i = 0; i < 7; i++) {
				if (datas.get(count - 7 + i).weight > max) {
					max = datas.get(count - 7 + i).weight;
				} else if (datas.get(count - 7 + i).weight < min) {
					min = datas.get(count - 7 + i).weight;
				}
				sum += datas.get(count - 7 + i).weight;
			}
			avg = sum/7;
//			System.out.println("avg: "+avg);
			target = avg;
		} else {
			// 数量小于7
			for (int i = 0; i < count; i++) {
				if (datas.get(i).weight > max) {
					max = datas.get(i).weight;
				} else if (datas.get(i).weight < min) {
					min = datas.get(i).weight;
				}
			}
			avg = sum/count;
//			System.out.println("avg: "+avg);
			target = avg;
		}
//		int size = count > 7 ? 7 : count;
//		// 数量大于7
//		for (int i = 0; i < size; i++) {
//			if (datas.get(i).weight > max) {
//				max = datas.get(i).weight;
//			} else if (datas.get(i).weight < min) {
//				min = datas.get(i).weight;
//			}
//			sum += datas.get(i).weight;
//		}
//		avg = sum / count;
//		System.out.println("............................max: "+max+" min: "+min);
		int targetWeight = (int)Activity1stUserList.TARGET_WEIGHT;
		//计算scale
		if (unit) {			
			//上下超过目标5kg则scale为5
			if (max <= targetWeight+4 && min >= targetWeight-4) {
				scale = 1;
			}else if (max < targetWeight+20 && min > targetWeight-20){
				scale = 5;
			}else {
				//计算scale,暂时将scale设置为10
				scale = 10;
			}
		}else {
			target = (int) (Activity1stUserList.TARGET_WEIGHT*trans);
			//上下超过目标5kg则scale为5
			if (max*trans <= targetWeight+4 && 
					min*trans >= targetWeight-4) {
				scale = 1;
			}else if (max*trans < targetWeight+20 && 
					min*trans > targetWeight-20){
				scale = 5;
			}else if (max*trans < targetWeight+40 && 
					min*trans > targetWeight-40){
				scale = 10;
			}else {
				//计算scale,暂时将scale设置为10
				scale = 20;
			}
		}
//		System.out.println("............................scale: "+scale);
//		}else {
//			//数量小于7
//			for (int i = 0; i < count; i++) {
//				if (datas.get(i).weight > max) {
//					max = datas.get(i).weight;
//				}else if (datas.get(i).weight < min) {
//					min = datas.get(i).weight;
//				}
//				sum += datas.get(i).weight;	
//			}
//		}
	}
	
	
	
	//内部类，用于记录圆的圆心坐标
	class TrendCircle{
		//圆心横纵坐标
		float x, y, weight;
		TrendCircle(float x, float y, float weight) {
			this.x = x;
			this.y = y;
			this.weight = weight;
		}
		//返回某一坐标是否在此圆内
		boolean isInside(float getX, float getY){
			//圆的范围扩大一点
			if (Math.pow(Math.abs(getX - x), 2) + Math.pow(Math.abs(getY - y), 2) <= Math.pow(20, 2)) {
				return true;
			}else {
				return false;
			}
		}
		//返回weight
		float getWeight(){
			return this.weight;
		}
		//返回坐标
		float getCircleX(){
			return this.x;
		}
		float getCircleY(){
			return this.y;
		}
	}



//	@Override
//	public void onClick(View arg0) {
//		System.out.println("............x"+arg0.getX());
//	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
//		System.out.println("............x0"+arg0.getX());
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//
//		  case MotionEvent.ACTION_DOWN:
//
//		   System.out.println("aaaaaaaaaaaaaaa");
//		   break;
//
//		  case MotionEvent.ACTION_UP:
//		   System.out.println("bbbbbbbbbbbbbbbbb");
//		   System.out.println("............x: "+event.getX());
//			System.out.println("............y: "+event.getY());
//			for (int i = 0; i < circles.size(); i++) {
//				System.out.println("circleX : "+circles.get(i).x);
//				System.out.println("circleY : "+circles.get(i).y);
//				if (circles.get(i).isInside(event.getX()-10, event.getY())) {
//					System.out.println("............x0"+event.getX());
//				}
//			}
////			return true;
//		   break;
//
//		  case MotionEvent.ACTION_MOVE:
//		   System.out.println("cccccccccccccccccc");
//		   
//		   break;
//		  }
//		return true;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {	
			
//			System.out.println("...................ontouch");
//			System.out.println("............x: "+event.getX());
//			System.out.println("............y: "+event.getY());
			int i;
			for (i = 0; i < circles.size(); i++) {
//				System.out.println("circleX : "+circles.get(i).x);
//				System.out.println("circleY : "+circles.get(i).y);
				if (circles.get(i).isInside(event.getX(), event.getY())) {
//					System.out.println("............circle："+circles.get(i).getWeight());
					// 将体重显示在圆点上方
//					Paint p = new Paint();
//					p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
//					canvas.drawText(""+circles.get(i).getWeight(),
//							circles.get(i).getCircleX()-10,circles.get(i).getCircleY()-15,p);
					chooseCircle = circles.get(i);
					this.invalidate();
					return true;
				}
			}
			if (i>=circles.size()) {
				chooseCircle = null;
				this.invalidate();
			}
			return false;
		}else {
			return super.onTouchEvent(event);
		}
	}
}
