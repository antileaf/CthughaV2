package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LuXinRongJiePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.MakePath(LuXinRongJiePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LuXinRongJiePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        // this.loadRegion("deva2");
        this.img = new Texture("cthughaResources/img/power/212_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atStartOfTurn() {
        this.flash();

        if (this.amount > 0) {
            this.amount--;
            this.updateDescription();
        } else {
            this.amount = 0;
        }

        if (this.amount == 0) {
            this.addToBot(new ApplyPowerAction(owner, owner, new CanYangPower(owner)));
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    public void onCardDraw(AbstractCard card) {
        if (this.amount > 0) {
            if (ModHelper.IsBurn(card)) {
                this.flash();
                this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                this.addToBot(new DrawCardAction(1));
                this.amount--;
                this.updateDescription();
            }
        }

        if (this.amount == 0) {
            this.addToBot(new ApplyPowerAction(owner, owner, new CanYangPower(owner)));
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

}
