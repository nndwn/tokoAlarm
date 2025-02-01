package com.example.tokoalarm

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivitySettings :AppCompatActivity() {

    private var mediaPlayer :MediaPlayer? = null
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefManager = PrefManager(this)


        findViewById<ImageView>(R.id.iconMenu)
            .setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        findViewById<TextView>(R.id.textMenu)
            .text = getString(R.string.setting)

        val dialogConfirm = DialogConfirm(this)
        val containerView = findViewById<RecyclerView>(R.id.container)
        val nada = DataManual().nada
        containerView.layoutManager = LinearLayoutManager(this)
        containerView.adapter = AdapterListNada(nada) {
            val position = containerView.getChildAdapterPosition(it)
            mediaPlayer = MediaPlayer.create(this, nada[position].value)
            mediaPlayer?.start()
            dialogConfirm.show(
                "${getString(R.string.ubah_suara)} ${nada[position].name}",
                R.raw.lottie_music, {
                    prefManager.setTone(nada[position].name)
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                    }, {
                     mediaPlayer?.stop()
                    mediaPlayer?.release()
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}