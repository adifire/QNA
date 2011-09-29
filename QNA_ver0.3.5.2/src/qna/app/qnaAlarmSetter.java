package qna.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.widget.Toast;



public class qnaAlarmSetter {
	static final String QNA_PREF = "qna_pref_0.3.5";
	private int interval;
	private int minutes;
	private boolean alarmOn;
	private String alarmTone;
	private long setTime;
	SharedPreferences alarmSettings;
	SharedPreferences.Editor editor;
	
	public qnaAlarmSetter(Context context) {
		// Setting the preferences
		alarmSettings  = context.getSharedPreferences(QNA_PREF, 2);
		editor = alarmSettings.edit();
		alarmOn = alarmSettings.getBoolean("alarmOn", false);
		setInterval(alarmSettings.getInt("interval", 1));
		setMinutes(alarmSettings.getInt("minutes", 0));
		setAlarmTone(alarmSettings.getString("alarmTone",
				 												RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
	}
	
	public boolean setAlarm(Context context, int min) {
		this.minutes = min;
		alarmOn = alarmSettings.getBoolean("alarmOn", false);
		
		// Check if the alarm is already set; If set then set alarm for a positive value and return to calling method
		if(alarmOn) {
			Toast.makeText(context, "Alarm already set", Toast.LENGTH_LONG).show();
			return false;
		}
		else {
			if(minutes <= 0) {
				Toast.makeText(context, "Should be a positive value", Toast.LENGTH_LONG).show();
				return false;
			}
			
			try {
				Intent alarmIntent = new Intent(context, qnaAlarmReciever.class);
				PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
				AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				setTime = System.currentTimeMillis();
				alarmManager.set(AlarmManager.RTC_WAKEUP, setTime + (minutes*60000), sender);
				setSetTime(setTime);
				setAlarmOn(true);
				setMinutes(minutes);
			}
			catch (Exception e) {
				Toast.makeText(context, "Some error", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}

	
	public boolean cancelAlarm(Activity activity) {
		/*
		 *  This is where an alarm is cancelled when it's still active and set the alarmOn flag off (Also for after stopping the alarm).
		 */
		getAlarmOn();
		
		if(!alarmOn) {
			Toast.makeText(activity, "Alarm not set", Toast.LENGTH_LONG).show();
			return false;
		}
		else {
			try {
				Intent alarmIntent = new Intent(activity, qnaAlarmReciever.class);
				PendingIntent sender = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0);
				AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
				setTime = 0;
				alarmManager.cancel(sender);
			}
			catch (Exception e) {
				Toast.makeText(activity, "Some error", Toast.LENGTH_LONG).show();
				return false;
			}
			setAlarmOn(false);
		}
		return true;
	}

	public boolean getAlarmOn() {
		setAlarmOn(alarmSettings.getBoolean("alarmOn", false));
		return alarmOn;
	}
	
	public String getAlarmTone() {
		setAlarmTone(alarmSettings.getString("alarmTone",
					RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
		return alarmTone;
	}
	
	public int getInterval() {
		setInterval(alarmSettings.getInt("interval", 1));
		return interval;
	}
	
	public int getMinutes() {
		setMinutes(alarmSettings.getInt("minutes", 1));
		return this.minutes;
	}

	public long getSetTime() {
		setSetTime(alarmSettings.getLong("setTime", 0));
		return setTime;
	}

	public void setAlarmOn(boolean alarmOn) {
		this.alarmOn = alarmOn;
		editor.putBoolean("alarmOn", this.alarmOn);
		editor.commit();
	}

	public void setAlarmTone(String alarmTone) {
		this.alarmTone = alarmTone;
		editor.putString("alarmTone", this.alarmTone);
		editor.commit();
	}
	
	public void setMinutes(int minutes) {
		this.minutes = minutes;
		editor.putInt("minutes", this.minutes);
		editor.commit();
	}

	public void setInterval(int interval) {
		this.interval = interval;
		editor.putInt("interval", this.interval);
		editor.commit();
	}
	
	public void setSetTime(long setTime) {
		this.setTime = setTime;
		editor.putLong("setTime", this.setTime);
		editor.commit();
	}
}
