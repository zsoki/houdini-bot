package hu.zsoki.houdinibot.app.subscription

sealed class ChannelIdQueryResult {
    class Success(val channelIds: Collection<Long>) : ChannelIdQueryResult()
    object Empty : ChannelIdQueryResult()
    class Error(val exception: Exception) : ChannelIdQueryResult()
}
