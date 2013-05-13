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
import java.util.Iterator;

import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;

/**
 * Must be strings like "2.1.3", with no extra spaces.<br />
 * 1.1 > 1.0.1 <br />
 * 2.0 > 1.2 <br />
 * 1.0.0 = 1.0 <br />
 */
public class VersionComparator implements Comparator<String> {
	@Override
	public int compare(String object1, String object2) {

		// Should check that the versions are well formed.

		StringSplitter splitter1 = new SimpleStringSplitter('.');
		StringSplitter splitter2 = new SimpleStringSplitter('.');

		splitter1.setString(object1);
		splitter2.setString(object2);

		Iterator<String> it1 = splitter1.iterator();
		Iterator<String> it2 = splitter2.iterator();

		// Yeah dude, this code is soooo sexy!
		while (true) {
			if (!it1.hasNext()) {
				if (!it2.hasNext()) {
					return 0;
				} else if (it2.next().equals("0")) {
					continue;
				} else {
					// version 2 > version 1
					return -1;
				}
			} else if (!it2.hasNext()) {
				if (it1.next().equals("0")) {
					continue;
				} else {
					return 1;
				}
			} else {
				int compare = Integer.valueOf(it1.next()).compareTo(Integer.valueOf(it2.next()));
				if (compare != 0) {
					return compare;
				}
			}
		}

	}
}