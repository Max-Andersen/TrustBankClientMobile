package com.sibstream.digitallab.ui.topbar

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_MEDIUM
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleLevelAppBar(
    title: String,
    elevation: Dp = 0.dp,
    onBackClick: (() -> Unit) = {},
    isTitleCentered: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    backButtonRequired: Boolean = true,
) {
    val backComp: (@Composable () -> Unit) =
        if (backButtonRequired) {
            { BackAction(onBackClicked = onBackClick) }
        } else {
            {}
        }

    Surface(shadowElevation = elevation) {
        TopAppBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .statusBarsPadding(),
            title = {
                if (isTitleCentered) {
                    Text(
                        text = title,
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                } else {
                    Text(text = title, style = MaterialTheme.typography.headlineSmall)
                }
            },
            navigationIcon = backComp,
            actions = actions,
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary,
                ),
        )
    }
}

@Composable
fun TwoLevelAppBar(
    title: String,
    description: String,
    elevation: Dp = 0.dp,
    onBackClick: (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        // Убирает тень
        Surface(shadowElevation = 0.dp) {
            SingleLevelAppBar(
                title = title,
                elevation = elevation,
                onBackClick = onBackClick,
                actions = actions,
            )
        }
        Text(
            modifier =
                Modifier.padding(
                    start = PADDING_BIG,
                    end = PADDING_BIG,
                    bottom = PADDING_MEDIUM,
                ),
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun ThreeLevelAppBar(
    title: String,
    description: String,
    filter: String,
    elevation: Dp = 0.dp,
    isHighlight: Boolean = false,
    onBackClick: (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    descriptionIcon: @Composable () -> Unit = {},
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        // Убирает тень
        Surface(shadowElevation = 0.dp) {
            SingleLevelAppBar(
                title = title,
                elevation = elevation,
                onBackClick = onBackClick,
                actions = actions,
            )
        }
        Text(
            modifier = Modifier.padding(horizontal = PADDING_BIG),
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    if (isHighlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(PADDING_BIG))
            descriptionIcon()
            Text(
                modifier =
                Modifier
                    .then(
                        if (isHighlight) {
                            Modifier
                        } else {
                            Modifier
                                .padding(start = PADDING_MEDIUM)
                        },
                    )
                    .fillMaxWidth()
                    .padding(end = PADDING_BIG),
                text = filter,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}


@Preview
@Composable
private fun AppBarsPreviewLightTheme() {
    TrustBankClientMobileTheme {
        Surface {
            Column {
                SingleLevelAppBar(
                    title = "Трубы",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                    backButtonRequired = true,
                )
                Spacer(modifier = Modifier.height(10.dp))
                TwoLevelAppBar(
                    title = "Трубы",
                    description = "Дорога: Володино - Красный Яр",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                ThreeLevelAppBar(
                    title = "Трубы",
                    description = "Дорога: Володино - Красный Яр",
                    filter = "По возрастанию даты",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                    descriptionIcon = {
//                        Icon(
//                            modifier = Modifier.size(20.dp),
//                            imageVector = Icons.Filled.Sort,
//                            contentDescription = "",
//                            tint = Color.White,
//                        )
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AppBarsPreviewDarkTheme() {
    TrustBankClientMobileTheme {
        Surface {
            Column {
                SingleLevelAppBar(
                    title = "Трубы",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                TwoLevelAppBar(
                    title = "Трубы",
                    description = "Дорога: Володино - Красный Яр",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                ThreeLevelAppBar(
                    title = "Трубы",
                    description = "Дорога: Володино - Красный Яр",
                    filter = "Для редактирования нажмите на иконку карандаша",
                    onBackClick = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Меню",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Информация о приложении",
                                tint = Color.White,
                            )
                        }
                    },
                    descriptionIcon = {},
                    isHighlight = true,
                )
            }
        }
    }
}