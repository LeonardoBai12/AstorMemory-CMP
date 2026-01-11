package io.lb.astormemory.music

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.lb.astormemory.game.platform.utils.MusicManager

class MusicLifecycleObserver(
    private val musicManager: MusicManager
) : DefaultLifecycleObserver {
    
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        musicManager.pauseAll()
    }
    
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        musicManager.resumeLastSong()
    }
    
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        musicManager.release()
    }
}