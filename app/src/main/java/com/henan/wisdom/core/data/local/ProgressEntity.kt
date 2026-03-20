package com.henan.wisdom.core.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 学习进度实体 - 记录用户的学习进度
 * 包含统计信息和成就相关数据
 */
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("cardId")]
)
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val cardId: String,               // 关联的卡片ID
    val userId: String = "default",    // 用户ID（支持多用户）
    
    // 学习状态
    val status: String = "new",       // new, learning, review, mastered
    
    // 统计信息
    val totalReviews: Int = 0,         // 总复习次数
    val correctReviews: Int = 0,       // 正确复习次数
    val totalTimeSpent: Long = 0L,    // 总学习时间（毫秒）
    
    // 成就相关
    val streakDays: Int = 0,          // 连续学习天数
    val lastReviewDate: Long = 0L,     // 上次复习日期
    
    // 时间戳
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
