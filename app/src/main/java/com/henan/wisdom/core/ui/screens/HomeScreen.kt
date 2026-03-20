package com.henan.wisdom.core.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henan.wisdom.core.ui.components.StatCard
import com.henan.wisdom.core.ui.theme.*

/**
 * 首页Screen
 * 今日学习概览、分类选择、每日目标进度
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    todayProgress: Int = 0,
    totalGoal: Int = 10,
    streakDays: Int = 0,
    learnedToday: Int = 0,
    categories: List<String> = listOf("全部", "省情", "经济", "文化", "旅游"),
    selectedCategory: String = "全部",
    onCategorySelected: (String) -> Unit = {},
    onStartStudy: () -> Unit = {},
    onViewProgress: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "智慧河南",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, "设置")
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
            // 今日学习概览卡片
            item {
                TodayOverviewCard(
                    todayProgress = todayProgress,
                    totalGoal = totalGoal,
                    streakDays = streakDays,
                    learnedToday = learnedToday
                )
            }

            // 快速开始按钮
            item {
                QuickStartButton(onClick = onStartStudy)
            }

            // 分类选择标签
            item {
                CategorySelector(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            // 学习目标进度
            item {
                DailyGoalProgress(
                    current = todayProgress,
                    goal = totalGoal
                )
            }
        }
    }
}

@Composable
private fun TodayOverviewCard(
    todayProgress: Int,
    totalGoal: Int,
    streakDays: Int,
    learnedToday: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryGreen, PrimaryGreen.copy(alpha = 0.8f))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "今日学习",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$todayProgress / $totalGoal",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // 连续学习天数
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🔥",
                            fontSize = 24.sp
                        )
                        Text(
                            text = "$streakDays",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "天连续",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 进度条
                LinearProgressIndicator(
                    progress = { (todayProgress.toFloat() / totalGoal).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryGold,
                    trackColor = Color.White.copy(alpha = 0.3f),
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "今日已学习 $learnedToday 个知识点",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun QuickStartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen
        )
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "开始学习",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(
            text = "选择分类",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    text = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryGreen,
            selectedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun DailyGoalProgress(
    current: Int,
    goal: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "每日目标",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(current.toFloat() / goal * 100).toInt()}%",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { (current.toFloat() / goal).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = PrimaryGreen,
                trackColor = PrimaryGreen.copy(alpha = 0.2f),
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "完成 $current / $goal 个卡片",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
