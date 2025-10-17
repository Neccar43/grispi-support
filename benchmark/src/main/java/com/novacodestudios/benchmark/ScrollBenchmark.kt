package com.novacodestudios.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollMessageList() {
        benchmarkRule.measureRepeated(
            packageName = BenchmarkUtil.PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            iterations = BenchmarkUtil.DEFAULT_ITERATION,
            setupBlock = {
                pressHome()
                startActivityAndWait()
                device.wait(
                    Until.gone(By.res("loading_circle")),
                    5_000
                )

                val benchmarkTicket = device.findObject(By.text("Ürün iadesi hakkında bilgi"))
                benchmarkTicket.click()

                device.wait(
                    Until.hasObject(By.text("Son mesaj")),
                    5_000
                )

                device.waitForIdle()
            }
        ) {
            val list = device.findObject(By.res("conversation_list"))
            while (!device.hasObject(By.text("İlk mesaj"))) {
                list.swipe(Direction.DOWN, 0.7f)
                device.waitForIdle()
            }
            killProcess()
        }
    }

//    @RequiresApi(Build.VERSION_CODES.Q)
//    @OptIn(ExperimentalMetricApi::class)
//    @Test
//    fun powerMetric(){
//        val categories = mapOf(
//            PowerCategory.CPU to PowerCategoryDisplayLevel.TOTAL,
//            PowerCategory.DISPLAY to PowerCategoryDisplayLevel.TOTAL,
//            PowerCategory.GPU to PowerCategoryDisplayLevel.TOTAL,
//        )
//        val powerMetric =PowerMetric(type = PowerMetric.Type.Power(powerCategories = categories))
//        val memoryMetric = MemoryUsageMetric(
//            mode = MemoryUsageMetric.Mode.Max,
//            subMetrics = listOf(
//                MemoryUsageMetric.SubMetric.HeapSize,
//                MemoryUsageMetric.SubMetric.RssAnon,
//                MemoryUsageMetric.SubMetric.Gpu
//            )
//        )
//        benchmarkRule.measureRepeated(
//            packageName = BenchmarkUtil.PACKAGE_NAME,
//            metrics = listOf(powerMetric, memoryMetric),
//            iterations = 1,
//            setupBlock = {
//                pressHome()
//            }
//        ) {
//            startActivityAndWait()
//
//            val benchmarkTicket = device.findObject(By.text("Ürün teslim edilmedi"))
//            benchmarkTicket.click()
//
//            val fakeReplayText = device.findObject(By.res("fake_reply_text"))
//            fakeReplayText.click()
//
//            val replayText = device.findObject(By.res("reply_text"))
//            replayText.text = "Merhaba"
//            val sendButton = device.findObject(By.res("send_button"))
//            sendButton.click()
//            device.waitForIdle()
//            //killProcess()
//
//        }
//    }
}