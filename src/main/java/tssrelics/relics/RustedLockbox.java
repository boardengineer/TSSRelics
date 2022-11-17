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

public class RustedLockbox extends CustomRelic {
    private static final HashMap<AbstractPlayer.PlayerClass, String> PLAYER_CLASS_TO_STARTER_MAP = new HashMap<AbstractPlayer.PlayerClass, String>() {{
        put(AbstractPlayer.PlayerClass.IRONCLAD, BurningBlood.ID);
        put(AbstractPlayer.PlayerClass.THE_SILENT, SnakeRing.ID);
        put(AbstractPlayer.PlayerClass.DEFECT, CrackedCore.ID);
        put(AbstractPlayer.PlayerClass.WATCHER, PureWater.ID);
    }};

    private static final Texture IMAGE = new Texture("img/lockbox.png");
    public static final String ID = "Rusted Lockbox";

    public RustedLockbox() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        List<String> relicIds = PLAYER_CLASS_TO_STARTER_MAP.entrySet().stream()
                                                           .filter(entry -> entry
                                                                   .getKey() != AbstractDungeon.player.chosenClass)
                                                           .map(Map.Entry::getValue)
                                                           .collect(Collectors.toList());
        String id = relicIds.get(AbstractDungeon.relicRng.random(relicIds.size() - 1));
        AbstractRelic relic = RelicLibrary.getRelic(id).makeCopy();
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
