package ru.fizteh.fivt.students.dnovikov.filemap;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataBaseConnector {
    private SingleTable table = null;

    public DataBaseConnector() throws Exception {

        try {
            Path dataBasePath = Paths.get(System.getProperty("user.dir")).resolve(System.getProperty("db.file"));
            table = new SingleTable(dataBasePath);
        } catch (NullPointerException ex) {
            throw new Exception("database file not set");
        }
    }

    public SingleTable getState() {
        return table;
    }

    public void loadTable() throws Exception {
        if (table != null) {
            table.load();
        }
    }

    public void saveTable() throws Exception {
        if (table != null) {
            table.save();
        }
    }

}
