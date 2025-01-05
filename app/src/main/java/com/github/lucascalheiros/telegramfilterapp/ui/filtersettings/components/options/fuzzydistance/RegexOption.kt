package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.fuzzydistance

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.SettingItem

@Composable
fun FuzzyDistanceOption(distance: Int, onUpdateFuzzyDistance: (Int) -> Unit) {
    var showOptionsMenu by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.fuzzy_distance), { showOptionsMenu = true }) {
        Box {
            Text(distance.toString(), color = MaterialTheme.colorScheme.primary)
            DropdownMenu(
                expanded = showOptionsMenu,
                onDismissRequest = { showOptionsMenu = false },
                modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                (1..5).forEach {
                    DropdownMenuItem(
                        text = { Text(it.toString()) },
                        onClick = {
                            showOptionsMenu = false
                            onUpdateFuzzyDistance(it)
                        },
                    )
                }
            }
        }
    }

}