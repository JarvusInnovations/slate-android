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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.googlecode.plouf.AbstractUpdateChecker;
import com.googlecode.plouf.UpdateInfo;

public class SimpleUrlUpdateChecker extends AbstractUpdateChecker {

	protected final String	url;

	public SimpleUrlUpdateChecker(String url, String currentVersion, Comparator<String> versionComparator) {
		super(currentVersion, versionComparator);
		this.url = url;
	}

	@Override
	protected UpdateInfo findLatestReleaseInfo() {

		BufferedReader br = null;
		HttpClient client = null;
		try {

			client = new DefaultHttpClient();
			HttpGet method = new HttpGet(url);

			HttpResponse response = client.execute(method);
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				throw new NullPointerException("No entity found in response. The Http Request seems to have no body.");
			}

			br = new BufferedReader(new InputStreamReader(entity.getContent()));

			return new UpdateInfo(br.readLine(), "Currently no description", true);

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
			}
			if (client != null) {
				client.getConnectionManager().shutdown(); // Is it ok ?
			}
		}
	}
}
