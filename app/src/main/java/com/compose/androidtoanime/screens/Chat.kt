package com.compose.androidtoanime.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.compose.androidtoanime.R
import com.compose.androidtoanime.data.model.Message
import com.compose.androidtoanime.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chat(viewModel: MainViewModel) {

    val listState = LazyListState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentMessages = remember {
        viewModel.messages
    }
    for (message in arrayMessages())
        viewModel.sendMessage(message)

    Column(modifier = Modifier.fillMaxSize()) {
        Appbar()
        ChatBody(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            currentMessages.toList(),
            listState
        )
        BottomChat() {
            viewModel.sendMessage(Message(it.text, it.sender, it.timestamp))
            currentMessages.add(Message(it.text, it.sender, it.timestamp))
            coroutineScope.launch {
                listState.animateScrollToItem(currentMessages.size - 1)
            }
            Log.d("message", it.sender)
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
fun Appbar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back",
                modifier = Modifier.padding(start = 6.dp)
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
        colors = TopAppBarDefaults.topAppBarColors()

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatBody(modifier: Modifier, messages: List<Message>, state: LazyListState) {
    LazyColumn(
        modifier = modifier,
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
                Text(text = "Start chat with AnimeBot")
            }
        }
        items(messages.size) {
            if (messages[it].sender == "chatBot")
                ChatBot(messages[it])
            else
                User(messages[it])
        }
        item {
            Text(
                text = "typing ...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ChatBot(message: Message) {
    Column(modifier = Modifier.padding(9.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 40.dp, bottomEnd = 40.dp))
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.background,
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
            keyboardActions = KeyboardActions { sendMessage() },
            interactionSource = MutableInteractionSource(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.background,
                disabledTextColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "send",
                    tint = if (inputValue.isNotBlank()) {
                        MaterialTheme.colorScheme.background
                    } else {
                        MaterialTheme.colorScheme.background.copy(0.5f)
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

class ChatClass {
    val messages = arrayListOf<Message>()

    fun addMessage(text: String, sender: String, timestamp: String) {
        val message = Message(text, sender, timestamp)
        messages.add(message)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun arrayMessages(): ArrayList<Message> {
    val chat = ArrayList<Message>()
    chat.add(
        Message(
            "Hello!",
            "you",
            "10:00 AM"
        )
    )
    //generateMessages(chat)
    return chat
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateMessages(chat: ArrayList<Message>) {

    val random = Random()
    val messages = arrayOf(
        "Hey, what's up?",
        "How's your day going?",
        "I had a great time last night",
        "I'm running late",
        "Let's meet at 5 PM",
        "Did you see the latest news?",
        "Can you send me that file?",
        "I'm on my way",
        "Okay, talk to you later",
        "Goodnight!"
    )
    val senders = arrayOf("chatBot", "you")

    for (i in 1..40) {
        val messageIndex = random.nextInt(messages.size)
        val senderIndex = random.nextInt(senders.size)
        val timestamp = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"
        chat.add(Message(messages[messageIndex], senders[senderIndex], timestamp))
    }
}
