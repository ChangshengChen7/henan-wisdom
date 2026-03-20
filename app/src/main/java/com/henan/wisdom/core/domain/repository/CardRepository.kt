package com.henan.wisdom.core.domain.repository

import com.henan.wisdom.core.domain.model.CardRating
import com.henan.wisdom.core.domain.model.KnowledgeCard
import kotlinx.coroutines.flow.Flow

/**
 * 卡片仓库接口
 * 定义卡片数据的操作规范
 * 遵循Repository模式，用于解耦数据层和领域层
 */
interface CardRepository {
    
    /**
     * 获取所有卡片
     */
    fun getAllCards(): Flow<List<KnowledgeCard>>
    
    /**
     * 获取指定分类的卡片
     */
    fun getCardsByCategory(category: String): Flow<List<KnowledgeCard>>
    
    /**
     * 获取今日待复习的卡片
     * 根据间隔重复算法，返回当前应该复习的卡片
     */
    fun getDueCards(): Flow<List<KnowledgeCard>>
    
    /**
     * 获取指定ID的卡片
     */
    fun getCardById(cardId: Long): Flow<KnowledgeCard?>
    
    /**
     * 更新卡片评分和下次复习时间
     * @param cardId 卡片ID
     * @param rating 评分
     * @param newInterval 新间隔天数
     */
    suspend fun updateCardRating(cardId: Long, rating: CardRating, newInterval: Int)
    
    /**
     * 获取卡片总数
     */
    fun getTotalCardCount(): Flow<Int>
    
    /**
     * 获取已学习卡片数
     */
    fun getLearnedCardCount(): Flow<Int>
}
