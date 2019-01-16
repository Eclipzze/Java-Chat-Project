package server;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Utils {
	public static Logger createLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter() {
		    private static final String format = "[%1$tF %1$tT] [%2$s] <%4$s> %3$s %n";
		
		    @Override
		    public synchronized String format(LogRecord lr) {
		        return String.format(format,
		                new Date(lr.getMillis()),
		                lr.getLevel().getLocalizedName(),
		                "Thread-" + lr.getThreadID() + ": " + lr.getMessage(),
		                lr.getLoggerName()
		        );
		    }
		});
	
		logger.addHandler(handler);
		return logger;
		
	}
}
