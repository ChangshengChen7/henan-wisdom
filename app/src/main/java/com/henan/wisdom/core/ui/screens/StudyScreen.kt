package com.henan.wisdom.core.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henan.wisdom.core.ui.components.FlipCard
import com.henan.wisdom.core.ui.components.KnowledgeCard
import com.henan.wisdom.core.ui.components.RatingButtons
import com.henan.wisdom.core.ui.components.RatingLevel
import com.henan.wisdom.core.ui.theme.*

/**
 * 学习Screen
 * 知识卡片展示、评分按钮、进度指示器、完成动画
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    currentCard: KnowledgeCard?,
    currentIndex: Int = 0,
    totalCards: Int = 0,
    isFlipped: Boolean = false,
    onFlip: () -> Unit = {},
    onRatingSelected: (RatingLevel) -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showCompletionAnimation by remember { mutableStateOf(false) }
    
    // 检查是否完成
    LaunchedEffect(currentCard) {
        if (currentCard == null && totalCards > 0) {
            showCompletionAnimation = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("学习")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, "关闭")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showCompletionAnimation) {
                // 完成动画
                CompletionAnimation(
                    onFinish = { showCompletionAnimation = false }
                )
            } else if (currentCard != null) {
                // 学习卡片
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 进度指示器
                    ProgressIndicator(
                        current = currentIndex + 1,
                        total = totalCards,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 翻转卡片
                    FlipCard(
                        card = currentCard,
                        onFlip = onFlip,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 评分按钮（翻转到背面后显示）
                    AnimatedVisibility(
                        visible = isFlipped,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        RatingButtons(
                            selectedRating = null,
                            onRatingSelected = { rating ->
                                onRatingSelected(rating)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // 如果还没翻转，显示提示
                    if (!isFlipped) {
                        Text(
                            text = "点击卡片查看答案",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            } else {
                // 没有卡片
                EmptyState()
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "第 $current / $total 张",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Text(
                text = "${(current.toFloat() / total * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { (current.toFloat() / total).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PrimaryGreen,
            trackColor = PrimaryGreen.copy(alpha = 0.2f),
        )
    }
}

@Composable
private fun CompletionAnimation(
    onFinish: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "completionScale"
    )
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉",
            fontSize = (80 * scale).sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "太棒了！",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "今日学习任务已完成",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen
            )
        ) {
            Text("返回首页")
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = PrimaryGreen
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "暂无学习内容",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "今天的卡片已经学完啦！",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}
