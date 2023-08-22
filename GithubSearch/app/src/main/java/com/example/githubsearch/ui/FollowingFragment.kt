package com.example.githubsearch.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearch.adapter.ListUserAdapter
import com.example.githubsearch.User
import com.example.githubsearch.databinding.FragmentFollowingBinding
import com.example.githubsearch.viewmodel.UserDetailViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentFollowingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)

        val userDetailViewModel = ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]

        userDetailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        userDetailViewModel.detailfollowinguser.observe(viewLifecycleOwner) {
            setUserFollowingData(it)
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFollowing.alpha = 0.0F
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFollowing.alpha = 1F
        }
    }

    private fun setUserFollowingData(user: List<User>) {
        binding.labelFollowing.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowing.apply {
            binding.rvFollowing.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.rvFollowing.adapter = listUserAdapter


            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: User) {
                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                    startActivity(intent)
                    activity?.finish()
                }
            })

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}