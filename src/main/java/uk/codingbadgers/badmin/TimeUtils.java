package uk.codingbadgers.badmin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

public class TimeUtils {

	private static final Pattern TIME_REGEX = Pattern.compile("([0-9]+)([mhd])");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	private TimeUtils() {
	}
	
	public static long parseInput(String arg) {
		System.out.println(arg);
		Matcher matcher = TIME_REGEX.matcher(arg);
		
		if (!matcher.matches()) {
			return System.currentTimeMillis();
		}
		
		long time = parseLong(matcher.group(1));
		String timescale = matcher.group(2);
		
		long bantime = System.currentTimeMillis();
		
		switch (timescale) {
			case "m":
				bantime += time * 60 * 1000;
				break;
			case "h":
				bantime += time * 60 * 60 * 1000;
				break;
			case "d":
				bantime += time * 60 * 60 * 24 * 1000;
				break;
		}
		
		return bantime;
	}
	
	public static String parseDate(long time) {
		return DATE_FORMAT.format(new Date(time));
	}
}
