package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BeiLuoShiMenXieDingPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.MakePath(BeiLuoShiMenXieDingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BeiLuoShiMenXieDingPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = new Texture("cthughaResources/img/power/203_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flash();
        if (card.hasTag(CustomTags.Yan_Bao) && !card.hasTag(CustomTags.Yan_Bao_Triggered)) {
            for (int i = 0; i < this.amount; i ++) {
                this.addToBot(new ChannelAction(new YanZhiJing()));
            }
            this.addToBot(new GainEnergyAction(1));
        }

    }

}