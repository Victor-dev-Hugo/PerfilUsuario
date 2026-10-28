# Perfil do Usuário — Login com FirebaseUI (Android / Kotlin)

## Autores
- Victor Hugo Soares Matos Brandão
- Thales de Melo Fontes

App com:
- **Login** via FirebaseUI Auth (e-mail/senha), conforme a documentação oficial
- **Cadastro** de usuários (tela própria com nome, e-mail e senha — a FirebaseUI também permite criar conta)
- **Botão "Sair"** na tela principal, que desloga e volta para o login

## Como rodar
1. Abra a pasta no **Android Studio** (Ladybug ou mais recente, JDK 17).
2. No [Console do Firebase](https://console.firebase.google.com): crie/abra o projeto e
   adicione um app Android com o package **`com.example.perfilusuario`**
   (ou altere `namespace`/`applicationId` em `app/build.gradle.kts` para o seu).
3. Em **Authentication → Método de login**, ative **E-mail/senha**.
4. Baixe o **`google-services.json`** e coloque em **`app/google-services.json`**.
5. Sincronize o Gradle e execute no emulador/dispositivo.

## Estrutura
- `LoginActivity` — FirebaseUI (`AuthUI.createSignInIntentBuilder`) + botão Cadastrar
- `RegisterActivity` — `createUserWithEmailAndPassword` + nome no perfil
- `MainActivity` — mostra nome/e-mail e botão **Sair** (`AuthUI.signOut`)
