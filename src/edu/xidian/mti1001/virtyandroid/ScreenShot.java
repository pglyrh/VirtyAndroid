package edu.xidian.mti1001.virtyandroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;

public class ScreenShot {
	// 取得外部存储所在目录
	public static File dir;
	
	// 获取指定Activity的截屏，保存到png文件  
   public static Bitmap takeScreenShot(Activity activity){  
        //View是你需要截图的View  
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap b1 = view.getDrawingCache(); 
//    	View view = activity.getWindow().getDecorView(); 
//        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.buildDrawingCache();
//        Bitmap b1 = view.getDrawingCache();
          
//        //获取状态栏高度  
        Rect frame = new Rect();    
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
        int statusBarHeight = frame.top;    
//        System.out.println("............"+statusBarHeight);  
//          
//        //获取屏幕长和高  
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();    
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
//        System.out.println("............"+width);
//        System.out.println("............"+height);
//        System.out.println("b1............"+b1.getWidth());
//        System.out.println("b1............"+b1.getHeight());
        //去掉标题栏  
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight());  
        view.destroyDrawingCache();  
        return b; 
    	
    }  
    
//    public static  boolean takeScreenShot(View view ){
//        boolean isSucc=false;
//        /**
//         *  我们要获取它的cache先要通过setDrawingCacheEnable方法把cache开启，
//         *  然后再调用getDrawingCache方法就可以获得view的cache图片了。
//         *  buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，
//         *  若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
//         *  若果要更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，
//         *  才能建立新的。
//         */
//        view.setDrawingCacheEnabled(true);//开启获取缓存
//        view.buildDrawingCache();
//        Bitmap bitmap=view.getDrawingCache();//得到View的cache
//        Canvas canvas=new Canvas(bitmap);
//        int w=bitmap.getWidth();
//        int h=bitmap.getHeight();
//        
//        Paint paint=new Paint();
//        paint.setColor(Color.YELLOW);
//        SimpleDateFormat simple=new SimpleDateFormat("yyyyMMddhhmmss");
//        String time=simple.format(new Date());
//     // 创建文件夹
////     		String dirPath = dir.getAbsolutePath() + "/virty";
////     		File path1 = new File(dirPath);
////     		if (!path1.exists()) {
////     			path1.mkdirs();
////     		}
//
//     		// 保存文件
//     		String filepath = dir.getAbsolutePath() + "/virty/";
////     		System.out.println("filepath............"+filepath);
//        //canvas.drawText(time, w-w/2, h-h/10, paint);
//        canvas.save();
//        canvas.restore();
//        FileOutputStream fos=null;
//        try{
//                File sddir=new File(filepath);
//                if(!sddir.exists()){
//                        sddir.mkdir();
//                }
//                File file=new File(filepath+time + ".jpg");
//                fos=new FileOutputStream(file);
//                if(fos!=null){
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
//                        fos.close();
//                        isSucc=true;
//                }
//        }catch(Exception e){
//                
//                e.printStackTrace();
//        }
//        return isSucc;
//}
    
    //保存到sdcard  
    private static void savePic(Bitmap b,String fileName){
    	// 取得外部存储所在目录
		dir = Environment.getExternalStorageDirectory();

		// 创建文件夹
		String dirPath = dir.getAbsolutePath() + "/virty";
		File path1 = new File(dirPath);
		if (!path1.exists()) {
			path1.mkdirs();
			System.out.println("...........mkdirs");
		}

		// 保存文件
		String filepath = dir.getAbsolutePath() + "/virty/"+fileName;
//		System.out.println("filepath............"+filepath);
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(filepath);  
            if (null != fos)  
            {  
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);  
                fos.flush();  
                fos.close();  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    //程序入口  
    public static void shoot(Activity a, String fileName){  
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), fileName);  
    }  
}
