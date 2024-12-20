package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSettingsTopBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.filter_settings))
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}