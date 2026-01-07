package io.lb.astormemory

import androidx.compose.ui.window.ComposeUIViewController
import org.jetbrains.compose.resources.InternalResourceApi

@InternalResourceApi
fun MainViewController() = ComposeUIViewController { App() }