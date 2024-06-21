/**
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.soundchecker.utils

import android.media.AudioFormat
import android.util.Log
import com.google.android.soundchecker.mediacodec.AudioEncoderSource
import java.util.Arrays
import kotlin.math.sin

class WaveFileSource : AudioSource() {

    public var waveFileReader : WaveFileReader? = null

    override fun pull(buffer: ByteArray, numFrames: Int): Int {
        val floatArray = FloatArray(numFrames * getChannelCount())
        val framesRead = pull(floatArray, numFrames)
        //Log.d(TAG, "floatArray: " + Arrays.toString(floatArray))
        floatArrayToByteArray(floatArray, buffer)
        //Log.d(TAG, "byteArray: " + Arrays.toString(buffer))
        return framesRead
    }

    // Pull I16 bytes
    override fun pull(numBytes: Int, buffer: ByteArray): Int {
        val int16SizeBytes = 2
        val floatArray = FloatArray(numBytes / Float.SIZE_BYTES * int16SizeBytes)
        val framesRead = pull(floatArray, numBytes / Float.SIZE_BYTES *
                int16SizeBytes / getChannelCount())
        floatArrayToI16ByteArray(floatArray, buffer)
        //Log.d(TAG, "byteArray: " + Arrays.toString(buffer))
        return framesRead
    }

    override fun pull(buffer: FloatArray, numFrames: Int): Int {
        val framesRead = waveFileReader!!.getDataFloat(buffer, numFrames)
        //Log.d(TAG, "numFrames: " + numFrames)
        //Log.d(TAG, "framesRead: " + framesRead)
        return framesRead
    }

    companion object {
        private const val TAG = "WaveFileSource"
    }
}
