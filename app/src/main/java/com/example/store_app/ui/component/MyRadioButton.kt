package com.example.store_app.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyRadioButton(
    option: Int,
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onClickBody: (List<String>, Int) -> Unit
) {
    RadioButton(
        selected = (radioOptions[option] == selectedOption),
        onClick = {
            onOptionSelected(radioOptions[option])
            onClickBody(radioOptions, option)
        }
    )
    Text(
        text = radioOptions[option],
        style = MaterialTheme.typography.body1.merge(),
        modifier = Modifier.padding(start = 8.dp)
    )
}