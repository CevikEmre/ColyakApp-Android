package com.example.colyak.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.colyak.barcode.BarcodeScreen
import com.example.colyak.model.BolusReport
import com.example.colyak.model.CommentRepliesResponse
import com.example.colyak.model.MealDetail
import com.example.colyak.model.PDFResponse
import com.example.colyak.model.Quiz
import com.example.colyak.model.Receipt
import com.example.colyak.viewmodel.answerList
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PageNavigations() {
    val navController = rememberNavController()
    NavControllerHolder.navController = navController
    NavHost(
        navController = navController,
        startDestination = Screens.Login.screen,
    ) {
        composable(Screens.Login.screen) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Register.screen) {
            RegisterScreen(navController = navController)
        }
        composable(Screens.MainScreen.screen) {
            MainScreen(navController = navController)
        }
        composable("${Screens.ReceiptDetailScreen.screen}/{receipt}") {
            navArgument("receipt") {
                type = NavType.StringType
            }
            val receiptJson = it.arguments?.getString("receipt")
            val receipt = Gson().fromJson(receiptJson, Receipt::class.java)
            ReceiptDetailScreen(receipt = receipt, navController = navController)
        }

        composable("${Screens.MealDetail.screen}/{meal}") {
            navArgument("meal") {
                type = NavType.StringType
            }
            val mealJson = it.arguments?.getString("meal")
            val meal = Gson().fromJson(mealJson, MealDetail::class.java)
            MealDetailScreen(mealDetail = meal, navController = navController)
        }
        composable("${Screens.AddMealScreen.screen}/{mealName}") {
            val mealName = it.arguments?.getString("mealName") ?: ""
            AddMealScreen(mealName = mealName, navController = navController)
        }
        composable(Screens.QuizScreen.screen) {
            QuizScreen(navController)
        }
        composable("${Screens.QuizDetailScreen.screen}/{quiz}") {
            navArgument("quiz") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("quiz")
            val quiz = Gson().fromJson(json, Quiz::class.java)
            QuizDetailScreen(quiz, navController)
        }

        composable(Screens.QuizReportScreen.screen) {
            QuizReportScreen(quizReportList = answerList, navController = navController)
        }
        composable("${Screens.CommentReplyScreen.screen}/{comment}") {
            navArgument("comment") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("comment")
            val comment = Gson().fromJson(json, CommentRepliesResponse::class.java)
            CommentReplyScreen(comment = comment, navController = navController)
        }
        composable(Screens.VerificationScreen.screen) {
            VerificationScreen(navController)
        }
        composable(Screens.ForgotPasswordScreen.screen) {
            ForgotPasswordScreen(navController)
        }
        composable(Screens.SuggestionScreen.screen) {
            SuggestionScreen(navController)
        }
        composable(Screens.UsefulInformationScreen.screen) {
            UsefulInformationScreen(navController)
        }
        composable("${Screens.PdfDetailScreen.screen}/{pdf}") {
            navArgument("pdf") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("pdf")
            val pdf = Gson().fromJson(json, PDFResponse::class.java)
            PdfDetailScreen(pdfResponse = pdf, navController = navController)
        }
        composable(Screens.BarcodeScreen.screen) {
            BarcodeScreen(navController)
        }
        composable(Screens.BarcodeDetailScreen.screen) {
            BarcodeDetailScreen(navController = navController)
        }
        composable(Screens.DateRangePickerScreen.screen) {
            DateRangePickerScreen(navController = navController)
        }
        composable(Screens.MealReportScreen.screen) {
            MealReportScreen(navController = navController)
        }
        composable("${Screens.MealReportDetailScreen.screen}/{meal}") {
            navArgument("meal") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("meal")
            val meal = Gson().fromJson(json, BolusReport::class.java)
            MealReportDetailScreen(bolusReport = meal, navController = navController)
        }
        composable(Screens.BolusScreen.screen) {
            BolusScreen(navController)
        }
    }
}
