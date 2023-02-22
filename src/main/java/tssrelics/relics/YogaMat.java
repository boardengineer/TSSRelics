package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;

public class YogaMat extends CustomRelic {
    private static final Texture OPTION_IMAGE = ImageMaster
            .loadImage("images/ui/campfire/meditate.png");
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/livingBlood.png");
    public static final String ID = "Yoga Mat";

    public YogaMat() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new YogaMat();
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new MeditateOption(counter < 3));
    }

    @Override
    public void atTurnStart() {
        if (this.counter > 0) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, null, new MantraPower(AbstractDungeon.player, this.counter), this.counter));
        }
    }

    public static class MeditateOption extends AbstractCampfireOption {
        public MeditateOption(boolean active) {
            this.label = "Meditate";
            this.usable = active;
            this.description = "Serenity Now, Insanity later";
            this.img = OPTION_IMAGE;
        }

        public void useOption() {
            if (this.usable) {
                AbstractDungeon.effectList.add(new CampfireMeditateEffect());
            }
        }
    }

    public static class CampfireMeditateEffect extends AbstractGameEffect {
        private static final float DUR = 2.0F;
        private boolean hasTrained = false;
        private final Color screenColor;

        public CampfireMeditateEffect() {
            this.screenColor = AbstractDungeon.fadeColor.cpy();
            this.duration = 2.0F;
            this.screenColor.a = 0.0F;
            ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
        }

        public void update() {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
            if (this.duration < 1.0F && !this.hasTrained) {
                this.hasTrained = true;
                if (AbstractDungeon.player.hasRelic(YogaMat.ID)) {
                    AbstractDungeon.player.getRelic(YogaMat.ID).flash();
                    ++AbstractDungeon.player.getRelic(YogaMat.ID).counter;
                    CardCrawlGame.sound.play("ATTACK_HEAVY");
                    CardCrawlGame.screenShake
                            .shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, true);
                }

                AbstractDungeon.topLevelEffects
                        .add(new BorderFlashEffect(new Color(0.8F, 0.6F, 0.1F, 0.0F)));
            }

            if (this.duration < 0.0F) {
                this.isDone = true;
                ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }

        }

        private void updateBlackScreenColor() {
            if (this.duration > 1.5F) {
                this.screenColor.a = Interpolation.fade
                        .apply(1.0F, 0.0F, (this.duration - 1.5F) * 2.0F);
            } else if (this.duration < 1.0F) {
                this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
            } else {
                this.screenColor.a = 1.0F;
            }

        }

        public void render(SpriteBatch sb) {
            sb.setColor(this.screenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        }

        public void dispose() {
        }
    }
}
