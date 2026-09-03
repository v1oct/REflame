package com.currupt.reflame.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.currupt.reflame.core.MockData
import com.currupt.reflame.core.model.*
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreen(
    onProjectClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 100.dp) // Space for floating bottom nav
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        AnnouncementBoard(
            announcement = MockData.announcements.first()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        HeroCarousel(
            projects = MockData.projects.filter { it.isFeatured },
            onProjectClick = onProjectClick
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Studio Sections
        MockData.sections.forEach { section ->
            val titles = when (section.type) {
                SectionType.FEATURED -> MockData.projects.filter { it.isFeatured }
                SectionType.IN_DEVELOPMENT -> MockData.projects.filter { it.status == ProjectStatus.IN_DEVELOPMENT }
                SectionType.GAMES -> MockData.projects.filter { it.type == ProjectType.GAME }
                SectionType.APPS -> MockData.projects.filter { it.type == ProjectType.APP }
                SectionType.EXPERIMENTS -> MockData.projects.filter { it.type == ProjectType.EXPERIMENT }
                else -> MockData.projects
            }
            
            if (titles.isNotEmpty()) {
                HomeSection(
                    title = section.title,
                    subtitle = section.subtitle,
                    projects = titles,
                    onProjectClick = onProjectClick
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AnnouncementBoard(announcement: Announcement) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Action */ },
        color = Color.White.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "ANNOUNCEMENT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                )
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun HeroCarousel(
    projects: List<Project>,
    onProjectClick: (String) -> Unit
) {
    if (projects.isEmpty()) return
    
    var currentIndex by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(5.seconds)
            currentIndex = (currentIndex + 1) % projects.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF121212))
            .clickable { onProjectClick(projects[currentIndex].slug) }
    ) {
        // Hero Content with Crossfade
        AnimatedContent(
            targetState = projects[currentIndex],
            transitionSpec = {
                fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(1000))
            },
            label = "hero_fade"
        ) { project ->
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = project.heroUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Readability Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = 300f
                            )
                        )
                )
                
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = project.status.name.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                    
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.6f)
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { onProjectClick(project.slug) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text("VIEW PROJECT", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        // Indicators
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            projects.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(if (index == currentIndex) 12.dp else 6.dp)
                        .background(
                            if (index == currentIndex) Color.White else Color.White.copy(alpha = 0.3f),
                            CircleShape
                        )
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun HomeSection(
    title: String,
    subtitle: String?,
    projects: List<Project>,
    onProjectClick: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    )
                }
            }
            Text(
                text = "SEE ALL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier.clickable { /* Navigate to Projects with filter */ }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(projects) { project ->
                ProjectCard(project = project, onClick = { onProjectClick(project.slug) })
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1A1A1A))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = project.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Progress Bar if in development
            if (project.status == ProjectStatus.IN_DEVELOPMENT) {
                LinearProgressIndicator(
                    progress = { project.progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter),
                    color = Color.White,
                    trackColor = Color.Transparent
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = project.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        Text(
            text = project.type.name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White.copy(alpha = 0.4f)
            )
        )
    }
}
