package com.compose.androidtoanime.Utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AppUtils {

    companion object {
        val TAG_D = "debug_response"
        lateinit var bitmap: Bitmap

        fun getBitmapUri(context: Context, bitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String =
                MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
            return Uri.parse(path)
        }

        fun saveBitmapToFile(bitmap: Bitmap, file: File): File {
            // Create a file output stream to write the bitmap data to the file
            val fos = FileOutputStream(file)

            // Compress the bitmap to a JPEG image and write it to the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

            // Flush and close the output stream
            fos.flush()
            fos.close()

            // Return the file
            return file
        }

        fun queryImage(uri: Uri, context: Context): String {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val contentResolver: ContentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val filePath = cursor.getString(columnIndex)
                Log.d(TAG_D, "path ====")
                Log.d(TAG_D, "path ${filePath} +${columnIndex}")
                bitmap = compressImage(filePath)!!
                cursor.close()
                return filePath
            }
            cursor?.close()
            throw Exception("File not found")
        }


        fun compressImage(imageFilePath: String): Bitmap? {
            // Get the size of the image
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFilePath, options)

            // Compute the sample size to fit the image within the desired size
            val maxWidth = 400
            val maxHeight = 400
            var sampleSize = 1
            while ((options.outWidth / sampleSize) >= maxWidth || (options.outHeight / sampleSize) >= maxHeight) {
                sampleSize *= 2
            }
            options.inSampleSize = sampleSize

            // Decode the image with the sample size
            options.inJustDecodeBounds = false
            val image = BitmapFactory.decodeFile(imageFilePath, options)

            // Compress the image
            val byteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val imageData = byteArrayOutputStream.toByteArray()

            // Return the compressed image
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        }

    }
}