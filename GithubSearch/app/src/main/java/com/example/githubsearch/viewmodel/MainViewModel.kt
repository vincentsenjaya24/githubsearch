package com.example.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.BuildConfig
import com.example.githubsearch.SearchResponse
import com.example.githubsearch.User
import com.example.githubsearch.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {
    var filtered = mutableListOf<User>()
    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _searchUser = MutableLiveData<List<User>>()
    val searchUser: LiveData<List<User>> = _searchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUser()
    }

    private fun search() {
        filtered.clear()
        val filteredtext = newText.lowercase(Locale.getDefault())
        if (filteredtext.isNotEmpty()) {
            fetchUserSearch(filteredtext)
        }
    }

    private fun fetchUserSearch(searchUser: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDetailUserSearch(searchUser)
        api.enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchUser.value = responseBody.items
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    var newText: String = ""
        set(value) {
            field = value
            search()
        }


    private fun getUser() {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getAllUser()
        api.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _user.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val TOKEN = "token ${BuildConfig.APITOKEN}"
    }
}