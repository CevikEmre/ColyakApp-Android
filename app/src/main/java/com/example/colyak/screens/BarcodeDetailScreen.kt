import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.components.functions.ImageFromUrl
import com.example.colyak.model.data.SuggestionData
import com.example.colyak.screens.Screens
import com.example.colyak.viewmodel.SuggestionViewModel
import com.example.colyak.viewmodel.barcode
import kotlinx.coroutines.launch

var readedBarcode = ""
var barcodeAlert = false

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BarcodeDetailScreen(navController: NavController) {
    var showAlert by remember { mutableStateOf(false) }
    val isVisible = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val sugggestionTf = remember { mutableStateOf(readedBarcode) }
    val suggestionVM: SuggestionViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Barkod Detayı")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            barcode.id = 0
                        }
                    ) {
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
        },
        content = { padding ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (barcode.id.toInt() == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Barkod bulunamadı",
                                    fontWeight = FontWeight.W500,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        CustomizeButton(
                            onClick = { navController.navigate(Screens.MainScreen.screen) },
                            buttonText = "Ana Sayfa",
                            backgroundColor = colorResource(id = R.color.appBarColor)
                        )

                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageFromUrl(
                        url = "https://api.colyakdiyabet.com.tr/api/image/get/${barcode.imageId}",
                        modifier = Modifier.size(250.dp)
                    )
                    barcode.name?.let { name -> Text(text = name) }
                    if (barcode.glutenFree == true) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Gluten içeriyor",
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Gluten içermiyor",
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "",
                                tint = Color.Green
                            )
                        }
                    }
                    Text(text = "Besin Değerleri")
                    Card(
                        elevation = CardDefaults.cardElevation(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(barcode.nutritionalValuesList?.size ?: 0) {
                                val barcode = barcode.nutritionalValuesList?.get(it)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 12.dp),
                                    elevation = CardDefaults.cardElevation(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFF1EC),
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        barcode?.type?.let {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center,
                                            ) {
                                                Text(
                                                    text = it,
                                                    Modifier.padding(vertical = 10.dp),
                                                    fontWeight = FontWeight.W500,
                                                )
                                            }
                                        }
                                    }
                                }
                                barcode?.let {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Kalori")
                                        Text(text = it.calorieAmount.toString())
                                    }
                                    HorizontalDivider(thickness = 1.dp)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Karbonhidrat")
                                        Text(text = it.carbohydrateAmount.toString())
                                    }
                                    HorizontalDivider(thickness = 1.dp)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Protein")
                                        Text(text = it.proteinAmount.toString())
                                    }
                                    HorizontalDivider(thickness = 1.dp)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Yağ")
                                        Text(text = it.fatAmount.toString())
                                    }
                                }
                            }
                        }
                    }
                }
                CustomizeButton(
                    onClick = { navController.navigate(Screens.MainScreen.screen) },
                    buttonText = "Ana Sayfa",
                    backgroundColor = colorResource(id = R.color.appBarColor)
                )
                if (barcodeAlert) {
                    AlertDialog(
                        onDismissRequest = {
                            barcodeAlert = false
                        },
                        title = { Text("Barkod Bulunamadı") },
                        text = { Text("$readedBarcode Barkod Bulunamadı\nOkunan barkod değeri doğru ise bize önerin") },
                        confirmButton = {
                            Text(text = "Öner", modifier = Modifier.clickable {
                                scope.launch {
                                    barcodeAlert = false
                                    /* loading = true
                                     delay(500)
                                     loading = false*/
                                    isVisible.value = true
                                }
                            }
                            )
                        },
                        dismissButton = {
                            Text(text = "Tekrar Tara", modifier = Modifier.clickable {
                                showAlert = false
                                navController.popBackStack()
                            }
                            )
                        }
                    )
                }

                if (isVisible.value) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            isVisible.value = false
                        }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Öneri Gönder",
                                fontWeight = FontWeight.W700,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                            Input(
                                tfValue = sugggestionTf.value,
                                onValueChange = {
                                    sugggestionTf.value = it
                                },
                                label = "Öneri Ekle",
                                isPassword = false
                            )

                            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                            CustomizeButton(
                                onClick = {
                                    scope.launch {
                                        suggestionVM.addSuggestion(SuggestionData(sugggestionTf.value))
                                        Toast.makeText(
                                            context,
                                            "Öneri Gönderildi Teşekkürler ☺",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isVisible.value = false

                                    }
                                },
                                buttonText = "Ekle",
                                backgroundColor = colorResource(id = R.color.appBarColor)
                            )
                            Spacer(modifier = Modifier.size(height = 30.dp, width = 0.dp))
                        }
                    }
                }
            }
            BackHandler(true) {
                navController.popBackStack()
                barcode.id = 0
            }
        }
    )
}


