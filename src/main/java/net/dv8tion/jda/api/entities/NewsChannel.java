package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.managers.channel.concrete.NewsChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@link StandardGuildMessageChannel} that are News Channels.
 *
 * The Discord client may refer to these as Announcement Channels.
 *
 * Members can subscribe channels in their own guilds to receive messages crossposted from this channel.
 * This is referred to as following this channel.
 *
 * Messages sent in this channel can be crossposted, at which point they will be sent (via webhook) to all subscribed channels.
 *
 * @see Message#getFlags()
 * @see net.dv8tion.jda.api.entities.Message.MessageFlag#CROSSPOSTED
 */
public interface NewsChannel extends StandardGuildMessageChannel
{
    /**
     * Subscribes to the crossposted messages in this channel.
     * <br>This will create a {@link Webhook} of type {@link WebhookType#FOLLOWER FOLLOWER} in the target channel.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>If the target channel doesn't exist or is not visible to the currently logged in account</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>If the currently logged in account does not have {@link Permission#MANAGE_WEBHOOKS} in the <b>target channel</b></li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MAX_WEBHOOKS MAX_WEBHOOKS}
     *     <br>If the target channel already has reached the maximum capacity for webhooks</li>
     * </ul>
     *
     * @param  targetChannelId
     *         The target channel id
     *
     * @throws IllegalArgumentException
     *         If null is provided
     *
     * @return {@link RestAction}
     *
     * @since  4.2.1
     */
    @NotNull
    @CheckReturnValue
    RestAction<Webhook.WebhookReference> follow(@NotNull String targetChannelId);

    /**
     * Subscribes to the crossposted messages in this channel.
     * <br>This will create a {@link Webhook} of type {@link WebhookType#FOLLOWER FOLLOWER} in the target channel.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>If the target channel doesn't exist or not visible to the currently logged in account</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>If the currently logged in account does not have {@link Permission#MANAGE_WEBHOOKS} in the <b>target channel</b></li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MAX_WEBHOOKS MAX_WEBHOOKS}
     *     <br>If the target channel already has reached the maximum capacity for webhooks</li>
     * </ul>
     *
     * @param  targetChannelId
     *         The target channel id
     *
     * @return {@link RestAction}
     *
     * @since  4.2.1
     */
    @NotNull
    @CheckReturnValue
    default RestAction<Webhook.WebhookReference> follow(long targetChannelId)
    {
        return follow(Long.toUnsignedString(targetChannelId));
    }

    /**
     * Subscribes to the crossposted messages in this channel.
     * <br>This will create a {@link Webhook} of type {@link WebhookType#FOLLOWER FOLLOWER} in the target channel.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>If the target channel doesn't exist or not visible to the currently logged in account</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>If the currently logged in account does not have {@link Permission#MANAGE_WEBHOOKS} in the <b>target channel</b></li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MAX_WEBHOOKS MAX_WEBHOOKS}
     *     <br>If the target channel already has reached the maximum capacity for webhooks</li>
     * </ul>
     *
     * @param  targetChannel
     *         The target channel
     *
     * @throws MissingAccessException
     *         If the currently logged in account does not have {@link Member#hasAccess(GuildChannel) access} in the <b>target channel</b>.
     * @throws InsufficientPermissionException
     *         If the currently logged in account does not have {@link Permission#MANAGE_WEBHOOKS} in the <b>target channel</b>.
     * @throws IllegalArgumentException
     *         If null is provided
     *
     * @return {@link RestAction}
     *
     * @since  4.2.1
     */
    @NotNull
    @CheckReturnValue
    default RestAction<Webhook.WebhookReference> follow(@NotNull TextChannel targetChannel)
    {
        Checks.notNull(targetChannel, "Target Channel");
        Member selfMember = targetChannel.getGuild().getSelfMember();
        Checks.checkAccess(selfMember, targetChannel);
        if (!selfMember.hasPermission(targetChannel, Permission.MANAGE_WEBHOOKS))
            throw new InsufficientPermissionException(targetChannel, Permission.MANAGE_WEBHOOKS);
        return follow(targetChannel.getId());
    }

    /**
     * Attempts to crosspost the provided message.
     *
     * <p>The following {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} are possible:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#ALREADY_CROSSPOSTED ALREADY_CROSSPOSTED}
     *     <br>The target message has already been crossposted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The request was attempted after the account lost access to the
     *         {@link net.dv8tion.jda.api.entities.Guild Guild}
     *         typically due to being kicked or removed, or after {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL}
     *         was revoked in the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The request was attempted after the account lost
     *         {@link net.dv8tion.jda.api.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE} in the TextChannel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The provided {@code messageId} is unknown in this MessageChannel, either due to the id being invalid, or
     *         the message it referred to has already been deleted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>The request was attempted after the channel was deleted.</li>
     * </ul>
     *
     * @param  messageId
     *         The messageId to crosspost
     *
     * @throws java.lang.IllegalArgumentException
     *         If provided {@code messageId} is {@code null} or empty.
     * @throws MissingAccessException
     *         If the currently logged in account does not have {@link Member#hasAccess(GuildChannel) access} in this channel.
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have
     *         {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL} in this channel.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction} - Type: {@link Message}
     *
     * @since  4.2.1
     */
    @NotNull
    @CheckReturnValue
    default RestAction<Message> crosspostMessageById(@NotNull String messageId)
    {
        Checks.isSnowflake(messageId);
        Checks.checkAccess(getGuild().getSelfMember(), this);
        Route.CompiledRoute route = Route.Messages.CROSSPOST_MESSAGE.compile(getId(), messageId);
        return new RestActionImpl<>(getJDA(), route,
                (response, request) -> request.getJDA().getEntityBuilder().createMessageWithChannel(response.getObject(), this, false));
    }

    /**
     * Attempts to crosspost the provided message.
     *
     * <p>The following {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} are possible:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#ALREADY_CROSSPOSTED ALREADY_CROSSPOSTED}
     *     <br>The target message has already been crossposted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The request was attempted after the account lost access to the
     *         {@link net.dv8tion.jda.api.entities.Guild Guild}
     *         typically due to being kicked or removed, or after {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL}
     *         was revoked in the {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The request was attempted after the account lost
     *         {@link net.dv8tion.jda.api.Permission#MESSAGE_MANAGE Permission.MESSAGE_MANAGE} in the TextChannel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The provided {@code messageId} is unknown in this MessageChannel, either due to the id being invalid, or
     *         the message it referred to has already been deleted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>The request was attempted after the channel was deleted.</li>
     * </ul>
     *
     * @param  messageId
     *         The messageId to crosspost
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have
     *         {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL} in this channel.
     *
     * @return {@link net.dv8tion.jda.api.requests.RestAction} - Type: {@link Message}
     *
     * @since  4.2.1
     */
    @NotNull
    @CheckReturnValue
    default RestAction<Message> crosspostMessageById(long messageId)
    {
        return crosspostMessageById(Long.toUnsignedString(messageId));
    }

    @NotNull
    @Override
    ChannelAction<NewsChannel> createCopy(@NotNull Guild guild);

    @NotNull
    @Override
    default ChannelAction<NewsChannel> createCopy()
    {
        return createCopy(getGuild());
    }

    @NotNull
    @Override
    NewsChannelManager getManager();
}
