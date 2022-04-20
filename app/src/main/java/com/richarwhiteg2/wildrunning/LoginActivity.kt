package com.richarwhiteg2.wildrunning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Para que se oculten los terminos y condiciones
        lyTerms = findViewById(R.id.lyTerms)
        lyTerms.visibility = View.INVISIBLE

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        mAuth = FirebaseAuth.getInstance()
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
    cuando olvidad la contraseña
    fun forgotPassword(view: View) {
        //startActivity(Intent(this, ForgotPasswordActivity::class.java))
        resetPassword()
    }
}