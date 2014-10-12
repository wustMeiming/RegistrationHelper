package com.meiming.registrationhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StorageHelper {
	private static final String REGISTRATION_DB = "Registration";
	private static final String REGISTRATION_LOCK_ID = "RegistrationLockId";
	private static final String SHOULD_TIMES = "应到天数";
	
	private SharedPreferences sharedPreferences;
	
	public StorageHelper(Context context){
		sharedPreferences = context.getSharedPreferences(REGISTRATION_DB, Context.MODE_PRIVATE); //私有数据
	}
	/**
	 * 判断是否已经打卡
	 * @return
	 */
	public boolean hasRegistration(){
		String registrationLockId = sharedPreferences.getString(REGISTRATION_LOCK_ID, "");
		String nowRegistrationLockId = TimeHelper.GetTimeId();
		
		if(registrationLockId.equals(nowRegistrationLockId)){
			return true;
		}

		return false;
	}
	
	/**
	 * 锁定打卡，保证在打卡时段只能打卡一次
	 */
	public void lockRegistration(){
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString(REGISTRATION_LOCK_ID, TimeHelper.GetTimeId());
		editor.commit();//提交修改
	}
	
	/**
	 * 设置应到次数
	 * @param times
	 */
	public void setShouldTimes(float times){
		Editor editor = sharedPreferences.edit();
		editor.putFloat(SHOULD_TIMES, times);
		editor.commit();
	}
	
	/**
	 * 获得应到次数
	 * @return
	 */
	public float getShouldTimes(){
		float dTimes = sharedPreferences.getFloat(SHOULD_TIMES, 0);
		return dTimes;
	}
}
