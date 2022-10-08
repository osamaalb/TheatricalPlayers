import java.text.NumberFormat;
import java.util.*;

public class Invoice {

  public String customer;
  public List<Performance> performances;

  public List<InvoiceItem> invoiceItems;

  public int totalInvoiceAmount;

  public int volumeCredits;

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
}
