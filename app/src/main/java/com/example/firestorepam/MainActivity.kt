package com.example.firestorepam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firestorepam.ui.theme.FirestorePamTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirestorePamTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(paddingValues) // Aplicando o padding
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLogin = { userName ->
                                    navController.navigate("home/${userName}")
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterComplete = {
                                    navController.navigate("login")
                                },
                                onLoginClick = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable(
                            "home/{userName}",
                            arguments = listOf(navArgument("userName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName") ?: ""
                            HomeScreen(userName = userName)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RegisterScreen(
    onRegisterComplete: () -> Unit,
    onLoginClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    val db = Firebase.firestore

    // Cores do tema
    val primaryColor = Color(0xFF4CAF50) // Verde claro
    val primaryDarkColor = Color(0xFF388E3C) // Verde mais escuro
    val textColor = Color.White
    val cardBackground = Color.White
    val textColorDark = Color(0xFF333333)

    // Gradiente de fundo
    val gradient = Brush.verticalGradient(
        colors = listOf(primaryColor, primaryDarkColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_principal_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(80.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Cadastro",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome completo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = apelido,
                    onValueChange = { apelido = it },
                    label = { Text("Apelido/Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    visualTransformation = if (mostrarSenha) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                            Icon(
                                painter = painterResource(
                                    id = if (mostrarSenha) R.drawable.logo_principal_foreground else R.drawable.logo_principal_foreground
                                ),
                                contentDescription = "Toggle password visibility",
                                tint = primaryColor
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text("Telefone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val usuario = hashMapOf(
                            "nome" to nome,
                            "apelido" to apelido,
                            "email" to email,
                            "senha" to senha,
                            "telefone" to telefone
                        )

                        db.collection("usuarios")
                            .add(usuario)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Documento adicionado com ID: ${it.id}")
                                onRegisterComplete()
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Erro ao adicionar documento", e)
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cadastrar", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    onLoginClick()
                }) {
                    Text(
                        "Já tem uma conta? Faça login",
                        color = primaryColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(userName: String = "Usuário") {
    var menuExpanded by remember { mutableStateOf(false) }
    var mostrarRegistros by remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val usuarios = remember { mutableStateListOf<Map<String, Any>>() }

    // Cores do tema
    val primaryColor = Color(0xFF4CAF50)
    val primaryDarkColor = Color(0xFF388E3C)
    val textColor = Color.White

    // Gradiente de fundo
    val gradient = Brush.verticalGradient(
        colors = listOf(primaryColor, primaryDarkColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Menu dropdown
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = textColor
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Listar Usuários") },
                        onClick = {
                            menuExpanded = false
                            db.collection("usuarios")
                                .get()
                                .addOnSuccessListener { result ->
                                    usuarios.clear()
                                    for (document in result) {
                                        usuarios.add(document.data)
                                    }
                                    mostrarRegistros = true
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("Firestore", "Error getting documents", exception)
                                }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Perfil") },
                        onClick = { menuExpanded = false }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Sair") },
                        onClick = { menuExpanded = false }
                    )
                }
            }
        }

        // Card principal
        Card(
            modifier = Modifier
                .padding(top = 80.dp, bottom = 40.dp)
                .fillMaxWidth(0.9f)
                .fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_principal_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(80.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Bem-vindo, $userName!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Use o menu no canto superior direito para navegar.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                if (mostrarRegistros) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Usuários Cadastrados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        usuarios.forEachIndexed { index, usuario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        "Usuário ${index + 1}",
                                        color = primaryColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("Nome: ${usuario["nome"]}")
                                    Text("Apelido: ${usuario["apelido"]}")
                                    Text("Email: ${usuario["email"]}")
                                    Text("Telefone: ${usuario["telefone"]}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    val db = Firebase.firestore

    // Cores do tema
    val primaryColor = Color(0xFF4CAF50)
    val primaryDarkColor = Color(0xFF388E3C)
    val textColor = Color.White
    val cardBackground = Color.White
    val textColorDark = Color(0xFF333333)

    // Gradiente de fundo
    val gradient = Brush.verticalGradient(
        colors = listOf(primaryColor, primaryDarkColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_principal_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(80.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    visualTransformation = if (mostrarSenha) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                            Icon(
                                painter = painterResource(
                                    id = if (mostrarSenha) R.drawable.logo_principal_foreground else R.drawable.logo_principal_foreground
                                ),
                                contentDescription = "Toggle password visibility",
                                tint = primaryColor
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        focusedTextColor = textColorDark,
                        unfocusedTextColor = textColorDark,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        db.collection("usuarios")
                            .whereEqualTo("email", email)
                            .whereEqualTo("senha", senha)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    Log.d("Login", "Credenciais inválidas")
                                } else {
                                    val nomeUsuario = documents.documents[0].getString("apelido") ?: email
                                    onLogin(nomeUsuario)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Login", "Erro ao verificar login", exception)
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Entrar", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = {
                    onRegisterClick()
                }) {
                    Text(
                        "Não tem conta? Cadastre-se",
                        color = primaryColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    FirestorePamTheme {
        LoginScreen(onLogin = {}, onRegisterClick = {})
    }
}

@Preview
@Composable
fun RegisterPreview() {
    FirestorePamTheme {
        RegisterScreen(onRegisterComplete = {}, onLoginClick = {})
    }
}

@Preview
@Composable
fun HomePreview() {
    FirestorePamTheme {
        HomeScreen("Usuário Teste")
    }
}