package com.henan.wisdom.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 学习进度数据访问对象
 * 提供学习进度和统计的CRUD操作
 */
@Dao
interface ProgressDao {
    
    // ==================== 基础 CRUD ====================
    
    @Query("SELECT * FROM progress ORDER BY updatedAt DESC")
    fun getAllProgress(): Flow<List<ProgressEntity>>
    
    @Query("SELECT * FROM progress WHERE cardId = :cardId")
    suspend fun getProgressByCardId(cardId: String): ProgressEntity?
    
    @Query("SELECT * FROM progress WHERE cardId = :cardId")
    fun getProgressByCardIdFlow(cardId: String): Flow<ProgressEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity): Long
    
    @Update
    suspend fun updateProgress(progress: ProgressEntity)
    
    @Delete
    suspend fun deleteProgress(progress: ProgressEntity)
    
    @Query("DELETE FROM progress WHERE cardId = :cardId")
    suspend fun deleteProgressByCardId(cardId: String)
    
    @Query("DELETE FROM progress")
    suspend fun deleteAllProgress()
    
    // ==================== 状态查询 ====================
    
    /**
     * 获取指定状态的学习进度
     * status: new, learning, review, mastered
     */
    @Query("SELECT * FROM progress WHERE status = :status ORDER BY updatedAt DESC")
    fun getProgressByStatus(status: String): Flow<List<ProgressEntity>>
    
    /**
     * 获取今日已复习的卡片
     */
    @Query("SELECT * FROM progress WHERE lastReviewDate >= :todayStart AND lastReviewDate < :todayEnd")
    fun getTodayReviewedCards(todayStart: Long, todayEnd: Long): Flow<List<ProgressEntity>>
    
    /**
     * 获取指定用户的进度
     */
    @Query("SELECT * FROM progress WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getProgressByUser(userId: String): Flow<List<ProgressEntity>>
    
    // ==================== 统计查询 ====================
    
    @Query("SELECT COUNT(*) FROM progress WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM progress WHERE lastReviewDate >= :dayStart")
    suspend fun getReviewCountForDay(dayStart: Long): Int
    
    @Query("SELECT COUNT(*) FROM progress WHERE lastReviewDate >= :dayStart")
    fun getReviewCountForDayFlow(dayStart: Long): Flow<Int>
    
    @Query("SELECT SUM(totalTimeSpent) FROM progress WHERE lastReviewDate >= :dayStart")
    suspend fun getTotalTimeForDay(dayStart: Long): Long?
    
    @Query("SELECT SUM(totalTimeSpent) FROM progress WHERE lastReviewDate >= :dayStart")
    fun getTotalTimeForDayFlow(dayStart: Long): Flow<Long?>
    
    /**
     * 获取连续学习天数
     */
    @Query("SELECT MAX(streakDays) FROM progress")
    fun getMaxStreakDays(): Flow<Int>
    
    /**
     * 获取用户总学习天数
     */
    @Query("SELECT COUNT(DISTINCT DATE(lastReviewDate / 1000, 'unixepoch')) FROM progress WHERE lastReviewDate > 0")
    fun getTotalStudyDays(): Flow<Int>
    
    // ==================== 日历数据查询 ====================
    
    /**
     * 获取指定日期范围内的学习记录
     * 用于日历视图展示
     */
    @Query("SELECT * FROM progress WHERE lastReviewDate >= :startDate AND lastReviewDate < :endDate ORDER BY lastReviewDate DESC")
    fun getProgressInRange(startDate: Long, endDate: Long): Flow<List<ProgressEntity>>
    
    /**
     * 获取每日复习数量（用于热力图）
     * 返回日期和对应的复习卡片数量
     */
    @Query("""
        SELECT DATE(lastReviewDate / 1000, 'unixepoch') as date, COUNT(*) as count 
        FROM progress 
        WHERE lastReviewDate >= :startDate 
        GROUP BY DATE(lastReviewDate / 1000, 'unixepoch')
        ORDER BY date ASC
    """)
    fun getDailyReviewCounts(startDate: Long): Flow<List<DailyCount>>
    
    /**
     * 获取每日学习时间（用于统计）
     */
    @Query("""
        SELECT DATE(lastReviewDate / 1000, 'unixepoch') as date, SUM(totalTimeSpent) as totalTime
        FROM progress 
        WHERE lastReviewDate >= :startDate 
        GROUP BY DATE(lastReviewDate / 1000, 'unixepoch')
        ORDER BY date ASC
    """)
    fun getDailyStudyTime(startDate: Long): Flow<List<DailyTime>>
    
    // ==================== 成就查询 ====================
    
    /**
     * 获取用户总复习次数
     */
    @Query("SELECT SUM(totalReviews) FROM progress WHERE lastReviewDate > 0")
    fun getTotalReviews(): Flow<Int?>
    
    /**
     * 获取用户总正确次数
     */
    @Query("SELECT SUM(correctReviews) FROM progress WHERE lastReviewDate > 0")
    fun getTotalCorrect(): Flow<Int?>
    
    /**
     * 获取平均正确率
     */
    @Query("SELECT CAST(SUM(correctReviews) AS FLOAT) / CAST(SUM(totalReviews) AS FLOAT) * 100 FROM progress WHERE lastReviewDate > 0 AND totalReviews > 0")
    fun getAverageAccuracy(): Flow<Float?>
    
    // ==================== 批量操作 ====================
    
    /**
     * 批量更新学习状态
     */
    @Query("UPDATE progress SET status = :status, updatedAt = :updatedAt WHERE cardId IN (:cardIds)")
    suspend fun updateProgressStatus(cardIds: List<String>, status: String, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * 删除指定用户的所有进度
     */
    @Query("DELETE FROM progress WHERE userId = :userId")
    suspend fun deleteProgressByUser(userId: String)
    
    /**
     * 重置所有进度
     */
    @Query("UPDATE progress SET status = 'new', totalReviews = 0, correctReviews = 0, streakDays = 0, updatedAt = :updatedAt")
    suspend fun resetAllProgress(updatedAt: Long = System.currentTimeMillis())
}

/**
 * 每日复习数量（用于日历热力图）
 */
data class DailyCount(
    val date: String,  // 日期字符串 YYYY-MM-DD
    val count: Int     // 复习数量
)

/**
 * 每日学习时间（用于统计）
 */
data class DailyTime(
    val date: String,      // 日期字符串 YYYY-MM-DD
    val totalTime: Long   // 学习时间（毫秒）
)
