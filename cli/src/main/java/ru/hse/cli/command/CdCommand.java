package ru.hse.cli.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import ru.hse.cli.entities.Result;

public class CdCommand implements Command {

    @Override
    public Result invoke(List<String> args, InputStream in, OutputStream out) {
        if (args.size() == 1) {
            String homeDirectory = System.getProperty("user.home");
            System.setProperty("user.dir", homeDirectory);
            System.out.print(Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString());
            return new Result(Status.OK, 0, Optional.empty());
        } else {
            String targetDirectory = args.get(1);
            String currentDirectory = System.getProperty("user.dir");
            Path newDirectoryPath = Paths.get(currentDirectory, targetDirectory);

            if (Files.exists(newDirectoryPath) && Files.isDirectory(newDirectoryPath)) {
                System.setProperty("user.dir", newDirectoryPath.toString());
                System.out.print(Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString());
                return new Result(Status.OK, 0, Optional.empty());
            } else {
                return new Result(Status.ERROR, 1, Optional.of("No such file or directory: " + targetDirectory));
            }
        }
    }
}
