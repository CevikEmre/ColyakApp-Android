package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


    LaunchedEffect(Unit) {
        replyVM.getCommentsById(comment.commentResponse.commentId)
        Log.e("globalCommentList", globalReplyList.toString())
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 6.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = comment.commentResponse.userName,
                                        fontWeight = FontWeight.W600,
                                        fontSize = 18.sp
                                    )
                                    Text(text = timeSince(comment.commentResponse.createdDate)).toString()
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
                                        text = comment.commentResponse.comment,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W400
                                    )
                                }
                            }
                        }
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
                            elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
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
                                    if (reply != null) {
                                        if (reply.userName == loginResponse.userName) {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    replyVM.deleteReply(reply.replyId)
                                                    replyVM.getCommentsById(comment.commentResponse.commentId)
                                                    Toast.makeText(
                                                        context,
                                                        "Cevap Silindi",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.delete),
                                                    contentDescription = ""
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
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Text(
                                        text = reply?.reply ?: "",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W400
                                    )
                                }
                            }
                        }

                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Input(
                        tfValue = replyTf.value,
                        onValueChange = {
                            replyTf.value = it
                        },
                        label = "Cevap Ekle",
                        isPassword = false
                    )
                    Spacer(
                        modifier = Modifier.size(
                            height = 10.dp,
                            width = 0.dp
                        )
                    )
                    CustomizeButton(
                        onClick = {
                            scope.launch {
                                Toast.makeText(
                                    context,
                                    "Cevap Başarıyla Eklendi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                replyVM.createReply(
                                    ReplyData(
                                        commentId = comment.commentResponse.commentId,
                                        reply = replyTf.value
                                    )
                                )
                                replyTf.value = ""
                                replyVM.getCommentsById(comment.commentResponse.commentId)
                            }
                        },
                        buttonText = "Ekle",
                        backgroundColor = colorResource(id = R.color.appBarColor)
                    )
                }
            }
            BackHandler(true) {
                navController.popBackStack()
                globalReplyList.clear()
            }
        },
    )
}

