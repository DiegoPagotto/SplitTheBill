package br.edu.ifsp.scl.ads.splitthebill.view

import androidx.appcompat.app.AppCompatActivity

sealed class BaseActivity :  AppCompatActivity(){
    protected val EXTRA_PERSON = "Person"
    protected val EXTRA_VIEW_PERSON = "View Person"
}
