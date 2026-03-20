package com.henan.wisdom.core.domain.algorithm

import com.henan.wisdom.core.domain.model.CardRating
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * FSRS (Free Spaced Repetition Scheduler) 算法实现
 * 基于SM-2算法的改进版本
 * 
 * 算法核心：
 * 1. 根据用户评分计算记忆稳定性变化
 * 2. 计算下次复习时间
 * 3. 记录遗忘次数用于调整难度
 */
object FSRAlgorithm {
    
    // 难度映射：评分 -> 难度变化
    private const val DIFFICULTY_INCREASE = 0.1f
    private const val DIFFICULTY_DECREASE = 0.15f
    
    // 稳定度衰减常数
    private const val STABILITY_DECAY = 0.9f
    
    // 最小/最大稳定度
    private const val MIN_STABILITY = 0.1f
    private const val MAX_STABILITY = 10.0f
    
    /**
     * 复习间隔计算结果
     */
    data class ReviewResult(
        val newStability: Float,        // 新稳定度
        val newDifficulty: Float,       // 新难度
        val newInterval: Int,          // 新间隔天数
        val nextReviewDate: Long,      // 下次复习时间戳
        val shouldResetReps: Boolean    // 是否应该重置复习次数
    )
    
    /**
     * 根据评分计算复习结果
     * 
     * @param currentStability 当前稳定度
     * @param currentDifficulty 当前难度
     * @param currentReps 当前复习次数
     * @param currentLapses 当前遗忘次数
     * @param rating 用户评分
     * @return 复习结果
     */
    fun calculateReview(
        currentStability: Float,
        currentDifficulty: Float,
        currentReps: Int,
        currentLapses: Int,
        rating: CardRating
    ): ReviewResult {
        // 如果忘记（AGAIN），需要重置并降低稳定度
        if (rating == CardRating.AGAIN) {
            val newStability = max(MIN_STABILITY, currentStability * 0.5f)
            val newDifficulty = min(5.0f, currentDifficulty + DIFFICULTY_INCREASE)
            val interval = calculateInterval(newStability, newDifficulty, 1, currentLapses + 1)
            return ReviewResult(
                newStability = newStability,
                newDifficulty = newDifficulty,
                newInterval = interval,
                nextReviewDate = System.currentTimeMillis() + (interval * 24 * 60 * 60 * 1000L),
                shouldResetReps = true
            )
        }
        
        // 根据评分调整稳定度
        val stabilityChange = when (rating) {
            CardRating.HARD -> currentStability * 1.2f
            CardRating.GOOD -> currentStability * 1.5f
            CardRating.EASY -> currentStability * 2.0f
            else -> currentStability
        }
        
        val newStability = min(MAX_STABILITY, stabilityChange)
        
        // 根据评分调整难度
        val difficultyChange = when (rating) {
            CardRating.HARD -> currentDifficulty + 0.05f
            CardRating.EASY -> max(1.0f, currentDifficulty - DIFFICULTY_DECREASE)
            else -> currentDifficulty
        }
        val newDifficulty = min(5.0f, max(1.0f, difficultyChange))
        
        // 计算新间隔
        val newReps = currentReps + 1
        val interval = calculateInterval(newStability, newDifficulty, newReps, currentLapses)
        
        return ReviewResult(
            newStability = newStability,
            newDifficulty = newDifficulty,
            newInterval = interval,
            nextReviewDate = System.currentTimeMillis() + (interval * 24 * 60 * 60 * 1000L),
            shouldResetReps = false
        )
    }
    
    /**
     * 计算复习间隔
     * 
     * @param stability 稳定度
     * @param difficulty 难度
     * @param reps 复习次数
     * @param lapses 遗忘次数
     * @return 间隔天数
     */
    private fun calculateInterval(
        stability: Float,
        difficulty: Float,
        reps: Int,
        lapses: Int
    ): Int {
        // 基础间隔 = 稳定度 * 难度系数
        var interval = (stability * (1.0f + difficulty / 5.0f)).roundToInt()
        
        // 根据复习次数调整
        when (reps) {
            1 -> interval = 1
            2 -> interval = 3
            3 -> interval = (7 * stability / 2.0f).roundToInt()
            4 -> interval = (14 * stability / 2.0f).roundToInt()
            5 -> interval = (30 * stability / 2.0f).roundToInt()
            6 -> interval = (60 * stability / 2.0f).roundToInt()
            else -> {
                // 后续复习使用稳定度直接计算
                interval = (stability * 4 * lapses.coerceAtLeast(1)).coerceAtMost(365)
            }
        }
        
        // 确保最小间隔为1天，最大间隔为365天
        return interval.coerceIn(1, 365)
    }
    
    /**
     * 估算记忆稳定性
     * 
     * @param rating 用户评分
     * @param previousInterval 上次间隔
     * @param responseTime 响应时间
     * @return 估算的稳定度
     */
    fun estimateStability(
        rating: CardRating,
        previousInterval: Int,
        responseTime: Long
    ): Float {
        // 响应时间影响（毫秒转秒）
        val responseSeconds = responseTime / 1000.0
        
        // 基础稳定度 = 上次间隔 * 难度系数
        val baseStability = when (rating) {
            CardRating.AGAIN -> 0.1f
            CardRating.HARD -> previousInterval * 0.8
            CardRating.GOOD -> previousInterval * 1.0
            CardRating.EASY -> previousInterval * 1.3
        }
        
        // 响应时间调整（响应越快，稳定度越高）
        val timeAdjustment = when {
            responseSeconds < 2.0 -> 1.2  // 2秒内
            responseSeconds < 5.0 -> 1.0  // 5秒内
            responseSeconds < 10.0 -> 0.9  // 10秒内
            responseSeconds < 30.0 -> 0.8  // 30秒内
            else -> 0.7  // 30秒以上
        }
        
        return (baseStability * timeAdjustment).toFloat().coerceIn(MIN_STABILITY, MAX_STABILITY)
    }
    
    /**
     * 获取学习建议
     * 根据当前状态推荐下次评分
     */
    fun getStudyAdvice(
        currentStability: Float,
        currentDifficulty: Float,
        recentRatings: List<CardRating>
    ): String {
        // 根据历史评分判断学习状态
        val recentGoodCount = recentRatings.count { it == CardRating.GOOD || it == CardRating.EASY }
        val recentHardCount = recentRatings.count { it == CardRating.HARD }
        val recentAgainCount = recentRatings.count { it == CardRating.AGAIN }
        
        return when {
            // 遗忘较多，建议降低难度
            recentAgainCount >= 2 -> "这张卡片比较难，建议先降低难度，多复习几次"
            // 多次困难，建议巩固基础
            recentHardCount >= 3 -> "这张卡片需要更多练习，建议选择'HARD'评分"
            // 学习顺利，建议增加难度
            recentGoodCount >= 5 && currentStability > 3.0f -> "这张卡片掌握得很好，可以尝试'EASY'评分"
            // 稳定度较低，建议选择GOOD
            currentStability < 1.5f -> "继续选择'GOOD'评分来巩固记忆"
            // 正常状态
            else -> "根据记忆曲线选择合适的评分即可"
        }
    }
}
