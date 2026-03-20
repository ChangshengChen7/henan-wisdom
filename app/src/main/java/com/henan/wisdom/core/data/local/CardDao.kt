package com.henan.wisdom.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 卡片数据访问对象
 * 提供卡片的CRUD操作和间隔重复算法查询
 */
@Dao
interface CardDao {
    
    // ==================== 基础 CRUD ====================
    
    @Query("SELECT * FROM cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<CardEntity>>
    
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): CardEntity?
    
    @Query("SELECT * FROM cards WHERE id = :cardId")
    fun getCardByIdFlow(cardId: String): Flow<CardEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)
    
    @Update
    suspend fun updateCard(card: CardEntity)
    
    @Delete
    suspend fun deleteCard(card: CardEntity)
    
    @Query("DELETE FROM cards WHERE id = :cardId")
    suspend fun deleteCardById(cardId: String)
    
    @Query("DELETE FROM cards")
    suspend fun deleteAllCards()
    
    // ==================== 间隔重复查询 ====================
    
    /**
     * 获取到期需要复习的卡片
     * 用于FSRS算法的取卡
     */
    @Query("SELECT * FROM cards WHERE dueDate <= :now ORDER BY dueDate ASC")
    fun getDueCards(now: Long): Flow<List<CardEntity>>
    
    /**
     * 获取今日新卡片
     * 限制每日新增卡片数量
     */
    @Query("SELECT * FROM cards WHERE reps = 0 AND dueDate = 0 ORDER BY createdAt ASC LIMIT :limit")
    fun getNewCards(limit: Int): Flow<List<CardEntity>>
    
    /**
     * 获取指定分类的卡片
     */
    @Query("SELECT * FROM cards WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    fun getCardsByCategory(categoryId: String): Flow<List<CardEntity>>
    
    /**
     * 获取复习中的卡片（已学习但未掌握）
     */
    @Query("SELECT * FROM cards WHERE reps > 0 AND dueDate > :now ORDER BY dueDate ASC")
    fun getLearningCards(now: Long): Flow<List<CardEntity>>
    
    /**
     * 获取已掌握的卡片
     * 稳定度 > 3.0 且复习次数 > 3
     */
    @Query("SELECT * FROM cards WHERE stability >= 3.0 AND reps > 3 ORDER BY updatedAt DESC")
    fun getMasteredCards(): Flow<List<CardEntity>>
    
    /**
     * 获取需要学习的卡片（新卡片 + 到期卡片）
     */
    @Query("SELECT * FROM cards WHERE (reps = 0 AND dueDate = 0) OR (dueDate <= :now AND dueDate > 0) ORDER BY dueDate ASC LIMIT :limit")
    fun getStudyQueueCards(now: Long, limit: Int): Flow<List<CardEntity>>
    
    // ==================== 统计查询 ====================
    
    @Query("SELECT COUNT(*) FROM cards")
    fun getTotalCardsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE reps = 0")
    fun getNewCardsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE dueDate <= :now AND dueDate > 0")
    fun getDueCardsCount(now: Long): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE categoryId = :categoryId")
    fun getCardsCountByCategory(categoryId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE stability >= 3.0 AND reps > 3")
    fun getMasteredCardsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE reps > 0 AND dueDate > :now")
    fun getLearningCardsCount(now: Long): Flow<Int>
    
    // ==================== 批量操作 ====================
    
    /**
     * 批量更新卡片复习数据
     */
    @Query("UPDATE cards SET stability = :stability, difficulty = :difficulty, dueDate = :dueDate, reps = :reps, lapses = :lapses, updatedAt = :updatedAt WHERE id = :cardId")
    suspend fun updateCardReview(
        cardId: String,
        stability: Float,
        difficulty: Float,
        dueDate: Long,
        reps: Int,
        lapses: Int,
        updatedAt: Long = System.currentTimeMillis()
    )
    
    /**
     * 重置所有卡片的学习进度
     */
    @Query("UPDATE cards SET stability = 1.0, difficulty = 2.5, dueDate = 0, reps = 0, lapses = 0, updatedAt = :updatedAt")
    suspend fun resetAllCards(updatedAt: Long = System.currentTimeMillis())
    
    // ==================== 搜索查询 ====================
    
    /**
     * 搜索卡片（按标题或内容）
     */
    @Query("SELECT * FROM cards WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchCards(query: String): Flow<List<CardEntity>>
    
    /**
     * 获取随机卡片（用于测验模式）
     */
    @Query("SELECT * FROM cards ORDER BY RANDOM() LIMIT :limit")
    fun getRandomCards(limit: Int): Flow<List<CardEntity>>
}
