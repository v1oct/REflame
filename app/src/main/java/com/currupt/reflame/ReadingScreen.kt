package com.currupt.reflame

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currupt.reflame.core.model.*
import com.currupt.reflame.feature.reading.ReadingHomeState
import com.currupt.reflame.feature.reading.ReadingHomeViewModel
import com.currupt.reflame.feature.reading.TitleDetailsState
import com.currupt.reflame.feature.reading.TitleDetailsViewModel
import com.currupt.reflame.ui.theme.RΞTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Utilities ---

fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color.DarkGray
    }
}

// --- Mock Data (Fallbacks for Previews) ---

object ReadingMockData {
    private val mockChapters = listOf(
        Chapter("c130", "1", 130.0, "The Final Battle Begins", "2h ago", "EARLY_ACCESS", 5),
        Chapter("c129", "1", 129.0, "Unlikely Allies", "1d ago", "NEW"),
        Chapter("c128", "1", 128.0, "The Shadow Rises", "3d ago", "AVAILABLE"),
        Chapter("c127", "1", 127.0, "A Broken Promise", "5d ago", "AVAILABLE"),
        Chapter("c126", "1", 126.0, "Descent into Darkness", "1w ago", "AVAILABLE")
    )

    val featuredTitle = ContentTitle(
        id = "1",
        title = "The Law Of Being Friends With A Male",
        description = "Jay is a guy that wanted to talk with his friend about their secret crushes. When Jay was about to confess who he has a crush on, his friend Jiwoon, whose crush is actually towards...",
        vertical = Vertical.READING,
        type = ContentType.COMIC,
        genres = listOf("Romance", "Drama", "School Life"),
        isHot = true,
        artworkColorHex = "#2C3E50"
    )

    val titles = listOf(
        ContentTitle("2", "Hidden Fire", "Description here", "", "", Vertical.READING, ContentType.MANHWA, listOf("Fantasy"), status = ContentStatus.ONGOING, isEarlyAccess = true, artworkColorHex = "#311B92"),
        ContentTitle("3", "My Husband Didn't Want...", "Description here", "", "", Vertical.READING, ContentType.MANHWA, listOf("Action"), status = ContentStatus.ONGOING, isTrending = true, artworkColorHex = "#0D47A1"),
        ContentTitle("4", "The Beginning After The End", "Description here", "", "", Vertical.READING, ContentType.MANHWA, listOf("Isekai"), status = ContentStatus.ONGOING, isNew = true, artworkColorHex = "#004D40")
    )

    fun getTitleById(id: String): ContentTitle? = (listOf(featuredTitle) + titles).find { it.id == id }

    fun getReaderChapter(titleId: String, chapterId: String): ReaderChapter {
        val title = getTitleById(titleId) ?: featuredTitle
        return ReaderChapter(
            id = chapterId,
            titleId = title.id,
            title = title.title,
            chapterNumber = 128,
            chapterTitle = "The Shadow Rises",
            pages = List(15) { i ->
                ReaderPage(
                    id = "p$i",
                    color = title.artworkColorHex.toColor().copy(alpha = 0.1f + (i % 5) * 0.1f)
                )
            }
        )
    }
}

data class ReaderChapter(
    val id: String,
    val titleId: String,
    val title: String,
    val chapterNumber: Int,
    val chapterTitle: String = "",
    val pages: List<ReaderPage>
)

data class ReaderPage(
    val id: String,
    val imageUrl: String = "",
    val aspectRatio: Float = 0.7f,
    val color: Color = Color.DarkGray
)

// --- UI Components ---

