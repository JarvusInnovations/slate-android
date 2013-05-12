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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractApplicationDownloader implements ApplicationDownloader {

	@Override
	public InputStream getDownloadInputStream() throws IOException {
		URL u = new URL(getDownloadUrl());

		HttpURLConnection c = (HttpURLConnection) u.openConnection();

		c.setRequestMethod("GET");
		c.setDoOutput(true);
		c.connect();

		return c.getInputStream();
	}

	protected abstract String getDownloadUrl();
}
