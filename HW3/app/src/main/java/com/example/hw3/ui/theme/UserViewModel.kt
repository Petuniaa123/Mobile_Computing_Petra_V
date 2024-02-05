package com.example.hw3.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application)  {

    val user = MutableLiveData<User?>()

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(application, AppDatabase::class.java, "user_profile_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    init {
        loadUser()
    }

    fun saveUser(username: String, imageUri: String?) {
        viewModelScope.launch(Dispatchers.IO) {

            val newUser = User(username = username, image = imageUri ?: "")
            db.userDaoInterface().insertAndUpdateUser(newUser)
            user.postValue(newUser)

        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            user.postValue(db.userDaoInterface().getUser())
        }
    }
}