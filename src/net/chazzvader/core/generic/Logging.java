package net.chazzvader.core.generic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.chazzvader.core.generic.interfaces.IValidatorBasic;

/**
 * Class to handle all logging requirements
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public final class Logging {

	private Logging() {
	}

	/**
	 * Enum to represent different logging levels. DEBUG should be used for normally
	 * unnecessary information INFO should be used for updates on program status
	 * WARN should be for non-critical issues ERR should be for critical issues
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	@SuppressWarnings("javadoc")
	public enum LoggingLevel {
		DEBUG((IValidatorBasic) () -> Configuration.isDebugMode(), "DEBUG", false), INFO("INFO", false),
		WARN("WARN", true), ERR("ERR", true);

		/**
		 * Confirms the message is valid (not always printing DEBUG)
		 */
		public final IValidatorBasic validator;

		/**
		 * The name to printing with the message
		 */
		public final String name;

		/**
		 * Use System.err or System.out
		 */
		public final boolean err;

		private LoggingLevel(String name, boolean err) {
			this((IValidatorBasic) () -> true, name, err);
		}

		private LoggingLevel(IValidatorBasic validator, String name, boolean err) {
			this.validator = validator;
			this.name = name;
			this.err = err;
		}
	}

	/**
	 * Same as Logging.log(String message, String source, LoggingLevel level)
	 * 
	 * @param message The message to be printed
	 * @param source  The source of the message, typically the class, i.e. LWJGL,
	 *                Texture Manager, etc
	 * @param level   The logging level.
	 * @see Logging#log(String message, String source, LoggingLevel level)
	 */
	public static void print(String message, String source, LoggingLevel level) {
		log(message, source, level);
	}

	/**
	 * Same as Logging.log(String message, String source, LoggingLevel level)
	 * 
	 * @param message The message to be printed
	 * @param source  The source of the message, typically the class, i.e. LWJGL,
	 *                Texture Manager, etc
	 * @param level   The logging level.
	 * @see Logging#log(String message, String source, LoggingLevel level)
	 */
	public static void debug(String message, String source, LoggingLevel level) {
		log(message, source, level);
	}

	/**
	 * Prints out the source, message and level as [LEVEL:DATE/TIME]: (SOURCE)
	 * MESSAGE
	 * 
	 * @param message The message to be printed
	 * @param source  The source of the message, typically the class, i.e. LWJGL,
	 *                Texture Manager, etc
	 * @param level   The logging level.
	 * @see LoggingLevel
	 */
	public static void log(String message, String source, LoggingLevel level) {
		if (message == null) {
			log("Message given by" + source + "is null!", "Logger", LoggingLevel.WARN);
			log("That's not a very useful message is it?", "Logger", LoggingLevel.WARN);
			return;
		}
		if (source == null) {
			source = "Unknown";
			log("Source given is null!", "Logger", LoggingLevel.WARN);
		}
		if (level == null) {
			log("Level given by" + source + "is null!", "Logger", LoggingLevel.WARN);
			log("Print message anyway", "Logger", LoggingLevel.WARN);
			log(message, source, LoggingLevel.WARN);
		}
		if (level.validator.validate())
			_log(message, source, level);
	}

	/**
	 * Version of log. Does the actual printing. Parameters must already be checked
	 * and level validated!
	 * 
	 * @param message The message to be printed
	 * @param source  The source of the message, typically the class, i.e. LWJGL,
	 *                Texture Manager, etc
	 * @param level   The logging level.
	 * @see Logging#log(String, String, LoggingLevel)
	 */
	private static void _log(String message, String source, LoggingLevel level) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String out = "[" + level.name + ":" + dtf.format(now) + "]: (" + source + ") " + message;
		if (level.err) {
			System.err.println(out);
		} else {
			System.out.println(out);
		}
	}

}
