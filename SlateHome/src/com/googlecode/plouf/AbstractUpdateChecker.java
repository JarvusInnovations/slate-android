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

import java.util.Comparator;

public abstract class AbstractUpdateChecker implements UpdateChecker {

	protected final String				currentVersion;
	protected final Comparator<String>	versionComparator;

	public AbstractUpdateChecker(String currentVersion, Comparator<String> versionComparator) {

		this.currentVersion = currentVersion;
		this.versionComparator = versionComparator;
	}

	@Override
	public UpdateInfo checkForUpdates() {

		UpdateInfo updateInfo = findLatestReleaseInfo();

		if (versionComparator.compare(currentVersion, updateInfo.getVersionName()) < 0) {
			return updateInfo;
		}
		return null;
	}

	/**
	 * Must return a string representation of the version number, something
	 * like"32.1.3" etc
	 * 
	 * @return
	 */
	protected abstract UpdateInfo findLatestReleaseInfo();

}