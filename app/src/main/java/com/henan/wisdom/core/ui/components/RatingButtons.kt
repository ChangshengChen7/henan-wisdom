package com.henan.wisdom.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.*

/**
 * 评分等级
 */
enum class RatingLevel(val value: Int, val label: String, val color: Color, val emoji: String) {
    VERY_HARD(1, "太难了", ErrorRed, "😫"),
    HARD(2, "有点难", WarningOrange, "😕"),
    NORMAL(3, "一般", InfoBlue, "😐"),
    EASY(4, "简单", PrimaryGreen, "😊"),
    VERY_EASY(5, "太简单了", SuccessGreen, "😄")
}

/**
 * 评分回调
 */
typealias OnRatingSelected = (RatingLevel) -> Unit

/**
 * 评分按钮组件
 */
@Composable
fun RatingButtons(
    selectedRating: RatingLevel?,
    onRatingSelected: OnRatingSelected,
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "你对这道题的掌握程度？",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 评分按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingLevel.entries.forEach { level ->
                    RatingButton(
                        level = level,
                        isSelected = selectedRating == level,
                        onClick = { onRatingSelected(level) }
                    )
                }
            }

            // 选中提示
            Spacer(modifier = Modifier.height(12.dp))
            if (selectedRating != null) {
                Text(
                    text = selectedRating.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = selectedRating.color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RatingButton(
    level: RatingLevel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 动画效果
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "ratingScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) level.color.copy(alpha = 0.2f) else Color.Transparent,
        label = "ratingBackground"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) level.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        label = "ratingBorder"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = level.emoji,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = level.value.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) level.color else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * 简化版评分按钮 - 仅三个选项
 */
@Composable
fun SimpleRatingButtons(
    selectedRating: Int?,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 不会
            SimpleRatingButton(
                label = "不会",
                isSelected = selectedRating == 1,
                color = ErrorRed,
                onClick = { onRatingSelected(1) }
            )
            // 一般
            SimpleRatingButton(
                label = "一般",
                isSelected = selectedRating == 2,
                color = WarningOrange,
                onClick = { onRatingSelected(2) }
            )
            // 掌握
            SimpleRatingButton(
                label = "掌握",
                isSelected = selectedRating == 3,
                color = SuccessGreen,
                onClick = { onRatingSelected(3) }
            )
        }
    }
}

@Composable
private fun SimpleRatingButton(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        ),
        label = "simpleRatingScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        label = "simpleRatingBg"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (label) {
                    "不会" -> "😕"
                    "一般" -> "😐"
                    else -> "😄"
                },
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}