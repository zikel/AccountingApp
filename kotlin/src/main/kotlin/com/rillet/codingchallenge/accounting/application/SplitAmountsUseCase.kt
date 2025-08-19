package com.rillet.codingchallenge.accounting.application

import java.math.BigDecimal
import java.math.RoundingMode
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money
import org.springframework.stereotype.Service

data class MonthlyAmounts(val amounts: List<MonetaryAmount>, val total: MonetaryAmount)

enum class RoundingStrategy {
    FIRST,
    MIDDLE,
    LAST
}

@Service
class SplitAmountsUseCase {
    fun execute(
            amount: MonetaryAmount?,
            roundingStrategy: RoundingStrategy = RoundingStrategy.LAST
    ): MonthlyAmounts {
        requireNotNull(amount)
        require(amount.number.numberValue(BigDecimal::class.java) > BigDecimal.ZERO)

        val currency = amount.currency
        val total = amount.number.numberValue(BigDecimal::class.java)

        val baseMonthlyAmount = total.divide(BigDecimal(12), 2, RoundingMode.DOWN)

        val sumOfBaseMonthlyAmount = baseMonthlyAmount.multiply(BigDecimal(12))
        val roundingDifference = total.subtract(sumOfBaseMonthlyAmount)

        val monthlyAmounts = mutableListOf<MonetaryAmount>()
        val adjustmentIndex =
                when (roundingStrategy) {
                    RoundingStrategy.FIRST -> 0
                    RoundingStrategy.MIDDLE -> 5
                    RoundingStrategy.LAST -> 11
                }

        for (i in 0 until 12) {
            val monthlyAmount =
                    if (i == adjustmentIndex) {
                        baseMonthlyAmount.add(roundingDifference)
                    } else {
                        baseMonthlyAmount
                    }
            monthlyAmounts.add(Money.of(monthlyAmount, currency))
        }

        return MonthlyAmounts(amounts = monthlyAmounts, total = amount)
    }
}
