package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class XinShangWuMingHuoPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(XinShangWuMingHuoPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public XinShangWuMingHuoPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "208");
//        this.img = new Texture("cthughaResources/img/power/208_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atStartOfTurnPostDraw() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.getIntentBaseDmg() < 0) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, this.amount), this.amount));
            }
        }
    }
}
