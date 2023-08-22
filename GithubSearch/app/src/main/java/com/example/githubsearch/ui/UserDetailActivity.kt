package com.example.githubsearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubsearch.DetailAccount
import com.example.githubsearch.R
import com.example.githubsearch.adapter.SectionsPagerAdapter
import com.example.githubsearch.User
import com.example.githubsearch.databinding.ActivityDetailBinding
import com.example.githubsearch.viewmodel.UserDetailViewModel
import com.example.githubsearch.viewmodel.FavoriteUserViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var isFav: Boolean = false

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val detailViewModel = obtainViewModel(this)
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        detailViewModel.userlogin = user.login

        detailViewModel.detailuser.observe(this) { setDetailUser(it) }
        detailViewModel.getAllUsers().observe(this) {
            isFav = it.contains(user)
            if (isFav) {
                binding.iconFav.setImageResource(R.drawable.fav_filled)
            } else {
                binding.iconFav.setImageResource(R.drawable.fav_empty)
            }
        }

        detailViewModel.isLoading.observe(this) { showLoading(it) }

        binding.iconFav.setOnClickListener {
            if (isFav) {
                detailViewModel.delete(user)
                binding.iconFav.setImageResource(R.drawable.fav_empty)
                Snackbar.make(
                    binding.root,
                    "${user.login} is deleted from favorites",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        detailViewModel.insert(user)
                        Toast.makeText(this, "Undo", Toast.LENGTH_SHORT).show()
                    }.show()
            } else {
                detailViewModel.insert(user)
                binding.iconFav.setImageResource(R.drawable.fav_filled)
                Snackbar.make(
                    binding.root,
                    "${user.login} is added to favorites",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("See favorite") {
                        startActivity(Intent(this, FavoriteUserActivity::class.java))
                    }.show()
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    private fun setDetailUser(detailuser: DetailAccount) {
        binding.tvDetailNama.text = detailuser.name ?: " - "
        binding.tvFollower.text = detailuser.followers
        binding.tvFollowing.text = detailuser.following
        val actionBar = supportActionBar
        actionBar!!.title = detailuser.login.toUsernameFormat()
        Glide.with(this)
            .load(detailuser.avatarUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imgAvatar)
    }

    private fun String.toUsernameFormat(): String {
        return "@$this"
    }
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    companion object {
        const val EXTRA_USER = "extra_user"


        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}

