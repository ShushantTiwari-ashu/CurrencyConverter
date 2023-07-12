package dev.shushant.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.shushant.dashboard.utils.getFlagEmoji

@Composable
fun RoundedBoxWithText(
    currencyCode: String,
    decimalNumber: Double
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
            .testTag(currencyCode.plus(decimalNumber))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getFlagEmoji(currencyCode),
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = currencyCode,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.testTag(currencyCode)
        )
        Text(
            text = decimalNumber.toString(),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp,
            modifier = Modifier
                .padding(8.dp)
                .testTag(decimalNumber.toString()),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}