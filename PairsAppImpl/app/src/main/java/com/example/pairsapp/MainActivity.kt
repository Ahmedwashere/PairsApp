package com.example.pairsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlin.math.ceil
import kotlin.math.exp

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color(100, 34, 100)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome To The Pairs App!", modifier = Modifier.padding(16.dp),
            fontSize = 25.sp,
            color = Color(225, 225, 225)
        )
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
                    .padding(bottom = 8.dp, top = 24.dp, start = 16.dp, end = 16.dp)
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
                Text(
                    "Get Started",
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
    val context = LocalContext.current
    var teamSize by remember { mutableStateOf("") }
    var totalPlayers by remember { mutableStateOf("") }
    // Make this into an Enum down the line for the dropdown menu
    var selectedFile by remember { mutableStateOf("") }
    val csv_files =
        listOf("Liverpool Players", "Saints Players", "Chelsea Players", "Random Players")

    var selectedSimilarity by remember { mutableStateOf("") }
    val similarities = listOf(
        "Same skill level",
        "Names start with the same letter",
        "Names end with same letter",
        "last names are same length"
    )

    // Map for the teams once submit is clicked
    var teams = mutableMapOf<String, List<String>>()


    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            MainScreenHeader()

            OutlinedTextBoxForInput(
                value = teamSize,
                onValueChange = { teamSize = it.filter { it.isDigit() } },
                label = "Team Size",
                placeholder = "Please input a number",
                supportingText = "The number of players you would like for each team " +
                        "to contain",
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextBoxForInput(
                value = totalPlayers,
                onValueChange = { totalPlayers = it.filter { it.isDigit() } },
                label = "Total Players",
                placeholder = "Please input a number",
                supportingText = "The total number of players you would like to " +
                        "select from.",
                modifier = Modifier.padding(8.dp)
            )

            MainAppScreenDropDownMenu(
                items = csv_files,
                selectedItem = selectedFile,
                onItemSelected = { selectedFile = it },
                label = "Input File",
                placeholder = "Please Selected An Input File",
                supportingText = "Input csv file which will contain players and " +
                        "will be used to create teams.",
                modifier = Modifier.padding(8.dp)

            )

            MainAppScreenDropDownMenu(
                items = similarities,
                selectedItem = selectedSimilarity,
                onItemSelected = { selectedSimilarity = it },
                label = "Similarity",
                placeholder = "Please select a similarity",
                supportingText = "The similarity you would like to group " +
                        "teams by.",
                modifier = Modifier.padding(8.dp)

            )

            Button(
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                    teams = createTeams(
                        context,
                        teamSize.toInt(),
                        totalPlayers.toInt(),
                        selectedFile,
                        selectedSimilarity
                    )
                },
            ) {
                Text("Make My Teams!")
            }
        }

    }
}

@Composable
fun MainScreenHeader() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.mainappscreenbackgroundcropped),
            contentDescription = "Tennis ball laying on a green tennis court.",
            modifier = Modifier
                .size(400.dp, 100.dp)
                .clip(RectangleShape),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Text(
            "Make Your Team!",
            fontSize = 36.sp,
            fontFamily = LuckiestGuy,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 30.dp),
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun OutlinedTextBoxForInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    supportingText: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            supportingText = { Text(supportingText) },
            trailingIcon = {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreenDropDownMenu(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    label: String,
    placeholder: String,
    supportingText: String,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    // Send these two in

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedItem,
            onValueChange = {

            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            supportingText = { Text(supportingText) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onItemSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun createTeams(
    context: Context,
    teamSize: Int,
    totalPlayers: Int,
    selectedFile: String,
    selectedSimilarity: String
): MutableMap<String, List<String>> {

    // Input File Map
    val inputFileMap = mapOf(
        "Liverpool Players" to R.raw.liverpool_players_data,
        "Saints Players" to R.raw.saints_players,
        "Chelsea Players" to R.raw.team_chelsea,
        "Random Players" to R.raw.unique_team_data
    )

    // selectedSimilarity Map
    val similarityMap: Map<String, (Player, Player) -> Boolean> = mapOf(
        "Same skill level" to {p1, p2 -> p1.skillLevel == p2.skillLevel},
        "Names start with the same letter" to { p1 , p2 -> p1.firstName[0] == p2.firstName[0] },
        "Names end with same letter" to {p1, p2 -> p1.lastName[0] == p2.lastName[0] },
        "last names are same length" to {p1, p2 -> p1.lastName.length == p2.lastName.length}
    )

    // Maximum number of teams
    val max_teams = ceil((totalPlayers / teamSize).toDouble())

    Log.d("Creating Teams", "createTeams: I reached here")

    // Now I need to read in the csv file line by line.
    inputFileMap[selectedFile]?.let {
        val inputStream = context.resources.openRawResource(it)
        csvReader().open(inputStream)  {
            val headerRow = readNext()
            readAllAsSequence().forEach { player ->
                Log.d("Creating Teams", "createTeams: $player")
            }
        }
    }

    // Now that I can read it line by line, I'll filter the sequence.


    return mutableMapOf()

}

@Preview(
    name = "GetStartedScreenUI",
    showBackground = true,
)
@Composable
fun GetStartedScreenPreview() {
    val navController = navigation()

    PairsAppTheme {
        GetStartedScreen(navController = navController)
    }
}

@Preview(
    name = "MainAppScreenUI",
    showBackground = true,

    )
@Composable
fun MainAppScreenPreview() {

    PairsAppTheme {
        MainAppScreen(navController = rememberNavController())
    }
}

@Preview(
    name = "MainScreenHeader",
    showBackground = true,
)
@Composable
fun MainScreenHeaderPreview() {

    PairsAppTheme {
        MainScreenHeader()
    }
}

@Preview(
    name = "OutlinedTextField",
    showBackground = true,
)
@Composable
fun OutlinedTextBoxPreview() {

    PairsAppTheme {
        var test by remember { mutableStateOf("") }
        OutlinedTextBoxForInput(
            value = test,
            onValueChange = { test = it },
            label = "Test",
            placeholder = "placeholder!",
            supportingText = "Supporting text here blah blah blah"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Dropdown Menu Preview",
    showBackground = true
)
@Composable
fun MainAppScreenDropDownPreview() {
    var test by remember { mutableStateOf("placeholder") }
    MainAppScreenDropDownMenu(
        items = listOf("Item 1", "Item 2", "Item 3"),
        selectedItem = test,
        onItemSelected = { test = it },
        label = "Test",
        placeholder = "placeholder!",
        supportingText = "Supporting text here blah blah blah",
        modifier = Modifier.padding(16.dp)
    )
}



