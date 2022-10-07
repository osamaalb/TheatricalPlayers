import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
  public enum printFormat {
    TEXT,
    HTML
  }
  public String print(Invoice invoice, HashMap<String, Play> plays, printFormat format) {
    int totalAmount = 0;
    int volumeCredits = 0;
    List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
    String result = String.format("Statement for %s\n", invoice.customer);

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int thisAmount = 0;

      switch (play.type) {
        case "tragedy":
          thisAmount = 40000;
          if (perf.audience > 30) {
            thisAmount += 1000 * (perf.audience - 30);
          }
          break;
        case "comedy":
          thisAmount = 30000;
          if (perf.audience > 20) {
            thisAmount += 10000 + 500 * (perf.audience - 20);
          }
          thisAmount += 300 * perf.audience;
          break;
        default:
          throw new Error("unknown type: ${play.type}");
      }

      invoiceItems.add(new InvoiceItem(play.name, thisAmount, perf.audience));
      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result += String.format("  %s: %s (%s seats)\n", play.name, frmt.format(thisAmount / 100), perf.audience);
      totalAmount += thisAmount;
    }
    result += String.format("Amount owed is %s\n", frmt.format(totalAmount / 100));
    result += String.format("You earned %s credits\n", volumeCredits);
    return result;
  }

  public String printText(Invoice invoice, HashMap<String, Play> plays, int totalAmount, int volumeCredits, List<InvoiceItem> invoiceItems) {
    String result = String.format("Statement for %s\n", invoice.customer);
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    for (InvoiceItem invoiceItem : invoiceItems){
      result += String.format("  %s: %s (%s seats)\n", invoiceItem.name, frmt.format(invoiceItem.amount / 100), invoiceItem.audience);
    }
    result += String.format("Amount owed is %s\n", frmt.format(totalAmount / 100));
    result += String.format("You earned %s credits\n", volumeCredits);
    return result;
  }
}
