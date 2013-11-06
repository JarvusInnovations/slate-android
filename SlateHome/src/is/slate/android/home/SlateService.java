package is.slate.android.home;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
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
//	private static final long TRDUPTIME = android.os.SystemClock.currentThreadTimeMillis();

	//private static final String URL = "http://myscoutteam.net/test/Slated/slated.php";
		//private static final String URL = "http://10.20.30.42/Slated/slated.php";
		//private static final String URL = "http://furiousbox.sytes.net/Slated/slated.php";
		private static final String URL = "http://kfulton.sites.emr.ge/process";
		//private static final String URL = "http://kfulton.sites.emr.ge/register-tablet";
		Date dat = new Date();
		//CharSequence TIME = DateFormat.format("yyyy-mm-dd HH:ii:ss", dat.getTime());
		@SuppressLint("SimpleDateFormat")
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String TIME = s.format(new Date());

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
			sendLogFile();		//After 30 seconds, every half an hour
			recurTask();
			MemCollector();		//Collect task Info
		}
			catch (IOException e) {e.printStackTrace();}

		// BroadCast Receivers the programmatic way
		rec = new SlateReceiver();
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

	//	int ipaddress = wInfo.getIpAddress();


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
		// TODO Auto-generated method stub
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
		},10000,360000);
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
//						writetoFile("[Log TTL]: "+setTTL()+";\n");
//						writetoFile("[DeviceID]: "+ANDROIDID+";\n");
//						writetoFile("[OS_Release]: "+OS_VR+";\n");
//						writetoFile("[DeviceUptime]: "+UPTIME+";\n");
//						writetoFile("[DeviceSerial]: "+ANDROID_SERIAL+";\n");
//						writetoFile("[DeviceMacAddress]: "+ANDROID_MACADDRESS+";\n");
//						writetoFile("availableMemory:"+avilMEM()+"MBs\n");
			}

		},30000,1800000);
	}

	public void writetoFile(String string) throws IOException{
		FileOutputStream fos = openFileOutput("SLATEd.log", Context.MODE_APPEND);
		fos.write(string.getBytes());
		fos.close();
		Log.i("SLATEd", string);
	}

	@SuppressLint("NewApi")
	//@SuppressWarnings("deprecation")
	public void doUpload(String filepath,String filename) throws ClientProtocolException, IOException {

//		HttpClient httpclient = new DefaultHttpClient();
//	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//	    HttpPost httppost = new HttpPost(URL);
//	    File file = new File(getFilesDir().toString()+"/SLATEd.log");
//	    //FileBody fileBody = new FileBody(file, "application/octet-stream");
//	    MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//	    ContentBody cbFile = new FileBody(file, "text/plain");
//	    mpEntity.addPart("log", cbFile);
//	    //mpEntity.addPart("androidid", new StringBody(ANDROIDID));
//	    httppost.setEntity(mpEntity);
//	    HttpResponse response = httpclient.execute(httppost);
//	    HttpEntity resEntity = response.getEntity();
//	    String res = resEntity.toString();
//	    Log.i("SLATEd", res);

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

	@SuppressWarnings("rawtypes")
	public void MemCollector() throws IOException{

		ActivityManager mgr = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningServiceInfo> services = mgr.getRunningServices(100);
//		List<ActivityManager.RunningTaskInfo> tasks = mgr.getRunningTasks(100);
//
//		for(Iterator i = tasks.iterator();i.hasNext();){
//			RunningTaskInfo task = (RunningTaskInfo)i.next();
//			Log.e("debug","name: "+task.baseActivity.getPackageName());
//			Log.e("debug","num of activities"+task.numActivities);
//			Log.e("debug","top activity: "+task.topActivity.getPackageName());
//			Log.e("debug","top activity: "+task.topActivity.getClassName());
//		}

		List<RunningServiceInfo> services = mgr.getRunningServices(100);
		Log.e("DEBUG", "services:");
		for(Iterator<RunningServiceInfo> i = services.iterator();i.hasNext();){
				RunningServiceInfo p = (RunningServiceInfo)i.next();
//				Log.e("SLATEd", "     process name: "+p.process);
//				Log.e("SLATEd", "     user id of owner: "+p.uid);
//				Log.e("SLATEd", "     number of clients: "+p.clientCount);
//				Log.e("SLATEd", "     client package name: "+p.clientPackage);
//				Log.e("SLATEd", "     activeSince started (secs): "+p.activeSince/1000.0);
//				Log.e("SLATEd", "     last active: "+p.lastActivityTime/1000.0);
				writetoFile("serviceInfo\t"+TIME+"\tname="+p.process+"&activeSince="+p.activeSince+"&lastActive="+p.lastActivityTime+"\n");

			}

		 List<ActivityManager.RecentTaskInfo> recentTasks = mgr.getRecentTasks(100,ActivityManager.RECENT_WITH_EXCLUDED);
		 //Log.e("SLATEd", "Recently started tasks");
		 int count = 0;
		 for(Iterator i = recentTasks.iterator();i.hasNext();){
			 count++;
			 RecentTaskInfo p = (RecentTaskInfo)i.next();
			// Log.e("SLATEd", "  package name: "+p.baseIntent.getComponent().getPackageName());
			 writetoFile("taskInfo\t"+TIME+"\tname="+URLEncoder.encode(p.baseIntent.getComponent().getPackageName(),"UTF-8")+"&order="+count+"&deviceid="+ANDROIDID+"\n");
		 }

		List<RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();
		//Log.e("SLATEd", "Running processes:");
		for(Iterator i = processes.iterator(); i.hasNext();){
			RunningAppProcessInfo p = (RunningAppProcessInfo)i.next();
			writetoFile("appProcesses\t"+TIME+"\tname="+URLEncoder.encode(p.processName,"UTF-8")+"&pid="+p.pid+"\n");
			//Log.e("SLATEd", "  process name: "+p.processName);
			//Log.e("SLATEd", "     pid: "+p.pid);                    // process id
//            for( String str : p.pkgList){
//            	Log.e("DEBUG", "     package: "+str);
//            }
//			writetoFile("[RunningProcesses]: Process name: "+p.processName+", "+TIME+";\n"+"[RunningProcesses]: Process pid: "+p.pid+", "+TIME+";\n");

		}

	}

//	chkConn =  new Thread(){
//	@Override
//	public void run(){
//	super.run();
//	try {
//		PostBootData();
//	} catch (ClientProtocolException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//}
//};chkConn.start();

	private long avilMEM(){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		return availableMegs;

	}

//	int width = getWindowManager().getDefaultDisplay().getWidth();
//	int height = getWindowManager().getDefaultDisplay().getHeight();



}
