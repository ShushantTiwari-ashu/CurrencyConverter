package dev.shushant.dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shushant.dashboard.utils.getFlagEmoji
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoundedBoxWithTextKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRoundedBoxWithText() {
        val currencyCode = "USD"
        val decimalNumber = 10.5

        composeTestRule.setContent {
            MaterialTheme {
                RoundedBoxWithText(currencyCode, decimalNumber)
            }
        }
        composeTestRule.onNodeWithText(getFlagEmoji(currencyCode)).assertIsDisplayed()
        composeTestRule.onNodeWithText(currencyCode).assertIsDisplayed()
            .assertTextEquals(currencyCode)
        composeTestRule.onNodeWithText(decimalNumber.toString()).assertIsDisplayed()
            .assertTextEquals(decimalNumber.toString())

        composeTestRule.onNodeWithTag(currencyCode).assertIsDisplayed()
        composeTestRule.onNodeWithTag(decimalNumber.toString()).assertIsDisplayed()
    }
}
