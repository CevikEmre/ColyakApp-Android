import android.annotation.SuppressLint
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.components.timeSince
import com.example.colyak.model.Comment
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.CommentData
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
    commentList: MutableList<Comment>,
    receipt: Receipt,
    navController: NavController
) {
    val isVisible = remember { mutableStateOf(false) }
    val replyVM: ReplyViewModel = viewModel()
    val commentTf = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val commentVM: CommentViewModel = viewModel()
    val loading by replyVM.loading.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val commentReplies by replyVM.commentRepliesList.observeAsState()

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
        floatingActionButton = {
            Button(
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    scope.launch {
                        isVisible.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.appBarColor))
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Yorum Ekle")
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
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = paddingValues,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(commentList.size) {
                        val comment = commentList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 6.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
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
                                    Text(
                                        text = comment.userName,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W600
                                    )
                                    Text(text = timeSince(comment.createdDate)).toString()
                                    if (comment.userName == loginResponse.userName) {
                                        IconButton(onClick = {
                                            scope.launch {
                                                commentVM.deleteComment(comment.commentId)
                                                commentVM.getCommentsById(receipt.id)
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
                                        text = comment.comment,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W400
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (commentReplies?.isNotEmpty() == true) {
                                        Text(
                                            text = "${commentReplies!!.size}" + " Cevap",
                                            fontSize = 12.sp,
                                            color = Color.Blue,
                                            modifier = Modifier.clickable {
                                                scope.launch {
                                                    replyVM.getCommentsById(comment.commentId)
                                                    val commentJson = Gson().toJson(comment)
                                                    navController.navigate("${Screens.CommentReplyScreen.screen}/$commentJson")
                                                }
                                            }
                                        )
                                    } else {
                                        Text(
                                            text = "Cevap Ekle",
                                            fontSize = 12.sp,
                                            modifier = Modifier.clickable {
                                                scope.launch {
                                                    replyVM.getCommentsById(comment.commentId)
                                                    val commentJson = Gson().toJson(comment)
                                                    navController.navigate("${Screens.CommentReplyScreen.screen}/$commentJson")
                                                }

                                            }
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
                        Text(text = "Yorum Ekle", fontWeight = FontWeight.W700, fontSize = 16.sp)
                        Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                        HorizontalDivider(thickness = 0.5.dp, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                        Input(
                            tfValue = commentTf.value,
                            onValueChange = {
                                commentTf.value = it
                            },

                            label = "Yorum Ekle",
                            isPassword = false
                        )

                        Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                        CustomizeButton(
                            onClick = {
                                scope.launch {
                                    commentVM.createComment(
                                        CommentData(
                                            receipt.id,
                                            commentTf.value
                                        )
                                    )
                                    commentTf.value = ""
                                    commentVM.getCommentsById(receipt.id)
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
    )
}

