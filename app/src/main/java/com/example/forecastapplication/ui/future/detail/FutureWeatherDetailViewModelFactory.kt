package com.example.forecastapplication.ui.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.threeten.bp.LocalDate

class FutureWeatherDetailViewModelFactory(
    private val detailDate: LocalDate
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureWeatherDetailViewModel(detailDate) as T
    }
}