package com.cthugha.actions;

import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class EachYanZhiJingDamageAction extends AbstractGameAction {

    private int damage;
    private boolean needRemove;
    private AbstractPlayer p;
    private AbstractMonster m;

    public EachYanZhiJingDamageAction(AbstractPlayer p, AbstractMonster m, int damage, boolean needRemove) {
        this.damage = damage;
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
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }

        this.isDone = true;
    }

}
