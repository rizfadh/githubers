package com.rizfadh.githubers.ui.main

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfadh.githubers.R
import com.rizfadh.githubers.databinding.ActivityMainBinding
import com.rizfadh.githubers.ui.adapter.UserAdapter
import com.rizfadh.githubers.ui.detail.DetailActivity
import com.rizfadh.githubers.ui.favorite.FavoriteActivity
import com.rizfadh.githubers.utils.Result
import com.rizfadh.githubers.utils.ViewModelFactory
import com.rizfadh.githubers.utils.myAlert

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    private lateinit var userAdapter: UserAdapter
    private var isDarkModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.home)

        viewModelFactory = ViewModelFactory.getInstance(application)

        userAdapter = UserAdapter {
            val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
            detailIntent.apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                putExtra(DetailActivity.EXTRA_AVATAR, it.avatarUrl)
            }
            startActivity(detailIntent)
        }

        mainViewModel.userList.observe(this@MainActivity) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        userAdapter.submitList(it.data)
                        showUserNotFound(false)
                    } else showUserNotFound(true)
                    showLoading(false)
                }
                is Result.Error -> {
                    myAlert.showAlert(this@MainActivity, it.error).show()
                    showLoading(false)
                }
            }
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        mainViewModel.getDarkModeSettings().observe(this@MainActivity) {

            isDarkModeActive = it

            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menu?.getItem(1)?.setIcon(R.drawable.ic_moon_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menu?.getItem(1)?.setIcon(R.drawable.ic_sun_24)
            }
        }

        val searchManager = getSystemService<SearchManager>()
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager?.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.insert_username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    val username = p0?.trim() as String

                    mainViewModel.searchUser(username)

                    searchView.clearFocus()

                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favorite -> {
                val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.dark_mode -> {
                mainViewModel.saveDarkModeSettings(!isDarkModeActive)
            }
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