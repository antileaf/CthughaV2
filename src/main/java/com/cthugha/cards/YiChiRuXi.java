package com.cthugha.cards;


import java.util.ArrayList;
import java.util.Iterator;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
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

public class YiChiRuXi extends CustomCard {

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
        
        // // 测试卡图
        // Texture img = ImageMaster.loadImage("cthughaResources/img/joke_card/一赤如烍.png");
        // TextureAtlas.AtlasRegion imgRegion = new TextureAtlas.AtlasRegion(img, 0, 0, img.getWidth(), img.getHeight());
        // this.jokePortrait = imgRegion;

        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    // // TODO 这里功能没有生效，效果是：该卡牌加入卡组时触发
    // public void onChoseThisOption() {
    // System.out.println("==================!!!!!!!!!!!!!!!!");
    // AbstractCard card = AbstractDungeon.player.masterDeck.findCardById("Burn");
    // CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    // tmp.addToTop(card);
    // deleteCards(tmp.group);
    // }

    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(new Burn(), this.magicNumber));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    public void deleteCards(ArrayList<AbstractCard> group) {
        float displayCount = 0.0F;
        for (Iterator<AbstractCard> i = group.iterator(); i.hasNext();) {
            AbstractCard card = i.next();
            card.untip();
            card.unhover();
            AbstractDungeon.topLevelEffects
                    .add(new PurgeCardEffect(card, Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
            displayCount += Settings.WIDTH / 6.0F;
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
        // (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
        // AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    // public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    // return false;
    // }

}
