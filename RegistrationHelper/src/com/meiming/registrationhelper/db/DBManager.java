package com.meiming.registrationhelper.db;

import java.util.ArrayList;
import java.util.List;

import com.meiming.registrationhelper.dao.RgInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private static final String TAG = "RegistrationHelper::DBManager";
	
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {
		helper = new DBHelper(context);
//		//因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
//		//所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
//		db = helper.getWritableDatabase();

	}
	
	/**
	 * add RgInfo
	 * @param rgInfo
	 */
	public void add(RgInfo rgInfo) {
		Log.i(TAG, "rgInfo.Date:"+rgInfo.getDate());
		db = helper.getWritableDatabase();    
        db.beginTransaction();	//开始事务
        try {
        	db.execSQL("INSERT INTO RG_INFO VALUES(null, ?, ?)", new Object[]{rgInfo.getDate(), rgInfo.getPeriod()});
        	db.setTransactionSuccessful();	//设置事务成功完成
        } finally {
        	db.endTransaction();	//结束事务
        }
        db.close();
	}
	
	
	/**
	 * query all RgInfos, return list
	 * @return List<RgInfo>
	 */
	public List<RgInfo> query(int year, int month) {
		db = helper.getReadableDatabase();
		ArrayList<RgInfo> rgInfos = new ArrayList<RgInfo>();
		Cursor c = queryTheCursor(year, month);
        while (c.moveToNext()) {
        	RgInfo rgInfo = new RgInfo();
        	rgInfo.setId(c.getInt(c.getColumnIndex("_id")));
        	String curTime = c.getString(c.getColumnIndex("rgdate"));
        	Log.i(TAG, "query:"+curTime);
        	rgInfo.setDate(getTimeString(curTime));
        	rgInfo.setPeriod(c.getInt(c.getColumnIndex("period")));
        	rgInfos.add(rgInfo);
        	Log.i(TAG, rgInfo.toString());
        }
        c.close(); 
        db.close();
        return rgInfos;

	}

	private String getTimeString(String curTime) {
		String strTime = ""+curTime.substring(0, 4)+"-"+curTime.substring(5, 7)+"-"+curTime.substring(8, 10);
		if(curTime.length() > 11){
			strTime += " "+curTime.substring(11, 13)+":"+curTime.substring(14, 16);
		}else{
			strTime += " 00:00";
		}
		return strTime;
	}
	
	/**
	 * query all RgInfos, return cursor
	 * @return	Cursor
	 */
	public Cursor queryTheCursor(int year, int month) {
		String year_month = ""+year+"-"+ (month<10? "0"+month:month);
		Log.i(TAG, "queryTheCursor:"+ year_month);
        Cursor c = db.rawQuery("SELECT * FROM RG_INFO where strftime('%Y-%m',rgdate) = '"+year_month+"' order by rgdate desc", null);
        return c;
	}
	
	/**
	 * 查询指定年月的打卡次数
	 * @param year
	 * @param month
	 * @return
	 */
	public int queryRegistrationInfo(int year, int month){
		SQLiteDatabase db = helper.getReadableDatabase();
		String year_month = ""+year+"-"+ (month<10? "0"+month:month);
		Log.i(TAG, "queryRegistrationInfo:"+ year_month);
		// 查询记录条数 
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM RG_INFO where strftime('%Y-%m',rgdate) = '"+year_month+"'", null); 
		c.moveToNext(); 
		int count =c.getInt(0);  
		c.close(); 
		db.close(); 
		return count; 
	}
	
	/**
	 * 查询指定时间是否有记录
	 * @param year
	 * @param month
	 * @param day
	 * @param period 时间段 1:上午 2:下午
	 * @return
	 */
	public boolean hasRegistration(int year, int month, int day, int period){
		boolean bHasReg = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		String year_month_day = ""+year+"-"+ (month<10? "0"+month:month)+"-"+(day<10?"0"+day:day);
		Log.i(TAG, "hasRegistration:"+ year_month_day);
		// 查询记录条数 
		Cursor c = db.rawQuery("SELECT * FROM RG_INFO where strftime('%Y-%m-%d',rgdate) = '"+year_month_day+"' and period = " + period, null); 
		bHasReg = c.moveToNext();
		c.close();
		db.close();
		return bHasReg;
	}
	
	public void test(int year,int month){
		db = helper.getReadableDatabase();
		String year_month = ""+year+"-"+ (month<10? "0"+month:month);
		Cursor c = db.rawQuery("select rgdate from RG_INFO where strftime('%Y-%m',rgdate) = '"+year_month+"'", null);
		while(c.moveToNext()){
		String t = c.getString(0);
		Log.i(TAG, "select:"+t);
		}
		db.close();
	}
}
