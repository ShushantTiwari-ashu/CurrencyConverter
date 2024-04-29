package dev.shushant.dashboard.utils

import org.junit.Test

class ExtensionsKtTest {
    @Test
    fun `test get flag emoji based on currency code`() {
        assert(getFlagEmoji("USD") == "\uD83C\uDDFA\uD83C\uDDF8")
    }
}
