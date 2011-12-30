package inc.util;



public abstract class HexDumpPattern {

  private static enum FieldTypeDescription {
    SIZE, STRING, SIZE_STRING, RANGE_REPLACE
  }

  public static enum FieldType {
    ADRESS(FieldTypeDescription.SIZE), //
    FIXED(FieldTypeDescription.STRING), //
    VALUE(FieldTypeDescription.SIZE), //
    FILL(FieldTypeDescription.SIZE_STRING), //
    ASCII(FieldTypeDescription.RANGE_REPLACE), //
    ;

    private FieldTypeDescription desc;

    private FieldType(FieldTypeDescription desc) {
      this.desc = desc;
    }

    public FieldTypeDescription getDesc() {
      return desc;
    }
  }

  public static class FieldDescription {
    private final FieldType type;

    private final int size;
    private final String string;
    private final int startRange;
    private final int endRange;
    private final char replaceChar;

    public FieldDescription(FieldType type, int size) {
      if (type.getDesc() != FieldTypeDescription.SIZE) {
        throw new IllegalArgumentException();
      }
      this.type = type;
      this.size = size;

      this.string = null;
      this.startRange = 0;
      this.endRange = 0;
      this.replaceChar = '\0';
    }

    public FieldDescription(FieldType type, String string) {
      if (type.getDesc() != FieldTypeDescription.STRING) {
        throw new IllegalArgumentException();
      }
      this.type = type;
      this.string = string;

      this.size = 0;
      this.startRange = 0;
      this.endRange = 0;
      this.replaceChar = '\0';
    }

    public FieldDescription(FieldType type, int size, String string) {
      if (type.getDesc() != FieldTypeDescription.SIZE_STRING) {
        throw new IllegalArgumentException();
      }
      this.type = type;
      this.size = size;
      this.string = string;

      this.startRange = 0;
      this.endRange = 0;
      this.replaceChar = '\0';
    }

    public FieldDescription(FieldType type, int startRange, int endRange,
        char replaceChar) {
      if (type.getDesc() != FieldTypeDescription.RANGE_REPLACE) {
        throw new IllegalArgumentException();
      }
      this.type = type;
      this.startRange = startRange;
      this.endRange = endRange;
      this.replaceChar = replaceChar;

      this.size = 0;
      this.string = null;
    }

    public FieldType getType() {
      return type;
    }

    public int getSize() {
      return size;
    }

    public String getString() {
      return string;
    }

    public int getStartRange() {
      return startRange;
    }

    public int getEndRange() {
      return endRange;
    }

    public char getReplaceChar() {
      return replaceChar;
    }


  }

  public static final HexDumpPattern DEFAULT = new HexDumpPattern() {

    @Override
    public FieldDescription[] getFields() {
      return new FieldDescription[] {adress(8), fixed("  "), values(8),
          fixed(" "), values(8), fill(61, " "), fixed("  |"),
          ascii(32, 126, '.')};
    }

  };



  public static FieldDescription adress(int size) {
    return new FieldDescription(FieldType.ADRESS, size);
  }

  public static FieldDescription fixed(String string) {
    return new FieldDescription(FieldType.FIXED, string);
  }

  public static FieldDescription values(int size) {
    return new FieldDescription(FieldType.VALUE, size);
  }

  public static FieldDescription fill(int size, String string) {
    return new FieldDescription(FieldType.FILL, size, string);
  }

  public static FieldDescription ascii(int startRange, int endRange,
      char replaceChar) {
    return new FieldDescription(FieldType.ASCII, startRange, endRange,
        replaceChar);
  }

  public abstract FieldDescription[] getFields();

}
