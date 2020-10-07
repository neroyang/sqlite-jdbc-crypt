package org.sqlite;

/*
 * Copyright (c) 2020 INPUTsys Software Solutions GmbH
 *
 * Nutzungsbedingungen für Fremdfirmen auf Basis der vertraglichen Vereinbarungen. Eine anderweitige Nutzung oder eine
 * Weitergabe des Codes in Quell- oder Maschinenform ist untersagt. Die Haftung der INPUTsys Software Solutions GmbH
 * beschränkt sich auf eigene Entwicklungen. Eine Haftung für modifizierte Software kann nicht übernommen werden.
 */

import org.junit.Test;

import java.io.File;
import java.sql.*;

import static org.junit.Assert.*;

public class ParametersTest {

    @Test
    public void testSqliteConfigViaStatements() throws Throwable {
        File testDB = File.createTempFile("test.db", "", new File("target"));
        testDB.deleteOnExit();

        String uri = "jdbc:sqlite:file:" + testDB + "?cache=shared";
        try (Connection connection = DriverManager.getConnection(uri)) {
            try (Statement stat = connection.createStatement()) {
                stat.execute("SELECT sqlite3mc_config('cipher', 'sqlcipher');");
                stat.execute("SELECT sqlite3mc_config('sqlcipher', 'legacy', 4);");
                stat.execute("PRAGMA key='a';");
                stat.execute("select 1 from sqlite_master");

                stat.execute("PRAGMA busy_timeout = 1800000;");
                stat.execute("PRAGMA auto_vacuum = incremental;");
                stat.execute("PRAGMA journal_mode = truncate;");
                stat.execute("PRAGMA synchronous = full;");
                stat.execute("PRAGMA cache_size = -65536;");

                checkPragma(stat, "busy_timeout", "1800000");
                checkPragma(stat, "auto_vacuum", "2");
                checkPragma(stat, "journal_mode", "truncate");
                checkPragma(stat, "synchronous", "2");
                checkPragma(stat, "cache_size", "-65536");
                assertFalse(((SQLiteConnection) stat.getConnection()).getDatabase().getConfig().isEnabledSharedCache());
                assertTrue(((SQLiteConnection) stat.getConnection()).getDatabase().getConfig().isEnabledSharedCacheConnection());
            }
        }
    }

    @Test
    public void testSqliteConfigViaURI() throws Throwable {
        File testDB = File.createTempFile("test.db", "", new File("target"));
        testDB.deleteOnExit();

        String uri = "jdbc:sqlite:file:" + testDB + "?cache=private&busy_timeout=1800000&auto_vacuum=2&journal_mode=truncate&synchronous=full&cache_size=-65536";
        try (Connection connection = DriverManager.getConnection(uri)) {
            try (Statement stat = connection.createStatement()) {
                stat.execute("select 1 from sqlite_master");

                checkPragma(stat, "busy_timeout", "1800000");
                checkPragma(stat, "auto_vacuum", "2");
                checkPragma(stat, "journal_mode", "truncate");
                checkPragma(stat, "synchronous", "2");
                checkPragma(stat, "cache_size", "-65536");
                assertFalse(((SQLiteConnection) stat.getConnection()).getDatabase().getConfig().isEnabledSharedCache());
                assertFalse(((SQLiteConnection) stat.getConnection()).getDatabase().getConfig().isEnabledSharedCacheConnection());
            }
        }
    }

    private void checkPragma(Statement stat, String key, String expectedValue) throws SQLException {
        try (ResultSet resultSet = stat.executeQuery("pragma " + key + ";")) {
            resultSet.next();
            String value = resultSet.getString(1);
            assertEquals(expectedValue, value);
        }
    }

}
