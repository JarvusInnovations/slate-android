/*
 * Copyright 2009 Pierre-Yves Ricau
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package is.slate.android.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.googlecode.plouf.AbstractApplicationDownloader;
import com.googlecode.plouf.ApplicationDownloader;
import com.googlecode.plouf.ApplicationUpdater;
import com.googlecode.plouf.UpdateCheckedListener;
import com.googlecode.plouf.UpdateChecker;
import com.googlecode.plouf.UpdateExceptionListener;
import com.googlecode.plouf.UpdateInfo;
import com.googlecode.plouf.VersionComparator;
import com.googlecode.plouf.task.CheckUpdateTask;
import com.googlecode.plouf.task.UpdateApplicationTask;

public class UpdateApplicationActivity extends Activity implements UpdateCheckedListener {

	private static final int	DIALOG_CHECKING_UPDATE		= 1;
	//private static final int	DIALOG_CHECKING_EXCEPTION	= 2;
	private static final int	DIALOG_UPDATING				= 3;
	//private static final int	DIALOG_UPDATING_EXCEPTION	= 4;

	private String				updateVersion;
	private ProgressDialog		checkingUpdateDialog;
	private ProgressDialog		updatingDialog;
	//private String				updatingExceptionMessage;
	//private String				checkingExceptionMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String versionName = null;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new IllegalStateException("Should not happen", e);
		}
		
		Log.d(UpdateApplicationActivity.class.getSimpleName(), "Got current version: "+ versionName);

		UpdateChecker checker = new SimpleUrlUpdateChecker("http://wilco.io/SlateHome/latest", versionName, new VersionComparator());

		new CheckUpdateTask(checker, this).execute();

		showDialog(DIALOG_CHECKING_UPDATE);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d(UpdateApplicationActivity.class.getSimpleName(), "Creating dialog: " + id);

		switch (id) {
			case DIALOG_CHECKING_UPDATE:
				checkingUpdateDialog = new ProgressDialog(this);
				checkingUpdateDialog.setTitle("Checking for update");
				checkingUpdateDialog.setCanceledOnTouchOutside(false);
				checkingUpdateDialog.setCancelable(false);
				checkingUpdateDialog.setMessage("Making sure you have the latest software, this could take a moment");
				checkingUpdateDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				return checkingUpdateDialog;
			case DIALOG_UPDATING:
				updatingDialog = new ProgressDialog(this);
				updatingDialog.setTitle("Updating application");
				updatingDialog.setCanceledOnTouchOutside(false);
				updatingDialog.setCancelable(false);
				return updatingDialog;
				
			/*
			case DIALOG_UPDATING_EXCEPTION:

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false).setMessage("").setPositiveButton("Retry update", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						updateToVersion(updateVersion);
					}
				}).setNegativeButton("Exit application", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						UpdateApplicationActivity.this.finish();
					}
				});
				return builder.create();
			*/
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
			case DIALOG_UPDATING:
				((ProgressDialog) dialog).setMessage("Updating to version " + updateVersion);
				break;
			/*
			case DIALOG_UPDATING_EXCEPTION:
				((AlertDialog) dialog).setMessage("An exception occurred while updating: " + updatingExceptionMessage);
				break;
			*/
		}
	}

	@Override
	public void onUpdateChecked(UpdateInfo updateInfo) {
		checkingUpdateDialog.dismiss();
		if (updateInfo == null) {
			Intent i = new Intent(this, SlateHome.class);
			startActivity(i);
		} else {
			updateVersion = updateInfo.getVersionName();
			updateToVersion(updateVersion);
		}
	}

	private void updateToVersion(final String version) {

		showDialog(DIALOG_UPDATING);

		ApplicationDownloader downloader = new AbstractApplicationDownloader() {
			@Override
			protected String getDownloadUrl() {
				return "http://wilco.io/SlateHome/" + version + ".apk";
			}
		};
		
		String downloadPath = getCacheDir().getAbsolutePath() + "/AppUpdating.apk";
		Log.d(UpdateApplicationActivity.class.getSimpleName(), "Saving update to: "+ downloadPath);
		
		ApplicationUpdater updater = new ApplicationUpdater(downloader, downloadPath);
		UpdateExceptionListener listener = new UpdateExceptionListener() {
			@Override
			public void onUpdateException(Exception e) {
				Log.e(UpdateApplicationActivity.class.getSimpleName(), "An Exception occurred while updating", e);
				updatingDialog.dismiss();
				onUpdateFailed(e);
			}
		};

		new UpdateApplicationTask(this, updater, listener).execute();
	}

	@Override
	public void onUpdateFailed(Exception e) {
		finish();
		Intent i = new Intent(this, SlateHome.class);
		startActivity(i);
	}
}
