package com.airbnb.android.showkase_processor_testing

import androidx.compose.ui.graphics.vector.ImageVector
import com.airbnb.android.showkase.annotation.ShowkaseIcon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

public class Composables {
    @ShowkaseIcon("name", "group")
    public val closeIcon: ImageVector = Icons.Filled.Close
}