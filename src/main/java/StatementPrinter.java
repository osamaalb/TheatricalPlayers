import java.util.*;

public final class StatementPrinter {
  public enum PrintFormat {
    TEXT,
    HTML
  }
  public String print(Invoice invoice, HashMap<String, Play> plays, PrintFormat format) {
    invoice.CalculateInvoice(plays);
    switch (format) {
      case TEXT:
        return invoice.printText();
      case HTML:
        return invoice.printHTML();
      default:
        throw new Error("unknown print format: ${format}");
    }
  }
}
