package com.cthugha.orbs;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.characters.Cthugha;
import com.cthugha.orbs.effect.FireEffect;
import com.cthugha.orbs.effect.WeakFireEffect;
import com.cthugha.power.RiShiPower;
import com.cthugha.relics.cthugha.EmeraldTabletVolumeVII;
import com.cthugha.ui.SkinSelectScreen;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.FenJiPower;
import com.cthugha.power.WuYouBuZhuPower;
import com.cthugha.relics.cthugha.HuoTiHuoYan;
import com.cthugha.relics.cthugha.ShengLingLieYan;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FireVampire extends AbstractOrb {
	private static final Logger logger = LogManager.getLogger(FireVampire.class.getName());

	public static final float PARTICLE = 0.06F;
	public static final int BONUS = 5;

	public static final String ORB_ID = CthughaHelper.makeID(FireVampire.class.getSimpleName());
	private static final OrbStrings STRINGS = CardCrawlGame.languagePack.getOrbString(ORB_ID);
//	private static final String IMG_PATH = "cthughaResources/img/huozhijing_2.png";

	private Color color = null;
	private float activatedTimer = 0.0F;
	private float particleTimer = 0.0F;
	private float spinTimer = 0.0F;
	private float spinX, spinY;

	private float angularV = 0.0F;
	private boolean junko = false;

	public int charge = 0;

	public FireVampire() {
		this.ID = ORB_ID;
//		this.img = ImageMaster.loadImage(IMG_PATH);
		this.name = STRINGS.NAME;
		this.baseEvokeAmount = -1;
		this.basePassiveAmount = BONUS;
		updateDescription();

		this.color = Color.SCARLET;

		if (AbstractDungeon.player instanceof Cthugha) {
			Pair<String, Color> pair = SkinSelectScreen.getFireVampireSkin();

			if (pair.getKey() != null) {
				this.img = ImageMaster.loadImage(pair.getKey());

				if (pair.getKey().contains("mcdonald")) {
					this.angularV = 60.0F;
					this.angle = MathUtils.random(0.0F, 360.0F);
				}
				else if (pair.getKey().contains("stardust")) {
					this.angularV = MathUtils.random(12.0F, 18.0F);
					this.angle = MathUtils.random(0.0F, 360.0F);
				}
				else if (pair.getKey().contains("junko"))
					this.junko = true;
			}
			else
				this.color = pair.getValue();
		}
	}

	public void passiveVFX(AbstractMonster target) {
		AbstractDungeon.actionManager
				.addToTop(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA)));

		if (!this.junko)
			AbstractDungeon.actionManager.addToTop(new AnonymousAction(() -> {
				this.activatedTimer = 1.0F;
			}));
		else {
			AbstractDungeon.actionManager.addToTop(new AnonymousAction(() -> {
				this.spinTimer = 1.0F;
				this.spinX = target.hb.cX;
				this.spinY = target.hb.cY;
			}));
		}
	}

	public void triggerPassive(AbstractCard card) {
		if (!CthughaHelper.isBurn(card) || card.retain) {
			logger.warn("FireVampire: triggerPassive: card is illegal");
			return;
		}

		if (AbstractDungeon.player.hand.contains(card))
			AbstractDungeon.player.hand.moveToDiscardPile(card);
		else if (AbstractDungeon.player.drawPile.contains(card))
			AbstractDungeon.player.drawPile.moveToDiscardPile(card);
//		else if (AbstractDungeon.player.discardPile.contains(card))
//			AbstractDungeon.player.discardPile.moveToDiscardPile(card);

		this.applyFocus();
		int damage = this.passiveAmount + card.baseMagicNumber;

		AbstractDungeon.actionManager.addToBottom(new AnonymousAction(() -> {
			AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
					AbstractDungeon.cardRandomRng);
			if (randomMonster != null) {
				if (AbstractDungeon.player.hasPower(RiShiPower.POWER_ID)) {
					AbstractPower power = AbstractDungeon.player.getPower(RiShiPower.POWER_ID);
					if (power != null)
						CthughaHelper.addToBuffer(((RiShiPower) power).getAction(randomMonster));
				}

				if (AbstractDungeon.player.hasPower(WuYouBuZhuPower.POWER_ID)) {
					AbstractPower power = AbstractDungeon.player.getPower(WuYouBuZhuPower.POWER_ID);
					if (power != null) {
						power.flash();
						CthughaHelper.addToBuffer(new DamageCallbackAction(randomMonster,
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
					CthughaHelper.addToBuffer(new DamageAction(randomMonster,
							new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS),
							AbstractGameAction.AttackEffect.FIRE));
			}

			CthughaHelper.commitBuffer();

			this.passiveVFX(randomMonster);
		}));

		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.0F));
	}

	@Override
	public void onEndOfTurn() {
//		AbstractDungeon.actionManager.addToBottom(new AnonymousAction(this::applyPassive));
		this.charge++;
	}

	@Override
	public void onStartOfTurn() {
		this.charge = 0;
	}

	@Override
	public AbstractOrb makeCopy() {
		return new FireVampire();
	}

	@Override
	public void onEvoke() {
	}

	@Override
	public void playChannelSFX() {
		CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F);
	}

	@Override
	public void updateAnimation() {
		super.updateAnimation();

		if (this.angularV > 0.0F)
			this.angle += Gdx.graphics.getDeltaTime() * this.angularV;

		if (this.img == null) {
			this.activatedTimer -= Gdx.graphics.getDeltaTime();
			this.particleTimer -= Gdx.graphics.getDeltaTime();

			if (this.particleTimer < 0.0F) {
				if (this.activatedTimer < 0.0F) {

					AbstractDungeon.effectsQueue.add(new WeakFireEffect(
							this.cX + this.bobEffect.y * 2.0F,
							this.cY + this.bobEffect.y * 2.0F,
							this.color
					));
				}
				else {
					Color tmp = this.color.cpy();
					tmp.r *= 0.5F;
					tmp.g *= 0.5F;
					tmp.b *= 0.5F;

					AbstractDungeon.effectsQueue.add(new FireEffect(
							this.cX + this.bobEffect.y * 2.0F,
							this.cY + this.bobEffect.y * 2.0F,
							tmp
					));
				}

				this.particleTimer = PARTICLE;
			}
		}
		else if (this.junko) {
			this.spinTimer -= Gdx.graphics.getDeltaTime();

			if (this.spinTimer < 0.0F) {
				this.spinTimer = 0.0F;
				this.angle = 0.0F;
			}

			if (this.spinTimer > 0.0F) {
				if (this.spinTimer > 0.8F) {
					float targetAngle = MathUtils.atan2(this.spinY - this.cY, this.spinX - this.cX) *
							MathUtils.radiansToDegrees - 90.0F;

					float progress = (1.0F - this.spinTimer) / 0.2F;
					progress = 1.0F - ((1.0F - progress) * (1.0F - progress));

					this.angle = MathUtils.lerpAngleDeg(this.angle, targetAngle,
							progress);
				} else if (this.spinTimer < 0.3F) {
					float targetAngle = MathUtils.atan2(this.spinY - this.cY, this.spinX - this.cX) *
							MathUtils.radiansToDegrees - 90.0F;

					this.angle = MathUtils.lerpAngleDeg(targetAngle, 0.0F,
							1.0F - this.spinTimer / 0.3F);
				}
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		if (this.img != null) {
			sb.setColor(Color.WHITE);

			sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F,
					48.0F, 96.0F, 96.0F, this.scale +
							MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale,
					this.scale,
					this.angle, 0, 0, 96, 96,
					false, false);

//			if (this.color.r == 0.0F) {
//				sb.setBlendFunction(770, 1);
//
//				sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F,
//						48.0F, 96.0F, 96.0F, this.scale,
//						this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale,
//						this.angle, 0, 0, 96, 96, false, false);
//
//				sb.setBlendFunction(770, 771);
//			}
		}

		this.renderText(sb);
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
	public void applyFocus() {
		this.passiveAmount = this.basePassiveAmount;

		if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID))
			this.passiveAmount += HuoTiHuoYan.BONUS;
		if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID))
			this.passiveAmount += ShengLingLieYan.BONUS;

		if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID))
			this.passiveAmount += AbstractDungeon.player.getPower(FenJiPower.POWER_ID).amount;

		if (AbstractDungeon.player.hasPower(VigorPower.POWER_ID) &&
				AbstractDungeon.player.hasRelic(EmeraldTabletVolumeVII.ID)) {
			int vigor = Math.min(AbstractDungeon.player.getPower(VigorPower.POWER_ID).amount,
					this.passiveAmount);

			this.passiveAmount += vigor;
		}
	}

	@Override
	public void updateDescription() {
		this.applyFocus();
		this.description = String.format(STRINGS.DESCRIPTION[0], this.passiveAmount);
	}

}
