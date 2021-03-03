package com.xz.xlogin.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JodaUtils {

  /**
   * 转为日期标准ISO8601格式
   *
   * @param date YYYYMMDD
   * @return YYYY-MM-DD
   */
  public static String toISODate(int date) {
    int day = date % 100;
    int month = date / 100 % 100;
    int year = date / 10000;
    return year + "-" + month + "-" + day;
  }


  /**
   * 转为日期标准ISO8601格式
   *
   * @param date YYYYMMDD
   * @param reverse 日期是否是反转格式,如: 08/03/2018, 08-03-2018,默认是正常格式,如20180308 2018/03/08
   * @return YYYY-MM-DD
   */
	public static String toISODate(String date, Boolean reverse){
	    if(date.length() < 8){
	      throw new RuntimeException("date formation error:" + date);
	    }
	    if(reverse){
	      date = StringUtil.reverse(date);
	    }
	    char sep = date.charAt(4);
	    if(date.length() == 8 && (sep >= '0' && sep <= '9')){
	      return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
	    }else if(sep < '0' || sep > '9'){
	      int  nextPos = date.indexOf(sep, 5);
	      if(nextPos < 0 || nextPos + 1 > date.length()){
	        throw new RuntimeException("date formation error:" + date);
	      }
	      String monthStr =date.substring(5, nextPos);
	      String dayStr = date.substring(nextPos + 1);
	      int month = Integer.valueOf(monthStr);
	      int day = Integer.valueOf(dayStr);
	      if(monthStr.isEmpty()
	          || dayStr.isEmpty()
	          || month < 1
	          || month > 12
	          || day < 1
	          || day > 31){
	        throw new RuntimeException("date formation error:" + date);
	      }
	      return date.replace(sep, '-');
	    }else
	      throw new RuntimeException("date formation error:" + date);
	  }


  /**
   * 输入日期是星期几 如:20180308 -> 4  即周四
   *
   * @param date YYYYMMDD
   * @return 周几，值一定落在集合{1, 2, 3, 4, 5, 6, 7}, eg. 1 -> Monday
   */
  public static int dayOfWeek(int date) {
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    map.put(1, 0);
    map.put(2, 3);
    map.put(3, 3);
    map.put(4, 6);
    map.put(5, 1);
    map.put(6, 4);
    map.put(7, 6);
    map.put(8, 2);
    map.put(9, 5);
    map.put(10, 0);
    map.put(11, 3);
    map.put(12, 5);
    // Map(1 -> 0, 2 -> 3, 3 -> 3, 4 -> 6, 5 -> 1, 6 -> 4, 7 -> 6, 8 -> 2, 9 -> 5, 10 -> 0, 11 -> 3, 12 -> 5)
    int year = date / 10000 % 100;
    int month = map.get(date / 100 % 100);
    int day = date % 100;
    int yearNum = year / 4;
    int N = (6 + year + yearNum + month + day) % 7;
    if (0 == N) {
      return 7;
    } else {
      return N;
    }
  }

  /**
   * 返回上delta个周的自然周的第一天和最后一天 如: 
   * (20180308,-2)-> (20180219,20180225) 
   * (20180308,-1)-> (20180226,20180304) 
   * (20180308,0) -> (20180305,20180311) 
   * (20180308,1) -> (20180312,20180318)
   * (20180308,2) -> (20180319,20180325)
   *
   * @param date YYYYMMDD
   * @param delta 上n周,默认n为1,即上周
   * @return 上个自然周周一、周日的日期(YYYYMMDD)
   */
  public static int[] getDeltaNaturalWeek(int date, int delta) {
    DateTime day = new DateTime(toISODate(date));
    int Monday = Integer
        .parseInt(day.plusWeeks(delta).minusDays(day.getDayOfWeek() - 1).toString("yyyyMMdd"));
    int Sunday = Integer
        .parseInt(day.plusWeeks(delta + 1).minusDays(day.getDayOfWeek()).toString("yyyyMMdd"));
    int[] arr = {Monday, Sunday};
    return arr;
  }

  /**
   * 返回上delta个月的自然月的第一天和最后一天 如: 
   * (20180308,-2)) -> (20180101,20180131) 
   * (20180308,-1) ->  (20180201,20180228) 
   * (20180308,0)  -> (20180301,20180331) 
   * (20180308,1)  -> (20180401,20180430)
   * (20180308,2)  -> (20180501,20180531)
   *
   * @param date YYYYMMDD
   * @param delta 上delta个月,默认delta为1,即上个月
   * @return 上delta个自然月第一天、最后一天的日期(YYYYMMDD)
   */
  public static int[] getDeltaNaturalMonth(int date, int delta) {
    DateTime day = new DateTime(toISODate(date)).plusMonths(delta);
    int firstDay = Integer.parseInt(day.toString("yyyyMM01"));
    int lastDay = Integer
        .parseInt(
            new DateTime(toISODate(firstDay)).plusMonths(1).minusDays(1).toString("yyyyMMdd"));
    int[] arr = {firstDay, lastDay};
    return arr;
  }


  /**
   * 返回上delta个周的自然周的第一天和最后一天 如: 
   * (20180308,-2)-> (20180219,20180304) 
   * (20180308,-1)-> (20180226,20180304) 
   * (20180308,0) -> (20180305,20180311) 
   * (20180308,1) -> (20180312,20180318)
   * (20180308,2) -> (20180312,20180325)
   *
   * @param date YYYYMMDD
   * @param delta 上n周,默认n为1,即上周
   * @return 上个自然周周一、周日的日期(YYYYMMDD)
   */
  public static int[] getDeltaWeekRange(int date, int delta) {
    DateTime day = new DateTime(toISODate(date));
    if (delta >= 0) {
      int incremental = 1;
      if (delta == 0) {
        incremental = 0;
      }
      int Monday = Integer.parseInt(
          day.plusWeeks(incremental).minusDays(day.getDayOfWeek() - 1).toString("yyyyMMdd"));
      int Sunday = Integer
          .parseInt(day.plusWeeks(delta + 1).minusDays(day.getDayOfWeek()).toString("yyyyMMdd"));
      int[] arr = {Monday, Sunday};
      return arr;
    } else {
      int Monday = Integer
          .parseInt(day.plusWeeks(delta).minusDays(day.getDayOfWeek() - 1).toString("yyyyMMdd"));
      int Sunday = Integer.parseInt(day.minusDays(day.getDayOfWeek()).toString("yyyyMMdd"));
      int[] arr = {Monday, Sunday};
      return arr;
    }
  }


  /**
   * 返回上delta个月的自然月的第一天和最后一天 如: 
   * (20180308,-2) -> (20180101,20180228) 
   * (20180308,-1) -> (20180201,20180228) 
   * (20180308,0)  -> (20180301,20180331) 
   * (20180308,1)  -> (20180401,20180430)
   * (20180308,2)  -> (20180401,20180531)
   *
   * @param date YYYYMMDD
   * @param delta 上delta个月,默认delta为1,即上个月
   * @return 上delta个自然月第一天、最后一天的日期(YYYYMMDD)
   */
  public static int[] getDeltaMonthRange(int date, int delta) {
    DateTime day = new DateTime(toISODate(date));
    if (delta >= 0) {
      int incremental = 1;
      if (delta == 0) {
        incremental = 0;
      }
      int firstDay = Integer.parseInt(day.plusMonths(incremental).toString("yyyyMM01"));
      int lastDay = Integer
          .parseInt(day.plusMonths(delta + 1).minusDays(day.getDayOfMonth()).toString("yyyyMMdd"));
      int[] arr = {firstDay, lastDay};
      return arr;
    } else {
      int firstDay = Integer.parseInt(day.plusMonths(delta).toString("yyyyMM01"));
      int lastDay = Integer.parseInt(day.minusDays(day.getDayOfMonth()).toString("yyyyMMdd"));
      int[] arr = {firstDay, lastDay};
      return arr;
    }
  }

  /**
   * 返回上delta个月的月份形式 如:20180308,delta=1 ->  201802
   *
   * @param date YYYYMMDD
   * @param delta 上几个月,默认n=1,即上一个月
   * @return YYYYMM
   */
  public static int getDeltaMonthCut(int date, int delta) {
    DateTime day = new DateTime(toISODate(date));
    return Integer.parseInt(day.plusMonths(delta).toString("yyyyMM"));
  }

  /**
   * 返回具体日期，如
   * （20180308，-1）-> 20180307,
   * （20180308，0）-> 20180308,
   * （20180308，1）-> 20180309,
   *
   * @param date YYYYMMDD
   * @param delta 间隔的天数
   * @return YYYYMMDD
   */
  public static int getDeltaDay(int date, int delta) {
    DateTime day = new DateTime(toISODate(date));
    return Integer.parseInt(day.plusDays(delta).toString("yyyyMMdd"));
  }


  /**
   *
   * @param delta 间隔天数,支持正负
   * @return  YYYYMMDD
   */
  public static int getDeltaDay(int delta) {
    DateTime startTime = new DateTime();
    return Integer.parseInt(startTime.plusDays(delta).toString("yyyyMMdd"));
  }


  /**
   * 从开始日期和结合日期获取日期列表
   *
   * @param startDay 开始日期
   * @param endDay 结束日期
   * @return 日期列表(字符串类型)
   */
  public static String[] getRangeDays(String startDay, String endDay) {
    DateTime startTime = new DateTime(startDay);
    DateTime endTime = new DateTime(endDay);
    int numberOfDays = Days.daysBetween(startTime, endTime).getDays();
    String[] arr = new String[numberOfDays + 1];
    for (int i = 0; i <= numberOfDays; i++) {
      arr[i] = startTime.plusDays(i).toString("yyyyMMdd");
    }
    return arr;
  }

  /**
   * 从开始日期和结束日期获取日期列表
   *
   * @return 日期列表(整数类型)
   */
  public static int[] getRangeDays(int startDay, int endDay) {
    if (startDay > endDay) {
      int tmp = startDay;
      startDay = endDay;
      endDay = tmp;
    }
    DateTime startTime = new DateTime(toISODate(startDay));
    DateTime endTime = new DateTime(toISODate(endDay));
    int numberOfDays = Days.daysBetween(startTime, endTime).getDays();
    int[] arr = new int[numberOfDays + 1];
    for (int i = 0; i <= numberOfDays; i++) {
      arr[i] = Integer.parseInt(startTime.plusDays(i).toString("yyyyMMdd"));
    }
    return arr;
  }


  /**
   * 获取制定月份的所有日期
   *
   * @param month 支持 YYYYMM 或 YYYYMMDD
   * @return [20180201, 20180202, ..., 201802028] 从小到大排序
   */
  public static int[] getDaysOfMonth(int month) {
    int start;
    if (month < 1000000) {
      start = month * 100 + 1;
    } else {
      start = month / 100 * 100 + 1;
    }
    DateTime startDay = new DateTime(toISODate(start));
    DateTime endDay = startDay.plusMonths(1).minusDays(1);
    return getRangeDays(Integer.parseInt(startDay.toString("yyyyMMdd")),
        Integer.parseInt(endDay.toString("yyyyMMdd")));
  }

  /**
   * 从开始日期和结束日期获取月份列表
   *
   * @param start YYYYMMDD
   * @param end YYYYMMDD
   * @return [201712, 201801, 201802]
   */
  public static List<Integer> getRangeMonths(int start, int end) {
    if (start > end) {
      int tmp = start;
      start = end;
      end = tmp;
    }
    DateTime startDay = new DateTime(toISODate(start));
    DateTime endDay = new DateTime(toISODate(end));
    List<Integer> monthList = new ArrayList<>();
    int index = 0;
    int monthStr = Integer.parseInt(startDay.plusMonths(index).toString("yyyyMM"));
    int endMonth = Integer.parseInt(endDay.toString("yyyyMM"));
    while (monthStr <= endMonth) {
      index += 1;
      monthList.add(monthStr);
      monthStr = Integer.parseInt(startDay.plusMonths(index).toString("yyyyMM"));
    }
    return monthList;
  }


  /**
   * 查看两个日期相差多少天 - 支持负数， eg
   * (20170101,20170131,"day") -> 30,
   * (20170101,20170131,"month") -> 0,
   * (20170101,20170131,"year")->0
   */
  public static int getDelta(int startDay, int endDay) {
    DateTime startTime = new DateTime(toISODate(startDay));
    DateTime endTime = new DateTime(toISODate(endDay));
    return Days.daysBetween(startTime, endTime).getDays();
  }
  
  public static int getToday() {
    return Integer.parseInt(new DateTime().toString("yyyyMMdd"));
  }
}
