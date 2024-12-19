package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun PhoneNumberInput(
    number: String,
    areaCode: String,
    onChangeNumber: (String) -> Unit,
    onChangeAreaCode: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Please input your telegram's existing account phone number:",
            textAlign = TextAlign.Center
        )

        TextField(
            value = number,
            onValueChange = onChangeNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            prefix = {
                PhoneAreaCodeSelector(areaCode, onChangeAreaCode)
            },
            label = {
                Text(stringResource(R.string.phone_number))
            }
        )
    }
}