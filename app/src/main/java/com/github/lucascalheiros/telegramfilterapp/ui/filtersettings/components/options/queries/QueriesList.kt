package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.queries

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QueriesList(queries: List<String>, onRemoveIndex: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        queries.forEachIndexed { index, value ->
            InputChip(
                modifier = Modifier.padding(4.dp),
                selected = true,
                onClick = {
                    onRemoveIndex(index)
                },
                label = { Text(value) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.remove_query),
                        Modifier.size(InputChipDefaults.AvatarSize)
                    )
                }
            )
        }
    }
}