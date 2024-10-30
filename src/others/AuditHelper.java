package others;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class AuditHelper {
    private static AuditHelper instance;
    private final FileWriter writer;

    private AuditHelper() throws IOException {
        this.writer = new FileWriter("src/others/istoric.csv", true);
    }

    public static synchronized AuditHelper getInstance() throws IOException {
        if (instance == null) {
            instance = new AuditHelper();
        }
        return instance;
    }

    public void addAction(String action) {
        try {
            long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            writer.write(action + ',' + timestamp + '\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
