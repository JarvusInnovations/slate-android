package is.slate.android.home;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class SlateService extends Service{

	private BroadcastReceiver rec;
	private String ANDROIDID;
	private static final long UPTIME = android.os.SystemClock.uptimeMillis();
	private static final String OS_VR = android.os.Build.VERSION.RELEASE;
	private static final String ANDROID_SERIAL = android.os.Build.ID;
	private String ANDROID_MACADDRESS;

		private static final String URL = "http://kfulton.sites.emr.ge/process";
		Date dat = new Date();
		//CharSequence TIME = DateFormat.format("yyyy-mm-dd HH:ii:ss", dat.getTime());
		@SuppressLint("SimpleDateFormat")
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String TIME = s.format(new Date());
		//TimeUnit.MILLISECONDS.toMinutes(millis);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate(){
		ANDROIDID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		super.onCreate();
		Log.i("SLATEd", "SLATEd Service Started: onCreate()");
		
		try {
			forceWifi();		//Force WiFi on
			bootCollector();	//Collect device info (device specific)
			sendLogFile();		//After 30 seconds and every half hour
			recurTask();		//Collect TaskInfo every 15 seconds 
		}
			catch (IOException e) {e.printStackTrace();}
		
			addIntentFilterStuff();

}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i("SLATEd","OnStartCommand()");
			if(intent.getExtras() != null){
				switch (intent.getExtras().getInt("SLATEd_CODE")){
				case 0: try {
						writetoFile("systemState\t"+TIME+"\tSCREEN_POWERED_OFF\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;
				case 1: try {
						writetoFile("systemState\t"+TIME+"\tSCREEN_POWERED_ON\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 2: try {
					writetoFile("[PackageStatus]: A Application has been installed: "+TIME+";\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 3: try {
					writetoFile("systemState\t"+TIME+"\tHEADSET_PLUGGED_IN\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 4: try {
					writetoFile("systemState\t"+TIME+"\tPOWER_DISCONNECTED\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 5: try {
					writetoFile("systemState\t"+TIME+"\tPOWER_CONNECTED\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 6: try {
					writetoFile("systemState\t"+TIME+"\tUSER_PRESENT\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 7: try {
					writetoFile("systemState\t"+TIME+"\tCAMERA_BUTTON_PUSHED\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

//				case 8: try {
//					writetoFile("batteryState\t"+TIME+"\tCHANGED;\n");
//					} catch (IOException e1){
//					// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} break;

				case 9: try {
					writetoFile("systemState\t"+TIME+"\tBATTERY_IS_LOW\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 10: try {
					writetoFile("systemState\t"+TIME+"\tSYSTEM_SHUTDOWN\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

//				case 11: try {
//					writetoFile("systemState\t"+TIME+"\tDATA_CLEARED\n");
//					} catch (IOException e1) {
//					// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} break;

				case 12: try {
					writetoFile("[MediaCardState]: External Media has been removed: "+TIME+";\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 13: try {
					writetoFile("[MediaCardState]: External Media has been mounted: "+TIME+";\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 14: try {
					writetoFile("[PackageStatus]: An app has been replaced/updated: "+TIME+";\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 15: try {
					writetoFile("[PackageStatus]: An app has been removed completely: "+TIME+";\n");
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				case 16: try {
					Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
					int orientation = display.getOrientation();
					Log.i("SLATEd", Integer.toString(orientation));
					int landsc = Configuration.ORIENTATION_LANDSCAPE;
					int portir = Configuration.ORIENTATION_PORTRAIT;
					int onKey = Configuration.KEYBOARDHIDDEN_YES;
					int offKey = Configuration.KEYBOARDHIDDEN_NO;

						if(orientation == landsc){
							writetoFile("[AppRotate]: LANDSCAPE: "+TIME+";\n");
						}else if(orientation == portir){
							writetoFile("AppRotate\t"+TIME+"\tPORTRAIT\n");
						}else if(orientation == onKey){
							writetoFile("[AppRotate]: KEYBOARD HIDDEN: "+TIME+";\n");
						}else if(orientation == offKey){
							writetoFile("[AppRotate]: KEYBOARD SHOWING: "+TIME+";\n");
						}
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					} break;

				}
			}

	    return START_STICKY;
	}

	@Override
    public void onDestroy() {
		try {
			writetoFile("onDestroy\t"+TIME+"\tSTOPPED\n");
			unregisterReceiver(rec);
			Log.i("SLATEd", "Receiver Unregistered");
		} catch (IOException e) {e.printStackTrace();}
    }
	
	private void addIntentFilterStuff() {
		rec = new SlateReceiver();
		// BroadCast Receivers the programmatic way
		IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
		filter.addAction("android.intent.action.SCREEN_ON");
		filter.addAction("android.intent.action.HEADSET_PLUG");
		filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
		filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
		filter.addAction("android.intent.action.USER_PRESENT");
		filter.addAction("android.intent.action.CAMERA_BUTTON");
		filter.addAction("android.intent.action.BATTERY_CHANGED");
		filter.addAction("android.intent.action.BATTERY_LOW");
		filter.addAction("android.intent.action.MEDIA_MOUNTED");
		filter.addAction("android.intent.action.ACTION_SHUTDOWN");
		filter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
		filter.addAction("android.intent.action.MEDIA_EJECT"); //Data More in extras
		filter.addAction("android.intent.action.MEDIA_MOUNTED");
		filter.addAction("android.intent.action.MY_PACKAGE_REPLACED");
		filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
		registerReceiver(rec, filter);
		
	}

	private void forceWifi() throws IOException{
		//NetworkInfo netInfo = null;
		//netInfo = connectMan.getActiveNetworkInfo();
		//ConnectivityManager connectMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
			Log.i("SLATEd", "WiFi is off");
			writetoFile("systemState\t"+TIME+"\tWiFi IS OFF\n");
			wifi.setWifiEnabled(true);
			Log.i("SLATEd", "WiFi is now turning on");
			writetoFile("wifiState\t"+TIME+"\tON\n");
		}else if(wifi.isWifiEnabled()){
			Log.i("SLATEd", "WiFi is on already");
			writetoFile("systemState\t"+TIME+"\tWiFi ALREADY ON\n");
		}
	}

	private void bootCollector(){
		Date dat = new Date();
		CharSequence s = DateFormat.format("EEEE MMMM d, yyyy", dat.getTime());
		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiMan.getConnectionInfo();
		ANDROID_MACADDRESS = wInfo.getMacAddress();

		SharedPreferences tabletDetails = this.getSharedPreferences("SlateDeviceInfo", MODE_PRIVATE);
		Editor edit = tabletDetails.edit();
		edit.clear();
		edit.putString("MacAddress", ANDROID_MACADDRESS);
		//Log.i("DeviceInfo:", macAddress);
		//edit.putString("Serial", serial);
		Log.i("SLATEd", "Device serial: "+ANDROID_SERIAL);
		edit.putString("Date", (String) s);
		Log.i("SLATEd", "Device Date: "+(String) s);
		edit.putString("DeviceID", ANDROIDID);
		Log.i("SLATEd", "Device ID: "+(String) ANDROIDID);
		edit.commit();
		Log.i("SLATEd", "Done Collecting Boot Data");

	}

	private void recurTask() {
		Timer tim = new Timer();
		tim.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				Log.i("SLATEd", "Collecting Device Data....");
				try {
					MemCollector();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		},10000,15000);
	}

	public void sendLogFile(){
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				try {
					if(isConnected()){
						Log.i("SLATEd", "Sever is UP, Sending Log file");
						writetoFile("Log Ended\t"+TIME+" __EOF__");
						doUpload(getFilesDir().toString()+"/SLATEd.log", "SLATEd.log");
						createNewLog();
					}else{
						Log.i("SLATEd", "SERVER ProBLEM / CAN nOT Connect via HTTP");
						writetoFile("systemState\t"+TIME+"\tSLATEd LOG POST FAILURE\n");
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void createNewLog() throws IOException{
				File file = new File(getFilesDir().toString()+"/SLATEd.log");
				file.delete();
						Log.i("SLATEd", "Log Deleted");
						writetoFile("Log Started\t"+TIME+"\t ttl="+setTTL()+"&deviceID="+ANDROIDID+"&osRelease="+OS_VR+"&uptime="+UPTIME+"&serial="+ANDROID_SERIAL+"&macAddress="+ANDROID_MACADDRESS+"&availableMem="+avilMEM()+"MBs\n");

			}

		},30000,1800000);
	}

	public void writetoFile(String string) throws IOException{
		FileOutputStream fos = openFileOutput("SLATEd.log", Context.MODE_APPEND);
		fos.write(string.getBytes());
		fos.close();
		Log.i("SLATEd", string);
	}
	
	private int setTTL() {
		int currentCount;
		SharedPreferences ttlCount = this.getSharedPreferences("TTL", MODE_PRIVATE);
		if (ttlCount.contains("TTL")) {
			currentCount = ttlCount.getInt("TTL", 0);
			currentCount ++;
			Editor edit = ttlCount.edit();
			edit.clear();
			edit.putInt("TTL", currentCount);
			edit.commit();
			Log.i("SLATEd","TTL is Now "+currentCount);
			}else{
				Log.i("SLATEd","TTL First Ever Set to 1");
			Editor edit = ttlCount.edit();
			edit.clear();
			edit.putInt("TTL", 1);
			edit.commit();
			currentCount = 1;
			}
		return currentCount;
	}
	
	private void setCurrentTask(String taskName) throws UnsupportedEncodingException, IOException{
		String taskSetName;
		SharedPreferences currTask = this.getSharedPreferences("CurrentTaskName", MODE_PRIVATE);
			if(currTask.contains("CurrentTaskName")){
				taskSetName = currTask.getString("CurrentTaskName", null);
					if(taskName.equals(taskSetName)){
						Log.i("SLATEd", "No new task started");
					}else{
						Log.i("SLATEd", "TaskInfo has changed, the new task is "+taskName);
						Editor edit = currTask.edit();
						edit.clear();
						edit.putString("CurrentTaskName", taskName);
						edit.commit();
						writetoFile("taskInfo\t"+TIME+"\tname="+URLEncoder.encode(taskName,"UTF-8")+"&deviceid="+ANDROIDID+"\n");
					}
			}else{
				Log.i("SLATEd","First Task has been set, it is "+taskName);
				Editor edit = currTask.edit();
				edit.clear();
				edit.putString("CurrentTaskName", taskName);
				edit.commit();
			}

		
	}

	@SuppressLint("NewApi")
	public void doUpload(String filepath,String filename) throws ClientProtocolException, IOException {
		HttpClient http = AndroidHttpClient.newInstance("SLATEd");
		HttpPost method = new HttpPost(URL+"?mac="+ANDROID_MACADDRESS);
		method.setEntity(new FileEntity(new File(getFilesDir().toString()+"/SLATEd.log"), "text/plain"));
		HttpResponse response = http.execute(method);
		Log.i("SLATEd", "Server Response: "+response.toString());

}

	private boolean isConnected() throws IOException{
		java.net.URL slateSrv = new java.net.URL(URL);
		URLConnection conn = slateSrv.openConnection();
		conn.setConnectTimeout(20000);
		conn.connect();
		Log.i("SLATEd", "Checking Server Status......");
		return true;
	};

//	@SuppressWarnings("rawtypes")
	public void MemCollector() throws IOException{
	
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		  if (!tasks.isEmpty()){
			  String topActivityName = tasks.get(0).topActivity.toString();
			  setCurrentTask(topActivityName);
		  }
		 
		 }

	private long avilMEM(){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		return availableMegs;
	}


}
