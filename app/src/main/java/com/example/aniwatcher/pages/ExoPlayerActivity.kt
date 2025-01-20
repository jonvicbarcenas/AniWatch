package com.example.aniwatcher.pages

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aniwatcher.R
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class ExoPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)

        playerView = findViewById(R.id.player_view)
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse("https://g4fv.biananset.net/_v7/8ff4583ec989bfd91ed9f86b8ac537b6d4b1e0c5a2956301e856a768b38c60ef436e9b9a3d39225dbe7870795370e0e247aac08fb0767d57e038de60f5ce7d4ee5143ef59d2c623df118bf57ec96ed94b3c5fdceb6ab43b6edd905a29aa06c30c838277878ade777e32f3c42685418a6415e5f7cf6d21097e57fa54cda597d48/master.m3u8"))
            .setSubtitleConfigurations(
                listOf(
                    MediaItem.SubtitleConfiguration.Builder(Uri.parse("https://s.megastatics.com/subtitle/32d724910503a79386c4c51ab77e7681/eng-2.vtt"))
                        .setMimeType("text/vtt")
                        .setLanguage("en")
                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                        .build()
                )
            )
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}