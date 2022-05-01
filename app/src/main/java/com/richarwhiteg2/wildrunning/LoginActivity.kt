package com.richarwhiteg2.wildrunning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.richarwhiteg2.wildrunning.ValidateEmail.ValidateEmail.Companion.isEmail
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    companion object{   //PAra que se tenga la referencia en todas las clases   companion object
        lateinit var useremail: String
        lateinit var providerSession: String
    }

    private var email by Delegates.notNull<String>() // Delegates No permite que sea nulo
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var lyTerms: LinearLayout  //terminos y condiciones

    private lateinit var mAuth: FirebaseAuth //autenticación de base de datos FIREBASE

    private var RESULT_CODE_GOOGLE_SIGN_IN = 100 //INICIAR SESION CON GOOGLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Para que se oculten los terminos y condiciones
        lyTerms = findViewById(R.id.lyTerms)
        lyTerms.visibility = View.INVISIBLE

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        mAuth = FirebaseAuth.getInstance()

        //para validar emeil y contraseña es valido en el formato
        manageButtonLogin()
        etEmail.doOnTextChanged { text, start, before, count ->  manageButtonLogin() }
        etPassword.doOnTextChanged { text, start, before, count ->  manageButtonLogin() }
    }

    //Para saber si hay un usuario en la app
    public override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser //obtiene datos del usuario actual
        if (currentUser != null)  goHome(currentUser.email.toString(), currentUser.providerId)

    }

    //para que al dar atras desde adentro de la app no regrese al login
    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    //para validar emeil y contraseña es valido en el formato si no es valido el boton iniciar sesión no se activa
    private fun manageButtonLogin(){
        var tvLogin = findViewById<TextView>(R.id.tvLogin)
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        if (TextUtils.isEmpty(password) || !isEmail(email)){  //si la contraseña esta vacia se deshabilita el login o si el imail no es correcto

            tvLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            tvLogin.isEnabled = false
        }
        else{
            tvLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            tvLogin.isEnabled = true
        }
    }

    fun login(view: View) {
        loginUser()
    }
    private fun loginUser(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email, password) //para entrar con email y password
            .addOnCompleteListener(this){ task ->  //task nos indica si el incio fue satisfactorio
                if (task.isSuccessful)  goHome(email, "email")  //loggin exitoso manda a pantalla principal
                else{
                    if (lyTerms.visibility == View.INVISIBLE) lyTerms.visibility = View.VISIBLE
                    else{
                        var cbAcept = findViewById<CheckBox>(R.id.cbAcept)
                        if (cbAcept.isChecked) register()
                    }
                }
            }

    }
    //Si el loggin fue exitoso esta funcion envia a la panatalla principal de la app
    private fun goHome(email: String, provider: String){

        useremail = email
        providerSession = provider
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun register(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        //crea el usuario en Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){

                    var dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date()) //Para guardar la fecha
                    var dbRegister = FirebaseFirestore.getInstance() //Para acceder a la base da dates y acceder a la colecciones
                    dbRegister.collection("users").document(email).set(hashMapOf(
                        "user" to email,
                        "dateRegister" to dateRegister
                    ))

                    goHome(email, "email")
                }
                else Toast.makeText(this, "Error, algo ha salido mal", Toast.LENGTH_SHORT).show()
            }
    }
    //para ir a los terminos y condiciones
    fun goTerms(v: View){
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)
    }
    //cuando olvidad la contraseña
    fun forgotPassword(view: View) {
        //startActivity(Intent(this, ForgotPasswordActivity::class.java))
        resetPassword()
    }
    //para restablecer contraseña
    private fun resetPassword(){
        var e = etEmail.text.toString()

        if (!TextUtils.isEmpty(e)){
            mAuth.sendPasswordResetEmail(e)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) Toast.makeText(this, "Email Enviado a $e", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(this, "No se encontró el usuario con este correo", Toast.LENGTH_SHORT).show()
                }
        }
        else Toast.makeText(this, "Indica un email", Toast.LENGTH_SHORT).show()
    }

    //Para iniciar sesión con google
    fun callSignInGoogle (view:View){
        signInGoogle()
    }
    private fun signInGoogle(){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()    //cierra cualquier sesión anterior abierta

        startActivityForResult(googleSignInClient.signInIntent, RESULT_CODE_GOOGLE_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_CODE_GOOGLE_SIGN_IN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                if (account != null){
                    email = account.email!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    mAuth.signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful) goHome(email, "Google")
                        else Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT)

                    }
                }


            } catch (e: ApiException) {
                Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT)
            }
        }

    }
}