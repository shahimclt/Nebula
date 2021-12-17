package com.example.nebula.data.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.nebula.R
import com.example.nebula.data.model.ImageObject
import java.io.File
import java.io.FileOutputStream

/**
 * Utility class to save an image to gallery
 */
object ImageDownloadUtil {

    /**
     * Saves the image to gallery. This methods tries to access the ImageObject.hdurl first. If that is not set, it falls back to ImageObject.url
     * The image is saved to public gallery
     *
     * @param c context
     * @param image ImageObject instance
     * @return RxKotlin observable which returns a reference to the saved file on success.
     */
    fun download(c: Context, image: ImageObject) {

            try {
                val listener = object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        throw java.lang.Exception()
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }


                }

                val imageUrl = if(image.hdUrl.isNullOrBlank()) image.url else image.hdUrl
                val futureBitmap = Glide.with(c)
                    .asBitmap()
                    .listener(listener)
                    .load(imageUrl)
                    .submit()
                val bitmap = futureBitmap.get()
                saveImageToGallery(c,bitmap)

            } catch (e: Exception) {
                throw java.lang.Exception("Failed loading image")
            }
    }

    private fun saveImageToGallery(c: Context, bitmap: Bitmap) {
        val directory: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val folder = File(directory,c.getString(R.string.app_name))
        if(!folder.isDirectory) folder.mkdir()
        val file = File(folder, "NB_" + System.currentTimeMillis() + ".jpg")

        val outputStream = FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)

        outputStream.flush()
        outputStream.fd.sync()
        outputStream.close()

        MediaScannerConnection.scanFile(c, arrayOf(file.absolutePath), null, null)

    }
}