package com.compose.androidtoanime.Utils

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class AppUtils {

    companion object {
        val TAG_D = "debug_response"
        const val TABLE_IMAGE = "table_photos"
        const val DATABASE_NAME = "db_name"
        const val MAX_PHOTO = 20
        lateinit var bitmap: Bitmap


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

                Log.d(TAG_D, "path ==== $uri")
                Log.d(TAG_D, "path ${filePath} +${columnIndex}")
                bitmap = compressImage(filePath)!!
                cursor.close()
                return filePath
            }
            cursor?.close()
            throw Exception("File not found")
        }

         fun hasStoragePermission(context: Context): Boolean {
            return (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED)
        }

        fun compressImage(imageFilePath: String): Bitmap? {
            // Get the size of the image
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFilePath, options)

            // Compute the sample size to fit the image within the desired size
            val maxWidth = 1000
            val maxHeight = 1000
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
            image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            val imageData = byteArrayOutputStream.toByteArray()

            // Return the compressed image
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        }

        fun generateNewPath(): String {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            return "$year-$month-$day-$hour-$minute-$second"
        }


        fun saveImage(context: Context, bitmap: Bitmap?) {
            val filename = "${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }
            fos?.use {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
            }
        }

        fun toBitmap(context: Context, string: String): Bitmap? {
            val url: URL = mStringToURL(string)!!
            val connection: HttpURLConnection?
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                return BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
            return null
        }

        fun mStringToURL(string: String): URL? {
            try {
                return URL(string)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return null
        }

        fun sharePalette(context: Context, bitmap: Bitmap) {
            val bitmapPath = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "palette",
                "share palette"
            )
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            context.startActivity(Intent.createChooser(intent, "Share"))
        }


    }


}