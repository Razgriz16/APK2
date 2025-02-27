package com.example.oriencoop_score.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.HandleNotifications
import com.example.oriencoop_score.model.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val handleNotifications: HandleNotifications) : ViewModel() {

    fun sendNotification(notification: Notifications) {
        viewModelScope.launch {
            handleNotifications.showNotification(notification)
        }
    }
}