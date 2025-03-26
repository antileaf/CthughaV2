package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BaoRan extends AbstractCthughaCard {
    public static final String ID = CthughaHelper.makeID(BaoRan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/111.png";

    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BaoRan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 1;
    }

    @Override
    public void updateBgImg() {
        super.updateBgImg();

        String backup = this.textureImg;

        if (this.triggeredShunRanThisTurn)
            this.textureImg = "cthughaResources/img/card/111_bw.png";
        else
            this.textureImg = "cthughaResources/img/card/111.png";

        if (!this.textureImg.equals(backup))
            this.loadCardImage(this.textureImg);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.shining())
            this.addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onFlare(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new GainEnergyAction(this.upgraded ? 2 : 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
