/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.xidian.mti1001.virty.bluetooth;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity4th.Activity4thSettingDeviceConnect;
import edu.xidian.mti1001.virty.bluetooth.BluetoothLeClass.OnDataAvailableListener;
import edu.xidian.mti1001.virty.bluetooth.BluetoothLeClass.OnServiceDiscoverListener;
import edu.xidian.mti1001.virty.welcome.R;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
@SuppressLint("NewApi") //BLE需要至少API 18才可以支持
public class BluetoothDeviceScan{
	private final static String TAG = BluetoothDeviceScan.class.getSimpleName();
	//test
//	private final static String UUID_KEY_WRITE = "0000fff3-0000-1000-8000-00805f9b34fb";
//	private final static String UUID_KEY_NOTIFY = "0000fff4-0000-1000-8000-00805f9b34fb";
	
	private final static String UUID_KEY_WRITE = "0000fac1-0000-1000-8000-00805f9b34fb";
	private final static String UUID_KEY_NOTIFY = "0000fac2-0000-1000-8000-00805f9b34fb";
	private final static String UUID_KEY_INDICATE = "0000fac3-0000-1000-8000-00805f9b34fb";

    private BluetoothLeDeviceListAdapter mLeDeviceListAdapter;
    /**搜索BLE终端*/
    private BluetoothAdapter mBluetoothAdapter;
    /**读写BLE终端*/
    private static BluetoothLeClass mBLE;
    private boolean mScanning;
    private Handler mHandler;
    // 等待搜索Handler
    private Handler searchHandler;
    
    //保存
    private BluetoothLeClass bleDevice;
    private List<BluetoothLeClass> devices = new ArrayList<BluetoothLeClass>();
    
    //Context
    private Context context;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;

    //构造函数
    public BluetoothDeviceScan(Context context){
    	this.context = context;
    	mHandler = new Handler();
    	searchHandler = new Handler();
    	if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
            return;
        }
        //�??启蓝�??
        mBluetoothAdapter.enable();
        
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
        }
        //response to find the device
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //response to receive the data
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        
     // Initializes list view adapter.
        mLeDeviceListAdapter = new BluetoothLeDeviceListAdapter(context);
//        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }
    
    
	// 获得context
	public Context getContext() {
		return this.context;
	}
	
	// 返回蓝牙设备
	public static BluetoothLeClass getBluetoothLeClass(){
		return mBLE;
	}

	// 等待搜索线程
	private class SearchThread implements Runnable{
		int i = 0;
		boolean find = false;
		
		public boolean getFind(){
			return find;
		}
		
		@Override
		public void run() {
			while (i < 20 && !find) {
				if (mLeDeviceListAdapter.getCount() > 0) {
					for (int j = 0; j < mLeDeviceListAdapter.getCount(); j++) {
						// 假设设备为“Smart body scale” 则返回设备
						if (mLeDeviceListAdapter.getDevice(j).getName()
								.toLowerCase().contains("body".toLowerCase())
								|| mLeDeviceListAdapter.getDevice(j).getName()
										.toLowerCase()
										.contains("scale".toLowerCase())) {
							BluetoothDevice device = mLeDeviceListAdapter.getDevice(0);
							if (mScanning) {
								mBluetoothAdapter.stopLeScan(mLeScanCallback);
								mScanning = false;
							}
							//查找成功
							mBLE.connect(device.getAddress());
							find = true;
							Message msg = new Message();
							msg.what = 2;
							Activity4thSettingDeviceConnect.getHandler().sendMessage(msg);
							
							Activity4thSettingDeviceConnect.find = true;
							
							Thread.currentThread().interrupt();
						}
					}
				} else {
					System.out.println("连接中。。。。。。。。。" + i);
					Message msg = new Message();
					msg.what = 1;
					Activity4thSettingDeviceConnect.getHandler().sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					searchHandler.postDelayed(this, 1000);
					i ++;
				}
			}
			if (i>=20) {
				Message msg = new Message();
				msg.what = 3;
				Activity4thSettingDeviceConnect.getHandler().sendMessage(msg);
				Thread.currentThread().interrupt();
			}
			
		}
		
	}
	
	public void connectToScale() {
		BluetoothDevice device = null;
		SearchThread searchThread = new SearchThread();
		searchHandler.post(searchThread);
//		// 等待10s，让蓝牙搜索设备
//		for (int i = 0; i < 20; i++) {
//			if (mLeDeviceListAdapter.getCount() > 0) {
//				for (int j = 0; j < mLeDeviceListAdapter.getCount(); j++) {
//					// 假设设备为“Smart body scale” 则返回设备
//					if (mLeDeviceListAdapter.getDevice(j).getName()
//							.toLowerCase().contains("body".toLowerCase())
//							|| mLeDeviceListAdapter.getDevice(j).getName()
//									.toLowerCase()
//									.contains("scale".toLowerCase())) {
//						device = mLeDeviceListAdapter.getDevice(0);
//						if (mScanning) {
//							mBluetoothAdapter.stopLeScan(mLeScanCallback);
//							mScanning = false;
//						}
//						mBLE.connect(device.getAddress());
//						// 处理完成后给handler发送消息
////						Message msg = new Message();
////						msg.what = 2;
////						Activity4thSettingDeviceConnect.getHandler().sendMessage(msg);
//						return true;
//					}
//				}
//			} else {
//				System.out.println("连接中。。。。。。。。。。" + i);
//				try {
//					// 处理完成后给handler发送消息
////					Message msg = new Message();
////					msg.what = 1;
////					Activity4thSettingDeviceConnect.getHandler().sendMessage(msg);
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
//		return false;
	}

