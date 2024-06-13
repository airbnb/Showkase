package com.airbnb.android.showkasesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        replaceFragment(MainFragment.newInstance(), "MainFragment")
    }

    fun replaceFragment(fragment: Fragment, tag: String?) {
        val fragmentManager = supportFragmentManager

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)

        transaction.addToBackStack(tag)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}