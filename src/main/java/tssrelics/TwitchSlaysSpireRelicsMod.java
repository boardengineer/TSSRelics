package tssrelics;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.RelicStrings;
import tssrelics.relics.*;

@SpireInitializer
public class TwitchSlaysSpireRelicsMod implements PostInitializeSubscriber, EditRelicsSubscriber, EditStringsSubscriber {
    public static void initialize() {
        BaseMod.subscribe(new TwitchSlaysSpireRelicsMod());
    }

    @Override
    public void receivePostInitialize() {
        PrismaticBranch.initializeCardPools();
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new PrismaticBranch().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new Fridge().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new NoxiousStone().makeCopy(), RelicType.SHARED);

        BaseMod.addRelic(new Sail().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new FreshWater().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoSkinBoots().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new ClericsGoldenHelm().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new PaperBomb().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new CircularLogicWidget().makeCopy(), RelicType.SHARED);

        BaseMod.addRelic(new AchillesHeelGuard().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new RunicReplicator().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new SolidStateDrive().makeCopy(), RelicType.BLUE);
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

        BaseMod.addRelic(new ComicBook().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new BlightedSnail().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new JadeMysticKnot().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new VenomousScales().makeCopy(), RelicType.GREEN);
        BaseMod.addRelic(new HornOfPlenty().makeCopy(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/other.json");
    }
}