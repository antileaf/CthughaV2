package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ZhuZhaoShuJiPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makeID(ZhuZhaoShuJiPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ZhuZhaoShuJiPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = new Texture("cthughaResources/img/power/211_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters)
            if (!monster.isDeadOrEscaped())
                this.addToBot(new DecreaseMonsterMaxHealthAction(monster, this.amount));
    }
}
