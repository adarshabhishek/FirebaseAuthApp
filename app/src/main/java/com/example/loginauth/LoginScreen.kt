package com.example.loginauth


import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var forgotPassword by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context= LocalContext.current

            Button(onClick = {
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context,
                                task.exception?.message ?: "Login failed",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }, modifier = Modifier.fillMaxWidth()){
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (forgotPassword) {
                var resetEmail by remember { mutableStateOf("") }
                val context = LocalContext.current

                AlertDialog(
                    title = { Text("Forgot Password") },
                    text = {
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (resetEmail.isNotEmpty()) {
                                Firebase.auth.sendPasswordResetEmail(resetEmail)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Password reset email sent",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            forgotPassword = false
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Registered email not found.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter your registered email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }) { Text("Submit") }
                    }, dismissButton = {
                        TextButton(onClick = { forgotPassword = false }) {
                            Text("Cancel")
                        }
                    }, onDismissRequest = { forgotPassword = false }
                )
            }

            TextButton(onClick = {forgotPassword=true})  {
                Text("Forgot Password?")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = true }
                }
            }){
                Text("Don't have an account? Sign up")
            }
        }
    }


}