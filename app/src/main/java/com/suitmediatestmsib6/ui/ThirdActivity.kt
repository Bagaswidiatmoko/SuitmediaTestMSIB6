package com.suitmediatestmsib6.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.suitmediatestmsib6.adapter.UserAdapter
import com.suitmediatestmsib6.databinding.ActivityThirdBinding
import com.suitmediatestmsib6.response.ApiResponse
import com.suitmediatestmsib6.response.DataItem
import com.suitmediatestmsib6.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityThirdBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ACTIVITY_TITLE
        }

        setupRecyclerView()
        setupSwipeRefresh()
        loadUsers(false)
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter().apply {
            setClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: DataItem) {
                    navigateToSecondActivity(user)
                }
            })
        }

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@ThirdActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.refresh.setOnRefreshListener(this)
    }

    private fun loadUsers(isRefresh: Boolean) {
        isLoading = true

        if (!isRefresh) {
            binding.progressbar.visibility = View.VISIBLE
        }

        Handler().postDelayed({
            val params = HashMap<String, String>().apply {
                put("page", currentPage.toString())
            }

            ApiConfig.getApiService().getUser(params).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            totalPages = it.totalPages
                            it.data?.let { users ->
                                Log.d(ACTIVITY_TITLE, "onResponse: $users")
                                if (users.isNotEmpty()) {
                                    userAdapter.setList(ArrayList(users))
                                }
                            }
                        }
                        binding.progressbar.visibility = View.GONE
                        isLoading = false
                        binding.refresh.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("onFailure", t.message.toString())
                    Toast.makeText(this@ThirdActivity, "Connection Failed...", Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.GONE
                    isLoading = false
                    binding.refresh.isRefreshing = false
                }
            })
        }, 3000)
    }

    private fun navigateToSecondActivity(user: DataItem) {
        Intent(this, SecondActivity::class.java).apply {
            putExtra(SecondActivity.EXTRA_NAME, user.firstName)
            startActivity(this)
        }
        resetPage()
        finish()
    }

    private fun resetPage() {
        currentPage = 1
    }

    override fun onRefresh() {
        userAdapter.clearUsers()
        currentPage = 2
        loadUsers(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val ACTIVITY_TITLE = "Third Screen"
        private var isLoading = false
        private var currentPage: Int = 1
        private var totalPages: Int = 1
    }
}