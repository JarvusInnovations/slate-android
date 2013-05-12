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

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.googlecode.plouf.ApplicationUpdater;
import com.googlecode.plouf.UpdateExceptionListener;

public class UpdateApplicationTask extends AsyncTask<Object, Object, IOException> {

	private final Context					context;
	private final ApplicationUpdater		applicationUpdater;
	private final UpdateExceptionListener	updateExceptionListener;

	public UpdateApplicationTask(Context context, ApplicationUpdater applicationUpdater, UpdateExceptionListener updateExceptionListener) {
		this.context = context;
		this.applicationUpdater = applicationUpdater;
		this.updateExceptionListener = updateExceptionListener;
	}

	@Override
	protected IOException doInBackground(Object... params) {
		try {
			applicationUpdater.updateApplication(context);
			return null;
		} catch (IOException e) {
			return e;
		}
	}

	@Override
	protected void onPostExecute(IOException result) {
		if (result != null) {
			updateExceptionListener.onUpdateException(result);
		}
	}

}
