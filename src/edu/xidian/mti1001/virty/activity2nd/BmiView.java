package edu.xidian.mti1001.virty.activity2nd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import edu.xidian.mti1001.virty.welcome.R;

public class BmiView extends View implements Runnable {
	private final Context context;
	// 屏幕长、宽
	int width, height;
	// 屏幕中心x， y
	int centerX;
	int centerY;
	// 分别为刻度盘、刻度、小球绘制区域
	RectF rectF1, rectF2, rectF3;
	// 刻度绘制区域的原始值（调整过的）
	float rx, ry;
	float rl, rr, rt, rb;

	// 是否为第一次加载，用于初始化
	boolean flag = false;

	// 其他单位数据
	int pointInterval, ballInterval, delta, delta2;

	// ball上的内容
	String ballNumber = "60.0";
	// 刻度盘数据
	String meterNumber = "60.0";

	// 图片旋转角度
	float Angle,Angle2 = 0;
	// 构建Matrix对象
	Matrix mMatrix = new Matrix();
	// 声明Bitmap对象（刻度）
	Bitmap mBitQQ, mBitQQ2 = null;
	int mBitQQX = 0;
	int mBitQQY = 0;
	// 小球图片
	Bitmap bitmapBall = null;
	// 刻度盘图片
	Bitmap bitmapMeter = null;

	public BmiView(Context context) {
		this(context, null);
	}

	public BmiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 装载刻度图片资源
		mBitQQ = BitmapFactory.decodeResource(getResources(), R.drawable.image_weight_scale);
		mBitQQX = mBitQQ.getWidth();
		mBitQQY = mBitQQ.getHeight();
		
		//小球图片
		bitmapBall = BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_green);
		
		// 刻度盘图片
		bitmapMeter = BitmapFactory.decodeResource(getResources(),
				R.drawable.weight_record_meter);
		
		// 开启线程
//		new Thread(this).start();
	}

	// 设置内容
	public void setBallNumber(String num) {
		this.ballNumber = num;
		//计算
		computeAngel();
	}

	public String getBallNumber() {
		return this.ballNumber;
	}
	public void setMeterNumber(String num) {
		this.meterNumber = num;
	}
	
	public String getMeterNumber() {
		return this.meterNumber;
	}
	// 设置旋转角度
	public void setAngle(float angle) {
		this.Angle = angle;
	}

	public float getAngle() {
		return this.Angle;
	}
	// 设置小球图片
	public void setBallImage(Bitmap bitmap){
		this.bitmapBall = bitmap;
	}

	// 初始化view的一些参数
	public void initView() {
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// 屏幕尺寸
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		width = getWidth();
		height = getHeight();

		// 调整间隔
		pointInterval = (int) (width / 2.05);
		delta = width / 65;
		delta2 = width / 45;
		ballInterval = (int) (width / 9.6);

		if (!flag) {
			// 刻度
			rectF2 = new RectF(centerX - pointInterval + delta2, centerY
					- pointInterval + delta, centerX + pointInterval - delta2,
					centerY + pointInterval - delta);
			// 刻度盘
			rectF1 = new RectF(centerX - width / 2, rectF2.top, centerX + width
					/ 2, centerY);

			// 记录第一次刻度的绘制尺寸
			rx = rectF2.width();
			ry = rectF2.height();
			rl = rectF2.left;
			rr = rectF2.right;
			rt = rectF2.top;
			rb = rectF2.bottom;
			// System.out.println("rx:" + rx);
			// System.out.println("ry:" + ry);

			// 圆球
			rectF3 = new RectF(centerX - width / 11,
					rectF1.bottom - width / 16, centerX + width / 11,
					rectF1.bottom - width / 16 + width / 11 + width / 11);

			// 更改标志位
			flag = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initView();

		// 绘制刻度盘
		drawMeter(rectF1, canvas);

		// 绘制刻度
		drawKedu(rectF2, canvas);

		// 绘制小球
		drawBall(rectF3, ballInterval, canvas);
		
		drawText(rectF1, 0, canvas);

		drawMeterNumber(canvas);
		
		super.onDraw(canvas);
	}

	// 绘制刻度盘
	public void drawMeter(RectF oval1, Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmapMeter, null, oval1, paint);
	}

	public void drawKedu(RectF oval1, Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		// //重置mMatrix对象
		mMatrix.reset();
		mMatrix.setTranslate(rectF2.centerX(), rectF2.centerY()); // 设置图片的旋转中心，即绕（X,Y）这点进行中心旋转
		// //设置旋转
		mMatrix.setRotate(Angle);

		// 生成旋转后的新图片
		// 如果图片还没有回收，强制回收  
		if (mBitQQ2!=null && !mBitQQ2.isRecycled()) {  
			mBitQQ2.recycle();  
		} 
		mBitQQ2 = Bitmap.createBitmap(mBitQQ, 0, 0, mBitQQX, mBitQQY, mMatrix,
				true);
		// 新图片的宽、高
		int x = mBitQQ2.getWidth();
		int y = mBitQQ2.getHeight();
		// 与原图片的差距，并更改绘制区域，使刻度看起来大小未变
		float dx = (x - mBitQQX) * rx / mBitQQX;
		float dy = (y - mBitQQY) * ry / mBitQQY;

		// 重置刻度的外围轮廓
		oval1.left = rl - dx / 2;
		oval1.top = rt - dy / 2;
		oval1.right = rr + dx / 2;
		oval1.bottom = rb + dy / 2;

		// 绘制图片
		canvas.drawBitmap(mBitQQ2, null, oval1, paint);
	}

	// 绘制圆球
	public void drawBall(RectF oval1, int width, Canvas canvas) {

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmapBall,null, oval1, paint);
	}

	// 绘制圆球上的数据
	public void drawText(RectF oval1, int ringWidth, Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 255, 255);
		paint.setTextAlign(Align.CENTER);
		float sizesp = px2sp(context, 22);
