package qna.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class qnaPreferences extends Activity {
	private int interval;
	private String alarmToneURI;
	qnaAlarmSetter alarmSetter;
	Dialog intervalChangeDialog;
	ViewFlipper flipper;
	EditText intervalEdit;
	TextView intervalSummary;
	TextView rtSummary;
	
	private OnClickListener changeIntervalButton = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			interval = Integer.parseInt(intervalEdit.getText().toString());
			alarmSetter.setInterval(interval);
			flipper.showPrevious();
			Toast.makeText(v.getContext(), "Interval changed to " + interval + " minutes", Toast.LENGTH_LONG);
			if(interval > 1) {
				intervalSummary.setText("The current interval is " + interval + " minutes");
			}
			else {
				intervalSummary.setText("The current interval is " + interval + " minute");
			}
		}
	};
	
	private OnClickListener changeRTButton = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent rtSelect = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
			rtSelect.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
			rtSelect.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
			rtSelect.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(alarmSetter.getAlarmTone()));
			startActivityForResult(rtSelect, 10);
		}
	};
	
	/*
	 *  For handling return code of the ringtone tone selector
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			Ringtone alarmTone = RingtoneManager.getRingtone(this, Uri.parse(alarmToneURI));
			Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			rtSummary = (TextView)findViewById(R.id.qna_pref_rt_text);
			if(uri == null){
				alarmSetter.setAlarmTone("");
				rtSummary.setText("No alarm tone set");
			}
			else {
				alarmSetter.setAlarmTone(uri.toString());
				rtSummary.setText("Current alarm tone is " + alarmTone.getTitle(this));
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qna_preferences);
		
		alarmSetter = new qnaAlarmSetter(this);
		interval = alarmSetter.getInterval();
		alarmToneURI = alarmSetter.getAlarmTone();
		Ringtone alarmTone = RingtoneManager.getRingtone(this, Uri.parse(alarmToneURI));
		intervalSummary = (TextView)findViewById(R.id.qna_pref_interval_text);
		rtSummary = (TextView)findViewById(R.id.qna_pref_rt_text);
		flipper = (ViewFlipper)findViewById(R.id.p_flipper_interval);
		intervalEdit = (EditText)findViewById(R.id.p_interval_edit);
		
		if(interval > 1) {
			intervalSummary.setText("The current interval is " + interval + " minutes");
		}
		else {
			intervalSummary.setText("The current interval is " + interval + " minute");
		}
		intervalEdit.setText(Integer.toString(interval));
		
		if(alarmTone == null) {
			rtSummary.setText("No alarm tone set");
		}
		else {
			rtSummary.setText("Current alarm tone is " + alarmTone.getTitle(this));
		}
		
		Button changeInterval = (Button)findViewById(R.id.p_interval_button_submit);
		Button flipOn = (Button)findViewById(R.id.p_flip_on);
		Button flipOff = (Button)findViewById(R.id.p_flip_off);
		Button changeRT = (Button)findViewById(R.id.qna_pref_change_rt_button);
		
		flipOn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				flipper.showNext();
			}
		});
		
		flipOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				flipper.showPrevious();
				
			}
		});
		changeInterval.setOnClickListener(changeIntervalButton);
		changeRT.setOnClickListener(changeRTButton);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
