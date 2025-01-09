package com.cthugha.power;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SiHuiFuRanPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(SiHuiFuRanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SiHuiFuRanPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        // this.loadRegion("envenom");
        CthughaHelper.loadPowerRegion(this, "死灰复燃");
//        this.img = new Texture("cthughaResources/img/power/207_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void onExhaust(AbstractCard card) {
        if (CthughaHelper.isBurn(card)) {
            this.flash();
            this.addToBot(new MakeTempCardInDiscardAction(new Burn(), this.amount));

            AbstractGameAction lastAction = AbstractDungeon.actionManager.actions
                    .get(AbstractDungeon.actionManager.actions.size() - 1);
            if (lastAction instanceof MakeTempCardInDiscardAction)
                ReflectionHacks.setPrivate(lastAction, MakeTempCardInDiscardAction.class,
                        "duration", 0.1F);
        }
    }
}
