package ru.hse.cli.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import ru.hse.cli.entities.Result;

/**
 * Implements UNIX-like 'pwd' command
 */
public class PwdCommand implements Command {

	@Override
	public Result invoke(List<String> args, InputStream in, OutputStream out) {
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		try {
			out.write(currentPath.toAbsolutePath().toString().getBytes());
			return new Result(Status.OK, 0, Optional.empty());
		} catch (IOException e) {
			return new Result(Status.ERROR, 1, Optional.of(e.getMessage()));
		}
	}
}
