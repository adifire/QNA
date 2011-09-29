package qna.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class qnaAlarmHandle extends Activity {
	qnaAlarmSetter alarmSetter;
	String notification_id = "qna";
	private OnClickListener alarmStopClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			alarmSetter.cancelAlarm(qnaAlarmHandle.this);
			final NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(notification_id, 1);
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qna_alarm_handle);
		alarmSetter = new qnaAlarmSetter(this);
		
		final NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		Uri ringUri = Uri.parse(alarmSetter.getAlarmTone());
		notification.sound = ringUri;
		
		try {
			nm.notify(notification_id, 1, notification);
		} catch (Exception e) {
			Toast.makeText(this, "Could not set the alarm tone", Toast.LENGTH_LONG);
		}
		
		Button alarmStop = (Button)findViewById(R.id.qna_alarm_handle_button);
		alarmStop.setOnClickListener(alarmStopClick);
	}
	
	@Override
	public void onBackPressed() {
		// It's not supposed to go back anywhere unless the user stops the alarm.. :)
	}
}

