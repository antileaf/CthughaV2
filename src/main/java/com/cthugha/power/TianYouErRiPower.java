package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TianYouErRiPower extends AbstractPower {
    public static final String POWER_ID = CthughaHelper.makeID(TianYouErRiPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TianYouErRiPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();

        CthughaHelper.loadPowerRegion(this, "天有二日");
//        this.img = new Texture("cthughaResources/img/power/222_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
//        if (card instanceof AbstractShunRanCard) {
//            card.tags.remove(CustomTags.Shun_Ran_Triggered);
//        }
        if (card instanceof AbstractCthughaCard) {
            ((AbstractCthughaCard) card).triggeredShunRanThisTurn = false;
            ((AbstractCthughaCard) card).updateBgImg();
        }
    }

}
