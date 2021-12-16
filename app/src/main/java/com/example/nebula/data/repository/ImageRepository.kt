package com.example.nebula.data.repository

import android.content.Context
import com.example.nebula.R
import com.example.nebula.data.model.ImageObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ImageRepository {

    /**
     * Async fetch the data from the source. Subscribe to the observable to get the actual results
     *
     * @param c context
     * @return RxJava Single observable which returns ArrayList<ImageObject>
     */
    fun fetchImages(c: Context): List<ImageObject> {
        /**
         * Read the contents of an InputStream into a string
         *
         * @param inputStream Inputstream to read
         * @return String with the read data
         */
        fun readTextFile(inputStream: InputStream): String {
            val outputStream = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            try {
                while (inputStream.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len)
                }
                outputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                throw Exception("Error Reading File")
            }
            return outputStream.toString()
        }

        val xmlFileInputStream: InputStream = c.resources.openRawResource(R.raw.data)

        val jsonString: String = readTextFile(xmlFileInputStream)

        val gson = Gson()
        val type = object : TypeToken<ArrayList<ImageObject>>() {}.type
        val images: ArrayList<ImageObject> = gson.fromJson(jsonString, type)

        /* Sort by date: Descending */
        images.sortWith { a: ImageObject, b: ImageObject -> b.date.compareTo(a.date) }
        return images
    }
}