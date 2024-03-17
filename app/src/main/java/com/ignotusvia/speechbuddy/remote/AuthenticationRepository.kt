package com.ignotusvia.speechbuddy.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ignotusvia.speechbuddy.manager.FirebaseAuthenticationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor(
    private val firebaseAuthenticationManager: FirebaseAuthenticationManager
) {

    fun login(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        firebaseAuthenticationManager.signIn(email, password, onComplete)
    }

    fun register(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        firebaseAuthenticationManager.signUp(email, password, onComplete)
    }

    fun sendPasswordReset(email: String, onComplete: (Exception?) -> Unit) {
        try {
            firebaseAuthenticationManager.sendPasswordReset(email, onComplete)
        } catch (e: Exception) {
            onComplete(e)
        }
    }

    fun signOut() {
        firebaseAuthenticationManager.signOut()
    }

    val getCurrentUser: FirebaseUser? get() = firebaseAuthenticationManager.getCurrentUser
}