@Composable
fun ReadingHomeScreen(
    onBackClick: () -> Unit,
    onTitleClick: (String) -> Unit,
    viewModel: ReadingHomeViewModel? = null,
    modifier: Modifier = Modifier
) {
    if (viewModel == null) {
        ReadingHomeContent(
            titles = ReadingMockData.titles,
            onBackClick = onBackClick,
            onTitleClick = onTitleClick,
            modifier = modifier
        )
    } else {
        val uiState by viewModel.uiState.collectAsState()
        Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
            when (val state = uiState) {
                is ReadingHomeState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
                is ReadingHomeState.Error -> {
                    Text(text = "Error: ${state.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
                is ReadingHomeState.Success -> {
                    ReadingHomeContent(
                        titles = state.titles,
                        onBackClick = onBackClick,
                        onTitleClick = onTitleClick
                    )
                }
            }
        }
    }
}

@Composable
fun ReadingHomeContent(
    titles: List<ContentTitle>,
    onBackClick: () -> Unit,
    onTitleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ReadingHomeHeader(onBackClick)
        
        AnnouncementBar(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        val featured = titles.find { it.isHot } ?: ReadingMockData.featuredTitle
        FeaturedHero(
            title = featured,
            onClick = { onTitleClick(featured.id) },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContentRail("New Series:", titles.filter { it.isNew || it.isTrending }, onTitleClick)
        ContentRail("🔥 HOT MANHWA", titles.filter { it.type == ContentType.MANHWA && it.isHot }, onTitleClick)
        ContentRail("⚡ EARLY ACCESS", titles.filter { it.isEarlyAccess }, onTitleClick)
        ContentRail("🔥 HOT MANGA", titles.filter { it.type == ContentType.MANGA }, onTitleClick)
        ContentRail("🦸 HOT COMICS", titles.filter { it.type == ContentType.COMIC }, onTitleClick)

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ReadingHomeHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(
                text = "ᏒΞ𝐟𝐥𝐚𝐦𝐞",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            )
        }

        Row {
            IconButton(onClick = {}) { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile", tint = Color.White) }
        }
    }
}

@Composable
fun AnnouncementBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {},
        color = Color.White.copy(alpha = 0.03f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✦ RΞFLAME NEWS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(2.dp).background(Color.White.copy(alpha = 0.3f), CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "8 new chapters just dropped",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "View updates →",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
fun FeaturedHero(
    title: ContentTitle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color(0xFF0A0A0A),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(title.artworkColorHex.toColor().copy(alpha = 0.6f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    color = Color(0xFFB71C1C),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = title.type.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = title.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (title.description.isNotEmpty()) {
                    Text(
                        text = title.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("READ NOW", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Visibility, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "983,838", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.4f)))
                    }
                }
            }
        }
    }
}

@Composable
fun ContentRail(
    title: String,
    titles: List<ContentTitle>,
    onTitleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (titles.isEmpty()) return
    
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                )
                Box(
                    modifier = Modifier.padding(top = 2.dp).width(32.dp).height(3.dp).background(Color(0xFFB71C1C), RoundedCornerShape(1.dp))
                )
            }
            Text(
                text = "View all →",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.White.copy(alpha = 0.4f)),
                modifier = Modifier.clickable {}
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(titles) { item -> ReadingCard(item, onTitleClick) }
        }
    }
}

@Composable
fun ReadingCard(title: ContentTitle, onTitleClick: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, animationSpec = tween(100), label = "card_scale")

    Column(modifier = Modifier.width(160.dp).scale(scale).clickable(interactionSource = interactionSource, indication = null, onClick = { onTitleClick(title.id) })) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(8.dp))
                .background(title.artworkColorHex.toColor().copy(alpha = 0.1f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)))))
            
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp),
                color = Color(0xFFB71C1C).copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "${title.type.name} +18",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(
            text = title.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White.copy(alpha = 0.03f),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Latest", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 10.sp))
                    Text(title.updatedAt ?: "Recently", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f)))
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Available", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 10.sp))
                    Text("Check now", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f)))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ReadingHomePreview() {
    RΞTheme(darkTheme = true) {
        Surface(color = Color.Black) {
            ReadingHomeScreen(onBackClick = {}, onTitleClick = {})
        }
    }
}

