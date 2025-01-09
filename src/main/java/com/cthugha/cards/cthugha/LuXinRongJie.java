package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.LuXinRongJiePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LuXinRongJie extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(LuXinRongJie.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/151.png";
    private static final int COST = 3;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public LuXinRongJie() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 21;

        this.keywords.add(GameDictionary.ENLIGHTENMENT.NAMES[0].toLowerCase());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new LuXinRongJiePower(p, this.magicNumber), 0));
    }
}
