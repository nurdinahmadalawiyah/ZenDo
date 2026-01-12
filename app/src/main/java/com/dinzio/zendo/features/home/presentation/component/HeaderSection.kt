package com.dinzio.zendo.features.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.navigation.ZenDoRoutes

@Composable
fun HeaderSection(
    navController: NavController,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_zendo_2),
                contentDescription = stringResource(R.string.zendo_logo),
                modifier = Modifier
                    .height(36.dp)
            )
            IconButton(onClick = {
                navController.navigate(ZenDoRoutes.Profile.route)
            }) {
                Icon(
                    imageVector = Icons.TwoTone.Person,
                    contentDescription = stringResource(R.string.profile),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}