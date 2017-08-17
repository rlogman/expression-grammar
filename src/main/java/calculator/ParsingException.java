package calculator;

public class ParsingException extends Exception {
  private static final long serialVersionUID = 684946826553504749L;

  public ParsingException(String msg, Exception e) {
    super(msg, e);
  }

  public ParsingException(String msg) {
    super(msg);
  }

}
