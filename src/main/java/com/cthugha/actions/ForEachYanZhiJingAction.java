package com.cthugha.actions;

import com.cthugha.orbs.FireVampire;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class ForEachYanZhiJingAction extends AbstractGameAction {
    private final Consumer<Integer> callback;
    private final boolean remove;

    public ForEachYanZhiJingAction(Consumer<Integer> callback, boolean remove) {
        this.callback = callback;
        this.remove = remove;
    }

    @Override
    public void update() {
        if (!this.isDone) {
            int count = (int) AbstractDungeon.player.orbs.stream()
                    .filter(orb -> orb instanceof FireVampire)
                    .count();

            this.callback.accept(count);

            if (this.remove) {
                for (int i = 0; i < count; i++)
                    this.addToTop(new RemoveNextOrbAction());
            }
        }

        this.isDone = true;
    }
}
