package com.cthugha.power;

import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RevitalizingLifePower extends AbstractPower implements InvisiblePower {
    public static final String POWER_ID = CthughaHelper.makeID(RevitalizingLifePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RevitalizingLifePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
//        this.img = new Texture("cthughaResources/img/power/.png");
    }
}
