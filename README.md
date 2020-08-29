[![Build Status](https://travis-ci.org/Willena/sqlite-jdbc-crypt.svg?branch=master)](https://travis-ci.org/Willena/sqlite-jdbc-crypt)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.willena/sqlite-jdbc/badge.svg)](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/)

# SQLite JDBC Driver 

This is a library for accessing and creating [SQLite](http://sqlite.org) database files in Java.

This SQLiteJDBC library requires no configuration since native libraries for major OSs, including Windows, Mac OS X, Linux etc., are assembled into a single JAR (Java Archive) file (the native library is provided by [Utelle](http://github.com/utelle) as part of the [SQLite3MultipleCiphers](https://github.com/utelle/SQLite3MultipleCiphers) project.

The usage is quite simple;
Download the sqlite-jdbc library from [Maven Central](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/) or from [Github Release](https://github.com/Willena/sqlite-jdbc-crypt/releases/latest), then append the library (JAR file) to your class path or use Maven, Gradle.

## Table of content

- [SQLite JDBC Driver](#sqlite-jdbc-driver)
  * [Table of content](#table-of-content)
  * [Setup](#setup)
    + [Supported Operating Systems](#supported-operating-systems)
    + [Manual Download](#manual-download)
    + [Maven](#maven)
    + [Gradle](#gradle)
    + [Configuration](#configuration)
    + [Build from scratch](#build-from-scratch)
  * [Usage and examples](#usage-and-examples)
    + [Basic usage](#basic-usage)
      - [Simple example](#simple-example)
      - [Specify database file](#specify-database-file)
      - [Special database access](#special-database-access)
        * [Database files in classpaths or network](#database-files-in-classpaths-or-network)
        * [In-memory databases](#in-memory-databases)
      - [Using the restore feature](#using-the-restore-feature)
      - [Using the Blob datatype](#using-the-blob-datatype)
      - [Connection configuration](#connection-configuration)
    + [Get started with encryption](#get-started-with-encryption)
      - [Introduction](#introduction)
      - [Supported ciphers](#supported-ciphers)
        * [Introduction](#introduction-1)
        * [AES 128 Bit CBC - No HMAC (wxSQLite3)](#aes-128-bit-cbc---no-hmac--wxsqlite3-)
        * [AES 256 Bit CBC - No HMAC (wxSQLite3)](#aes-256-bit-cbc---no-hmac--wxsqlite3-)
        * [ChaCha20 - Poly1305 HMAC (sqleet)](#chacha20---poly1305-hmac--sqleet-)
        * [AES 256 Bit CBC - SHA1/SHA256/SHA512 HMAC (SQLCipher)](#aes-256-bit-cbc---sha1-sha256-sha512-hmac--sqlcipher-)
      - [Configuration methods](#configuration-methods)
        * [Configure using SQLiteMCConfig objects](#configure-using-sqlitemcconfig-objects)
        * [Configure using SQL specific SQL functions](#configure-using-sql-specific-sql-functions)
        * [Configure using URI](#configure-using-uri)
      - [Using an encryption key](#using-an-encryption-key)
        * [ASCII](#ascii)
        * [Hex](#hex)
      - [SQLite3 backup API and encryption](#sqlite3-backup-api-and-encryption)
      - [Encryption key manipulations](#encryption-key-manipulations)
        * [Encrypt a plain database](#encrypt-a-plain-database)
        * [Open an encrypted DB](#open-an-encrypted-db)
        * [Change the key used for a database](#change-the-key-used-for-a-database)
        * [Remove the key and go back to plain](#remove-the-key-and-go-back-to-plain)
  * [Licenses](#licenses)
    + [Utelle (sqlite3mc)](#utelle--sqlite3mc-)
    + [Willena](#willena)
    + [Xerial](#xerial)

## Setup

### Supported Operating Systems

The native Sqlite library is compiled and *automaticaly* tested for the following platforms and OSs:

| Operating System / Architecture  	| x86 	| x86_64 	| arm 	| armv6 	| armv7 	| arm64 	| ppc64 	|
|---------------------------------	|-----	|--------	|-----	|-------	|-------	|-------	|-------	|
| Windows          	                | ✅   	| ✅      	| ❌   	| ❌     	| ❌     	| ❌     	| ❌     	|
| Mac Os X                          | ❌   	| ✅      	| ❌   	| ❌     	| ❌     	| ❌     	| ❌     	|
| Linux Generic                     | ✅   	| ✅      	| ✅   	| ✅     	| ✅     	| ✅     	| ✅     	|
| Android                           | ✅   	| ✅      	| ✅   	| ✅     	| ✅     	| ✅     	| ✅     	|


If your os is not listed and you want to use the native library for your OS, build the source from scratch (see the build from scratch section).

### Manual Download

 1. Download the latest version of SQLiteJDBC from the [Maven Central](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/) or from [Github Release](https://github.com/Willena/sqlite-jdbc-crypt/releases/latest)
 2. Add the downloaded jar to your Java classpath

### Maven

If you are familiar with [Maven](http://maven.apache.org), add the following XML fragments into your pom.xml file. With those settings, your Maven will automatically download our SQLiteJDBC library into your local Maven repository, since our sqlite-jdbc libraries are synchronized with the [Maven's central repository](http://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/).
        
        <dependency>
          <groupId>io.github.willena</groupId>
          <artifactId>sqlite-jdbc</artifactId>
          <version> version_number </version>
        </dependency>

### Gradle

If you are familiar with [Gradle](https://gradle.org/), use the following line.
This will automatically download the SQLiteJDBC library into your project.
        
        implementation 'io.github.willena:sqlite-jdbc:version_number'

### Configuration

This library is very limited in global configuration settings.
You only need to know that it will extracts the native library for your OS to the directory specified by `java.io.tmpdir` JVM property. 

To use another directory, set `org.sqlite.tmpdir` JVM property to your favorite path.

### Build from scratch

        Comming soon !
        
## Usage and examples
### Basic usage

#### Simple example

To open an SQLite database connection from your code, here is an example.

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
            System.err.println(e.getMessage());
          }
        }
      }
    }
```    
#### Specify database file


Here is an example to establishing a connection to a database file `C:\work\mydatabase.db` (in Windows)

```java
Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/work/mydatabase.db");
```

Opening a UNIX (Linux, Mac OS X, etc.) file `/home/leo/work/mydatabase.db`
```java
Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/leo/work/mydatabase.db");
```
#### Special database access

##### Database files in classpaths or network

To load db files that can be found from the class loader (e.g., db 
files inside a jar file in the classpath), 
use `jdbc:sqlite::resource:` prefix. 

For example, here is an example to access an SQLite DB file, `sample.db` 
in a Java package `org.yourdomain`: 

```java
Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:org/yourdomain/sample.db"); 
```
In addition, external DB resources can be used as follows: 

```java
Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:http://www.xerial.org/svn/project/XerialJ/trunk/sqlite-jdbc/src/test/java/org/sqlite/sample.db"); 
```

To access db files inside some specific jar file (in local or remote), 
use the [JAR URL](http://java.sun.com/j2se/1.5.0/docs/api/java/net/JarURLConnection.html):

```java
Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:jar:http://www.xerial.org/svn/project/XerialJ/trunk/sqlite-jdbc/src/test/resources/testdb.jar!/sample.db"); 
```

DB files will be extracted to a temporary folder specified in `System.getProperty("java.io.tmpdir")`.

##### In-memory databases

SQLite supports on-memory database management, which does not create any database files. To use a memory database in your Java code, get the database connection as follows:

```java
Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
```

And also, you can create memory database as follows:

```java
Connection connection = DriverManager.getConnection("jdbc:sqlite:");
```

#### Using the restore feature

Take a backup of the whole database to `backup.db` file:


```java
// Create a memory database
Connection conn = DriverManager.getConnection("jdbc:sqlite:");
Statement stmt = conn.createStatement();
// Do some updates
stmt.executeUpdate("create table sample(id, name)");
stmt.executeUpdate("insert into sample values(1, \"leo\")");
stmt.executeUpdate("insert into sample values(2, \"yui\")");
// Dump the database contents to a file
stmt.executeUpdate("backup to backup.db");
Restore the database from a backup file:
// Create a memory database
Connection conn = DriverManager.getConnection("jdbc:sqlite:");
// Restore the database from a backup file
Statement stat = conn.createStatement();
stat.executeUpdate("restore from backup.db");
```

#### Using the Blob datatype

1. Create a table with a column of blob type: `create table T (id integer, data blob)`
2. Create a prepared statement with `?` symbol: `insert into T values(1, ?)`
3. Prepare a blob data in byte array (e.g., `byte[] data = ...`)
4. `preparedStatement.setBytes(1, data)`
5. `preparedStatement.execute()...`

#### Connection configuration

Using the SQLiteConfig you can configure a number of things. Here is an example.

```java
SQLiteConfig config = new SQLiteConfig();
// config.setReadOnly(true);   
config.setSharedCache(true);
config.recursiveTriggers(true);
// ... other configuration can be set via SQLiteConfig object
Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db", config.toProperties());
```

### Get started with encryption

The main goal of this library is to allow users to encrypt databases they are producing.
In this section we will walk through the main aspect to understand to make this library
work correctly with your requierements.

The content of this section is maintly extracted from the WxSQLite3 repository.

#### Introduction

This library is compiled with a modified SQLite native library that support multiple
cipher schemes. In order to be used the user must choose a cipher scheme manually. If
not using the default one (at the moment the default cipher is CHACHA20) is applied. 

Before applying a configuration, choose the encryption scheme you would like to use in the
supported cipher list. 

#### Supported ciphers

##### Introduction

The following ciphers are currently supported by wxSQLite3:

| Full Name                                             	| SQL/URI interface name 	|
|-------------------------------------------------------	|------------------------	|
| AES 128 Bit CBC - No HMAC (wxSQLite3)                 	| aes128cbc              	|
| AES 256 Bit CBC - No HMAC (wxSQLite3)                 	| aes256cbc              	|
| ChaCha20 - Poly1305 HMAC (sqleet)                     	| chacha20               	|
| AES 256 Bit CBC - SHA1/SHA256/SHA512 HMAC (SQLCipher) 	| sqlcipher              	|


Definition of abbreviations:

* AES = Advanced Encryption Standard (Rijndael algorithm)
* CBC = Cipher Block Chaining mode
* HMAC = Hash Message Authentication Code
* ChaCha20 = symmetric stream cipher developed by Daniel J. Bernstein
* Poly1305 = cryptographic message authentication code (MAC) developed by Daniel J. Bernstein
* SHA1 = Secure Hash Algorithm 1
* SHA256 = Secure Hash Algorithm 2 (256 bit hash)
* SHA512 = Secure Hash Algorithm 2 (512 bit hash)

Each of these algorithme can be used with default configuration or configured.
Configuration parameters are given bellow.  
##### AES 128 Bit CBC - No HMAC (wxSQLite3)

This cipher was added to **wxSQLite3** in 2007 as the first supported encryption scheme. It is a 128 bit AES encryption in CBC mode.

The encryption key is derived from the passphrase according to the algorithm described in the PDF specification (using the MD5 hash function and the RC4 algorithm).

The initial vector for the encryption of each database page is derived from the page number.

The cipher does not use a HMAC, and requires therefore no reserved bytes per database page.

The following table lists all parameters related to this cipher that can be set before activating database encryption.

| Parameter          	| Default 	| Min 	|  Max  	| Description                                                   	|
|--------------------	|:-------:	|:---:	|:-----:	|---------------------------------------------------------------	|
| `legacy`           	|    0    	|  0  	|   1   	| Boolean flag whether the legacy mode should be used           	|
| `legacy_page_size` 	|    0    	|  0  	| 65536 	| Page size to use in legacy mode, 0 = default SQLite page size 	|

**Note**: It is not recommended to use _legacy_ mode for encrypting new databases. It is supported for compatibility reasons only, so that databases that were encrypted in _legacy_ mode can be accessed.

##### AES 256 Bit CBC - No HMAC (wxSQLite3)

This cipher was added to **wxSQLite3** in 2010. It is a 256 bit AES encryption in CBC mode.

The encryption key is derived from the passphrase using an SHA256 hash function.

The initial vector for the encryption of each database page is derived from the page number.

The cipher does not use a Hash Message Authentication Code (HMAC), and requires therefore no reserved bytes per database page.

The following table lists all parameters related to this cipher that can be set before activating database encryption.

| Parameter          	| Default 	| Min 	|  Max  	| Description                                                   	|
|--------------------	|:-------:	|:---:	|:-----:	|---------------------------------------------------------------	|
| `kdf_iter`         	|   4001  	|  1  	|       	| Number of iterations for the key derivation function          	|
| `legacy`           	|    0    	|  0  	|   1   	| Boolean flag whether the legacy mode should be used           	|
| `legacy_page_size` 	|    0    	|  0  	| 65536 	| Page size to use in legacy mode, 0 = default SQLite page size 	|

**Note**: It is not recommended to use _legacy_ mode for encrypting new databases. It is supported for compatibility reasons only, so that databases that were encrypted in _legacy_ mode can be accessed.

##### ChaCha20 - Poly1305 HMAC (sqleet)

This cipher was introduced for SQLite database encryption by the project [sqleet](https://github.com/resilar/sqleet) in 2017.

The Internet Engineering Task Force (IETF) officially standardized the cipher algorithm **ChaCha20** and the message authentication code **Poly1305** in [RFC 7905](https://tools.ietf.org/html/rfc7905) for Transport Layer Security (TLS).

The new default **wxSQLite3** cipher is **ChaCha20 - Poly1305**.

The encryption key is derived from the passphrase using a random salt (stored in the first 16 bytes of the database file) and the standardized PBKDF2 algorithm with an SHA256 hash function.

One-time keys per database page are derived from the encryption key, the page number, and a 16 bytes nonce. Additionally, a 16 bytes **Poly1305** authentication tag per database page is calculated. Therefore this cipher requires 32 reserved bytes per database page.

The following table lists all parameters related to this cipher that can be set before activating database encryption.

| Parameter          	| Default 	| sqleet 	| Min 	|  Max  	| Description                                                   	|
|--------------------	|:-------:	|:------:	|:---:	|:-----:	|---------------------------------------------------------------	|
| `kdf_iter`         	|  64007  	|  12345 	|  1  	|       	| Number of iterations for the key derivation function          	|
| `legacy`           	|    0    	|    1   	|  0  	|   1   	| Boolean flag whether the legacy mode should be used           	|
| `legacy_page_size` 	|   4096  	|  4096  	|  0  	| 65536 	| Page size to use in legacy mode, 0 = default SQLite page size 	|

**Note**: It is not recommended to use _legacy_ mode for encrypting new databases. It is supported for compatibility reasons only, so that databases that were encrypted in _legacy_ mode can be accessed.

##### AES 256 Bit CBC - SHA1/SHA256/SHA512 HMAC (SQLCipher)

SQLCipher was developed by [Zetetic LLC](http://zetetic.net) and initially released in 2008. It is a 256 bit AES encryption in CBC mode.

The encryption key is derived from the passphrase using a random salt (stored in the first 16 bytes of the database file) and the standardized PBKDF2 algorithm with an SHA1, SHA256, or SHA512 hash function.

A random 16 bytes initial vector (nonce) for the encryption of each database page is used for the AES algorithm. Additionally, an authentication tag per database page is calculated. SQLCipher version 1 used no tag; SQLCipher version 2 to 3 used a 20 bytes **SHA1** tag; SQLCipher version 4 uses a 64 bytes **SHA512** tag, allowing to optionally choose a 32 bytes **SHA256** tag instead. Therefore this cipher requires 16, 48 or 80 reserved bytes per database page (since the number of reserved bytes is rounded to the next multiple of the AES block size of 16 bytes).

The following table lists all parameters related to this cipher that can be set before activating database encryption. The columns labelled **v4**, **v3**, **v2**, and **v1** show the parameter values used in legacy SQLCipher versions **3**, **2**, and **1** respectively. To access databases encrypted with the respective SQLCipher version the listed parameters have to be set explicitly.

| Parameter               | Default | v4     | v3    | v2    | v1     | Min   | Max   | Description |
| :---                    | :---:   | :---:  | :---: | :---: | :---:  | :---: | :---: | :--- |
| `kdf_iter`              | 256000  | 256000 | 64000 | 4000  | 4000   | 1     |       | Number of iterations for key derivation |
| `fast_kdf_iter`         | 2       | 2      | 2     | 2     | 2      | 1     |       | Number of iterations for HMAC key derivation |
| `hmac_use`              | 1       | 1      | 1     | 1     | 0      | 0     | 1     | Flag whether a HMAC should be used |
| `hmac_pgno`             | 1       | 1      | 1     | 1     | n/a    | 0     | 2     | Storage type for page number in HMAC:<br/>0 = native, 1 = little endian, 2 = big endian|
| `hmac_salt_mask`        | 0x3a    | 0x3a   | 0x3a  | 0x3a  | n/a    | 0     | 255   | Mask byte for HMAC salt |
| `legacy`                | 0       | 4      | 3     | 2     | 1      | 0     | 4     | SQLCipher version to be used in legacy mode |
| `legacy_page_size`      | 4096    | 4096   | 1024  | 1024  | 1024   | 0     | 65536 | Page size to use in legacy mode, 0 = default SQLite page size |
| `kdf_algorithm`         | 2       | 2      | 0     | 0     | 0      | 0     | 2     | Hash algoritm for key derivation function<br/>0 = SHA1, 1 = SHA256, 2 = SHA512 |
| `hmac_algorithm`        | 2       | 2      | 0     | 0     | 0      | 0     | 2     | Hash algoritm for HMAC calculation<br/>0 = SHA1, 1 = SHA256, 2 = SHA512 |
| `plaintext_header_size` | 0       | 0      | n/a   | n/a   | n/a    | 0     | 100   | Size of plaintext database header<br/>must be multiple of 16, i.e. 32 |

**Note**: It is not recommended to use _legacy_ mode for encrypting new databases. It is supported for compatibility reasons only, so that databases that were encrypted in _legacy_ mode can be accessed. However, the default _legacy_ mode for the various SQLCipher versions can be easily set using just the parameter `legacy` set to the requested version number. That is, all other parameters have to be specified only, if their requested value deviates from the default value.

**Note**: Version 4 of SQLCipher introduces a new parameter `plain_text_header_size` to overcome an issue with shared encrypted databases under **iOS**. If this parameter is set to a non-zero value (like 16 or 32), the corresponding number of bytes at the beginning of the database header are not encrypted allowing **iOS** to identify the file as a SQLite database file. The drawback of this approach is that the cipher salt used for the key derivation can't be stored in the database header any longer. Therefore it is necessary to retrieve the cipher salt on creating a new database, and to specify the salt on opening an existing database. The cipher salt can be retrieved with the function `wxsqlite3_codec_data` using parameter `cipher_salt`, and has to be supplied on opening a database via the database URI parameter `cipher_salt`.

#### Configuration methods

##### Configure using SQLiteMCConfig objects

Starting with version 3.32.0 the java implementation has a new configuration object called SQLiteMCConfig that can hold 
the cipher configuration. 
The interface allows for very simple and quick configuration of the choosen cipher algorithm. 

For each cipher supported a ready to use and a customizable object is present. If you want to go completely custom it
 is also possible. All parameters are available through setters.

Each conbinaison of paramters presented in previous section are implemented as getDefault (SQLiteMC default) or get<name>Defautls (ex: getV2Defaults() for SQLCipher))

The object names are :

````
SQLiteMCConfig; //Super class

SQLiteMCConfig.Builder; Use this to build a configuration object from scratch
    
SQLiteMCSqlCipherConfig; // Generate a configuration for SQLCipher
SQLiteMCChacha20Config; // Generate a configuration for Chacha20
SQLiteMCWxAES256Config; // Generate a configuration for legacy AES 256 WxSQLite3
SQLiteMCWxAES128Config; // Generate a configuration for legacy AES 128 WxSQLite3
SQLiteMCRC4Config; // Generate a configuration for System.Data.SQLite
````

To specify the key you just need to use the `withKey(String Key)` of any of the configuration object.
To create the connection it is now very simple:

```java

//Using the SQLiteMC default parameters
Connection connection = DriverManager.getConnection("jdbc:sqlite:file:file.db", new SQLiteMCConfig().withKey("Key").toProperties());
Connection connection = new SQLiteMCConfig().withKey("Key").createConnection("jdbc:sqlite:file:file.db");

//Using Chacha20
Connection connection = DriverManager.getConnection("jdbc:sqlite:file:file.db", SQLiteMCChacha20Config.getDefault().withKey("Key").toProperties());
Connection connection = SQLiteMCChacha20Config.getDefault().withKey("Key").createConnection("jdbc:sqlite:file:file.db");

```

##### Configure using SQL specific SQL functions

**wxSQLite3** additionally defines the `wxsqlite3_config()` SQL function which can be used to get or set encryption parameters by using SQL queries.

| SQL function | Description |
| :--- | :--- |
| `wxsqlite3_config(paramName TEXT)` | Get value of database encryption parameter `paramName` |
| `wxsqlite3_config(paramName TEXT, newValue)` | Set value of database encryption parameter `paramName` to `newValue` |
| `wxsqlite3_config(cipherName TEXT, paramName TEXT)` | Get value of cipher `cipherName` encryption parameter `paramName` |
| `wxsqlite3_config(cipherName TEXT, paramName TEXT, newValue)` | Set value of cipher `cipherName` encryption parameter `paramName` to `newValue` |
| `wxsqlite3_codec_data(paramName TEXT)` | Get value of parameter `paramName` |
| `wxsqlite3_codec_data(paramName TEXT, schemaName TEXT)` | Get value of parameter `paramName` from schema `schemaName` |

**Note:** See the [supported cipher](#encryption_config_cipher) the list of possible `cipherName`s.

**Note:** Calling the configuration functions, the `paramName` can have a prefix as decribed bellow.
wxsqlite3_config() gets or sets encryption parameters which are relevant for the entire database instance. paramName is the name of the parameter which should be get or set. To set a parameter, pass the new parameter value as newValue. To get the current parameter value, pass -1 as newValue.

Parameter names use the following prefixes:

| Prefix | Description|
| :--- | :--- |
| *no prefix* | Get or set the *transient* parameter value. Transient values are only used **once** for the next usage of the `key` . Afterwards, the *permanent* default values will be used again. |
| `default:` | Get or set the *permanent* default parameter value. Permanent values will be used during the entire lifetime of the `db` database instance, unless explicitly overridden by a transient value. The initial values for the permanent default values are the compile-time default values. |
| `min:` | Get the lower bound of the valid parameter value range. This is read-only. |
| `max:` | Get the upper bound of the valid parameter value range. This is read-only. |

The following parameter names are supported for paramName:

| Parameter name | Description | Possible values |
| :--- | :--- | :--- |
| `cipher` | The cipher to be used for encrypting the database. | `aes128cbc` <br/>`aes256cbc`<br/>`chacha20`<br/>`sqlcipher` |
| `hmac_check` | Boolean flag whether the HMAC should be validated on read operations for encryption schemes using HMACs | `0` <br/> `1` |

The return value always is the current parameter value on success, or -1 on failure.

Note: Checking the HMAC on read operations is active by default. With the parameter hmac_check the HMAC check can be disabled in case of trying to recover a corrupted database. It is not recommended to deactivate the HMAC check for regular database operation. Therefore the default can not be changed.

**Examples:**

```SQL
-- Get cipher used for the next key or rekey operation
SELECT sqlite3mc_config('cipher');
```

```SQL
-- Set cipher used by default for all key and rekey operations
SELECT sqlite3mc_config('default:cipher', 'sqlcipher');
```

```SQL
-- Get number of KDF iterations for the AES-256 cipher
SELECT sqlite3mc_config('aes256cbc', 'kdf_iter');
```

```SQL
-- Set number of KDF iterations for the AES-256 cipher to 54321
SELECT sqlite3mc_config('aes256cbc', 'kdf_iter', 54321);
```

```SQL
-- Activate SQLCipher version 1 encryption scheme for the subsequent key PRAGMA
SELECT sqlite3mc_config('cipher', 'sqlcipher');
SELECT sqlite3mc_config('sqlcipher', 'kdf_iter', 4000);
SELECT sqlite3mc_config('sqlcipher', 'fast_kdf_iter', 2);
SELECT sqlite3mc_config('sqlcipher', 'hmac_use', 0);
SELECT sqlite3mc_config('sqlcipher', 'legacy', 1);
SELECT sqlite3mc_config('sqlcipher', 'legacy_page_size', 1024);
PRAGMA key='<passphrase>';
```

```SQL
-- Get the random key salt as a hexadecimal encoded string (if database is encrypted and uses key salt)
SELECT sqlite3mc_codec_data('salt');
```

##### Configure using URI

SQLite3 allows to specify database file names as [SQLite Uniform Resource Identifiers](https://www.sqlite.org/uri.html) on opening or attaching databases. The advantage of using a URI file name is that query parameters on the URI can be used to control details of the newly created database connection. The **sqlite3mc** encryption extension now allows to configure the encryption cipher via URI query parameters.

| URI Parameter | Description |
| :--- | :--- |
| `cipher`=_cipher name_ | The `cipher` query parameter specifies which cipher should be used. It has to be the identifier name of one of the supported ciphers. |
| `key`=_passphrase_ | The `key` query parameter allows to specify the passphrase used to initialize the encryption extension for the database connection. If the query string does not contain a `cipher` parameter, the default cipher selected at compile time is used. |
| `hexkey`=_hex-passphrase_ | The `hexkey` query parameter allows to specify a hexadecimal encoded passphrase used to initialize the encryption extension for the database connection. If the query string does not contain a `cipher` parameter, the default cipher selected at compile time is used. |

**Note 1**: The URI query parameters `key` and `hexkey` are detected and applied by the SQLite3 library itself. If one of them is used and if it is not intended to use the default cipher, then the `cipher` query parameter and optionally further cipher configuration parameters have to be given in the URI query string as well. 

**Note 2**: The URI query parameters `key` and `hexkey` are only respected by SQLite3 on **opening** a database, but not on **attaching** a database. To specify the passphrase on attaching a database the keyword `KEY` of the SQL command `ATTACH` has to be used.

Depending on the cipher selected via the `cipher` parameter, additional query parameters can be used to configure the encryption extension. All parameters as described for each supported cipher (like `legacy`, `kdf_iter`, and so on) can be used to modify the cipher configuration. Default values are used for all cipher parameters which are not explicitly added to the URI query string. Misspelled parameters are silently ignored.

**Note 3**: The `cipher` query parameter is always required, if further query parameters should be used to configure the encryption extension. If this parameter is missing or specifies an unknown cipher, all other cipher configuration parameters are silently ignored, and the default cipher as selected at compile time will be used.

**Note 4**: On **opening** a database all cipher configuration parameters given in the URI query string are used to set the **default** cipher configuration of the database connection. On **attaching** a database the cipher configuration parameters given in the URI query string will be used for the attached database, but will not change the defaults of the database connection.

Example: URI query string to select the legacy SQLCipher Version 2 encryption scheme:  
```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000
```
#### Using an encryption key

##### ASCII

Passing the key to SQLite in order to decrypt the database is quite simple.
It can be done using either the SQL syntax or the URI syntax. 

Keep in mind that you always need to configure the cipher algorithm before applying the key !
If your key is an ASCII key you can provide it using 

```sqlite
PRAGMA key='mykey';
```

or 

```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000&key=mykey
```

##### Hex

Passing the hexadecimal version of the key to SQLite in order to decrypt the database is quite simple.
It is very handy in case of a binary key. It can be done using either the SQL syntax or the URI syntax. 

Keep in mind that you always need to configure the cipher algorithm before applying the key !
If your key is an Hexadecimal key you can provide it using 

```sqlite
PRAGMA key="x'myHexKey'";
```

or 

```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000&hexkey=myHexKey
```

#### SQLite3 backup API and encryption

When using the SQLite3 backup API to create a backup copy of a SQLite database, the most common case is that source and target database use the same encryption cipher, if any. However, the **sqlite3mc** multi-cipher encryption extension allows to assign different ciphers to the source and target database.

Problems can arise from the fact that different ciphers may require a different number of reserved bytes per database page. If the number of reserved bytes for the target database is greater than that for the source database, performing a backup via the SQLite3 backup API is unfortunately not possible. In such a case the backup will be aborted.

To allow as many cipher combinations as possible the **sqlite3mc** multi-cipher encryption extension implements fallback solutions for the most common case where the source database is not encrypted, but a cipher usually requiring a certain number of reserved bytes per database page was selected for the target database. In this case no reserved bytes will be used by the ciphers. The drawback is that the resulting encryption is less secure and that the resulting databases will not be compatible with the corresponding legacy ciphers.

Please find below a table describing with which encryption cipher combinations the backup API can be used.

| **Backup**&nbsp;&nbsp;**To** |  SQLite3 |  sqlite3mc |  sqlite3mc | sqlite3mc | SQLCipher v1 | SQLCipher v2+ |
| --- | :---: | :---: | :---: | :---: | :---: | :---: |
<br/>**From** | Plain<br/>&nbsp; | AES-128<br/>&nbsp; | AES-256<br/>&nbsp; | ChaCha20<br/>Poly1305 | AES-256<br/>&nbsp; | AES-256<br/>SHA1 |
SQLite3<br/>Plain<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/> AES-128<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/>AES-256<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/>ChaCha20<br/>Poly1305 |  :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: | :x: | :x:
SQLCipher v1<br/>AES-256<br/>&nbsp; | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :x: | :ok: | :x:
SQLCipher&nbsp;v2+<br/>AES-256<br/>SHA1 | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :x: | :ok:

Symbol | Description
:---: | :---
:ok:  | Works
:x: | Does **not** work
:exclamation: | Works only for non-legacy ciphers with reduced security
<sup>:small_red_triangle_down:</sup> | Keeps reserved bytes per database page

**Note**: It is strongly recommended to use the same encryption cipher for source **and** target database.
#### Encryption key manipulations

Several manipulation can be very usefull when encrypting a database.
For example you may want to change the password used, remove it, or encrypt a plain database.

Here is what you need to know. 

##### Encrypt a plain database

1. Open the database file
2. Set cipher configuration
3. Apply the key for the first time using the `PRAGMA` syntax
4. Use as usual

##### Open an encrypted DB

1. Open the database file
2. set cipher configuration
3. Apply the corresponding key using the `PRAGMA` syntax
4. Use normally

##### Change the key used for a database

1. Open the database file
2. Set cipher configuration
3. Apply the current key using the `PRAGMA key='mykey'` syntax (It needs to be adapted if using an hexadecimal key)
4. Change the key using the `PRAGMA rekey='my_new_key'` syntax (It needs to be adapted if using an hexadecimal key)
5. Use normally

##### Remove the key and go back to plain

1. Open the database file
2. Set cipher configuration
3. Apply the current key using the `PRAGMA key='mykey'` syntax (It needs to be adapted if using an hexadecimal key)
4. Change the key to `null` using`PRAGMA rekey=''`
5. Use normally

## Licenses

### Utelle (sqlite3mc)

This project includes parts of the sqlite3mc project witch is licenced under the following licence

Sqlite3mc is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License version 3 or later as published by the Free Software Foundation, with the wxWindows 3.1 exception.

### Willena

This project includes modification done by Guillaume VILLENA (Willena) that are under the following licence.

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

### Xerial

This project is based on xerial work and is frequently synchronized with their [repository](https://github.com/xerial/sqlite-jdbc).
Here is their Licence.

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
