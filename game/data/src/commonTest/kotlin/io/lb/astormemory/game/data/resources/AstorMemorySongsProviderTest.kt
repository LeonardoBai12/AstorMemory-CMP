package io.lb.astormemory.game.data.resources

import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.data.fake.FakePreferencesDataStore
import io.lb.astormemory.game.platform.music.MusicPlayer
import io.lb.astormemory.game.platform.utils.MusicManager
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// MusicManager is a concrete class and cannot be mocked by MockMP (which only mocks interfaces).
// MusicPlayer (interface) is mocked instead, and a real MusicManager is constructed around it,
// recording which song files were requested. The settings come from an in-memory DataStore.
@UsesMocks(MusicPlayer::class)
class AstorMemorySongsProviderTest {
    val mocker = Mocker()
    val musicPlayer: MusicPlayer = mocker.mock()
    val requestedFiles = mutableListOf<String>()
    lateinit var dataStore: FakePreferencesDataStore
    lateinit var provider: AstorMemorySongsProvider

    @BeforeTest
    fun setUp() {
        mocker.reset()
        requestedFiles.clear()
        mocker.every { musicPlayer.play(isAny()) } returns Unit
        mocker.every { musicPlayer.pause() } returns Unit
        mocker.every { musicPlayer.resume() } returns Unit
        mocker.every { musicPlayer.release() } returns Unit
        val musicManager = MusicManager { fileName ->
            requestedFiles.add(fileName)
            musicPlayer
        }
        dataStore = FakePreferencesDataStore()
        provider = AstorMemorySongsProvider(musicManager, SettingsDataSource(dataStore))
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When play menu song expect title screen song to be played`() = runTest {

        provider.playMenuSong()

        assertEquals(listOf("title_screen.mp3"), requestedFiles)
        mocker.verify(exhaustive = false) { musicPlayer.play(0.7f) }
    }

    @Test
    fun `When play high score song expect high scores song to be played`() = runTest {

        provider.playHighScoreSong()

        assertEquals(listOf("highscores_screen.mp3"), requestedFiles)
        mocker.verify(exhaustive = false) { musicPlayer.play(0.7f) }
    }

    @Test
    fun `When play game song expect song according to the amount of cards`() = runTest {
        val expectedSongs = mapOf(
            1 to ("victory_road.mp3" to 1.0f),
            5 to ("wild.mp3" to 1.0f),
            8 to ("trainer.mp3" to 0.85f),
            12 to ("gym_leader.mp3" to 0.85f),
            20 to ("final_battle.mp3" to 0.85f),
        )

        expectedSongs.forEach { (amount, song) ->
            val (fileName, volume) = song
            requestedFiles.clear()

            provider.playGameSong(amount)

            assertEquals(listOf(fileName), requestedFiles, "Wrong song for amount $amount")
            mocker.verify(exhaustive = false) { musicPlayer.play(volume) }
        }
    }

    @Test
    fun `When play game over song with no score expect lavender song to be played`() = runTest {

        provider.playGameOverSong(score = 0, amount = 20)

        assertEquals(listOf("lavender.mp3"), requestedFiles)
        mocker.verify(exhaustive = false) { musicPlayer.play(0.95f) }
    }

    @Test
    fun `When play game over song after a hard game expect victory final song to be played`() = runTest {

        provider.playGameOverSong(score = 1000, amount = 20)

        assertEquals(listOf("victory_final.mp3"), requestedFiles)
        mocker.verify(exhaustive = false) { musicPlayer.play(1.0f) }
    }

    @Test
    fun `When play game over song expect game over song to be played`() = runTest {

        provider.playGameOverSong(score = 1000, amount = 10)

        assertEquals(listOf("gameover_screen.mp3"), requestedFiles)
        mocker.verify(exhaustive = false) { musicPlayer.play(1.0f) }
    }

    @Test
    fun `When muted expect no song to be played`() = runTest {
        dataStore[PreferencesKeys.IS_MUTED] = true

        provider.playMenuSong()
        provider.playHighScoreSong()
        provider.playGameSong(10)
        provider.playGameOverSong(score = 1000, amount = 10)

        assertTrue(requestedFiles.isEmpty())
    }

    @Test
    fun `When muted after a song was played expect the song to be paused`() = runTest {
        provider.playMenuSong()

        dataStore[PreferencesKeys.IS_MUTED] = true
        provider.playMenuSong()

        mocker.verify {
            musicPlayer.play(0.7f)
            musicPlayer.pause()
        }
    }

    @Test
    fun `When pause songs expect the song to be paused`() = runTest {
        provider.playMenuSong()

        provider.pauseSongs()

        mocker.verify(exhaustive = false) { musicPlayer.pause() }
    }

    @Test
    fun `When resume song expect the last song to be resumed`() = runTest {
        provider.playMenuSong()

        provider.resumeSong()

        mocker.verify(exhaustive = false) { musicPlayer.resume() }
    }

    @Test
    fun `When resume song while muted expect the last song not to be resumed`() = runTest {
        provider.playMenuSong()

        dataStore[PreferencesKeys.IS_MUTED] = true
        provider.resumeSong()

        mocker.verify { musicPlayer.play(0.7f) }
    }

    @Test
    fun `When released expect the songs to be released`() = runTest {
        provider.playMenuSong()

        provider.release()

        mocker.verify(exhaustive = false) { musicPlayer.release() }
    }
}
