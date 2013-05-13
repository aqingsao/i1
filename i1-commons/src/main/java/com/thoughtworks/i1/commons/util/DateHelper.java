package com.thoughtworks.i1.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DateHelper {
    public static final long MILLI_SECOND = 1;
    public static final long SECOND_IN_MILLISECONDS = 1000 * MILLI_SECOND;
    public static final long MINUTE_IN_MILLISECONDS = 60 * SECOND_IN_MILLISECONDS;
    public static final long HOUR_IN_MILLISECONDS = 60 * MINUTE_IN_MILLISECONDS;
    public static final long DAY_IN_MILLISECONDS = 24 * HOUR_IN_MILLISECONDS;

    public static Date nextDay(Date now) {
        return daysLater(now, 1);
    }

    public static Date today() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date currentTime() {
        return new Date();
    }


    public static Date tomorrow() {
        return nextDay(today());
    }

    public static Date yesterday() {
        return daysLater(today(), -1);
    }

    public static Date monthsLater(Date now, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, amount);
        return c.getTime();
    }

    public static Date daysLater(Date now, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_YEAR, amount);
        return c.getTime();
    }

    public static String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public static long minutes(int minutes){
        return minutes * MINUTE_IN_MILLISECONDS;
    }
    public static Date formatTimestamp(Date timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = sdf.parse(sdf.format(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static Iterable<Date> range(Date startDate, final Date endDate) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return new Iterable<Date>() {
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {
                    @Override
                    public boolean hasNext() {
                        return !calendar.getTime().after(endDate);
                    }

                    @Override
                    public Date next() {
                        Date time = calendar.getTime();
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        return time;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean before(Date date1, Date date2) {
        return date1.before(date2);
    }

    public static boolean notBefore(Date date1, Date date2) {
        return !date1.before(date2);
    }

    public static boolean notAfter(Date date1, Date date2) {
        return !date1.after(date2);
    }
}