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
import edu.xidian.mti1001.virty.welcome.R;

public class ShowFatTrend extends View implements OnTouchListener{

	// 长、宽
	private int height, width;
	//y轴跨度
	private float scale = 5;
	//图片区域
	private Rect dst;
	//记录数组
	private ArrayList<TrendFatCellData> datas = new ArrayList<TrendFatCellData>();
	//选择显示的指标项目，体重/脂肪/水分/肌肉/骨量/基础代谢
	private int healthItem = 0;
	//周期（0代表周，1代表月，2代表季，3代表年）
	private int period = 0;
	//选中的circle
	private TrendCircle chooseCircle = null;
	//单位
	private boolean unit = true; 
	//最大值、最小值、均值
//	private float max, min;
	private final double trans = 2.20462;
	// target，画线基准
	private float target;
	//记录圆心坐标的数组，响应点击事件
	private ArrayList<TrendCircle> circles = new ArrayList<TrendCircle>();
	
	public ShowFatTrend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//为记录数组赋值
	public void setDatas(ArrayList<TrendFatCellData> datas){
		this.datas = datas;
	}
	public ArrayList<TrendFatCellData> getDatas(){
		return datas;
	}
	//为周期赋值
	public void setPeriod(int period){
		this.period = period;
	}
	public int getPeriod(){
		return this.period;
	}
	//单位
	public void setUnit(boolean unit){
		this.unit = unit;
	}
	//显示的指标项
	public void setHealthItem(int item){
		this.healthItem = item;
	}
	public int getHealthItem(){
		return this.healthItem;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取长、宽
//		int w = View.MeasureSpec.makeMeasureSpec(0,
//				View.MeasureSpec.AT_MOST);
//		int h = View.MeasureSpec.makeMeasureSpec(0,
//				View.MeasureSpec.AT_MOST);
		height = this.getMeasuredHeight();
		width = this.getMeasuredWidth();
		
		//绘制背景图片
		drawBackgroundPicture(canvas);

		//根据项目，计算最大值、最小值、平均值等
		computeValue(this.healthItem);
		
		//绘制横纵坐标
		drawOrdinateY(canvas);
		drawOrdinateX(canvas);
		
		//绘制趋势
		drawTrends(canvas);
		
		if (chooseCircle != null) {
			drawData(canvas, chooseCircle);
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
		int x=0, y=0;
		Paint p = new Paint();
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		p.setTextSize(14);
		if (healthItem == 0) {
			for (int i = 0; i < 9; i++) {			
				canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
						x+12, y+((height-45)*i+180)/9, p);
			}
/*			if (!Activity4thSettingDefaultUnit.unit) {
				for (int i = 0; i < 9; i++) {			
					canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
							x+8, y+((height-45)*i+180)/9, p);
				}
			}else {
				for (int i = 0; i < 9; i++) {			
					canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
							x+8, y+((height-45)*i+180)/9, p);
//					canvas.drawText(String.format("%.1f", (int)target+4*scale-i*scale), 
//							x+8, y+((height-45)*i+180)/9, p);
				}
			}*/			
		}else if (healthItem == 5) {
			//基础代谢卡路里，数量值比较大
			for (int i = 0; i < 9; i++) {			
				canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
						x+5, y+((height-45)*i+180)/9, p);
			}
		}else if (healthItem == 4) {
			//骨量需要显示到0.1数量级
			for (int i = 0; i < 9; i++) {			
				canvas.drawText(String.format("%.1f", target+4*scale-i*scale), 
						x+10, y+((height-45)*i+180)/9, p);
			}
		}
		else {
			//其他健康单位
			for (int i = 0; i < 9; i++) {			
				canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
						x+13, y+((height-45)*i+180)/9, p);
			}			
/*			for (int i = 0; i < 9; i++) {			
				canvas.drawText(String.format("%d", (int)(target+4*scale-i*scale)), 
						x+7, y+((height-45)*i+180)/9, p);
			}			
*/		}

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
		
	}
	
	// 绘制一个数据
	public void drawATrend(float x, float y, Canvas canvas) {
		// 画点
		Paint p = new Paint();
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		canvas.drawCircle(x, y, 5, p);// 小圆
		
		// 画直线 (startX, startY, stopX, stopY, paint)
		p.setStrokeWidth(1);	//宽度
//		画直线
//		canvas.drawLine(x, y, x, height-31, p);	
		//画折现，连接两个原点
		//取得前一个点
		TrendCircle previousCircle;
		if (circles.size() > 1) {
			previousCircle = circles.get(circles.size()-2);
			canvas.drawLine(x, y, previousCircle.getCircleX(), previousCircle.getCircleY(), p);
		}
	}
	
	// 绘制一组数据
	public void drawTrends(Canvas canvas){
		//先将保存圆心的数组清空
		circles.clear();
		int count = datas.size();

		// 计算式子
		if (count > 7) {
			// 数量大于7
			for (int i = 0; i < 7; i++) {
				float x = (width * (i + 1) + 158) / 8;
				float y;
				//体重需转换
				if (healthItem == 0) {
					if (unit) {
						y = 16 + (target + scale * 4 - datas.get(count - 7 + i).weight)
								* (height - 45) / (scale * 9);					
					}else {
						y = (float) (16 + (target + scale * 4 - datas.get(count - 7 + i).weight*trans)
								* (height - 45) / (scale * 9));	
					}
				}else {
					y = 16 + (target + scale * 4 - datas.get(count - 7 + i).getValue(healthItem))
							* (height - 45) / (scale * 9);
				}
				//将圆心坐标保存到数组中
				circles.add(new TrendCircle(x, y, datas.get(count - 7 + i).getValue(healthItem)));
				drawATrend(x, y, canvas);
			}
		} else {
			// 数量小于7
			for (int i = 0; i < count; i++) {
				float x = (width * (i + 1) + 158) / 8;
				float y;
				if (healthItem == 0) {
					if (unit) {
						y = 16 + (target + scale * 4 - datas.get(i).weight)
								* (height - 45) / (scale * 9);					
					}else {
						y = (float) (16 + (target + scale * 4 - datas.get(i).weight*trans)
								* (height - 45) / (scale * 9));	
					}
				}else {
					y = 16 + (target + scale * 4 - datas.get(i).getValue(healthItem))
							* (height - 45) / (scale * 9);
				}
				//将圆心坐标保存到数组中
				circles.add(new TrendCircle(x, y, datas.get(i).getValue(healthItem)));
				drawATrend(x, y, canvas);
			}
		}
	}
	
	// 点击原点绘制体重
	public void drawData(Canvas canvas, TrendCircle circle){
		Paint p = new Paint();
		p.setTextSize(16);
		p.setAntiAlias(true);//此函数是用来防止边缘的锯齿
		if (healthItem==0) {
			// 判断单位
			if (unit) {			
				canvas.drawText(String.format("%.1f", circle.getData()),
						circle.getCircleX()-17,circle.getCircleY()-10,p);
			}else {
				canvas.drawText(String.format("%.1f", circle.getData()*trans),
						circle.getCircleX()-20,circle.getCircleY()-10,p);
			}
		}else if(healthItem == 5){
			canvas.drawText(String.format("%d", (int)circle.getData()),
					circle.getCircleX()-17,circle.getCircleY()-10,p);
		}
		else {
			canvas.drawText(String.format("%.1f", circle.getData()),
					circle.getCircleX()-15,circle.getCircleY()-10,p);
		}
	}
	
	//计算最大值、最小值、均值
	//scale计算有问题
	public void computeValue(int item){
		int count = datas.size();
		float sum = 0, avg = 0, max, min;
		if (count > 0) {
			max = datas.get(count-1).getValue(item);
			min = datas.get(count-1).getValue(item);
//			avg = 0;
//			sum = 0;
			//判断记录的数量是否大于7，不大于7则按实际数量绘图，大于7的都只算最后7组
			if (count > 7) {
				// 数量大于7
				for (int i = 0; i < 7; i++) {
					if (datas.get(count - 7 + i).getValue(item) > max) {
						max = datas.get(count - 7 + i).getValue(item);
					} else if (datas.get(count - 7 + i).getValue(item) < min) {
						min = datas.get(count - 7 + i).getValue(item);
					}
					sum += datas.get(count - 7 + i).getValue(item);
				}			
				//target为倒数第七组数据
//				target = datas.get(count - 7).getValue(item);
				avg = sum/7;
//				System.out.println("avg: "+avg);
				target = avg;
			} else {
				// 数量小于7
				for (int i = 0; i < count; i++) {
					if (datas.get(i).getValue(item) > max) {
						max = datas.get(i).getValue(item);
					} else if (datas.get(i).getValue(item) < min) {
						min = datas.get(i).getValue(item);
					}
					sum += datas.get(i).getValue(item);
				}
				//target为第1组数据
//				target = datas.get(0).getValue(item);
				avg = sum/count;
//				System.out.println("avg: "+avg);
				target = avg;
			}
			if (item == 0 && !unit) {
				//体重，且单位为lb
				target = (int) (target*trans);
				min = (float) (min*trans);
				max = (float) (max*trans);				
			}
			
			//除了骨量，其他的target都变为int型
//			if (item != 5) {
//				target = (int) target;
//			}
			
//			avg = sum / count;
			//计算scale，应该看最大值和最小值之间的差距/9和与target之间的差距
//			int interval1 = (int) Math.ceil((max - min)/9);  
			float interval1 = (float) (target - min)/4;  
			float interval2 = (float) (max - target)/4;
//			float interval1 = (float) Math.ceil((target - min)/4);  
//			float interval2 = (float) Math.ceil((max - target)/4);
//			System.out.println("interval1: "+interval1);
//			System.out.println("interval2: "+interval2);
			float interval = interval1>interval2?interval1:interval2;

			
			if (interval <= 0.1) {
				scale = (float) 0.1;
			}
			else if (interval <= 0.5) {
				scale = (float) 0.5;
			}
			else if (interval <= 1) {
				scale = 1;
			} else if (interval <= 2) {
				scale = 2;
			} 
			else {
				// 计算scale,scale为5的倍数
//				scale = interval+1;
				scale = ((int)Math.ceil(interval/5+1))*5;
			}
//			System.out.println("interval: "+interval);
//			System.out.println("scale: "+scale);
		}else {
			//没有记录
			max = 0;
			min = 0;
			target = 0;
		}		
	}
	
	
	
	//内部类，用于记录圆的圆心坐标
	class TrendCircle{
		//圆心横纵坐标
		float x, y, data;
		TrendCircle(float x, float y, float weight) {
			this.x = x;
			this.y = y;
			this.data = weight;
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
		float getData(){
			return this.data;
		}
		//返回坐标
		float getCircleX(){
			return this.x;
		}
		float getCircleY(){
			return this.y;
		}
	}

/*	//计算各项指标
	public float compute() {
		final double A = 2.877;
		final double B = 0.0009;
		final double C = 0.392;
		final double D = 0.00095;
		final double E = 4.5;
		final double F = 0.069;
		// 性别，男性为0，女性为1
		int sexInt = (Activity1stUserList.SEX.equals("男")) ? 0 : 1;
		// 瘦体重
		double lbm = A + B * height * height + C * weight - D * resistor - E
				* sexInt - F * age;
		// 脂肪含量
		fat = (float) ((weight - lbm) / weight);
		// 水分
		water = (float) ((lbm * 0.73) / weight);
		// 肌肉
		if (sexInt == 0) {
			muscle = (float) (24.4 + (334 + 7.78 * height - 9.8 * age) / weight);
		} else {
			muscle = (float) (24.4 + (7.74 * height - 318 - 9.8 * age) / weight);
		}
		// 骨量
		// 年龄判断
		if (age > 35) {
			bone = (float) (water * 0.18 - 2 * sexInt + 9 - 4 * age / 35);
		} else {
			bone = (float) (water * 0.18 - 2 * sexInt + 5 * age / 35);
		}
		// 基础代谢
		if (sexInt == 0) {
			metabolism = (int) (13.7 * weight + 5 * height - 6.8 * age + 66);
		} else {
			metabolism = (int) (9.6 * weight + 1.8 * height - 4.7 * age + 655);
		}
	}*/

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
//		System.out.println("............x0"+arg0.getX());
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
