package com.rizfadh.githubers.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfadh.githubers.R
import com.rizfadh.githubers.data.api.response.UserItem
import com.rizfadh.githubers.databinding.ActivityFavoriteBinding
import com.rizfadh.githubers.ui.adapter.UserAdapter
import com.rizfadh.githubers.ui.detail.DetailActivity
import com.rizfadh.githubers.utils.Result
import com.rizfadh.githubers.utils.ViewModelFactory
import com.rizfadh.githubers.utils.myAlert

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(application)
        val favoriteViewModel: FavoriteViewModel by viewModels { viewModelFactory }

        val userAdapter = UserAdapter {
            val detailIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            detailIntent.apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                putExtra(DetailActivity.EXTRA_AVATAR, it.avatarUrl)
            }
            startActivity(detailIntent)
        }

        favoriteViewModel.getUserFavorite()
        favoriteViewModel.getUserFavorite().observe(this@FavoriteActivity) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        val userFavorite = it.data.map { favorite ->
                            UserItem(favorite.avatarUrl, favorite.login)
                        }
                        userAdapter.submitList(userFavorite)
                        showUserNotFound(false)
                    } else showUserNotFound(true)
                    showLoading(false)
                }
                is Result.Error -> {
                    myAlert.showAlert(this@FavoriteActivity, it.error).show()
                    showLoading(false)
                }
            }
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.rvUsers.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}