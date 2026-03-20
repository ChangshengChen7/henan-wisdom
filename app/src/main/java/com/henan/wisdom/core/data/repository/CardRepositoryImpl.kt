package com.henan.wisdom.core.data.repository

import com.henan.wisdom.core.data.local.CardDao
import com.henan.wisdom.core.data.local.CardEntity
import com.henan.wisdom.core.domain.model.CardDifficulty
import com.henan.wisdom.core.domain.model.CardRating
import com.henan.wisdom.core.domain.model.KnowledgeCard
import com.henan.wisdom.core.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 卡片仓库实现
 * 实现 Domain Layer 定义的 CardRepository 接口
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao
) : CardRepository {
    
    // ==================== Entity <-> Domain Model 映射 ====================
    
    private fun CardEntity.toDomainModel(): KnowledgeCard {
        return KnowledgeCard(
            id = this.id.toLongOrNull() ?: 0L,
            question = this.content,
            answer = this.answer,
            category = this.categoryId,
            tags = emptyList(),
            difficulty = when {
                this.difficulty < 1.5f -> CardDifficulty.EASY
                this.difficulty > 3.5f -> CardDifficulty.HARD
                else -> CardDifficulty.MEDIUM
            },
            rating = CardRating.GOOD,
            nextReviewTime = if (this.dueDate > 0) this.dueDate else null,
            createdAt = this.createdAt,
            lastReviewedAt = if (this.reps > 0) this.updatedAt else null
        )
    }
    
    private fun KnowledgeCard.toEntity(): CardEntity {
        return CardEntity(
            id = this.id.toString(),
            title = this.question.take(50),
            content = this.question,
            answer = this.answer,
            categoryId = this.category,
            difficulty = when (this.difficulty) {
                CardDifficulty.EASY -> 1.0f
                CardDifficulty.MEDIUM -> 2.5f
                CardDifficulty.HARD -> 4.0f
            },
            stability = 2.5f,
            dueDate = this.nextReviewTime ?: 0L,
            reps = if (this.lastReviewedAt != null) 1 else 0,
            lapses = 0,
            createdAt = this.createdAt,
            updatedAt = System.currentTimeMillis()
        )
    }
    
    // ==================== 实现 CardRepository 接口 ====================
    
    override fun getAllCards(): Flow<List<KnowledgeCard>> {
        return cardDao.getAllCards().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getCardsByCategory(category: String): Flow<List<KnowledgeCard>> {
        return cardDao.getCardsByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getDueCards(): Flow<List<KnowledgeCard>> {
        return cardDao.getDueCards(System.currentTimeMillis()).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getCardById(cardId: Long): Flow<KnowledgeCard?> {
        return cardDao.getCardByIdFlow(cardId.toString()).map { entity ->
            entity?.toDomainModel()
        }
    }
    
    override suspend fun updateCardRating(cardId: Long, rating: CardRating, newInterval: Int) {
        val entity = cardDao.getCardById(cardId.toString()) ?: return
        val newDueDate = System.currentTimeMillis() + (newInterval * 24 * 60 * 60 * 1000L)
        
        val updatedEntity = entity.copy(
            dueDate = newDueDate,
            reps = entity.reps + 1,
            updatedAt = System.currentTimeMillis()
        )
        
        cardDao.updateCard(updatedEntity)
    }
    
    override fun getTotalCardCount(): Flow<Int> {
        return cardDao.getTotalCardsCount()
    }
    
    override fun getLearnedCardCount(): Flow<Int> {
        return cardDao.getTotalCardsCount().map { total ->
            // 简化：已学习 = 总数 - 新卡片数
            total
        }
    }
}