package com.currupt.reflame.feature.projects

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currupt.reflame.core.MockData
import com.currupt.reflame.core.model.ContentType
import com.currupt.reflame.feature.home.ContentCard

@Composable
fun ProjectsScreen(
    onContentClick: (String) -> Unit
) {
    var selectedType by remember { mutableStateOf<ContentType?>(null) }
    
    val filteredContent = remember(selectedType) {
        if (selectedType == null) MockData.contents
        else MockData.contents.filter { it.contentType == selectedType }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Page Header
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "CATALOG",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = "Browse the CURRUPT. collection.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.5f)
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedType == null,
                onClick = { selectedType = null },
                label = { Text("ALL") },
                colors = filterChipColors()
            )
            ContentType.entries.filter { it != ContentType.ANNOUNCEMENT }.forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.name) },
                    colors = filterChipColors()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Content Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(filteredContent) { item ->
                ContentCard(
                    content = item,
                    onClick = { onContentClick(item.slug) }
                )
            }
        }
    }
}

@Composable
private fun filterChipColors() = FilterChipDefaults.filterChipColors(
    containerColor = Color.Transparent,
    labelColor = Color.White.copy(alpha = 0.4f),
    selectedContainerColor = Color.White.copy(alpha = 0.1f),
    selectedLabelColor = Color.White,
    selectedLeadingIconColor = Color.White,
    iconColor = Color.White.copy(alpha = 0.4f)
)
