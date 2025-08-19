package com.rillet.codingchallenge.accounting

import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount
import org.javamoney.moneta.Money

val dollarsCurrency: CurrencyUnit = Monetary.getCurrency("USD")

fun dollars(n: Number): MonetaryAmount = Money.of(n, dollarsCurrency)

fun List<MonetaryAmount>.sum(): MonetaryAmount {
    require(this.isNotEmpty())
    val sum =
            this.map { it.number.numberValue(BigDecimal::class.java) }.fold(BigDecimal.ZERO) {
                    acc,
                    amount ->
                acc + amount
            }
    return Money.of(sum, this.first().currency)
}
