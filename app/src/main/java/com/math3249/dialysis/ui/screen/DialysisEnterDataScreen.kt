package com.math3249.dialysis.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.math3249.dialysis.R

class DialysisEnterDataScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EnterData(){

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){

            var secondValue by remember {
                mutableStateOf("")
            }
            WeightRow(stringResource(R.string.weight_before))
            WeightRow(stringResource(R.string.weight_after))
            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(start = 5.dp, top = 5.dp, end = 5.dp)
            ){
                TextField(
                    value = secondValue,
                    placeholder = {
                        Text("Ultra Filtration")
                    },
                    onValueChange = {},
                    modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    private fun WeightRow(rowName: String){
        val items = listOf("kg", "g", "oz.", "lb", "ml", "cl", "dl" )
        var selected by remember {
            mutableStateOf("")
        }
        var value by remember {
            mutableStateOf("")
        }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            TextField(
                value = value,
                placeholder = {
                    Text(rowName)
                },
                onValueChange = {
                    value = it
                },
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp)
            )
        }
    }



    @Preview
    @Composable
    fun EnterDataPreview(){
        EnterData()
    }
}