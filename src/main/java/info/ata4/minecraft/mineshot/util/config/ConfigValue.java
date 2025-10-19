package info.ata4.minecraft.mineshot.util.config;

import net.minecraftforge.common.config.Property;

public abstract class ConfigValue<T> {

    private T value;
    private final T valueDefault;

    public ConfigValue(T value) {
        this.value = value;
        this.valueDefault = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public T getDefault() {
        return valueDefault;
    }

    public void reset() {
        set(getDefault());
    }

    public abstract Property.Type getPropType();

    public abstract void importProp(Property prop);

    public abstract void exportProp(Property prop);
}
