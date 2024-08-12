package com.suitmediatestmsib6.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.suitmediatestmsib6.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeActions()
    }

    private fun initializeActions() {
        val palindromeText = binding.edtPalindrome.text
        val nameText = binding.edtName.text

        binding.btnCheck.setOnClickListener {
            validatePalindrome(palindromeText.toString())
        }

        binding.btnNext.setOnClickListener {
            navigateToSecondActivity(nameText.toString())
        }
    }

    private fun validatePalindrome(palindrome: String) {
        if (palindrome.isEmpty()) {
            binding.edtPalindrome.error = "Must be filled!"
            return
        }

        val isPalindrome = palindrome == palindrome.reversed()
        val message = if (isPalindrome) "Palindrome" else "Not Palindrome"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToSecondActivity(name: String) {
        if (name.isEmpty()) {
            binding.edtName.error = "Must be filled!"
            return
        }

        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(SecondActivity.EXTRA_NAME, name)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "FirstActivity"
    }
}