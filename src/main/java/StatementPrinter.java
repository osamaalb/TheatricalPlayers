import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
  public enum printFormat {
    TEXT,
    HTML
  }
  public String print(Invoice invoice, HashMap<String, Play> plays, printFormat format) {
    invoice.CalculateInvoice(plays);
    switch (format) {
      case TEXT:
        return(printText(invoice));
      case HTML:
        return(printHTML(invoice));
      default:
        throw new Error("unknown print format: ${format}");
    }
  }

  public String printText(Invoice invoice) {
    String result = String.format("Statement for %s\n", invoice.customer);
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    for (InvoiceItem invoiceItem : invoice.invoiceItems){
      result += String.format("  %s: %s (%s seats)\n", invoiceItem.name, frmt.format(invoiceItem.amount / 100), invoiceItem.audience);
    }
    result += String.format("Amount owed is %s\n", frmt.format(invoice.totalInvoiceAmount / 100));
    result += String.format("You earned %s credits\n", invoice.volumeCredits);
    return result;
  }

  public String printHTML(Invoice invoice) {
    String result = String.format("<h3>Statement for %s</h3>\n", invoice.customer);
    result += String.format("<ul>\n");
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    for (InvoiceItem invoiceItem : invoice.invoiceItems){
      result += String.format("<li>%s: %s (%s seats)</li>\n", invoiceItem.name, frmt.format(invoiceItem.amount / 100), invoiceItem.audience);
    }
    result += String.format("</ul>\n");
    result += String.format("<p>Amount owed is <strong>%s</strong></br>\n", frmt.format(invoice.totalInvoiceAmount / 100));
    result += String.format("<p>You earned <strong>%s</strong> credits</p>\n", invoice.volumeCredits);
    return result;
  }
}
