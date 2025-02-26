package com.example.oriencoop_score.view_model


import android.app.Notification
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.HandleNotifications
import com.example.oriencoop_score.model.Notifications
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val handleNotifications: HandleNotifications) : ViewModel() {

    fun sendNotification(notification: Notifications) {
        viewModelScope.launch {
            handleNotifications.showNotification(notification)
        }
    }
}