package com.txt.mongoredis.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    public static final String UTC_NATIONAL = "UTC";
    public static final String UTC_VIETNAM = "UTC+7";

    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DDMMYYYY = "ddMMyyyy";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";


    public static String formatDate(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.format(date);
        } catch (Exception e) {
            log.error("Error formatDate:" + e);
        }
        return null;
    }

    public static String formatDate(Object object, String format) {
        try {
            if (object == null) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(object);
        } catch (Exception e) {
            log.error("Error format from date object:" + e.getMessage());
        }
        return null;
    }

    public static Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.parse(dateStr);
        } catch (Exception e) {
            log.error("parseDate" + e);
        }
        return null;
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dateStr);
        } catch (Exception e) {
            log.error("parseDate" + e);
        }
        return null;
    }

    public static LocalDateTime parseDateTimeUTC(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            Instant instant = Instant.parse(dateStr);
            LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
            return result;
        } catch (Exception e) {
            log.error("parseDateTimeUTC" + e);
        }
        return null;
    }

    public static LocalDateTime parseDateTimeUTC_VN(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            Instant instant = Instant.parse(dateStr);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(DateUtil.UTC_VIETNAM));
            return zonedDateTime.toLocalDateTime();
        } catch (Exception e) {
            log.debug("parseDateTimeUTC_VN encounter an error {} with {}", e.getMessage(), dateStr);
        }
        return null;
    }

    public static Date parseDateUTC_VN(String dateStr) {
        try {
            LocalDateTime localDateTime = parseDateTimeUTC_VN(dateStr);
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.debug("parseDateUTC_VN encounter an error {} with {}", e.getMessage(), dateStr);
        }

        return null;
    }

    public static String parseToString(Date date, String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
            log.error("parseToString:" + e);
        }
        return null;
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            //convert String to LocalDate
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            log.error("parseLocalDate:" + e);
        }
        return null;
    }

    public static LocalDate parseLocalDate(Object dateObj) {
        if (ObjectUtils.isEmpty(dateObj)) return null;
        try {
            Date date = (Date) dateObj;
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            return localDate;
        } catch (Exception e) {
            log.error("parseLocalDate:" + e);
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        if (StringUtils.isBlank(dateTimeStr)) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            //convert String to LocalDateTime
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            log.error("parseLocalDateTime:" + e);
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        if (StringUtils.isBlank(dateTimeStr)) return null;
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            log.error("parseLocalDateTime:" + e);
        }
        return null;
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
            return dtf.format(date);
        } catch (Exception e) {
            log.error("formatLocalDate:" + e);
        }
        return null;
    }

    public static Date addDate(Date date, int numberOfDates) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + numberOfDates);
        return cal.getTime();
    }

    public static Instant getCurrentTimeISOInstant() {
        ZoneId zoneId = ZoneId.of(DateUtil.UTC_NATIONAL);
        LocalDateTime localDateTime = LocalDateTime.now(zoneId).withNano(0);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    public static Date getCurrentDateWithTimeZone(String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        LocalDateTime localDateTime = LocalDateTime.now(zoneId).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public static Date getDateStartDay(LocalDateTime dateTime) {
        LocalDateTime ldt = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return date;
    }

    public static Date getDateEndDay(LocalDateTime dateTime) {
        LocalDateTime ldt = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return date;
    }

    public static String formatStr(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formatDateTime = localDateTime.format(formatter);
        return formatDateTime;
    }

    public static LocalDateTime fromDateToIso(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld = LocalDate.parse(dateString, formatter);
        return LocalDateTime.of(ld, LocalDateTime.MIN.toLocalTime());
    }

    public static LocalDateTime toDateToIso(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld = LocalDate.parse(dateString, formatter);
        LocalDateTime localDateTime = LocalDateTime.of(ld, LocalDateTime.MAX.toLocalTime());
        return localDateTime;
    }

    public static LocalDateTime getDateTimeNowUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime getDateTimeNowUTC_VN() {
        OffsetDateTime offsetDateTime = Instant.now().atOffset(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeVN = offsetDateTime.atZoneSameInstant(ZoneId.of(UTC_VIETNAM));
        return zonedDateTimeVN.toLocalDateTime();
    }
}
