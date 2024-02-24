package com.trustbank.client_mobile.presentation.ui.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerBig(){
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun SpacerMedium(){
    Spacer(modifier = Modifier.size(12.dp))
}

@Composable
fun SpacerSmall(){
    Spacer(modifier = Modifier.size(8.dp))
}