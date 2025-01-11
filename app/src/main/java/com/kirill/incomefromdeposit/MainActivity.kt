package com.kirill.incomefromdeposit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlin.properties.Delegates

typealias boolean = Boolean
typealias double = Double

class MainActivity : ComponentActivity() {

    private val tax: double = 0.13
    private var depositRateForYear = 0.0
    private var sum: double = 0.0
    private var income: double = 0.0

    private val depositMap: Map<Int, Deposits> = mapOf(
        0 to Deposits.Monthly, // 1/12
        1 to Deposits.Two_Months, // 2/12
        2 to Deposits.Three_Months, // 3/12
        3 to Deposits.Six_Months,
        4 to Deposits.Twelve_Months,
        5 to Deposits.TwentyFour_Months,
        6 to Deposits.ThirtySix_Months
    )

    private lateinit var moneyHere: TextView
    private lateinit var enteredMoney: EditText
    private lateinit var bankRate: EditText
    private lateinit var howLongDeposit: LinearLayout

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var isCapitalization: Switch
    private var depositVal by Delegates.notNull<double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFields()
    }

    private fun initFields() {
        enteredMoney = findViewById(R.id.enteredMoney)
        bankRate = findViewById(R.id.bankRate)
        isCapitalization = findViewById(R.id.Capitalization)
        moneyHere = findViewById(R.id.MoneyHere)
        howLongDeposit = findViewById(R.id.HowLongDeposit)
    }

    fun onClick1(v: View) {
        val whichView = howLongDeposit.indexOfChild(v as Button)
        depositVal = depositMap[whichView]!!.number
    }

    fun onClick2(v: View) {
        setIncome(depositVal)
    }

    private fun setIncome(deposit: double) {
        depositRateForYear = bankRate.text.toString().toDouble()
        income = enteredMoney.text.toString().toDouble()
        if (numCheck()) {
            if (isCapitalization.isActivated && deposit >= 1.0) {
                capitalization(deposit)
            } else {
                nonCapitalization(deposit)
            }
        }
    }

    private fun numCheck(): boolean {
        if (income != 0.0 && depositRateForYear != 0.0) {
            return true
        } else {
            Toast.makeText(this, R.string.FillForm, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun nonCapitalization(deposit: double) {
        val earned = (income + ((income / 100.0) * (depositRateForYear * deposit))) - income
        val taxed = earned - (earned * tax)
        sum = income + taxed
        getSubstring()
    }

    private fun capitalization(deposit: double) {
        sum = income
        val duration = deposit.toInt() * 12
        for (i in 0 until duration) {
            sum += (income / 100.0) * (depositRateForYear * deposit)
        }
        val earned = income - (income + ((income / 100.0) * (depositRateForYear * deposit)))
        val taxed = earned - (earned * tax)
        sum = income + (earned - taxed)
        getSubstring()
    }

    private fun getSubstring() {
        val str: String = sum.toString()
        val point = str.indexOf(".")
        enteredMoney.setText(str.substring(0, point + 2))
        moneyHere.setText(R.string.YourMoneyIncome)
    }

    fun resetParams(v: View) {
        income = 0.0
        depositRateForYear = 0.0
        sum = 0.0
        enteredMoney.setText("")
        bankRate.setText("")
        isCapitalization.isActivated = false
        moneyHere.setText(R.string.EnterMoney)
    }
}