public class TragedyPlay extends Play {
    public TragedyPlay(String name) {
        this.name = name;
        this.type = "tragedy";
    }

    public int getPrice(int audience) {
        return 40000 + ((audience > 30) ? 1000 * (audience - 30) : 0);
    }

    public int getCredits(int audience) {
        return Math.max(audience - 30, 0);
    }
}
