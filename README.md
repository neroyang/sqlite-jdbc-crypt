NOTE
====
This is not the original version from xerial.org. It is a fork of the original.
This version include and use https://github.com/Willena/libsqlite3-crypt-auth as the native librairy. So the output jar is equivalent to xerial's one. This one includes some functionalities such as encryption and user authentication. Thanks to https://github.com/utelle/wxsqlite3 project that added these improvement, this library can now be updated in order to support these new functionalities.


SQLite JDBC Driver
==================
SQLite JDBC, developed by [Taro L. Saito](http://www.xerial.org/leo), is a library for accessing and creating [SQLite](http://sqlite.org) database files in Java.

Our SQLiteJDBC library requires no configuration since native libraries for major OSs, including Windows, Mac OS X, Linux etc., are assembled into a single JAR (Java Archive) file. The usage is quite simple; download the sqlite-jdbc library from the releases tab, then append the library (JAR file) to your class path.

See [the sample code](#usage).

What is different from Zentus' SQLite JDBC?
--------------------------------------------
The current sqlite-jdbc implementation is forked from [Zentus' SQLite JDBC driver](https://github.com/crawshaw/sqlitejdbc). We have improved it in two ways:

* Support major operating systems by embedding native libraries of SQLite, compiled for each of them.
* Remove manual configurations

In the original version, in order to use the native version of sqlite-jdbc, users had to set a path to the native codes (dll, jnilib, so files, etc.) through the command-line arguments,
e.g., `-Djava.library.path=(path to the dll, jnilib, etc.)`, or `-Dorg.sqlite.lib.path`, etc.
This process was error-prone and bothersome to tell every user to set these variables.
Our SQLiteJDBC library completely does away these inconveniences.

Another difference is that we are keeping this SQLiteJDBC library up-to-date to
the newest version of SQLite engine, because we are one of the hottest users of
this library. For example, SQLite JDBC is a core component of
[UTGB (University of Tokyo Genome Browser) Toolkit](http://utgenome.org/), which
is our utility to create personalized genome browsers.


Usage
============
SQLite JDBC is a library for accessing SQLite databases through the JDBC API. For the general usage of JDBC, see [JDBC Tutorial](http://docs.oracle.com/javase/tutorial/jdbc/index.html) or [Oracle JDBC Documentation](http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136101.html).

1.  Download sqlite-jdbc-(VERSION).jar from the download page
then append this jar file into your classpath.
2.  Open a SQLite database connection from your code. (see the example below)

* More usage examples are available at [Usage](Usage.md)
* Usage Example (Assuming `sqlite-jdbc-(VERSION).jar` is placed in the current directory)

```
> javac Sample.java
> java -classpath ".;sqlite-jdbc-(VERSION).jar" Sample   # in Windows
or
> java -classpath ".:sqlite-jdbc-(VERSION).jar" Sample   # in Mac or Linux
name = leo
id = 1
name = yui
id = 2
```    

**Sample.java**

```java
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

    public class Sample
    {
      public static void main(String[] args)
      {
        Connection connection = null;
        try
        {
          // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
          Statement statement = connection.createStatement();
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

          statement.executeUpdate("drop table if exists person");
          statement.executeUpdate("create table person (id integer, name string)");
          statement.executeUpdate("insert into person values(1, 'leo')");
          statement.executeUpdate("insert into person values(2, 'yui')");
          ResultSet rs = statement.executeQuery("select * from person");
          while(rs.next())
          {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
          }
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          System.err.println(e.getMessage());
        }
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e);
          }
        }
      }
    }
```    


How to Specify Database Files
-----------------------------

Here is an example to select a file `C:\work\mydatabase.db` (in Windows)

    Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/work/mydatabase.db");


A UNIX (Linux, Mac OS X, etc) file `/home/leo/work/mydatabase.db`

    Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/leo/work/mydatabase.db");



How to Use Memory Databases
---------------------------
SQLite supports on-memory database management, which does not create any database files.
To use a memory database in your Java code, get the database connection as follows:

    Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");


## Configuration

sqlite-jdbc extracts a native library for your OS to the directory specified by `java.io.tmpdir` JVM property. To use another directory, set `org.sqlite.tmpdir` JVM property to your favorite path.


Download
========
Download the latest version of SQLiteJDBC from the downloads page.


Build
=====

See [how to build](https://github.com/Willena/sqlite-jdbc/blob/master/README_BUILD.md). 

Supported Operating Systems
===========================
Since sqlite-jdbc-3.6.19, the natively compiled SQLite engines will be used for
the following operating systems:

*   Windows (Windows, x86 architecture, x86_64)
*   Mac OS X x86_64 (Support for SnowLeopard (i386) has been deprecated)
*   Linux x86, x86_64, arm (v5, v6, v7 and for android), ppc64

In the other OSs not listed above, the pure-java SQLite is used. (Applies to versions before 3.7.15)

If you want to use the native library for your OS, [build the source from scratch.


How does SQLiteJDBC work?
-------------------------
Our SQLite JDBC driver package (i.e., `sqlite-jdbc-(VERSION).jar`) contains three
types of native SQLite libraries (`sqlite-jdbc.dll`, `sqlite-jdbc.jnilib`, `sqlite-jdbc.so`),
each of them is compiled for Windows, Mac OS and Linux. An appropriate native library
file is automatically extracted into your OS's temporary folder, when your program
loads `org.sqlite.JDBC` driver.


License
-------
This program follows the Apache License version 2.0 (<http://www.apache.org/licenses/> ) That means:

It allows you to:

*   freely download and use this software, in whole or in part, for personal, company internal, or commercial purposes;
*   use this software in packages or distributions that you create.

It forbids you to:

*   redistribute any piece of our originated software without proper attribution;
*   use any marks owned by us in any way that might state or imply that we xerial.org endorse your distribution;
*   use any marks owned by us in any way that might state or imply that you created this software in question.

It requires you to:

*   include a copy of the license in any redistribution you may make that includes this software;
*   provide clear attribution to us, xerial.org for any distributions that include this software

It does not require you to:

*   include the source of this software itself, or of any modifications you may have
    made to it, in any redistribution you may assemble that includes it;
*   submit changes that you make to the software back to this software (though such feedback is encouraged).

See License FAQ <http://www.apache.org/foundation/licence-FAQ.html> for more details.

