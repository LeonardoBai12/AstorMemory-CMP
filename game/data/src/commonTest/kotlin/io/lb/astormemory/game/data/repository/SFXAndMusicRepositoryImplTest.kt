package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.data.resources.AstorMemorySFXProvider
import io.lb.astormemory.game.data.resources.AstorMemorySongsProvider
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.music.MusicPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.MusicManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// AstorMemorySFXProvider and AstorMemorySongsProvider are concrete classes and cannot be mocked
// by MockMP (which only mocks interfaces). AudioPlayer, MusicPlayer and AppPreferences (interfaces)
// are mocked instead, and real providers are constructed around them.
@UsesMocks(AudioPlayer::class, MusicPlayer::class, AppPreferences::class)
class SFXAndMusicRepositoryImplTest {
    val mocker = Mocker()
    val audioPlayer: AudioPlayer = mocker.mock()
    val musicPlayer: MusicPlayer = mocker.mock()
    val prefs: AppPreferences = mocker.mock()
    val requestedFiles = mutableListOf<String>()
    lateinit var repository: SFXAndMusicRepositoryImpl

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        requestedFiles.clear()
        mocker.every { prefs.getBoolean(isAny(), isAny()) } runs { args -> args[1] as Boolean }
        mocker.every { audioPlayer.playSound(isAny(), isAny()) } returns Unit
        mocker.every { audioPlayer.stopSound(isAny()) } returns Unit
        mocker.every { audioPlayer.release() } returns Unit
        mocker.every { musicPlayer.play(isAny()) } returns Unit
        mocker.every { musicPlayer.pause() } returns Unit
        mocker.every { musicPlayer.resume() } returns Unit
        mocker.every { musicPlayer.release() } returns Unit

        val musicManager = MusicManager { fileName ->
            requestedFiles.add(fileName)
            musicPlayer
        }
        repository = SFXAndMusicRepositoryImpl(
            AstorMemorySFXProvider(audioPlayer, prefs),
            AstorMemorySongsProvider(musicManager, prefs)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When play button clicked sound effect expect click sound to be played`() {
        repository.playButtonClickedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("click", 1.7f) }
    }

    @Test
    fun `When play option selected sound effect expect option selected sound to be played`() {
        repository.playOptionSelectedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("option_selected", 0.4f) }
    }

    @Test
    fun `When play card flipped sound effect expect flip sound to be played`() {
        repository.playCardFlippedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("flip_card_final", 0.95f) }
    }

    @Test
    fun `When play shuffle sound effect expect shuffle sound to be played`() {
        repository.playShuffleSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("shuffle", 0.65f) }
    }

    @Test
    fun `When stop shuffle sound effect expect shuffle sound to be stopped`() {
        repository.stopShuffleSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.stopSound("shuffle") }
    }

    @Test
    fun `When play match card sound effect expect match sound to be played`() {
        repository.playMatchCardSoundEffect(matches = 1, totalAmount = 8)

        mocker.verify(exhaustive = false) { audioPlayer.playSound("match_card_init", 0.7f) }
    }

    @Test
    fun `When play menu song expect title screen song to be played`() {
        repository.playMenuSong()

        assertEquals(listOf("title_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When play high score song expect high scores song to be played`() {
        repository.playHighScoreSong()

        assertEquals(listOf("highscores_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When play game song expect game song to be played`() {
        repository.playGameSong(12)

        assertEquals(listOf("gym_leader.mp3"), requestedFiles)
    }

    @Test
    fun `When play game over song expect game over song to be played`() {
        repository.playGameOverSong(score = 1000, amount = 10)

        assertEquals(listOf("gameover_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When pause songs expect the song to be paused`() {
        repository.playMenuSong()

        repository.pauseSongs()

        mocker.verify(exhaustive = false) { musicPlayer.pause() }
    }

    @Test
    fun `When resume song expect the last song to be resumed`() {
        repository.playMenuSong()

        repository.resumeSong()

        mocker.verify(exhaustive = false) { musicPlayer.resume() }
    }

    @Test
    fun `When released expect sound effects and songs to be released`() {
        repository.playMenuSong()

        repository.release()

        mocker.verify(exhaustive = false) {
            audioPlayer.release()
            musicPlayer.release()
        }
    }
}
