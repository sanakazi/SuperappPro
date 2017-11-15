package com.superapp.fragment.dashboard.pager_fragment.tasklist;

/**
 * Created by ss on 08-Sep-16.
 */
public enum TaskType {

    Process("Process"),
    Pending("Pending"),
    Delayed("Delayed"),
    History("History"),;

    private final String value;

    TaskType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }

}
