package com.compose.androidtoanime.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode.Companion.SrcAtop
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.data.model.Message
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel
import com.wishes.jetpackcompose.runtime.NavRoutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chat(
    viewModel: MainViewModel,
    pricingViewModel: PricingViewModel,
    navController: NavHostController
) {

    val listState = LazyListState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentMessages = remember {
        //mutableStateListOf(Message("How I can help you", "chatBot", "Now"))
        viewModel.messages
    }
    val textArray = context.resources.getStringArray(R.array.animeBot)
    LaunchedEffect(key1 = viewModel.messages.size) {
        Log.d(AppUtils.TAG_D, "to bottom")
        listState.animateScrollToItem(currentMessages.size - 1)
    }
    LaunchedEffect(key1 = Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.pop)
        mediaPlayer.start()
        listState.animateScrollToItem(currentMessages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Appbar(
            pricingViewModel.isSubscribe.value,
            onBack = { navController.popBackStack() },
            goToPro = {
                navController.navigate(NavRoutes.Premium.route)
            })
        ChatBody(
            context = context,
            textArray.toList(),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            (viewModel._message is NetworkResults.Loading),
            currentMessages,
            listState
        )
        BottomChat() {
            viewModel.sendMessage(
                pricingViewModel.isSubscribe.value,
                Message(it.text, it.sender, it.timestamp),
                context
            )
            //currentMessages.add(Message(it.text, it.sender, it.timestamp))
            coroutineScope.launch {
                listState.animateScrollToItem(currentMessages.size - 1)
            }
            Log.d("message", "{$it.sender}")
        }
    }
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            //listState.animateScrollToItem(messages.value.size - 1)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(isSubscribe: Boolean, onBack: () -> Unit, goToPro: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable {
                        onBack()
                    }
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    painter = painterResource(id = R.drawable.chatbot),
                    contentDescription = "iconBot",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column() {
                    Text(
                        text = "AnimeBot",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                        )
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 3.dp)
                        )
                    }
                }


            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            if (!isSubscribe)
                Box(modifier = Modifier.padding(6.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.premium),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .padding(6.dp)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                val brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Yellow,
                                        Color.Magenta
                                    )
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(brush, blendMode = SrcAtop)
                                }
                            }
                            .clickable {
                                goToPro()
                            }
                    )
                }

        }

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatBody(
    context: Context,
    textArray: List<String>,
    modifier: Modifier, typing: Boolean,
    messages: MutableList<Message>,
    state: LazyListState
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val offsetTyping by animateDpAsState(
        targetValue = if (!typing) -100.dp else 0.dp,
        tween(1000)
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier,
            state = state
        ) {
            item() {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chatbot),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    TypewriterText(texts = textArray)
                }
            }
            items(messages.size) {
                if (messages[it].sender == "AnimeBot")
                    ChatBot(messages[it]) {
                        clipboardManager.setText(AnnotatedString(it))
                        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
                    }
                else
                    User(messages[it])
            }
            item {
                Text(
                    text = "typing ...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .offset(offsetTyping)
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBot(message: Message, onLongClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(9.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp)
                .border(
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(0.5f)),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 40.dp, bottomEnd = 40.dp)
                )

        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(10.dp)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            onLongClick(message.text)
                        },
                    )
            )
        }
        Text(
            text = "${message.sender} ${message.timestamp}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(start = 10.dp)
        )

    }
}

@Composable
fun User(message: Message) {

    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 10.dp, bottomStart = 40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.End)
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(10.dp)
            )
        }

        Text(
            text = "${message.sender} ${message.timestamp}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomChat(addMessage: (Message) -> Unit) {
    var inputValue by remember { mutableStateOf("") } // 2
    fun sendMessage() { // 3
        inputValue = ""
    }
    Row(modifier = Modifier.padding(10.dp)) {
        TextField( // 4
            modifier = Modifier.weight(1f),
            value = inputValue,
            onValueChange = { inputValue = it },
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Send),
            keyboardActions = KeyboardActions {
                if (inputValue.isNotBlank()) {
                    val message = Message(inputValue, "you", "10:12")
                    //chat.addMessage(inputValue, "you", "10:12")
                    addMessage(message)
                    inputValue = ""
                    //Log.d("message_debug",message.toString())
                }
            },
            interactionSource = MutableInteractionSource(),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.background,
                disabledTextColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.onBackground.copy(0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

                ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "send",
                    tint = if (inputValue.isNotBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(0.5f)
                    },
                    modifier = Modifier.clickable {
                        if (inputValue.isNotBlank()) {
                            val message = Message(inputValue, "you", "10:12")
                            //chat.addMessage(inputValue, "you", "10:12")
                            addMessage(message)
                            inputValue = ""
                            //Log.d("message_debug",message.toString())
                        }
                    }
                )
            }

        )
        Spacer(modifier = Modifier.width(6.dp))
    }
}


@Composable
fun TypewriterText(
    texts: List<String>,
) {
    var textIndex by remember {
        mutableStateOf(0)
    }
    var textToDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = texts,
    ) {
        while (textIndex < texts.size) {
            texts[textIndex].forEachIndexed { charIndex, _ ->
                textToDisplay = texts[textIndex]
                    .substring(
                        startIndex = 0,
                        endIndex = charIndex + 1,
                    )
                delay(100)
            }
            textIndex = (textIndex + 1) % texts.size
            delay(1000)
        }
    }

    Text(
        text = textToDisplay,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(16.dp)
    )
}




