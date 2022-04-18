package com.richarwhiteg2.wildrunning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates
import android.view.View

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
}