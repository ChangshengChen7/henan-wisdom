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
     * 用于间隔重复学习算法的取卡
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
    
    // ==================== 统计查询 ====================
    
    @Query("SELECT COUNT(*) FROM cards")
    fun getTotalCardsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE reps = 0")
    fun getNewCardsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE dueDate <= :now AND dueDate > 0")
    fun getDueCardsCount(now: Long): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM cards WHERE categoryId = :categoryId")
    fun getCardsCountByCategory(categoryId: String): Flow<Int>
}
