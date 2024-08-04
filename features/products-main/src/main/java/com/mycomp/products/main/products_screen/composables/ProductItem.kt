package com.mycomp.products.main.products_screen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycomp.products.main.R
import com.mycomp.products.main.models.ProductUi
import com.mycomp.products.main.utils.convertMillisToDate

@Composable
internal fun ProductItem(
    product: ProductUi,
    onChangeAmountClick: (Int) -> Unit,
    onRemoveProductClick: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        ChangeQuantityDialog(
            initialQuantity = product.amount,
            onDismissRequest = { showEditDialog = false },
            onConfirm = { quantity ->
                onChangeAmountClick(quantity)
                showEditDialog = false
            }
        )
    }

    if (showRemoveDialog) {
        DeleteProductDialog(
            onDismissRequest = { showRemoveDialog = false },
            onConfirm = {
                onRemoveProductClick()
                showRemoveDialog = false
            }
        )
    }


    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { showEditDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        tint = colorResource(id = R.color.icon_blue)
                    )
                }
                IconButton(
                    onClick = { showRemoveDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = colorResource(id = R.color.icon_red)
                    )
                }
            }
            TagsFlowRow(product)
            Row {
                Column {
                    Text(text = stringResource(R.string.in_stock))
                    Text(text = product.amount.toString())
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(text = stringResource(R.string.date_of_adding))
                    Text(text = convertMillisToDate(product.time), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun TagsFlowRow(product: ProductUi) {
    val itemSpacing = 8.dp

    Layout(
        content = {
            product.tags.forEach { tag ->
                SuggestionChip(
                    onClick = {},
                    label = { BasicText(tag) }
                )
            }
        },
        modifier = Modifier
    ) { measurables, constraints ->
        val itemConstraints = constraints.copy(maxWidth = constraints.maxWidth / 2)
        val placeables = measurables.map { it.measure(itemConstraints) }

        var rowWidth = 0
        var rowHeight = 0
        var maxHeight = 0

        val positions = mutableListOf<Pair<Int, Int>>()
        var isSecondRow = false

        placeables.forEachIndexed { index, placeable ->
            if (rowWidth + placeable.width > constraints.maxWidth) {
                rowWidth = 0
                rowHeight += maxHeight + itemSpacing.roundToPx()
                maxHeight = 0
                isSecondRow = true
            }

            positions.add(Pair(rowWidth, rowHeight))
            rowWidth += placeable.width + itemSpacing.roundToPx()
            maxHeight = maxOf(maxHeight, placeable.height)
        }

        layout(constraints.maxWidth, rowHeight + maxHeight) {
            positions.forEachIndexed { index, position ->
                val (x, y) = position
                val offsetY = if (isSecondRow && y > 0) -16.dp.roundToPx() else 0
                placeables[index].placeRelative(x, y + offsetY)
            }
        }
    }
}