package com.sciome.opera;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomMatcher {
	private static double epsilon = .0001;
	
    public static Matcher matches(final Object expected){
            return new BaseMatcher() {
                protected Object expectedObject = expected;
                public boolean matches(Object item) {
                	if(item instanceof Double && expected instanceof Double) {
                		if(((Double)item).isNaN() && ((Double)expected).isNaN()) {
                			return true;
                		}
                		if(Math.abs(((Double)item) - ((Double)expected)) < epsilon) {
                			return true;
                		} else {
                			return false;
                		}
                	} else {
                		return expectedObject.equals(item);
                	}
                }
                public void describeTo(Description description) {
                    description.appendText(expectedObject.toString());
                }
            };
    }
}
