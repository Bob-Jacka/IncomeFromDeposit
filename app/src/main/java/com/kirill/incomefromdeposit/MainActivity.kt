package com.kirill.incomefromdeposit

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kirill.incomefromdeposit.ui.theme.Count
import com.kirill.incomefromdeposit.ui.theme.EnterBankRate
import com.kirill.incomefromdeposit.ui.theme.EnterMoney
import com.kirill.incomefromdeposit.ui.theme.IncomeFromDepositTheme
import com.kirill.incomefromdeposit.ui.theme.MonthlyDeposit
import com.kirill.incomefromdeposit.ui.theme.SixMonthsDeposit
import com.kirill.incomefromdeposit.ui.theme.ThirtySixMonths
import com.kirill.incomefromdeposit.ui.theme.TwelveMonthsDeposit
import com.kirill.incomefromdeposit.ui.theme.TwentyFourMonths
import com.kirill.incomefromdeposit.ui.theme.TwoMonthsDepost
import com.kirill.incomefromdeposit.ui.theme.titleLarge

class MainActivity : ComponentActivity() {

    private var depositRateForYear = 0.0
    private var sum: Double = 0.0
    private var income: Double = 0.0
    private lateinit var enteredMoney: String
    private lateinit var bankRate: String
    private var moneyHere: String? = null
    private lateinit var `val`: String

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var isCapitalization: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IncomeFromDepositTheme(darkTheme = false) {
                Scaffold { innerPadding ->
                    InitScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    @Composable
    private fun InitScreen(modifier: Modifier) {
        Column(modifier = modifier) {
            TextField(
                value = "",
                onValueChange = { moneys -> enteredMoney = moneys },
                placeholder = { Text(text = EnterMoney) }
            )
            TextField(
                value = "",
                onValueChange = { rate -> bankRate = rate },
                placeholder = { Text(text = EnterBankRate) }
            )
            Text(text = moneyHere ?: "")

            Button(onClick = { `val` = MonthlyDeposit }) {
                Text(text = MonthlyDeposit, style = titleLarge)
            }
            Button(onClick = { `val` = TwoMonthsDepost }) {
                Text(text = TwoMonthsDepost, style = titleLarge)
            }

            Button(onClick = { `val` = SixMonthsDeposit }) {
                Text(text = SixMonthsDeposit, style = titleLarge)
            }

            Button(onClick = { `val` = TwelveMonthsDeposit }) {
                Text(text = TwelveMonthsDeposit, style = titleLarge)
            }

            Button(onClick = { `val` = TwentyFourMonths }) {
                Text(text = TwentyFourMonths, style = titleLarge)
            }

            Button(onClick = { `val` = ThirtySixMonths }) {
                Text(text = ThirtySixMonths, style = titleLarge)
            }

            Button(onClick = {
                when (`val`) {
                    "Monthly", "На месяц" -> setIncome(Deposits.Monthly)
                    "Two months", "Два месяца" -> setIncome(Deposits.Two_Months)
                    "Six months", "Шесть месяцев" -> setIncome(Deposits.Six_Months)
                    "Twelve months", "Двенадцать месяцев" -> setIncome(Deposits.Twelve_Months)
                    "Twenty four months", "Двадцать четыре месяца" -> setIncome(Deposits.TwentyFour_Months)
                    "Thirty six months", "Тридцать шесть месяцев" -> setIncome(Deposits.ThirtySix_Months)
                }
            }) {
                Text(text = Count, style = titleLarge)
            }
        }
    }

    private fun setIncome(deposit: Deposits) {
        depositRateForYear = bankRate.toDouble()
        income = enteredMoney.toDouble()
        if (numCheck()) {
            if (isCapitalization.isActivated && deposit.depositDuration() >= 1) {
                capitalization(deposit)
            } else {
                nonCapitalization(deposit)
            }
        }
    }

    private fun numCheck(): Boolean {
        if (income != 0.0 && depositRateForYear != 0.0) {
            return true
        } else {
            Toast.makeText(this, R.string.FillForm, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun getSubstringAndResetParams() {
        val str: String = sum.toString()
        val point = str.indexOf(".")
//        enteredMoney!!.setText(str.substring(0, point + 2))
        moneyHere = R.string.YourMoneyIncome.toString()
        resetParams()
    }

    private fun nonCapitalization(deposit: Deposits) {
        sum = income + ((income / 100) * (depositRateForYear * deposit.depositDuration()))
        getSubstringAndResetParams()
    }

    private fun capitalization(deposit: Deposits) {
        sum = income
        val duration = deposit.depositDuration().toInt() * 12
        for (i in 0 until duration) {
            sum += (income / 100) * (depositRateForYear * deposit.depositDuration())
        }
        getSubstringAndResetParams()
    }

    private fun resetParams() {
        income = 0.0
        depositRateForYear = 0.0
        sum = 0.0
    }
}