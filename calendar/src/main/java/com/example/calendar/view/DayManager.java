package com.example.calendar.view;

import android.util.Log;

import com.example.calendar.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 日期的管理类
 * Created by xiaozhu on 2016/8/7.
 */
public class DayManager {
    /**
     * 记录当前的时间
     */
    public static String currentTime;

    /**
     * 当前的日期
     */
    private static int current = -1;
    /**
     * 储存当前的日期
     */
    private static int tempcurrent = -1;
    /**
     *
     */
    static String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    static String[] dayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    /**
     * 设置当前的时间
     *
     * @param currentTime
     */
    public static void setCurrentTime(String currentTime) {
        DayManager.currentTime = currentTime;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return currentTime;
    }


    /**
     * 储存正常天数
     */
    static Set<Integer> normalDays = new HashSet<>();

    /**
     * 添加正常天数
     *
     * @param i
     */
    public static void addNomalDays(int i) {
        normalDays.add(i);
    }

    /**
     * 移除正常的天数
     *
     * @param i
     */
    public static void removeNomalDays(int i) {
        if (normalDays.contains(i)) {
            normalDays.remove(i);
        }
    }

    /**
     * 储存异常天数
     */
    static Set<Integer> abnormalDays = new HashSet<>();

    /**
     * 添加异常天数
     *
     * @param i
     */
    public static void addAbnormalDays(int i) {
        abnormalDays.add(i);
    }

    /**
     * 移除异常的天数
     *
     * @param i
     */
    public static void removeAbnormalDays(int i) {
        if (abnormalDays.contains(i)) {
            abnormalDays.remove(i);
        }
    }

    /**
     * 储存外出天数
     */
    static Set<Integer> outDays = new HashSet<>();

    /**
     * 添加外出天数天数
     *
     * @param i
     */
    public static void addOutDays(int i) {
        outDays.add(i);
    }

    /**
     * 移除外出天数的天数
     *
     * @param i
     */
    public static void removeOutDays(int i) {
        if (outDays.contains(i)) {
            outDays.remove(i);
        }
    }

    /**
     * 储存休息天数
     */
    static Set<Integer> restDays = new HashSet<>();


    public static void setTempcurrent(int tempcurrent) {
        DayManager.tempcurrent = tempcurrent;
    }

    public static int getTempcurrent() {
        return tempcurrent;
    }


    public static void setCurrent(int current) {
        DayManager.current = current;
    }

    private static int select = -1;

    public static void setSelect(int select) {
        DayManager.select = select;
    }

    /**
     * 根据日历对象创建日期集合
     *
     * @param calendar 日历
     * @param width    控件的宽度
     * @param heigh    控件的高度
     * @param
     * @return 返回的天数的集合
     */
    public static List<Day> createDayByCalendar(Calendar calendar, int width, int heigh, boolean drawOtherDay) {
        //初始化休息的天数
        initRestDays(calendar);
        //模拟数据
        imitateData();

        List<Day> days = new ArrayList<>();


        Day day = null;


        int dayWidth = width / 7;
        int dayHeight = heigh / (calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1);
        //添加星期标识，
        for (int i = 0; i < 7; i++) {
            day = new Day(dayWidth, dayHeight);
            //为星期设置位置，为第0行，
            day.location_x = i;
            day.location_y = 0;
            day.text = weeks[i];
            //设置日期颜色
            day.textClor = 0xFF699CF0;
            days.add(day);

        }
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeekCount = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //添加上一个月的天数
        if (drawOtherDay) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);

            int preCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < firstWeekCount; i++) {
                day = new Day(dayWidth, dayHeight);
                day.text = dayArray[preCount - firstWeekCount + i];
                day.location_x = i;
                day.location_y = 1;
                day.textClor=0xffaaaaaa;
                day.isCurrent=false;
                day.dateText=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+day.text;
                days.add(day);
            }

            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }


        //生成每一天的对象，其中第i次创建的是第i+1天
        for (int i = 0; i < count; i++) {
            day = new Day(dayWidth, dayHeight);
            day.text = dayArray[i];

            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            //设置每个天数的位置
            day.location_y = calendar.get(Calendar.WEEK_OF_MONTH);

            day.location_x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            day.dateText=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+day.text;

            //设置日期选择状态
            if (i == current - 1) {
                day.backgroundStyle = 3;
                day.textClor = 0xFF4384ED;

            } else if (i == select - 1) {
                day.backgroundStyle = 2;

                day.textClor = 0xFFFAFBFE;

            } else {
                day.backgroundStyle = 1;
                day.textClor = 0xFF8696A5;
            }
            //设置工作状态
            if (restDays.contains(1 + i)) {
                day.workState = 0;
            } else if (abnormalDays.contains(i + 1)) {

                day.workState = 2;
            } else if (outDays.contains(i + 1)) {
                day.workState = 3;
            } else {
                day.workState = 1;
            }
            days.add(day);
        }

        //添加下一个月的天数
        int lastCount = calendar.get(Calendar.DAY_OF_WEEK);


        for (int i = 0; i < 7 - lastCount; i++) {
            day = new Day(dayWidth, dayHeight);
            day.text = dayArray[i];

            //设置每个天数的位置
            day.location_y = calendar.get(Calendar.WEEK_OF_MONTH);

            day.location_x = lastCount+i;
            day.isCurrent=false;
            day.textClor=0xffaaaaaa;
            day.dateText=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-"+day.text;
            days.add(day);
        }

        return days;
    }

    /**
     * 模拟数据
     */
    private static void imitateData() {
        abnormalDays.add(2);
        abnormalDays.add(11);
        abnormalDays.add(16);
        abnormalDays.add(17);
        abnormalDays.add(23);

        outDays.add(8);
        outDays.add(9);
        outDays.add(18);
        outDays.add(22);

    }

    /**
     * 初始化休息的天数  计算休息的天数
     *
     * @param calendar
     */
    private static void initRestDays(Calendar calendar) {
        int total = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < total; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                restDays.add(i + 1);
            }
        }
    }

}
