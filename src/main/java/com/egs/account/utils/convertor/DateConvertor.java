package com.egs.account.utils.convertor;

import java.time.LocalDate;
import java.sql.Date;

public class DateConvertor {

    public static java.util.Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.valueOf(dateToConvert);
    }
}
