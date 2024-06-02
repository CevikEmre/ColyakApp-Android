package com.example.colyak.components.functions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.example.colyak.R

@Composable
fun SearchTextField(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    TextField(
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = ""
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = text,
        onValueChange = {
            text = it
            onSearch(it.text)
        },
        placeholder = { Text("Ara") },
        modifier = Modifier.fillMaxWidth()
    )
}