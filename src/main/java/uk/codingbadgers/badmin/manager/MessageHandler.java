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
		
		public static BaseComponent[] currentlyTempBanned(String reason, String expire) {
			BaseComponent[] components = new BaseComponent[4];
			components[0] = createComponent("You are currently tempbanned from this server", false, ChatColor.RED);
			components[1] = createComponent("\nThis will expire at " + expire, false, ChatColor.RED);
			components[2] = createComponent("\n\nReason:\n", true,ChatColor.RED);
			components[3] = createComponent(reason, false,ChatColor.RED);
			return components;
		}
	
		public static BaseComponent[] banned(String user, String reason) {
			BaseComponent[] components = new BaseComponent[3];
			components[0] = createComponent("You have been banned from this server by " + user.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
			components[1] = createComponent("\n\nReason:\n", true, ChatColor.RED);
			components[2] = createComponent(reason, false, ChatColor.RED);
			return components;
		}
	
		public static BaseComponent[] tempbanned(String user, String reason, String expire) {
			BaseComponent[] components = new BaseComponent[4];
			components[0] = createComponent("You have been tempbanned from this server by " + user.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
			components[1] = createComponent("\nThis will expire at " + expire, false, ChatColor.RED);
			components[2] = createComponent("\n\nReason:\n", true, ChatColor.RED);
			components[3] = createComponent(reason, false, ChatColor.RED);
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
		
		public static BaseComponent[] isbanned() {
			BaseComponent[] components = new BaseComponent[2];
			components[0] = createPrefix();
			components[1] = createComponent("/isbanned <player>", false, ChatColor.RED);
			return components;
		}
		
		public static BaseComponent[] unban() {
			BaseComponent[] components = new BaseComponent[2];
			components[0] = createPrefix();
			components[1] = createComponent("/unban <player>", false, ChatColor.RED);
			return components;
		}
		
		public static BaseComponent[] warn() {
			BaseComponent[] components = new BaseComponent[2];
			components[0] = createPrefix();
			components[1] = createComponent("/warn <player> <reason>", false, ChatColor.RED);
			return components;
		}
		
		public static BaseComponent[] tempban() {
			BaseComponent[] components = new BaseComponent[5];
			components[0] = createPrefix();
			components[1] = createComponent("/tempban <player> <time<timeformat>> <reason>", false, ChatColor.RED);
			components[2] = createComponent("timeformat - m = minute", false, ChatColor.RED);
			components[3] = createComponent("           - h = hour", false, ChatColor.RED);
			components[4] = createComponent("           - h = day", false, ChatColor.RED);
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

	public static BaseComponent[] unbanBroadcast(String banned, String banner) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(banned.toLowerCase(Locale.ENGLISH) + " has been unbanned from this server by " + banner.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] unbanSuccess(String banned) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You have unbanned " + banned.toLowerCase(Locale.ENGLISH), false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] warningBroadcast(String banned, String banner, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(banned.toLowerCase(Locale.ENGLISH) + " has been warned by " + banner.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] warningSuccess(String banned, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You have warned " + banned.toLowerCase(Locale.ENGLISH) + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] tempbanBroadcast(String banned, String banner, String expire, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(banned.toLowerCase(Locale.ENGLISH) + " has been tempbanned from this server by " + banner.toLowerCase(Locale.ENGLISH) + " until " + expire + " for " + reason, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] tempbanSuccess(String banned, String expire, String reason) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You have tempbanned " + banned.toLowerCase(Locale.ENGLISH) + " until " + expire + " for " + reason, false, ChatColor.RED);
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
		components[1] = createComponent("Multiple players for " + user + " found on the lookup, using player with name " + banned, false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] alreadyBanned(String user) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(user + " is already banned", false, ChatColor.RED);
		return components;
	}
	
	public static BaseComponent[] selfError(String type) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("You cannot " + type + " yourself!", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] mojangIDLookup(String user) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent("Searching for ban for " + user + "...", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] notBanned(String user) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(user + " is currently not banned", false, ChatColor.RED);
		return components;
	}

	public static BaseComponent[] isBanned(String user) {
		BaseComponent[] components = new BaseComponent[2];
		components[0] = createPrefix();
		components[1] = createComponent(user + " is currently banned", false, ChatColor.RED);
		return components;
	}
	
	private static BaseComponent createComponent(String message, boolean bold, ChatColor color) {
		return new ComponentBuilder(message).bold(bold).color(color).create()[0];
	}
	
	private static BaseComponent createPrefix() {
		return new ComponentBuilder("[bAdmin] ").bold(true).color(ChatColor.DARK_RED).create()[0];
	}
	
}
