package com.example.pairsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.Dp
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
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.lazyTableDimensions
import java.util.regex.Pattern
import kotlin.math.ceil

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
            MainAppScreen()
        }
    }
    return navController
}

@Composable
fun GetStartedScreen(navController: NavHostController) {

    Box {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
fun MainAppScreen() {
    val context = LocalContext.current
    var teamSize by remember { mutableStateOf("") }
    var totalPlayers by remember { mutableStateOf("") }
    // Make this into an Enum down the line for the dropdown menu
    var selectedFile by remember { mutableStateOf("") }
    val csvFiles =
        listOf("Liverpool Players", "Saints Players", "Chelsea Players", "Random Players")

    var selectedSimilarity by remember { mutableStateOf("") }
    val similarities = listOf(
        "Same skill level",
        "Names start with the same letter",
        "Names end with same letter",
        "last names are same length"
    )

    // Map for the teams once submit is clicked
    var teams by remember { mutableStateOf(mapOf<String, MutableList<Player>>()) }

    // boolean for showing players.
    var showPlayers by remember { mutableStateOf(false) }
    
    //mutable state that will be empty at the start. Will hold all players
    var players by remember { mutableStateOf(listOf<Player>()) }


    Box {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
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
                items = csvFiles,
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

                    if (teamSize.isNotBlank() && totalPlayers.isNotBlank()
                        && selectedFile.isNotBlank() && selectedSimilarity.isNotBlank()
                        && totalPlayers >= teamSize && teamSize.toInt() > 0) {
                        teams = createTeams(
                            context,
                            // Validating that our input is valid.
                            teamSize.toInt(),
                            // Validating that our input is valid.
                            totalPlayers.toInt(),
                            selectedFile,
                            selectedSimilarity
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
                    .padding(8.dp),
            ) {
                Text("Make My Teams!")
            }

            if (teams.isNotEmpty()) {
                DisplayTeamsTable(teams)
            }

            Button(
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                    if (selectedFile.isNotBlank()) {
                        showPlayers = !showPlayers
                        players = getPlayers(context, selectedFile)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(170.dp)
                    .padding(8.dp),
            ) {
                Text("Show Players")
            }

            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(500.dp)) {
                if (showPlayers && players.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(players) { player ->
                            PlayerCard(player)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun PlayerCard(player: Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${player.firstName} ${player.lastName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Skill Level: ${player.skillLevel}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Suppress("DEPRECATION")
fun getPlayers(context: Context, selectedFile: String): List<Player> {

    var validPlayers = listOf<Player>()

    val inputFileMap = mapOf(
        "Liverpool Players" to R.raw.liverpool_players_data,
        "Saints Players" to R.raw.saints_players,
        "Chelsea Players" to R.raw.team_chelsea,
        "Random Players" to R.raw.unique_team_data
    )

    inputFileMap[selectedFile]?.let { selected ->
        val inputStream = context.resources.openRawResource(selected)
        csvReader().open(inputStream) {
            readNext()
            validPlayers = readAllAsSequence()
                .filter { it.size >= 4 }
                .map { row ->
                    Player(
                        row[0],
                        row[1].replaceFirstChar { it.titlecaseChar() },
                        row[2].replaceFirstChar { it.titlecaseChar() },
                        SkillLevel.getSkillLevel(row[3])
                    )
                }
                // filter out the invalid data from our list
                .filter { p ->
                    (isValidEmail(p.email) &&
                            p.firstName.isNotBlank() &&
                            p.lastName.isNotBlank() &&
                            p.skillLevel != SkillLevel.UNKNOWN)
                }
                .toList()
        }
    }
    return validPlayers
}

@Composable
fun MainScreenHeader() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.mainappscreenbackgroundcropped),
            contentDescription = "Tennis ball laying on a green tennis court.",
            modifier = Modifier
                .size(500.dp, 100.dp)
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

@Suppress("DEPRECATION")
fun createTeams(
    context: Context,
    teamSize: Int,
    totalPlayers: Int,
    selectedFile: String,
    selectedSimilarity: String
): MutableMap<String, MutableList<Player>> {

    if (totalPlayers < teamSize || selectedFile.isEmpty() ||
        selectedSimilarity.isEmpty() || teamSize < 1) {
        throw IllegalArgumentException("Invalid input provided.")
    }

    // Input File Map
    val inputFileMap = mapOf(
        "Liverpool Players" to R.raw.liverpool_players_data,
        "Saints Players" to R.raw.saints_players,
        "Chelsea Players" to R.raw.team_chelsea,
        "Random Players" to R.raw.unique_team_data
    )

    // selectedSimilarity Map
    val similarityMap: Map<String, (Player) -> Any> = mapOf(
        "Same skill level" to { player -> player.skillLevel },
        "Names start with the same letter" to { player -> player.firstName.firstOrNull()?.uppercase() ?: "" },
        "Names end with the same letter" to { player -> player.lastName.lastOrNull()?.uppercase() ?: "" },
        "Last names are the same length" to { player -> player.lastName.length }
    )

    // Maximum number of teams
    ceil(totalPlayers.toDouble() / teamSize).toInt()

    // Initialize an empty map for teams
    val teams = mutableMapOf<String, MutableList<Player>>()

    // Now I need to read in the csv file line by line.
    inputFileMap[selectedFile]?.let { selected ->
        val inputStream = context.resources.openRawResource(selected)
        csvReader().open(inputStream) {
            readNext()
            val validPlayers = readAllAsSequence()
                .filter { it.size >= 4 }
                .map { row ->
                    Player(
                        row[0],
                        row[1].replaceFirstChar { it.titlecaseChar() },
                        row[2].replaceFirstChar { it.titlecaseChar() },
                        SkillLevel.getSkillLevel(row[3])
                    )
                }
                // filter out the invalid data from our list
                .filter { p ->
                    (isValidEmail(p.email) &&
                            p.firstName.isNotBlank() &&
                            p.lastName.isNotBlank() &&
                            p.skillLevel != SkillLevel.UNKNOWN)
                }
                .toList()

            // Group the players
            val groupedPlayers = validPlayers.groupBy { similarityMap[selectedSimilarity]?.invoke(it) ?: "" }

            // Make the teams from the grouping
            var teamNumber = 1
            groupedPlayers.forEach { (_, playersInGroup) ->
                var index = 0
                while (index < playersInGroup.size) {
                    val currentTeamKey = "Team $teamNumber"
                    val currentTeam = teams.getOrPut(currentTeamKey) { mutableListOf() }

                    // Calculate the remaining spots
                    val remainingSpots = teamSize - currentTeam.size

                    // Get a slice of the players to add to the current team using
                    // end index and remainingSpots
                    val endIndex = (index + remainingSpots)
                            // prevents out of bounds errors from occurring
                        .coerceAtMost(playersInGroup.size)
                    val playersToAdd = playersInGroup.subList(index, endIndex)

                    // Add those players to the team we are building up
                    currentTeam.addAll(playersToAdd)

                    // Move the index forward after adding the players
                    index += playersToAdd.size

                    // Add to teamNumber to begin building the next team.
                    if (currentTeam.size == teamSize) {
                        teamNumber++
                    }
                }
            }
        }
    }

    // Return the populated teams
    return teams
}


fun isValidEmail(email: String): Boolean {
    val pattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    val matcher = pattern.matcher(email)
    return matcher.matches()
}

@Composable
fun DisplayTeamsTable(teams: Map<String, MutableList<Player>>) {
    val columns = teams.size
    val rows = teams.maxOf { it.value.size }

    LazyTable(
        dimensions = lazyTableDimensions(150.dp, 48.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = columns,
            layoutInfo = {
                LazyTableItem(
                    column = it % columns,
                    row = 0,
                )
            },
        ) { index ->
            val teamName = teams.keys.elementAt(index)
            HeaderCell(teamName)
        }

        items(
            count = columns * rows,
            layoutInfo = {
                LazyTableItem(
                    column = it % columns,
                    row = (it / columns) + 1
                )
            },
        ) { index ->
            val teamIndex = index % columns
            val rowIndex = (index / columns)

            val teamName = teams.keys.elementAt(teamIndex)
            val players = teams[teamName] ?: emptyList()

            if (rowIndex < players.size) {
                val player = players[rowIndex]
                Cell(player.firstName, player.lastName)
            } else {
                Cell("", "")
            }
        }
    }
}

@Composable
private fun HeaderCell(teamName: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .border(Dp.Hairline, MaterialTheme.colorScheme.onPrimary)
    ) {
        Text(text = teamName, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun Cell(firstName: String, lastName: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface)
    ) {
        Text(text = "$firstName $lastName", style = MaterialTheme.typography.bodyMedium)
    }
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
        MainAppScreen()
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