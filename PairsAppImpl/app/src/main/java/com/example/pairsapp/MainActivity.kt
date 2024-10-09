package com.example.pairsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pairsapp.ui.theme.BreeSerif
import com.example.pairsapp.ui.theme.LuckiestGuy
import com.example.pairsapp.ui.theme.PairsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navigation()
        }
    }
}

@Composable
fun navigation(): NavHostController {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "getStartedScreen") {
        composable("getStartedScreen") {
            GetStartedScreen(navController)
        }

        composable("mainScreen") {
            MainAppScreen(navController)
        }
    }
    return navController
}

@Composable
fun Header() {
    Column( modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(color = Color(100, 34, 100)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Welcome To The Pairs App!", modifier = Modifier.padding(16.dp),
            fontSize = 25.sp,
            color = Color(225, 225, 225))
    }
}

@Composable
fun GetStartedScreen(navController: NavHostController) {
    Box {
        Column {
            Image(
                painter = painterResource(id = R.drawable.pairslogoblack_nobackground),
                contentDescription = "Pairs App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.FillWidth
            )

            Image(
                painter = painterResource(id = R.drawable.soccerhomepageimagecropped),
                contentDescription = "Man Playing Soccer",
                modifier = Modifier
                    .clip(RectangleShape)
                    .height(524.dp),
                contentScale = ContentScale.Crop
            )

            // This text composable gave me so much trouble since I forgot about
            // the TextAlign attribute
            Text(
                "The Easy Way To Make Teams",
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 24.dp, start= 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 37.sp,
                fontFamily = LuckiestGuy,
                textAlign = TextAlign.Center
            )


            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(371.dp, 70.dp),
                onClick = { navController.navigate("mainScreen") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64D667),
                    contentColor = Color.Black
                ),

            ) {
                Text("Get Started",
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = BreeSerif,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun MainAppScreen(navController: NavHostController) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Click me to go back to the main screen!")
        }
    }
}

@Preview(
    name = "GetStartedScreenUI"
)
@Composable
fun GetStartedScreenPreview() {
    val navController = navigation()

    PairsAppTheme {
        GetStartedScreen(navController = navController)
    }
}

@Preview(
    name = "MainAppScreenUI"
)
@Composable
fun MainAppScreenPreview() {
    val navController = navigation()

    PairsAppTheme {
        MainAppScreen(navController = navController)
    }
}

