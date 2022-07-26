package com.airbnb.android.showkase_processor_testing

import androidx.annotation.DrawableRes
import com.airbnb.android.showkase.annotation.ShowkaseIcon

public class Composables {
    @ShowkaseIcon("name", "group")
    @DrawableRes
    public val verifiedUserIcon: Int = R.drawable.ic_baseline_verified_user_24
}