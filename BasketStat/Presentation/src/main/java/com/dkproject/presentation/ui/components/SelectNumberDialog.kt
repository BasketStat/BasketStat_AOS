package com.dkproject.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.splashColor


@Composable
fun SelectNumberDialog(
    onDismiss: () -> Unit,
    onBackNumber: (Int) -> Unit
) {
    var selectedNumber by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE0E0E0)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "해당 경기에서의\n선수 등번호를 입력해 주세요.", color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedNumber,
                    onValueChange = {number->
                        if (number.isEmpty()) {
                            selectedNumber = number
                        } else if (number.length <= 2 && number.toIntOrNull() != null) {
                            selectedNumber = number
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        textAlign = TextAlign.End,  // 텍스트를 오른쪽 정렬
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "등 번호를 입력해 주세요.",
                            textAlign = TextAlign.End,
                            color = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(modifier = Modifier.weight(1f),
                        onClick = onDismiss) {
                        Text(text = "취소", color = Color.Black)
                    }
                    TextButton(modifier = Modifier.weight(1f),
                        onClick = { onBackNumber(selectedNumber.toInt()) }) {
                        Text(text = "확인", color = Color.Black)
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun SelectNumberDialogPreview() {
    SelectNumberDialog(onDismiss = {}) {}
}