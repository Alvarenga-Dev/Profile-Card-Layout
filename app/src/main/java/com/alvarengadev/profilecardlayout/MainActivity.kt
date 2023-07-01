package com.alvarengadev.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.alvarengadev.profilecardlayout.model.UserProfile
import com.alvarengadev.profilecardlayout.model.userProfileList
import com.alvarengadev.profilecardlayout.ui.theme.MyTheme
import com.alvarengadev.profilecardlayout.ui.theme.lightYellow

const val USERS_LIST_SCREEN = "users_list"
const val USER_DETAILS_SCREEN = "user_details"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                UsersApplication()
            }
        }
    }
}

@Composable
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = USERS_LIST_SCREEN) {
        composable(route = USERS_LIST_SCREEN) {
            UserListScreen(userProfiles, navController)
        }
        composable(route = USER_DETAILS_SCREEN) {
            UserProfileDetailsScreen()
        }
    }
}

@Composable
fun UserListScreen(
    userProfiles: List<UserProfile>,
    navController: NavController?
) {
    Scaffold(topBar = { AppBar("Messaging App") }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                items(userProfiles) { userProfile ->
                    ProfileCard(userProfile = userProfile) {
                        navController?.navigate(USER_DETAILS_SCREEN)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(titleScreen: String) {
    TopAppBar(
        title = { Text(titleScreen) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = ""
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun ProfileCard(
    userProfile: UserProfile,
    clickAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .shadow(elevation = 16.dp, shape = CutCornerShape(topEnd = 32.dp))
            .clickable(onClick = clickAction)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            userProfile.apply {
                ProfilePicture(pictureUrl, status, 72.dp)
                ProfileContent(name, status, Alignment.Start)
            }
        }
    }
}

//Done
@Composable
fun ProfilePicture(
    pictureUrl: String,
    onlineStatus: Boolean,
    imageSize: Dp
) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus) MaterialTheme.colorScheme.lightYellow else Color.Red
        ),
        modifier = Modifier
            .padding(16.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
    ) {
        SubcomposeAsyncImage(
            model = pictureUrl,
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = "",
            modifier = Modifier.size(imageSize),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProfileContent(
    name: String,
    onlineStatus: Boolean,
    alignment: Alignment.Horizontal
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = if (onlineStatus.not()) Modifier.alpha(0.5f) else Modifier.alpha(1f)
        )
        Text(
            text = if (onlineStatus) "Active now" else "Off-line",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alpha(0.5f)
        )
    }
}

//Second Screen
@Composable
fun UserProfileDetailsScreen(userProfile: UserProfile = userProfileList[0]) {
    Scaffold(topBar = { AppBar("Contact: ${userProfile.name}") }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                userProfile.apply {
                    ProfilePicture(pictureUrl, status, 240.dp)
                    ProfileContent(name, status, Alignment.CenterHorizontally)
                }
            }
        }
    }
}

@Preview
@Composable
fun UserProfileDetailsPreview() {
    MyTheme {
        UserProfileDetailsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    MyTheme {
        UserListScreen(userProfiles = userProfileList, navController = null)
    }
}
