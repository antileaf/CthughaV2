package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnCreateCardInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DaoHuoShiYanPower extends AbstractPower implements OnCreateCardInterface {
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

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onCreateCard(AbstractCard card) {
        if (card instanceof Burn && !card.upgraded)
            card.upgrade();
    }
}
