package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BeiLuoShiMenXieDingPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(BeiLuoShiMenXieDingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BeiLuoShiMenXieDingPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "北落师门协定");
    }

    public void updateDescription() {
        this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
    }

    public void onAfterUseCard(AbstractCard c, UseCardAction action) {
        if (!(c instanceof AbstractCthughaCard))
            return;

        AbstractCthughaCard card = (AbstractCthughaCard) c;
        if (card.canBaoYan && !card.triggeredBaoYanLastTime) {
            this.flash();

            for (int i = 0; i < this.amount; i++)
                this.addToBot(new ChannelAction(new FireVampire()));

            this.addToBot(new GainEnergyAction(1));
        }
    }

}
