package org.sqlite.mc;

public class SQLiteMCChacha20Config extends SQLiteMCConfig.Builder {

  public SQLiteMCChacha20Config(){
    super();
    setCipher(CipherAlgorithm.CHACHA20);
  }

  public SQLiteMCChacha20Config setLegacy(int value) {
    assert isValid(value, 0, 4);
    super.setLegacy(value);
    return this;
  }

  public SQLiteMCChacha20Config setLegacyPageSize(int value) {
    assert isValid(value, 0, 65536);
    super.setLegacyPageSize(value);
    return this;
  }

  public SQLiteMCChacha20Config setKdfIter(int value) {
    assert isValid(value, 1, Integer.MAX_VALUE);
    super.setKdfIter(value);
    return this;
  }

  public static SQLiteMCChacha20Config getDefault() {
    SQLiteMCChacha20Config config = new SQLiteMCChacha20Config();
    config.setKdfIter(64007);
    config.setLegacy(0);
    config.setLegacyPageSize(4096);
    return config;
  }

  public static SQLiteMCChacha20Config getSqlleetDefaults() {
    SQLiteMCChacha20Config config = new SQLiteMCChacha20Config();
    config.setKdfIter(12345);
    config.setLegacy(1);
    config.setLegacyPageSize(4096);
    return config;
  }

}
