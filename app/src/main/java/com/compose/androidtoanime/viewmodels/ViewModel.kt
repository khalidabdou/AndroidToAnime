package com.compose.androidtoanime.viewmodels


import android.app.Application

import androidx.lifecycle.*
import com.compose.androidtoanime.RepositoryImpl



import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(
    private val imageRepo: RepositoryImpl,
    application: Application
) : AndroidViewModel(application) {


}