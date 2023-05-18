package com.enricog.libraries.notification.api.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.libraries.notification.api.PermissionNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationRationaleViewModel @Inject constructor(
    private val navigationActions: PermissionNavigationActions
) : ViewModel() {

    fun onCancel() {
        viewModelScope.launch {
            navigationActions.back()
        }
    }

    fun onContinue() {
        viewModelScope.launch {
            navigationActions.back()
        }
    }
}