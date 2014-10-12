package com.meiming.registrationhelper;

import com.meiming.registrationhelper.dao.RgInfo;
import com.meiming.registrationhelper.db.DBManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddRecordActivity extends Activity implements OnClickListener {

	private Button btnAddRecord;
	private Button btnGoBack;
	private DatePicker datePicker;
	private RadioGroup radioGroup;
	
	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_record);
		
		btnAddRecord = (Button) findViewById(R.id.btnAddRecord);
		btnGoBack = (Button) findViewById(R.id.btnGoBack);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupDate);
		
		btnAddRecord.setOnClickListener(this);
		btnGoBack.setOnClickListener(this);
		
		dbManager = new DBManager(getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnAddRecord:
			int year = datePicker.getYear();
			int month = datePicker.getMonth()+1;
			int day = datePicker.getDayOfMonth();
			int iPeroid = (radioGroup.getCheckedRadioButtonId() == R.id.radio0 ? 1 : 2);
			if(dbManager.hasRegistration(year, month, day, iPeroid)){
				Toast.makeText(this, "记录已经存在，请勿重复添加！", Toast.LENGTH_SHORT).show();
				return;
			}
			RgInfo rgInfo = new RgInfo();
			rgInfo.setDate(""+year+"-"+(month < 10 ? "0"+month:month)+"-"+(day < 10 ? "0"+day:day));
			rgInfo.setPeriod(iPeroid);
			dbManager.add(rgInfo);
			Toast.makeText(this, "添加："+rgInfo.getDate()+ " " + (rgInfo.getPeriod() == 1 ?  "上午":"下午") +"的记录成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnGoBack:
			this.finish();
			break;
		}
	}

}
