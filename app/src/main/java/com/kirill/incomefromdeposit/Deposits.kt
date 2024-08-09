package com.kirill.incomefromdeposit

enum class Deposits(val number: Double) {

    Monthly(0.08333333333), // 1/12
    Two_Months(0.16666666666), // 2/12
    Six_Months(0.5),
    Twelve_Months(1.0),
    TwentyFour_Months(2.0),
    ThirtySix_Months(3.0);

    fun depositDuration(): Double {
        return number
    }
}