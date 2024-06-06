package com.example.multicalculator

class Caculator {

    fun add(left: Int, right: Int): Int{
        return left + right
    }

    fun subtract(left: Int, right: Int): Int{
        return left - right
    }

    fun divide(left: Int, right: Int): Int{
        return if (right == 0 ){
            throw IllegalArgumentException("Cannot divide by zero")
        }
        else {
            left / right
        }
    }

    fun multiply(left: Int, right: Int): Int{
        return left * right
    }


}