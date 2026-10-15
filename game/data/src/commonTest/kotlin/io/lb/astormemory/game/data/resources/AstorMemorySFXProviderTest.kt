package io.lb.astormemory.game.data.resources

import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
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

// AstorMemoryAudio plays the sounds on Dispatchers.Main, so an unconfined test dispatcher
// is set as Main to run them immediately.
@UsesMocks(AudioPlayer::class, AppPreferences::class)
class AstorMemorySFXProviderTest {
    val mocker = Mocker()
    val audioPlayer: AudioPlayer = mocker.mock()
    val prefs: AppPreferences = mocker.mock()
    lateinit var provider: AstorMemorySFXProvider

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        mocker.every { audioPlayer.playSound(isAny(), isAny()) } returns Unit
        mocker.every { audioPlayer.stopSound(isAny()) } returns Unit
        mocker.every { audioPlayer.release() } returns Unit
        provider = AstorMemorySFXProvider(audioPlayer, prefs)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When button is clicked expect click sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playButtonClickedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("click", 1.7f) }
    }

    @Test
    fun `When button is clicked while muted expect click sound to be played lower`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = true)

        provider.playButtonClickedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("click", 0.4f) }
    }

    @Test
    fun `When option is selected expect option selected sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playOptionSelectedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("option_selected", 0.4f) }
    }

    @Test
    fun `When card is flipped expect flip sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playCardFlippedSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("flip_card_final", 0.95f) }
    }

    @Test
    fun `When cards are shuffled expect shuffle sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playShuffleSoundEffect()

        mocker.verify(exhaustive = false) { audioPlayer.playSound("shuffle", 0.65f) }
    }

    @Test
    fun `When shuffle is stopped expect shuffle sound to be stopped`() {
        provider.stopShuffleSoundEffect()

        mocker.verify { audioPlayer.stopSound("shuffle") }
    }

    @Test
    fun `When cards are matched early in the game expect match init sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playMatchCardSoundEffect(matches = 1, totalAmount = 8)

        mocker.verify(exhaustive = false) { audioPlayer.playSound("match_card_init", 0.7f) }
    }

    @Test
    fun `When cards are matched late in the game expect match effect sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = true, isMuted = false)

        provider.playMatchCardSoundEffect(matches = 7, totalAmount = 8)

        mocker.verify(exhaustive = false) { audioPlayer.playSound("match_effect", 0.35f) }
    }

    @Test
    fun `When sound effects are disabled expect no sound to be played`() {
        stubPreferences(isSoundEffectsEnabled = false, isMuted = false)

        provider.playButtonClickedSoundEffect()
        provider.playOptionSelectedSoundEffect()
        provider.playCardFlippedSoundEffect()
        provider.playShuffleSoundEffect()
        provider.playMatchCardSoundEffect(matches = 1, totalAmount = 8)

        mocker.verify {
            repeat(5) { prefs.getBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, true) }
        }
    }

    @Test
    fun `When released expect audio player to be released`() {
        provider.release()

        mocker.verify { audioPlayer.release() }
    }

    private fun stubPreferences(isSoundEffectsEnabled: Boolean, isMuted: Boolean) {
        mocker.every {
            prefs.getBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, true)
        } returns isSoundEffectsEnabled
        mocker.every { prefs.getBoolean(PreferencesKeys.IS_MUTED, false) } returns isMuted
    }
}
