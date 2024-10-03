package com.example.shoppingapp


import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController


data class shoppingList(val id:Int, var name:String, var price:Int, var address: String, var isEditing:Boolean=false)

@Composable
fun ShoppingListApp(locationUtils: LocationUtils, context: Context, viewModel: LocationViewModel, navController: NavController, address: String){
    var alertDialog by remember{mutableStateOf(false)}
    var itemName by remember{mutableStateOf("")}
    var itemPrice by remember{mutableStateOf("")}
    var SItems by remember{ mutableStateOf(listOf<shoppingList>())}
    var itemAddress by remember{ mutableStateOf("")}

    val requestPermissionLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = {
        permissions->
        if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true){
            locationUtils.updateInformation(viewModel)
        }else{
            val rationaleRequest=ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION )
            if(rationaleRequest){
                Toast.makeText(context, "Please give permission", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Please give permission", Toast.LENGTH_LONG).show()
            }
        }
    })

    if(alertDialog){

        AlertDialog(onDismissRequest = {
            alertDialog=false
        }, confirmButton = {
            Row {
                Button(onClick={
                    if(itemName.isNotBlank()){
                        var newItem=shoppingList(SItems.size+1, itemName, itemPrice.toInt(), itemAddress )
                        SItems=SItems+newItem
                        itemName=""
                        itemAddress=""
                    }
                    alertDialog=false
                }){
                    Text("ADD")
                }
            }
        }, title={ Text("Add Items ") },
            text={
                Column { OutlinedTextField(value=itemName, onValueChange = { itemName=it }, modifier = Modifier.padding(8.dp))
                    OutlinedTextField(value=itemPrice, onValueChange = { itemPrice=it }, modifier = Modifier.padding(8.dp))
                    Button(onClick = {
                        if(locationUtils.hasLocationPermission(context)){
                            locationUtils.updateInformation(viewModel)
                            navController.navigate("locationScreen"){
                                this.launchSingleTop
                            }
                        }else{
                            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                        }
                    }){
                        Text("Add place")
                    }
                }
            }
        )


    }
    Column() {
        Button(onClick = {
            alertDialog=true
        }){
            Text("ADD ITEM")
        }
        LazyColumn(modifier = Modifier.padding(8.dp).fillMaxSize()){
            items(SItems){
                item->
                if(item.isEditing){
                    EditButton(item, saveButton={editName, editPrice->SItems=SItems.map {
                        it.copy(isEditing = false) }
                        val editedItem=SItems.find{it.id==item.id}
                        editedItem?.let {
                            it.name=editName
                            it.price=editPrice
                            it.address=address
                        }
                    })
                }else{
                    EachItem(item, onDeleteButton={
                        SItems=SItems-item
                    }, onEditButton={
                        SItems=SItems.map { it.copy(isEditing = it.id==item.id) }
                    } )
                }
            }

        }
    }
}

@Composable
fun EachItem(shoppinglist:shoppingList, onDeleteButton:()->Unit, onEditButton:()->Unit){
    Column {
        Row{
            Text(shoppinglist.name)
            Text(shoppinglist.price.toString())

            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = onEditButton
            ){
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
        Row{
            Text(shoppinglist.address)
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = onDeleteButton ){
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun EditButton(shoppinglist:shoppingList, saveButton:(String, Int)->Unit){
    var editName by remember { mutableStateOf(shoppinglist.name) }
    var editPrice by remember { mutableStateOf(shoppinglist.price.toString()) }
    var isEditing by remember { mutableStateOf(shoppinglist.isEditing) }
    Row{
        Column {BasicTextField(value=editName, onValueChange = { editName=it }, modifier = Modifier.padding(8.dp))
            BasicTextField(value=editPrice, onValueChange = { editPrice=it }, modifier = Modifier.padding(8.dp))
        }
        Button(onClick = {
            saveButton(editName,editPrice.toIntOrNull()?:1)
            isEditing=false
        }){
            Text("Save")
        }
    }
}