package com.cthugha.actions;

import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.WeakPower;

@Deprecated
public class EachYanZhiJingWeakAction extends AbstractGameAction {

    private int count;
    private boolean needRemove;
    private AbstractPlayer p;
    private AbstractMonster m;

    public EachYanZhiJingWeakAction(AbstractPlayer p, AbstractMonster m, int count, boolean needRemove) {
        this.count = count;
        this.needRemove = needRemove;
        this.p = p;
        this.m = m;
    }

    @Override
    public void update() {
        int size = AbstractDungeon.player.orbs.size();
        for (int i = 0; i < size; i++) {
            AbstractOrb orb = AbstractDungeon.player.orbs.get(i);
            if (orb.ID == YanZhiJing.ORB_ID) {
                if (needRemove) {
                    this.addToBot(new RemoveNextOrbAction());
                }
                this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, count, false)));
            }
        }

        this.isDone = true;
    }

}
