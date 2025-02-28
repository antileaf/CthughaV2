package com.cthugha.actions;

import com.cthugha.effect.SpiritAttachedEffect;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.object.GiveFire;
import com.cthugha.patches.SpiritField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Predicate;

public class RandomAttachSpiritAction extends AbstractGameAction {
    private final boolean hideVisual;

    private AbstractSpirit spirit;

    private Predicate<AbstractCard> predicate;

    private boolean fast;

    public RandomAttachSpiritAction(AbstractSpirit spirit, int amount, Predicate<AbstractCard> predicate,
            boolean hideVisual, boolean fast) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.spirit = spirit;
        this.predicate = predicate;
        this.duration = this.startDuration = fast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.hideVisual = hideVisual;
        this.fast = fast;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            ArrayList<AbstractCard> rngPool = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (this.predicate.test(card)) {
                    AbstractSpirit spirit = SpiritField.spirit.get(card);
                    if (spirit == null) {
                        rngPool.add(card);
                    } else {
                        if (spirit instanceof GiveFire) {
                            if (spirit.amount < 3) {
                                rngPool.add(card);
                            }
                        }
                    }
                }
            }
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (this.predicate.test(card)) {
                    AbstractSpirit spirit = SpiritField.spirit.get(card);
                    if (spirit == null) {
                        rngPool.add(card);
                    } else {
                        if (spirit instanceof GiveFire) {
                            if (spirit.amount < 3) {
                                rngPool.add(card);
                            }
                        }
                    }
                }
            }
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (this.predicate.test(card)) {
                    AbstractSpirit spirit = SpiritField.spirit.get(card);
                    if (spirit == null) {
                        rngPool.add(card);
                    } else {
                        if (spirit instanceof GiveFire) {
                            if (spirit.amount < 3) {
                                rngPool.add(card);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < this.amount && !rngPool.isEmpty(); i++) {
                AbstractCard card = rngPool.get(AbstractDungeon.cardRandomRng.random(rngPool.size() - 1));

                AbstractSpirit spirit = SpiritField.spirit.get(card);
                if (spirit != null) {
                    spirit.amount++;
                } else {
                    this.spirit.makeCopy().attachToCard(card);
                }

                if (!this.hideVisual) {
                    AbstractDungeon.effectList.add(new SpiritAttachedEffect(card));
                }
                rngPool.remove(card);
            }
        }

        tickDuration();
    }
}
