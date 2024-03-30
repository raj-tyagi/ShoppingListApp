package com.example.myshoppinglistapp
// This file is created to keep the mainactivity.kt clean

import android.view.RoundedCorner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


data class ShoppingItem(val id:Int,         //A data class to store data about the objects i.e.shopping items
                        var name: String,
                        var quantity:Int,
                        var isEditing : Boolean = false)



@OptIn(ExperimentalMaterial3Api::class) //the Alert Dialog is in experimental stage and can be removed in future so this @OptIn had to be used for now
@Composable
fun ShoppingListApp()
{
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    /* this line is very deep with meaning
    //here we are setting up a variable named sItems that changes UI along with the changes made
    // It will be used to maintain the state of list of items --> that is why used remember keyword to remember its space and use
    // in the brackets of mutablestateof , we use datatype generally

    In short iska matlab h ki hmne ek variable bnaya jo ki UI mei show hoga aur as per the values-changed, change hoga
    aur fun fact about this variable is ki ye variable acrtually mei ek list hoga of type ShoppingItem - class {Like an array}
    jo akela list ko hold krega

    ###############################GPT MADE RESPONSE FOR THIS LINE####################################
    Certainly! Let's break down the line:

var sItems by remember { mutableStateOf(listOf(<ShoppingItem>())) }

Here's what each part does:
1. `var sItems`: This declares a mutable variable named `sItems`. It's mutable because it can be reassigned to different values.
2. `by`: This is a keyword used in Kotlin for property delegation. It means that the property `sItems` will be delegated to another object or function, which will provide the implementation for getting and setting the value of `sItems`.
3. `remember { mutableStateOf(...) }`: This is where the magic happens. It uses two important concepts:
   - `remember`: This is a composable function provided by Jetpack Compose. It's used to retain the state of a composable function across recompositions. In simpler terms, it remembers the value across UI updates.

   - `mutableStateOf(...)`: This is another composable function that provides a state holder for a single value that can be read and modified. When the value held by `mutableStateOf` changes, the composable recomposes automatically.
4. `listOf(<ShoppingItem>())`: This initializes the `sItems` variable with an empty list containing elements of type `ShoppingItem`. `<ShoppingItem>` is a placeholder here, and it should be replaced with actual `ShoppingItem` objects.
So, overall, this line initializes a mutable state variable `sItems` using `remember` and `mutableStateOf`, ensuring that any changes to `sItems` will automatically recompose the UI. It starts with an empty list of `ShoppingItem`.
    */


    var showDialog by remember {mutableStateOf(false) }

    var itemName by remember { mutableStateOf("") }  // these are variables are used to enter data and quantity in the alert dialog
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(), // so that column can fill the maximum space of page
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = { showDialog = true }, // whenever we use this button alert dialog will appear to take input
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        //Note that modifier is generally used for alignment purposes of any row, column, box etc
        //It is always written in () brackets before {}
        {
            Text(text = "Add Item")
        }

        /*A lazy column is a composable function provided by Jetpack Compose
         that allows you to create a vertically scrolling list of items efficiently.
         It's called "lazy" because it only composes and lays out the items that
          are currently visible on the screen, recycling the views as you scroll
         to conserve resources.--> can ask gpt to give simple statement about it*/
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()  //You can see the button of add item will bw pushed to top because this lazy column is set to fit max screen although not visible yet
                .padding(8.dp) //Note that here a padding is added rather than spacer to explore the syntax
        )
        {

            items(sItems){
                item ->
                if(item.isEditing)
                {

                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}

                        val editedItem = sItems.find{it.id==item.id}
                        editedItem?.let{ it.name =editedName
                        it.quantity = editedQuantity}
                    })
                }
                else
                {
                    ShoppingListItem(item = item ,
                        onEditClick = {
                        //finding out which item we are editing and changing is "is Editing boolean" to true
                        sItems =sItems.map{it.copy(isEditing = it.id == item.id)}},
                        onDeleteClick = { sItems =sItems-item
                    })
                }
            }
        }


    }

    if(showDialog)
    {/*An AlertDialog in Kotlin is a pop-up window that displays urgent messages or prompts users for decisions.
    It's created using the `AlertDialog.Builder` class and can have buttons for actions like "OK" or "Cancel".
    Here we change its scope to modify it to use for enetring and adding items in the list*/

        AlertDialog(onDismissRequest = { showDialog = false }, //this statement tells alertdialog to be dismissed whenever showDialog variable is false
            confirmButton = {
                            Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) // this means row elements should have a space between them and fill max width
                            {
                                Button(onClick = {
                                    if (itemName.isNotBlank()){
                                        val newItem = ShoppingItem(    // This is a shopping item object of class ShoppingItem class and it will be created each time an item is added to the list to add all its details
                                            id = sItems.size +1, //id of obj will be list size +1
                                            name = itemName,
                                            quantity = itemQuantity.toInt()
                                        )
                                        sItems = sItems + newItem // after creating the new object add it to the sItems list which is actually a list of objects
                                        showDialog = false

                                        itemName ="" //clearing the itemName and itemQuantity
                                        itemQuantity=""
                                    }
                                })
                                {
                                    Text(text = "Add")
                                }
                                Button(onClick = { showDialog = false}) // this means all this second button does is closing the alert dialog box
                                {
                                    Text(text = "Cancel")
                                }

                            }
            },
            title = {Text("Add Shopping Item")} ,
            text ={
                //Although here only we must use text as per the use case of alert dialog but as per our need we are using it in advanced scope
                // because it is actually a composable not simple text
                Column {
                    //Outlined Text Field is used to take user inputs
                    OutlinedTextField( // what we actually did here is,in the column that we created in the text field of alert column to use composable items in it. Mtlb hmne yaha pr ek outlined field bnayii like ek oulined box inside a column aur uski cheezein (about) daali phir.
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine =true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField( // yaha hmne quantity ka input liya h
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine =true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

            })

    }
}


@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete : (String , Int) ->Unit)
{
    var editedName by remember { mutableStateOf(item.name)  }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())  }
    var isEditing by remember { mutableStateOf(item.isEditing)  }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) //SpaceEvenly would make all elments of row evenly spaced
    {
        Column {
            BasicTextField( value =editedName ,onValueChange = {editedName = it}, // the it here is a variable which is used to store the changed value to editedName Variable as per onValueChange
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp) //wrapContentSize means to give just enough space to accomodate the content
            )

            BasicTextField( value =editedQuantity ,onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp) //wrapContentSize means to give just enough space to accomodate the content
            )
        }

        Button(onClick = {
            isEditing =false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1) //if entered quantity is some gibberish and not a number , it will automatically take qty =1 due to use of elvis operator
        })
        {
            Text(text = "Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: ()-> Unit, //lambda function explained below
    onDeleteClick: ()-> Unit,)
{
    Row(modifier= Modifier
        .padding(8.dp)
        .fillMaxSize()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween)

    {
        Text(text =item.name ,modifier=Modifier.padding(8.dp))
        Text(text ="Qty:${item.quantity}" ,modifier=Modifier.padding(8.dp))

        Row (modifier =Modifier.padding(8.dp)){ //we created a row inside a row to insert 2 buttons Edit and Delete

            IconButton(onClick = onEditClick) {
                Icon(imageVector =Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector =Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

/*
Just to specify the difference between these all:

Use Box for stacking elements, applying transformations, or positioning items relative to each other.
Use Column for arranging elements vertically, like lists or top-to-bottom layouts.
Use OutlinedTextField for capturing user input with an outlined border.
Use Row for arranging elements horizontally, like lists or left-to-right layouts.

* use Box when you need to layer or stack composables on top of each other with precise control over their positioning,
* use Column when you need to arrange composables vertically in a top-to-bottom fashion, such as for lists, forms, or sections of your UI.
*/


/*
#################################### LAMBDA FUNCTIONS ###############################################
Lambda function is something jo ki ek function h and is passed as a parameter inside someother big function
and is executed by itself when the bigger function is called

syntax : fun bigfun(lambdafun : @Composable ()->Unit){}

 this line explains that a bigfun named function has a function named lambdafun passed as a parameter to it
 and it is a composable .   ()->Unit means the lambda function doesnot contain any parameters and returns unity on execution i.e.no value

 Another EXAMPLE OF LAMBDA EXPRESSION :  val twiceNumber : (Int) -> Int ={it*2}
                                          Text(twiceNumber(5).toString())

           this in simpler terms means ,pass an integer with the variable twiceNumber and it will return it*2 as answer in Integer form stored in the variable itself
*/

/*
gpt answer for lambda functions:


Sure, let's break down the lambda functions used in the Jetpack Compose example:

1. **Lambda Function in `MyApp` Composable**:
```kotlin

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Column {
        content()
    }
}
```
Here, `MyApp` is a composable function that takes another composable function as a parameter named `content`. The `@Composable` annotation indicates that this function describes a UI element. The `content` parameter is a lambda function that takes no arguments and returns `Unit` (i.e., nothing). This lambda function is invoked inside the `Column` composable to include the UI elements defined by the caller of `MyApp`.

2. **Lambda Function in `Button` Composable**:
```kotlin
Button(onClick = { /* Handle button click */ }) {
    Text("Click Me")
}
```
In this part of the code, we define a `Button` composable. The `onClick` parameter of the `Button` is a lambda function that gets executed when the button is clicked. This lambda function is defined inline using the `{}` syntax. However, in this example, it's commented out (`{/* Handle button click */}`), so it doesn't actually perform any action when the button is clicked. Inside the `Button` composable, we have another lambda function that defines the content of the button, which in this case is a `Text` composable displaying "Click Me".

Lambda functions in Jetpack Compose are a concise way to define behavior inline, especially for event handling and defining the content of composables. They make the code more readable and maintainable by keeping the logic close to where it's used.
*/



/*###################################### MAPS ######################################
* MAPS are used to iterate over the list elements and make a change to them as per the given condition to d=store the new answer at their very index
*
* eg : val numbers = listOf(1,2,3)
*      val doubled = numbers.map{it*2}
*       println(doubled)
*
* The output will be a list [2,4,6]
*
* */


/*#################################### COPY FUNCTION ###########################################
*This function is used to create an exact copy of any object without messing around with the original object and additionally we can do some changes in the new copied objed properties as per requirement
* eg : data class classy(val name :string , val gender : STRING)
*
*   val objman = classy(name = "Raman" , gender =" Male")
*    val objman2 = objman.copy(name = "Rohan")
*
* println(objman)-----------> gives : objman(name = Raman, gender = Male)
* println(objman2)-----------> gives : objman2(name = Rohan, gender = Male)
* */



/*######################################## LET FUNCTION AND KEYWORD #########################################
* */