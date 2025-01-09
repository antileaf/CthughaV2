package com.cthugha.power;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HeiYanPower extends AbstractPower implements HealthBarRenderPower {
    public static final String POWER_ID = CthughaHelper.makeID(HeiYanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static int percentage = 10; // 10%
    public static final int BASE_PERCENTAGE = 10;

    public HeiYanPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "黑焰");
//        this.img = new Texture("cthughaResources/img/power/202_32.png");

        this.type = PowerType.DEBUFF;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(powerStrings.DESCRIPTIONS[0],
                this.amount,
                percentage,
                (int)((long) this.amount * percentage / 100));
    }

    @Override
    public void onInitialApplication() {
        this.addToBot(new DecreaseMonsterMaxHealthAction(owner, this.amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.addToBot(new DecreaseMonsterMaxHealthAction(owner, stackAmount));
    }

    @Override
    public void atStartOfTurn() {
        this.flashWithoutSound();

        int amountToLose = (int)((long) this.amount * percentage / 100);
        this.addToBot(new LoseHPAction(owner, owner, amountToLose));
        this.addToBot(new DecreaseMonsterMaxHealthAction(owner, amountToLose));
    }

    @Override
    public int getHealthBarAmount() {
        return (int)((long) this.amount * percentage / 100);
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }
}
