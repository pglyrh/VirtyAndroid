package edu.xidian.mti1001.virty.activity2nd;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import edu.xidian.mti1001.virty.activity1st.Activity1stUserList;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDefaultUnit;
import edu.xidian.mti1001.virty.welcome.R;
import edu.xidian.mti1001.virtyandroid.ScreenShot;
import edu.xidian.mti1001.virtyandroid.SysApplication;
import edu.xidian.mti1001.virtyandroid.UserDatabaseHelper;

public class Activity2ndWeightRecordBmiHistory extends Activity implements OnClickListener {
	//本次称重
	public static TextView textView2ndBmiHistoryNowWeight;
	private TextView textView2ndBmiHistoryUnit;
	//按钮，删除和导出
	private Button button2ndBmiHistoryClear, button2ndBmiHistoryExport;
	public static ListView listView2ndBmiHistory;

	//设置的体重/称重
	public static float nowWeight;
	
	//默认单位
	private boolean unit;
	private String unitOfWeight;

	// 数据库行指针
	public static BmiHistoryCustomListViewAdapter adapter;
	
	// 数据库对象
	private UserDatabaseHelper db;
	private SQLiteDatabase dbReadDatabase;
	private SQLiteDatabase dbWriteDatabase;
	// 数据库指针
	private String name;
	
	//时间戳，分享图片时候使用
	private String timeStamp;
	
	//Resource
	Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2nd_weight_record_bmi_history);
		
		//获得控件
		textView2ndBmiHistoryNowWeight = (TextView) findViewById(R.id.textView2ndBmiHistoryNowWeight);
		textView2ndBmiHistoryUnit = (TextView) findViewById(R.id.textView2ndBmiHistoryUnit);
		button2ndBmiHistoryExport = (Button) findViewById(R.id.button2ndBmiHistoryExport);
		button2ndBmiHistoryClear = (Button) findViewById(R.id.button2ndBmiHistoryClear);
		listView2ndBmiHistory = (ListView) findViewById(R.id.listView2ndBmiHistory);
		
		//监听器
		button2ndBmiHistoryClear.setOnClickListener(this);
		button2ndBmiHistoryExport.setOnClickListener(this);
		adapter = new BmiHistoryCustomListViewAdapter(this);
//		listView2ndBmiHistory.setAdapter(adapter);
		db = new UserDatabaseHelper(this, "VirtyDB", null, 1);
		SysApplication.getInstance().addActivity(this); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		resources = getResources();
		
//		listView2ndBmiHistory.setAdapter(new CustomListViewAdapter(this));
		adapter.initRecord();
