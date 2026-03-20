package com.henan.wisdom.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.*

/**
 * 首页 - HomeScreen
 * 精美的学习概览页面
 */
@Composable
fun HomeScreen(
    onNavigateToStudy: () -> Unit = {},
    onNavigateToCategory: (String) -> Unit = {}
) {
    val todayLearned = 15
    val totalCards = 67
    val reviewDue = 8
    val streakDays = 7
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GreetingSection(userName = "学习者", streakDays = streakDays)
        StatsSection(todayLearned = todayLearned, totalCards = totalCards, reviewDue = reviewDue)
        QuickStartButton(onClick = onNavigateToStudy, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        CategorySection(
            categories = listOf(
                Category("历史", "🏛️", 25, PrimaryGreen),
                Category("地理", "🗺️", 15, PrimaryGold),
                Category("文化", "🎭", 13, PrimaryOrange),
                Category("经济", "💰", 14, PrimaryRed)
            ),
            onCategoryClick = onNavigateToCategory
        )
        DailyGoalSection(current = todayLearned, goal = 20, modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun GreetingSection(userName: String, streakDays: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreen.copy(alpha = 0.8f))))
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "你好，$userName！", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "今天要学习吗？", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.9f))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                    Text(text = "🔥", style = MaterialTheme.typography.headlineMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$streakDays 天", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatsSection(todayLearned: Int, totalCards: Int, reviewDue: Int) {
    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item { MiniStatCard(title = "今日已学", value = todayLearned.toString(), icon = "📚", color = PrimaryGreen) }
        item { MiniStatCard(title = "知识卡片", value = totalCards.toString(), icon = "🎴", color = PrimaryGold) }
        item { MiniStatCard(title = "待复习", value = reviewDue.toString(), icon = "⏰", color = PrimaryOrange) }
    }
}

@Composable
private fun MiniStatCard(title: String, value: String, icon: String, color: Color) {
    Card(modifier = Modifier.width(120.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f))
        }
    }
}

@Composable
private fun QuickStartButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = "▶ 开始学习", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

data class Category(val name: String, val icon: String, val count: Int, val color: Color)

@Composable
private fun CategorySection(categories: List<Category>, onCategoryClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "知识分类", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(categories) { category ->
                CategoryCard(category = category, onClick = { onCategoryClick(category.name) })
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = category.color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = category.icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = category.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = category.color)
            Text(text = "${category.count} 张卡片", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DailyGoalSection(current: Int, goal: Int, modifier: Modifier = Modifier) {
    val progress = (current.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = "progress")
    
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "每日目标", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "$current / $goal", style = MaterialTheme.typography.titleMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = PrimaryGreen,
                trackColor = PrimaryGreen.copy(alpha = 0.2f)
            )
            if (current >= goal) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎉", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "今日目标已完成！", style = MaterialTheme.typography.bodyMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}