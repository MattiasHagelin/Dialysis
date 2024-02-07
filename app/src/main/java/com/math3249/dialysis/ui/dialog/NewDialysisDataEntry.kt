package com.math3249.dialysis.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.math3249.dialysis.data.model.Test

class NewDialysisDataEntry {
    @Composable
    fun EnterData(
        data: Test,
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit ){
        var text by remember {
            mutableStateOf("")
        }
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card {
                Column (
                    horizontalAlignment = Alignment.End
                ){
                    Row {
                        Text(
                            text = "New data",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(4f)
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel",
                            modifier = Modifier
                                .clickable { onDismissRequest() }
                                .padding(10.dp)
                                .weight(1f)
                        )
                    }
                    Row (
                        modifier = Modifier.
                            fillMaxWidth()
                    ){
                        OutlinedTextField(

                            placeholder = {
                                Text("Type here")
                            },
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.
                                padding(10.dp)
                        )
                    }
                    Button(
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            onConfirmation()
                            data.testname = text
                        },
                        modifier = Modifier.
                            padding(10.dp)
                        ) {
                        Text("Done")
                    }
                }
            }
            
        }
    }
}