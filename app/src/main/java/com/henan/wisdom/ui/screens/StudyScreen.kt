package com.henan.wisdom.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.*

/**
 * 学习页 - StudyScreen
 * 知识卡片学习界面
 */
@Composable
fun StudyScreen(
    onStudyComplete: () -> Unit = {}
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var showAnswer by remember { mutableStateOf(false) }
    var completedCount by remember { mutableIntStateOf(0) }
    
    val totalCards = 10
    val cards = listOf(
        KnowledgeCard("河南省位于中国什么位置？", "河南省位于中国中部，黄河中下游，因大部分地区位于黄河以南而得名。"),
        KnowledgeCard("河南的省会是哪个城市？", "河南省会是郑州市，是中国重要的交通枢纽城市。"),
        KnowledgeCard("河南有哪些著名的历史文化遗迹？", "包括龙门石窟、白马寺、少林寺、殷墟等世界文化遗产。"),
        KnowledgeCard("黄河在河南的地位如何？", "黄河是河南的母亲河，河南大部分地区位于黄河中下游。"),
        KnowledgeCard("河南的人口特点是什么？", "河南是中国人口大省，人口超过1亿，劳动力资源丰富。")
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 进度条
        StudyProgress(
            current = currentIndex,
            total = cards.size,
            modifier = Modifier.padding(16.dp)
        )
        
        // 卡片区域
        if (currentIndex < cards.size) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                FlipCard(
                    question = cards[currentIndex].question,
                    answer = cards[currentIndex].answer,
                    showAnswer = showAnswer,
                    onFlip = { showAnswer = !showAnswer },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // 评分按钮
            if (showAnswer) {
                RatingSection(
                    onRate = { rating ->
                        completedCount++
                        if (currentIndex < cards.size - 1) {
                            currentIndex++
                            showAnswer = false
                        } else {
                            onStudyComplete()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                HintText(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

data class KnowledgeCard(val question: String, val answer: String)

@Composable
private fun StudyProgress(current: Int, total: Int, modifier: Modifier = Modifier) {
    val progress = (current.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "progress"
    )
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "学习进度",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${current + 1} / $total",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrimaryGreen,
                trackColor = PrimaryGreen.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun FlipCard(
    question: String,
    answer: String,
    showAnswer: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (showAnswer) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "rotation"
    )
    
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer { rotationY = rotation }
            .pointerInput(Unit) {
                detectTapGestures { onFlip() }
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (showAnswer) PrimaryGold else PrimaryGreen
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (showAnswer) "💡 答案" else "❓ 问题",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (showAnswer) answer else question,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RatingSection(
    onRate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "你觉得这道题怎么样？",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingButton(
                emoji = "😫",
                label = "太难",
                color = PrimaryRed,
                onClick = { onRate(1) }
            )
            RatingButton(
                emoji = "😕",
                label = "一般",
                color = PrimaryOrange,
                onClick = { onRate(2) }
            )
            RatingButton(
                emoji = "😊",
                label = "简单",
                color = PrimaryGreen,
                onClick = { onRate(3) }
            )
        }
    }
}

@Composable
private fun RatingButton(
    emoji: String,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                Modifier.background(color.copy(alpha = 0.1f))
            )
            .padding(16.dp)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HintText(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "👆 点击卡片查看答案",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}