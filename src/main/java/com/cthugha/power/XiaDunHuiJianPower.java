package com.cthugha.power;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class XiaDunHuiJianPower extends AbstractPower {
    public static final String POWER_ID = "XiaDunHuiJian";

    public XiaDunHuiJianPower(AbstractCreature owner, int thornsDamage) {
        this.ID = POWER_ID;
        this.name = "下蹲挥剑";
        this.owner = owner;
        this.amount = thornsDamage;
        this.updateDescription();
        this.loadRegion("thorns");
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS && info.owner != null
                && info.owner != this.owner) {
            this.flash();
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, this.amount, DamageType.THORNS),
                    AttackEffect.SLASH_HORIZONTAL, true));
        }

        return damageAmount;
    }

    public void updateDescription() {
        this.description = "敌人每次攻击时受到123123123123" + this.amount + "点伤害";
    }

    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    // static {
    // powerStrings = CardCrawlGame.languagePack.getPowerStrings("Thorns");
    // NAME = powerStrings.NAME;
    // DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    // }
}
