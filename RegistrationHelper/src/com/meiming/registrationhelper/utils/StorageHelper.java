package com.meiming.registrationhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StorageHelper {
	private static final String REGISTRATION_DB = "Registration";
	private static final String REGISTRATION_LOCK_ID = "RegistrationLockId";
	private static final String SHOULD_TIMES = "Ӧ������";
	
	private SharedPreferences sharedPreferences;
	
	public StorageHelper(Context context){
		sharedPreferences = context.getSharedPreferences(REGISTRATION_DB, Context.MODE_PRIVATE); //˽������
	}
	/**
	 * �ж��Ƿ��Ѿ���
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
	 * �����򿨣���֤�ڴ�ʱ��ֻ�ܴ�һ��
	 */
	public void lockRegistration(){
		Editor editor = sharedPreferences.edit();//��ȡ�༭��
		editor.putString(REGISTRATION_LOCK_ID, TimeHelper.GetTimeId());
		editor.commit();//�ύ�޸�
	}
	
	/**
	 * ����Ӧ������
	 * @param times
	 */
	public void setShouldTimes(float times){
		Editor editor = sharedPreferences.edit();
		editor.putFloat(SHOULD_TIMES, times);
		editor.commit();
	}
	
	/**
	 * ���Ӧ������
	 * @return
	 */
	public float getShouldTimes(){
		float dTimes = sharedPreferences.getFloat(SHOULD_TIMES, 0);
		return dTimes;
	}
}
