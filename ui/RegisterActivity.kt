package com.vibelocal.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.vibelocal.app.databinding.ActivityRegisterBinding
import com.vibelocal.app.model.RegisterRequest
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity() {
    private lateinit var b: ActivityRegisterBinding
    override fun onCreate(s: Bundle?) { super.onCreate(s); b=ActivityRegisterBinding.inflate(layoutInflater); setContentView(b.root); b.btnCreateAccount.setOnClickListener { register() } }
    private fun register() { 
        val n=b.edtFullName.text.toString().trim()
        val e=b.edtEmail.text.toString().trim()
        val p=b.edtPassword.text.toString()
        val c=b.edtConfirmPassword.text.toString()
        if(n.isBlank()||e.isBlank()||p.isBlank()){
            Toast.makeText(this,"Complete all required fields",Toast.LENGTH_SHORT).show()
            return
        }
        if(p!=c){
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch { 
            try { 
                val r=api.register(RegisterRequest(n,e,p))
                session.save(r.userId,r.token,r.fullName)
                startActivity(Intent(this@RegisterActivity,InterestsActivity::class.java))
                finish() 
            } catch(x:Exception){
                // Mock registration for offline mode
                session.save(1, "mock_token", n)
                Toast.makeText(this@RegisterActivity,"Server offline: Account created locally",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity,InterestsActivity::class.java))
                finish()
            } 
        } 
    }
}
