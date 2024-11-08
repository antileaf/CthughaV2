package com.cthugha.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.helpers.ModHelper;
import com.cthugha.power.WuYouBuZhuPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

public class YanZhiJing extends AbstractOrb {

	public static final String ORB_ID = ModHelper.makeID(YanZhiJing.class.getSimpleName());
	private static final OrbStrings STRINGS = CardCrawlGame.languagePack.getOrbString(ORB_ID);
	private static final String IMG_PATH = "cthughaResources/img/huozhijing_2.png";

	public AbstractCard card;

	public YanZhiJing() {
		this.ID = ORB_ID;
		this.img = ImageMaster.loadImage(IMG_PATH);
		this.name = STRINGS.NAME;
		this.baseEvokeAmount = -1;
		this.basePassiveAmount = -1;
		updateDescription();
	}

	// 吸取灼伤
	public void loading(AbstractCard card) {
		if (card != null) {
			if (ModHelper.isBurn(card)) {
				if (card.retain) {
					return;
				}
				this.card = card;
			}
		}
	}

	// 使用被动能力
	public void applyPassive() {
		if (this.card == null) {
			AbstractCard card = AbstractDungeon.player.hand.findCardById("Burn");
			this.loading(card);
		}

		if (this.card != null) {
			AbstractCard cardFromHand = AbstractDungeon.player.hand.getSpecificCard(this.card);
			AbstractCard cardFromDrawPile = AbstractDungeon.player.drawPile.getSpecificCard(this.card);
			AbstractCard cardFromDiscardPile = AbstractDungeon.player.discardPile.getSpecificCard(this.card);
			if (cardFromHand != null) {
//				ModHelper.addToBuffer(new DiscardSpecificCardAction(cardFromHand,
//						AbstractDungeon.player.hand));
                AbstractDungeon.player.hand.moveToDiscardPile(this.card);
			} else if (cardFromDrawPile != null) {
//				ModHelper.addToBuffer(new DiscardSpecificCardAction(cardFromDrawPile,
//						AbstractDungeon.player.drawPile));
                AbstractDungeon.player.drawPile.moveToDiscardPile(this.card);
			} else if (cardFromDiscardPile != null) {
//				ModHelper.addToBuffer(new DiscardSpecificCardAction(cardFromDiscardPile,
//						AbstractDungeon.player.discardPile));
                AbstractDungeon.player.discardPile.moveToDiscardPile(this.card);
			}

			int damage = 10;
			AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
					AbstractDungeon.cardRandomRng);
			if (randomMonster != null) {
				if (AbstractDungeon.player.hasPower(WuYouBuZhuPower.POWER_ID)) {
					AbstractPower power = AbstractDungeon.player.getPower(WuYouBuZhuPower.POWER_ID);
					if (power != null) {
						power.flash();
						ModHelper.addToBuffer(new DamageCallbackAction(randomMonster,
								new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS),
								AbstractGameAction.AttackEffect.FIRE,
								(amount) -> {
									if (amount > 0) {
										AbstractDungeon.actionManager.addToTop(
												new DecreaseMonsterMaxHealthAction(randomMonster, amount));
									}
								}));

//                        AbstractDungeon.actionManager
//                                .addToTop(new DecreaseMonsterMaxHealthAction(randomMonster, damage));
					}
				}
//				AbstractDungeon.actionManager
//						.addToTop(new DamageAction(randomMonster,
//								new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS)));
				else
					ModHelper.addToBuffer(new DamageAction(randomMonster,
							new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS),
							AbstractGameAction.AttackEffect.FIRE));
			}

			ModHelper.commitBuffer();
			AbstractDungeon.actionManager
					.addToTop(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA)));

			this.card = null;
		}
	}

	public void onEndOfTurn() {
		AbstractDungeon.actionManager.addToBottom(new AnonymousAction(this::applyPassive));
	}

	@Override
	public AbstractOrb makeCopy() {
		return new YanZhiJing();
	}

	@Override
	public void onEvoke() {
	}

	@Override
	public void playChannelSFX() {
		CardCrawlGame.sound.play("ORB_PLASMA_CHANNEL", 0.2F);
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F,
				48.0F, 96.0F, 96.0F, this.scale +
						MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale,
				this.scale,
				this.angle, 0, 0, 96, 96,
				false, false);
		sb.setBlendFunction(770, 1);
		sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F,
				48.0F, 96.0F, 96.0F, this.scale,
				this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale,
				this.angle, 0, 0, 96, 96, false, false);
		sb.setBlendFunction(770, 771);
		renderText(sb);
		this.hb.render(sb);
	}

	protected void renderText(SpriteBatch sb) {
		if (this.showEvokeValue)
			FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
					Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale,
					new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
	}

	// protected void renderText(SpriteBatch sb) {
	// if (this.showEvokeValue) {
	// FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
	// Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET,
	// this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.CYAN,
	// this.fontScale);
	// } else {
	// FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
	// Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET,
	// this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.WHITE,
	// this.fontScale);
	// }
	// }

	@Override
	public void updateDescription() {
		applyFocus();
		this.description = STRINGS.DESCRIPTION[0];
	}

}
