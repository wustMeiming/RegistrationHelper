package com.meiming.registrationhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.meiming.registrationhelper.dao.RgInfo;
import com.meiming.registrationhelper.db.DBManager;
import com.meiming.registrationhelper.utils.StorageHelper;
import com.meiming.registrationhelper.utils.TimeHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";
	private Button btnRegitration;
	private TextView tvTimesShould;
	private TextView tvTimesReal;
	
	private int count;
	
	private StorageHelper storageHelper;
	
	private ListView listView;
	private SimpleAdapter adapter;
	
	private List<HashMap<String,String>> registrationInfoList;
	
	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		storageHelper = new StorageHelper(getApplicationContext());
		
		dbManager = new DBManager(getApplicationContext());
		
		registrationInfoList = new ArrayList<HashMap<String,String>>(); 
		
		btnRegitration = (Button) findViewById(R.id.btnRegistration);
		btnRegitration.setOnClickListener(this);
		
		tvTimesShould = (TextView) findViewById(R.id.lbTimesShould);
		tvTimesReal = (TextView)findViewById(R.id.lbTimesReal);
		
		listView = (ListView) findViewById(R.id.registrationInfo);
		getData(registrationInfoList);
		adapter = new SimpleAdapter(this, registrationInfoList, R.layout.registration_item,
				new String[]{"time","period"},
				new int[]{R.id.tvTime,R.id.tvPeroid});
		listView.setAdapter(adapter);
	}

	private void getData(List<HashMap<String,String>> registrationList) {
		registrationList.clear();
		Time t = new Time();
		t.setToNow();
		Log.i(TAG, "year="+t.year+",month="+t.month+1);
		List<RgInfo> RgInfoList = dbManager.query(t.year, t.month+1);
		for(int i = 0;  i < RgInfoList.size(); i++){
			HashMap<String, String> obj = new HashMap<String, String>();
			obj.put("time", RgInfoList.get(i).getDate());
			obj.put("period", RgInfoList.get(i).getPeriod() == 1 ? "上午":"下午");
			registrationList.add(obj);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(MainActivity.this, SetttingActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.action_add_record){
			Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Time t = new Time();
		t.setToNow();
		Log.i(TAG, "time:"+t.year+"-"+t.month+1+"-"+t.monthDay+1);
		count = dbManager.queryRegistrationInfo(t.year, t.month+1);
		
		tvTimesReal.setText("实到：" + count/2.0);
		tvTimesShould.setText("应到：" + storageHelper.getShouldTimes());
		
		getData(registrationInfoList);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Time t = new Time();
		t.setToNow();
		//dbManager.test(t.year, t.month+1);
		switch (v.getId()) {
		case R.id.btnRegistration:
			int iState = TimeHelper.getCurrentState();
			Log.i(TAG, "btnRegistration:"+t.toString()+" peroid:"+iState);	
			if(iState == TimeHelper.MORNING_TIME || iState == TimeHelper.AFTERNOON_TIME){
				if(storageHelper.hasRegistration()){
					Toast.makeText(this, "您已打卡，请勿重复打卡！", Toast.LENGTH_SHORT).show();
					return;
				}
				dbManager.add(TimeHelper.getRegistrationInfo());
				storageHelper.lockRegistration();
				getData(registrationInfoList);
				adapter.notifyDataSetChanged();
				String info = TimeHelper.MORNING_TIME == iState ? "早上好！":"下午好！";
				Toast.makeText(this, info+"打卡成功！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "对不起，不在打卡时间！", Toast.LENGTH_SHORT).show();
				return;
			}
			count++;
			tvTimesReal.setText("实到："+count/2.0);
			break;

		default:
			break;
		}
	}
}
