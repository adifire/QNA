package qna.app;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class qnaActivity extends Activity {
	static private int interval;
	private int minutes;
	private boolean alarmOn;
	
	private ViewFlipper qnaFlipper;
	static private boolean flipOn = false;
	SharedPreferences alarmSettings;
	qnaAlarmSetter alarmSetter;
	
	CountDownTimer qna_main_ct;
	TextView minutesText;
	long setTime;
	
	/*All onClick Listeners start here*/
	private OnClickListener onPlusClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.isPressed()) {
				minutes += interval;
				minutesText.setText(formatTime());
			}
		}
	};
	
	private OnClickListener onMinusClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			minutes -= interval;
			if(minutes < 0) {
				minutes = 0;
			}
			minutesText.setText(formatTime());
		}
	};
	
	private OnClickListener onBuzzClick = new OnClickListener() {
		/* Setting the alarm after Buzz button click */
		@Override
		public void onClick(View v) {
			if(alarmSetter.setAlarm(qnaActivity.this, minutes)) {
				qnaFlipper = (ViewFlipper)findViewById(R.id.qna_flipper);
				qnaFlipper.showNext();
				Toast.makeText(v.getContext(), "Alarm Set", Toast.LENGTH_LONG).show();
				setTime = alarmSetter.getSetTime();
				setCountDownTimer();
			}
			
		}
	};
	
	private OnClickListener onCancelClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(alarmSetter.cancelAlarm(qnaActivity.this)) {
				qnaFlipper = (ViewFlipper)findViewById(R.id.qna_flipper);
				
				qnaFlipper.showPrevious();
				
				try {
					qna_main_ct.cancel();
					Toast.makeText(v.getContext(), "Alarm Cancelled", Toast.LENGTH_LONG);
				}
				catch (Exception e) {
					Toast.makeText(v.getContext(), "Some error in the countdown times", Toast.LENGTH_LONG);
				}
			}
			
		}
	};

	/* All methods starts here */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flipOn = false;
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setContentView(R.layout.qna_main);
        
        alarmSetter = new qnaAlarmSetter(this);
        interval = alarmSetter.getInterval();
        minutes = alarmSetter.getMinutes();
        setTime = alarmSetter.getSetTime();
        
        minutesText = (TextView)findViewById(R.id.qna_main_time_text);
        minutesText.setText(formatTime());
       
        Button plusButton = (Button)findViewById(R.id.qna_main_plus_button);
        Button minusButton = (Button)findViewById(R.id.qna_main_minus_button);
        Button buzzButton = (Button)findViewById(R.id.qna_main_buzz_button);
        Button cancelButton = (Button)findViewById(R.id.qna_main_cancel_button);
        
        plusButton.setOnClickListener(onPlusClick);
        minusButton.setOnClickListener(onMinusClick);
        buzzButton.setOnClickListener(onBuzzClick);
        cancelButton.setOnClickListener(onCancelClick);
		
		if(checkAlarmOn()) {
			try {
				qna_main_ct.cancel();
			} catch (Exception e) {
				minutes = alarmSetter.getMinutes();
				setCountDownTimer();
			} 
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkAlarmOn();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		// Stop the main activity
		super.onBackPressed();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.qna_main_menu, menu);
		return true;
	};
	
	private boolean checkAlarmOn() {
		alarmOn = alarmSetter.getAlarmOn();
		qnaFlipper = (ViewFlipper)findViewById(R.id.qna_flipper);
		
		if(alarmOn && !flipOn) {
			qnaFlipper.showNext();
			flipOn = true;
			return true;
		}
		
		else if(!alarmOn && flipOn) {
			//qnaFlipper.showPrevious();
			flipOn = false;
		}
		
		return false;
	}
	
	/* Methods to handle the menu; Leads to either the preferences menu or the about window  */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.option:
			preferencesMenu();
			return true;
		case R.id.about:
			aboutWindow();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	};
	
	public void preferencesMenu() {
		Intent optionsIntent = new Intent(this,qnaPreferences.class);
		startActivity(optionsIntent);
	}
	
	public void aboutWindow() {
		
	}
	
	public void setCountDownTimer() {
		long time = (long)(minutes * 60000) - (System.currentTimeMillis() - setTime);
		
		qna_main_ct = new CountDownTimer(time,1000) {
			long ticks = 60;
			TextView ctText = (TextView)findViewById(R.id.qna_main_countdown_text);
			@Override
			public void onTick(long millisUntilFinished) {
				String time;
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumIntegerDigits(2);
				long seconds = millisUntilFinished / 1000;
				ticks = seconds % 60;
				
				time = nf.format((seconds / 3600) % 3600) + ":" + nf.format((seconds / 60) % 60) +
						":" + nf.format(ticks);
				ctText.setText(time);
			}
			
			@Override
			public void onFinish() {
				qnaFlipper.showPrevious();
			}
		};
		qna_main_ct.start();
	}
	
	private String formatTime() {
		String time;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		time = nf.format(minutes / 60) + ":" + nf.format(minutes % 60);
		return time;
	}
	
	
}