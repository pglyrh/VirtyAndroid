package edu.xidian.mti1001.virty.activity4th;

import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.xidian.mti1001.virty.welcome.R;

//扩展BaseAdapter，用于自定义listView中显示内容

public class HelpListViewAdapter extends BaseAdapter {

	//调用context
	private Context context;
	//列表，包含每一个帮助项（名字、描述、图片）
	private HelpListCellData[] data;
	
	//传入调用的activity，构造函数
	public HelpListViewAdapter(Context context) {
		this.context = context;
		initData();
	}
	
	//获得调用context
	public Context getContext() {
		return context;
	}
	
	
	
	private void initData(){
		//判断语言环境
		String l = Locale.getDefault().getLanguage().toLowerCase();
		if (l.contains("zh")) {
			// 简体中文
			//列表，包含每一个帮助项（名字、描述、图片）
			data = new HelpListCellData[]{
				new HelpListCellData("1、用户管理", "添加多个用户同时进行管理，了解自己的同时也了解家人和朋友的健康状况", R.drawable.help1),
				new HelpListCellData("2、进入健康工具", "快速查看用户在两种健康秤模式（BMI健康体重秤和FAT脂肪秤）下的健康报告", R.drawable.help2),
				new HelpListCellData("3、健康报告", "在两种健康秤模式下，显示身体各方面参数的健康报告。不同的颜色代表不同的健康程度，根据健康报告，随时调整自己的健康计划。", R.drawable.help3),
				new HelpListCellData("4、趋势图", "切换时间单位，可查看自己不同周期的健康变化趋势，长期关注身体健康。", R.drawable.help4),
				new HelpListCellData("5、 数据输入模式", "进入手动输入模式：手动输入体重值和人体阻抗。开启蓝牙配对连接：手机与体重秤连接，轻松将体重值传至手机。", R.drawable.help5),
				new HelpListCellData("6、健康秤模式切换", "有BMI健康体重秤和FAT脂肪秤两种模式可供选择，全面记录健康状况。", R.drawable.help6),
				new HelpListCellData("7、计步器", "设定目标步数，每天按进度完成自己的健身计划。", R.drawable.help7),
				new HelpListCellData("8、健康时讯", "根据用户的健康状况，提供饮食、运动、生活常识等方面的推荐。", R.drawable.help8),
				new HelpListCellData("9、分享", "随时随地将自己的健康状况分享给好友，大家一起关注身体健康吧！", R.drawable.help9)
			};
		}
		else if (l.contains("en")) {
			//英文
			//列表，包含每一个帮助项（名字、描述、图片）
			data = new HelpListCellData[]{
				new HelpListCellData("1. User Management", 
						"Adding multiple users to manage at the same time, you can understand not only yourself" +
						"but also your family and friends as well.", R.drawable.help1),
				new HelpListCellData("2. Enter Health Tool", 
						"You can quickly check the health reports of users in two health scale models (BMI" +
						" Health Weighing Scale and Fat Scale).", R.drawable.help2),
				new HelpListCellData("3. Health Report", 
						"It shows body parameters in all respects under two health scale modes. Different " +
						"color balls represent different degrees of health. According to the health report, " +
						"you can adjust your health plan at any time.", R.drawable.help3),
				new HelpListCellData("4. Tendency Chart", 
						"Switching the unit of time, you can see your health " +
						"change trend in different period. Make long-term plans.", R.drawable.help4),
				new HelpListCellData("5. Data Entry Mode", 
						"Mannual input mode: Enter your weight and body impedance value manually. Open the " +
						"Bluetooth: Connecting the scale to the mobile phone, transmit the weight value" +
						"to mobile phone easily.", R.drawable.help5),
				new HelpListCellData("6. Health Scale Mode", 
						"There are BMI Health Weighing Scale mode and Fat Scale mode you can choose from" +
						" so that it can record health status comprehensively.", R.drawable.help6),
				new HelpListCellData("7. Pedometer", 
						"Setting goals number, complete your health plan step by step.", R.drawable.help7),
				new HelpListCellData("8. Timely Health Information", 
						"According to the user's health status, some recommendations of diet, exercise " +
						"and life common sense are provided.", R.drawable.help8),
				new HelpListCellData("9. Sharing", 
						"Share health status with your friends anytime and anywhere. Pay attention to " +
						"our health together!", R.drawable.help9)
			};
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//显示每一项
		LinearLayout linearLayout = null;
		if (convertView != null) {
			linearLayout = (LinearLayout) convertView;
		}else {
			linearLayout = (LinearLayout) LayoutInflater.from(
					getContext()).inflate(R.layout.activity_4th_setting_help_listcell_layout, null);
		}
		
		//保存选择项
		HelpListCellData data = getItem(position);
		
		//为不居中的每一个控件赋值
		ImageView icon = (ImageView) linearLayout.findViewById(R.id.imageViewHelpImage);
		TextView name = (TextView) linearLayout.findViewById(R.id.textViewHelpName);
		TextView desc = (TextView) linearLayout.findViewById(R.id.textViewHelpDesc);
		icon.setImageResource(data.iconId);
		name.setText(data.name);
		desc.setText(data.desc);
		
		return linearLayout;
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return 0;
		return position;
	}
	
	@Override
	//public Object getItem(int position)
	public HelpListCellData getItem(int position) {
		// TODO Auto-generated method stub
		//return null;
		return data[position];
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return 0;
		return data.length;
	}

}
