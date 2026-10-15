package io.lb.astormemory.game.data.resources

import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.platform.utils.MusicManager
import io.lb.astormemory.game.platform.utils.MusicManager.MusicTrack

private const val DEFAULT_VOLUME = 1.0f
private const val MENU_VOLUME = 0.7f
private const val INTENSE_GAME_VOLUME = 0.85f
private const val LAVENDER_VOLUME = 0.95f

/**
 * Provider to play the songs of the game, respecting the user's audio settings.
 *
 * @property musicManager The manager used to play the songs.
 * @property settingsDataSource The data source used to read the user's audio settings.
 */
class AstorMemorySongsProvider(
    private val musicManager: MusicManager,
    private val settingsDataSource: SettingsDataSource,
) {
    suspend fun playMenuSong() {
        playIfNotMuted(MusicTrack.TITLE_SCREEN, MENU_VOLUME)
    }

    suspend fun playHighScoreSong() {
        playIfNotMuted(MusicTrack.HIGHSCORES, MENU_VOLUME)
    }

    suspend fun playGameSong(amount: Int) {
        val (track, volume) = when {
            amount == 1 -> MusicTrack.VICTORY_ROAD to DEFAULT_VOLUME
            amount < 8 -> MusicTrack.WILD to DEFAULT_VOLUME
            amount >= 20 -> MusicTrack.FINAL_BATTLE to INTENSE_GAME_VOLUME
            amount >= 12 -> MusicTrack.GYM_LEADER to INTENSE_GAME_VOLUME
            else -> MusicTrack.TRAINER to INTENSE_GAME_VOLUME
        }
        playIfNotMuted(track, volume)
    }

    suspend fun playGameOverSong(score: Int, amount: Int) {
        val (track, volume) = when {
            score == 0 -> MusicTrack.LAVENDER to LAVENDER_VOLUME
            amount >= 20 -> MusicTrack.VICTORY_FINAL to DEFAULT_VOLUME
            else -> MusicTrack.GAMEOVER to DEFAULT_VOLUME
        }
        playIfNotMuted(track, volume)
    }

    fun pauseSongs() {
        musicManager.pauseAll()
    }

    suspend fun resumeSong() {
        if (!isMuted()) musicManager.resumeLastSong()
    }

    fun release() {
        musicManager.release()
    }

    private suspend fun isMuted() = settingsDataSource.getSettings().isMuted

    private suspend fun playIfNotMuted(track: MusicTrack, volume: Float) {
        if (isMuted()) {
            musicManager.pauseAll()
        } else {
            musicManager.play(track, volume)
        }
    }
}
