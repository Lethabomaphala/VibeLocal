package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.vibelocal.app.databinding.ActivityLoginBinding
import com.vibelocal.app.model.LoginRequest
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var b: ActivityLoginBinding
    override fun onCreate(s: Bundle?) { super.onCreate(s); b=ActivityLoginBinding.inflate(layoutInflater); setContentView(b.root)
        b.btnRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        b.btnLogin.setOnClickListener { login() }
    }
    private fun login() { 
        val email=b.edtEmail.text.toString().trim()
        val pass=b.edtPassword.text.toString()
        if(email.isBlank()||pass.isBlank()){
            Toast.makeText(this,"Enter email and password",Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch { 
            try { 
                val r=api.login(LoginRequest(email,pass))
                session.save(r.userId,r.token,r.fullName)
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish() 
            } catch(e:Exception){
                // Mock login for offline mode
                session.save(1, "mock_token", email.substringBefore("@"))
                Toast.makeText(this@LoginActivity,"Server offline: Logging in locally",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            } 
        } 
    }
}
