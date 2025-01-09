package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CounterOfLossMaxHpPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(CounterOfLossMaxHpPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CounterOfLossMaxHpPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "失去最大生命");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}
