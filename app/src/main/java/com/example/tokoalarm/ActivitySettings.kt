package com.example.tokoalarm

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivitySettings :AppCompatActivity() {

    private val nada  : List<Tone> = listOf(
        Tone("tone 1", R.raw.suara_satu),
        Tone("tone 2", R.raw.suara_dua),
        Tone("tone 3", R.raw.suara_tiga),
        Tone("tone 4", R.raw.suara_empat)
    )

    private lateinit var mediaPlayer :MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<ImageView>(R.id.iconMenu)
            .setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        findViewById<TextView>(R.id.textMenu)
            .text = getString(R.string.setting)

        val dialogConfirm = DialogConfirm(this)
        val containerView = findViewById<RecyclerView>(R.id.container)
        containerView.layoutManager = LinearLayoutManager(this)
        containerView.adapter = AdapterListNada(nada) {
            val position = containerView.getChildAdapterPosition(it)
            mediaPlayer = MediaPlayer.create(this, nada[position].value)
            mediaPlayer.start()
            dialogConfirm.show(
                "${getString(R.string.ubah_suara)} ${nada[position].name}",
                R.raw.lottie_music, {
                    mediaPlayer.stop()

                    }, {
                mediaPlayer.stop()
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}