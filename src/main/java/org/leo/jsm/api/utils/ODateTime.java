package org.leo.jsm.api.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期处理类.
 */
public class ODateTime implements Comparable<ODateTime>, Serializable {

    /**
     * 序列化默认参数
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日期格式化类型(YYYYMMDD)
     */
    public static final int DATE_TYPE_YMD = 1;

    /**
     * 日期格式化类型(YYYY/MM/DD)
     */
    public static final int DATE_TYPE_YMD_SLASH = 2;

    /**
     * 日期格式化类型(YYYY-MM-DD)
     */
    public static final int DATE_TYPE_YMD_MINUS = 3;

    /**
     * 日期格式化类型(YYYYMM)
     */
    public static final int DATE_TYPE_YM = 4;

    /**
     * 日期格式化类型(YYYY/MM)
     */
    public static final int DATE_TYPE_YM_SLASH = 5;

    /**
     * 日期格式化类型(YYYY-MM)
     */
    public static final int DATE_TYPE_YM_MINUS = 6;

    /**
     * 日期格式化(yyyyMMdd)
     */
    private final String dateFormatYmd = "yyyyMMdd";

    /**
     * 日期格式化(yyyy/MM/dd)
     */
    private final String dateFormatYmdSlash = "yyyy/MM/dd";

    /**
     * 日期格式化(yyyy-MM-dd)
     */
    private final String dateFormatYmdMinus = "yyyy-MM-dd";

    /**
     * 日期格式化(yyyy-MM-dd)
     */
    public static final String dateFormatYmdMinusFull = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 日期格式化(yyyy年MM月dd日)
     */
    private final String dateFormatYmdCn = "yyyy年MM月dd日";

    /**
     * 日期格式化(HH:mm)
     */
    private final String dateFormatH24m = "HH:mm";

    /**
     * 日历类
     */
    private Calendar calendar;

    /**
     * 构造函数。
     *
     * @param datetime 日期
     */
    public ODateTime(ODateTime datetime) {
        this.calendar = ((Calendar) datetime.calendar.clone());
    }

    /**
     * 构造函数。
     *
     * @param date 日期
     */
    public ODateTime(Date date) {
        if (date == null) {
            return;
        }
        this.calendar = new GregorianCalendar();
        this.calendar.setTime(date);
    }

    /**
     * 构造函数。
     *
     * @param strDate  日期(6位、8位)
     * @param dateType 日期类型
     * @throws Exception 系统异常
     */
    public ODateTime(String strDate, int dateType) throws Exception {
        SimpleDateFormat format;
        Date date;
        String wkDate = strDate;
        if ("".equals(strDate) || strDate == null) {
            return;
        }
        if (DATE_TYPE_YMD == dateType || DATE_TYPE_YM == dateType) {
            format = new SimpleDateFormat(dateFormatYmd);
        } else if (DATE_TYPE_YMD_SLASH == dateType || DATE_TYPE_YM_SLASH == dateType) {
            format = new SimpleDateFormat(dateFormatYmdSlash);
        } else if (DATE_TYPE_YMD_MINUS == dateType || DATE_TYPE_YM_MINUS == dateType) {
            format = new SimpleDateFormat(dateFormatYmdMinus);
        } else {
            throw new Exception("日期格式化错误。");
        }
        try {
            if (DATE_TYPE_YM == dateType) {
                wkDate = wkDate + "01";
            } else if (DATE_TYPE_YM_SLASH == dateType) {
                wkDate = wkDate + "/01";
            } else if (DATE_TYPE_YM_MINUS == dateType) {
                wkDate = wkDate + "-01";
            }
            date = format.parse(wkDate);
        } catch (ParseException e) {
            throw new Exception(e);
        }
        this.calendar = new GregorianCalendar();
        this.calendar.setTime(date);
    }

    /**
     * 取得年龄。
     *
     * @param datetime 日期
     * @return 年龄
     */
    public Integer getAge(ODateTime datetime) {
        Integer age = null;
        if (this.getYear() == datetime.getYear()) {
            if (Integer.parseInt(this.getMonth()) <= Integer.parseInt(datetime.getMonth())
                    && Integer.parseInt(this.getDay()) <= Integer.parseInt(datetime.getDay())) {
                age = 0;
            }
        } else if (Integer.parseInt(this.getMonth()) < Integer.parseInt(datetime.getMonth())
                || (Integer.parseInt(this.getMonth()) == Integer.parseInt(datetime.getMonth()) && Integer.parseInt(this.getDay()) <= Integer
                .parseInt(datetime.getDay()))) {
            age = Integer.parseInt(datetime.getYear()) - Integer.parseInt(this.getYear());
        } else {
            age = Integer.parseInt(datetime.getYear()) - Integer.parseInt(this.getYear()) - 1;
        }
        return age;
    }

