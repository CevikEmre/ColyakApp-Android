package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.cards.ProfileSettingsCard
import com.example.colyak.viewmodel.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    val profileList = listOf(
        SettingsItem(R.drawable.settings, "Settings", ""),
        SettingsItem(
            R.drawable.language_icon,
            "Prefered Language",
            "You can change system language"
        )
    )

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Profile",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W500
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.appBarColor),
                titleContentColor = Color.White
            )
        )
    }, content = { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxSize()
            ) {
           /* ProfileCard(user = user, Modifier.padding(padding)) {
                navController.navigate(Screens.ProfileEditScreen.screen)
            }*/
            LazyColumn(Modifier.padding(horizontal = 5.dp)) {
                items(profileList.count()) {
                    val profile = profileList[it]
                    ProfileSettingsCard(
                        icon = profile.icon,
                        name = profile.name,
                        description = profile.description,
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 6.dp)
                    )
                }

            }
        }


    })


}