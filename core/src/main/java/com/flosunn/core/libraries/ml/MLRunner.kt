package com.flosunn.core.libraries.ml

import android.app.Application
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.CompiledModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

enum class MLAccelerator {
    CPU,
    GPU,
    NPU,
    NNAPI;

    fun toAccelerator(): Accelerator {
        return when (this) {
            CPU -> Accelerator.CPU
            GPU -> Accelerator.GPU
            NPU -> Accelerator.NPU
            NNAPI -> throw UnsupportedOperationException("NNAPI is not supported yet")
        }
    }
}

// Generic MLRunner class that can be used for different types of models (e.g., float, int, etc.)
@Singleton
class MLRunner<T> @Inject constructor(
    private val application: Application,
) {

    private var runner: Runner<T>? = null
    private val singleThreadDispatcher = Dispatchers
        .IO
        .limitedParallelism(1, "MLRunner-SingleThread")

    suspend fun initialize(mlAccelerator: MLAccelerator) = withContext(singleThreadDispatcher) {
        val model = CompiledModel.create(
            application.assets,
            "alexa.tflite",
            CompiledModel.Options(mlAccelerator.toAccelerator())
        )

        runner = Runner<T>(model)
    }

    private class Runner<T>(
        private val model: CompiledModel,
    ) {

        private val inputBuffers = model.createInputBuffers()
        private val outputBuffers = model.createOutputBuffers()

        fun cleanup() {
            inputBuffers.forEach { it.close() }
            outputBuffers.forEach { it.close() }
            model.close()
        }

        fun runInference(input: FloatArray): FloatArray {

            return input
        }
    }
}