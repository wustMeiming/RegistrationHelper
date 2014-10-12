package com.meiming.registrationhelper.utils;

import com.meiming.registrationhelper.dao.RgInfo;

import android.text.format.Time;
import android.util.Log;

public class TimeHelper {
	private static final String TAG = "RegistrationHelper::TimeHelper";
	
	public static final int INVALIDATE_TIME = -1;	// �Ƿ�ʱ��
	public static final int    MORNING_TIME = 1;	// �����ʱ��
	public static final int  AFTERNOON_TIME = 2;	// �����ʱ��

	/**
	 * ��õ�ǰ��ʱ��״̬
	 * @return ��ǰʱ��״̬ 
	 * INVALIDATE_TIME:�Ƿ�ʱ��,
	 * MORNING_TIME:����ʱ�䣬
	 * AFTERNOON_TIME:����ʱ��
	 */
	public static int getCurrentState() {
		int iState = -1;
		Time t=new Time();
		t.setToNow();
		Log.i(TAG, t.toString());
		
		if(isMorning(t)){
			iState = MORNING_TIME;
		}else if(isAfternoon(t)){
			iState = AFTERNOON_TIME;
		}
		return iState;
	}
	
	/**
	 * �ж��Ƿ������ϴ�ʱ��
	 * @param t Ҫ�жϵ�ʱ��
	 * @return �Ƿ������ϴ�ʱ��
	 */
	private static boolean isMorning(Time t){
		Time start = new Time();
		start.set(t);
		start.hour = 8;
		start.minute = 30;
		start.second = 0;
		
		Time end = new Time();
		end.set(t);
		end.hour = 9;
		end.minute = 0;
		end.second = 0;
		
		if(t.after(start) && t.before(end)){
			return true;
		}
		return false;
	}
	
	/**
	 * �ж��Ƿ��������ʱ��
	 * @param t Ҫ�жϵ�ʱ��
	 * @return �Ƿ��������ʱ��
	 */
	private static boolean isAfternoon(Time t){
		Time start = new Time();
		start.set(t);
		start.hour = 14;
		start.minute = 0;
		start.second = 0;
		
		Time end = new Time();
		end.set(t);
		end.hour = 14;
		end.minute = 30;
		end.second = 0;
		
		if(t.after(start) && t.before(end)){
			return true;
		}
		return false;
	}
	
	/**
	 * ��õ�ǰʱ��ID
	 * @return
	 */
	public static String GetTimeId(){
		Time t=new Time();
		t.setToNow();
		return ""+t.year+"-"+t.month+"-"+t.monthDay+"-"+getCurrentState();
	}
	
	/**
	 * ��ô���Ϣ
	 * @return
	 */
	public static RgInfo getRegistrationInfo(){
		RgInfo rgInfo = new RgInfo();
		Time t = new Time();
		t.setToNow();
		Log.i(TAG, "current time:"+timeToString(t));
		rgInfo.setDate(timeToString(t));
		rgInfo.setPeriod(getCurrentState());
		Log.i(TAG, "rgInfo:"+rgInfo.toString());
		return rgInfo;
	}

	private static String timeToString(Time t) {
		return ""+t.year+"-"+(t.month+1<10?"0"+t.month+1:t.month+1) + "-" + (t.monthDay < 10 ? "0"+t.monthDay:t.monthDay) + 
				" " + (t.hour<10 ? "0"+t.hour:t.hour)+":" + (t.minute<10?"0"+t.minute:t.minute) + ":" + (t.second<10?"0"+t.second:t.second);
	}
}
