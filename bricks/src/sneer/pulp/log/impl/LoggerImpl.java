package sneer.pulp.log.impl;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import sneer.pulp.log.Logger;

public class LoggerImpl implements Logger {

	{
		org.apache.log4j.BasicConfigurator.configure();
	}
	
	private org.slf4j.Logger _logger = LoggerFactory.getLogger("sneer");
	
	public void debug(String arg0, Object arg1, Object arg2) {
		//Implement: _logger.debug(arg0, arg1, arg2);
	}

	public void debug(String arg0, Object arg1) {
		//Implement: _logger.debug(arg0, arg1);
	}

	public void debug(String arg0, Object[] arg1) {
		//Implement: _logger.debug(arg0, arg1);
	}

	public void debug(String arg0, Throwable arg1) {
		//Implement: _logger.debug(arg0, arg1);
	}

	public void debug(String arg0) {
		//Implement: _logger.debug(arg0);
	}

	public void error(String arg0, Object arg1, Object arg2) {
		_logger.error(arg0, arg1, arg2);
	}

	public void error(String arg0, Object arg1) {
		_logger.error(arg0, arg1);
	}

	public void error(String arg0, Object[] arg1) {
		_logger.error(arg0, arg1);
	}

	public void error(String arg0, Throwable arg1) {
		_logger.error(arg0, arg1);
	}

	public void error(String arg0) {
		_logger.error(arg0);
	}

	public String getName() {
		return _logger.getName();
	}

	public void info(String arg0, Object arg1, Object arg2) {
		//Implement: _logger.info(arg0, arg1, arg2);
	}

	public void info(String arg0, Object arg1) {
		//Implement: _logger.info(arg0, arg1);
	}

	public void info(String arg0, Object[] arg1) {
		//Implement: _logger.info(arg0, arg1);
	}

	public void info(String arg0, Throwable arg1) {
		//Implement: _logger.info(arg0, arg1);
	}

	public void info(String arg0) {
		//Implement: _logger.info(arg0);
	}

	public boolean isDebugEnabled() {
		return _logger.isDebugEnabled();
	}

	public boolean isDebugEnabled(Marker arg0) {
		return _logger.isDebugEnabled(arg0);
	}

	public boolean isErrorEnabled() {
		return _logger.isErrorEnabled();
	}

	public boolean isErrorEnabled(Marker arg0) {
		return _logger.isErrorEnabled(arg0);
	}

	public boolean isInfoEnabled() {
		return _logger.isInfoEnabled();
	}

	public boolean isInfoEnabled(Marker arg0) {
		return _logger.isInfoEnabled(arg0);
	}

	public boolean isTraceEnabled() {
		return _logger.isTraceEnabled();
	}

	public boolean isTraceEnabled(Marker arg0) {
		return _logger.isTraceEnabled(arg0);
	}

	public boolean isWarnEnabled() {
		return _logger.isWarnEnabled();
	}

	public boolean isWarnEnabled(Marker arg0) {
		return _logger.isWarnEnabled(arg0);
	}

	public void trace(String arg0, Object arg1, Object arg2) {
		//Implement: _logger.trace(arg0, arg1, arg2);
	}

	public void trace(String arg0, Object arg1) {
		//Implement: _logger.trace(arg0, arg1);
	}

	public void trace(String arg0, Object[] arg1) {
		//Implement: _logger.trace(arg0, arg1);
	}

	public void trace(String arg0, Throwable arg1) {
		//Implement: _logger.trace(arg0, arg1);
	}

	public void trace(String arg0) {
		//Implement: _logger.trace(arg0);
	}

	public void warn(String arg0, Object arg1, Object arg2) {
		_logger.warn(arg0, arg1, arg2);
	}

	public void warn(String arg0, Object arg1) {
		_logger.warn(arg0, arg1);
	}

	public void warn(String arg0, Object[] arg1) {
		_logger.warn(arg0, arg1);
	}

	public void warn(String arg0, Throwable arg1) {
		_logger.warn(arg0, arg1);
	}

	public void warn(String arg0) {
		_logger.warn(arg0);
	}
}
