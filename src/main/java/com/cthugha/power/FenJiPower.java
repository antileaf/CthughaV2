package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FenJiPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(FenJiPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FenJiPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "焚寂");
//        this.img = new Texture("cthughaResources/img/power/220_32.png");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return CthughaHelper.isBurn(card) ? damage + this.amount : damage;
    }
}
