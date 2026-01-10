package io.lb.astormemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.lb.astormemory.app.AstorMemoryApp
import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import org.jetbrains.compose.resources.InternalResourceApi

@InternalResourceApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val soundPlayer = AudioPlayerFactory(this)

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