package com.example.colyak.screens

sealed class Screens(
    val screen: String
){
    data object MainScreen: Screens("mainScreen")
    data object Login: Screens("loginScreen")
    data object Register: Screens("registerScreen")
    data object Profile: Screens("profileScreen")
    data object MealDetail: Screens("mealDetail")
    data object ProfileEditScreen: Screens("profileEditScreen")
    data object AddMealScreen: Screens("addMealScreen")
    data object AddReceiptScreen : Screens("addReceiptDetailScreen")
    data object AddReadyFoodScreen : Screens("addReadyFoodDetailScreen")
    data object QuizScreen : Screens("quizScreen")
    data object QuizDetailScreen : Screens("quizDetailScreen")
    data object ReceiptDetailScreen : Screens("receiptDetailScreen")
    data object QuizReportScreen : Screens("quizReportScreen")
    data object CommentReplyScreen : Screens("commentReplyScreen")
    data object VerificationScreen : Screens("verificationScreen")
    data object ForgotPasswordScreen : Screens("passwordForgotScreen")
    data object SuggestionScreen : Screens("suggestionScreen")
    data object UsefulInformationScreen : Screens("usefulInformationScreen")
    data object PdfDetailScreen : Screens("pdfDetailScreen")
    data object BarcodeScreen : Screens("barcodeScreen")
    data object BarcodeDetailScreen : Screens("barcodeDetailScreen")
    data object DateRangePickerScreen : Screens("dateRangePickerScreen")
    data object MealReportScreen : Screens("mealReportScreen")
    data object MealReportDetailScreen : Screens("mealReportDetailScreen")
    data object BolusScreen : Screens("bolusScreen")

}
