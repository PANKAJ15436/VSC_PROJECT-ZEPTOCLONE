package com.example.zeptoclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GroceryItem(
    val icon: String,
    val name: String,
    val price: Int,
    val time: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeptoCloneFullApp()
        }
    }
}

@Composable
fun ZeptoCloneFullApp() {
    var isRegistered by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }

    when {
        !isRegistered -> RegisterScreen {
            isRegistered = true
        }

        !isLoggedIn -> LoginScreen {
            isLoggedIn = true
        }

        else -> ZeptoCloneApp()
    }
}

@Composable
fun RegisterScreen(onRegister: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Zepto Register", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A1B9A)
            )
        ) {
            Text("Register")
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A1B9A)
            )
        ) {
            Text("Login")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZeptoCloneApp() {

    val items = listOf(
        GroceryItem("🥛", "Milk", 30, "10 mins"),
        GroceryItem("🍞", "Bread", 40, "8 mins"),
        GroceryItem("🍎", "Apple", 80, "7 mins"),
        GroceryItem("🍪", "Biscuits", 25, "5 mins"),
        GroceryItem("🥤", "Cold Drink", 45, "6 mins"),
        GroceryItem("🍚", "Rice", 120, "15 mins")
    )

    var cartItems by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }
    var showPayment by remember { mutableStateOf(false) }
    var orderDone by remember { mutableStateOf(false) }

    if (orderDone) {
        OrderDoneScreen()
        return
    }

    if (showPayment) {
        PaymentScreen(total) {
            orderDone = true
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Zepto", color = Color.White)
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("P", color = Color(0xFF6A1B9A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text("Delivering to Home 📍", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.LightGray
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔥 50% OFF on first order")
                    Text("Use code: ZEPTO50")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Products", fontSize = 22.sp)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    GroceryCard(item) {
                        cartItems++
                        total += item.price
                    }
                }
            }

            Text("Cart Items: $cartItems")
            Text("Total: ₹$total")
            Text("Delivery in 10 mins 🚚")

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    showPayment = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            ) {
                Text("Go to Payment")
            }
        }
    }
}

@Composable
fun GroceryCard(
    item: GroceryItem,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.icon, fontSize = 30.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 20.sp)
                Text("₹${item.price}")
                Text("Delivery: ${item.time}")
            }

            Button(onClick = onAdd) {
                Text("Add")
            }
        }
    }
}

@Composable
fun PaymentScreen(
    total: Int,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Payment Page", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total: ₹$total", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay with UPI")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cash on Delivery")
        }
    }
}

@Composable
fun OrderDoneScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✅ Order Placed", fontSize = 24.sp)
        Text("Arriving in 10 mins 🚚")
    }
}