package com.cthugha.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class KillMonsterAction extends AbstractGameAction {

    private AbstractCreature traget;
    private DamageInfo info;
    private AbstractGameAction action;

    public KillMonsterAction(AbstractCreature target, DamageInfo info, AbstractGameAction action) {
        this.target = target;
        this.info = info;
        this.action = action;
    }

    @Override
    public void update() {
        this.target.damage(this.info);
        if (this.target.isDying || this.target.currentHealth <= 0) {
            this.addToBot(action);
        }
        this.isDone = true;
    }

}
