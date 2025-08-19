package com.rillet.codingchallenge.accounting.application

import com.rillet.codingchallenge.accounting.dollars
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

class SplitAmountsUseCaseTest {

    private val useCase = SplitAmountsUseCase()

    @Test
    fun `returns amounts calculated over a year`() {
        val result = useCase.execute(dollars(1200))

        expectThat(result).isNotEqualTo(null)
    }

    @Test
    fun `handles even division`() {
        val result = useCase.execute(dollars(1200))

        expectThat(result.amounts.size).isEqualTo(12)
        for (i in 0 until 12) {
            expectThat(result.amounts[i]).isEqualTo(dollars(100))
        }
    }

    @Test
    fun `handles rounding difference - default adjustment month`() {
        val result = useCase.execute(dollars(1000))

        expectThat(result.amounts.size).isEqualTo(12)
        for (i in 0 until 11) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
        expectThat(result.amounts[11]).isEqualTo(dollars(83.37))
    }

    @Test
    fun `handles rounding difference - first month`() {
        val result = useCase.execute(dollars(1000), RoundingStrategy.FIRST)

        expectThat(result.amounts.size).isEqualTo(12)
        expectThat(result.amounts[0]).isEqualTo(dollars(83.37))
        for (i in 1 until 12) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
    }

    @Test
    fun `handles rounding difference - middle month`() {
        val result = useCase.execute(dollars(1000), RoundingStrategy.MIDDLE)

        expectThat(result.amounts.size).isEqualTo(12)

        for (i in 0 until 12) {
            if (i == 5) {
                expectThat(result.amounts[i]).isEqualTo(dollars(83.37))
            } else {
                expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
            }
        }
    }

    @Test
    fun `handles rounding difference - last month`() {
        val result = useCase.execute(dollars(1000))

        expectThat(result.amounts.size).isEqualTo(12)
        for (i in 0 until 11) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
        expectThat(result.amounts[11]).isEqualTo(dollars(83.37))
    }
}
