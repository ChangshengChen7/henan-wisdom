package com.henan.wisdom.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.*

/**
 * 学习统计数据
 */
data class StudyStats(
    val totalPoints: Int,
    val masteredCount: Int,
    val learningCount: Int,
    val pendingCount: Int,
    val totalDays: Int,
    val currentStreak: Int
)

/**
 * 统计图表组件
 * 使用 Jetpack Compose Canvas 绘制自定义图表
 */
@Composable
fun StatsChart(
    stats: StudyStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Text(
                text = "学习统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 环形进度图
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 主进度环
                CircularProgressRing(
                    progress = if (stats.totalPoints > 0) 
                        stats.masteredCount.toFloat() / stats.totalPoints else 0f,
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 12.dp,
                    progressColor = PrimaryGreen,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )

                // 统计数字
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatItem(
                        label = "总知识点",
                        value = stats.totalPoints.toString(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    StatItem(
                        label = "已掌握",
                        value = stats.masteredCount.toString(),
                        color = PrimaryGreen
                    )
                    StatItem(
                        label = "学习中",
                        value = stats.learningCount.toString(),
                        color = WarningOrange
                    )
                    StatItem(
                        label = "待学习",
                        value = stats.pendingCount.toString(),
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 连续学习天数
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "连续学习",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${stats.currentStreak} 天",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGold
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(8.dp),
            color = color,
            shape = MaterialTheme.shapes.extraSmall
        ) {}
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

/**
 * 环形进度组件
 * 使用 Jetpack Compose Canvas 绘制
 */
@Composable
fun CircularProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = PrimaryGreen,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    animationDuration: Int = 1000
) {
    var animatedProgress by remember { mutableFloatStateOf(0f) }
    
    // 动画
    LaunchedEffect(progress) {
        animatedProgress = progress
    }
    
    val animatedValue by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = animationDuration),
        label = "progressAnimation"
    )

    Canvas(modifier = modifier) {
        val sweepAngle = animatedValue * 360f
        val stroke = Stroke(
            width = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )
        val diameter = size.minDimension - stroke.width
        val topLeft = Offset(
            (size.width - diameter) / 2,
            (size.height - diameter) / 2
        )

        // 背景环
        drawArc(
            color = backgroundColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(diameter, diameter),
            style = stroke
        )

        // 进度环
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = Size(diameter, diameter),
            style = stroke
        )
    }

    // 中心文字
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${(animatedValue * 100).toInt()}%",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = progressColor
        )
    }
}

/**
 * 柱状图组件
 */
@Composable
fun BarChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    barColor: Color = PrimaryGreen,
    maxValue: Float = 100f
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(60.dp)
                    )
                    AnimatedBar(
                        progress = value / maxValue,
                        color = barColor,
                        modifier = Modifier
                            .weight(1f)
                            .height(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value.toInt().toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    var animatedProgress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(progress) {
        animatedProgress = progress
    }
    
    val animatedValue by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 800),
        label = "barAnimation"
    )

    Canvas(modifier = modifier) {
        val width = size.width * animatedValue
        drawRoundRect(
            color = color,
            size = Size(width, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
        )
    }
}
