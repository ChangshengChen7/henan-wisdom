package com.henan.wisdom.core.domain.model

/**
 * 学习进度模型
 * 追踪用户的学习统计和进度
 */
data class StudyProgress(
    val totalCards: Int,           // 总卡片数
    val learnedCards: Int,         // 已学习卡片数
    val masteredCards: Int,        // 掌握卡片数
    val dueToday: Int,            // 今日待复习数
    val streakDays: Int,          // 连续学习天数
    val totalReviews: Int,        // 总复习次数
    val lastStudyDate: String?,  // 最后学习日期 (yyyy-MM-dd)
    val todayStudyTime: Long      // 今日学习时长（毫秒）
) {
    /**
     * 学习完成百分比
     */
    val progressPercent: Float
        get() = if (totalCards > 0) (learnedCards.toFloat() / totalCards) * 100 else 0f

    /**
     * 掌握完成百分比
     */
    val masteredPercent: Float
        get() = if (totalCards > 0) (masteredCards.toFloat() / totalCards) * 100 else 0f
}
