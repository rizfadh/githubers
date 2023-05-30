package com.rizfadh.githubers.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfadh.githubers.R
import com.rizfadh.githubers.databinding.FragmentFollowBinding
import com.rizfadh.githubers.ui.adapter.UserAdapter
import com.rizfadh.githubers.utils.myAlert
import com.rizfadh.githubers.utils.Result
import com.rizfadh.githubers.utils.ViewModelFactory

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userAdapter: UserAdapter
    private val detailViewModel: DetailViewModel by viewModels { viewModelFactory }
    private var position = 0
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = ViewModelFactory.getInstance(requireActivity())

        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) as String
        }

        userAdapter = UserAdapter {
            val detailIntent = Intent(requireContext(), DetailActivity::class.java)
            detailIntent.apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                putExtra(DetailActivity.EXTRA_AVATAR, it.avatarUrl)
            }
            startActivity(detailIntent)
        }

        detailViewModel.getUserFollow(username, DetailActivity.TABS_CONTENT[position])
        detailViewModel.userFollow.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        userAdapter.submitList(it.data)
                        showFollowersNotFound(false)
                    } else showFollowersNotFound(true)
                    showLoading(false)
                }
                is Result.Error -> {
                    myAlert.showAlert(requireContext(), it.error).show()
                    showLoading(false)
                }
            }
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFollowersNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.rvUsers.visibility = View.GONE
            binding.tvNoFollowers.text = resources
                .getString(R.string.no_followers, username, DetailActivity.TABS_CONTENT[position])
            binding.tvNoFollowers.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.VISIBLE
            binding.tvNoFollowers.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}