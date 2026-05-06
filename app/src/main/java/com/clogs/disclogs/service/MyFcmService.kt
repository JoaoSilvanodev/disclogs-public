package com.clogs.disclogs.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.clogs.disclogs.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFcmService : FirebaseMessagingService() {

    // Sempre que o Google gera um token novo, este método é chamado
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // Quando uma notificação chega com o app aberto
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Nova Interação"
        val body = message.notification?.body ?: "Você tem novidades no Disclogs."

        val notificationBuilder = NotificationCompat.Builder(this, "DISCLOGS_CHANNEL")
            .setSmallIcon(R.mipmap.ic_icon) // Você pode trocar por um ícone específico depois, tipo R.drawable.ic_notification
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Faz a notificação descer na tela (Heads-up)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}