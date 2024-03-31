package ru.hse.cli.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import ru.hse.cli.entities.Result;

public class LsCommand implements Command {

    @Override
    public Result invoke(List<String> args, InputStream in, OutputStream out) {
        try {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            String fileNames = Files.list(currentDirectory)
                                    .map(Path::getFileName)
                                    .map(Path::toString)
                                    .collect(Collectors.joining(System.lineSeparator()));

            out.write(fileNames.getBytes());
            return new Result(Status.OK, 0, null);
        } catch (IOException e) {
            return new Result(Status.ERROR, 1, Optional.of("Failed to list files in directory: " + e.getMessage()));
        }
    }
}
