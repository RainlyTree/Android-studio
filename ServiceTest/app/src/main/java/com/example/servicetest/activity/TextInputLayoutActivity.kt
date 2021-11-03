package com.example.servicetest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.servicetest.R
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class TextInputLayoutActivity : AppCompatActivity() {

    var tl_username: TextInputLayout? = null
    var el_password: TextInputLayout? = null

    companion object {
        val EMALL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
    }

    val pattern = Pattern.compile(EMALL_PATTERN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input_layout)
        tl_username = findViewById(R.id.el_username)
        el_password = findViewById(R.id.el_password)
    }

    fun login() {
        val username = tl_username?.editText?.text.toString()
        val password = el_password?.editText?.text.toString()
        if(!validateUserName(username)) {
        }
    }

    //验证账号
    fun validateUserName(username: String): Boolean {
        val matcher = pattern.matcher(username)
        return matcher.matches()
    }

    //验证密码
    fun validatePassword(password: String): Boolean {
        return password.length > 6
    }
}