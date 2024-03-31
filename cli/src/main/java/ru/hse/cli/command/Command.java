package ru.hse.cli.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ru.hse.cli.entities.Result;

/**
 * Provide CLI's command implementation
 */
public interface Command {

	/**
	 * Execute command with given args
	 *
	 * @param args list of command args
	 * @return result of command execution
	 */
	Result invoke(List<String> args, InputStream in, OutputStream out);

	static Command of(String name) {
		return switch (name) {
			case "cat" -> new CatCommand();
			case "echo" -> new EchoCommand();
			case "exit" -> new ExitCommand();
			case "pwd" -> new PwdCommand();
			case "wc" -> new WcCommand();
			case "grep" -> new GrepCommand();
			case "cd" -> new CdCommand();
			case "ls" -> new LsCommand();
			default -> new ExternalCommand();
		};
	}

}