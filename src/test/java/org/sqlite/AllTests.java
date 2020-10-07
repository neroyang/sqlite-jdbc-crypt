package org.sqlite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.sqlite.util.OSInfoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BackupTest.class,
        ConnectionTest.class,
        DBMetaDataTest.class,
        ErrorMessageTest.class,
        ExtendedCommandTest.class,
        ExtensionTest.class,
        FetchSizeTest.class,
        InsertQueryTest.class,
        JDBCTest.class,
        OSInfoTest.class,
        PrepStmtTest.class,
        QueryTest.class,
        ReadUncommittedTest.class,
        RSMetaDataTest.class,
        SavepointTest.class,
        SQLiteConfigTest.class,
        SQLiteDataSourceTest.class,
        SQLiteConnectionPoolDataSourceTest.class,
        SQLiteJDBCLoaderTest.class,
        StatementTest.class,
        TransactionTest.class,
        UDFTest.class,
        JSON1Test.class,
        ProgressHandlerTest.class,
        BusyHandlerTest.class,
        ParametersTest.class,
        // SQLiteMC related Tests
        SQLiteMCPragmaTest.class,
        SQLiteMCSQLInterfaceTest.class,
        SQLiteMCURIInterfaceTest.class,
        ResultSetTest.class,
        TypeMapTest.class,
        ListenerTest.class,
})
public class AllTests {
// runs all Tests
}