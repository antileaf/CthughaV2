package com.cthugha.actions;

import com.cthugha.power.CounterOfLossMaxHpPower;
import com.cthugha.power.ShengMingFanHuanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;

// 减少怪物最大生命值
public class DecreaseMonsterMaxHealthAction extends AbstractGameAction {
    // 怪物
    private AbstractCreature m;
    // 最大生命值
    private int maxHealth;

    public DecreaseMonsterMaxHealthAction(AbstractCreature m, int maxHealth) {
        this.m = m;
        this.maxHealth = maxHealth;
    }

    @Override
    public void update() {
        if (m.isDying || m.isDead || this.maxHealth == 0) {
            this.isDone = true;
            return;
        }

        if (this.maxHealth == -1) {
            this.maxHealth = m.maxHealth - m.currentHealth;
        }

        // 生命返还效果
        if (m.hasPower(ShengMingFanHuanPower.POWER_ID)) {
            AbstractPower power = m.getPower(ShengMingFanHuanPower.POWER_ID);
            this.addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
        }

        m.maxHealth -= this.maxHealth;
        // 处理最大生命值为负的情况
        if (m.maxHealth < 0) {
            m.maxHealth = 0;
        }
        // 处理当前生命值大于最大生命值的情况
        if (m.currentHealth > m.maxHealth) {
            m.currentHealth = m.maxHealth;
        }

        m.damage(new DamageInfo(AbstractDungeon.player, 0, DamageType.HP_LOSS));
        m.healthBarUpdatedEvent();

        // 更新计数器
        this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new CounterOfLossMaxHpPower(m, this.maxHealth)));

        this.isDone = true;
    }

}
