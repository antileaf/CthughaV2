package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CanYangPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makeID(CanYangPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CanYangPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        this.img = new Texture("cthughaResources/img/power/204_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    public void atStartOfTurnPostDraw() {
        this.flash();

        this.addToBot(new ChangeStanceAction("Divinity"));
        int toDraw = 10 - AbstractDungeon.player.hand.size();
        if (toDraw > 0) {
            this.addToTop(new DrawCardAction(AbstractDungeon.player, toDraw));
        }
    }

    public void onInitialApplication() {
        this.flash();

        this.addToBot(new ChangeStanceAction("Divinity"));
        int toDraw = 10 - AbstractDungeon.player.hand.size();
        if (toDraw > 0) {
            this.addToTop(new DrawCardAction(AbstractDungeon.player, toDraw));
        }
    }

}
