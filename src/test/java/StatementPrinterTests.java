import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.approvaltests.Approvals.verify;

public class StatementPrinterTests {

    @Test
    void exampleStatement() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new TragedyPlay("Hamlet"));
        plays.put("as-like",  new ComedyPlay("As You Like It"));
        plays.put("othello",  new TragedyPlay("Othello"));

        Invoice invoice = new Invoice(new Customer("1", "BigCo", 103), List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        var result = invoice.print(plays, PrintFormat.TEXT);

        verify(result);
    }
    /*
    @Test
    void statementWithNewPlayTypes() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("henry-v",  new Play("Henry V", "history"));
        plays.put("as-like",  new Play("As You Like It", "pastoral"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("henry-v", 53),
                new Performance("as-like", 55)));

        Assertions.assertThrows(Error.class, () -> {
            invoice.print(plays, PrintFormat.TEXT);
        });
    }
    */
    @Test
    void testOtherCases() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new TragedyPlay("Hamlet"));
        plays.put("as-like",  new ComedyPlay("As You Like It"));
        plays.put("othello",  new TragedyPlay("Othello"));

        Invoice invoice = new Invoice(new Customer("1", "BigCo", 135), List.of(
                new Performance("hamlet", 30),
                new Performance("as-like", 20),
                new Performance("othello", 40)));

        var result = invoice.print(plays, PrintFormat.TEXT);

        verify(result);
    }

    @Test
    void exampleStatementHTML() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new TragedyPlay("Hamlet"));
        plays.put("as-like",  new ComedyPlay("As You Like It"));
        plays.put("othello",  new TragedyPlay("Othello"));

        Invoice invoice = new Invoice(new Customer("1", "BigCo", 136), List.of(
                new Performance("hamlet", 30),
                new Performance("as-like", 20),
                new Performance("othello", 40)));

        var result = invoice.print(plays, PrintFormat.HTML);

        verify(result);
    }

    @Test
    void exampleStatementHTMLNoDiscount() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new TragedyPlay("Hamlet"));
        plays.put("as-like",  new ComedyPlay("As You Like It"));
        plays.put("othello",  new TragedyPlay("Othello"));

        Invoice invoice = new Invoice(new Customer("1", "BigCo", 135), List.of(
                new Performance("hamlet", 30),
                new Performance("as-like", 20),
                new Performance("othello", 40)));

        var result = invoice.print(plays, PrintFormat.HTML);

        verify(result);
    }
}
