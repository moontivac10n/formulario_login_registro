package com.example.formulario_login_registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var contraseña: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nombre = findViewById(R.id.nombre)
        contraseña = findViewById(R.id.contraseña)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val user = nombre.text.toString()
        val pass = contraseña.text.toString()

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            try {
                val url = URL("http://192.168.1.114/login.php")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.doOutput = true

                val writer = OutputStreamWriter(httpURLConnection.outputStream)
                writer.write("nombre=$user&contraseña=$pass")
                writer.flush()

                val reader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val result = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }

                runOnUiThread {
                    Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
                }

                writer.close()
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
