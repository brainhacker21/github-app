package com.auric.submission3githubusersapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.auric.submission2_aplikasigithubuser.R
import com.auric.submission2_aplikasigithubuser.databinding.FragmentFollowsBinding
import com.auric.submission3githubusersapp.adapter.ListUserAdapter
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission3githubusersapp.viewmodel.FollowsViewModel

class FollowsFragment: Fragment(R.layout.fragment_follows) {

    private var _binding : FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var username:String
    private val followsviewmodel by viewModels<FollowsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        //     val args = arguments
        username = activity?.intent?.getStringExtra(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowsBinding.bind(view)


        listUserAdapter = ListUserAdapter()
        listUserAdapter.notifyDataSetChanged()

        binding.apply {
            rvUsersFollows.setHasFixedSize(true)
            rvUsersFollows.layoutManager = LinearLayoutManager(activity)
            rvUsersFollows.adapter = listUserAdapter
        }
        showLoading(true)

      when (index) {
          0 -> {
              followsviewmodel.setListFollowers(username)
              followsviewmodel.listFollowers.observe(viewLifecycleOwner) {
                  listUserAdapter.setData(it)
                  showLoading(false)
              }
          }

          1 -> {
              followsviewmodel.setListFollowing(username)
              followsviewmodel.listFollowing.observe(viewLifecycleOwner) {
                  listUserAdapter.setData(it)
                  showLoading(false)
              }
          }
      }

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
           override fun onItemClicked(item: ItemsItem) {
           val intent = Intent(requireActivity(), DetailUserActivity::class.java)
               intent.putExtra(DetailUserActivity.EXTRA_USERNAME, item.login)
               intent.putExtra(DetailUserActivity.EXTRA_ID, item.id)
               intent.putExtra(DetailUserActivity.EXTRA_URL, item.avatarUrl)
                    startActivity(intent)
           }
        })
    }

    override fun onDestroy() {
    super.onDestroy()
    _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(index: Int) =
            FollowsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }
}