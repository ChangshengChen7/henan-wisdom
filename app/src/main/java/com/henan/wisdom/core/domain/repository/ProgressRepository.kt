package com.henan.wisdom.core.domain.repository

import com.henan.wisdom.core.domain.model.StudyProgress
import kotlinx.coroutines.flow.Flow

/**
 * 学习进度仓库接口
 * 定义学习进度相关的数据操作
 */
interface ProgressRepository {
    
    /**
     * 获取学习进度
     * 返回用户的整体学习统计数据
     */
    fun getStudyProgress(): Flow<StudyProgress>
    
    /**
     * 获取今日待复习卡片数
     */
    fun getDueTodayCount(): Flow<Int>
    
    /**
     * 获取连续学习天数
     */
    fun getStreakDays(): Flow<Int>
    
    /**
     * 更新最后学习日期
     */
    suspend fun updateLastStudyDate(date: String)
    
    /**
     * 增加复习次数
     */
    suspend fun incrementReviewCount()
    
    /**
     * 增加今日学习时长
     * @param milliseconds 增加的毫秒数
     */
    suspend fun addStudyTime(milliseconds: Long)
    
    /**
     * 检查并更新连续学习天数
     * 如果昨天学习了但今天还没学习，应该增加连续天数
     */
    suspend fun checkAndUpdateStreak()
}
