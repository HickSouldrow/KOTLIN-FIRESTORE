package com.example.firestorepam

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.firestorepam.ui.theme.FirestorePamTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirestorePamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var nome by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var mostrarRegistros by remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val banco = remember { mutableStateListOf<Map<String, Any>>() }
    val scrollState = rememberScrollState()
    val backgroundColor = Color(0xFF121212)
    val fieldBackground = Color(0xFF1E1E1E)
    val textColor = Color.White
    val labelColor = Color.Gray
    val primaryColor = Color(0xFF5EA500) // Nova cor verde

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(backgroundColor)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Registro Hick1lugar",
            fontFamily = FontFamily.Cursive,
            fontSize = 26.sp,
            color = primaryColor, // Alterado para a nova cor
            modifier = Modifier.padding(vertical = 24.dp)
        )

        CustomDarkTextField(value = nome, onValueChange = { nome = it }, label = "Nome", fieldBackground, textColor, labelColor)
        CustomDarkTextField(value = apelido, onValueChange = { apelido = it }, label = "Nickname", fieldBackground, textColor, labelColor)
        CustomDarkTextField(value = email, onValueChange = { email = it }, label = "E-mail", fieldBackground, textColor, labelColor)
        CustomDarkTextField(value = senha, onValueChange = { senha = it }, label = "Senha", backgroundColor = fieldBackground, textColor = textColor, labelColor = labelColor, isPassword = true)
        CustomDarkTextField(value = telefone, onValueChange = { telefone = it }, label = "Telefone", fieldBackground, textColor, labelColor)

        Button(
            onClick = {
                val banco = hashMapOf(
                    "nome" to nome,
                    "apelido" to apelido,
                    "email" to email,
                    "senha" to senha,
                    "telefone" to telefone,
                )

                db.collection("banco")
                    .add(banco)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                nome = ""
                apelido = ""
                email = ""
                senha = ""
                telefone = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor), // Alterado para a nova cor
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Cadastrar", fontSize = 18.sp, color = Color.White)
        }

        Button(
            onClick = {
                if (!mostrarRegistros) {
                    db.collection("banco")
                        .get()
                        .addOnSuccessListener { result ->
                            banco.clear()
                            for (document in result) {
                                banco.add(document.data)
                            }
                            mostrarRegistros = true
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents.", exception)
                        }
                } else {
                    mostrarRegistros = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor), // Alterado para a nova cor
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                if (mostrarRegistros) "Ocultar registros" else "Exibir todos os registros",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        if (mostrarRegistros) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                banco.forEachIndexed { index, banco ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color(0xFF2C2C2C), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text("Registro ${index + 1}", color = primaryColor, fontSize = 18.sp) // Alterado para a nova cor
                        Text("Nome: ${banco["nome"]}", color = Color.White)
                        Text("Apelido: ${banco["apelido"]}", color = Color.White)
                        Text("Email: ${banco["email"]}", color = Color.White)
                        Text("Senha: ${banco["senha"]}", color = Color.White)
                        Text("Telefone: ${banco["telefone"]}", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDarkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    backgroundColor: Color,
    textColor: Color,
    labelColor: Color,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = labelColor) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = Color(0xFF5EA500), // Alterado para a nova cor
            focusedLabelColor = labelColor,
            unfocusedLabelColor = labelColor,
            focusedIndicatorColor = Color(0xFF5EA500), // Adicionado - cor do indicador quando focado
            unfocusedIndicatorColor = Color.Gray // Adicionado - cor do indicador quando não focado
        )
    )
}

@Preview
@Composable
fun AppPreview() {
    FirestorePamTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            App()
        }
    }
}