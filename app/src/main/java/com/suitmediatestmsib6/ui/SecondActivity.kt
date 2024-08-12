package com.suitmediatestmsib6.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.suitmediatestmsib6.R
import com.suitmediatestmsib6.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ACTIVITY_TITLE
        }

        val username = intent.getStringExtra(EXTRA_NAME)
        binding.tvUsername.text = username

        initializeActions()
    }

    private fun initializeActions() {
        var isUserSelected = binding.tvSelectedUser.isVisible

        binding.btnChoseUser.setOnClickListener {
            if (!isUserSelected) {
                startActivity(Intent(this, ThirdActivity::class.java))
            } else {
                Toast.makeText(this, "Must click name!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvUsername.setOnClickListener {
            updateUserSelection()
            isUserSelected = false
        }
    }

    private fun updateUserSelection() {
        binding.tvSelectedUser.apply {
            visibility = View.VISIBLE
            text = SELECTED
        }
        binding.tvUsername.setTextColor(ContextCompat.getColor(this, R.color.cl_button))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val SELECTED = "Selected User Name"
        private const val ACTIVITY_TITLE = "Second Screen"
    }
}