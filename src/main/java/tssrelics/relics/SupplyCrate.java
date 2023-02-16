package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplyCrate extends CustomRelic {
    private static final HashMap<AbstractPlayer.PlayerClass, String> PLAYER_CLASS_TO_STARTER_MAP = new HashMap<AbstractPlayer.PlayerClass, String>() {{
        put(AbstractPlayer.PlayerClass.IRONCLAD, BlackBlood.ID);
        put(AbstractPlayer.PlayerClass.THE_SILENT, RingOfTheSerpent.ID);
        put(AbstractPlayer.PlayerClass.DEFECT, FrozenCore.ID);
        put(AbstractPlayer.PlayerClass.WATCHER, HolyWater.ID);
    }};

    private static final Texture IMAGE = new Texture("img/supplycrate.png");
    public static final String ID = "Supply Crate";

    @Override
    public int getPrice() {
        return 350;
    }

    public SupplyCrate() {
        super(ID, IMAGE, RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        List<String> relicIds = PLAYER_CLASS_TO_STARTER_MAP.entrySet().stream()
                                                           .filter(entry -> entry
                                                                   .getKey() != AbstractDungeon.player.chosenClass)
                                                           .map(Map.Entry::getValue)
                                                           .collect(Collectors.toList());
        relicIds.add(PrismaticShard.ID);
        String id = relicIds.get(AbstractDungeon.relicRng.random(relicIds.size() - 1));

        if (id.equals(PrismaticShard.ID)) {
            AbstractDungeon.shopRelicPool.remove(PrismaticShard.ID);
        }

        AbstractRelic relic = RelicLibrary.getRelic(id).makeCopy();
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
