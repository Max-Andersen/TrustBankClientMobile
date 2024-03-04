package com.trustbank.client_mobile.presentation.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.convertToReadable(): String = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("ru")).format(this)

fun Date.convertToReadableTimeLess(): String = SimpleDateFormat("dd.MM.yyyy").format(this)