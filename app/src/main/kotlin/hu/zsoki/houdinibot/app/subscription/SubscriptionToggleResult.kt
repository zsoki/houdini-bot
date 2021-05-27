package hu.zsoki.houdinibot.app.subscription

sealed class SubscriptionToggleResult {
    object Subscribed : SubscriptionToggleResult()
    object Unsubscribed : SubscriptionToggleResult()
    class Error(error: Exception) : SubscriptionToggleResult()
}
