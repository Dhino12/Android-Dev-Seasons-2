package com.example.myapplication

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.lang.ref.WeakReference

class MediaService : Service() , MediaPlayerCallback {
    private val TAG = MediaService::class.java.simpleName
    private var mMediaPlayer : MediaPlayer? = null
    private var isReady:Boolean = false
    private val mMessenger = Messenger(IncomingHandler(this))

    companion object{
        const val ACTION_CREATE = "com.example.mymediaplayer.mediaservice.create"
        const val ACTION_DESTROY = "com.example.mymediaplayer.mediaservice.destroy"
        const val PLAY =  0
        const val STOP = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if(action != null){
            //Ketika nilai dari action adalah ACTION_CREATE, maka metode init() akan dijalankan.
            // Dan ketika nilai dari action adalah ACTION_DESTROY, maka metode stopSelf() akan dijalan.
            // Metode stopSelf() berfungsi untuk menghentikan service.
            when(action){
                ACTION_CREATE ->
                    if(mMediaPlayer == null){
                        init()
                    }
                ACTION_DESTROY ->
                    if(mMediaPlayer?.isPlaying() as Boolean){
                        Log.d(TAG, "onDestroy: ")
                        onStop()
                        stopSelf()
                    }
                else -> {
                    init()
                }
            }
        }
        Log.d(TAG, "onStartCommand:")
        return flags
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG,"onBind")
        return mMessenger.binder
    }

    // membuat handle ketika button pada MainActivity diklik.
    /**
     * Method incomingHandler sebagai handler untuk aksi dari onklik button di MainActivity
     */
    internal class IncomingHandler(playerCallback: MediaPlayerCallback): Handler(){
        private val mediaPlayerCallbackWeakReference: WeakReference<MediaPlayerCallback> = WeakReference(playerCallback)

        override fun handleMessage(msg: Message) {
            when(msg.what){
                //Dengan bantuan WeakReference sebagai penerima dari pesan callback MainActivity.
                // Jadi ketika button PLAY ditekan maka akan menjalankan fungsi dari onPlay:
                PLAY -> mediaPlayerCallbackWeakReference.get()?.onPlay()
                STOP -> mediaPlayerCallbackWeakReference.get()?.onStop()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onPlay() {
        if(!isReady){
            mMediaPlayer?.prepareAsync()
        }else{
            if(mMediaPlayer?.isPlaying() as Boolean){
                mMediaPlayer?.pause()
            }else{
                mMediaPlayer?.start()
                showNotif() // Notifikasi
            }
        }
    }

    override fun onStop() {
        if(mMediaPlayer?.isPlaying() as Boolean || isReady){
            mMediaPlayer?.stop()
            isReady = false
            stopNotify() // Notifikasi
        }
    }

    private fun init(){
        mMediaPlayer = MediaPlayer()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        }else{
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        val afd = applicationContext.resources.openRawResourceFd(R.raw.song_starwish)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        }catch (e: IOException){
            e.printStackTrace()
        }
        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
            showNotif() // Notifikasi
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra -> false}
    }

    //==============================================================================================
    //tambahkan notifikasi ketika service sedang berjalan
    //==============================================================================================
    //ForegroundService yang digunakan untuk handle notifikasi ketika service sedang berjalan
    //----------------------------------------------------------------------------------------------
    private fun showNotif(){
        val CHANNEL_DEFAULT_IMPORTANCE = "Channel_Test"
        val ONGOING_NOTIFICATION = 1

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        // Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT. Flags tersebut berfungsi untuk
        // memanggil activity yang sudah ada tanpa membuat activity baru dan menampilkannya.

        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
        val notification = NotificationCompat.Builder(this,CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle("TES1")
            .setContentText("TES2")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setTicker("TES3")
            .build()

        createChannel(CHANNEL_DEFAULT_IMPORTANCE)

        //Untuk menjalankan ForegroundService, perintah yang digunakan adalah sebagai berikut:
        startForeground(ONGOING_NOTIFICATION,notification)
    }

    private fun createChannel(CHANNEL_ID:String){
        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"Battery",NotificationManager.IMPORTANCE_DEFAULT)
            channel.setShowBadge(false)
            channel.setSound(null,null)
            mNotificationManager.createNotificationChannel(channel)
        }
    }
    private fun stopNotify(){
        //Dan untuk menghentikannya menggunakan kode berikut:
        stopForeground(false)
    }
    //Yang perlu diingat ketika menjalankan ForegroundService adalah menambahkan permission di AndroidManifest.
    //==============================================================================================
}
