package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun PhoneNumberInput(
    number: String,
    region: String,
    onChangeNumber: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.please_input_your_telegram_s_existing_account_phone_number),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )

        TextField(
            value = number,
            onValueChange = onChangeNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            prefix = {
                Text("+")
            },
            label = {
                Text(stringResource(R.string.phone_number))
            },
            supportingText = {
                if (region.isNotEmpty()) {
                    Text(stringResource(R.string.region, region))
                }
            }
        )
    }
}