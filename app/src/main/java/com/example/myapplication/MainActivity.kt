package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var resultTv: TextView
    private var expression = StringBuilder()
    private var lastResult: String? = null
    private var isNewCalculation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTv = findViewById(R.id.resultTv)

        val buttonIds = listOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
            R.id.button_8, R.id.button_9, R.id.button_add, R.id.button_minus,
            R.id.button_multiply, R.id.button_divide, R.id.button_dot
        )

        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener { appendToExpression(it as Button) }
        }

        // Xóa một ký tự cuối cùng (Backspace)
        findViewById<Button>(R.id.button_bs).setOnClickListener {
            if (isNewCalculation) {
                return@setOnClickListener
            }

            if (expression.isNotEmpty()) {
                expression.deleteCharAt(expression.length - 1)
                resultTv.text = if (expression.isNotEmpty()) expression.toString() else "0"
            }
        }

        // Xóa toàn bộ biểu thức (Clear Entry)
        findViewById<Button>(R.id.button_ce).setOnClickListener {
            expression.clear()
            resultTv.text = "0"
            lastResult = null
            isNewCalculation = false
        }

        // Xóa tất cả và đưa về trạng thái ban đầu (Clear - nút "C")
        findViewById<Button>(R.id.button_c).setOnClickListener {
            expression.clear()
            resultTv.text = "0"
            lastResult = null
            isNewCalculation = false
        }

        // Thực hiện phép tính
        findViewById<Button>(R.id.button_equals).setOnClickListener {
            calculateResult()
        }
    }

    private fun appendToExpression(button: Button) {
        val input = button.text.toString()

        if (isNewCalculation) {
            expression.clear()
            if (input.matches(Regex("[0-9]"))) {
                expression.append(input)
            } else {
                expression.append(lastResult ?: "0").append(input)
            }
            isNewCalculation = false
        } else {
            expression.append(input)
        }

        resultTv.text = expression.toString()
    }

    private fun calculateResult() {
        val expr = expression.toString().replace("x", "*")
        if (expr.isNotEmpty()) {
            try {
                val result = ExpressionBuilder(expr).build().evaluate()
                lastResult = if (result % 1 == 0.0) {
                    result.toInt().toString()
                } else {
                    String.format(Locale.getDefault(), "%.2f", result)
                }
                resultTv.text = lastResult
                isNewCalculation = true
            } catch (e: Exception) {
                resultTv.text = "Lỗi"
            }
        }
    }
}
