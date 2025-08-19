package com.rillet.codingchallenge.accounting.application

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Month
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money
import org.springframework.stereotype.Service

data class MonthlyAmounts(val amounts: List<MonetaryAmount>)

enum class RoundingStrategy(val month: Month) {
    FIRST(Month.JANUARY),
    MIDDLE(Month.JUNE),
    LAST(Month.DECEMBER)
}

@Service
class SplitAmountsUseCase {
    fun execute(
            amount: MonetaryAmount,
            roundingStrategy: RoundingStrategy = RoundingStrategy.LAST
    ): MonthlyAmounts {
        // TODO: Validation

        val currency = amount.currency
        val total = amount.number.numberValue(BigDecimal::class.java)

        val baseMonthlyAmount = total.divide(BigDecimal(12), 2, RoundingMode.DOWN)
        val sumOfMonthlyAmounts = baseMonthlyAmount.multiply(BigDecimal(12))
        val roundingDifference = total.subtract(sumOfMonthlyAmounts)

        val amounts = mutableListOf<MonetaryAmount>()

        for (month in Month.values()) {
            if (month == roundingStrategy.month) {
                amounts.add(Money.of(baseMonthlyAmount.add(roundingDifference), currency))
            } else {
                amounts.add(Money.of(baseMonthlyAmount, currency))
            }
        }
        return MonthlyAmounts(amounts)
    }
}
