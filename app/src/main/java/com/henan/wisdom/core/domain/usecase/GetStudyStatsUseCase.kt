package com.henan.wisdom.core.domain.usecase

import com.henan.wisdom.core.domain.model.StudyProgress
import com.henan.wisdom.core.domain.repository.CardRepository
import com.henan.wisdom.core.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * 获取学习统计用例
 * 整合卡片仓库和进度仓库的数据，返回完整的学习统计
 */
class GetStudyStatsUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val progressRepository: ProgressRepository
) {
    /**
     * 执行获取学习统计
     * @return 包含所有学习相关统计数据的StudyProgress对象
     */
    operator fun invoke(): Flow<StudyProgress> {
        return combine(
            cardRepository.getTotalCardCount(),
            cardRepository.getLearnedCardCount(),
            progressRepository.getDueTodayCount(),
            progressRepository.getStreakDays(),
            progressRepository.getStudyProgress()
        ) { total, learned, dueToday, streak, baseProgress ->
            // 计算掌握（评分>=GOOD）的卡片数
            // 这里简化处理，实际应该从数据库查询
            val mastered = (learned * 0.6).toInt()
            
            StudyProgress(
                totalCards = total,
                learnedCards = learned,
                masteredCards = mastered,
                dueToday = dueToday,
                streakDays = streak,
                totalReviews = baseProgress.totalReviews,
                lastStudyDate = baseProgress.lastStudyDate,
                todayStudyTime = baseProgress.todayStudyTime
            )
        }
    }
}