@Composable
fun TitleDetailsScreen(
    titleId: String,
    onBackClick: () -> Unit,
    onTitleClick: (String) -> Unit,
    onReadClick: (String, String) -> Unit,
    viewModel: TitleDetailsViewModel? = null,
    modifier: Modifier = Modifier
) {
    if (viewModel == null) {
        val title = ReadingMockData.getTitleById(titleId) ?: ReadingMockData.featuredTitle
        TitleDetailsContent(
            title = title,
            chapters = emptyList(),
            onBackClick = onBackClick,
            onTitleClick = onTitleClick,
            onReadClick = onReadClick,
            modifier = modifier
        )
    } else {
        val uiState by viewModel.uiState.collectAsState()
        Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
            when (val state = uiState) {
                is TitleDetailsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
                is TitleDetailsState.Error -> {
                    Text(text = "Error: ${state.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
                is TitleDetailsState.Success -> {
                    TitleDetailsContent(
                        title = state.title,
                        chapters = state.chapters,
                        onBackClick = onBackClick,
                        onTitleClick = onTitleClick,
                        onReadClick = onReadClick
                    )
                }
            }
        }
    }
}

@Composable
fun TitleDetailsContent(
    title: ContentTitle,
    chapters: List<Chapter>,
    onBackClick: () -> Unit,
    onTitleClick: (String) -> Unit,
    onReadClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TitleDetailsHeader(title, onBackClick)
        
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            TitleDetailsActions(title, onReadClick)
            Spacer(modifier = Modifier.height(24.dp))
            TitleDetailsInfo(title)
            Spacer(modifier = Modifier.height(32.dp))
            ChapterListSection(title, chapters, onReadClick)
            Spacer(modifier = Modifier.height(40.dp))
            ContentRail("YOU MAY ALSO LIKE", ReadingMockData.titles, onTitleClick)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun TitleDetailsHeader(title: ContentTitle, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        // Backdrop Mock Artwork
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(title.artworkColorHex.toColor().copy(alpha = 0.8f), Color.Black)
                    )
                )
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f), Color.Black)
                    )
                )
        )

        // Top Navigation
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        // Title & Metadata Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            StatusBadge(if (title.isHot) "🔥 HOT" else "✦ FEATURED", Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title.title,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${title.type.name} · ${title.genres.joinToString(" · ")}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.6f))
            )
            Text(
                text = "Not started",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.4f)),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun TitleDetailsActions(title: ContentTitle, onReadClick: (String, String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { onReadClick(title.id, "1") },
            modifier = Modifier.weight(1f).height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "START READING",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Surface(
            onClick = {},
            modifier = Modifier.size(48.dp),
            color = Color.White.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.BookmarkAdd, contentDescription = "Library", tint = Color.White)
            }
        }
    }
}

@Composable
private fun TitleDetailsInfo(title: ContentTitle) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = title.description,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f), lineHeight = 22.sp),
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { expanded = !expanded }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoItem("Vertical", title.vertical.name)
            InfoItem("Status", title.status.name)
            InfoItem("Created", title.createdAt?.take(10) ?: "Recently")
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.4f)))
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun ChapterListSection(title: ContentTitle, chapters: List<Chapter>, onReadClick: (String, String) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "CHAPTERS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp, color = Color.White))
            Icon(Icons.AutoMirrored.Rounded.Sort, contentDescription = "Sort", tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (chapters.isEmpty()) {
            Text(text = "No chapters available yet.", color = Color.White.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 16.dp))
        }

        chapters.forEach { chapter ->
            ChapterRow(chapter) { onReadClick(title.id, chapter.id) }
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
        }
    }
}

