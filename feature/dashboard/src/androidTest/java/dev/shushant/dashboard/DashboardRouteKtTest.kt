package dev.shushant.dashboard

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shushant.dashboard.utils.getFlagEmoji
import dev.shushant.model.Currencies
import dev.shushant.test.HiltComponentActivity
import dev.shushant.test.convertCurrency
import dev.shushant.test.currencies
import dev.shushant.test.exchangeRates
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardRouteKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Test
    fun testDashboardScreen_with_Loader() {
        composeTestRule.setContent {
            DashboardScreen(
                modifier = Modifier,
                dashboardState = DashboardUIState(isLoading = true),
                updateBaseCurrency = { _, _ -> },
            )
        }
        composeTestRule.onNodeWithTag("Loader").assertExists()
    }

    @Test
    fun testDashboardScreen_with_amount_text_field_and_currency_chooser() {
        composeTestRule.setContent {
            DashboardScreen(
                modifier = Modifier,
                dashboardState = DashboardUIState(),
                updateBaseCurrency = { _, _ -> },
            )
        }
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.enter_amount_to_convert))
            .assertExists()
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.choose_currency))
            .assertExists()
    }

    @Test
    fun test_currency_chooser_to_select_currency() {
        val dashboardState =
            DashboardUIState(
                currencies =
                    Currencies(
                        items =
                            currencies.map {
                                Currencies.Item(
                                    it.key,
                                    it.value,
                                )
                            },
                    ),
            )
        val currencyCode = dashboardState.currencies?.items?.first()?.currencyCode ?: ""
        val currencyName = dashboardState.currencies?.items?.first()?.currencyName ?: ""
        composeTestRule.setContent {
            DashboardScreen(
                modifier = Modifier,
                dashboardState = dashboardState,
                updateBaseCurrency = { _, _ -> },
            )
        }
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.enter_amount_to_convert))
            .assertExists()
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.choose_currency))
            .assertExists().performClick()
        composeTestRule.onNodeWithTag("Currency_Chooser").assertExists().performClick()
        composeTestRule.onNodeWithTag(currencyName).assertExists().performClick()

        composeTestRule.onNodeWithTag(
            getFlagEmoji(currencyCode).plus(currencyCode),
            useUnmergedTree = true,
        ).assertExists()
    }

    @Test
    fun test_exchange_rates_ui() {
        val dashboardState =
            DashboardUIState(
                currencies =
                    Currencies(
                        items =
                            currencies.map {
                                Currencies.Item(
                                    it.key,
                                    it.value,
                                )
                            },
                    ),
                exchangeRates = exchangeRates,
                convertedAmount = convertCurrency("USD", 123.toBigDecimal()),
            )
        val currencyName = dashboardState.currencies?.items?.first()?.currencyName ?: ""

        composeTestRule.setContent {
            DashboardScreen(
                modifier = Modifier,
                dashboardState = dashboardState,
                updateBaseCurrency = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.enter_amount_to_convert))
            .assertExists().performTextInput("123")
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.choose_currency))
            .assertExists().performClick()
        composeTestRule.onNodeWithTag("Currency_Chooser").assertExists().performClick()
        composeTestRule.onNodeWithTag(currencyName).assertExists().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Converted_currencies").assertExists().performScrollToNode(
            hasTestTag(
                dashboardState.convertedAmount.let {
                    it.keys.first().plus(it.values.first())
                },
            ),
        ).assertExists()
    }
}
