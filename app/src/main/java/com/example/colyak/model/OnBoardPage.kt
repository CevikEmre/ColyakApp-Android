package com.example.colyak.model

import androidx.annotation.DrawableRes
import com.example.colyak.R

data class OnBoardPage(
    val title: String,

    val description: String,

    @DrawableRes
    val image:Int
)
val onBoardPages = listOf(
    OnBoardPage(
        title = "Başlarken",
        description = "BLA BLA BLA",
        image = R.drawable.colyak
    ),
    OnBoardPage(
        title = "ZART",
        description = "BLA BLA BLA",
        image = R.drawable.target
    ),
    OnBoardPage(
        title = "ZORT",
        description = "BLA BLA BLA",
        image = R.drawable.arrow_back
    ),
    OnBoardPage(
        title = "AYAK",
        description = "BLA BLA BLA",
        image = R.drawable.colyak
    )
)
