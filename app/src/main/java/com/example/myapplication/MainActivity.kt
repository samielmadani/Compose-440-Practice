package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                MyApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var fromText by rememberSaveable { mutableStateOf("") }
    var toText by rememberSaveable { mutableStateOf("") }
    var multilineText by rememberSaveable { mutableStateOf("") }

    // Navigation setup
    val navController = rememberNavController()




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sort of Game",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        content = {
            // NavHost for navigation
            NavHost(
                navController = navController,
                startDestination = "main_screen"
            ) {
                composable("main_screen") {

                    BackHandler(onBack = {
                        // Do nothing when the back button is pressed on this screen
                        // If you want to perform some action, you can replace the comment above with your desired action.
                    })

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 50.dp)
                    ) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column (
                                    horizontalAlignment = Alignment.End
                                ) {
                                    // Input for "From"
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(text = "From")
                                        TextField(
                                            value = fromText,
                                            onValueChange = { fromText = it },
                                            maxLines = 1,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .width(250.dp)
                                        )
                                    }

                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(text = "To")
                                        TextField(
                                            value = toText,
                                            onValueChange = { toText = it },
                                            maxLines = 1,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .width(250.dp)
                                        )
                                    }


                                    // Input for "To"


                                    // Multiline text input
                                    TextField(
                                        value = multilineText,
                                        onValueChange = { multilineText = it },
                                        label = { Text("Enter items in sorted order, one item per line") },
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .height(500.dp)
                                            .fillMaxWidth(),
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = Color.Transparent
                                        )

                                    )
                                }

                                // Buttons at the bottom
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = {
                                            fromText = ""
                                            toText = ""
                                            multilineText = ""
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Clear")
                                    }

                                    Spacer(modifier = Modifier.width(150.dp)) // Gap between the buttons

                                    Button(
                                        onClick = {
                                            // Navigate to the new screen
                                            navController.navigate("new_game_screen")
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("New game")
                                    }
                                }
                            }
                        }
                    }
                }
                composable("new_game_screen") {
                    // Navigate to the new game screen
                    NewGameScreen(navController, fromText, toText, multilineText.split('\n'))
                }
            }
        }
    )
}


@Composable
fun NewGameScreen(navController: NavHostController, fromText: String, toText: String, multilineItems: List<String>) {
    var itemList by rememberSaveable { mutableStateOf(multilineItems) }
    var isOrdered = rememberSaveable{ mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display "From" at the top
        Text(fromText, modifier = Modifier.padding(top = 80.dp))

        // Display list items with up and down arrow buttons
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(itemList.size) { index ->
                val item = itemList[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item)
                    Row {
                        // Up arrow button
                        IconButton(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, shape = CircleShape),
                            onClick = {
                                if (index > 0) {
                                    // Swap the item with the one above it
                                    val temp = itemList[index]
                                    itemList = itemList.toMutableList().apply {
                                        set(index, itemList[index - 1])
                                        set(index - 1, temp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.rotate(90f),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Up"
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp)) // Gap between the buttons

                        // Down arrow button
                        IconButton(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, shape = CircleShape),
                            onClick = {
                                if (index < itemList.size - 1) {
                                    // Swap the item with the one below it
                                    val temp = itemList[index]
                                    itemList = itemList.toMutableList().apply {
                                        set(index, itemList[index + 1])
                                        set(index + 1, temp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.rotate(-90f),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Down"
                            )
                        }
                    }
                }
            }
        }

        // Display "To" at the bottom
        Text(toText, modifier = Modifier.padding(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    // Check if the items are ordered alphabetically
                    isOrdered.value = isOrderedAlphabetically(itemList)
                    // Show the dialog
                    showDialog = true
                },
            ) {
                Text("CHECK")
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = {
                    if (isOrdered.value) {
                        // If sorted correctly, navigate back to the main screen
                        showDialog = false
                        navController.navigate("main_screen")
                    } else {
                        showDialog = false
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White)

                ) {

                    Column (
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween,

                        ){
                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                if (isOrdered.value) "You sorted these Correctly. Nice work!" else "Uh oh. That's not the correct order.",
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Black

                            )
                        }



                        Button(
                            onClick = {
                                if (isOrdered.value) {
                                    // If sorted correctly, navigate back to the main screen
                                    showDialog = false
                                    navController.navigate("main_screen")
                                } else {
                                    showDialog = false
                                }
                            }
                        ) {
                            Text(
                                if (isOrdered.value) "OKAY" else "RETRY",
                                color = Color.White
                            )
                        }

                    }

                }
            }
        }
    }
}

fun isOrderedAlphabetically(items: List<String>): Boolean {
    val sortedItems = items.sorted()
    return items == sortedItems
}



