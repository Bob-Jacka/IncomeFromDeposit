package com.kirill.incomefromdeposit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var depositRateForYear = 0.0
    private var sum: Double = 0.0
    private var income: Double = 0.0

    private lateinit var moneyHere: TextView
    private lateinit var enteredMoney: EditText
    private lateinit var bankRate: EditText

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var isCapitalization: Switch
    private lateinit var deposit: Deposits

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
    }

    fun onClick1(v: View) {
        deposit = Deposits.valueOf((v as Button).text.toString())
    }

    fun onClick2(v: View): Unit {
        setIncome(deposit)
    }

    private fun setIncome(deposit: Deposits) {
        depositRateForYear = bankRate.text.toString().toDouble()
        income = enteredMoney.text.toString().toDouble()
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

    private fun nonCapitalization(deposit: Deposits) {
        sum = income + ((income / 100) * (depositRateForYear * deposit.depositDuration()))
        getSubstring()
    }

    private fun capitalization(deposit: Deposits) {
        sum = income
        val duration = deposit.depositDuration().toInt() * 12
        for (i in 0 until duration) {
            sum += (income / 100) * (depositRateForYear * deposit.depositDuration())
        }
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