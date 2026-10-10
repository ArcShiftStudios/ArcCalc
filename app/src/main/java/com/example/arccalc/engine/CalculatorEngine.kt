package com.example.arccalc.engine

import com.example.arccalc.model.AngleMode
import com.example.arccalc.model.DecimalPrecision
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

object CalculatorEngine {

    sealed class EvalResult {
        data class Success(val value: Double, val formatted: String) : EvalResult()
        data class Error(val message: String) : EvalResult()
    }

    /**
     * Evaluates a mathematical expression string.
     */
    fun evaluate(
        rawExpression: String,
        angleMode: AngleMode = AngleMode.DEG,
        precision: DecimalPrecision = DecimalPrecision.AUTO
    ): EvalResult {
        val expr = prepareExpression(rawExpression)
        if (expr.isBlank()) {
            return EvalResult.Success(0.0, "0")
        }

        return try {
            val tokens = tokenize(expr)
            if (tokens.isEmpty()) {
                return EvalResult.Success(0.0, "0")
            }
            val parsedResult = parseAndEvaluate(tokens, angleMode)
            if (parsedResult.isNaN()) {
                EvalResult.Error("Undefined")
            } else if (parsedResult.isInfinite()) {
                if (parsedResult > 0) EvalResult.Error("Overflow") else EvalResult.Error("-Overflow")
            } else {
                val formatted = formatResult(parsedResult, precision)
                EvalResult.Success(parsedResult, formatted)
            }
        } catch (e: ArithmeticException) {
            EvalResult.Error(e.message ?: "Math error")
        } catch (e: IllegalArgumentException) {
            EvalResult.Error(e.message ?: "Invalid expression")
        } catch (e: Exception) {
            EvalResult.Error("Invalid format")
        }
    }

    private fun prepareExpression(raw: String): String {
        return raw
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("—", "-")
            .replace("π", "PI")
            .replace("√", "sqrt")
            .replace("∛", "cbrt")
            .replace("sin⁻¹", "asin")
            .replace("cos⁻¹", "acos")
            .replace("tan⁻¹", "atan")
            .replace("EXP", "*10^")
            .replace("mod", "%")
            .replace("MOD", "%")
            .trim()
    }

    // Tokenizer
    private sealed class Token {
        data class Number(val value: Double) : Token()
        data class Op(val symbol: String, val precedence: Int, val isRightAssoc: Boolean = false) : Token()
        data class Func(val name: String) : Token()
        object LeftParen : Token()
        object RightParen : Token()
        object Factorial : Token()
        object Percent : Token()
    }

    private fun tokenize(expr: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val len = expr.length

        fun canImplicitMultiply(): Boolean {
            if (tokens.isEmpty()) return false
            return when (tokens.last()) {
                is Token.Number, is Token.RightParen, is Token.Factorial, is Token.Percent -> true
                else -> false
            }
        }

        while (i < len) {
            val c = expr[i]

            if (c.isWhitespace()) {
                i++
                continue
            }

            if (c.isDigit() || c == '.') {
                if (canImplicitMultiply()) {
                    tokens.add(Token.Op("*", 2))
                }
                val sb = StringBuilder()
                var hasDot = false
                while (i < len && (expr[i].isDigit() || (expr[i] == '.' && !hasDot))) {
                    if (expr[i] == '.') hasDot = true
                    sb.append(expr[i])
                    i++
                }
                // Handle scientific notation e/E like 1.2e5
                if (i < len && (expr[i] == 'e' || expr[i] == 'E') && i + 1 < len && (expr[i + 1].isDigit() || expr[i + 1] == '+' || expr[i + 1] == '-')) {
                    sb.append(expr[i])
                    i++
                    if (i < len && (expr[i] == '+' || expr[i] == '-')) {
                        sb.append(expr[i])
                        i++
                    }
                    while (i < len && expr[i].isDigit()) {
                        sb.append(expr[i])
                        i++
                    }
                }
                val numStr = sb.toString()
                val value = numStr.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: $numStr")
                tokens.add(Token.Number(value))
                continue
            }

            if (c.isLetter()) {
                val sb = StringBuilder()
                while (i < len && expr[i].isLetter()) {
                    sb.append(expr[i])
                    i++
                }
                val name = sb.toString()
                if (name.equals("PI", ignoreCase = true)) {
                    if (canImplicitMultiply()) tokens.add(Token.Op("*", 2))
                    tokens.add(Token.Number(Math.PI))
                } else if (name.equals("e", ignoreCase = true) || name.equals("E", ignoreCase = true)) {
                    if (canImplicitMultiply()) tokens.add(Token.Op("*", 2))
                    tokens.add(Token.Number(Math.E))
                } else {
                    if (canImplicitMultiply()) tokens.add(Token.Op("*", 2))
                    tokens.add(Token.Func(name.lowercase(Locale.ROOT)))
                }
                continue
            }

            when (c) {
                '+' -> {
                    val isUnary = tokens.isEmpty() || tokens.last() is Token.Op || tokens.last() is Token.LeftParen
                    if (!isUnary) {
                        tokens.add(Token.Op("+", 1))
                    }
                    i++
                }
                '-' -> {
                    val isUnary = tokens.isEmpty() || tokens.last() is Token.Op || tokens.last() is Token.LeftParen
                    if (isUnary) {
                        // Unary negation: represented as 0 - x or multiplying by -1
                        tokens.add(Token.Number(0.0))
                        tokens.add(Token.Op("-", 1))
                    } else {
                        tokens.add(Token.Op("-", 1))
                    }
                    i++
                }
                '*' -> {
                    tokens.add(Token.Op("*", 2))
                    i++
                }
                '/' -> {
                    tokens.add(Token.Op("/", 2))
                    i++
                }
                '%' -> {
                    // Check if it's modulo or percentage
                    // If followed by another operator or parenthesis or end of expression, it acts as percentage
                    tokens.add(Token.Percent)
                    i++
                }
                '^' -> {
                    tokens.add(Token.Op("^", 3, isRightAssoc = true))
                    i++
                }
                '!' -> {
                    tokens.add(Token.Factorial)
                    i++
                }
                '(' -> {
                    if (canImplicitMultiply()) tokens.add(Token.Op("*", 2))
                    tokens.add(Token.LeftParen)
                    i++
                }
                ')' -> {
                    tokens.add(Token.RightParen)
                    i++
                }
                else -> {
                    i++
                }
            }
        }

        return tokens
    }

