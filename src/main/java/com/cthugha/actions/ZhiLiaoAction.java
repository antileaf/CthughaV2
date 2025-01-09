package com.cthugha.actions;

import com.cthugha.utils.ZhiLiaoHelper;
import com.cthugha.power.HuoZhuoLianZiPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EntanglePower;

public class ZhiLiaoAction extends AbstractGameAction {
    private final AbstractCard card;
    private final Runnable followUp;
//    private AbstractGameAction action;

    public ZhiLiaoAction(AbstractCard card, Runnable followUp) {
        this.card = card;
        this.followUp = followUp;
    }

    public ZhiLiaoAction(AbstractCard card, AbstractGameAction action) {
        this(card, () -> AbstractDungeon.actionManager.addToTop(action));
    }

    public void update() {
        if (ZhiLiaoHelper.canTriggerZhiLiao(this.card)) {
            this.followUp.run();

            if (!AbstractDungeon.player.hasPower(HuoZhuoLianZiPower.POWER_ID)) {
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new EntanglePower(AbstractDungeon.player), 0));
            }
        }

        this.isDone = true;
    }
}
