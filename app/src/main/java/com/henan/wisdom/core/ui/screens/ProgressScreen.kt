package com.henan.wisdom.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henan.wisdom.core.ui.components.StatCard
import com.henan.wisdom.core.ui.theme.*

/**
 * 进度Screen
 * 学习统计图表、日历热力图、成就徽章
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    totalCards: Int = 100,
    learnedCards: Int = 45,
    streakDays: Int = 7,
    totalReviews: Int = 200,
    todayStudyTime: Long = 1800000, // 毫秒
    calendarData: Map<String, Int> = emptyMap(), // 日期 -> 学习数量
    achievements: List<Achievement> = emptyList(),
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("学习进度") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 统计卡片
            item {
                StatsSection(
                    totalCards = totalCards,
                    learnedCards = learnedCards,
                    streakDays = streakDays,
                    totalReviews = totalReviews,
                    todayStudyTime = todayStudyTime
                )
            }
            
            // 日历热力图
            item {
                CalendarHeatmap(
                    calendarData = calendarData,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // 成就徽章
            item {
                AchievementsSection(
                    achievements = achievements.ifEmpty {
                        // 默认成就
                        listOf(
                            Achievement("初学者", "完成第一次学习", "🌱", false),
                            Achievement("连续3天", "连续学习3天", "🔥", true),
                            Achievement("学习达人", "学习50张卡片", "📚", false),
                            Achievement("完美主义", "连续7天完成目标", "⭐", false)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun StatsSection(
    totalCards: Int,
    learnedCards: Int,
    streakDays: Int,
    totalReviews: Int,
    todayStudyTime: Long
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "学习统计",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "学习进度",
                value = "$learnedCards/$totalCards",
                subtitle = "张卡片",
                gradientColors = listOf(PrimaryGreen, PrimaryGreen.copy(alpha = 0.7f)),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "连续天数",
                value = "$streakDays",
                subtitle = "天 🔥",
                gradientColors = listOf(PrimaryOrange, PrimaryOrange.copy(alpha = 0.7f)),
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "复习次数",
                value = "$totalReviews",
                subtitle = "次",
                gradientColors = listOf(InfoBlue, InfoBlue.copy(alpha = 0.7f)),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "今日学习",
                value = formatStudyTime(todayStudyTime),
                subtitle = "",
                gradientColors = listOf(PrimaryGold, PrimaryGold.copy(alpha = 0.7f)),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CalendarHeatmap(
    calendarData: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "学习日历",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 星期标题
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 日历网格（简化版，显示30天）
            val weeks = (0..4).toList() // 5周
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (0..6).forEach { dayOfWeek ->
                            val dayIndex = week * 7 + dayOfWeek
                            val intensity = (dayIndex % 5).coerceIn(0, 4) // 0-4的学习强度
                            
                            CalendarCell(
                                intensity = intensity,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 图例
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "少",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                listOf(0.2f, 0.4f, 0.6f, 0.8f, 1f).forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(PrimaryGreen.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = "多",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun CalendarCell(
    intensity: Int,
    modifier: Modifier = Modifier
) {
    val alpha = when (intensity) {
        0 -> 0.1f
        1 -> 0.3f
        2 -> 0.5f
        3 -> 0.7f
        else -> 1f
    }
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(PrimaryGreen.copy(alpha = alpha))
    )
}

@Composable
private fun AchievementsSection(
    achievements: List<Achievement>
) {
    Column {
        Text(
            text = "成就徽章",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementBadge(achievement = achievement)
            }
        }
    }
}

@Composable
private fun AchievementBadge(
    achievement: Achievement
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) 
                SurfaceLight 
            else 
                SurfaceLight.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) 
                            PrimaryGold.copy(alpha = 0.2f) 
                        else 
                            Color.Gray.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.icon,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = achievement.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (achievement.isUnlocked) TextPrimary else TextSecondary
            )
            
            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = TextSecondary,
                maxLines = 2
            )
        }
    }
}

private fun formatStudyTime(milliseconds: Long): String {
    val minutes = milliseconds / 60000
    return if (minutes >= 60) {
        "${minutes / 60}小时${minutes % 60}分"
    } else {
        "${minutes}分钟"
    }
}

/**
 * 成就数据类
 */
data class Achievement(
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean
)
