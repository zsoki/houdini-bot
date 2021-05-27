package hu.zsoki.houdinibot.app.subscription

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

object SubscriptionTable : Table("subscription") {
    val CHANNEL_ID: Column<Long> = long("channel_id")
    val CREATED_AT: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val DIGEST: Column<Digest> = enumeration("digest", Digest::class)

    override val primaryKey = PrimaryKey(CHANNEL_ID)

    suspend fun getChannelIdFor(digest: Digest): ChannelIdQueryResult = newSuspendedTransaction(Dispatchers.IO) {
        try {
            val channelIds = SubscriptionTable
                .select { DIGEST eq digest }
                .map { resultRow -> resultRow[CHANNEL_ID] }
                .toSet()
            when {
                channelIds.isNotEmpty() -> ChannelIdQueryResult.Success(channelIds)
                else -> ChannelIdQueryResult.Empty
            }
        } catch (e: Exception) {
            ChannelIdQueryResult.Error(e)
        }
    }

    suspend fun toggleSubscription(channelId: Long, digest: Digest): SubscriptionToggleResult = newSuspendedTransaction(Dispatchers.IO) {
        try {
            val subscription = SubscriptionTable
                .select { (DIGEST eq digest) and (CHANNEL_ID eq channelId) }
                .firstOrNull()

            if (subscription == null) {
                subscribeInternal(channelId, digest)
                SubscriptionToggleResult.Subscribed
            } else {
                unSubscribeInternal(subscription)
                SubscriptionToggleResult.Unsubscribed
            }
        } catch (e: Exception) {
            SubscriptionToggleResult.Error(e)
        }
    }

    private fun subscribeInternal(guildId: Long, digest: Digest) {
        SubscriptionTable.insert { insertRow ->
            insertRow[CHANNEL_ID] = guildId
            insertRow[DIGEST] = digest
        }
    }

    private fun unSubscribeInternal(subscriptionRow: ResultRow) {
        SubscriptionTable.deleteWhere { CHANNEL_ID eq subscriptionRow[CHANNEL_ID] }
    }
}
