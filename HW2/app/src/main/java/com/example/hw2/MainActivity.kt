package com.example.hw2

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hw2.ui.theme.HW2Theme
import com.example.hw2.ui.theme.Pink80
import com.example.hw2.ui.theme.PurpleGrey40
import com.example.hw2.ui.theme.PurpleGrey80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HW2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    MyNavHost(navController = navController)
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "messages"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("messages") {
            Conversation(
                SampleData.conversationSample,
                navigateToFriends = {
                    navController.navigate("friendsList") {
                        popUpTo("friendslist") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("friendslist") {
            Friends(
                navigateBack = {
                    navController.navigate("messages") {
                        popUpTo("messages") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Friends(navigateBack: () -> Unit) {
    Column {
        Row {
            Text(text = "                               ")
            Button(
                onClick = navigateBack,
                colors = buttonColors(containerColor = PurpleGrey80)
            ) {
                Text(text = "Go back")
            }
        }
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 1.dp,
            // surfaceColor color will be changing gradually from primary to surface
            //color = surfaceColor,
            // animateContentSize will change the Surface size gradually
            modifier = Modifier
                .animateContentSize()
                .padding(1.dp)
        ) {
            Column {

                Text(
                    text = "Friends",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.demi),
                        contentDescription = "demi",
                        modifier = Modifier
                            // Set image size to 40 dp
                            .size(40.dp)
                            // Clip image to be shaped as a circle
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Text(
                        text = "Demi",
                        modifier = Modifier.padding(all = 4.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.suvi),
                        contentDescription = "suvi",
                        modifier = Modifier
                            // Set image size to 40 dp
                            .size(40.dp)
                            // Clip image to be shaped as a circle
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Text(
                        text = "Suvi",
                        modifier = Modifier.padding(all = 4.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.niisku),
                        contentDescription = "niisku",
                        modifier = Modifier
                            // Set image size to 40 dp
                            .size(40.dp)
                            // Clip image to be shaped as a circle
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Text(
                        text = "Niiskuneiti",
                        modifier = Modifier.padding(all = 4.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun FriendsPreview() {
    val navController = rememberNavController()
    Friends(navigateBack = {navController.navigate("messages")})
}

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.profiilikuva),
            contentDescription = "kuva",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "",
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    HW2Theme {
        Surface {
            MessageCard(
                msg = Message("Collegue", "Hey, take a look at Jetback Compose, it's great!")
            )
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, navigateToFriends: () -> Unit,) {
    Column {
        Row {
            Text(text = "                         ")
            Button(
                onClick = navigateToFriends,
                modifier = Modifier.padding(all = 4.dp),
                colors = buttonColors(containerColor = PurpleGrey80)
            ) {
                Text(text = "See friends list")
            }
        }
        LazyColumn {
            items(messages) { message ->
                MessageCard(message)
            }
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    HW2Theme {
        val navController = rememberNavController()
        Conversation(
            SampleData.conversationSample,
            navigateToFriends = { navController.navigate("friendsList")}

        )
    }
}
