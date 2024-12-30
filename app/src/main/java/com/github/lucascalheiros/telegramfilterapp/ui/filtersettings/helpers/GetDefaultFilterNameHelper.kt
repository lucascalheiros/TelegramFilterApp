package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.helpers

import android.content.Context
import com.github.lucascalheiros.telegramfilterapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetDefaultFilterNameHelper @Inject constructor(@ApplicationContext private val context: Context)  {

    operator fun invoke(): String {
        return context.resources.getString(R.string.new_filter)
    }

}