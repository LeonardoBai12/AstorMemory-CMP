package io.lb.astormemory.game.platform.utils

import io.lb.astormemory.game.platform.music.MusicPlayer

class MusicManager(private val createPlayer: (fileName: String) -> MusicPlayer) {
    
    private val players = mutableMapOf<MusicTrack, MusicPlayer>()
    private var currentTrack: MusicTrack? = null
    
    enum class MusicTrack(val fileName: String) {
        TITLE_SCREEN("title_screen.mp3"),
        WILD("wild.mp3"),
        TRAINER("trainer.mp3"),
        GYM_LEADER("gym_leader.mp3"),
        FINAL_BATTLE("final_battle.mp3"),
        HIGHSCORES("highscores_screen.mp3"),
        GAMEOVER("gameover_screen.mp3"),
        VICTORY_ROAD("victory_road.mp3"),
        LAVENDER("lavender.mp3"),
        VICTORY_FINAL("victory_final.mp3")
    }
    
    fun play(track: MusicTrack, volume: Float = 0.7f) {
        if (currentTrack != track) {
            currentTrack?.let { pauseAll() }
        }
        
        val player = players.getOrPut(track) {
            createPlayer(track.fileName)
        }
        
        player.play(volume)
        currentTrack = track
    }
    
    fun pauseAll() {
        players.values.forEach { it.pause() }
    }

    fun resumeLastSong() {
        currentTrack?.let {
            players[it]?.resume()
        }
    }
    
    fun stopAll() {
        players.values.forEach { it.stop() }
        currentTrack = null
    }
    
    fun release() {
        players.values.forEach { it.release() }
        players.clear()
        currentTrack = null
    }
}
