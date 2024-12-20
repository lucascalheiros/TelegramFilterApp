package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.filterdatetime

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.common.datetime.millisToLocalDate
import com.github.lucascalheiros.common.datetime.toMillis
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.SettingItem
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDateTimeOption(dateTime: LocalDateTime, onUpdateDateTime: (LocalDateTime) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        dateTime.toMillis()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = dateTime.hour,
        initialMinute = dateTime.minute,
        is24Hour = true,
    )
    fun dispatchNewDate()  {
        val dateMillis = datePickerState.selectedDateMillis ?: return
        val date = dateMillis.millisToLocalDate()
        val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
        val newLocalDateTime = LocalDateTime.of(date, time)
        onUpdateDateTime(newLocalDateTime)
    }
    SettingItem(stringResource(R.string.filter_after_date), {
        showDatePicker = true
    }) {
        Text(
            dateTime.format(
                DateTimeFormatter.ofLocalizedDateTime(
                    FormatStyle.SHORT
                )
            ), color = MaterialTheme.colorScheme.primary
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                    dispatchNewDate()
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker) {
        TimePickerDialog(
            onCancel = { showTimePicker = false },
            onConfirm = {
                showTimePicker = false
                dispatchNewDate()
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}