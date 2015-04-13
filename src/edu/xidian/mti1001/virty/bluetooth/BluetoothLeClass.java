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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import edu.xidian.mti1001.virty.activity2nd.Activity2ndWeightRecordFat;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi") 
public class BluetoothLeClass{
	
	public File file;
    private final static String TAG = BluetoothLeClass.class.getSimpleName();
    
    private final static double WEIGHT_DIV = 79.98f;

    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    //test
//    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002901-0000-1000-8000-00805f9b34fb";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    // 阻抗和体重
    public static double bleWeight, bleResistor; 
    
	public interface OnConnectListener {
		public void onConnect(BluetoothGatt gatt);
	}
	public interface OnDisconnectListener {
		public void onDisconnect(BluetoothGatt gatt);
	}
	public interface OnServiceDiscoverListener {
		public void onServiceDiscover(BluetoothGatt gatt);
	}
	public interface OnDataAvailableListener {
		 public void onCharacteristicRead(BluetoothGatt gatt,
		            BluetoothGattCharacteristic characteristic,
		            int status);
		 public void onCharacteristicWrite(BluetoothGatt gatt,
	                BluetoothGattCharacteristic characteristic);
	}
    
	private OnConnectListener mOnConnectListener;
	private OnDisconnectListener mOnDisconnectListener;
	private OnServiceDiscoverListener mOnServiceDiscoverListener;
	private OnDataAvailableListener mOnDataAvailableListener;
	private Context mContext;
	public void setOnConnectListener(OnConnectListener l){
		mOnConnectListener = l;
	}
	public void setOnDisconnectListener(OnDisconnectListener l){
		mOnDisconnectListener = l;
	}
	public void setOnServiceDiscoverListener(OnServiceDiscoverListener l){
		mOnServiceDiscoverListener = l;
	}
	public void setOnDataAvailableListener(OnDataAvailableListener l){
		mOnDataAvailableListener = l;
	}
	
	public StringBuilder stringBuilder = new StringBuilder();
	
	public BluetoothLeClass(Context c){
		mContext = c;
//		file = new File(mContext.getFilesDir(), "dataSave");
		file = new File(Environment.getExternalStorageDirectory()+ "/BLE.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
				Toast.makeText(c, "file not exist", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast.makeText(c, "file has existed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public BluetoothLeClass(BluetoothDeviceScan bluetoothDeviceScan){
		mContext = bluetoothDeviceScan.getContext();
//		file = new File(mContext.getFilesDir(), "dataSave");
		file = new File(Environment.getExternalStorageDirectory()+ "/BLE.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
				Toast.makeText(bluetoothDeviceScan.getContext(), "file not exist", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast.makeText(bluetoothDeviceScan.getContext(), "file has existed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
            	if(mOnConnectListener!=null)
            		mOnConnectListener.onConnect(gatt);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if(mOnDisconnectListener!=null)
                	mOnDisconnectListener.onDisconnect(gatt);
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS && mOnServiceDiscoverListener!=null) {
                	mOnServiceDiscoverListener.onServiceDiscover(gatt);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        	if (mOnDataAvailableListener!=null)
        		mOnDataAvailableListener.onCharacteristicRead(gatt, characteristic, status);
        }

        
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
        	byte[] value = characteristic.getValue();
        	String[] hexValue = new String[value.length];
        	for (int i = 0; i < value.length; i++) {
        		String part = BluetoothUtils.getHexString(value[i]);
        		hexValue[value.length - i - 1] = part;
        	}
        	int[] result = BluetoothUtils.calculateResult(hexValue);

        	//result[0]--阻抗  result[1]--体重
        	double weight = Double.valueOf(String.format("%.2f", result[1]/WEIGHT_DIV));
        	System.out.println("阻抗 = " + result[0] + "  体重 = " + weight);
        	//将以上这两个值传入
        	if (Activity2ndWeightRecordFat.front) {
        		System.out.println("fat true..............");
        		// 发送Msg
        		Message msg = new Message();
        		msg.what = 0;
        		Activity2ndWeightRecordFat.getBleHandler().sendMessage(msg);
        		bleWeight = weight;
        		bleResistor = result[0];				
			}else {
				System.out.println("fat false..............");
			}
			
        	if (mOnDataAvailableListener!=null) {
        		mOnDataAvailableListener.onCharacteristicWrite(gatt, characteristic);
        	}
        }
    };

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "---------------BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "---------------Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "---------------Device not found.  Unable to connect.");
            System.out.println("---------------Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        System.out.println("Device UUID -----------------> " + device.getUuids());
        Log.d(TAG, "---------------Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        System.out.println("disconnect..........");
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
			        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
		mBluetoothGatt.writeDescriptor(descriptor);
		
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
    	 mBluetoothGatt.writeCharacteristic(characteristic);
    }
    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}
