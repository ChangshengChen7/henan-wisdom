package com.henan.wisdom.core.data.repository

import com.henan.wisdom.core.data.local.ProgressDao
import com.henan.wisdom.core.data.local.ProgressEntity
import com.henan.wisdom.core.domain.model.StudyProgress
import com.henan.wisdom.core.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

/**
 * 学习进度仓库实现
 * 实现 Domain Layer 定义的 ProgressRepository 接口
 */
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : ProgressRepository {
    
    // ==================== 实现 ProgressRepository 接口 ====================
    
    override fun getStudyProgress(): Flow<StudyProgress> {
        return progressDao.getAllProgress().map { entities ->
            val totalReviews = entities.size
            val mastered = entities.count { it.status == "mastered" }
            val learning = entities.count { it.status == "learning" }
            
            StudyProgress(
                totalCards = totalReviews,
                learnedCards = mastered + learning,
                masteredCards = mastered,
                dueToday = 0,
                streakDays = 0,
                totalReviews = totalReviews,
                lastStudyDate = null,
                todayStudyTime = 0
            )
        }
    }
    
    override fun getDueTodayCount(): Flow<Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        return progressDao.getReviewCountForDayFlow(todayStart)
    }
    
    override fun getStreakDays(): Flow<Int> {
        return progressDao.getMaxStreakDays()
    }
    
    override suspend fun updateLastStudyDate(date: String) {
        // 更新最后学习日期到 DataStore 或数据库
        // 简化实现
    }
    
    override suspend fun incrementReviewCount() {
        // 增加复习次数
        // 简化实现
    }
    
    override suspend fun addStudyTime(milliseconds: Long) {
        // 增加学习时长
        // 简化实现
    }
    
    override suspend fun checkAndUpdateStreak() {
        // 检查并更新连续学习天数
        // 简化实现
    }
}