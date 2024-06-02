package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.cards.PdfCard
import com.example.colyak.viewmodel.PDFViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsefulInformationScreen(navController: NavController) {
    val pdfVM: PDFViewModel = viewModel()
    val pdfList by pdfVM.pdfList.collectAsState()
    val loading by pdfVM.loading.collectAsState()
    Log.e("pdfList", pdfList.toString())

    LaunchedEffect(Unit) {
        pdfVM.getAllPdfs()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Faydalı Bilgiler")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { padding ->
            if (loading) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularIndeterminateProgressBar(isDisplay = loading)
                }
            } else {
                LazyColumn(
                    content = {
                        if (pdfList?.isNotEmpty() == true) {
                            items(pdfList!!.size) {
                                val pdf = pdfList!![it]
                                if (pdf != null) {
                                    PdfCard(
                                        topic = pdf.name,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                val pdfJson = Gson().toJson(pdf)
                                                navController.navigate("${Screens.PdfDetailScreen.screen}/$pdfJson")
                                            }
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    )
}