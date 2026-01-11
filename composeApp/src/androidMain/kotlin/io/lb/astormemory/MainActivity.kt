package io.lb.astormemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.lb.astormemory.app.AstorMemoryApp
import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.utils.MusicManager
import io.lb.astormemory.music.MusicLifecycleObserver
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.android.ext.android.inject

@InternalResourceApi
class MainActivity : ComponentActivity() {
    private val musicManager: MusicManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(MusicLifecycleObserver(musicManager))
        setContent {
            AstorMemoryApp(
                onQuitApp = {
                    finishAffinity()
                },
            )
        }
    }
}

@InternalResourceApi
@Preview
@Composable
fun AstorMemoryAppAndroidPreview() {
    AstorMemoryApp({})
}