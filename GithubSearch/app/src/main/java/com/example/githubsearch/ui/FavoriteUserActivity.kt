package com.example.githubsearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.adapter.ListUserAdapter
import com.example.githubsearch.R
import com.example.githubsearch.User
import com.example.githubsearch.databinding.ActivityFavoriteBinding
import com.example.githubsearch.viewmodel.FavoriteUserViewModel
import com.example.githubsearch.viewmodel.FavoriteUserViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private var _activityFavBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        _activityFavBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rvUsersFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUsersFavorite?.setHasFixedSize(true)

        val favoriteViewModel = obtainViewModel(this)

        favoriteViewModel.getAllUsers().observe(this) { userList ->
            if (userList != null) {
                setUserData(userList)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun setUserData(user: List<User>) {
        val listUserAdapter = ListUserAdapter(user)
        binding?.rvUsersFavorite?.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                sendSelectedUser(data)
            }
        })
    }

    private fun sendSelectedUser(user: User) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
