package com.example.composeapplication.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.compose.ui.unit.dp
import com.example.composeapplication.R

/**
 *Time 2023/6/28
 *Author gaojinliang
 *Describtion:
 */

data class MusicData(
    val id: Int = 0,
    val name: String? = null,
    val singer: String? = null,
    val album: Bitmap? = null,
    val path: String? = null,
    val duration: Long = 0,
    val size: Long = 0,
    val uri: Uri,
    val title: String?
)

object MetadataReaderUtils {
    fun getMusicDataList(context: Context): List<MusicData> {
        val list = mutableListOf<MusicData>()
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                val singer =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val duration =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val size =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                val uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                val musicData = MusicData(
                    id = id.toInt(),
                    name = name,
                    singer = singer,
                    getAlbumArt(context, albumId),
                    path,
                    duration.toLong(),
                    size.toLong(),
                    uri,
                    title = title
                )
                list.add(musicData)
            }
            cursor.close()
        }
        return list
    }

    @SuppressLint("NewApi")
    fun getAlbumArt(context: Context, albumId: Long): Bitmap? {
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId
        )
        return try {
            context.contentResolver.loadThumbnail(
                contentUri, Size(120, 120), null
            )
        } catch (e: Exception) {
            var albumPicture =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_bruce)
            val width = albumPicture.width
            val height = albumPicture.height
            // 创建操作图片用的Matrix对象
            val matrix = Matrix()
            // 计算缩放比例
            val sx = (120.dp.value/ width)
            val sy = (120.dp.value / height)
            // 设置缩放比例
            matrix.postScale(sx, sy)
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false)
            return albumPicture
        }
    }

    fun getAlumPicture(context: Context, path: Uri): Bitmap {
        // 歌曲检索
        val mediaMetadataRetriever = MediaMetadataRetriever()
        // 设置数据源
        mediaMetadataRetriever.setDataSource(context, path)

        // 获取图片数据
        val embeddedPicture = mediaMetadataRetriever.embeddedPicture
        var albumPicture: Bitmap?
        if (embeddedPicture != null) {
            // 获取bitmap 对象
            albumPicture =
                BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
            val width = albumPicture.width
            val height = albumPicture.height
            // 创建操作图片用的Matrix对象
            val matrix = Matrix()
            // 计算缩放比例
            val sx = (120F / width)
            val sy = (120F / height)
            // 设置缩放比例
            matrix.postScale(sx, sy)
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false)
        } else {
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            albumPicture = BitmapFactory.decodeResource(context.resources, R.drawable.ic_bruce)
            val width = albumPicture.width
            val height = albumPicture.height
            // 创建操作图片用的Matrix对象
            val matrix = Matrix()
            // 计算缩放比例
            val sx = (120F / width)
            val sy = (120F / height)
            // 设置缩放比例
            matrix.postScale(sx, sy)
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false)
        }
        return albumPicture
    }

}