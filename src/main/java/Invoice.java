import java.text.NumberFormat;
import java.util.*;

public final class Invoice {

  public String customer;
  public List<Performance> performances;

  private float totalInvoiceAmount;

  private int volumeCredits;

  final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

  public Invoice(String customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
  }

  public String print(HashMap<String, Play> plays, PrintFormat format) {
    this.calculateInvoice(plays);
    switch (format) {
      case TEXT:
        return this.printText(plays);
      case HTML:
        return this.printHTML(plays);
      default:
        throw new Error("unknown print format: ${format}");
    }
  }
  private void calculateInvoice(HashMap<String, Play> plays) {
    final String tragedy = "tragedy";
    final String comedy = "comedy";
    this.volumeCredits = 0;
    this.totalInvoiceAmount = 0;
    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      volumeCredits += play.getCredits(perf.audience);
      totalInvoiceAmount += play.getPrice(perf.audience);
    }
  }
  private String printText(HashMap<String, Play> plays) {
    StringBuffer result = new StringBuffer(String.format("Statement for %s\n", this.customer));
    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result.append(String.format(
              "  %s: %s (%s seats)\n",
              play.name,
              frmt.format(play.getPrice(perf.audience)),
              perf.audience
      ));
    }
    result.append(String.format("Amount owed is %s\n", frmt.format(this.totalInvoiceAmount)));
    result.append(String.format("You earned %s credits\n", this.volumeCredits));
    return result.toString();
  }

  private String printHTML(HashMap<String, Play> plays) {
    StringBuffer result = new StringBuffer(String.format("<h3>Statement for %s</h3>\n", this.customer));
    result.append(String.format("<ul>\n"));
    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result.append(String.format(
              "<li>%s: %s (%s seats)</li>\n",
              play.name,
              frmt.format(play.getPrice(perf.audience)),
              perf.audience
      ));
    }
    result.append(String.format("</ul>\n"));
    result.append(String.format("<p>Amount owed is <strong>%s</strong></br>\n", frmt.format(this.totalInvoiceAmount)));
    result.append(String.format("<p>You earned <strong>%s</strong> credits</p>\n", this.volumeCredits));
    return result.toString();
  }
}
