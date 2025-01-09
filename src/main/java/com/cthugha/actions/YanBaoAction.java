package com.cthugha.actions;

import com.cthugha.cards.cthugha.FenJi;
import com.cthugha.enums.CustomTags;
import com.cthugha.utils.StaticHelper;
import com.cthugha.orbs.FireVampire;
import com.cthugha.power.FenJiPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

@Deprecated
public class YanBaoAction extends AbstractGameAction {

    private AbstractGameAction action;
    private AbstractCard card;

    public YanBaoAction(AbstractCard card, AbstractGameAction action) {
        this.action = action;
        this.card = card;
    }

    @Override
    public void update() {
        if (canYanBao()) {
            this.addToTop(this.action);
            card.tags.add(CustomTags.Yan_Bao_Triggered);
        } else {
            card.tags.remove(CustomTags.Yan_Bao_Triggered);
        }
        this.isDone = true;
    }

    // 是否能 炎爆
    private boolean canYanBao() {
        // 焚寂效果
        if (!(card instanceof FenJi)) {
            if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
                power.flash();
                return false;
            }
        }

        // 满足炎之精数量
        int matchNums = 0;
        int size = AbstractDungeon.player.orbs.size();
        for (int i = 0; i < size; i++) {
            AbstractOrb orb = AbstractDungeon.player.orbs.get(i);
            if (orb.ID == FireVampire.ORB_ID) {
                matchNums++;
            }
        }
        if (matchNums >= StaticHelper.canBaoYanNums) {
            return true;
        }
        return false;
    }
}
