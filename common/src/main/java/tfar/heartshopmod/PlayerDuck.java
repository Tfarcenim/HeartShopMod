package tfar.heartshopmod;

public interface PlayerDuck {

    int getHeartCurrency();
    void setHeartCurrency(int hearts);
    default void reset() {
        setHeartCurrency(0);
    }

    default void addHeartCurrency(int hearts) {
        setHeartCurrency(getHeartCurrency() + hearts);
    }

}
