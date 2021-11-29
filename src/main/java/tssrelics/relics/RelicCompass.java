package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import java.util.ArrayList;

public class RelicCompass extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/compass.png");
    public static final String ID = "Relic Compass";

    public RelicCompass() {
        super(ID, IMAGE, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RelicCompass();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @SpirePatch(clz = AbstractPlayer.class, paramtypez = {}, method = "preBattlePrep")
    public static class BuffElitePatch {
        @SpirePostfixPatch
        public static void buffElite(AbstractPlayer player) {
            if (AbstractDungeon.player
                    .hasRelic(ID) && AbstractDungeon.currMapNode.room instanceof MonsterRoomElite) {
                ArrayList<AbstractMonster> monsters =
                        AbstractDungeon.currMapNode.room.monsters.monsters;

                int buff = AbstractDungeon.mapRng.random(0, 3);

                for (AbstractMonster monster : monsters) {
                    switch (buff) {
                        case 0:
                            AbstractDungeon.actionManager
                                    .addToBottom(new ApplyPowerAction(monster, monster, new StrengthPower(monster, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
                            break;
                        case 1:
                            AbstractDungeon.actionManager
                                    .addToBottom(new IncreaseMaxHpAction(monster, 0.25F, true));
                            break;
                        case 2:
                            AbstractDungeon.actionManager
                                    .addToBottom(new ApplyPowerAction(monster, monster, new MetallicizePower(monster, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
                            break;
                        case 3:
                            AbstractDungeon.actionManager
                                    .addToBottom(new ApplyPowerAction(monster, monster, new RegenerateMonsterPower(monster, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
                            break;
                    }
                }
            }
        }
    }
}
