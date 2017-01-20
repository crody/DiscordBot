/*
 * Copyright 2017 Alex Thomson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.discordbot.listeners;

import io.github.lxgaming.discordbot.DiscordBot;
import io.github.lxgaming.discordbot.util.AudioPlayerSendHandler;
import io.github.lxgaming.discordbot.util.ConsoleOutput;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordBotListener extends ListenerAdapter {
	
	@Override
	public void onReady(ReadyEvent event) {
		event.getJDA().getGuildById(DiscordBot.getInstance().getConfiguration().getClient().getGuildId()).getAudioManager().setSendingHandler(new AudioPlayerSendHandler());
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event == null || event.getMember() == null || event.getMessage() == null) {
			return;
		}
		
		if (!event.isFromType(ChannelType.TEXT) || event.getMessage().isEdited() || event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getMessage().getContent().startsWith(DiscordBot.getInstance().getConfiguration().getClient().getCommandPrefix()) || event.getMessage().getContent().startsWith("/")) {
			ConsoleOutput.debug("Processing Command");
			if (DiscordBot.getInstance().getCommand().execute(event.getTextChannel(), event.getMember(), event.getMessage())) {
				DiscordBot.getInstance().getMessageSender().addMessage(event.getMessage());
			}
		}
		return;
	}
}
