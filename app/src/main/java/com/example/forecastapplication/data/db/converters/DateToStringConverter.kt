package com.example.forecastapplication.data.db.converters

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object DateToStringConverter {

    @TypeConverter
    @JvmStatic
    fun stringToDate(str: String?) = str?.let{
        LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: LocalDate?) = date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
}