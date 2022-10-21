public abstract class Play {
  public String name;

  public String type;

  public Play() {}

  public Play(String name) {
    this.name = name;
  }

  public abstract int getPrice(int audience);

  public abstract int getCredits(int audience);
}
