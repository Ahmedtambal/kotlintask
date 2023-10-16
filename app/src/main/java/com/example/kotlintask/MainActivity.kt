package com.example.kotlintask
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalTime
import java.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("welcome/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: "User"
                        WelcomeScreen(username)
                }
                    composable("medicineDetail/{medicineId}") { backStackEntry ->
                        val medicineId = backStackEntry.arguments?.getString("medicineId")
                        MedicineDetailScreen(medicineId = medicineId, viewModel = hiltViewModel())
                    }
            }
        }
    }
}
}
@Composable
fun MedicineDetailScreen(medicineId: String?, viewModel: AccountViewModel) {
    val medicine by viewModel.getMedicine(medicineId).observeAsState(null)
    // Display medicine details
}

@Composable
fun LoginScreen(navController: NavController, viewModel: AccountViewModel = hiltViewModel()) {
        var username by remember { mutableStateOf(TextFieldValue()) }
        var password by remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.insertUser(username.text, password.text)
                navController.navigate("welcome/${username.text}")
            }) {
                Text("Login")
            }
        }
    }

@Composable
fun WelcomeScreen(username: String, viewModel: AccountViewModel = hiltViewModel()) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("$greeting, $username")
    }
    val medicines by viewModel.medicines.observeAsState(emptyList())

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMedicines()
    }

    MedicineList(medicines = medicines)
}


@Composable
fun MedicineList(medicines: List<Medicine>) {
    LazyColumn {
        items(medicines) { medicine ->
            MedicineCard(medicine = medicine)
        }
    }
}

@Composable
fun MedicineCard(medicine: Medicine) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("medicineDetail/${medicine.id}") },
        shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = medicine.name)
            Text(text = medicine.dose)
            Text(text = medicine.strength)
        }
    }
}




