package com.henan.wisdom.core.domain.usecase

import com.henan.wisdom.core.domain.model.KnowledgeCard
import com.henan.wisdom.core.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取待复习卡片用例
 * 根据间隔重复算法，返回当前应该复习的卡片
 */
class GetDueCardsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    /**
     * 执行获取待复习卡片
     * @return 待复习的卡片列表，按紧急程度排序
     */
    operator fun invoke(): Flow<List<KnowledgeCard>> {
        return cardRepository.getDueCards()
    }
}
