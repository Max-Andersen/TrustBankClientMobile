package com.trustbank.client_mobile.presentation.ui.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun OutlinedDoubleField(
    modifier: Modifier = Modifier,
    value: Double?,
    label: String,
    restriction: Double? = null,
    onError: (String, String) -> Unit = { _, _ -> },
    negativeEnabled: Boolean = false,
    onValueChange: (Double?) -> Unit,
    // FIXME: Возвращать Double
    isReq: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isHighlighted: Boolean = false,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
        ),
    icon: (@Composable () -> Unit)? = null,
) {
    // Форматируем отображаемое значение до 2ух знаков после запятой
    var text by remember(value) {
        mutableStateOf(value?.let { "%.2f".format(it) } ?: "")
    }


    // Есть ли минус в начале (нужно когда negativeEnabled == true)
    val withMinus = remember { mutableStateOf(false) }

    val noIntegerRegex = Regex("^-?(,?|\\.?)\\d{2}") // Есть часть после запятой, нет целого

    OutlinedTextField(
        modifier = modifier,
        value = text,
        shape = Shapes().large,
        onValueChange = { textFieldValue ->
            // Замена запятой на точку во избежание ошибок у функции .toDouble() + убирание пробелов
            val currText = textFieldValue.replace(',', '.')

            when {
                restriction != null &&
                        currText.toDoubleOrNull() != null &&
                        (
                                currText.toDouble() > restriction ||
                                        currText.toDouble() < 0 - restriction
                                ) ->
                    onError(
                        label,
                        restriction.toString(),
                    )

                // Вписали пробел
                currText.contains(" ") -> {
                }

                // Если значение пустое
                currText.isEmpty() || currText.isBlank() -> {
                    text = currText
                    withMinus.value = false
                    onValueChange(null)
                }

                // Если есть дробная часть, но нет целой
                textFieldValue.matches(noIntegerRegex) -> {
                    text = ""
                    onValueChange(null)
                }

                // Если стерли запятую/точку её нужно оставить
                (text.contains(',') || text.contains('.')) && !currText.contains('.') -> {
                }

                // Если в поле появился знак "-"
                currText[0] == '-' && !withMinus.value && negativeEnabled -> {
                    withMinus.value = true
                    text = currText
                    onValueChange(null)
                }

                else -> {
                    if (currText[0] != '-') withMinus.value = false // Проверка убрали ли минус

                    // Проверка, что кроме минуса есть число
                    if (withMinus.value && currText.length >= 2 || !withMinus.value) {
                        when (currText.toDoubleOrNull()) {
                            null -> {}
                            // old value
                            else -> {
                                val currDouble = currText.toDouble()
                                text = "%.2f".format(currDouble) // new value
                                onValueChange(currDouble)
                            }
                        }
                    } else {
                        text = ""
                        withMinus.value = false
                        onValueChange(null)
                    }
                }
            }
        },
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
//        colors = textColors,
        minLines = 1,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        isError = isReq,
        readOnly = readOnly,
        enabled = enabled,
        trailingIcon = icon,
    )
}