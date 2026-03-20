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
    
    // ==================== 统计查询 ====================
    
    @Query("SELECT COUNT(*) FROM progress WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM progress WHERE lastReviewDate >= :dayStart")
    suspend fun getReviewCountForDay(dayStart: Long): Int
    
    @Query("SELECT COUNT(*) FROM progress WHERE lastReviewDate >= :dayStart")
    fun getReviewCountForDayFlow(dayStart: Long): Flow<Int>
    
    @Query("SELECT SUM(totalTimeSpent) FROM progress WHERE lastReviewDate >= :dayStart")
    suspend fun getTotalTimeForDay(dayStart: Long): Long?
    
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
}
