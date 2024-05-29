package com.example.colyak.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.cards.ReplyCommentCard
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.components.functions.timeSince
import com.example.colyak.model.CommentRepliesResponse
import com.example.colyak.model.data.ReplyData
import com.example.colyak.viewmodel.ReplyViewModel
import com.example.colyak.viewmodel.globalReplyList
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentReplyScreen(comment: CommentRepliesResponse, navController: NavController) {
    val replyTf = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val replyVM: ReplyViewModel = viewModel()
    val loading by replyVM.loading.collectAsState()
    val context = LocalContext.current
    val isVisible = remember { mutableStateOf(false) }
    val isUpdating = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val updatingReplyId = remember { mutableLongStateOf(0L) }


    LaunchedEffect(Unit) {
        replyVM.getCommentsById(comment.commentResponse.commentId)
    }
    if (loading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularIndeterminateProgressBar(isDisplay = loading)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Yorum Cevapları")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        globalReplyList.clear()
                    }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    scope.launch {
                        isVisible.value = true
                        isUpdating.value = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.appBarColor))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Cevap Ekle")
                    Spacer(modifier = Modifier.size(width = 3.dp, height = 0.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.comment),
                        contentDescription = ""
                    )

                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .weight(1f)
                ) {
                    items(1) {
                        ReplyCommentCard(
                            userName = comment.commentResponse.userName,
                            createDate = comment.commentResponse.createdDate,
                            comment = comment.commentResponse.comment.trim('"')
                        )
                    }
                    items(1) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Cevaplar",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                    items(globalReplyList.size) {
                        val reply = globalReplyList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = reply?.userName ?: "",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W600
                                    )
                                    if (reply != null) {
                                        Text(
                                            text = timeSince(reply.createdDate),
                                            fontSize = 12.sp
                                        )
                                    }
                                    val expanded = remember { mutableStateOf(false) }
                                    if (reply != null) {
                                        if (reply.userName == loginResponse.userName) {
                                            IconButton(onClick = {
                                                expanded.value = !expanded.value
                                            }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = ""
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expanded.value,
                                                onDismissRequest = { expanded.value = false }
                                            ) {
                                                DropdownMenuItem(
                                                    onClick = {
                                                        expanded.value = false
                                                        replyTf.value = reply.reply
                                                        isVisible.value = true
                                                        isUpdating.value = true
                                                        updatingReplyId.longValue = reply.replyId
                                                    },
                                                    text = {
                                                        Text(text = "Düzenle")
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.edit_icon),
                                                            contentDescription = ""
                                                        )
                                                    }
                                                )

                                                DropdownMenuItem(
                                                    onClick = {
                                                        expanded.value = false
                                                        scope.launch {
                                                            Toast.makeText(
                                                                context,
                                                                "Silme İşlemi Başarılı",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            replyVM.deleteReply(reply.replyId)
                                                            replyVM.getCommentsById(comment.commentResponse.commentId)
                                                        }
                                                    },
                                                    text = {
                                                        Text(text = "Sil")
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.delete),
                                                            contentDescription = ""
                                                        )
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
                                        .padding(horizontal = 8.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Text(
                                        text = reply?.reply?.trim('"') ?: "",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W400
                                    )
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
                                text = if (isUpdating.value) "Cevap Güncelle" else "Cevap Ekle",
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
                                tfValue = replyTf.value,
                                onValueChange = {
                                    replyTf.value = it
                                },

                                label = if (isUpdating.value) "Cevap Güncelle" else "Cevap Ekle",
                                isPassword = false
                            )

                            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                            CustomizeButton(
                                onClick = {
                                    scope.launch {
                                        if (isUpdating.value) {
                                            replyVM.updateReply(
                                                replyId = updatingReplyId.longValue,
                                                newReply = replyTf.value
                                            )
                                            Toast.makeText(
                                                context,
                                                "Cevap Başarıyla Güncellendi",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            replyVM.getCommentsById(comment.commentResponse.commentId)
                                        } else {
                                                replyVM.createReply(
                                                    ReplyData(
                                                        commentId = comment.commentResponse.commentId,
                                                        reply = replyTf.value
                                                    )
                                                )
                                                replyTf.value = ""
                                                replyVM.getCommentsById(comment.commentResponse.commentId)
                                                Toast.makeText(context, "Cevap Eklendi", Toast.LENGTH_SHORT).show()

                                            Toast.makeText(
                                                context,
                                                "Cevap Başarıyla Eklendi",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        replyTf.value = ""
                                        replyVM.getCommentsById(comment.commentResponse.commentId)
                                        isVisible.value = false
                                    }
                                },
                                buttonText = if (isUpdating.value) "Güncelle" else "Ekle",
                                backgroundColor = colorResource(id = R.color.appBarColor)
                            )
                            Spacer(modifier = Modifier.size(height = 30.dp, width = 0.dp))
                        }
                    }
                }
            }
            BackHandler(true) {
                navController.popBackStack()
                globalReplyList.clear()
            }
        },
    )
}

