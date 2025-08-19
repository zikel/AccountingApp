package com.rillet.codingchallenge.accounting.application

import com.rillet.codingchallenge.accounting.dollars
import com.rillet.codingchallenge.accounting.sum
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

    // Even Division
    @Test
    fun `splits $1200 for 12 months`() {
        val result = useCase.execute(dollars(1200))

        expectThat(result.amounts.size).isEqualTo(12)
    }

    @Test
    fun `splits $1200 into $100 per month`() {
        val result = useCase.execute(dollars(1200))

        for (i in 0 until 12) {
            expectThat(result.amounts[i]).isEqualTo(dollars(100))
        }
    }

    @Test
    fun `sum of monthly revenue adds up to $1200`() {
        val result = useCase.execute(dollars(1200))
        expectThat(result.amounts.sum()).isEqualTo(dollars(1200))
    }

    @Test
    fun `handle rounding difference with default strategy`() {
        val result = useCase.execute(dollars(1000))
        for (i in 0 until 11) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
        expectThat(result.amounts[11]).isEqualTo(dollars(83.37))
    }

    @Test
    fun `Puts rounding difference to the first meonth`() {
        val result = useCase.execute(dollars(1000), RoundingStrategy.FIRST)
        expectThat(result.amounts[0]).isEqualTo(dollars(83.37))
        for (i in 1 until 12) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
    }

    @Test
    fun `Puts rounding difference to the middle meonth`() {
        val result = useCase.execute(dollars(1000), RoundingStrategy.MIDDLE)
        for (i in 0 until 12) {
            if (i == 5) {
                expectThat(result.amounts[i]).isEqualTo(dollars(83.37))
            } else {
                expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
            }
        }
    }

    @Test
    fun `Puts rounding difference to the last meonth`() {
        val result = useCase.execute(dollars(1000))
        for (i in 0 until 11) {
            expectThat(result.amounts[i]).isEqualTo(dollars(83.33))
        }
        expectThat(result.amounts[11]).isEqualTo(dollars(83.37))
    }
}
