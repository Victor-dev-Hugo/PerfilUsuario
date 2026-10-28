package com.example.perfilusuario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perfilusuario.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

/**
 * Tela de login usando a FirebaseUI (modelo da documentação oficial).
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Callback do resultado do fluxo da FirebaseUI
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se já estiver logado, vai direto para a tela principal
        if (FirebaseAuth.getInstance().currentUser != null) {
            goToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener { startFirebaseUiSignIn() }
        binding.btnCadastrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun startFirebaseUiSignIn() {
        // Provedores de autenticação. Para habilitar outros (Google, Telefone...),
        // ative-os no Console do Firebase e adicione aqui.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true) // a própria FirebaseUI também permite criar conta
                .setRequireName(true)
                .build(),
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_logo)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Login realizado com sucesso
            goToMain()
        } else if (response == null) {
            // Usuário cancelou (botão voltar)
            Toast.makeText(this, R.string.msg_login_cancelado, Toast.LENGTH_SHORT).show()
        } else {
            val msg = if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                R.string.msg_sem_internet
            } else {
                R.string.msg_erro_login
            }
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
