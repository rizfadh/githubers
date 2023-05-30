package com.rizfadh.githubers.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rizfadh.githubers.R
import com.rizfadh.githubers.data.api.response.UserDetail
import com.rizfadh.githubers.data.local.entity.FavoriteEntity
import com.rizfadh.githubers.databinding.ActivityDetailBinding
import com.rizfadh.githubers.ui.adapter.FollowPagerAdapter
import com.rizfadh.githubers.utils.Result
import com.rizfadh.githubers.utils.ViewModelFactory
import com.rizfadh.githubers.utils.myAlert

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val detailViewModel: DetailViewModel by viewModels { viewModelFactory }
    private lateinit var userDetail: UserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)

        val username = intent.getStringExtra(EXTRA_USERNAME) as String

        supportActionBar?.title = resources.getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel.isFavorite(username)
        detailViewModel.isUserFavorite.observe(this@DetailActivity) { user ->
            if (user != null) {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite_24
                    )
                )
                binding.fabFavorite.setOnClickListener {
                    detailViewModel.deleteUserFavorite(user)
                    detailViewModel.isFavorite(username)
                }
            } else {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite_border_24
                    )
                )
                binding.fabFavorite.setOnClickListener {
                    val avatarUrl = intent.getStringExtra(EXTRA_AVATAR) as String
                    val userFavorite = FavoriteEntity(username, avatarUrl)
                    detailViewModel.insertUserFavorite(userFavorite)
                    detailViewModel.isFavorite(username)
                }
            }
        }

        detailViewModel.getUser(username)
        detailViewModel.userDetail.observe(this@DetailActivity) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    userDetail = it.data
                    setUserDetail(it.data)
                    showLoading(false)
                    binding.fabFavorite.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    myAlert.showAlert(this@DetailActivity, it.error).show()
                    showLoading(false)
                }
            }
        }

        val viewPager: ViewPager2 = binding.viewPager
        val sectionPagerAdapter = FollowPagerAdapter(this@DetailActivity)
        sectionPagerAdapter.username = username
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = TABS_CONTENT[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share -> {
                if (this::userDetail.isInitialized) {
                    val text = "Profile Github @${userDetail.login}:" +
                            "\n\u2022 Mengikuti: ${userDetail.following}" +
                            "\n\u2022 Pengikut: ${userDetail.following}" +
                            "\n\u2022 Repository: ${userDetail.publicRepos}" +
                            "\nInfo selengkapnya kunjungi https://github.com/${userDetail.login}" +
                            "\n\n\u00A9 Githubers by Rizfadh"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserDetail(detail: UserDetail) {
        Glide.with(this@DetailActivity)
            .load(detail.avatarUrl)
            .into(binding.civImage)

        binding.apply {
            tvRepos.text = resources.getString(R.string.repos, detail.publicRepos)
            tvFollowers.text = resources.getString(R.string.followers, detail.followers)
            tvFollowing.text = resources.getString(R.string.following, detail.following)
            tvUsername.text = resources.getString(R.string.username, detail.login)
            tvName.text = detail.name
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
        val TABS_CONTENT = arrayOf("followers", "following")
    }
}