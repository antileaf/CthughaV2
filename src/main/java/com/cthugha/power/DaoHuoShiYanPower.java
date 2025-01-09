package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DaoHuoShiYanPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(DaoHuoShiYanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DaoHuoShiYanPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        CthughaHelper.loadPowerRegion(this, "蹈火誓言");
//        this.img = new Texture("cthughaResources/img/power/213_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }
}
