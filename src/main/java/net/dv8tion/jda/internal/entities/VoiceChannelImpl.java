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

package net.dv8tion.jda.internal.entities;

import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.internal.entities.mixin.channel.middleman.AudioChannelMixin;
import net.dv8tion.jda.internal.managers.channel.concrete.VoiceChannelManagerImpl;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoiceChannelImpl extends AbstractStandardGuildChannelImpl<VoiceChannelImpl> implements
        VoiceChannel,
        AudioChannelMixin<VoiceChannelImpl>
{
    private final TLongObjectMap<Member> connectedMembers = MiscUtil.newLongMap();

    private String region;
    private int bitrate;
    private int userLimit;

    public VoiceChannelImpl(long id, GuildImpl guild)
    {
        super(id, guild);
    }

    @NotNull
    @Override
    public ChannelType getType()
    {
        return ChannelType.VOICE;
    }
    
    @Override
    public int getBitrate()
    {
        return bitrate;
    }

    @Nullable
    @Override
    public String getRegionRaw()
    {
        return region;
    }

    @Override
    public int getUserLimit()
    {
        return userLimit;
    }

    @NotNull
    @Override
    public List<Member> getMembers()
    {
        return Collections.unmodifiableList(new ArrayList<>(connectedMembers.valueCollection()));
    }

    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createCopy(@NotNull Guild guild)
    {
        Checks.notNull(guild, "Guild");
        //TODO-v5: .setRegion here?
        ChannelAction<VoiceChannel> action = guild.createVoiceChannel(name).setBitrate(bitrate).setUserlimit(userLimit);
        if (guild.equals(getGuild()))
        {
            Category parent = getParentCategory();
            if (parent != null)
                action.setParent(parent);
            for (PermissionOverride o : overrides.valueCollection())
            {
                if (o.isMemberOverride())
                    action.addMemberPermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
                else
                    action.addRolePermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
            }
        }
        return action;
    }

    @NotNull
    @Override
    public VoiceChannelManager getManager()
    {
        return new VoiceChannelManagerImpl(this);
    }

    @Override
    public TLongObjectMap<Member> getConnectedMembersMap()
    {
        return connectedMembers;
    }


    @Override
    public VoiceChannelImpl setBitrate(int bitrate)
    {
        this.bitrate = bitrate;
        return this;
    }

    @Override
    public VoiceChannelImpl setRegion(String region)
    {
        this.region = region;
        return this;
    }

    public VoiceChannelImpl setUserLimit(int userLimit)
    {
        this.userLimit = userLimit;
        return this;
    }

    // -- Abstract Hooks --
    @Override
    protected void onPositionChange()
    {
        getGuild().getVoiceChannelsView().clearCachedLists();
    }
}
