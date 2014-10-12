package com.meiming.registrationhelper;

import com.meiming.registrationhelper.utils.StorageHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetttingActivity extends Activity implements OnClickListener{
	
	private Button btnSave;
	private Button btnCancel;
	private EditText etShouldTimes;
	
	private StorageHelper storageHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		storageHelper = new StorageHelper(getApplicationContext());
		
		btnSave = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		etShouldTimes = (EditText) findViewById(R.id.etShouldTimes);
		
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		etShouldTimes.setText("" + storageHelper.getShouldTimes());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnOk:
			float dShouldTimes = Float.parseFloat(etShouldTimes.getText().toString());
			storageHelper.setShouldTimes(dShouldTimes);
			finish();
			break;
		case R.id.btnCancel:
			finish();
			break;
		}
	}
}
