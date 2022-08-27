package com.example.github

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.databinding.ActivityHomeBinding
import com.google.gson.Gson
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        val accountAdapter = AccountAdapter()
        with(homeBinding.recyclerviewHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }
        val jsonString = loadJSONFromAsset(this, "githubuser.json")
        val data = Gson().fromJson(jsonString, UserResponse::class.java)
        accountAdapter.setAccount(data.users as List<UsersItem>)
        homeBinding.recyclerviewHome.adapter = accountAdapter
    }
    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}