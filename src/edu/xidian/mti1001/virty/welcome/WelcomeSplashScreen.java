package edu.xidian.mti1001.virty.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import edu.xidian.mti1001.virtyandroid.MainTabs;
import edu.xidian.mti1001.virtyandroid.SysApplication;

public class WelcomeSplashScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_splash_screen);
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
            	// 开启
            	startActivity(new Intent(WelcomeSplashScreen.this, MainTabs.class)); 
            }  
        }, 2000); //2000 for release  
		
		SysApplication.getInstance().addActivity(this); 
	}
}
