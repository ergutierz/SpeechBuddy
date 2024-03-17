package com.ignotusvia.speechbuddy.core

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

import javax.inject.Singleton

@Singleton
class ChatbotManager(
    private val firestore: FirebaseFirestore
) {
    private val chatsCollection: String = "chats"

    suspend fun sendMessageAndGetResponse(userMessage: String): String {
        val documentReference = firestore.collection(chatsCollection)
            .add(mapOf("userMessage" to userMessage, "timestamp" to System.currentTimeMillis()))
            .await()

        val updatedDocument = firestore.collection(chatsCollection)
            .document(documentReference.id)
            .get()
            .await()

        return updatedDocument.getString("botResponse") ?: "Error: Response not found"
    }

    fun observeChatMessages(): Flow<List<DocumentSnapshot>> = callbackFlow {
        val listenerRegistration = firestore.collection(chatsCollection)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val documents = snapshot?.documents ?: emptyList()
                trySend(documents).isSuccess
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}
