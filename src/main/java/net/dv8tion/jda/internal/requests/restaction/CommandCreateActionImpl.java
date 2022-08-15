/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.internal.requests.restaction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationMap;
import net.dv8tion.jda.api.requests.Request;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.dv8tion.jda.internal.interactions.command.CommandImpl;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class CommandCreateActionImpl extends RestActionImpl<Command> implements CommandCreateAction
{
    private final Guild guild;
    private CommandDataImpl data;

    public CommandCreateActionImpl(JDAImpl api, CommandDataImpl command)
    {
        super(api, Route.Interactions.CREATE_COMMAND.compile(api.getSelfUser().getApplicationId()));
        this.guild = null;
        this.data = command;
    }

    public CommandCreateActionImpl(Guild guild, CommandDataImpl command)
    {
        super(guild.getJDA(), Route.Interactions.CREATE_GUILD_COMMAND.compile(guild.getJDA().getSelfUser().getApplicationId(), guild.getId()));
        this.guild = guild;
        this.data = command;
    }

    @NotNull
    @Override
    public CommandCreateAction addCheck(@NotNull BooleanSupplier checks)
    {
        return (CommandCreateAction) super.addCheck(checks);
    }

    @NotNull
    @Override
    public CommandCreateAction setCheck(BooleanSupplier checks)
    {
        return (CommandCreateAction) super.setCheck(checks);
    }

    @NotNull
    @Override
    public CommandCreateAction deadline(long timestamp)
    {
        return (CommandCreateAction) super.deadline(timestamp);
    }

    @NotNull
    @Override
    public CommandCreateAction setDefaultPermissions(@NotNull DefaultMemberPermissions permission)
    {
        data.setDefaultPermissions(permission);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setGuildOnly(boolean guildOnly)
    {
        data.setGuildOnly(guildOnly);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setLocalizationFunction(@NotNull LocalizationFunction localizationFunction)
    {
        data.setLocalizationFunction(localizationFunction);
        return this;
    }

    @NotNull
    @Override
    public String getName()
    {
        return data.getName();
    }

    @NotNull
    @Override
    public LocalizationMap getNameLocalizations()
    {
        return data.getNameLocalizations();
    }

    @NotNull
    @Override
    public Command.Type getType()
    {
        return data.getType();
    }

    @NotNull
    @Override
    public DefaultMemberPermissions getDefaultPermissions()
    {
        return data.getDefaultPermissions();
    }

    @Override
    public boolean isGuildOnly()
    {
        return data.isGuildOnly();
    }

    @NotNull
    @Override
    public CommandCreateAction timeout(long timeout, @NotNull TimeUnit unit)
    {
        return (CommandCreateAction) super.timeout(timeout, unit);
    }

    @NotNull
    @Override
    public CommandCreateAction setName(@NotNull String name)
    {
        data.setName(name);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setNameLocalization(@NotNull DiscordLocale locale, @NotNull String name)
    {
        data.setNameLocalization(locale, name);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setNameLocalizations(@NotNull Map<DiscordLocale, String> map)
    {
        data.setNameLocalizations(map);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setDescription(@NotNull String description)
    {
        data.setDescription(description);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setDescriptionLocalization(@NotNull DiscordLocale locale, @NotNull String description)
    {
        data.setDescriptionLocalization(locale, description);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction setDescriptionLocalizations(@NotNull Map<DiscordLocale, String> map)
    {
        data.setDescriptionLocalizations(map);
        return this;
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return data.getDescription();
    }

    @NotNull
    @Override
    public LocalizationMap getDescriptionLocalizations()
    {
        return data.getDescriptionLocalizations();
    }

    @NotNull
    @Override
    public List<SubcommandData> getSubcommands()
    {
        return data.getSubcommands();
    }

    @NotNull
    @Override
    public List<SubcommandGroupData> getSubcommandGroups()
    {
        return data.getSubcommandGroups();
    }

    @NotNull
    @Override
    public List<OptionData> getOptions()
    {
        return data.getOptions();
    }

    @NotNull
    @Override
    public CommandCreateAction addOptions(@NotNull OptionData... options)
    {
        data.addOptions(options);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction addSubcommands(@NotNull SubcommandData... subcommand)
    {
        data.addSubcommands(subcommand);
        return this;
    }

    @NotNull
    @Override
    public CommandCreateAction addSubcommandGroups(@NotNull SubcommandGroupData... group)
    {
        data.addSubcommandGroups(group);
        return this;
    }

    @Override
    public RequestBody finalizeData()
    {
        return getRequestBody(data.toData());
    }

    @Override
    protected void handleSuccess(Response response, Request<Command> request)
    {
        DataObject json = response.getObject();
        request.onSuccess(new CommandImpl(api, guild, json));
    }

    @NotNull
    @Override
    public DataObject toData()
    {
        return data.toData();
    }
}
