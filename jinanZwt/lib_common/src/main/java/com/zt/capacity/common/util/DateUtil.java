package com.zt.capacity.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YMD2 = "yyyy.MM.dd";
    public static final String DATE_FORMAT_YM = "yyyy-MM";
    public static final String DATE_FORMAT_YM2 = "yyyy.MM";
    public static final String DATE_FORMAT_MD = "MM-dd";
    public static final String DATE_FORMAT_MS = "mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final String DATE_FORMAT_HMSS = "HH:mm MM-dd";
    public static final String DATE_FORMAT_MDHM = "MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHM_D = "yyyy:MM:dd HH:mm";
    public static final String DATE_FORMAT_YMD_Z = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_MDHM_Z = "MM月dd日 HH:mm";
    public static final String DATE_FORMAT_MDHM_Z_D = "MM月dd日";
    public static final String DATE_FORMAT_Y = "yyyy";
    public static final String DATE_FORMAT_M = "MM";
    public static final String DATE_FORMAT_D = "dd";
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";
    public static final String DATE_FORMAT_YMD_2 = "yyyy/MM/dd";

    public static String showDate(String format, long millis) {
        return new SimpleDateFormat(format).format(new Date(millis));
    }

    public static String showDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String showDate4Card(long millis) {
        return new SimpleDateFormat("MM/dd").format(new Date(millis));
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static int getAge(String birth) {
        Date newTime = new Date();
        // 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String todayStr = format.format(newTime);
        String date = todayStr.substring(0, 4);
        String date1 = birth.substring(0, 4);
        int age = Integer.parseInt(date) - Integer.parseInt(date1);
        return age;
    }

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1 一：2 二：3 三：4 四：5 五：6 六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据时间戳判断当前是周几
     *
     * @param
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(long time) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        Date dt = new Date(time);
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static long birthdayToLong(String formatString, String birthdayString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = format.parse(birthdayString);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTimeInMillis();
    }

    public static long birthdayToLong(String in) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YMD);
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = format.parse(in);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTimeInMillis();
    }

    // 判断字符串是不是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    // //判断字符串是否是字母和数字的组合
    // public static boolean isPassword(String str){
    // Pattern pattern =
    // Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,20}$");
    // Matcher ispwd = pattern.matcher(str);
    // if(!ispwd.matches()){
    // return false;
    // }
    // return true;
    // }

    /**
     * 根据时间计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        // if (monthNow <= monthBirth){
        // if (monthNow == monthBirth){
        // if (dayOfMonthNow < dayOfMonthBirth)
        // age--;
        // }
        // else{
        // age--;
        // }
        // }
        return age;
    }

    public static String getTimeString(long time) {
        int ret = 0;
        try {
            ret = isYeaterday(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ret == 0) {
            return "昨天";
        } else if (ret == 1) {// 前天及更早
            return DateUtil.showDate(DateUtil.DATE_FORMAT_MDHM_Z_D, time);
        } else {// 今天
            return DateUtil.showDate(DateUtil.DATE_FORMAT_HM, time);
        }
    }

    private static int isYeaterday(long time) throws ParseException {
        Date newTime = new Date();
        // 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);
        // 昨天 86400000=24*60*60*1000 一天
        if ((today.getTime() - time) > 0
                && (today.getTime() - time) <= 86400000) {
            return 0;
        } else if ((today.getTime() - time) <= 0) { // 至少是今天
            return -1;
        } else { // 至少是前天
            return 1;
        }
    }

    /**
     * 判断time天后的 日期
     *
     * @param time
     * @return
     */
    public static String shij(int time) {// 判断time天后的 日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, time);
        String strDate = cal.get(Calendar.YEAR) + "-"
                + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE)
                + "";
        System.out.println("日期为：" + strDate);
        return strDate;
    }

    /**
     * 根据时间判断月份
     */

    public static String getMonth(long time) {
        String yue = showDate(DATE_FORMAT_M, time);
        System.out.println("yue" + yue);
        if (yue.equals("01")) {
            return "Jan.";
        } else if (yue.equals("02")) {
            return "Feb.";
        } else if (yue.equals("03")) {
            return "Mar.";
        } else if (yue.equals("04")) {
            return "Apr.";
        } else if (yue.equals("05")) {
            return "May.";
        } else if (yue.equals("06")) {
            return "June.";
        } else if (yue.equals("07")) {
            return "July.";
        } else if (yue.equals("08")) {
            return "Aug.";
        } else if (yue.equals("09")) {
            return "Sept.";
        } else if (yue.equals("10")) {
            return "Oct.";
        } else if (yue.equals("11")) {
            return "Nov.";
        } else if (yue.equals("12")) {
            return "Dec.";
        }

        return "";
    }

    /**
     * 根据日期计算属于第几周
     *
     * @param date 格式 yyyy-MM-dd
     * @throws ParseException
     */
    public static int getWeekOfYear(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
            // cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
            cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
            cal.setTime(df.parse(date));
            return cal.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 数据库查出来的时间多了个.0进行格式化
     *
     * @param date
     * @return
     */
    public static String dateFormat(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //创建要显示的日期格式
        //注意了，这里的   MM 在java中代表月份，而  mm 代表分钟， HH 代表24小时制的时间， hh 代表12小时制的时间,很严格的

        Date datex = null;      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
        try {
            datex = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String s = fmt.format(datex);
        return s;
    }

    public static String timeAddTwoHH() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeNow = System.currentTimeMillis();
        long timeLose = System.currentTimeMillis() + 2 * 60 * 60 * 1000;
        String dateNow = sdf.format(new Date(timeNow));
        String loseDate = sdf.format(new Date(timeLose));
        return loseDate;
    }
}
