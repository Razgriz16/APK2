package com.example.oriencoop_score.view.mis_productos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oriencoop_score.R
import com.example.oriencoop_score.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductButton(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentColor: Color = Color.Black,
    isVisible: Boolean = true,
    borderColor: Color? = null,
    iconSize: Dp = 30.dp, // Default icon size
    textStyle: TextStyle = TextStyle(fontSize = 13.sp)
) {
    if (isVisible) {
        Card(onClick = onClick,
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
                    .size(85.dp)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                    color = contentColor,
                    style = textStyle
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductButtonPreview() {
    ProductButton(
        icon = R.drawable.ic_launcher_foreground, // Replace with your actual icon resource
        text = "Example Button",
        onClick = {}
    )
}

