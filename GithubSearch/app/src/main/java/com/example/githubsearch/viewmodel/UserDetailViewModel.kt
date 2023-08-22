package com.example.githubsearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.BuildConfig
import com.example.githubsearch.DetailAccount
import com.example.githubsearch.User
import com.example.githubsearch.repository.UserRepository
import com.example.githubsearch.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {
    private val _detailuser = MutableLiveData<DetailAccount>()
    val detailuser: LiveData<DetailAccount> = _detailuser

    private val _detailfolloweruser = MutableLiveData<List<User>>()
    val detailfolloweruser: LiveData<List<User>> = _detailfolloweruser

    private val _detailfollowinguser = MutableLiveData<List<User>>()
    val detailfollowinguser: LiveData<List<User>> = _detailfollowinguser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val mUserRepository: UserRepository = UserRepository(application)
    fun getAllUsers(): LiveData<List<User>> = mUserRepository.getAllUsers()
    fun insert(user: User) {
        mUserRepository.insert(user)
    }

    fun delete(user: User) {
        mUserRepository.delete(user)
    }

    var userlogin: String = ""
        set(value) {
            field = value
            fetchDetailtUser()
            fetchDetailtUserFollower()
            fetchDetailtUserFollowing()
        }

    private fun fetchDetailtUser() {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDetailUser(userlogin)
        api.enqueue(object : retrofit2.Callback<DetailAccount> {
            override fun onResponse(call: Call<DetailAccount>, response: Response<DetailAccount>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailuser.value = responseBody!!

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailAccount>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun fetchDetailtUserFollower() {
        _isLoadingFollower.value = true
        val api = ApiConfig.getApiService().getAllUserFollower(userlogin)
    //    val api = ApiConfig.getApiService().getAllUserFollower(userlogin, token = TOKEN)
        api.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailfolloweruser.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun fetchDetailtUserFollowing() {
        _isLoadingFollowing.value = true
        val api = ApiConfig.getApiService().getAllUserFollowing(userlogin)
        api.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailfollowinguser.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }


    companion object {
        private const val TAG = "DetailViewModel"
        private const val TOKEN = "token ${BuildConfig.APITOKEN}"
    }

}