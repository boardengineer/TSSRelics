package tssrelics;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import tssrelics.relics.*;
import tssrelics.states.GreenSkullState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;

@SpireInitializer
public class TwitchSlaysSpireRelicsMod implements PostInitializeSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostDungeonInitializeSubscriber {
    private static final HashMap<AbstractRelic.RelicTier, Callable<ArrayList<String>>> TIER_TO_LIST_MAP = new HashMap<AbstractRelic.RelicTier, Callable<ArrayList<String>>>() {{
        put(AbstractRelic.RelicTier.COMMON, () -> AbstractDungeon.commonRelicPool);
        put(AbstractRelic.RelicTier.UNCOMMON, () -> AbstractDungeon.uncommonRelicPool);
        put(AbstractRelic.RelicTier.RARE, () -> AbstractDungeon.rareRelicPool);
        put(AbstractRelic.RelicTier.BOSS, () -> AbstractDungeon.bossRelicPool);
        put(AbstractRelic.RelicTier.SHOP, () -> AbstractDungeon.shopRelicPool);

        // This just prevents NPEs, we don't really remove special relics
        put(AbstractRelic.RelicTier.SPECIAL, () -> AbstractDungeon.shopRelicPool);
    }};

    public static SpireConfig modConfig = null;

    // These relics will be removed from the pool unless enabled
    private static ArrayList<AbstractRelic> experimentalRelics;

    public static void initialize() {
        BaseMod.subscribe(new TwitchSlaysSpireRelicsMod());
        Properties defaults = new Properties();

        defaults.put(TwitchSlaysSpireRelicsModPanel.EXPERIMENTAL_RELICS_KEY, true);

        try {
            modConfig = new SpireConfig("TwitchSlaysSpireRelicsMod", "config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivePostInitialize() {
        PrismaticBranch.initializeCardPools();

        if (BaseMod.hasModID("SaveStateMod:")) {
            GreenSkullState.addToStateFactory();
        }

        BaseMod.registerModBadge(ImageMaster
                .loadImage("Icon.png"), "TwitchSlaysSpire Relic Mod", "Board Engineer", "Plays the Battle for yourself", new TwitchSlaysSpireRelicsModPanel());
    }

    @Override
    public void receiveEditRelics() {
        experimentalRelics = new ArrayList<>();

        BaseMod.addRelic(new Sail().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new FreshWater().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoSkinBoots().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new ClericsGoldenHelm().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new PaperBomb().makeCopy(), RelicType.SHARED);

        BaseMod.addRelic(new AchillesHeelGuard().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new RunicReplicator().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoCharm().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new RelicCompass().makeCopy(), RelicType.SHARED);

        BaseMod.addRelic(new FiliformNeedle().makeCopy(), RelicType.PURPLE);
//        BaseMod.addRelic(new HotwiredCables().makeCopy(), RelicType.BLUE);
//        BaseMod.addRelic(new ScalesOfJustice().makeCopy(), RelicType.PURPLE);
        BaseMod.addRelic(new TacticalHarness().makeCopy(), RelicType.GREEN);
        BaseMod.addRelic(new VampireFang().makeCopy(), RelicType.RED);

        BaseMod.addRelic(new PreservedAmber().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new BusinessContract().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new DiceOfFate().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new Yin().makeCopy(), RelicType.PURPLE);
        BaseMod.addRelic(new HappyFlowerBed().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new FestivuePole().makeCopy(), RelicType.SHARED);

        BaseMod.addRelic(new BlightedSnail().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new JadeMysticKnot().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new VenomousScales().makeCopy(), RelicType.GREEN);

        BaseMod.addRelic(new HornOfPlenty().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new ComicBook().makeCopy(), RelicType.SHARED);

        addExperimentalRelic(new FreakyFork().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new ThiefGLoves().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new SolidStateDrive().makeCopy(), RelicType.BLUE);
        addExperimentalRelic(new CircularLogicWidget().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new PrismaticBranch().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new NoxiousStone().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new RustedLockbox().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new TungstenShield().makeCopy(), RelicType.SHARED);
        addExperimentalRelic(new RateLimiter().makeCopy(), RelicType.BLUE);
        addExperimentalRelic(new GreenSkull().makeCopy(), RelicType.GREEN);
        addExperimentalRelic(new YogaMat().makeCopy(), RelicType.PURPLE);
    }

    private static void addExperimentalRelic(AbstractRelic relic, RelicType relicType) {
        experimentalRelics.add(relic);
        BaseMod.addRelic(relic.makeCopy(), relicType);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/other.json");
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (!modConfig.getBool(TwitchSlaysSpireRelicsModPanel.EXPERIMENTAL_RELICS_KEY)) {
            experimentalRelics.forEach(relic -> {
                try {
                    System.err.println("removing " + relic.relicId);
                    TIER_TO_LIST_MAP.get(relic.tier).call().remove(relic.relicId);
                } catch (Exception e) {
                    System.err.println("problem with relic  " + relic.name);
                    e.printStackTrace();
                }
            });
        }
    }
}