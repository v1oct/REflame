package com.currupt.reflame.core.collections

import com.currupt.reflame.core.model.ContentTitle

/**
 * Universal repository for managing content collections.
 */
interface CollectionRepository {
    suspend fun getCollection(collectionId: String): List<ContentTitle>
}

class MockCollectionRepository : CollectionRepository {
    override suspend fun getCollection(collectionId: String): List<ContentTitle> {
        return emptyList() // To be implemented with Supabase
    }
}
