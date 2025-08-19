package com.rillet.codingchallenge.accounting.infra.dataclasses

import com.rillet.codingchallenge.accounting.application.MonthlyAmounts
import java.math.BigDecimal
import java.math.RoundingMode
import javax.money.MonetaryAmount

data class MonetaryAmountDto(val value: String, val currency: String)

data class MonthlyAmountDto(val month: String, val monetaryAmount: MonetaryAmountDto)

class ResponseDto(
        val total: MonetaryAmountDto,
        val monthlyAmounts: List<MonthlyAmountDto>,
        val numOfMonths: Int
) {
    companion object {
        private val monthNames =
                listOf(
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                )

        fun from(result: Any): ResponseDto {
            return when (result) {
                is MonthlyAmounts -> {
                    ResponseDto(
                            total = result.total.toDto(),
                            monthlyAmounts =
                                    result.amounts.mapIndexed { index, amount ->
                                        MonthlyAmountDto(
                                                month = monthNames[index],
                                                monetaryAmount = amount.toDto()
                                        )
                                    },
                            numOfMonths = result.amounts.size
                    )
                }
                else -> throw IllegalArgumentException("Unsupported result type ${result::class}")
            }
        }

        private fun MonetaryAmount.toDto(): MonetaryAmountDto {
            return MonetaryAmountDto(
                    value =
                            this.number
                                    .numberValue(BigDecimal::class.java)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .toPlainString(),
                    currency = this.currency.currencyCode
            )
        }
    }
}