    // Shunting-yard algorithm to Reverse Polish Notation (RPN)
    private fun parseAndEvaluate(tokens: List<Token>, angleMode: AngleMode): Double {
        val outputQueue = mutableListOf<Token>()
        val opStack = mutableListOf<Token>()

        for (token in tokens) {
            when (token) {
                is Token.Number -> outputQueue.add(token)
                is Token.Func -> opStack.add(token)
                is Token.Op -> {
                    while (opStack.isNotEmpty()) {
                        val top = opStack.last()
                        if (top is Token.Func) {
                            outputQueue.add(opStack.removeAt(opStack.size - 1))
                        } else if (top is Token.Op) {
                            if ((!token.isRightAssoc && token.precedence <= top.precedence) ||
                                (token.isRightAssoc && token.precedence < top.precedence)
                            ) {
                                outputQueue.add(opStack.removeAt(opStack.size - 1))
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                    opStack.add(token)
                }
                is Token.Percent, is Token.Factorial -> {
                    // Postfix operators have high precedence, apply directly
                    outputQueue.add(token)
                }
                is Token.LeftParen -> opStack.add(token)
                is Token.RightParen -> {
                    var foundLeft = false
                    while (opStack.isNotEmpty()) {
                        val top = opStack.removeAt(opStack.size - 1)
                        if (top is Token.LeftParen) {
                            foundLeft = true
                            break
                        } else {
                            outputQueue.add(top)
                        }
                    }
                    if (opStack.isNotEmpty() && opStack.last() is Token.Func) {
                        outputQueue.add(opStack.removeAt(opStack.size - 1))
                    }
                }
            }
        }

        while (opStack.isNotEmpty()) {
            val top = opStack.removeAt(opStack.size - 1)
            if (top is Token.LeftParen || top is Token.RightParen) {
                // Auto-close missing parentheses gracefully
                continue
            }
            outputQueue.add(top)
        }

        return evaluateRPN(outputQueue, angleMode)
    }

    private fun evaluateRPN(rpn: List<Token>, angleMode: AngleMode): Double {
        val stack = mutableListOf<Double>()

        fun toRadians(angle: Double): Double = when (angleMode) {
            AngleMode.DEG -> Math.toRadians(angle)
            AngleMode.RAD -> angle
            AngleMode.GRAD -> angle * Math.PI / 200.0
        }

        fun fromRadians(rad: Double): Double = when (angleMode) {
            AngleMode.DEG -> Math.toDegrees(rad)
            AngleMode.RAD -> rad
            AngleMode.GRAD -> rad * 200.0 / Math.PI
        }

        for (token in rpn) {
            when (token) {
                is Token.Number -> stack.add(token.value)
                is Token.Op -> {
                    if (stack.size < 2) throw IllegalArgumentException("Missing operand")
                    val b = stack.removeAt(stack.size - 1)
                    val a = stack.removeAt(stack.size - 1)
                    val res = when (token.symbol) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            if (b == 0.0) throw ArithmeticException("Cannot divide by 0")
                            a / b
                        }
                        "^" -> a.pow(b)
                        "%" -> a % b
                        else -> throw IllegalArgumentException("Unknown operator: ${token.symbol}")
                    }
                    stack.add(res)
                }
                is Token.Percent -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for %")
                    val a = stack.removeAt(stack.size - 1)
                    stack.add(a / 100.0)
                }
                is Token.Factorial -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for !")
                    val a = stack.removeAt(stack.size - 1)
                    if (a < 0 || a != floor(a)) throw ArithmeticException("Factorial of non-integer or negative")
                    if (a > 170) throw ArithmeticException("Value too large")
                    var fact = 1.0
                    for (k in 2..a.toInt()) {
                        fact *= k
                    }
                    stack.add(fact)
                }
                is Token.Func -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing argument for ${token.name}")
                    val a = stack.removeAt(stack.size - 1)
                    val res = when (token.name) {
                        "sin" -> {
                            val rad = toRadians(a)
                            // Fix precision issue for sin(180) or sin(360) in deg
                            val degMod = ((a % 360.0) + 360.0) % 360.0
                            if (angleMode == AngleMode.DEG && (degMod == 0.0 || degMod == 180.0)) 0.0
                            else if (angleMode == AngleMode.DEG && degMod == 30.0) 0.5
                            else if (angleMode == AngleMode.DEG && degMod == 150.0) 0.5
                            else sin(rad)
                        }
                        "cos" -> {
                            val rad = toRadians(a)
                            val degMod = ((a % 360.0) + 360.0) % 360.0
                            if (angleMode == AngleMode.DEG && (degMod == 90.0 || degMod == 270.0)) 0.0
                            else if (angleMode == AngleMode.DEG && degMod == 60.0) 0.5
                            else cos(rad)
                        }
                        "tan" -> {
                            val degMod = ((a % 180.0) + 180.0) % 180.0
                            if (angleMode == AngleMode.DEG && degMod == 90.0) throw ArithmeticException("Undefined")
                            tan(toRadians(a))
                        }
                        "asin" -> {
                            if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error")
                            fromRadians(asin(a))
                        }
                        "acos" -> {
                            if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error")
                            fromRadians(acos(a))
                        }
                        "atan" -> fromRadians(atan(a))
                        "sinh" -> sinh(a)
                        "cosh" -> cosh(a)
                        "tanh" -> tanh(a)
                        "ln" -> {
                            if (a <= 0) throw ArithmeticException("Domain error")
                            ln(a)
                        }
                        "log" -> {
                            if (a <= 0) throw ArithmeticException("Domain error")
                            log10(a)
                        }
                        "log2" -> {
                            if (a <= 0) throw ArithmeticException("Domain error")
                            ln(a) / ln(2.0)
                        }
                        "sqrt" -> {
                            if (a < 0) throw ArithmeticException("Domain error")
                            sqrt(a)
                        }
                        "cbrt" -> Math.cbrt(a)
                        "sqr", "square" -> a * a
                        "cube" -> a * a * a
                        "reciprocal", "inv" -> {
                            if (a == 0.0) throw ArithmeticException("Cannot divide by 0")
                            1.0 / a
                        }
                        "abs" -> abs(a)
                        "exp" -> exp(a)
                        "tenpow" -> 10.0.pow(a)
                        "twopow" -> 2.0.pow(a)
                        else -> throw IllegalArgumentException("Unknown function: ${token.name}")
                    }
                    stack.add(res)
                }
                else -> {}
            }
        }

        if (stack.size != 1) throw IllegalArgumentException("Malformed expression")
        return stack.first()
    }

    fun formatResult(value: Double, precision: DecimalPrecision = DecimalPrecision.AUTO): String {
        if (value.isNaN()) return "Undefined"
        if (value.isInfinite()) return if (value > 0) "Overflow" else "-Overflow"

        // Avoid -0.0
        val cleanValue = if (abs(value) < 1e-15) 0.0 else value

        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = ','
            decimalSeparator = '.'
        }

        // Scientific notation for very large / small values
        val absVal = abs(cleanValue)
        if (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-6)) {
            val df = DecimalFormat("0.######E0", symbols)
            return df.format(cleanValue).replace("E", "e")
        }

        return when (precision) {
            DecimalPrecision.AUTO -> {
                if (cleanValue == floor(cleanValue) && absVal < 1e15) {
                    val df = DecimalFormat("#,##0", symbols)
                    df.format(cleanValue)
                } else {
                    val df = DecimalFormat("#,##0.########", symbols)
                    val formatted = df.format(cleanValue)
                    formatted
                }
            }
            else -> {
                val digits = precision.digits
                val bd = BigDecimal(cleanValue.toString()).setScale(digits, RoundingMode.HALF_UP)
                val pattern = "#,##0." + "0".repeat(digits)
                val df = DecimalFormat(pattern, symbols)
                df.format(bd.toDouble())
            }
        }
    }
}
