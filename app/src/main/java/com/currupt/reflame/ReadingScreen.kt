package com.currupt.reflame

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currupt.reflame.ui.theme.RΞTheme

// --- Data Models ---

data class ReadingTitle(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val description: String = "",
    val type: String,
    val genres: List<String>,
    val latestChapter: String,
    val updateTime: String,
    val status: ReadingStatus = ReadingStatus.NOT_STARTED,
    val progress: Int = 0,
    val isEarlyAccess: Boolean = false,
    val isHot: Boolean = false,
    val isNew: Boolean = false,
    val isTrending: Boolean = false,
    val artworkColor: Color = Color.DarkGray
)

enum class ReadingStatus {
    NOT_STARTED, READING, COMPLETED, LOCKED
}

// --- Mock Data ---

object ReadingMockData {
    val featuredTitle = ReadingTitle(
        id = "1",
        title = "The Law Of Being Friends With A Male",
        subtitle = "Rise from the weakest to the strongest",
        description = "Jay is a guy that wanted to talk with his friend about their secret crushes. When Jay was about to confess who he has a crush on, his friend Jiwoon, whose crush is actually towards...",
        type = "Comics",
        genres = listOf("Romance", "Drama", "School Life"),
        latestChapter = "Chapter 212",
        updateTime = "2h ago",
        isHot = true,
        artworkColor = Color(0xFF2C3E50)
    )

    val titles = listOf(
        ReadingTitle("2", "Hidden Fire", type = "Manhwa", genres = listOf("Fantasy"), latestChapter = "Chapter 16", updateTime = "about 13 hours ago", isEarlyAccess = true, artworkColor = Color(0xFF311B92)),
        ReadingTitle("3", "My Husband Didn't Want...", type = "Manhwa", genres = listOf("Action"), latestChapter = "Chapter 12", updateTime = "about 14 hours ago", isTrending = true, artworkColor = Color(0xFF0D47A1)),
        ReadingTitle("4", "The Beginning After The End", type = "Manhwa", genres = listOf("Isekai"), latestChapter = "Ch. 175", updateTime = "3h ago", isNew = true, artworkColor = Color(0xFF004D40)),
        ReadingTitle("5", "Eleceed", type = "Manhwa", genres = listOf("Comedy"), latestChapter = "Ch. 260", updateTime = "5h ago", isHot = true, artworkColor = Color(0xFF1B5E20)),
        ReadingTitle("6", "One Piece", type = "Manga", genres = listOf("Adventure"), latestChapter = "Ch. 1100", updateTime = "2d ago", status = ReadingStatus.READING, progress = 85, artworkColor = Color(0xFFB71C1C)),
        ReadingTitle("7", "Berserk", type = "Manga", genres = listOf("Dark Fantasy"), latestChapter = "Ch. 375", updateTime = "1w ago", artworkColor = Color(0xFF212121)),
        ReadingTitle("8", "Lore Olympus", type = "Webcomic", genres = listOf("Romance"), latestChapter = "Ch. 250", updateTime = "12h ago", artworkColor = Color(0xFF880E4F)),
        ReadingTitle("9", "Batman: The World", type = "Comic", genres = listOf("Action"), latestChapter = "Issue 1", updateTime = "1mo ago", artworkColor = Color(0xFF000000))
    )
}

// --- UI Components ---

@Composable
fun ReadingHomeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        ReadingHomeHeader(onBackClick)
        
        AnnouncementBar(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        FeaturedHero(
            title = ReadingMockData.featuredTitle,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContentRail("New Series:", ReadingMockData.titles.filter { it.isNew || it.isTrending })
        ContentRail("🔥 HOT MANHWA", ReadingMockData.titles.filter { it.type == "Manhwa" && it.isHot })
        ContentRail("⚡ EARLY ACCESS", ReadingMockData.titles.filter { it.isEarlyAccess })
        ContentRail("🔥 HOT MANGA", ReadingMockData.titles.filter { it.type == "Manga" })
        ContentRail("🦸 HOT COMICS", ReadingMockData.titles.filter { it.type == "Comic" })

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
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
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
    title: ReadingTitle,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
            .clip(RoundedCornerShape(12.dp))
            .clickable {},
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
                            colors = listOf(title.artworkColor.copy(alpha = 0.6f), Color.Transparent)
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
                        text = title.type.uppercase(),
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
    titles: List<ReadingTitle>,
    modifier: Modifier = Modifier
) {
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
            items(titles) { item -> ReadingCard(item) }
        }
    }
}

@Composable
fun ReadingCard(title: ReadingTitle) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, animationSpec = tween(100), label = "card_scale")

    Column(modifier = Modifier.width(160.dp).scale(scale).clickable(interactionSource = interactionSource, indication = null, onClick = {})) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(8.dp))
                .background(title.artworkColor.copy(alpha = 0.1f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)))))
            
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp),
                color = Color(0xFFB71C1C).copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "${title.type} +18",
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
                    Text(title.latestChapter, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 10.sp))
                    Text(title.updateTime, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f)))
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Chapter 7", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 10.sp))
                    Text("8 days ago", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = Color.White.copy(alpha = 0.4f)))
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
            ReadingHomeScreen(onBackClick = {})
        }
    }
}