    /**
     * 取得日期的列表。
     *
     * @param datetime 日期
     * @return 日付列表(YYYYMMDD)
     */
    public String[] getArrDays(ODateTime datetime) {
        List<String> result = new ArrayList<String>();
        ODateTime wkDate = new ODateTime(this.addDays(1));
        while (wkDate.compareTo(datetime) <= 0) {
            result.add(wkDate.getYmd());
            wkDate = wkDate.addDays(1);
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * 取得日数。
     *
     * @param datetime 日期
     * @return 日数
     */
    public long getDays(ODateTime datetime) {
        long days = 0;
        days = (datetime.getTimeInMillis() - this.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 取得月末日。
     *
     * @return 月末日
     * @throws Exception 未知异常
     */
    public String getMonthLastDateDay() throws Exception {
        return this.getMonthLastDate().getDay();
    }

    /**
     * 取得月末日期。
     *
     * @return 月末日期
     * @throws Exception 未知异常
     */
    public ODateTime getMonthLastDate() throws Exception {
        try {
            if (calendar == null) {
                return null;
            }
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmd);
            Date date = format.parse(this.getYm() + "01");
            Calendar wkCalendar = new GregorianCalendar();
            wkCalendar.setTime(date);
            wkCalendar.add(Calendar.MONTH, 1);
            wkCalendar.add(Calendar.DAY_OF_MONTH, -1);
            return new ODateTime(wkCalendar.getTime());
        } catch (ParseException e) {
            throw new Exception(e);
        }
    }

    /**
     * 取得时间。
     *
     * @return 时间（long）
     */
    public long getTimeInMillis() {
        if (calendar == null) {
            return 0L;
        } else {
            return this.calendar.getTimeInMillis();
        }
    }

    /**
     * 取得日期。
     *
     * @return 日付(Date)
     */
    public Date getDate() {
        return this.calendar.getTime();
    }

    /**
     * 取得年。
     *
     * @return 年
     */
    public String getYear() {
        if (calendar == null) {
            return "0";
        } else {
            return String.valueOf(this.calendar.get(Calendar.YEAR));
        }
    }

    /**
     * 取得月。
     *
     * @return 月
     */
    public String getMonth() {
        if (calendar == null) {
            return "0";
        } else {
            int month = this.calendar.get(Calendar.MONTH) + 1;
            if (month < 10) {
                return "0" + String.valueOf(month);
            } else {
                return String.valueOf(month);
            }
        }
    }

    /**
     * 取得日。
     *
     * @return 日
     */
    public String getDay() {
        if (calendar == null) {
            return "0";
        } else {
            int day = this.calendar.get(Calendar.DAY_OF_MONTH);
            if (day < 10) {
                return "0" + String.valueOf(day);
            } else {
                return String.valueOf(day);
            }
        }
    }

    /**
     * 取得日期（yyyyMMdd）。
     *
     * @return 日期（yyyyMMdd）
     */
    public String getYmd() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmd);
            return format.format(this.calendar.getTime());
        }
    }

    /**
     * 取得日期（yyyy/MM/dd）。
     *
     * @return 日期（yyyy/MM/dd）
     */
    public String getYmdSlash() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmdSlash);
            return format.format(this.calendar.getTime());
        }
    }

    /**
     * 取得日期（yyyy-MM-dd）。
     *
     * @return 日期（yyyy-MM-dd）
     */
    public String getYmdMinus() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmdMinus);
            return format.format(this.calendar.getTime());
        }
    }

    /**
     * 取得日期（yyyyMM）。
     *
     * @return 日期（yyyyMM）
     */
    public String getYm() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmd);
            return format.format(this.calendar.getTime()).substring(0, 6);
        }
    }

    /**
     * 取得日期（yyyy年MM月）。
     *
     * @return 日期（yyyy年MM月）
     */
    public String getYmCn() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmdCn);
            return format.format(this.calendar.getTime()).substring(0, 8);
        }
    }

    /**
     * 取得自定义格式日期。
     *
     * @param 日期格式
     * @return 日期
     */
    public String getSelfFormat(String fmt) {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(fmt);
            return format.format(this.calendar.getTime());
        }
    }

    /**
     * 取得日期（yyyy/MM）。
     *
     * @return 日期（yyyy/MM）
     */
    public String getYmSlash() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmdSlash);
            return format.format(this.calendar.getTime()).substring(0, 7);
        }
    }

    /**
     * 取得日期（yyyy-MM）。
     *
     * @return 日期（yyyy-MM）
     */
    public String getYmMinus() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatYmdMinus);
            return format.format(this.calendar.getTime()).substring(0, 7);
        }
    }

    /**
     * 取得24进制时间（hh:mm）。
     *
     * @return 24进制时间（hh:mm）
     */
    public String getH24m() {
        if (calendar == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatH24m);
            return format.format(this.calendar.getTime());
        }
    }

    /**
     * 年的加减计算。
     *
     * @param years 年数
     * @return 日付
     */
    public ODateTime addYears(int years) {
        if (calendar == null) {
            return null;
        }
        ODateTime datetime = new ODateTime(this);
        datetime.calendar.add(Calendar.YEAR, years);
        return datetime;
    }

    /**
     * 月的加减计算。
     *
     * @param months 月数
     * @return 日付
     */
    public ODateTime addMonths(int months) {
        if (calendar == null) {
            return null;
        }
        ODateTime datetime = new ODateTime(this);
        datetime.calendar.add(Calendar.MONTH, months);
        return datetime;
    }

    /**
     * 日的加减计算。
     *
     * @param days 日数
     * @return 日付
     */
    public ODateTime addDays(int days) {
        if (calendar == null) {
            return null;
        }
        ODateTime datetime = new ODateTime(this);
        datetime.calendar.add(Calendar.DAY_OF_MONTH, days);
        return datetime;
    }

    /**
     * 取得时间(Timestamp型)。
     *
     * @return 时间
     */
    public Timestamp getTimestamp() {
        if (calendar == null) {
            return null;
        }
        Date date = this.calendar.getTime();
        return new Timestamp(date.getTime());
    }

    /**
     * 比较日期大小。
     *
     * @param datetime 日期
     * @return 比较结果
     */
    @Override
    public int compareTo(ODateTime datetime) {
        if (calendar == null && datetime == null) {
            return 0;
        }
        if (calendar == null && datetime.calendar == null) {
            return 0;
        }
        return this.calendar.compareTo(datetime.calendar);
    }
}
