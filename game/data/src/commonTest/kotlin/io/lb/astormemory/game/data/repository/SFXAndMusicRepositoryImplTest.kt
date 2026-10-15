package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.data.fake.FakePreferencesDataStore
import io.lb.astormemory.game.data.resources.AstorMemorySFXProvider
import io.lb.astormemory.game.data.resources.AstorMemorySongsProvider
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.music.MusicPlayer
import io.lb.astormemory.game.platform.utils.MusicManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// AstorMemorySFXProvider and AstorMemorySongsProvider are concrete classes and cannot be mocked
// by MockMP (which only mocks interfaces). AudioPlayer and MusicPlayer (interfaces) are mocked
// instead, and real providers are constructed around them with an in-memory DataStore.
@UsesMocks(AudioPlayer::class, MusicPlayer::class)
class SFXAndMusicRepositoryImplTest {
    val mocker = Mocker()
    val audioPlayer: AudioPlayer = mocker.mock()
    val musicPlayer: MusicPlayer = mocker.mock()
    val requestedFiles = mutableListOf<String>()
    lateinit var repository: SFXAndMusicRepositoryImpl

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        requestedFiles.clear()
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
        val settingsDataSource = SettingsDataSource(FakePreferencesDataStore())
        repository = SFXAndMusicRepositoryImpl(
            AstorMemorySFXProvider(audioPlayer, settingsDataSource),
            AstorMemorySongsProvider(musicManager, settingsDataSource)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When play button clicked sound effect expect click sound to be played`() = runTest {
        repository.playButtonClickedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("click", 1.7f) }
    }

    @Test
    fun `When play option selected sound effect expect option selected sound to be played`() = runTest {
        repository.playOptionSelectedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("option_selected", 0.4f) }
    }

    @Test
    fun `When play card flipped sound effect expect flip sound to be played`() = runTest {
        repository.playCardFlippedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("flip_card_final", 0.95f) }
    }

    @Test
    fun `When play shuffle sound effect expect shuffle sound to be played`() = runTest {
        repository.playShuffleSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("shuffle", 0.65f) }
    }

    @Test
    fun `When stop shuffle sound effect expect shuffle sound to be stopped`() = runTest {
        repository.stopShuffleSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.stopSound("shuffle") }
    }

    @Test
    fun `When play match card sound effect expect match sound to be played`() = runTest {
        repository.playMatchCardSoundEffect(matches = 1, totalAmount = 8)

        mocker.verify(exhaustive = false) { audioPlayer.playSound("match_card_init", 0.7f) }
    }

    @Test
    fun `When play menu song expect title screen song to be played`() = runTest {
        repository.playMenuSong()

        assertEquals(listOf("title_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When play high score song expect high scores song to be played`() = runTest {
        repository.playHighScoreSong()

        assertEquals(listOf("highscores_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When play game song expect game song to be played`() = runTest {
        repository.playGameSong(12)

        assertEquals(listOf("gym_leader.mp3"), requestedFiles)
    }

    @Test
    fun `When play game over song expect game over song to be played`() = runTest {
        repository.playGameOverSong(score = 1000, amount = 10)

        assertEquals(listOf("gameover_screen.mp3"), requestedFiles)
    }

    @Test
    fun `When pause songs expect the song to be paused`() = runTest {
        repository.playMenuSong()

        repository.pauseSongs()

        mocker.verify(exhaustive = false) { musicPlayer.pause() }
    }

    @Test
    fun `When resume song expect the last song to be resumed`() = runTest {
        repository.playMenuSong()

        repository.resumeSong()

        mocker.verify(exhaustive = false) { musicPlayer.resume() }
    }

    @Test
    fun `When released expect sound effects and songs to be released`() = runTest {
        repository.playMenuSong()

        repository.release()

        mocker.verify(exhaustive = false) {
            audioPlayer.release()
            musicPlayer.release()
        }
    }
}