//		Toast.makeText(this, "onresume",Toast.LENGTH_SHORT).show();
		listView2ndBmiHistory.setAdapter(adapter);
		refresh();
		
	}
	
	//刷新页面
	public void refresh(){
		name = Activity1stUserList.CHOOSE_USER_NAME;
		unit = Activity4thSettingDefaultUnit.unit;
		unitOfWeight = unit?"kg":"lb";
//		if (unit) {
//			unitOfWeight = "kg";
//		}else {
//			unitOfWeight = "lb";
//		}
//		Toast.makeText(this, "onresume nowWeight"+nowWeight,Toast.LENGTH_SHORT).show();
		textView2ndBmiHistoryNowWeight.setText(nowWeight+unitOfWeight);
		textView2ndBmiHistoryUnit.setText(resources.getString(R.string.Activity2BmiHistoryWeight)+
				"("+unitOfWeight+")");
	}
	
	//删除记录
	public void clearRecord(){
		//从数据库中删除数据
		dbWriteDatabase = db.getWritableDatabase();
		textView2ndBmiHistoryNowWeight.setText("0.0"+unitOfWeight);
		dbWriteDatabase.delete("userHealthRecord", "name=?", new String[] {name + "" });
		dbWriteDatabase.close();
		//刷新adapter
		adapter.initRecord();
		listView2ndBmiHistory.setAdapter(adapter);
		
		//更新bmi
		//更改球
		Activity2ndWeightRecordBmi.bmiView.setBallNumber("21.0");
		// 设置球形
		Activity2ndWeightRecordBmi.bmiView.setBallImage(BitmapFactory.decodeResource(getResources(), R.drawable.weight_record_bmi_green));
		// 转盘
		new Thread(Activity2ndWeightRecordBmi.bmiView).start();

//		//更改球
//		Activity2ndWeightRecordBmi.textView2ndBmiBall.setText("21.0");
//		// 设置球形
//		Activity2ndWeightRecordBmi.imageView2ndBmiBall
//		.setImageResource(R.drawable.weight_record_bmi_green);
		Activity2ndWeightRecordBmi.textView2ndBmiUserInfo.setText(resources.getString(R.string.Activity2BmiInfo));
//		Activity2ndWeightRecordBmi.buttonWeightSet.setText("0.0");
		Activity2ndWeightRecordBmi.bmiView.setMeterNumber("0.0");
	}
	
	// 获得当前日期、时间
	public void getCurrentTime() {
		SimpleDateFormat formatterTimeStamp = new SimpleDateFormat(
				"yyyyMMddHHmm", Locale.CHINESE);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		timeStamp = formatterTimeStamp.format(curDate);
		// Toast.makeText(this, "date: "+dateString+" time: "+timeString,
		// Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button2ndBmiHistoryClear:
			// 弹出提示框，提示用户是否确定要删除
			new AlertDialog.Builder(getParent())
					.setTitle(resources.getString(R.string.DialogPrompt))
					.setMessage(resources.getString(R.string.DialogConfirmDeleteRecord))
					.setPositiveButton(resources.getString(R.string.DialogSure), 
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							//删除该用户所有记录
							clearRecord();
							//清楚trend数据
							Activity2ndWeightRecordBmiTrend.getRecord();
						}
					}).setNegativeButton(resources.getString(R.string.DialogCancel), null).show();

			break;
		case R.id.button2ndBmiHistoryExport:		
			// 弹出导出对话框
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(
					R.layout.activity_2nd_weight_record_export_dialog,
					(ViewGroup) findViewById(R.id.dialog_fat_manual));
			// 弹出对话框
			final AlertDialog alertDialogInput = new AlertDialog.Builder(
								getParent()).setView(layout).show();
			// 获得dialog中的控件
			ImageView imageViewPhoto = (ImageView) layout
					.findViewById(R.id.imageViewExportDialogPhoto);
			ImageView imageViewPdf = (ImageView) layout
					.findViewById(R.id.imageViewExportDialogPdf);
			ImageView imageViewPrint = (ImageView) layout
					.findViewById(R.id.imageViewExportDialogPrint);
			// 相册按钮，保存信息
			imageViewPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {					
					// 保存图片
					// 取得外部存储所在目录
					File dir;
					getCurrentTime();
					String fileName = "BmiHistoryExport" + timeStamp + ".png";
					// 屏幕截图
					ScreenShot.shoot(Activity2ndWeightRecordBmiHistory.this, fileName);
					
					// 取得外部存储所在目录
					dir = Environment.getExternalStorageDirectory();
					String path = dir.getAbsolutePath() + "/virty/"+fileName;
					File f = new File(path);
					// 确定文件是否存在
					if (f != null && f.exists() && f.isFile()) {
						// 存在
						/*
						LayoutInflater inflater2 = getLayoutInflater();
						View layout2 = inflater2.inflate(
								R.layout.activity_2nd_weight_record_export_dialog,
								(ViewGroup) findViewById(R.id.dialog_fat_manual));
								*/
						AlertDialog alertDialog = new AlertDialog.Builder(
								getParent()).setMessage(resources.getString(R.string.DialogDoneToSaveAsPhoto))
								.setPositiveButton(resources.getString(R.string.DialogSure), null).show();
					} else {
						// 不存在
					}
					
					//关闭对话框
					alertDialogInput.dismiss();
				}
			});
			
			// pdf按钮，保存信息
			imageViewPdf.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getCurrentTime();
					//名称
					String fileName = "BmiHistoryExport" + timeStamp + ".pdf";
					// 屏幕截图
					Bitmap b = ScreenShot.takeScreenShot(Activity2ndWeightRecordBmiHistory.this);
					createPDF(b,fileName);
					AlertDialog alertDialog = new AlertDialog.Builder(
							getParent()).setMessage(resources.getString(R.string.DialogDoneToSaveAsPdf))
							.setPositiveButton(resources.getString(R.string.DialogSure), null).show();
					//关闭对话框
					alertDialogInput.dismiss();					
				}
			});
			// 打印按钮，保存信息
			imageViewPrint.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//版本号为version.release, sdk_int也可判断版本问题
					int sdk = android.os.Build.VERSION.SDK_INT;
					// 云打印在4.4以后添加
					if (sdk >= 19) {
						// 打印
						// 保存图片
						getCurrentTime();
						String fileName = "BmiHistoryExport" + timeStamp + ".png";
						// 屏幕截图
						ScreenShot.shoot(Activity2ndWeightRecordBmiHistory.this,
								fileName);
						Toast.makeText(getParent(), 
								resources.getString(R.string.ToastExportPrint), Toast.LENGTH_LONG).show();
						startActivity(new Intent(Settings.ACTION_SETTINGS));						
					}else {
						Toast.makeText(getParent(), 
								resources.getString(R.string.ToastExportPrintNotSupport), Toast.LENGTH_SHORT).show();
					}
					// 关闭对话框
					alertDialogInput.dismiss();
				}
			});

			break;

		default:
			break;
		}
	}
	public void createPDF(Bitmap bitmap, String fileName) {
		Document doc = new Document();
//		System.out.println("Document.........");
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/virty/export";

			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
//				System.out.println("makedir.........");
			}

			File file = new File(dir, fileName);
			FileOutputStream fOut = new FileOutputStream(file);

			PdfWriter.getInstance(doc, fOut);

//			// open the document
			doc.open();
//
//			Paragraph p1 = new Paragraph(
//					"Hi! I am generating my first PDF using DroidText----joe");
//			Font paraFont = new Font(Font.COURIER);
//			p1.setAlignment(Paragraph.ALIGN_CENTER);
//			p1.setFont(paraFont);
//
//			// add paragraph to document
//			doc.add(p1);
//
//			Paragraph p2 = new Paragraph(
//					"This is an example of a simple paragraph");
//			Font paraFont2 = new Font(Font.COURIER, 14.0f, Color.GREEN);
//			p2.setAlignment(Paragraph.ALIGN_CENTER);
//			p2.setFont(paraFont2);
//
//			doc.add(p2);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
//					.getResources(), imageId);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			Image myImg = Image.getInstance(stream.toByteArray());
			myImg.setAlignment(Image.MIDDLE);

			// add image to document
			doc.add(myImg);

			// set footer
//			Phrase footerText = new Phrase("This is an example of a footer");
//			HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
//			doc.setFooter(pdfFooter);

		} catch (DocumentException de) {

		} catch (IOException e) {

		} finally {
			doc.close();
		}

	}
}
