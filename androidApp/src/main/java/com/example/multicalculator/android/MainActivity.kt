package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface {
                    CalcView()
                }
            }
        }
    }
}


@Composable
fun CalcRow(onPress: (number: Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, onPress= onPress)
        }
    }
}


@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        text = display.value,
        fontSize = 42.sp,
        modifier = Modifier
            .height (50.dp)
            .padding(5.dp)
            .fillMaxWidth()
    )
}


@Composable
fun CalcNumericButton(number: Int, onPress: (number: Int) -> Unit) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier.padding(4.dp)
    )
    {
        Text(text = number.toString())
    }
}


@Composable
fun CalcOperationButton(operation: String, onPress: (operation: String) -> Unit) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp)
    )
    {
        Text(text = operation)
    }
}


@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(4.dp)
    )
    {
        Text(text = "=")
    }
}


@Composable
fun CalcView() {

    val displayText = remember { mutableStateOf("0") }
    val leftNumber = rememberSaveable { mutableStateOf(0) }
    val rightNumber = rememberSaveable { mutableStateOf(0) }
    val operation = rememberSaveable { mutableStateOf("") }
    val complete = rememberSaveable { mutableStateOf(false) }

    if (complete.value && operation.value != "") {
        var answer = 0
        when (operation.value) {
            "+" -> answer = leftNumber.value + rightNumber.value
            "-" -> answer = leftNumber.value - rightNumber.value
            "*" -> answer = leftNumber.value * rightNumber.value
            "/" -> answer = leftNumber.value / rightNumber.value
        }
        displayText.value = answer.toString()
    } else if (operation.value != "" && !complete.value) {
        displayText.value = rightNumber.value.toString()
    } else {
        displayText.value = leftNumber.value.toString()
    }


    fun numberPress(btnNum: Int) {
        if (complete.value) {
            leftNumber.value = 0
            rightNumber.value = 0
            operation.value = ""
            complete.value = false
        }
        if (operation.value != "" && !complete.value) {
            rightNumber.value = rightNumber.value * 10 + btnNum
        } else if (operation.value == "" && !complete.value) {
            leftNumber.value = leftNumber.value * 10 + btnNum
        }
    }

    fun operationPress(op: String) {
        if (!complete.value) {
            operation.value = op
        }
    }

    fun equalsPress() {
        complete.value = true
    }



    Column(Modifier.background(Color.LightGray)) {
        Row {
            CalcDisplay(display = displayText)
        }
        Row {
            Column {
                for (i in 7 downTo 1 step 3) {
                    CalcRow(onPress = { numberPress(it) }, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(number = 0, onPress = { numberPress(it) })
                    CalcEqualsButton(onPress = { equalsPress() })
                }
            }
            Column {
                val operations = listOf("+", "-", "*", "/")
                operations.forEach { operation ->
                    CalcOperationButton(operation = operation, onPress = { operationPress(it) })
                }
            }
        }
    }
}