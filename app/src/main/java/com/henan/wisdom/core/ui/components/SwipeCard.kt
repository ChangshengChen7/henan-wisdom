package com.henan.wisdom.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 滑动方向
 */
enum class SwipeDirection {
    LEFT,  // 跳过/不感兴趣
    RIGHT  // 掌握/已学会
}

/**
 * 滑动结果回调
 */
data class SwipeResult(
    val card: KnowledgeCard,
    val direction: SwipeDirection
)

/**
 * 滑动卡片组件
 * 使用 Jetpack Compose 官方手势 API 实现左右滑动效果
 *
 * 官方文档：https://developer.android.com/jetpack/compose/gestures
 */
@Composable
fun SwipeCardStack(
    cards: List<KnowledgeCard>,
    onSwiped: (SwipeResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 从后向前渲染卡片（最下面的先渲染）
        cards.reversed().forEachIndexed { index, card ->
            val isTopCard = index == cards.lastIndex
            if (isTopCard) {
                SwipeableCard(
                    card = card,
                    onSwiped = { direction -> onSwiped(SwipeResult(card, direction)) }
                )
            } else {
                // 非顶层卡片缩放显示
                val scale = 1f - (cards.lastIndex - index) * 0.05f
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(0.7f)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = (2 - index).dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card.title,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // 空状态
        if (cards.isEmpty()) {
            EmptySwipeState()
        }
    }
}

@Composable
private fun SwipeableCard(
    card: KnowledgeCard,
    onSwiped: (SwipeDirection) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300),
        label = "swipeOffset"
    )

    val rotation = (animatedOffset / 20f).coerceIn(-15f, 15f)
    val alpha = 1f - (abs(animatedOffset) / 500f).coerceIn(0f, 0.5f)

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(0.7f)
            .offset { IntOffset(animatedOffset.roundToInt(), 0) }
            .graphicsLayer {
                rotationZ = rotation
                this.alpha = alpha
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > 150 -> {
                                // 向右滑动 - 掌握
                                onSwiped(SwipeDirection.RIGHT)
                            }
                            offsetX < -150 -> {
                                // 向左滑动 - 跳过
                                onSwiped(SwipeDirection.LEFT)
                            }
                            else -> {
                                // 恢复原位
                                offsetX = 0f
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 顶部标签
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(card.category) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // 中间内容
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = card.content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 8
                )
            }

            // 底部滑动提示
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 向左提示
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "跳过",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "跳过",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // 向右提示
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "掌握",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "掌握",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 滑动方向指示器
            SwipeIndicator(
                offsetX = animatedOffset,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun SwipeIndicator(offsetX: Float, modifier: Modifier = Modifier) {
    val alpha = (abs(offsetX) / 100f).coerceIn(0f, 0.8f)

    when {
        offsetX > 50 -> {
            Surface(
                modifier = modifier,
                color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "掌握",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        offsetX < -50 -> {
            Surface(
                modifier = modifier,
                color = MaterialTheme.colorScheme.error.copy(alpha = alpha),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "跳过",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
private fun EmptySwipeState() {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(0.7f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎉",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "太棒了！",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "今日学习已完成",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
