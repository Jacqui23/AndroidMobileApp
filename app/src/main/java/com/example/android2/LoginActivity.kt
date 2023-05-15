package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.android2.R
import com.example.android2.Register
import com.example.android2.RetrofitClient
import com.example.android2.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private lateinit var btnLog: Button
private lateinit var tvreglink: TextView
private lateinit var eUsername: EditText
private lateinit var ePassword: EditText


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        eUsername = findViewById<EditText>(R.id.etUserName)
        ePassword = findViewById<EditText>(R.id.etPassword)
        tvreglink = findViewById(R.id.tvRegisterLink)
        btnLog = findViewById(R.id.btnLogin)

        tvreglink.setOnClickListener {
            intent = Intent(this,Register ::class.java)
            startActivity(intent)
        }

        btnLog.setOnClickListener {
            loginUser()
            }
        }


        private fun loginUser() {
            val userName: String = eUsername.getText().toString().trim()
            val password: String = ePassword.getText().toString().trim()

            if (userName.isEmpty()) {
                eUsername.setError("Username is required")
                eUsername.requestFocus()
                return
            } else if (password.isEmpty()) {
                ePassword.setError("Password is required")
                ePassword.requestFocus()
                return
            }
            val call: retrofit2.Call<ResponseBody> = RetrofitClient
                .getInstance()
                .api
                .checkUser(User(userName, password))
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: Response<ResponseBody?>
                ) {
                    var s = ""
                    try {
                        s = response.body()!!.string()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (s == userName) {
                        val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                        intent.putExtra("Username",s)
                        Toast.makeText(
                            this@LoginActivity,
                            "Successfully Logged in",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "User does not exist!", Toast.LENGTH_LONG)
                            .show()
                    }

                }
                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }