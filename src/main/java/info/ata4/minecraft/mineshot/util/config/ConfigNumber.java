package info.ata4.minecraft.mineshot.util.config;

public abstract class ConfigNumber<T extends Number & Comparable<T>> extends ConfigValue<T> {

    private final T min;
    private final T max;

    public ConfigNumber(T value, T min, T max) {
        super(value);
        this.min = min;
        this.max = max;
    }

    public ConfigNumber(T value, T min) {
        this(value, min, null);
    }

    @Override
    public void set(T value) {
        if (min != null && min.compareTo(value) > 0) {
            super.set(min);
        } else if (max != null && max.compareTo(value) < 0) {
            super.set(max);
        } else {
            super.set(value);
        }
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}
