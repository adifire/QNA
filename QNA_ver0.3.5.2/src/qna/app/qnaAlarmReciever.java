package qna.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class qnaAlarmReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		/*
		 *  This simply receives the broadcast and then sends it to the alarm handler activity
		 *  where it would carry out the necessary alarm raiser methods
		 */
		Intent handle_alarm = new Intent(context, qnaAlarmHandle.class);
		handle_alarm.addFlags(Intent.FLAG_FROM_BACKGROUND);
		handle_alarm.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		handle_alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(handle_alarm);
	}

}
