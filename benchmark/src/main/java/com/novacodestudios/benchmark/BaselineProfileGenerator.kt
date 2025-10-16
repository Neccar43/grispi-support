package com.novacodestudios.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = BenchmarkUtil.PACKAGE_NAME,
        profileBlock = {
            startActivityAndWait()
        }
    )
}