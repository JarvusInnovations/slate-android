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
package com.googlecode.plouf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ApplicationUpdater {

	protected final ApplicationDownloader	appDownloader;

	protected final String					tmpFilePath;

	private static final int				BUFFER_SIZE	= 1024;

	public ApplicationUpdater(ApplicationDownloader appDownloader, String tmpFilePath) {
		this.appDownloader = appDownloader;
		this.tmpFilePath = tmpFilePath;
	}

	public void updateApplication(Context context) throws IOException {
		InputStream is = appDownloader.getDownloadInputStream();

		File f = new File(tmpFilePath);
		f.setReadable(true, false);

		OutputStream out = new FileOutputStream(f);
		copy(is, out);

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + tmpFilePath), "application/vnd.android.package-archive");
		i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	/**
	 * Code extracted from Spring ;-)
	 */
	private static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

}
