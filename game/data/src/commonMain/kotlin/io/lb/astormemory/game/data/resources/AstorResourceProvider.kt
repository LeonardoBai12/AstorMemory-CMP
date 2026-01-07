package io.lb.astormemory.game.data.resources

import astormemory.game.data.generated.resources.Res
import io.lb.astormemory.resources.AstorResources
import io.lb.astormemory.shared.model.AstorCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

class AstorResourceProvider {
    @InternalResourceApi
    suspend fun getAllAstorCards(): List<AstorCard> = withContext(Dispatchers.Default) {
        AstorResources.all.map { info ->
            val imageBytes = Res.readBytes("drawable/${info.resourceName}.webp")
            AstorCard(
                id = info.id.toString(),
                astorId = info.id,
                imageUrl = "compose://drawable/${info.resourceName}.webp",
                imageData = imageBytes,
                name = info.name
            )
        }.shuffled()
    }
    
    @InternalResourceApi
    suspend fun getAstorPairs(amount: Int): List<AstorCard> {
        val astorCards = getAllAstorCards()
        val selectedAstor = astorCards.take(amount).shuffled().toMutableList()
        selectedAstor.addAll(selectedAstor.shuffled())
        return selectedAstor.shuffled()
    }
}
