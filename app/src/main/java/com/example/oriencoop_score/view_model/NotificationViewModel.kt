package com.example.oriencoop_score.view_model


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.HandleNotifications
import com.example.oriencoop_score.model.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(private val handleNotifications: HandleNotifications) : ViewModel() {

    // LiveData or State to hold the notifications.  Using mutableStateListOf for simplicity and reactivity.
    private val _notifications = mutableStateListOf<Notifications>()
    val notifications: List<Notifications> = _notifications


    fun sendNotification(notification: Notifications) {
        viewModelScope.launch {
            handleNotifications.showNotification(notification)
            // Add to our list for the dialog.  Crucially, add *after* showing the system notification.
            _notifications.add(notification)

        }
    }

    val singleNotification = Notifications(
        TITULO = "Movimiento realizado",
        DESCRIPCION = "Se ha realizado un giro de $1000 en la cuenta de ahorros.",
        DATE = "2023-10-27",
        TIME = "15:00"
    )


    // Function to create and send dummy notifications
    fun sendDummyNotifications() {
        val dummyNotifications = listOf(
            Notifications(
                TITULO = "Meeting Reminder",
                DESCRIPCION = "Don't forget the team meeting at 2 PM.",
                DATE = "2023-10-27",
                TIME = "14:00"
            ),
            Notifications(
                TITULO = "Task Deadline",
                DESCRIPCION = "Your project report is due tomorrow.",
                DATE = "2023-10-28",
                TIME = "09:00"
            )/*,
            Notifications(
                TITULO = "Lunch Break",
                DESCRIPCION = "It's time for lunch!",
                DATE = "2023-10-27",
                TIME = "12:30"
            ),
            Notifications(
                TITULO = "Workout Time",
                DESCRIPCION = "Don't forget your workout today",
                DATE = "2023-10-27",
                TIME = "18:00"
            )
            */
        )

        dummyNotifications.forEach { notification ->
            sendNotification(notification)
        }
    }
    //Clear all notifications
    fun clearNotifications() {
        _notifications.clear()
    }
}