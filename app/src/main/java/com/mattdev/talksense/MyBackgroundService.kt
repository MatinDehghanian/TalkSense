package com.mattdev.talksense

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.app.Service
import androidx.core.app.NotificationCompat
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.devices.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchProcessor
import java.util.*

class MyBackgroundService : Service() {
    private var previousVolume: Int = 0
    private var silenceTimer: Timer? = null

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val TAG = "MyBackgroundService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        showNotification()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startListening() {
        val audioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100, 1024, 0)
        val pitchProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100.0, 1024
        ) { pitch: Float ->
            if (pitch > 75 && pitch < 300) {
                reduceVolume()
            }
        }
        audioDispatcher.addAudioProcessor(pitchProcessor)
        Thread(Runnable { audioDispatcher.run() }).start()
    }

    private fun reduceVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume / 2, 0)
        startSilenceTimer()
    }

    private fun restoreVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0)
    }

    private fun startSilenceTimer() {
        silenceTimer?.cancel()
        silenceTimer = Timer()
        silenceTimer?.schedule(object : TimerTask() {
            override fun run() {
                restoreVolume()
            }
        }, 2000)
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Voice Monitor")
            .setContentText("Listening for conversations...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}