package com.ignotusvia.speechbuddy.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthenticationManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun signUp(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(firebaseAuth.currentUser, null)
                } else {
                    onComplete(null, task.exception)
                }
            }
    }

    fun signIn(email: String, password: String, onComplete: (FirebaseUser?, Exception?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(firebaseAuth.currentUser, null)
                } else {
                    onComplete(null, task.exception)
                }
            }
    }

    val getCurrentUser: FirebaseUser? get() = firebaseAuth.currentUser
}