package uk.codingbadgers.badmin.manager;

import java.util.Locale;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class MessageHandler {

	public static class KickMessage {
		public static BaseComponent[] currentlyBanned(String reason) {
			BaseComponent[] components = new BaseComponent[3];
			components[0] = createComponent("You are currently banned from this server", false, ChatColor.RED);
			components[1] = createComponent("\n\nReason:\n", true,ChatColor.RED);
			components[2] = createComponent(reason, false,ChatColor.RED);
			return components;
		}
	
		public static BaseComponent[] banned(String user, String reason) {
			BaseComponent[] components = new BaseComponent[3];
			components[0] = createComponent("You have been banned from this server by " + user.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
			components[1] = createComponent("\n\nReason:\n", true, ChatColor.RED);
			components[2] = createComponent(reason, false, ChatColor.RED);
			return components;
		}
	
		public static BaseComponent[] kicked(String user, String reason) {
			BaseComponent[] components = new BaseComponent[3];
			components[0] = createComponent("You have been kicked from this server by " + user.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
			components[1] = createComponent("\n\nReason:\n", true, ChatColor.RED);
			components[2] = createComponent(reason, false, ChatColor.RED);
			return components;
		}
	}
	
	public static class CommandUsage {
		public static BaseComponent[] ban() {
			BaseComponent[] components = new BaseComponent[2];
			components[0] = createPrefix();
			components[1] = createComponent("/ban <player> <reason>", false, ChatColor.RED);
			return components;
		}
		
		public static BaseComponent[] kick() {
			BaseComponent[] components = new BaseComponent[2];
			components[0] = createPrefix();
			components[1] = createComponent("/kick <player> <reason>", false, ChatColor.RED);
			return components;
		}
	}

	public static BaseComponent[] bannedBroadcast(String banned, String banner, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(banned.toLowerCase(Locale.ENGLISH) + " has been banned from this server by " + banner.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] bannedSuccess(String banned, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You have banned " + banned.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] kickedBroadcast(String banned, String banner, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(banned.toLowerCase(Locale.ENGLISH) + " has been kicked from this server by " + banner.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] kickedSuccess(String banned, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You have kicked " + banned.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] noPermission() {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You do not have permission to do that!", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] noPlayer(String user) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("We cannot find a player by the name " + user.toLowerCase(Locale.ENGLISH) + " on the server", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] multiplePlayers(String user, String banned) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("Multiple players for " + user + " found on the lookup using player with name " + banned, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] banUsage() {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You do not have permission to do that!", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] banSelf() {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You cannot ban yourself!", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] kickSelf() {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You cannot kick yourself!", false, ChatColor.RED);
		return components;
	}
	
	private static BaseComponent createComponent(String message, boolean bold, ChatColor color) {
		return new ComponentBuilder(message).bold(bold).color(color).create()[0];
	}
	
	private static BaseComponent createPrefix() {
		return new ComponentBuilder("[bAdmin] ").bold(true).color(ChatColor.DARK_RED).create()[0];
	}
	
}
