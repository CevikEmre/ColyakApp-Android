package com.example.colyak.components.receiptDetail

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.components.functions.timeSince
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.CommentData
import com.example.colyak.screens.ColyakApp
import com.example.colyak.screens.Screens
import com.example.colyak.viewmodel.CommentViewModel
import com.example.colyak.viewmodel.ReplyViewModel
import com.example.colyak.viewmodel.loginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CommentTab(
    paddingValues: PaddingValues,
    receipt: Receipt,
    navController: NavController
) {
    val isVisible = remember { mutableStateOf(false) }
    val isUpdating = remember { mutableStateOf(false) }
    val updatingCommentId = remember { mutableLongStateOf(0L) }
    val replyVM: ReplyViewModel = viewModel()
    val commentTf = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val commentVM: CommentViewModel = viewModel()
    val loading by replyVM.loading.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val commentReplies by replyVM.commentRepliesList.observeAsState()

    LaunchedEffect(Unit) {
        receipt.id?.let { replyVM.getCommentsRepliesByReceiptId(it) }
    }
    Scaffold(
        floatingActionButton = {
            Button(
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    scope.launch {
                        isVisible.value = true
                        isUpdating.value = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.statusBarColor))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Yorum Ekle")
                    Spacer(modifier = Modifier.size(width = 4.dp, height = 0.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.comment),
                        contentDescription = ""
                    )
                }
            }
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if (commentReplies.isNullOrEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Henüz bu tarife herhangi bir yorum yapılmadı.",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        contentPadding = paddingValues,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(commentReplies?.size ?: 0) {
                            val comment = commentReplies?.get(it)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 6.dp)
                                    .clickable {
                                        scope.launch {
                                            comment?.commentResponse?.let { comment ->
                                                replyVM.getCommentsById(
                                                    comment.commentId
                                                )
                                            }
                                            val commentJson = Gson().toJson(comment)
                                            val formattedCommentJson = Uri.encode(commentJson)
                                            navController.navigate("${Screens.CommentReplyScreen.screen}/$formattedCommentJson")
                                        }
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 10.dp, horizontal = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        comment?.commentResponse?.userName?.let { userName ->
                                            Text(
                                                text = userName,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.W600
                                            )
                                        }
                                        if (comment != null) {
                                            Text(text = timeSince(comment.commentResponse.createdDate)).toString()
                                        }
                                        val expanded = remember { mutableStateOf(false) }
                                        if (comment != null) {
                                            if (comment.commentResponse.userName == loginResponse.userName) {
                                                IconButton(onClick = {
                                                    expanded.value = !expanded.value
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.MoreVert,
                                                        contentDescription = null
                                                    )
                                                }
                                                DropdownMenu(
                                                    expanded = expanded.value,
                                                    onDismissRequest = { expanded.value = false },
                                                ) {
                                                    DropdownMenuItem(
                                                        text = { Text("Düzenle") },
                                                        leadingIcon = {
                                                            Icon(
                                                                painter = painterResource(id = R.drawable.edit_icon),
                                                                contentDescription = ""
                                                            )
                                                        },
                                                        onClick = {
                                                            expanded.value = false
                                                            commentTf.value =
                                                                comment.commentResponse.comment
                                                            isVisible.value = true
                                                            isUpdating.value = true
                                                            updatingCommentId.longValue =
                                                                comment.commentResponse.commentId
                                                        }
                                                    )
                                                    DropdownMenuItem(
                                                        text = { Text("Sil") },
                                                        leadingIcon = {
                                                            Icon(
                                                                painter = painterResource(id = R.drawable.delete),
                                                                contentDescription = ""
                                                            )
                                                        },
                                                        onClick = {
                                                            expanded.value = false
                                                            scope.launch {
                                                                Toast.makeText(
                                                                    ColyakApp.applicationContext(),
                                                                    "Silme İşlemi Başarılı",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                commentVM.deleteComment(comment.commentResponse.commentId)
                                                                receipt.id?.let { receiptId ->
                                                                    replyVM.getCommentsRepliesByReceiptId(
                                                                        receiptId
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    HorizontalDivider(
                                        thickness = 0.8.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ) {
                                        Text(
                                            text = comment?.commentResponse?.comment
                                                ?: "",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W400
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (comment?.replyResponses?.isNotEmpty() == true) {
                                            Text(
                                                text = "${comment.replyResponses.size}" + " Cevap",
                                                fontSize = 12.sp,
                                                color = Color.Blue,
                                            )
                                        } else {
                                            Text(
                                                text = "Cevap Ekle",
                                                fontSize = 12.sp,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
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
                                text = if (isUpdating.value) "Yorumu Güncelle" else "Yorum Ekle",
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
                                tfValue = commentTf.value,
                                onValueChange = {
                                    commentTf.value = it
                                },
                                label = if (isUpdating.value) "Yorumu Güncelle" else "Yorum Ekle",
                                isPassword = false
                            )
                            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                            CustomizeButton(
                                onClick = {
                                    scope.launch {
                                        if (isUpdating.value) {
                                            commentVM.updateComment(
                                                commentId = updatingCommentId.longValue,
                                                comment = commentTf.value
                                            )
                                            Toast.makeText(ColyakApp.applicationContext(), "Yorum Başarıyla Güncellendi", Toast.LENGTH_SHORT).show()
                                        } else {
                                            receipt.id?.let { CommentData(it, commentTf.value) }
                                                ?.let { receiptId ->
                                                    commentVM.createComment(receiptId)
                                                }
                                            Toast.makeText(ColyakApp.applicationContext(), "Yorum Başarıyla Eklendi", Toast.LENGTH_SHORT).show()
                                        }
                                        commentTf.value = ""
                                        receipt.id?.let { replyVM.getCommentsRepliesByReceiptId(it) }
                                        isVisible.value = false
                                    }
                                },
                                buttonText = if (isUpdating.value) "Güncelle" else "Ekle",
                                backgroundColor = colorResource(id = R.color.statusBarColor)
                            )
                            Spacer(modifier = Modifier.size(height = 30.dp, width = 0.dp))
                        }
                    }
                }
            }
        }
    )
}
