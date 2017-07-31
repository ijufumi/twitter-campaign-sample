package jp.ijufumi.sample.twitter.domain.value;

import org.seasar.doma.Domain;

import java.util.Optional;
import java.util.stream.Stream;

@Domain(valueType = int.class, factoryMethod = "of")
public enum PrizeStatusObject {
    WIN(1),
    LOSE(2);

    private final int value;
    PrizeStatusObject(int value) {
        this.value = value;
    }

    public static PrizeStatusObject of (int value) {
        return Stream.of(values()).filter(x -> x.getValue() == value).findFirst().orElseThrow(() -> new IllegalArgumentException());
    }

    public int getValue() {
        return value;
    }
}
