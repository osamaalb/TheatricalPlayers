import java.text.NumberFormat;
import java.util.*;

public class Invoice {

  private String customer;
  private List<Performance> performances;

  private List<InvoiceItem> invoiceItems;

  private int totalInvoiceAmount;

  private int volumeCredits;

  public Invoice(String customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
  }

  public void CalculateInvoice(HashMap<String, Play> plays) {
    this.volumeCredits = 0;
    this.totalInvoiceAmount = 0;
    this.invoiceItems = new ArrayList<InvoiceItem>();
    for (Performance perf : this.performances) {
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

      totalInvoiceAmount += thisAmount;
    }
  }
  public String printText() {
    String result = String.format("Statement for %s\n", this.customer);
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    for (InvoiceItem invoiceItem : this.invoiceItems){
      result += String.format("  %s: %s (%s seats)\n", invoiceItem.name, frmt.format(invoiceItem.amount / 100), invoiceItem.audience);
    }
    result += String.format("Amount owed is %s\n", frmt.format(this.totalInvoiceAmount / 100));
    result += String.format("You earned %s credits\n", this.volumeCredits);
    return result;
  }

  public String printHTML() {
    String result = String.format("<h3>Statement for %s</h3>\n", this.customer);
    result += String.format("<ul>\n");
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    for (InvoiceItem invoiceItem : this.invoiceItems){
      result += String.format("<li>%s: %s (%s seats)</li>\n", invoiceItem.name, frmt.format(invoiceItem.amount / 100), invoiceItem.audience);
    }
    result += String.format("</ul>\n");
    result += String.format("<p>Amount owed is <strong>%s</strong></br>\n", frmt.format(this.totalInvoiceAmount / 100));
    result += String.format("<p>You earned <strong>%s</strong> credits</p>\n", this.volumeCredits);
    return result;
  }
}
