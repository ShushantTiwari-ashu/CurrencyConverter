package dev.shushant.data.utils

import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ConnectivityManagerNetworkMonitorTest {
    private val networkMonitor = TestNetworkMonitor()

    @Test
    fun `test is online`() =
        runTest {
            networkMonitor.setConnected(true)
            assert(networkMonitor.isOnline.first())
        }

    @Test
    fun `test is offline`() =
        runTest {
            networkMonitor.setConnected(false)
            assertFalse(networkMonitor.isOnline.first())
        }
}
