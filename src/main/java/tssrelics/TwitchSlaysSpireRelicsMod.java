package tssrelics;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class TwitchSlaysSpireRelicsMod implements PostInitializeSubscriber, EditRelicsSubscriber {
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
    }
}