package edu.xidian.mti1001.virtyandroid;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//自定义的PagerAdapter，为PagerViewer数据适配
public class ArrayPagerViewerAdapter extends PagerAdapter {
	//保存传入的List
	ArrayList<String> list = new ArrayList<String>();

	public ArrayPagerViewerAdapter(ArrayList<String> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return 0;
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		// return false;
		return arg0 == arg1;
	}

//	@Override
//	public Object instantiateItem(View arg0, int arg1) {
//		ViewPager pViewPager = ((ViewPager) arg0);
//		pViewPager.addView(list.get(arg1));
//		return list.get(arg1);
//	}

//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		ViewPager pViewPager = ((ViewPager) container);
//		pViewPager.removeView(list.get(position));
//	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}
}
