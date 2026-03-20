package com.henan.wisdom.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 卡片实体 - 对应一张知识卡片
 * 包含间隔重复学习算法的核心字段
 */
@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey
    val id: String,
    
    val title: String,
    val content: String,
    val answer: String,
    val categoryId: String,
    
    // 间隔重复学习算法字段
    val difficulty: Float = 2.5f,      // 难度系数 (0.1-5.0)
    val stability: Float = 1.0f,        // 稳定度 (0.1-5.0)
    val dueDate: Long = 0L,             // 下次复习时间（毫秒时间戳）
    val reps: Int = 0,                 // 复习次数
    val lapses: Int = 0,               // 遗忘次数
    
    // 元数据
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
