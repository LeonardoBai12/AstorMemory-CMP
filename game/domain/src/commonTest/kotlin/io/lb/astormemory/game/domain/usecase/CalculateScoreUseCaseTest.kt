package io.lb.astormemory.game.domain.usecase

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateScoreUseCaseTest {
    lateinit var useCase: CalculateScoreUseCase

    @BeforeTest
    fun setUp() {
        useCase = CalculateScoreUseCase()
    }

    @Test
    fun `When amount is 1 or less, expect score to be zero`() {
        assertEquals(0, useCase(1, emptyList(), 0))
        assertEquals(0, useCase(0, emptyList(), 0))
    }

    @Test
    fun `When there are no mismatches or combos, expect max score`() {
        assertEquals(500, useCase(5, emptyList(), 0))
        assertEquals(1000, useCase(10, emptyList(), 0))
    }

    @Test
    fun `When there are mismatches, expect score to be reduced`() {
        assertEquals(490, useCase(5, emptyList(), 1))
        assertEquals(480, useCase(5, emptyList(), 2))
    }

    @Test
    fun `When mismatches exceed max score, expect score to be zero`() {
        assertEquals(0, useCase(5, emptyList(), 100))
    }

    @Test
    fun `When there are combos, expect bonus to be added`() {
        assertEquals(530, useCase(5, listOf(3), 0))
        assertEquals(550, useCase(5, listOf(3, 2), 0))
    }

    @Test
    fun `When there are both mismatches and combos, expect combined effect`() {
        assertEquals(520, useCase(5, listOf(3), 1))
    }
}
