package com.example.ecomapp

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.ecomapp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID

object AppUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun addToCart(context: Context, productId: String) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart).addOnCompleteListener { updateResponse ->
                    if (updateResponse.isSuccessful) {
                        showToast(context, "Item added to Cart")
                    } else {
                        showToast(context, "Failed to add to Cart")
                    }
                }
            }
        }
    }

    fun removeFromCart(context: Context, productId: String, removeAll: Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if (updatedQuantity <= 0 || removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart).addOnCompleteListener { updateResponse ->
                    if (updateResponse.isSuccessful) {
                        showToast(context, "Item removed from Cart")
                    } else {
                        showToast(context, "Failed to remove Item from Cart")
                    }
                }
            }
        }
    }

    fun clearCartAndAddToOrders() {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val order = OrderModel(
                    id = UUID.randomUUID().toString(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                    date = Timestamp.now(),
                    items = currentCart,
                    status = "Ordered",
                    address = it.result.get("address") as String
                )

                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            userDoc.update("cartItems", FieldValue.delete())
                        }
                    }
            }
        }
    }

    fun getDiscountPercentage() : Float {
        return 10.0f
    }

    fun getTaxPercentage() : Float {
        return 15.0f
    }

    fun showDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Payment Successful")
            .setMessage("Thank you! your payment was completed successfully")
            .setPositiveButton("OK") {_, _ ->
               GlobalNavigation.navController.popBackStack()
                GlobalNavigation.navController.navigate("home")
            }
            .setCancelable(false)
            .show()
    }
}