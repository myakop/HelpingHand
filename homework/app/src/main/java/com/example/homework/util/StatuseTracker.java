package com.example.homework.util;

import java.util.*;

public class StatuseTracker {
    private Map<String, String> mStatusMap;
    private List<String> mMethodList;
    private static StatuseTracker ourInstance = new StatuseTracker();
    private static final String STATUS_SUFFIX = "ed";

    public static StatuseTracker getInstance() {
        return ourInstance;
    }

    private StatuseTracker() {
        mStatusMap = new LinkedHashMap<String, String>();
        mMethodList = new ArrayList<String>();
    }

    public List<String> getMethodList() {
        return mMethodList;
    }

    public void clear() {
        mMethodList.clear();
        mStatusMap.clear();
    }


    public void setStatus(String activityName, String status) {
        mMethodList.add(activityName + "." + status + "()");
        if (mStatusMap.containsKey(activityName)) mStatusMap.remove(activityName);
        mStatusMap.put(activityName, status);
    }


    public String getStatus(String activityName) {
        String status = mStatusMap.get(activityName);
        status = status.substring(2, status.length());

        // String manipulation to ensure the status value is spelled correctly.
        if (status.endsWith("e")) {
            status = status.substring(0, status.length() - 1);
        }
        if (status.endsWith("p")) {
            status = status + "p";
        }
        status = status + STATUS_SUFFIX;
        return status;
    }

    public Set<String> keySet() {
        return mStatusMap.keySet();
    }
}