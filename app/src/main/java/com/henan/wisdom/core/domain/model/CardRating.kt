package com.henan.wisdom.core.domain.model

/**
 * 卡片评分（用于间隔重复算法）
 * 基于SM-2算法的评分系统
 */
enum class CardRating(val value: Int, val label: String) {
    /**
     * 完全忘记，需要重新学习
     */
    AGAIN(0, "忘记"),

    /**
     * 回答正确但比较困难
     */
    HARD(1, "困难"),

    /**
     * 回答正确且良好
     */
    GOOD(2, "良好"),

    /**
     * 回答正确且非常简单
     */
    EASY(3, "简单");

    companion object {
        /**
         * 根据数值获取评分
         */
        fun fromValue(value: Int): CardRating =
            entries.find { it.value == value } ?: AGAIN
    }
}

/**
 * 复习记录
 * 记录每次复习的结果
 */
data class ReviewRecord(
    val cardId: Long,
    val rating: CardRating,
    val reviewedAt: Long,        // 复习时间戳
    val responseTime: Long,      // 回答用时（毫秒）
    val previousInterval: Int,   // 之前间隔天数
    val newInterval: Int         // 新间隔天数
)
