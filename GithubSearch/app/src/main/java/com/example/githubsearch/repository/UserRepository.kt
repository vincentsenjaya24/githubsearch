package com.example.githubsearch.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubsearch.User
import com.example.githubsearch.database.UserDao
import com.example.githubsearch.database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUsersDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUsersDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<User>> = mUsersDao.getAllUsers()
    fun insert(note: User) {
        executorService.execute { mUsersDao.insert(note) }
    }

    fun delete(note: User) {
        executorService.execute { mUsersDao.delete(note) }
    }
}