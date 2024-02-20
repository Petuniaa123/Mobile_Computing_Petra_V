package com.example.hw4

import SampleData
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.hw4.ui.theme.HW4Theme
import com.example.hw4.ui.theme.PurpleGrey80
import com.example.hw4.ui.theme.User
import com.example.hw4.ui.theme.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HW4Theme {
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
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.observeAsState()

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
                },
                navigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("profile") {
                            inclusive = true
                        }
                    }
                },
                viewModel = userViewModel
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
        composable("profile") {
            Profile(
                navigateBack = {
                    navController.navigate("messages") {
                        popUpTo("messages") {
                            inclusive = true
                        }
                    }
                },
                viewModel = userViewModel,
                user = user
            )
        }
    }
}



@Composable
fun Profile(navigateBack: () -> Unit, viewModel: UserViewModel, user: User?) {

    var username by remember { mutableStateOf(user?.username?: "") }

    var imageUri by remember { mutableStateOf(user?.image?: "") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {

            imageUri = uri.toString()

        }
    }

    Column {

        Button(
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = buttonColors(containerColor = PurpleGrey80)
        ) {
            Text(text = "Go back")
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 1.dp,
            modifier = Modifier
                .animateContentSize()
                .padding(1.dp)
        ) {
            Column {
                Text(
                    text = "Profile",
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    AsyncImage(
                        model = imageUri,
                        placeholder = painterResource(id = R.drawable.defaultprofilepic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable {
                                launcher.launch(arrayOf("image/*"))
                            }
                    )

                    TextField(
                        value = username,
                        onValueChange = { newText -> username = newText },
                        placeholder = { Text("Default user") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.saveUser(username, imageUri)
                    },
                    modifier = Modifier
                        //.size(100.dp)
                        .width(400.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Save")

                }
            }
        }
    }
}

@Composable
fun Friends(navigateBack: () -> Unit) {
    Column {
        Button(
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = buttonColors(containerColor = PurpleGrey80)
        ) {
            Text(text = "Go back")
        }


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

@Composable
fun MessageCard(msg: Message, userViewModel: UserViewModel) {

    val user by userViewModel.user.observeAsState()

    Row(modifier = Modifier.padding(all = 8.dp)) {

        val image = user?.image
        if (image?.isNotEmpty() == true) {
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = "kuva",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.defaultprofilepic),
                contentDescription = "kuva",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
        //}
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
                text = user?.username ?: "Default user",
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

@Composable
fun Conversation(
    messages: List<Message>,
    navigateToFriends: () -> Unit, navigateToProfile: () -> Unit,
    viewModel: UserViewModel
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = navigateToFriends,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 16.dp),
                colors = buttonColors(containerColor = PurpleGrey80)
            ) {
                Text(text = "See friends list")
            }
            Button(
                onClick = navigateToProfile,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 16.dp),
            ) {
                Text(text = "Profile")
            }
        }

        LazyColumn {
            items(messages) { message ->
                MessageCard(message, viewModel)
            }
        }
    }
}
