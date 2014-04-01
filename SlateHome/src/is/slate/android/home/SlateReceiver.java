package is.slate.android.home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SlateReceiver extends BroadcastReceiver{

	public static final int BOOT_COMPLETED = 99;
	public static final int ACTION_SCREEN_ON = 1;
	public static final int APP_INSTALLED = 2;
	public static final int ACTION_HEADSET_PLUG = 3;
	public static final int ACTION_POWER_DISCONNECTED = 4;
	public static final int ACTION_POWER_CONNECTED = 5;
	public static final int ACTION_USER_PRESENT = 6;
	public static final int ACTION_CAMERA_BUTTON = 7;
	public static final int BATTERY_CHANGED = 8;
	public static final int BATTERY_LOW = 9;
	public static final int SYSTEM_SHUTDOWN = 10;
	public static final int DATA_CLEARED = 11;
	public static final int ACTION_MEDIA_EJECT = 12;
	public static final int ACTION_MEDIA_MOUNTED = 13;
	public static final int PACKAGE_REPLACED = 14;
	public static final int PACKAGE_REMOVED = 15;
	public static final int CONFIGURATION_CHANGED = 16;
	public static final int ACTION_SCREEN_OFF = 17;
	
	@Override
	public void onReceive(Context context, Intent intent) {
			Log.i("SLATEd", "Service Called: onStartCommand");
			if(intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)){
				Log.i("SLATEd", "Screen Went Off");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_SCREEN_OFF);
				context.startService(i);
			}else if((intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))){
				Log.i("SLATEd", "System Just Booted");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", BOOT_COMPLETED);
				context.startService(i);
			}else if(intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_ON)){
				Log.i("SLATEd", "Screen is on");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_SCREEN_ON);
				context.startService(i);
			}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
				Log.i("SLATEd", "An App has Been Installed");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", APP_INSTALLED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
				Log.i("SLATEd", "A Heaset has been Plugged-in");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_HEADSET_PLUG);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){
				Log.i("SLATEd", "Power has been Disconnected");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_POWER_DISCONNECTED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
				Log.i("SLATEd", "Power has been Connected");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_POWER_CONNECTED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
				Log.i("SLATEd", "User Present Hello Welcome Back!");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("user_state", ACTION_USER_PRESENT);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_CAMERA_BUTTON)){
				Log.i("SLATEd", "Camera Button has been pushed");
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
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", BATTERY_LOW);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
				Log.i("SLATEd", "SYSTEM DOWN");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", SYSTEM_SHUTDOWN);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_DATA_CLEARED)){
				Log.i("SLATEd", "Package Data Cleared");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", DATA_CLEARED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)){
				Log.i("SLATEd", "Memory Card Removed");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_MEDIA_EJECT);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
				Log.i("SLATEd", "Memory Card Mounted");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", ACTION_MEDIA_MOUNTED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){
				Log.i("SLATEd", "Package Updated");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", PACKAGE_REPLACED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
				Log.i("SLATEd", "Package full removed");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", PACKAGE_REMOVED);
				context.startService(i);
			}else if(intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)){
				Log.i("SLATEd", "Tablet Rotated");
				Intent i = new Intent(context, SlateService.class);
				i.putExtra("SLATEd_CODE", CONFIGURATION_CHANGED);
				context.startService(i);
			}
			//context.startService(new Intent(context, SlateService.class));
		}

	}


