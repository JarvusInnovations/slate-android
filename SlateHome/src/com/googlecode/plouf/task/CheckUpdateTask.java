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
package com.googlecode.plouf.task;

import android.os.AsyncTask;

import com.googlecode.plouf.UpdateCheckedListener;
import com.googlecode.plouf.UpdateChecker;
import com.googlecode.plouf.UpdateInfo;

public class CheckUpdateTask extends AsyncTask<Object, Object, UpdateInfo> {

	private final UpdateCheckedListener	updateCheckedListener;
	private final UpdateChecker			updateChecker;

	private RuntimeException			exception;

	public CheckUpdateTask(UpdateChecker updateChecker, UpdateCheckedListener updateCheckedListener) {
		this.updateChecker = updateChecker;
		this.updateCheckedListener = updateCheckedListener;
	}

	@Override
	protected UpdateInfo doInBackground(Object... params) {
		try {
			return updateChecker.checkForUpdates();
		} catch (RuntimeException e) {
			exception = e;
			return null;
		}
	}

	@Override
	protected void onPostExecute(UpdateInfo updateInfo) {
		if (exception != null) {
			updateCheckedListener.onUpdateFailed(exception);
		} else {
			updateCheckedListener.onUpdateChecked(updateInfo);
		}
	}

}
