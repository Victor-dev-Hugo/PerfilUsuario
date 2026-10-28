package com.example.perfilusuario

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.perfilusuario.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

/**
 * Tela exibida após o login, com o botão "Sair" que volta para a tela de login.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            goToLogin()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNome.text = user.displayName?.takeIf { it.isNotBlank() }
            ?: getString(R.string.main_sem_nome)
        binding.tvEmail.text = user.email ?: user.phoneNumber.orEmpty()

        binding.btnSair.setOnClickListener { signOut() }
    }

    private fun signOut() {
        // Sair pelo método de conveniência da FirebaseUI
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { goToLogin() }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
