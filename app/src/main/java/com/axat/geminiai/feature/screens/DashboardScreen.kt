package com.axat.geminiai.feature.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.axat.geminiai.feature.state.SummarizeUiState
import com.axat.geminiai.feature.viewmodel.GenerativeViewModelFactory
import com.axat.geminiai.feature.viewmodel.SummarizeViewModel


@Composable
internal fun DashboardScreen(
    controller: NavHostController,
    summarizeViewModel: SummarizeViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val summarizeUiState by summarizeViewModel.uiState.collectAsState()

    SummarizeScreen(summarizeUiState, onSummarizeClicked = { inputText ->
        summarizeViewModel.summarizeStreaming(inputText)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SummarizeScreen(
    uiState: SummarizeUiState = SummarizeUiState.Loading,
    onSummarizeClicked: (String) -> Unit = {}
) {

    var textToSummarize by rememberSaveable { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Gemini AI Chat", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (uiState) {
                SummarizeUiState.Initial -> {
                    Box(modifier = Modifier.weight(1f, true).fillMaxSize()) { }
                }

                SummarizeUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f, true).fillMaxSize()
                            .padding(all = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SummarizeUiState.Success -> {

                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState())
                            .weight(1f, false)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Person Icon",
                                tint = Color.Black,
                                modifier = Modifier
                                    .requiredSize(28.dp)
                                    .drawBehind {
                                        drawCircle(color = Color.White)
                                    }
                            )
                            Text(
                                text = uiState.outputText,
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }

                }

                is SummarizeUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                OutlinedTextField(
                    value = textToSummarize,
                    label = { Text("Search") },
                    placeholder = { Text("Search") },
                    onValueChange = { textToSummarize = it },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(0.85f)
                        .background(color = Color.Transparent),
                )

                IconButton(onClick = {
                    if (textToSummarize.isNotBlank()) {
                        onSummarizeClicked(textToSummarize)
                    }
                }) {
                    Icon(
                        Icons.Outlined.Send,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = "Back button",
                    )
                }

            }

        }


    }

}