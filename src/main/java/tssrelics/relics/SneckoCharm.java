package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.RestRoom;

public class SneckoCharm extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/blackhole.png");
    public static final String ID = "Snecko Charm";

    public SneckoCharm() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SneckoCharm();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @SpirePatch(clz = EventHelper.class, paramtypez = {Random.class}, method = "roll")
    public static class rollElitesPatch {
        @SpirePrefixPatch
        public static SpireReturn<EventHelper.RoomResult> rollBetter(Random eventRng) {
            if (AbstractDungeon.player.hasRelic(ID)) {
                return eventRng.random(1) == 0 ? SpireReturn
                        .Return(EventHelper.RoomResult.ELITE) : SpireReturn
                        .Continue();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, paramtypez = {EventHelper.RoomResult.class}, method = "generateRoom")
    public static class maybeMakeCampfiresPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractRoom> rollBetter(AbstractDungeon dungeon, EventHelper.RoomResult roomResult) {
            if (roomResult == EventHelper.RoomResult.ELITE && AbstractDungeon.player.hasRelic(ID)) {
                return AbstractDungeon.eventRng.random(1) == 0 ? SpireReturn
                        .Return(new MonsterRoomElite()) : SpireReturn.Return(new RestRoom());
            }
            return SpireReturn.Continue();
        }
    }
}