//    @Override
//    protected void onPause() {
//        super.onPause();
//        scanLeDevice(false);
//        mLeDeviceListAdapter.clear();
//        mBLE.disconnect();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mBLE.close();
//    }
    
    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	// 获取蓝牙设备
//        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
//        // 我用的时候更改上述一句话，改为寻找名字含有Fat body sacle的秤
//        if (device == null) return;
//        if (mScanning) {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            mScanning = false;
//        }
//        System.out.println("start connect..............");
//        mBLE.connect(device.getAddress());
//    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
//        invalidateOptionsMenu();
    }

    /**
     * list all the infomation for the device
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener(){

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			displayGattServices(mBLE.getSupportedGattServices());
		}
    	
    };
    
    /**
     * read the data
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener(){

    	/**
    	 * BLE终端数据被读的事�??
    	 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) 
				Log.e(TAG,"onCharRead "+gatt.getDevice().getName()
						+" read "
						+characteristic.getUuid().toString()
						+" -> "
						+BluetoothUtils.bytesToHexString(characteristic.getValue()));
		}
		
	    /**
	     * write data
	     */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.e(TAG,"onCharWrite "+gatt.getDevice().getName()
					+" write "
					+characteristic.getUuid().toString()
					+" -> "
					+new String(characteristic.getValue()));
		}
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        	System.out.println("........... BluetoothAdapter.LeScanCallback");
        	mLeDeviceListAdapter.addDevice(device);
        	System.out.println("device name: "+device.getName());
        	System.out.println("mLeDeviceListAdapter count: "+mLeDeviceListAdapter.getCount());
        	
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLeDeviceListAdapter.addDevice(device);
//                    mLeDeviceListAdapter.notifyDataSetChanged();
//                }
//            });
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        for (BluetoothGattService gattService : gattServices) {
        	//-----Service type-----//
        	int type = gattService.getType();
            Log.e(TAG,"-->service type:"+BluetoothUtils.getServiceType(type));
            Log.e(TAG,"-->includedServices size:"+gattService.getIncludedServices().size());
            Log.e(TAG,"-->service uuid:"+gattService.getUuid());
            
            //-----Characteristics infomation-----//
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
                Log.e(TAG,"---->char uuid:"+gattCharacteristic.getUuid());
                
                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,"---->char permission:"+BluetoothUtils.getCharPermission(permission));
                
                int property = gattCharacteristic.getProperties();
                Log.e(TAG,"---->char property:"+BluetoothUtils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
        		if (data != null && data.length > 0) {
        			Log.e(TAG,"---->char value:"+new String(data));
        		}

        		//UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
        		/*if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA)){        			
        			//测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
        			mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	mBLE.readCharacteristic(gattCharacteristic);
                        }
                    }, 500);
        			
        			//接受Characteristic被写的�??�知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
        			mBLE.setCharacteristicNotification(gattCharacteristic, true);
        			//设置数据内容
        			gattCharacteristic.setValue("send data->");
        			//�??蓝牙模块写入数据
        			mBLE.writeCharacteristic(gattCharacteristic);
        		}*/
//        		System.out.println(gattCharacteristic.getUuid().toString() + " === " + UUID_KEY_WRITE);
        	if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_WRITE)){
        		System.out.println("in the write~~~~~~~~~~~~~~");
    			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@write@@@@@@@@@@@@@@@@@@");
//       			mBLE.setCharacteristicNotification(gattCharacteristic, true);
    			gattCharacteristic.setValue("31");
    			mBLE.writeCharacteristic(gattCharacteristic);
    		}
        	
        	
        	if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_INDICATE)){
        		System.out.println("in the indicate~~~~~~~~~~~~~~");
    			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@notify@@@@@@@@@@@@@@@@@@");
       			mBLE.setCharacteristicNotification(gattCharacteristic, true);
    			
    		}
        	
        		
        		//-----Descriptors infomation-----//
				List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
				for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
					Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
					int descPermission = gattDescriptor.getPermissions();
					Log.e(TAG,"-------->desc permission:"+ BluetoothUtils.getDescPermission(descPermission));
					
					byte[] desData = gattDescriptor.getValue();
					if (desData != null && desData.length > 0) {
						Log.e(TAG, "-------->desc value:"+ new String(desData));
					} else {
						Log.e(TAG, "-------->desc value: null");
					}
        		 }
            }
        }//

    }
}