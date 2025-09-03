package com.example.ecomapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecomapp.AppUtils
import com.example.ecomapp.GlobalNavigation
import com.example.ecomapp.R
import com.example.ecomapp.model.ProductModel
import com.example.ecomapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

    val userModel = remember {
        mutableStateOf(UserModel())
    }

    var addressInput by remember {
        mutableStateOf(userModel.value.address)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                        addressInput = userModel.value.address
                    }
                }
            }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(R.drawable.profile_icon),
            contentDescription = "Profile Pic",
            modifier = Modifier
                .fillMaxWidth()
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = userModel.value.name,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Address:", fontWeight = FontWeight.Bold)
        TextField(
            value = addressInput,
            onValueChange = {
                addressInput = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (addressInput.isNotEmpty()) {
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("address", addressInput)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                AppUtils.showToast(context, "Address Updated")
                            }
                        }
                } else {
                    AppUtils.showToast(context, "Address Can't Be Empty")
                }
            })
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Email", fontWeight = FontWeight.Bold)
        Text(text = userModel.value.email)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Number of items in cart", fontWeight = FontWeight.Bold)
        Text(text = userModel.value.cartItems.values.sum().toString())
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("auth")
            },
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sign Out", fontSize = 18.sp)
        }
    }
}