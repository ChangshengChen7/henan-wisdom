package com.henan.wisdom.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 知识点数据模型
 */
data class KnowledgeCard(
    val id: Int,
    val title: String,
    val content: String,
    val category: String,
    val difficulty: String,
    val tags: List<String> = emptyList()
)

/**
 * 翻转卡片组件
 * 使用 Jetpack Compose 官方动画 API 实现卡片翻转效果
 *
 * 官方文档：https://developer.android.com/jetpack/compose/animation
 */
@Composable
fun FlipCard(
    card: KnowledgeCard,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 使用 animateFloatAsState 实现翻转动画（Jetpack Compose 官方动画 API）
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400
        ),
        label = "flipRotation"
    )

    // 根据翻转状态显示正面或背面
    val showBack = rotation > 90f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable {
                isFlipped = !isFlipped
                onFlip()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // 隐藏背面的镜像效果
                    rotationY = if (showBack) 180f else 0f
                },
            contentAlignment = Alignment.Center
        ) {
            if (!showBack) {
                // 正面
                CardFront(card = card)
            } else {
                // 背面
                CardBack(card = card)
            }
        }
    }
}

/**
 * 卡片正面
 */
@Composable
private fun CardFront(card: KnowledgeCard) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // 分类标签
            AssistChip(
                onClick = { },
                label = { Text(card.category, style = MaterialTheme.typography.labelSmall) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 标题
            Text(
                text = card.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        // 底部提示
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "点击翻转查看详情",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DifficultyBadge(difficulty = card.difficulty)
        }
    }
}

/**
 * 卡片背面
 */
@Composable
private fun CardBack(card: KnowledgeCard) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = card.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 内容
        Text(
            text = card.content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 标签
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            card.tags.take(3).forEach { tag ->
                SuggestionChip(
                    onClick = { },
                    label = { Text(tag, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
    }
}

/**
 * 难度徽章
 */
@Composable
fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty.lowercase()) {
        "easy" -> MaterialTheme.colorScheme.primary
        "hard" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.tertiary
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = when (difficulty.lowercase()) {
                "easy" -> "简单"
                "hard" -> "困难"
                else -> "中等"
            },
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
