package info.ata4.minecraft.mineshot.util.config;

import net.minecraftforge.common.config.Property;

public class ConfigBoolean extends ConfigValue<Boolean> {

    public ConfigBoolean(Boolean value) {
        super(value);
    }

    @Override
    public Property.Type getPropType() {
        return Property.Type.BOOLEAN;
    }

    @Override
    public void importProp(Property prop) {
        set(prop.getBoolean());
    }

    @Override
    public void exportProp(Property prop) {
        prop.set(get());
        prop.setDefaultValue(getDefault());
    }

}
