package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BlightedSnail extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/snail.png");
    public static final String ID = "Blighted Snail";

    public BlightedSnail() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        ++counter;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new DamageAllEnemiesAction(null, DamageInfo
                .createDamageMatrix(fib(this.counter), true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.stopPulse();
    }

    int fib(int n) {
        if (n == 1) {
            return 0;
        } else if (n == 2) {
            return 1;
        }

        int first = 0;
        int second = 1;

        for (int i = 3; i <= n; i++) {
            int temp = first + second;
            first = second;
            second = temp;
        }

        return second;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlightedSnail();
    }
}
