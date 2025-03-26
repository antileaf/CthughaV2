package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Objects;

public class LieXingSuiXuanPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(LieXingSuiXuanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LieXingSuiXuanPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "列星随旋");
//        this.img = new Texture("cthughaResources/img/power/223_32.png");
    }

    public void updateDescription() {
        if (this.amount == 1 || powerStrings.DESCRIPTIONS.length == 1)
            this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
        else
            this.description = String.format(powerStrings.DESCRIPTIONS[1], this.amount);
    }

    public void onChannel(AbstractOrb orb) {
        int count = 0;
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            if (AbstractDungeon.player.orbs.get(i) instanceof FireVampire) {
                count++;
            }
        }

        if (count == 1) {
            this.addToBot(new DrawCardAction(this.amount));
        }

        if (count == AbstractDungeon.player.maxOrbs) {
            this.addToBot(new GainEnergyAction(1));
        }
    }
}
