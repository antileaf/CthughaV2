package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WuYouBuZhuPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(WuYouBuZhuPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public WuYouBuZhuPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        // this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "烛幽");
//        this.img = new Texture("cthughaResources/img/power/221_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && (info.type == DamageInfo.DamageType.NORMAL)) {
            flash();
            this.addToBot(new DecreaseMonsterMaxHealthAction(target, damageAmount));
        }
    }

}
