package qna.app;

import java.text.NumberFormat;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class qnaWidget extends AppWidgetProvider {
	public static String PLUS_ACTION = "plus";
	public static String MINUS_ACTION = "minus";
	public static String BUZZ_ACTION = "buzz";
	public static int interval;
	public static int minutes;
	boolean mBound = false;
	boolean alarmOn = false;
	long currentTime;
	
	RemoteViews remoteView;
	ComponentName widgetComponent;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Intent buttons = new Intent(context, qnaWidget.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, buttons, 0);
		remoteView = new RemoteViews(context.getPackageName(), R.layout.qna_widget);
		
		buttons.setAction(PLUS_ACTION);
		pendingIntent = PendingIntent.getBroadcast(context, 0, buttons, 0);
		remoteView.setOnClickPendingIntent(R.id.w_plus_b, pendingIntent);
		
		buttons = new Intent(context, qnaWidget.class);
		buttons.setAction(MINUS_ACTION);
		pendingIntent = PendingIntent.getBroadcast(context, 0, buttons, 0);
		remoteView.setOnClickPendingIntent(R.id.w_minus_b, pendingIntent);
		
		buttons = new Intent(context, qnaWidget.class);
		buttons.setAction(BUZZ_ACTION);
		pendingIntent = PendingIntent.getBroadcast(context, 0, buttons, 0);
		remoteView.setOnClickPendingIntent(R.id.w_buzz_b, pendingIntent);
		
		widgetComponent = new ComponentName(context, qnaWidget.class);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		appWidgetManager.updateAppWidget(widgetComponent, remoteView);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		qnaAlarmSetter alarmSetter = new qnaAlarmSetter(context);
		interval = alarmSetter.getInterval();
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.qna_widget);
		NumberFormat nf = NumberFormat.getInstance();
		
		Log.i("onRecieve", "onRecieve called with " + intent.getAction());
		
		nf.setMinimumIntegerDigits(2);
		String text;
		if(intent.getAction().equals(PLUS_ACTION)){
			minutes += interval;
			if(minutes > 59) {
				text = nf.format(minutes / 60) + ":" + nf.format(minutes % 60);
			}
			else if(minutes < 0) {
				text = "00:00";
				minutes = 0;
			}
			else {
				text =  "00:" + nf.format(minutes % 60);
			}
			rv.setTextViewText(R.id.w_min_text, text);
			Toast.makeText(context, "sss", Toast.LENGTH_LONG);
		}
		
		else if(intent.getAction().equals(MINUS_ACTION)){
			minutes -= interval;
			if(minutes > 59) {
				text = nf.format(minutes / 60) + ":" + nf.format(minutes % 60);
			}
			else if(minutes < 0) {
				text = "00:00";
				minutes = 0;
			}
			else {
				text =  "00:" + nf.format(minutes % 60);
			}
			rv.setTextViewText(R.id.w_min_text, text);
		}
		
		else if(intent.getAction().equals(BUZZ_ACTION)){
			alarmOn = alarmSetter.getAlarmOn();
			if(!alarmOn){
				alarmSetter.setAlarm(context, minutes);
				Toast.makeText(context, "Alarm set", Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(context, "Alarm already set", Toast.LENGTH_LONG).show();
			}
		}
		else {
			super.onReceive(context, intent);
		}
		
		ComponentName cn = new ComponentName(context, qnaWidget.class);
		AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
	}
	
	
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		remoteView = new RemoteViews(context.getPackageName(), R.layout.qna_widget);
		minutes = 0;
	}

}
