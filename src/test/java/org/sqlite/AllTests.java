package org.sqlite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.sqlite.util.OSInfoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BackupTest.class,
    BusyHandlerTest.class,
    ConnectionTest.class,
    DBMetaDataTest.class,
    ErrorMessageTest.class,
    ExtendedCommandTest.class,
    ExtensionTest.class,
    FetchSizeTest.class,
    InsertQueryTest.class,
    JDBCTest.class,
    JSON1Test.class,
    ListenerTest.class,
    PrepStmtTest.class,
    ProgressHandlerTest.class,
    QueryTest.class,
    ReadUncommittedTest.class,
    ResultSetTest.class,
    RSMetaDataTest.class,
    SavepointTest.class,
    SQLiteConfigTest.class,
    SQLiteConnectionPoolDataSourceTest.class,
    SQLiteDataSourceTest.class,
    SQLiteJDBCLoaderTest.class,
    StatementTest.class,
    TransactionTest.class,
    TypeMapTest.class,
    UDFTest.class,
    OSInfoTest.class,

    // SQLiteMC related Tests
    SQLiteMCPragmaTest.class,
    SQLiteMCSQLInterfaceTest.class,
    SQLiteMCURIInterfaceTest.class,

})
public class AllTests {
// runs all Tests
}