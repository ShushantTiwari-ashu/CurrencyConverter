package dev.shushant.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shushant.dashboard.utils.getFlagEmoji
import kotlinx.coroutines.launch

@Composable
fun DashboardRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Unit = { _: String, _: String? -> },
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by dashboardViewModel.state.collectAsStateWithLifecycle()
    val okText = stringResource(id = R.string.ok)
    LaunchedEffect(key1 = uiState) {
        (uiState.error).takeIf { it != null }?.let {
            onShowSnackbar.invoke(it, okText)
        }
    }
    DashboardScreen(
        dashboardState = uiState,
        modifier = modifier,
        updateBaseCurrency = dashboardViewModel::updateBaseCurrency,
    )
}

@Composable
fun DashboardScreen(
    modifier: Modifier,
    dashboardState: DashboardUIState,
    updateBaseCurrency: (String, String) -> Unit,
) {
    var amount by rememberSaveable {
        mutableStateOf("")
    }
    val gridState = rememberLazyGridState()
    val currencyState = rememberLazyListState()
    var expanded by remember { mutableStateOf(false) }
    val placeHolder = stringResource(id = R.string.choose_currency)
    var selectedItem by remember {
        mutableStateOf(
            placeHolder,
        )
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = amount) {
        if (selectedItem != placeHolder) {
            updateBaseCurrency.invoke(selectedItem.takeLast(3), amount)
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
            TextField(
                value = amount,
                onValueChange = { s ->
                    val sanitizedValue = s.filter { it.isDigit() || it == '.' }
                    amount = sanitizedValue
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(R.string.enter_amount_to_convert)),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                placeholder = { Text(text = stringResource(R.string.enter_amount_to_convert)) },
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag(placeHolder),
            ) {
                TextButton(
                    onClick = {
                        expanded = expanded.not()
                        coroutineScope.launch {
                            val item =
                                dashboardState.currencies?.items?.find {
                                    it.currencyCode == selectedItem.takeLast(3)
                                }
                            currencyState.scrollToItem(
                                dashboardState.currencies?.items?.indexOf(item).takeIf { it != -1 }
                                    ?: 0,
                            )
                        }
                    },
                    modifier =
                        Modifier
                            .padding(vertical = 10.dp)
                            .align(Alignment.CenterEnd)
                            .testTag("Currency_Chooser")
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                            ),
                ) {
                    Text(
                        text = selectedItem,
                        modifier = Modifier.testTag(selectedItem),
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }
            Box {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = gridState,
                    modifier = Modifier.testTag("Converted_currencies"),
                ) {
                    dashboardState.convertedAmount.let { convertedAmount ->
                        items(
                            convertedAmount.size,
                            key = {
                                convertedAmount.keys.elementAt(it)
                            },
                        ) { index ->
                            RoundedBoxWithText(
                                currencyCode =
                                    convertedAmount.keys.elementAt(
                                        index,
                                    ),
                                decimalNumber = convertedAmount.values.elementAt(index),
                            )
                        }
                    }
                }
                if (expanded) {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                                .background(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                ),
                        state = currencyState,
                    ) {
                        items(dashboardState.currencies?.items ?: emptyList()) { item ->
                            DropdownMenuItem(onClick = {
                                expanded = expanded.not()
                                selectedItem =
                                    getFlagEmoji(item.currencyCode).plus(item.currencyCode)
                                updateBaseCurrency.invoke(item.currencyCode, amount)
                            }, text = {
                                Text(text = "${getFlagEmoji(item.currencyCode)} ${item.currencyName}")
                            }, modifier = Modifier.testTag(item.currencyName))
                        }
                    }
                }
            }
        }
        if (dashboardState.isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .testTag("Loader"),
            )
        }
    }
}
