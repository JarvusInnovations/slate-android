package is.slate.android.home;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SlateReceiver extends BroadcastReceiver{

	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
	String TIME = s.format(new Date());
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
			Log.i("SLATEd", "Service Called: onStartCommand");
		    	if(intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)){
		    		Log.i("SLATEd", "Screen Went Off");
		    		final int ACTION_SCREEN_OFF = 17;
		    		Intent i = new Intent(context, SlateService.class);
		    		i.putExtra("SLATEd_CODE", ACTION_SCREEN_OFF);
		    	    context.startService(i);
		    	}else if((intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))){
		    		Log.i("SLATEd", "System Just Booted");
		    		final String BOOT_COMPLETED = "System Just Booted";
		    		Intent i = new Intent(context, SlateService.class);
		    		i.putExtra("SLATEd_CODE", BOOT_COMPLETED);
		    	    context.startService(i);

		    	}else if(intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_ON)){
		    		Log.i("SLATEd", "Screen is on");
		    		final int ACTION_SCREEN_ON = 1;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_SCREEN_ON);
		    	    context.startService(i);
		    	}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
		    		Log.i("SLATEd", "An App has Been Installed");
		    		final int APP_INSTALLED = 2;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", APP_INSTALLED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
		    		Log.i("SLATEd", "A Heaset has been Plugged-in");
		    		final int ACTION_HEADSET_PLUG = 3;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_HEADSET_PLUG);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
		    		Log.i("SLATEd", "Power has been Disconnected");
		    		final int ACTION_POWER_DISCONNECTED = 4;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_POWER_DISCONNECTED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
		    		Log.i("SLATEd", "Power has been Connected");
		    		final int ACTION_POWER_CONNECTED = 5;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_POWER_CONNECTED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
		    		Log.i("SLATEd", "User Present Hello Welcome Back!");
		    		final int ACTION_USER_PRESENT = 6;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("user_state", ACTION_USER_PRESENT);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_CAMERA_BUTTON)){
		    		Log.i("SLATEd", "Camera Button has been pushed");
		    		final int ACTION_CAMERA_BUTTON = 7;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_CAMERA_BUTTON);
		    	    context.startService(i);
		    	}

//		    	else if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
//		    		Log.i("SLATEd", "Battery Level Changed");
//		    		final int BATTERY_CHANGED = 8;
//		    		Intent i = new Intent(context, SlateService.class);
//		    	    i.putExtra("SLATEd_CODE", BATTERY_CHANGED);
//		    	    context.startService(i);
//		    	}

		    	else if(intent.getAction().equals(Intent.ACTION_BATTERY_LOW)){
		    		Log.i("SLATEd", "Battery Level Low");
		    		final int BATTERY_LOW = 9;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", BATTERY_LOW);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
		    		Log.i("SLATEd", "SYSTEM DOWN");
		    		final int SYSTEM_SHUTDOWN = 10;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", SYSTEM_SHUTDOWN);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_DATA_CLEARED)){
		    		Log.i("SLATEd", "Package Data Cleared");
		    		final int DATA_CLEARED = 11;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", DATA_CLEARED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)){
		    		Log.i("SLATEd", "Memory Card Removed");
		    		final int ACTION_MEDIA_EJECT = 12;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_MEDIA_EJECT);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
		    		Log.i("SLATEd", "Memory Card Mounted");
		    		final int ACTION_MEDIA_MOUNTED = 13;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", ACTION_MEDIA_MOUNTED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){
		    		Log.i("SLATEd", "Package Updated");
		    		final int PACKAGE_REPLACED = 14;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", PACKAGE_REPLACED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
		    		Log.i("SLATEd", "Package full removed");
		    		final int PACKAGE_REMOVED = 15;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", PACKAGE_REMOVED);
		    	    context.startService(i);
		    	}else if(intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)){
		    		Log.i("SLATEd", "Tablet Rotated");
		    		final int CONFIGURATION_CHANGED = 16;
		    		Intent i = new Intent(context, SlateService.class);
		    	    i.putExtra("SLATEd_CODE", CONFIGURATION_CHANGED);
		    	    context.startService(i);
		    	}
				//context.startService(new Intent(context, SlateService.class));
		}

	}


