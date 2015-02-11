/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */



 /*
  * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
  
  
  package org.mot.feeder.iab.client;

import java.util.Vector;

public class Util {

	public static boolean StringIsEmpty(String str) {
		return str == null || str.length() == 0;
	}

    public static String NormalizeString(String str) {
    	return str != null ? str : "";
    }

    public static int StringCompare(String lhs, String rhs) {
    	return NormalizeString(lhs).compareTo(NormalizeString(rhs));
    }

    public static int StringCompareIgnCase(String lhs, String rhs) {
    	return NormalizeString(lhs).compareToIgnoreCase(NormalizeString(rhs));
    }

    public static boolean VectorEqualsUnordered(Vector lhs, Vector rhs) {

    	if (lhs == rhs)
    		return true;

    	int lhsCount = lhs == null ? 0 : lhs.size();
    	int rhsCount = rhs == null ? 0 : rhs.size();

    	if (lhsCount != rhsCount)
    		return false;

    	if (lhsCount == 0)
    		return true;

    	boolean[] matchedRhsElems = new boolean[rhsCount];

    	for (int lhsIdx = 0; lhsIdx < lhsCount; ++lhsIdx) {
    		Object lhsElem = lhs.get(lhsIdx);
    		int rhsIdx = 0;
    		for (; rhsIdx < rhsCount; ++rhsIdx) {
    			if (matchedRhsElems[rhsIdx]) {
    				continue;
    			}
    			if (lhsElem.equals(rhs.get(rhsIdx))) {
    				matchedRhsElems[rhsIdx] = true;
    				break;
    			}
    		}
    		if (rhsIdx >= rhsCount) {
    			// no matching elem found
    			return false;
    		}
    	}

    	return true;
    }

    public static String IntMaxString(int value) {
    	return (value == Integer.MAX_VALUE) ? "" : "" + value;
    }

    public static String DoubleMaxString(double value) {
    	return (value == Double.MAX_VALUE) ? "" : "" + value;
    }

}
