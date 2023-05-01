package com.ignotusvia.speechbuddy.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import javax.inject.Inject

class TFLiteManager @Inject constructor(
    @ApplicationContext private val context: Context
    ) {
    private lateinit var interpreter: Interpreter

    fun loadModel(modelFile: String) {
        val modelBuffer = FileUtil.loadMappedFile(context, modelFile)
        val options = Interpreter.Options().apply {
            setNumThreads(4) // Set the number of threads for the interpreter.
        }
        interpreter = Interpreter(modelBuffer, options)
    }

    fun runModel(inputArray: Array<FloatArray>, outputArray: Array<FloatArray>) {
        interpreter.run(inputArray, outputArray)
    }

    fun closeModel() {
        interpreter.close()
    }

    val inputTensor get() = interpreter.getInputTensor(0)

    val outputTensor get() = interpreter.getOutputTensor(0)

}



