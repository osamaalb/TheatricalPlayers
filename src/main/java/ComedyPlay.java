public final class ComedyPlay extends Play {
  public ComedyPlay(String name) {
    this.name = name;
  }

  public int getPrice(int audience) {
    return 30000 + 300 * audience + ((audience > 20) ? 10000 + 500 * (audience - 20) : 0);
  }

  public int getCredits(int audience) {
    return Math.max(audience - 30, 0) + (int)Math.floor(audience / 5);
  }
}