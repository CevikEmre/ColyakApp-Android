package com.example.colyak.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.components.consts.Input

@Composable
fun BolusCard(
    label:String,
    inputText:String,
    description:String,
    icon:Int
){
    var xd = inputText
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
                Input(

                    tfValue = xd,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() }) {
                            xd = newText
                        }
                    },
                    label = label,
                    isPassword = false,
                    keybordType = KeyboardType.Number,
                    modifier = Modifier
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    fontSize = 13.sp,
                )
            }
        }
    }
}