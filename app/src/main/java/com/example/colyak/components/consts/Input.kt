package com.example.colyak.components.consts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    readOnly: Boolean = false,
    tfValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean,
    backgroundColor: Color = Color(0xFFECECEC),
    keybordType: KeyboardType = KeyboardType.Text,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
    trailingIconClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TextField(
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = ""
                )
            }
        } else {
            null
        },
        trailingIcon = if (trailingIcon != null) {
            {
                IconButton(onClick = trailingIconClick) {
                    Icon(painter = painterResource(id = trailingIcon), contentDescription = "")
                }
            }
        } else {
            null
        },
        value = tfValue,
        readOnly = readOnly,
        textStyle = TextStyle(fontSize = 18.sp),
        onValueChange = onValueChange,
        modifier = modifier // Use the passed modifier here
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(text = label)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation =
        if (isPassword) {
            PasswordVisualTransformation()
        } else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keybordType
        )
    )
}
