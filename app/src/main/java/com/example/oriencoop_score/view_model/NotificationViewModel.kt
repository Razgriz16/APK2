package com.example.oriencoop_score.view_model


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.oriencoop_score.model.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _notifications = mutableStateOf<List<Notifications>>(emptyList())
    val notifications = _notifications

    fun addNotification(description: String) {
        val now = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val newNotification = Notifications(
            description = description,
            date = dateFormat.format(now),
            time = timeFormat.format(now)
        )
        _notifications.value = _notifications.value + newNotification
    }

    fun clearNotification(notification: Notifications) {
        _notifications.value = _notifications.value - notification
    }
}