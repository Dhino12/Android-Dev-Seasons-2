package com.example.myapplication

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        btn_play.setOnClickListener(this)
        btn_stop.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        /*
        * Perintah prepareAsync() berfungsi untuk menyiapkan MediaPlayer jika belum disiapkan atau diperbarui.
        * Ketika prepareAsync() dijalankan maka proses ini bersifat asynchronous.
        * Ini untuk memastikan aplikasi tetap berjalan secara responsif.
        * Sebenarnya, mediaplayer menyediakan metode prepare() yang berjalan secara synchronous.
        * Tetapi untuk proses yang memakan waktu lama, Anda sebaiknya menggunakan prepareAsync().
        * */
        when (view.id) {
            R.id.btn_play -> {
                if (!isReady) {
                    mMediaPlayer?.prepareAsync()
                } else {
                    if (mMediaPlayer?.isPlaying() as Boolean) {
                        mMediaPlayer?.pause()
                    } else {
                        mMediaPlayer?.start()
                    }
                }
            }
            R.id.btn_stop -> {
                if (mMediaPlayer?.isPlaying() as Boolean || isReady) {
                    mMediaPlayer?.stop()
                    isReady = false
                    //Fungsi mMediaPlayer.stop() digunakan untuk menghentikan MediaPlayer yang sedang berjalan (play).
                }
            }
            else -> {
            }
        }
    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attribute = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        } else {
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        //Lihat pada bagian di atas, kode tersebut berguna untuk memperbarui MediaPlayer.

        val afd = applicationContext.resources.openRawResourceFd(R.raw.song_starwish)
        try {
            //Kode setDataSource berfungsi untuk memasukkan detail informasi dari asset atau musik yang akan diputar.
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //Kode di atas berfungsi untuk mengambil file suara dalam folder R.raw, kemudian di masukkan ke dalam MediaPlayer.

        mMediaPlayer?.setOnPreparedListener {
            //Kemudian ketika MediaPlayer sudah disiapkan,
            // maka akan menjalankan musik atau asset yang sudah disiapkan sebelumnya dengan perintah start().
            isReady = true
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra -> false }
    }
}