//		System.out.println("size........"+px2sp(context, sizesp));
//		paint[i].setTextSize(30);
        paint.setTextSize(sp2px(context, sizesp));
		
		RectF itemOval=new RectF(rectF3.left, rectF3.top,rectF3.right,rectF3.top+rectF3.height()/2);
		canvas.drawText("BMI", itemOval.centerX(), itemOval.bottom-itemOval.width()/15, paint);

		RectF itemOval2=new RectF(rectF3.left, rectF3.bottom-rectF3.height()/2,rectF3.right,rectF3.bottom);
		canvas.drawText(ballNumber, itemOval.centerX(), itemOval2.bottom-itemOval2.width()/4, paint);
		
		//刻度盘
		paint.setARGB(255, 0, 0, 0);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
//		RectF meterNumberOval=new RectF(rectF1.left, rectF1.top,rectF1.right,rectF1.bottom);
//		RectF meterNumberOval=new RectF((float)(centerX-width/4.4), rectF1.top+height/9,
//				(float)(centerX+width/4.5),rectF1.bottom-height/13);
//		RectF meterNumberOval=new RectF((float)(centerX-width/4.4), (float)(rectF1.top+width/6.2),
//				(float)(centerX+width/4.4),(float)(rectF1.bottom-width/9.2));
//		canvas.drawRect(meterNumberOval, paint);
		
	}
	
	//刻度盘上的数据
	public void drawMeterNumber(Canvas canvas) {
		// 文字居中
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 255, 255);
		// 刻度盘
		//水平居中
		paint.setTextAlign(Align.CENTER);
		// 绘制盘上数据
		paint.setTextSize(65);
		// RectF meterNumberOval=new RectF(rectF1.left,
		// rectF1.top,rectF1.right,rectF1.bottom);
		// RectF meterNumberOval=new RectF((float)(centerX-width/4.4),
		// rectF1.top+height/9,
		// (float)(centerX+width/4.4),rectF1.bottom-height/13);
		RectF meterNumberOval = new RectF((float) (centerX - width / 4.4),
				(float) (rectF1.top + width / 6.2),
				(float) (centerX + width / 4.4),
				(float) (rectF1.bottom - width / 9.2));
		// canvas.drawRect(meterNumberOval, paint);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int baseline = (int) (meterNumberOval.top
				+ (meterNumberOval.bottom - meterNumberOval.top
						- fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
		// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(meterNumber, meterNumberOval.centerX(), baseline, paint);
		// canvas.drawText("000.00",
		// meterNumberOval.left,meterNumberOval.centerY()+meterNumberOval.height()/4,
		// paint);
	}
	
	//计算转换角度
	public void computeAngel(){
		//将bmi转成float
		float bmi = Float.parseFloat(ballNumber);
		
		//计算delta
		float delta = 21 - bmi;
		
//		//转换成角度
		this.Angle2 = delta*15;
//		this.Angle2 = (24+delta)%24*15;
//		System.out.println("..........angel............."+Angle2);
//		float nowAngle = (24+delta)%24;
//		
//		return nowAngle;
	}
	
	// 线程
	@Override
	public void run() {
		// 现将angle置为0
		Angle = 0;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// 进程刷新速度
				Thread.sleep(2);
				// angle2的正负，控制旋转方向
				if (Angle2 < 0) {
					Angle = (float) (Angle - 0.1);
					if (Angle <= Angle2) {
						// 减到指定角度停止进程
						Thread.currentThread().interrupt();
					}
				} else {
					Angle = (float) (Angle + 0.1);
					if (Angle >= Angle2) {
						// 加到指定角度停止进程
						Thread.currentThread().interrupt();
					}
				}
				// System.out.println(".............angle: "+Angle);

				// invalidate();

				// 处理完成后给handler发送消息
				Message msg = new Message();
				msg.what = Activity2ndWeightRecordBmi.getCompeted();
				Activity2ndWeightRecordBmi.getHandler().sendMessage(msg);

			} catch (Exception e) {
				// 可让当前进程停止
				// Thread.currentThread().interrupt();
				// 打印下错误
				System.out.println("exception........." + e.getMessage());
			}

		}
	}
	
	public static int sp2px(Context context, float spValue) { 
	    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
	    return (int) (spValue * fontScale + 0.5f); 
	}

	public static int px2sp(Context context, float pxValue) { 
	    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
	    return (int) (pxValue / fontScale + 0.5f); 
	}
}
