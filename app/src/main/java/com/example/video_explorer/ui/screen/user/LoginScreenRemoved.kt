package com.example.video_explorer.ui.screen.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.video_explorer.model.SignInViewModel


@Composable
fun LoginScreen(
    loginViewModel: SignInViewModel,
    modifier: Modifier = Modifier
) {
    val username by loginViewModel.username.collectAsState()
//    val password by loginViewModel.password.collectAsState()
    val password = loginViewModel.password

    val context = LocalContext.current
    
    Column(
        
    ) {
        Text(
            text = "Đăng nhập vào tài khoản của bạn",
            modifier = Modifier.padding(start = 4.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = username,
                onValueChange = { loginViewModel.updateUsername(it) }
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = password,
                onValueChange = {loginViewModel.updatePassword(it)}
            )
        }

        Button(onClick = { loginViewModel.showToast(context = context) }) {
            Text(text = "Đăng nhập")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Đăng ký")
        }
    }
}