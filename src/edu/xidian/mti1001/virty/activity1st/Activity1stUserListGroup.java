package edu.xidian.mti1001.virty.activity1st;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import edu.xidian.mti1001.virtyandroid.MainTabs;

public class Activity1stUserListGroup extends ActivityGroup {
	/** 
     * 一个静态的ActivityGroup变量，用于管理本Group中的Activity 
     */  
    public static ActivityGroup group;  
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
          
        group = this;  
        
        
      //要跳转的界面  
//      Intent intent = new Intent(this, Activity1stUserList.class).  
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//      Intent intent = new Intent(this, Activity1stUserList.class);
//      //把一个Activity转换成一个View  
//      Window w = group.getLocalActivityManager().startActivity("Activity1stUserList",intent);  
//      View view = w.getDecorView();  
//      //把View添加大ActivityGroup中  
//      group.setContentView(view);  
    }  
  
    @Override  
    public void onBackPressed() {  
        // TODO Auto-generated method stub  
//      super.onBackPressed();  
        //把后退事件交给子Activity处理  
//        group.getLocalActivityManager()  
//            .getCurrentActivity().onBackPressed(); 
    	startActivity(new Intent(group.getLocalActivityManager().getCurrentActivity(),MainTabs.class));
    }  
  
    @Override  
    protected void onResume() {  
        // TODO Auto-generated method stub  
        super.onResume();  
        //把界面切换放到onResume方法中是因为，从其他选项卡切换回来时，  
        //调用搞得是onResume方法  
//          
        //要跳转的界面  
        Intent intent = new Intent(this, Activity1stUserList.class).  
                  addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        //把一个Activity转换成一个View  
        Window w = group.getLocalActivityManager().startActivity("Activity1stUserList",intent);  
        View view = w.getDecorView();  
        //把View添加大ActivityGroup中  
        group.setContentView(view);  
    }  
}
