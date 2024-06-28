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
        title = "Hoşgeldiniz",
        description = "Uygulamamızın Ana ekranı karşınızda ! ",
        image = R.drawable.user_guide_main_screen
    ),
    OnBoardPage(
        title = "Özgürce Dolaş !",
        description = "Uygulamamızın menüsünü kullanarak size sunduğumuz özellikleri kolayca kullanabilirsin.",
        image = R.drawable.user_guide_menu
    ),
    OnBoardPage(
        title = "Tariflerimiz",
        description = "Diyetisyenimizin yayınladığı sağlıklı tarifleri sergilediğimiz sayfa karşınızda ! ",
        image = R.drawable.user_guide_receipt_screen
    ),
    OnBoardPage(
        title = "Favori Tarifler",
        description = "Dilediğiniz tarifleri favorilere ekleyip daha kolay erişim sağlayabilirsiniz.",
        image = R.drawable.user_guide_favorite_screen
    ),
    OnBoardPage(
        title = "Öğün Ekle",
        description = "Karta tıklayarak öğün ekleme sayfasına gidebilirsin.",
        image = R.drawable.user_guide_add_meal
    ),
    OnBoardPage(
        title = "Tarifleri öğününe ekle ! ",
        description = "Diyetisyenimizin yayınladığı sağlıklı tarifleri öğün listesine ekleyebilirsin.",
        image = R.drawable.user_guide_add_receipt
    ),
    OnBoardPage(
        title = "İşte öğün listen ! ",
        description = "Listeni düzenleyebilir ve tek tıkla bolus hesaplama ekranına gidebilirsin.",
        image = R.drawable.user_guide_add_meal_list
    ),
    OnBoardPage(
        title = "Bolus Hesapla",
        description = "Öğün Listendeki karbonhidrat otomatik hesaplanır , istersen ekstra karbonhidrat gir ve bolus değerini hesapla ! ",
        image = R.drawable.user_guide_bolus
    ),


    OnBoardPage(
        title = "Haydi Başlayalım",
        description = "Uygulamayı kullanmaya hazırsın butona tıkla ve kullanmaya başla ! ",
        image = R.drawable.colyak
    )
)
