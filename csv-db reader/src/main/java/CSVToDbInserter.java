import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import com.jcraft.jsch.*;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class CSVToDbInserter {

    private static void sftpFileReader()
    {
        {
            JSch jSch = new JSch();
            Session session;
            String username = "centos";
            String password = "centos";
            String host = "192.168.1.8";
            int port = 22;
            try
            {
                session = jSch.getSession(username,host,port);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(password);
                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                sftpChannel.cd("/home/centos");
                Vector<ChannelSftp.LsEntry> entries = sftpChannel.ls("*.*");
                for(ChannelSftp.LsEntry entry : entries)
                {
                    if(entry.getFilename().toLowerCase().endsWith(".csv"))
                    {
                        sftpChannel.get(entry.getFilename(), "E:/csv-db reader" );
                    }
                }
                sftpChannel.exit();
                session.disconnect();

            } catch (JSchException e) {
                e.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> readCSVFiles()
    {
        File myDirectory = new File("E:/csv-db reader");
        String[] containingFileNames = myDirectory.list();
        ArrayList arrayList = new ArrayList<String>();
        for (String fileName : containingFileNames) {
            if (fileName.endsWith(".csv"))
            {
                arrayList.add(fileName);
            }
        }
        return arrayList;
    }

    public static void main(String[] args){

        sftpFileReader();
        String jdbcURL = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "altran";

        ArrayList<String> listOfCsvFiles = readCSVFiles();

        for (String csvFile : listOfCsvFiles)
        {
            int batchSize = 20;
            Connection connection = null;
            ICsvBeanReader beanReader = null;
            CellProcessor[] processors = new CellProcessor[]{
                    new ParseTimestamp(),
                    new ParseDouble(),
                    new NotNull(),
                    new ParseDouble(),
                    new ParseDouble()
            };
            try {

                connection = DriverManager.getConnection(jdbcURL, username, password);
                connection.setAutoCommit(false);
                String sql = "INSERT INTO csv (result_time, granularity_period, object_name, cell_id, call_attempts) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                beanReader = new CsvBeanReader(new FileReader(csvFile),
                        CsvPreference.STANDARD_PREFERENCE);

                beanReader.getHeader(true);

                String[] header = {"resultTime", "granularityPeriod", "objectName", "cellID", "callAttempts"};

                CSVObject bean = null;

                int count = 0;

                while ((bean = beanReader.read(CSVObject.class, header, processors)) != null) {
                    Timestamp resultTime = bean.getResultTime();
                    double granularityPeriod = bean.getGranularityPeriod();
                    String objectName = bean.getObjectName();
                    double cellId = bean.getCellID();
                    double callAttempts = bean.getCallAttempts();

                    statement.setTimestamp(1, resultTime);
                    statement.setDouble(2, granularityPeriod);
                    statement.setString(3, objectName);
                    statement.setDouble(4, cellId);
                    statement.setDouble(5, callAttempts);

                    statement.addBatch();

                    if (count % batchSize == 0) {
                        statement.executeBatch();
                    }
                }

                beanReader.close();
                statement.executeBatch();
                connection.commit();
                connection.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.getNextException().printStackTrace();
                ex.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}