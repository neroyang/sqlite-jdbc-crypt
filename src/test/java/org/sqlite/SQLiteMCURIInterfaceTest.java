package org.sqlite;

import org.junit.Test;
import org.sqlite.mc.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import static org.junit.Assert.*;

public class SQLiteMCURIInterfaceTest {

    private static final String SQL_TABLE = "CREATE TABLE IF NOT EXISTS warehouses ("
        + "	id integer PRIMARY KEY,"
        + "	name text NOT NULL,"
        + "	capacity real"
        + ");";

    public String createFile() throws IOException {
        File tmpFile = File.createTempFile("tmp-sqlite", ".db");
        tmpFile.deleteOnExit();
        return tmpFile.getAbsolutePath();
    }


    public boolean databaseIsReadable(Connection connection) {
        if (connection == null)
            return false;
        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT count(*) as nb FROM sqlite_master");
            resultSet.next();
            //System.out.println("The out is : " + resultSet.getString("nb"));
            assertEquals("When reading the database, the result should contain the number 1", "1", resultSet.getString("nb"));
            return true;
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            return false;
        }
    }

    public void applySchema(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(SQL_TABLE);
    }

    public void plainDatabaseCreate(String dbPath) throws IOException, SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:file:" + dbPath);
        applySchema(conn);
        conn.close();
    }

    public void cipherDatabaseCreate(String dbPath, String key) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:file:" + dbPath + "?cipher=sqlcipher&legacy=1&kdf_iter=4000&key="+key);
        applySchema(connection);
        connection.close();
    }

    public Connection plainDatabaseOpen(String dbPath) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:file:" + dbPath);
    }

    @Test
    public void plainDatabaseTest() throws IOException, SQLException {
        String path = createFile();
        // 1.  Open + Write
        plainDatabaseCreate(path);

        // 2. Ensure another Connection can read the databse written
        Connection c = plainDatabaseOpen(path);
        assertTrue("The plain database should be always readable", databaseIsReadable(c));
        c.close();

    }


    public Connection cipherDatabaseOpen(String dbPath, String key) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:file:" + dbPath + "?cipher=sqlcipher&legacy=1&kdf_iter=4000&key="+key);
            return conn;
        } catch (SQLiteException e) {
            return null;
        }

    }

    public void genericDatabaseTest(SQLiteMCConfig config) throws IOException, SQLException {
        String path = createFile();
        // 1. Open + Write + cipher with "Key1" key
        String Key1 = "Key1";
        String Key2 = "Key2";

        cipherDatabaseCreate(path, Key1);

        //2. Ensure db is readable with good Password
        Connection c = cipherDatabaseOpen(path, Key1);
        assertTrue(
            String.format("1. Be sure the database with config %s can be read with the key '%s'", config.getClass().getSimpleName(), Key1)
            , databaseIsReadable(c));
        c.close();

        //3. Ensure db is not readable without the good password (Using Key2 as password)
        c = cipherDatabaseOpen(path, Key2);
        assertNull(
            String.format("2 Be sure the database with config %s cannot be read with the key '%s' (good key is %s)", config.getClass().getSimpleName(), Key2, Key1),
            c);

        //4. Rekey the database
        c = cipherDatabaseOpen(path, Key1);
        assertTrue(String.format("3. Be sure the database with config %s can be read before rekeying with the key '%s' (replacing %s with %s)", config.getClass().getSimpleName(), Key2, Key1, Key2)
            , databaseIsReadable(c));
        c.createStatement().execute(String.format("PRAGMA rekey=%s", Key2));
        assertTrue("4. Be sure the database is still readable after rekeying"
            , databaseIsReadable(c));
        c.close();

        //5. Should now be readable with Key2
        c = cipherDatabaseOpen(path, Key2);
        assertTrue(String.format("5. Should now be able to open the database with config %s and the new key '%s'", config.getClass().getSimpleName(), Key2)
            , databaseIsReadable(c));
        c.close();
    }


    @Test
    public void sqlCipherDatabaseHexKeyTest() throws SQLException, IOException {

        String dbfile = createFile();
        String Key1 = "x'54686973206973206D792076657279207365637265742070617373776F72642E'";
        String Key2 = "x'66086973206973206D792076657279207365637265742070617373776F72642E'";
        cipherDatabaseCreate(dbfile, Key1);

        //2. Ensure db is readable with good Password
        Connection c = cipherDatabaseOpen(dbfile, Key1);
        assertTrue(
            String.format("1. Be sure the database with config %s can be read with the key '%s'", "SQLCipher", Key1)
            , databaseIsReadable(c));
        c.close();

        //3. Ensure not readable with wrong key
        Connection c2 = cipherDatabaseOpen(dbfile, Key2);
        assertFalse(
            String.format("2. Be sure the database with config %s cannot be read with the key '%s'", "SQLCipher", Key2)
            , databaseIsReadable(c2));
        c.close();

    }


    @Test
    public void sqlCipherDatabaseTest() throws IOException, SQLException {
        genericDatabaseTest(SQLiteMCSqlCipherConfig.getDefault());
    }
}
