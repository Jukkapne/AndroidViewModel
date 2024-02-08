package com.example.androidviewmodel

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidviewmodel.ui.theme.AndroidViewModelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidViewModelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Obtain an instance of ShoppingListViewModel scoped to this Composable
                    val shoppingListViewModel: ShoppingListViewModel = viewModel()
                    ShoppingListApp(shoppingListViewModel)
                }
            }
        }
    }
}



class ShoppingListViewModel : ViewModel() {
    // Käytetään sisäistä tilaa ilman LiveDataa tai StateFlow'ta
    private var _shoppingList = listOf<String>()
    val shoppingList: List<String>
        get() = _shoppingList

    fun addItem(item: String) {
        _shoppingList = _shoppingList + item
    }

    fun removeItem(item: String) {
        _shoppingList = _shoppingList.filter { it != item }
    }
}

@Composable
fun ShoppingListApp(viewModel: ShoppingListViewModel) {
    // Muista ViewModel tilan muutokset
    var shoppingList by remember { mutableStateOf(viewModel.shoppingList) }

    // Päivitä composable tila kun viewModelin tila muuttuu
    ShoppingListScreen(
        shoppingItems = shoppingList,
        onAddItem = { item ->
            viewModel.addItem(item)
            shoppingList = viewModel.shoppingList
        },
        onRemoveItem = { item ->
            viewModel.removeItem(item)
            shoppingList = viewModel.shoppingList
        }
    )
}
@Composable
fun ShoppingListScreen(shoppingItems: List<String>, onAddItem: (String) -> Unit, onRemoveItem: (String) -> Unit) {
    val TAG = "ShoppingListScreen"
    Column {
        AddItemInput(onAddItem)
        Divider()
        ShoppingItemsList(shoppingItems, onRemoveItem)
    }
}

@Composable
fun AddItemInput(onAddItem: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val TAG = "AddItemInput"
    Row {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Add item") }
        )
        Button(
            onClick = {
                Log.d(TAG,"clicked add")
                onAddItem(text)
                text = ""
            }) {
            Text("Add")
        }
    }
}

@Composable
fun ShoppingItemsList(items: List<String>, onRemoveItem: (String) -> Unit) {
    LazyColumn {
        items(items) { item ->
            ShoppingItem(item, onRemoveItem)
        }
    }
}

@Composable
fun ShoppingItem(item: String, onRemoveItem: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = item, modifier = Modifier.weight(1f))
        IconButton(onClick = { onRemoveItem(item) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}