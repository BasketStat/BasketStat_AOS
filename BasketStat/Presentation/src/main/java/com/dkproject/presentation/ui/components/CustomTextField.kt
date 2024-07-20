package com.dkproject.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.dkproject.presentation.ui.theme.textColor


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
    isSingleLine:Boolean=false,
    enable:Boolean=true,
    leadingIcon : ImageVector?=null,
    trailingIcon : ImageVector?=null,
    shape : Shape = OutlinedTextFieldDefaults.shape,
    colors : TextFieldColors = TextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholderAlignment: TextAlign = TextAlign.Start,
    textStyle: TextStyle = LocalTextStyle.current,
    isError: Boolean = false,

) {
    OutlinedTextField(
        modifier = modifier,
        value = text, onValueChange = { value ->
            onTextChange(value)
        },
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                textAlign = placeholderAlignment,
                color = textColor

            )
        },
        keyboardOptions = keyboardOptions,
        colors = colors,
        enabled = enable,
        singleLine = isSingleLine,
        shape = shape,
        textStyle = textStyle,
        isError = isError,
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null
                )
            }
        }
    )
}