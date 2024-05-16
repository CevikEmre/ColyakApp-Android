

package com.example.colyak.components.consts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomizeButton(
    onClick: () -> Unit,
    buttonText: String,
    backgroundColor: Color,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    icon: Int? = null
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(60.dp),
        onClick = onClick, shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = backgroundColor,
        ),
        enabled = enabled
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    Modifier.padding(horizontal = 5.dp)
                )
            } else null
            Text(text = buttonText, fontSize = 24.sp, fontWeight = FontWeight.W700)
        }
    }
}