@Composable
private fun ChapterRow(chapter: Chapter, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Chapter ${chapter.number}${if (!chapter.title.isNullOrEmpty()) ": ${chapter.title}" else ""}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    text = chapter.releaseDate.take(10),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.4f))
                )
            }
            
            when (chapter.accessState) {
                "EARLY_ACCESS" -> {
                    StatusBadge("⚡ ${chapter.coinPrice} Coins", Color(0xFFB71C1C).copy(alpha = 0.8f))
                }
                "NEW" -> {
                    StatusBadge("NEW", Color(0xFFB71C1C))
                }
                "LOCKED" -> {
                    Icon(Icons.Rounded.Lock, contentDescription = "Locked", tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
                }
                else -> {}
            }
        }
    }
}

@Composable
fun StatusBadge(text: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(6.dp), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))) {
        Text(text = text, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TitleDetailsPreview() {
    RΞTheme(darkTheme = true) {
        Surface(color = Color.Black) {
            TitleDetailsScreen(titleId = "1", onBackClick = {}, onTitleClick = {}, onReadClick = { _, _ -> })
        }
    }
}

// --- Reader Screen ---

@Composable
fun ReaderScreen(
    titleId: String,
    chapterId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readerChapter = remember(titleId, chapterId) {
        ReadingMockData.getReaderChapter(titleId, chapterId)
    }
    
    var controlsVisible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    // Auto-hide controls
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3000)
            controlsVisible = false
        }
    }

    // Calculate progress
    val progress = remember {
        derivedStateOf {
            if (readerChapter.pages.isEmpty()) 0
            else {
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                if (totalItems == 0) 0
                else {
                    val firstVisible = listState.firstVisibleItemIndex
                    ((firstVisible.toFloat() / (totalItems - 1)) * 100).toInt().coerceIn(0, 100)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { controlsVisible = !controlsVisible })
            }
    ) {
        // Pages List
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp) // Space for "Next Chapter"
        ) {
            items(readerChapter.pages) { page ->
                ReaderPageItem(page)
            }
            
            item {
                ChapterEndAction()
            }
        }

        // Overlays
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            ReaderTopBar(readerChapter, onBackClick)
        }

        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ReaderBottomBar(readerChapter, progress.value)
        }
    }
}

@Composable
private fun ReaderPageItem(page: ReaderPage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(page.aspectRatio)
            .background(page.color)
    ) {
        // In a real app, this would be an Image component
        Text(
            text = "PAGE ${page.id.removePrefix("p").toInt() + 1}",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White.copy(alpha = 0.2f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ReaderTopBar(chapter: ReaderChapter, onBackClick: () -> Unit) {
    Surface(
        color = Color.Black.copy(alpha = 0.8f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Chapter ${chapter.chapterNumber}${if (chapter.chapterTitle.isNotEmpty()) ": ${chapter.chapterTitle}" else ""}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.6f))
                )
            }
        }
    }
}

@Composable
private fun ReaderBottomBar(chapter: ReaderChapter, progress: Int) {
    var showSettings by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (showSettings) {
            ReaderSettingsPanel(onClose = { showSettings = false })
        }
        
        Surface(
            color = Color.Black.copy(alpha = 0.8f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.ChevronLeft, contentDescription = "Prev", tint = Color.White)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Chapter ${chapter.chapterNumber} · $progress%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                        Text(
                            text = "${chapter.chapterNumber} / 212", // Mock total
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.4f))
                        )
                    }

                    Row {
                        IconButton(onClick = { showSettings = !showSettings }) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Rounded.ChevronRight, contentDescription = "Next", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReaderSettingsPanel(onClose: () -> Unit) {
    Surface(
        color = Color(0xFF121212),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reader Settings",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SettingRow("Reading Direction", "Vertical")
            SettingRow("Page Spacing", "Compact")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Fullscreen", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                Switch(checked = true, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedThumbColor = Color.White))
            }
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.6f)))
    }
}

@Composable
private fun ChapterEndAction() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("NEXT CHAPTER →", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ReaderPreview() {
    RΞTheme(darkTheme = true) {
        Surface(color = Color.Black) {
            ReaderScreen(titleId = "1", chapterId = "c128", onBackClick = {})
        }
    }
}
