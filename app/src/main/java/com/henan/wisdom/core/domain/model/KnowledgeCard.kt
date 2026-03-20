package com.henan.wisdom.core.domain.model

/**
 * 知识卡片模型
 * 代表一个学习卡片，包含问题和答案
 */
data class KnowledgeCard(
    val id: Long,
    val question: String,
    val answer: String,
    val category: String,
    val tags: List<String>,
    val difficulty: CardDifficulty,
    val rating: CardRating,
    val nextReviewTime: Long?, // 下次复习时间戳，null表示待学习
    val createdAt: Long,
    val lastReviewedAt: Long?
)

/**
 * 卡片难度等级
 */
enum class CardDifficulty(val level: Int, val label: String) {
    EASY(1, "简单"),
    MEDIUM(2, "中等"),
    HARD(3, "困难");

    companion object {
        fun fromLevel(level: Int): CardDifficulty = 
            entries.find { it.level == level } ?: MEDIUM
    }
}
