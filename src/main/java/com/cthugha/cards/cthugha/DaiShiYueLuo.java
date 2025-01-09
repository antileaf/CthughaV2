package com.cthugha.cards.cthugha;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DaiShiYueLuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(DaiShiYueLuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/137.png";
    private static final int COST = 3;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DaiShiYueLuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 13;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m))
            return false;

        int total = AbstractDungeon.player.hand.group.stream()
                .filter(c -> c.costForTurn > 0)
                .mapToInt(c -> c.costForTurn)
                .sum();

        if (total < this.magicNumber) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }

        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        int total = AbstractDungeon.player.hand.group.stream()
                .filter(c -> c.costForTurn > 0)
                .mapToInt(c -> c.costForTurn)
                .sum();

        if (total >= this.magicNumber)
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        else
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AnonymousAction(() -> {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card.costForTurn > 0)
                    card.setCostForTurn(0);
            }
        }));

        this.addToBot(new GainEnergyAction(this.costForTurn));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
            this.initializeDescription();
        }
    }
}
