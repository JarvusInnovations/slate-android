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

public class UpdateInfo {

	private final String	versionName;
	private final String	updateDescription;

	private final boolean	forceUpdate;

	public UpdateInfo(String versionName, String updateDescription, boolean forceUpdate) {
		this.versionName = versionName;
		this.updateDescription = updateDescription;
		this.forceUpdate = forceUpdate;
	}

	public String getVersionName() {
		return versionName;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

}
