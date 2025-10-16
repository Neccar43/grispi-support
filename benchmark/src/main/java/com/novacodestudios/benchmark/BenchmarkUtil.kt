package com.novacodestudios.benchmark

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric

object BenchmarkUtil {
    const val DEFAULT_ITERATION = 20

    //jit derlemesi için geçen süre
    @OptIn(ExperimentalMetricApi::class)
    val jitCompilationMetric = TraceSectionMetric("JIT Compiling %")

    //sınıf başlatma için geçen süre
    @OptIn(ExperimentalMetricApi::class)
    val classInitMetric = TraceSectionMetric("L%/%;")

    @OptIn(ExperimentalMetricApi::class)
    val allMetrics = listOf(StartupTimingMetric(), jitCompilationMetric, classInitMetric)

    const val PACKAGE_NAME = "com.novacodestudios.grispisupport"
}