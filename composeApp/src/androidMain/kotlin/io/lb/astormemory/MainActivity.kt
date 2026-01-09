package io.lb.astormemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.InternalResourceApi

@InternalResourceApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AstorMemoryApp(
                onQuitApp = {
                    finishAffinity()
                }
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