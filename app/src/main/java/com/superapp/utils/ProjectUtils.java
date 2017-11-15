package com.superapp.utils;

import com.superapp.beans.BeanAddress;

import java.util.Calendar;

public class ProjectUtils {
    private static ProjectUtils instance;

    private ProjectUtils() {

    }

    public static ProjectUtils getInstance() {
        if (instance != null)
            return instance;
        instance = new ProjectUtils();
        return instance;
    }

    public BeanAddress getFormattedAddress(String address) {
        BeanAddress beanAddress = new BeanAddress();
        try {
            String[] splitAddress = address.split("###");
            beanAddress.setAddressLine1(splitAddress[0]);
            beanAddress.setAddressLine2(splitAddress[1]);
            beanAddress.setAddressLine3(splitAddress[2]);
            beanAddress.setCity(splitAddress[3]);
            beanAddress.setPincode(splitAddress[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanAddress;
    }

    public String getFormattedDate(Calendar cal) {
//        DAY_OF_MONTH  with format DD/MM/YYYY
        return (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE) + "/"
                + ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.YEAR);
    }

    public String getFormattedTime(Calendar cal) {
//        HOUR_OF_DAY   with format 24 Hours
        int hours = cal.get(Calendar.HOUR_OF_DAY);

        int mod = (hours % 12);
        return (mod < 10 && mod != 0 ? "0" : "") + ((mod == 0) ? "12" : mod) + ":"
                + (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE)
                + " " + (((hours / 12) > 0) ? "PM" : "AM");
//        return (hours < 10 ? "0" : "") + hours + ":"
//                + (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE);
    }

    public long getDaysDifferenceFromCurrentDate(long otherDateStamp) {
        try {
            Calendar now = Calendar.getInstance();
            long dateDiff = ((now.getTimeInMillis() - (otherDateStamp)) / (1000 * 60 * 60 * 24));
//            return (dateDiff < 0 ? dateDiff * -1 : dateDiff) + "";
//            return ((dateDiff < 0) ? 0 : dateDiff) + "";
            return dateDiff;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void resetTimeTo00(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
