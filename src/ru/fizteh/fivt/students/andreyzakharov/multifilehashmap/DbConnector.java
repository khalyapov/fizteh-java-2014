package ru.fizteh.fivt.students.andreyzakharov.multifilehashmap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DbConnector implements AutoCloseable {
    Map<String, Command> commands = new HashMap<>();
    Path dbRoot;
    FileMap db;

    DbConnector(Path dbPath) throws ConnectionInterruptException {
        if (!Files.exists(dbPath)) {
            throw new ConnectionInterruptException("connection: destination does not exist");
        }
        if (!Files.isDirectory(dbPath)) {
            throw new ConnectionInterruptException("connection: destination is not a directory");
        }
        dbRoot = dbPath;

        commands.put("create", new CreateCommand());
        commands.put("drop", new DropCommand());
        commands.put("use", new UseCommand());
        commands.put("show", new ShowCommand());

        commands.put("put", new PutCommand());
        commands.put("get", new GetCommand());
        commands.put("list", new ListCommand());
        commands.put("remove", new RemoveCommand());

        commands.put("exit", new ExitCommand());
    }

    @Override
    public void close() {
        if (db != null) {
            try {
                db.unload();
            } catch (ConnectionInterruptException e) {
                // suppress the exception
            }
        }
    }

    public boolean tableExists(String name) {
        return Files.exists(dbRoot.resolve(name));
    }

    public String run(String argString) throws CommandInterruptException, ConnectionInterruptException {
        /*if (db == null && !(command instanceof CommandOverTable)) {
            throw new CommandInterruptException("no table");
        }*/
        String[] args = argString.trim().split("\\s+");
        Command command = commands.get(args[0]);
        if (command != null) {
            return command.execute(this, args);
        } else if (!args[0].equals("")) {
            throw new CommandInterruptException(args[0] + ": command not found");
        }
        return null;
    }
}
