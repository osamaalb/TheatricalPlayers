import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public final class Invoice {

  public Customer customer;
  public List<Performance> performances;

  private float totalInvoiceAmount;

  private int volumeCredits;
  private float fidelityDiscount;

  final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

  public Invoice(Customer customer, List<Performance> performances) {
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
    this.volumeCredits = 0;
    this.totalInvoiceAmount = 0;
    this.fidelityDiscount = 0;

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      volumeCredits += play.getCredits(perf.audience);
      totalInvoiceAmount += play.getPrice(perf.audience);
    }

    this.customer.creditsBalance += volumeCredits;

    if (this.customer.creditsBalance >= 150) {
      fidelityDiscount = 15;
      totalInvoiceAmount -= fidelityDiscount;
      this.customer.creditsBalance -= 150;
    }
  }
  private String printText(HashMap<String, Play> plays) {
    StringBuffer result = new StringBuffer(String.format("Statement for %s\n", this.customer.name));
    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result.append(String.format(
              "  %s: %s (%s seats)\n",
              play.name,
              frmt.format(play.getPrice(perf.audience)),
              perf.audience
      ));
    }
    if (this.fidelityDiscount > 0) {
      result.append(String.format("You earned a fidelity discount of %s\n", frmt.format(this.fidelityDiscount)));
    }
    result.append(String.format("Amount owed is %s\n", frmt.format(this.totalInvoiceAmount)));
    result.append(String.format("You earned %s credits\n", this.volumeCredits));
    result.append(String.format("Your accumulated fidelity points are %s\n", this.customer.creditsBalance));
    return result.toString();
  }

  private String printHTML(HashMap<String, Play> plays) {
    String result = "";
    try {
      result = Files.readString(Paths.get(getClass().getResource("InvoiceTemplates\\HTMLTemplate.html").toURI()));
    } catch (Exception e) {
      throw new Error("Cannot read invoice template");
    }

    StringBuffer invoiceItems = new StringBuffer();
    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      invoiceItems.append(String.format(
              "<tr>\n"
              + "<td>%s</td>\n"
              + "<td>%s</td>\n"
              + "<td>%s</td>\n"
              + "</tr>\n",
              play.name,
              perf.audience,
              frmt.format(play.getPrice(perf.audience))
      ));
    }

    result = result.replace("{@Customer_Name}", this.customer.name);
    result = result.replace("{@Invoice_Items}", invoiceItems.toString());
    result = result.replace("{@Invoice_Amount}", frmt.format(this.totalInvoiceAmount));
    result = result.replace("{@Total_Credits}", Integer.toString(this.volumeCredits));
    if (this.fidelityDiscount > 0) {
      result = result.replace("{@Fidelity_Discount}", String.format("<tr>\n"
              + "<td colspan=\"2\" style=\"text-align: right;\"><strong>Fidelity Discount:</strong></td>\n"
              + "<td>%s</td>\n"
              + "</tr>", frmt.format(this.fidelityDiscount)));
    } else {
      result = result.replace("{@Fidelity_Discount}", "");
    }
    result = result.replace("{@Accumulated_Credits}", Integer.toString(this.customer.creditsBalance));
    return result;
  }
}
