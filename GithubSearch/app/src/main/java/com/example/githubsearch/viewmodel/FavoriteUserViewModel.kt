package com.example.githubsearch.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.User
import com.example.githubsearch.repository.UserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getAllUsers(): LiveData<List<User>> = mUserRepository.getAllUsers()

}