package com.sibstream.digitallab.ui.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.trustbank.client_mobile.R

@Composable
fun BackAction(onBackClicked: () -> Unit) {
    IconButton(onClick = onBackClicked) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Назад",
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun BackActionOnSystemUI(onBackClicked: () -> Unit) {
    IconButton(onClick = onBackClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Назад",
//            tint = MaterialTheme.colorScheme.systemUIContent,
        )
    }
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Поиск",
//            tint = MaterialTheme.colorScheme.topAppBarContent,
        )
    }
}

@Composable
fun DeleteAction(onDeleteClicked: () -> Unit) {
    IconButton(onClick = onDeleteClicked) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Удалить",
//            tint = MaterialTheme.colorScheme.topAppBarContent,
        )
    }
}

@Composable
fun CancelAction(onCancelClicked: () -> Unit) {
    IconButton(onClick = onCancelClicked) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Отменить",
//            tint = MaterialTheme.colorScheme.topAppBarContent,
        )
    }
}

@Composable
fun LogoutAction(onLogoutClicked: () -> Unit) {
    IconButton(onClick = onLogoutClicked) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Выйти",
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun EditAction(onEditClicked: () -> Unit) {
    IconButton(onClick = onEditClicked) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Добавить сведения",
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun SettingsAction(onSettingsClicked: () -> Unit) {
    IconButton(onClick = onSettingsClicked) {
        Icon(
            painter = painterResource(R.drawable.ic_settings_24),
            contentDescription = "Перейти в настройки",
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
fun PreviewIcons() {
    Column {
        BackAction {}
        SearchAction {}
        CancelAction {}
        LogoutAction {}
        SettingsAction {}
    }
}