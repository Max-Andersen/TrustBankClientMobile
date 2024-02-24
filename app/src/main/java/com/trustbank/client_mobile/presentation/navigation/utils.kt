package com.trustbank.client_mobile.presentation.navigation

import android.os.Bundle
import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController

fun NavController.navigateToDestWithArgs(
    mask: String,
    bundle: Bundle?,
) {
    val destinationId =
        this.findDestination(mask)?.id
            ?: throw Exception("Маршрут до экрана $mask не был найден в стеке!")

    this.navigate(destinationId, bundle)
}

abstract class Navigation {
    // - Начальная точка входа в экран
    abstract val route: String

    // - Маска для маршрутизатора
    open var mask: String = ""

    // - Список аргументов
    open val arguments: List<NamedNavArgument>? = null

    /**
     * Осуществляет переход к экрану.
     */
    open fun routeTo(vararg params: Any?): String {
        // Если нет маски или аргументов, то переход к экрану осуществляется по его основному маршруту
        if (arguments == null) return route
        val argumentsWithParams = arguments!!.zip(params).toMap()
        val required = argumentsWithParams.filter { !it.key.argument.isNullable }
        val optional = argumentsWithParams.filter { it.key.argument.isNullable }
        // Формируем строку с обязательными аргументами
        val requiredArgumentsString =
            if (required.isNotEmpty()) {
                required.values.joinToString(
                    separator = "/",
                    prefix = "/",
                    postfix = "",
                ) { "$it" }
            } else {
                ""
            }
        val optionalArgumentsString =
            if (optional.isNotEmpty()) {
                optional.values.joinToString(
                    separator = "&",
                    prefix = "?",
                    postfix = "",
                ) { "$it=$it" }
            } else {
                ""
            }
        Log.d("required", requiredArgumentsString)
        return "$route$requiredArgumentsString$optionalArgumentsString"
    }

    /**
     * Генерирует маску на основание полученных навигационных аргументов.
     */
    fun generateMaskFromArguments(builder: () -> List<NamedNavArgument>): List<NamedNavArgument> {
        // Получаем список навигационных аргументов
        val arguments = builder()
        // Если не были переданы аргументы
        if (arguments.isEmpty()) mask = route
        // Получаем обязательные и не обязательные аргументы
        Log.d("route", route)
        val required = arguments.filter { !it.argument.isNullable }
        val optional = arguments.filter { it.argument.isNullable }
        // Формируем строку с обязательными аргументами
        val requiredArgumentsString =
            if (required.isNotEmpty()) {
                required.joinToString(
                    separator = "/",
                    prefix = "/",
                    postfix = "",
                ) { "{${it.name}}" }
            } else {
                ""
            }
        Log.d("requiredArgumentsString", requiredArgumentsString)
        val optionalArgumentsString =
            if (optional.isNotEmpty()) {
                optional.joinToString(
                    separator = "&",
                    prefix = "?",
                    postfix = "",
                ) { "$it={$it}" }
            } else {
                ""
            }
        Log.d("optionalArgumentsString", optionalArgumentsString)

        mask = "$route$requiredArgumentsString$optionalArgumentsString"
        return arguments
    }
}