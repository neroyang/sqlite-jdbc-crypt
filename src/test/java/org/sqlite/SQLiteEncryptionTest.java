package org.sqlite;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.*;

import static org.junit.Assert.assertEquals;

public class SQLiteEncryptionTest {

  String dbName;
  Connection dbCon;

  private static final String sql = "CREATE TABLE IF NOT EXISTS warehouses ("
      + "	id integer PRIMARY KEY,"
      + "	name text NOT NULL,"
      + "	capacity real"
      + ");";

  @Before
  public void setUp() throws Exception {
    File tmpFile = File.createTempFile("tmp-sqlite", ".db");
    tmpFile.deleteOnExit();
    dbName = tmpFile.getAbsolutePath();

    //System.out.println("The file is at " + dbName);

  }

  public void createTable() throws SQLException {
    try {
      Statement stmt = dbCon.createStatement();
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Connection connect() throws SQLException {
    dbCon = DriverManager.getConnection("jdbc:sqlite:" + dbName);
    return dbCon;
  }

  public boolean isReadable() {
    try {
      Statement st = dbCon.createStatement();
      ResultSet resultSet = st.executeQuery("SELECT COUNT(*) as nb FROM sqlite_master");
      resultSet.next();
      //System.out.println("The out is : " + resultSet.getString("nb"));
      assertEquals("1", resultSet.getString("nb"));
      return true;
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
      return false;
    }
  }

  @Test
  public void basicRead() throws SQLException {
    connect();
    createTable();
    assertEquals(true, isReadable());
    dbCon.close();
  }


  @Test
  public void cryptDataBase() throws SQLException {

    // First open and crypt the data base.
    connect();
    Statement stat = dbCon.createStatement();
    try {
      stat.execute("PRAGMA key='pass'");
    } finally {
      createTable();
      stat.close();
      dbCon.close();
    }

    connect();
    boolean v = isReadable();
    //System.out.println("The data is " + v + " readable ");
    assertEquals(false, v);
    dbCon.close();

  }

  @Test
  public void createCryptAndDecrypt() throws SQLException {


    cryptDataBase();

    connect();
    Statement stat = dbCon.createStatement();

    try {
      stat.execute("PRAGMA key='pass'");
    } finally {

      boolean v = isReadable();
      //System.out.println("The Crypt and Decrypt is " + v + " readable ");
      assertEquals(true, v);
      stat.close();
      dbCon.close();

    }

  }

  @Test
  public void checkFalsePassword() throws SQLException {

    cryptDataBase();

    connect();
    Statement stat = dbCon.createStatement();

    try {
      stat.execute("PRAGMA key='passa'");
    } finally {

      boolean v = isReadable();
      //System.out.println("The Check is " + v + " readable ");
      assertEquals(false, v);
      stat.close();
      dbCon.close();

    }

  }

  @Test
  public void changePasswordTest() throws SQLException {


    //Create a first encrypted db
    createCryptAndDecrypt();

    //Change the password
    connect();
    Statement stat = dbCon.createStatement();
    try {
      stat.execute("PRAGMA key='pass'");
      stat.execute("PRAGMA rekey='passa'");

    } finally {

      boolean v = isReadable();
      //System.out.println("The st1 open is " + v + " readable ");
      assertEquals(true, v);
      stat.close();
      dbCon.close();
    }


    //Check that the db is still not readable
    connect();
    boolean v1 = isReadable();
    //System.out.println("Should be not readable : " + v1);
    isReadable();
    dbCon.close();


    //Check that the DB is still openable with the new password.
    connect();
    Statement statr = dbCon.createStatement();
    try {
      statr.execute("PRAGMA key='passa'");
    } finally {
      boolean v = isReadable();
      //System.out.println("The st2 open is " + v + " readable ");
      assertEquals(true, v);
      statr.close();
      dbCon.close();
    }

  }

  @Test
  public void removePassword() throws SQLException {

    createCryptAndDecrypt();

    connect();
    Statement stat = dbCon.createStatement();
    try {
      stat.execute("PRAGMA key='pass'");
      stat.execute("PRAGMA rekey=''");

    } finally {
      stat.close();
      dbCon.close();
    }

    connect();
    boolean v = isReadable();
    //System.out.println("Without pass ? " + v);
    assertEquals(true, v);
    dbCon.close();

  }


}
