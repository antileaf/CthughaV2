package com.cthugha.cards;


import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import basemod.abstracts.CustomCard;

public class YiChiRuXi extends AbstractCthughaCard implements OnObtainCard {

    public static final String ID = ModHelper.MakePath(YiChiRuXi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/一赤如烍.png";
    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

    public YiChiRuXi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onObtainCard() {
        ArrayList<AbstractCard> burns = AbstractDungeon.player.masterDeck.group.stream()
                .filter(c -> c instanceof Burn)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
        for (int i = 0; i < this.magicNumber; i++) {
            if (burns.isEmpty())
                break;

            int index = AbstractDungeon.cardRng.random(burns.size() - 1);
            burns.get(index).stopGlowing();
            cardsToRemove.add(burns.get(index));
        }

        float displayCount = 0.0F;
		for (AbstractCard card : cardsToRemove) {
			card.untip();
			card.unhover();
			AbstractDungeon.topLevelEffects
					.add(new PurgeCardEffect(card, Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
			displayCount += Settings.WIDTH / 6.0F;
			AbstractDungeon.player.masterDeck.removeCard(card);
		}
    }

    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(new Burn(), this.magicNumber));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.initializeDescription();
        }
    }
}
