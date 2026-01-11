package com.dinzio.zendo.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinzio.zendo.core.data.local.EmojiDataSource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZenDoEmojiPicker(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = EmojiDataSource.allEmojis.keys.toList()

    val pagerState = androidx.compose.foundation.pager.rememberPagerState { categories.size }
    val scope = rememberCoroutineScope()

    val searchResults = remember(searchQuery) {
        if (searchQuery.isEmpty()) emptyList()
        else {
            val queryKeywords = searchQuery.lowercase().split(" ")
            EmojiDataSource.allEmojis.values.flatten().filter { emoji ->
                queryKeywords.all { it in emoji.label.lowercase() }
            }.distinctBy { it.char }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search emoji...") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        if (searchQuery.isEmpty()) {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = { Text(title) }
                    )
                }
            }

            androidx.compose.foundation.pager.HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                val categoryEmojis = EmojiDataSource.allEmojis[categories[pageIndex]] ?: emptyList()
                EmojiGrid(emojis = categoryEmojis, onEmojiSelected = onEmojiSelected)
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {
                EmojiGrid(emojis = searchResults, onEmojiSelected = onEmojiSelected)
            }
        }
    }
}

@Composable
fun EmojiGrid(
    emojis: List<com.dinzio.zendo.core.data.local.EmojiModel>,
    onEmojiSelected: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 56.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(emojis) { emoji ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable { onEmojiSelected(emoji.char) }
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji.char, fontSize = 28.sp)
            }
        }
    }
}