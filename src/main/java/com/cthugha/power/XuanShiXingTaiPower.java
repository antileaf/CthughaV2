package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class XuanShiXingTaiPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.MakePath(XuanShiXingTaiPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public XuanShiXingTaiPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = new Texture("cthughaResources/img/power/205_32.png");
        this.type = PowerType.DEBUFF;
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(this.owner, -this.amount), -this.amount));
    }

}
