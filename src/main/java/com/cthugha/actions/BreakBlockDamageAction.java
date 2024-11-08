package com.cthugha.actions;

import java.util.function.Consumer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@Deprecated
public class BreakBlockDamageAction extends AbstractGameAction {
    private AbstractMonster m;
    private DamageInfo damageInfo;
    private Consumer<Integer> callback;

    public BreakBlockDamageAction(AbstractMonster m, DamageInfo info,  Consumer<Integer> callback) {
        this.m = m;
        this.damageInfo = info;
        this.callback = callback;
    }

    @Override
    public void update() {
        m.damage(damageInfo);
        
        if (m.lastDamageTaken > 0) { // 造成了破甲伤害
            this.callback.accept(m.lastDamageTaken);
        }

        this.isDone = true;
    }

}
