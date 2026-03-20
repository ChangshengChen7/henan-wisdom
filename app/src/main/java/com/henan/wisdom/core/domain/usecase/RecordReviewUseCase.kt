package com.henan.wisdom.core.domain.usecase

import com.henan.wisdom.core.domain.model.CardRating
import com.henan.wisdom.core.domain.model.ReviewRecord
import com.henan.wisdom.core.domain.repository.CardRepository
import com.henan.wisdom.core.domain.repository.ProgressRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 记录复习结果用例
 * 处理用户对卡片的评分，更新间隔重复算法的参数
 */
class RecordReviewUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val progressRepository: ProgressRepository
) {
    /**
     * 执行记录复习结果
     * @param cardId 卡片ID
     * @param rating 用户评分
     * @param responseTimeMs 回答用时（毫秒）
     */
    suspend operator fun invoke(
        cardId: Long,
        rating: CardRating,
        responseTimeMs: Long
    ) {
        // 计算新间隔天数（基于SM-2算法简化版）
        val newInterval = calculateNewInterval(rating)
        
        // 更新卡片评分
        cardRepository.updateCardRating(cardId, rating, newInterval)
        
        // 增加复习次数
        progressRepository.incrementReviewCount()
        
        // 增加学习时长
        progressRepository.addStudyTime(responseTimeMs)
        
        // 更新最后学习日期
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        progressRepository.updateLastStudyDate(today)
        
        // 检查并更新连续学习天数
        progressRepository.checkAndUpdateStreak()
    }
    
    /**
     * 基于评分计算新的复习间隔
     * 简化版SM-2算法
     */
    private fun calculateNewInterval(rating: CardRating): Int {
        return when (rating) {
            CardRating.AGAIN -> 1           // 忘记，1分钟后重新学习
            CardRating.HARD -> 1            // 困难，1天后复习
            CardRating.GOOD -> 3            // 良好，3天后复习
            CardRating.EASY -> 7            // 简单，7天后复习
        }
    }
}
