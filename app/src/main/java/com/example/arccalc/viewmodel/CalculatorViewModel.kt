package com.example.arccalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.toArgb
import com.example.arccalc.data.AppSettings
import com.example.arccalc.data.ArcCalcDatabase
import com.example.arccalc.data.HistoryEntity
import com.example.arccalc.data.SettingsRepository
import com.example.arccalc.engine.CalculatorEngine
import com.example.arccalc.model.AngleMode
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.ui.theme.WallpaperPresets
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ArcCalcDatabase.getDatabase(application)
    private val historyDao = db.historyDao()
    private val settingsRepo = SettingsRepository(application)

    val settings: StateFlow<AppSettings> = settingsRepo.settings

    val historyList: StateFlow<List<HistoryEntity>> = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _livePreview = MutableStateFlow<String?>(null)
    val livePreview: StateFlow<String?> = _livePreview.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _isScientificExpanded = MutableStateFlow(false)
    val isScientificExpanded: StateFlow<Boolean> = _isScientificExpanded.asStateFlow()

    private val _is2ndActive = MutableStateFlow(false)
    val is2ndActive: StateFlow<Boolean> = _is2ndActive.asStateFlow()

    private var justCalculated = false

    private fun updateLivePreview(expr: String) {
        if (expr.isBlank()) {
            _livePreview.value = null
            return
        }
        val currentAngleMode = settings.value.angleMode
        val precision = settings.value.decimalPrecision
        val eval = CalculatorEngine.evaluate(expr, currentAngleMode, precision)
        if (eval is CalculatorEngine.EvalResult.Success) {
            _livePreview.value = eval.formatted
        } else {
            _livePreview.value = null
        }
    }

    fun onDigit(digit: String) {
        if (justCalculated) {
            _expression.value = digit
            _result.value = ""
            _isError.value = false
            justCalculated = false
        } else {
            _expression.value += digit
            _isError.value = false
        }
        updateLivePreview(_expression.value)
    }

    fun onDot() {
        if (justCalculated) {
            _expression.value = "0."
            _result.value = ""
            _isError.value = false
            justCalculated = false
        } else {
            val expr = _expression.value
            if (expr.isEmpty() || isOperatorOrParen(expr.last())) {
                _expression.value += "0."
            } else {
                // Check if last number token already has a dot
                val tokens = expr.split(Regex("[+\\-×÷%^()]"))
                val lastToken = tokens.lastOrNull() ?: ""
                if (!lastToken.contains(".")) {
                    _expression.value += "."
                }
            }
        }
        updateLivePreview(_expression.value)
    }

    fun onOperator(op: String) {
        if (justCalculated && _result.value.isNotEmpty() && !_isError.value) {
            _expression.value = _result.value + " $op "
            _result.value = ""
            justCalculated = false
        } else {
            val expr = _expression.value.trimEnd()
            if (expr.isNotEmpty()) {
                if (expr.endsWith("+") || expr.endsWith("-") || expr.endsWith("×") || expr.endsWith("÷")) {
                    _expression.value = expr.dropLast(1) + op
                } else {
                    _expression.value = "$expr $op "
                }
            } else if (op == "-") {
                _expression.value = "-"
            }
        }
        _isError.value = false
        updateLivePreview(_expression.value)
    }

    fun onParenthesis(paren: String) {
        if (justCalculated) {
            _expression.value = paren
            _result.value = ""
            justCalculated = false
        } else {
            _expression.value += paren
        }
        _isError.value = false
        updateLivePreview(_expression.value)
    }

    fun onPercent() {
        if (_expression.value.isNotEmpty()) {
            _expression.value += "%"
            updateLivePreview(_expression.value)
        }
    }

    fun onScientificFunction(fn: String) {
        if (justCalculated && _result.value.isNotEmpty() && !_isError.value) {
            _expression.value = fn + _result.value
            _result.value = ""
            justCalculated = false
        } else {
            _expression.value += fn
        }
        _isError.value = false
        updateLivePreview(_expression.value)
    }

    fun onClear() {
        _expression.value = ""
        _result.value = ""
        _livePreview.value = null
        _isError.value = false
        justCalculated = false
    }

    fun onDelete() {
        if (justCalculated) {
            onClear()
            return
        }
        val expr = _expression.value
        if (expr.isNotEmpty()) {
            // If it ends with spaces, drop the full operator with spaces
            _expression.value = if (expr.endsWith(" ")) {
                expr.trimEnd().dropLast(1).trimEnd()
            } else {
                expr.dropLast(1)
            }
            updateLivePreview(_expression.value)
        }
    }

    fun onEquals() {
        val expr = _expression.value.trim()
        if (expr.isBlank()) return

        val currentAngleMode = settings.value.angleMode
        val precision = settings.value.decimalPrecision
        val eval = CalculatorEngine.evaluate(expr, currentAngleMode, precision)

        when (eval) {
            is CalculatorEngine.EvalResult.Success -> {
                _result.value = eval.formatted
                _livePreview.value = null
                _isError.value = false
                justCalculated = true

                // Save to Room history
                viewModelScope.launch {
                    historyDao.insert(
                        HistoryEntity(
                            expression = expr,
                            result = eval.formatted,
                            angleMode = currentAngleMode.name
                        )
                    )
                }
            }
            is CalculatorEngine.EvalResult.Error -> {
                _result.value = eval.message
                _isError.value = true
                justCalculated = true
            }
        }
    }

    fun insertValue(value: String) {
        if (justCalculated) {
            _expression.value = value
            _result.value = ""
            justCalculated = false
        } else {
            _expression.value += value
        }
        updateLivePreview(_expression.value)
    }

    fun selectHistoryItem(item: HistoryEntity) {
        _expression.value = item.expression
        _result.value = item.result
        _isError.value = false
        justCalculated = true
    }

    fun deleteHistoryItem(item: HistoryEntity) {
        viewModelScope.launch {
            historyDao.delete(item)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDao.clearAll()
        }
    }

    fun toggleAngleMode() {
        val current = settings.value.angleMode
        val next = when (current) {
            AngleMode.DEG -> AngleMode.RAD
            AngleMode.RAD -> AngleMode.GRAD
            AngleMode.GRAD -> AngleMode.DEG
        }
        settingsRepo.updateSettings { it.copy(angleMode = next) }
        updateLivePreview(_expression.value)
    }

    fun toggleScientificExpanded() {
        _isScientificExpanded.value = !_isScientificExpanded.value
    }

    fun toggle2nd() {
        _is2ndActive.value = !_is2ndActive.value
    }

    fun updateSettings(transform: (AppSettings) -> AppSettings) {
        settingsRepo.updateSettings(transform)
    }

    fun applyTheme(themeId: String) {
        settingsRepo.updateSettings { it.copy(themeId = themeId) }
    }

    fun applyWallpaper(wallpaperId: String) {
        val wp = WallpaperPresets.find { it.id == wallpaperId }
        val seed = wp?.color?.toArgb()?.toLong() ?: 0xFF73866CL
        settingsRepo.updateSettings {
            it.copy(
                themeId = wallpaperId,
                activeWallpaperId = wallpaperId,
                customSeedHex = seed,
                designFinish = DesignFinish.M3_FLAT,
                adaptWallpaper = true
            )
        }
    }

    fun applyCustomPalette(seedHex: Long, finish: DesignFinish) {
        settingsRepo.updateSettings {
            it.copy(
                themeId = "custom_dynamic",
                customSeedHex = seedHex,
                designFinish = finish
            )
        }
    }

    private fun isOperatorOrParen(c: Char): Boolean {
        return c == '+' || c == '-' || c == '×' || c == '÷' || c == '(' || c == ')' || c == '%' || c == '^'
    }
}
