package tssrelics.states;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import savestate.StateFactories;
import savestate.relics.RelicState;
import tssrelics.relics.GreenSkull;

public class GreenSkullState extends RelicState {
    private final boolean isActive;

    public GreenSkullState(AbstractRelic relic) {
        super(relic);

        this.isActive = ((GreenSkull) relic).isActive;
    }

    public GreenSkullState(String jsonString) {
        super(jsonString);

        JsonObject parsed = new JsonParser().parse(jsonString).getAsJsonObject();

        this.isActive = parsed.get("is_active").getAsBoolean();
    }

    public GreenSkullState(JsonObject relicJson) {
        super(relicJson);

        this.isActive = relicJson.get("is_active").getAsBoolean();
    }

    @Override
    public AbstractRelic loadRelic() {
        GreenSkull result = (GreenSkull) super.loadRelic();

        result.isActive = this.isActive;
        return result;
    }

    @Override
    public String encode() {
        JsonObject parsed = new JsonParser().parse(super.encode()).getAsJsonObject();

        parsed.addProperty("is_active", isActive);

        return parsed.toString();
    }

    @Override
    public JsonObject jsonEncode() {
        JsonObject result = super.jsonEncode();

        result.addProperty("is_active", isActive);

        return result;
    }

    // Cheat the class loader?
    public static void addToStateFactory() {
        StateFactories.relicByIdMap
                .put(GreenSkull.ID, new RelicState.RelicFactories(relic -> new GreenSkullState(relic), json -> new GreenSkullState(json), jsonObject -> new GreenSkullState(jsonObject)));
    }
}
