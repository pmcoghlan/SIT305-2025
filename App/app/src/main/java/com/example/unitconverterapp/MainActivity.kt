package com.example.unitconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unitconverterapp.ui.theme.UnitConverterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UnitConverterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Conversion Function
fun convertUnits(sourceUnit: String, destinationUnit: String, inputValue: Double): Double {
    return when (sourceUnit to destinationUnit) {
        "inch" to "cm" -> inputValue * 2.54
        "cm" to "inch" -> inputValue / 2.54
        "foot" to "cm" -> inputValue * 30.48
        "cm" to "foot" -> inputValue / 30.48
        "yard" to "cm" -> inputValue * 91.44
        "cm" to "yard" -> inputValue / 91.44
        "mile" to "km" -> inputValue * 1.60934
        "km" to "mile" -> inputValue / 1.60934

        "pound" to "kg" -> inputValue * 0.453592
        "kg" to "pound" -> inputValue / 0.453592
        "ounce" to "g" -> inputValue * 28.3495
        "g" to "ounce" -> inputValue / 28.3495
        "ton" to "kg" -> inputValue * 907.185
        "kg" to "ton" -> inputValue / 907.185

        "Celsius" to "Fahrenheit" -> (inputValue * 1.8) + 32
        "Fahrenheit" to "Celsius" -> (inputValue - 32) / 1.8
        "Celsius" to "Kelvin" -> inputValue + 273.15
        "Kelvin" to "Celsius" -> inputValue - 273.15

        else -> throw IllegalArgumentException("Unsupported conversion from $sourceUnit to $destinationUnit")
    }
}

@Composable
fun UnitConverterScreen(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf("") }
    var sourceUnit by remember { mutableStateOf("inch") }
    var destinationUnit by remember { mutableStateOf("cm") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Unit Converter", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter value") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "From:", modifier = Modifier.padding(end = 8.dp))
                DropdownMenuBox(
                    selectedOption = sourceUnit,
                    onOptionSelected = { sourceUnit = it },
                    options = listOf("inch", "cm", "foot", "yard", "mile", "km", "pound", "kg", "ounce", "g", "ton", "Celsius", "Fahrenheit", "Kelvin")
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "To:", modifier = Modifier.padding(end = 8.dp))
                DropdownMenuBox(
                    selectedOption = destinationUnit,
                    onOptionSelected = { destinationUnit = it },
                    options = listOf("inch", "cm", "foot", "yard", "mile", "km", "pound", "kg", "ounce", "g", "ton", "Celsius", "Fahrenheit", "Kelvin")
                )
            }
        }

        Button(
            onClick = {
                result = try {
                    val input = inputValue.toDouble()
                    convertUnits(sourceUnit, destinationUnit, input).toString()
                } catch (e: Exception) {
                    "Invalid input"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Result: $result", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DropdownMenuBox(selectedOption: String, onOptionSelected: (String) -> Unit, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverterAppTheme {
        UnitConverterScreen()
    }
}
