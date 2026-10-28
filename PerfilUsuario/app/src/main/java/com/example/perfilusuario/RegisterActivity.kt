package com.example.perfilusuario

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perfilusuario.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest

/**
 * Cadastro de novos usuários (e-mail e senha) com Firebase Authentication.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCriarConta.setOnClickListener { register() }
        binding.btnJaTenhoConta.setOnClickListener { finish() }
    }

    private fun register() {
        val nome = binding.etNome.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()
        val confirmar = binding.etConfirmar.text.toString()

        if (!validate(nome, email, senha, confirmar)) return

        setLoading(true)
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Salva o nome no perfil do usuário
                    val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(nome)
                        .build()
                    val user = task.result?.user
                    if (user != null) {
                        user.updateProfile(profile).addOnCompleteListener { goToMain() }
                    } else {
                        goToMain()
                    }
                } else {
                    setLoading(false)
                    showError(task.exception)
                }
            }
    }

    private fun validate(nome: String, email: String, senha: String, confirmar: String): Boolean {
        binding.tilNome.error = null
        binding.tilEmail.error = null
        binding.tilSenha.error = null
        binding.tilConfirmar.error = null

        var ok = true
        if (nome.isEmpty()) {
            binding.tilNome.error = getString(R.string.erro_nome); ok = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.erro_email); ok = false
        }
        if (senha.length < 6) {
            binding.tilSenha.error = getString(R.string.erro_senha); ok = false
        }
        if (senha != confirmar) {
            binding.tilConfirmar.error = getString(R.string.erro_confirmar_senha); ok = false
        }
        return ok
    }

    private fun showError(e: Exception?) {
        val msg = when (e) {
            is FirebaseAuthUserCollisionException -> R.string.erro_email_em_uso
            is FirebaseAuthWeakPasswordException -> R.string.erro_senha_fraca
            is FirebaseNetworkException -> R.string.msg_sem_internet
            else -> R.string.erro_cadastro
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnCriarConta.isEnabled = !loading
        binding.btnJaTenhoConta.isEnabled = !loading
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
