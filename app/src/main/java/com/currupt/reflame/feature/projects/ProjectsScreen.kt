package com.currupt.reflame.feature.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.currupt.reflame.core.MockData
import com.currupt.reflame.core.model.ProjectType
import com.currupt.reflame.feature.home.ProjectCard

@Composable
fun ProjectsScreen(
    onProjectClick: (String) -> Unit
) {
    var selectedType by remember { mutableStateOf<ProjectType?>(null) }
    
    val filteredProjects = remember(selectedType) {
        if (selectedType == null) MockData.projects
        else MockData.projects.filter { it.type == selectedType }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "PROJECTS",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedType == null,
                onClick = { selectedType = null },
                label = { Text("ALL") },
                colors = filterChipColors()
            )
            ProjectType.entries.forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.name) },
                    colors = filterChipColors()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(filteredProjects) { project ->
                ProjectCard(
                    project = project,
                    onClick = { onProjectClick(project.slug) }
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
    selectedLabelColor = Color.White
)
