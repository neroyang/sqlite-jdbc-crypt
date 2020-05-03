package org.sqlite.mc;


import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SQLiteMCConfig extends SQLiteConfig {

    private static final Pragma[] CIPHER_PRAGMA_ORDER = new Pragma[]{
        Pragma.CIPHER,
        Pragma.HMAC_CHECK,
        Pragma.LEGACY,
        Pragma.LEGACY_PAGE_SIZE,
        Pragma.KDF_ITER,
        Pragma.FAST_KDF_ITER,
        Pragma.HMAC_USE,
        Pragma.HMAC_PGNO,
        Pragma.HMAC_SALT_MASK,
        Pragma.KDF_ALGORITHM,
        Pragma.HMAC_ALGORITHM,
        Pragma.PLAINTEXT_HEADER_SIZE,
    };

    public SQLiteMCConfig() {
        super();
    }

    public SQLiteMCConfig(Properties prop) {
        super(prop);
    }

    private boolean useSQLInterface = false;

    protected boolean isValid(Integer value, int min, int max) {
        return (value >= min && value <= max);
    }

    protected SQLiteMCConfig setCipher(CipherAlgorithm cipherAlgorithm) {
        setPragma(SQLiteConfig.Pragma.CIPHER, cipherAlgorithm.getValue());
        return this;
    }


    public SQLiteMCConfig withKey(String key) {

        // Hex Key is a string like any key. It will be processed by SQLite. ex: String a = "x'aecc05ff'"
        // Raw Key is a string like any other key.It will be processed by SQLite. ex: String a = "raw'aecc05ff'"
        setPragma(Pragma.KEY, key);

        // For compatibility reason key as the password Pragma.
        // Here to be compatible keep the code writen in original Xenial JDBC
        setPragma(Pragma.PASSWORD, key);

        return this;
    }

    protected SQLiteMCConfig setLegacy(int value) {
        setPragma(SQLiteConfig.Pragma.LEGACY, String.valueOf(value));
        return this;
    }

    protected SQLiteMCConfig setLegacyPageSize(int value) {
        setPragma(Pragma.LEGACY_PAGE_SIZE, String.valueOf(value));
        return this;
    }

    protected SQLiteMCConfig setKdfIter(int value) {
        setPragma(SQLiteConfig.Pragma.KDF_ITER, String.valueOf(value));
        return this;
    }

    protected SQLiteMCConfig setFastKdfIter(int value) {
        setPragma(SQLiteConfig.Pragma.FAST_KDF_ITER, String.valueOf(value));
        return this;
    }

    protected SQLiteMCConfig setHmacUse(boolean value) {
        setPragma(SQLiteConfig.Pragma.HMAC_USE, String.valueOf(value ? 1 : 0));
        return this;
    }

    protected SQLiteMCConfig setHmacPgno(HmacPgno value) {
        setPragma(SQLiteConfig.Pragma.HMAC_PGNO, String.valueOf(value.ordinal()));
        return this;
    }

    protected SQLiteMCConfig setHmacSaltMask(int value) {
        setPragma(SQLiteConfig.Pragma.HMAC_SALT_MASK, String.valueOf(value));
        return this;
    }

    protected SQLiteMCConfig setKdfAlgorithm(KdfAlgorithm value) {
        setPragma(SQLiteConfig.Pragma.KDF_ALGORITHM, String.valueOf(value.ordinal()));
        return this;
    }

    protected SQLiteMCConfig setHmacAlgorithm(HmacAlgorithm value) {
        setPragma(SQLiteConfig.Pragma.HMAC_ALGORITHM, String.valueOf(value.ordinal()));
        return this;
    }

    protected SQLiteMCConfig setPlaintextHeaderSize(int value) {
        setPragma(SQLiteConfig.Pragma.PLAINTEXT_HEADER_SIZE, String.valueOf(value));
        return this;
    }

    public SQLiteMCConfig useSQLInterface(boolean sqlInterface) {
        this.useSQLInterface = sqlInterface;
        return this;
    }

    public void applyCipherParameters(Connection conn, Statement stat) throws SQLException {
        applyCipherParametersByNames(CIPHER_PRAGMA_ORDER, conn, stat, useSQLInterface);
    }

    protected void applyCipherParametersByNames(Pragma[] pragmas, Connection conn, Statement statement, boolean useSQLInterface) throws SQLException {
        Properties p = super.toProperties();

        String cipherProperty = p.getProperty(Pragma.CIPHER.getPragmaName(), null);
        if (cipherProperty == null)
            throw new SQLException("Cipher name could not be empty at this stage");

        for (Pragma pragma : pragmas) {
            String property = p.getProperty(pragma.getPragmaName(), null);

            if (property != null) {
                if (!useSQLInterface)
                    statement.execute(String.format("PRAGMA %s = %s", pragma.getPragmaName(), property));
                else {
                    if (pragma.equals(Pragma.CIPHER)) {
                        PreparedStatement preparedStatement = conn.prepareStatement("SELECT sqlite3mc_config(?, ?);");
                        preparedStatement.setString(1, pragma.name());
                        preparedStatement.setString(2, cipherProperty);
                        preparedStatement.execute();
                    } else {
                        PreparedStatement preparedStatement = conn.prepareStatement("SELECT sqlite3mc_config(?, ?, ?);");
                        preparedStatement.setString(1, cipherProperty);
                        preparedStatement.setString(2, pragma.name().toLowerCase());
                        preparedStatement.setString(3, property);
                        preparedStatement.execute();
                    }
                }
            }
        }
    }

    public enum HmacPgno {
        NATIVE,
        LITTLE_ENDIAN,
        BIG_ENDIAN
    }

    public enum KdfAlgorithm {
        SHA1,
        SHA256,
        SHA512
    }

    public enum HmacAlgorithm {
        SHA1,
        SHA256,
        SHA512
    }

    public enum CipherAlgorithm {
        SQL_CIPHER("sqlcipher"),
        RC4("rc4"),
        CHACHA20("chacha20"),
        WX_AES128("aes128cbc"),
        WX_AES256("aes256cbc");

        private final String cipherName;

        CipherAlgorithm(String name) {
            this.cipherName = name;
        }

        public String getValue() {
            return this.cipherName;
        }
    }


    public static class Builder extends SQLiteMCConfig {
        @Override
        public Builder setPlaintextHeaderSize(int value) {
            return (Builder) super.setPlaintextHeaderSize(value);
        }

        @Override
        public Builder setLegacy(int value) {
            return (Builder) super.setLegacy(value);
        }

        @Override
        public Builder setKdfIter(int value) {
            return (Builder) super.setKdfIter(value);
        }

        @Override
        public Builder setKdfAlgorithm(KdfAlgorithm value) {
            return (Builder) super.setKdfAlgorithm(value);
        }

        @Override
        public Builder setHmacUse(boolean value) {
            return (Builder) super.setHmacUse(value);
        }

        @Override
        public Builder setHmacSaltMask(int value) {
            return (Builder) super.setHmacSaltMask(value);
        }

        @Override
        public Builder setHmacPgno(HmacPgno value) {
            return (Builder) super.setHmacPgno(value);
        }

        @Override
        public Builder setHmacAlgorithm(HmacAlgorithm value) {
            return (Builder) super.setHmacAlgorithm(value);
        }

        @Override
        public Builder setFastKdfIter(int value) {
            return (Builder) super.setFastKdfIter(value);
        }

        @Override
        public Builder setLegacyPageSize(int value) {
            return (Builder) super.setLegacyPageSize(value);
        }

        @Override
        public Builder setCipher(CipherAlgorithm cipherAlgorithm) {
            return (Builder) super.setCipher(cipherAlgorithm);
        }

        public SQLiteMCConfig toSQLiteMCConfig() {
            return this;
        }
    }

}
