package com.example.forecastapplication.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//в аннотации указываем параметр индекс для того, чтобы быстрее производить поиск по бд
@Entity(tableName = "future_weather", indices = [Index(value=["date"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: Int,
    @Embedded
    val day: Day
)