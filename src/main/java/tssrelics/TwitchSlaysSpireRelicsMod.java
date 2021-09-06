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

        BaseMod.addRelic(new Sail().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new FreshWater().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new SneckoSkinBoots().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new ClericsGoldenHelm().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new PaperBomb().makeCopy(), RelicType.SHARED);
        BaseMod.addRelic(new CircularLogicWidget().makeCopy(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/other.json");
    }
}