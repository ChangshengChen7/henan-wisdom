package com.henan.wisdom.core.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.henan.wisdom.core.ui.theme.*

/**
 * 设置Screen
 * 学习目标设置、提醒设置、主题切换
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    dailyGoal: Int = 10,
    reminderEnabled: Boolean = true,
    reminderTime: String = "08:00",
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onDailyGoalChange: (Int) -> Unit = {},
    onReminderEnabledChange: (Boolean) -> Unit = {},
    onReminderTimeChange: (String) -> Unit = {},
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showGoalDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 学习目标设置
            item {
                SettingsSection(title = "学习目标") {
                    SettingsItem(
                        icon = Icons.Default.Flag,
                        title = "每日目标",
                        subtitle = "$dailyGoal 张卡片/天",
                        onClick = { showGoalDialog = true }
                    )
                }
            }
            
            // 提醒设置
            item {
                SettingsSection(title = "提醒设置") {
                    SettingsSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "每日提醒",
                        subtitle = if (reminderEnabled) "每天 $reminderTime 提醒学习" else "已关闭",
                        checked = reminderEnabled,
                        onCheckedChange = onReminderEnabledChange
                    )
                    
                    if (reminderEnabled) {
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                        
                        SettingsItem(
                            icon = Icons.Default.Schedule,
                            title = "提醒时间",
                            subtitle = reminderTime,
                            onClick = { showTimeDialog = true }
                        )
                    }
                }
            }
            
            // 主题设置
            item {
                SettingsSection(title = "外观") {
                    ThemeSelector(
                        currentTheme = themeMode,
                        onThemeChange = onThemeModeChange
                    )
                }
            }
            
            // 关于
            item {
                SettingsSection(title = "关于") {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "版本",
                        subtitle = "1.0.0",
                        onClick = { }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                    
                    SettingsItem(
                        icon = Icons.Default.Description,
                        title = "隐私政策",
                        subtitle = "查看我们的隐私政策",
                        onClick = { }
                    )
                }
            }
        }
    }
    
    // 每日目标对话框
    if (showGoalDialog) {
        GoalSettingDialog(
            currentGoal = dailyGoal,
            onGoalChange = {
                onDailyGoalChange(it)
                showGoalDialog = false
            },
            onDismiss = { showGoalDialog = false }
        )
    }
    
    // 时间选择对话框
    if (showTimeDialog) {
        TimeSettingDialog(
            currentTime = reminderTime,
            onTimeChange = {
                onReminderTimeChange(it)
                showTimeDialog = false
            },
            onDismiss = { showTimeDialog = false }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryGreen
            )
        )
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "主题",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeMode.entries.forEach { mode ->
                ThemeChip(
                    mode = mode,
                    isSelected = mode == currentTheme,
                    onClick = { onThemeChange(mode) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ThemeChip(
    mode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = when (mode) {
                    ThemeMode.LIGHT -> "浅色"
                    ThemeMode.DARK -> "深色"
                    ThemeMode.SYSTEM -> "跟随系统"
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryGreen,
            selectedLabelColor = Color.White
        ),
        modifier = modifier
    )
}

@Composable
private fun GoalSettingDialog(
    currentGoal: Int,
    onGoalChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var goalValue by remember { mutableStateOf(currentGoal.toString()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("设置每日目标") },
        text = {
            Column {
                Text("设置每天学习的卡片数量")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = goalValue,
                    onValueChange = { goalValue = it.filter { c -> c.isDigit() } },
                    label = { Text("每日目标（张）") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    goalValue.toIntOrNull()?.let { onGoalChange(it) }
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun TimeSettingDialog(
    currentTime: String,
    onTimeChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val times = listOf("07:00", "08:00", "09:00", "12:00", "20:00", "21:00")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("设置提醒时间") },
        text = {
            Column {
                times.forEach { time ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTimeChange(time) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = time == currentTime,
                            onClick = { onTimeChange(time) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(time)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}
