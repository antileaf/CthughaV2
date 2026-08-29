package com.cthugha.power;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.CustomTags;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class YangYanPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(YangYanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public YangYanPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "Dazzling Sunlight");
//        this.img = new Texture("cthughaResources/img/power/205_32.png");
        this.type = PowerType.BUFF;
    }

    public void updateDescription() {
        this.description = this.amount <= 1 ? powerStrings.DESCRIPTIONS[0] :
                String.format(powerStrings.DESCRIPTIONS[1], this.amount);
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer)
            return;

        boolean triggered = false;

        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (!c.isEthereal && c instanceof AbstractCthughaCard &&
                    ((AbstractCthughaCard)c).shunRan != -1) {
                c.retain = true;
                triggered = true;
            }

        if (triggered)
            this.flash();
    }
}
