package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class QiSeGuBai extends AbstractCthughaCard {
    public static final String ID = CthughaHelper.makeID(QiSeGuBai.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/其色孤白.png";
    private static final int COST = 4;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public QiSeGuBai() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.exhaust = true;

        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(3);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SelectCardsInHandAction(1, cardStrings.EXTENDED_DESCRIPTION[0],
                false, true,
                        card -> CthughaHelper.isBurn(card) || CthughaHelper.isDefendCard(card),
                        cards -> cards.forEach(card -> {
							p.masterDeck.group.stream()
									.filter(o -> o.cardID.equals(card.cardID))
									.findFirst()
                                    .ifPresent(c -> {
                                        AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(
                                                c,
                                                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                                        p.masterDeck.removeCard(c);
                                    });
						})));
    }
}
