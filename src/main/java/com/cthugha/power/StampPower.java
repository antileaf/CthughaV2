package com.cthugha.power;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.awt.*;
import java.util.Arrays;

public class StampPower extends AbstractPower implements OnReceivePowerPower, InvisiblePower {
	public static final String POWER_ID = CthughaHelper.makeID(StampPower.class.getSimpleName());
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private ColorificStampModifier.ColorEnum color;

	public StampPower(ColorificStampModifier.ColorEnum color, int amount) {
		this.color = color;
		this.ID = POWER_ID + "_" + color.name();
		this.name = powerStrings.NAME;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.updateDescription();

		this.type = PowerType.BUFF;

		this.img = new Texture("cthughaResources/img/power/stamp/" +
				color.name().toLowerCase() + ".png");
	}

	@Override
	public void updateDescription() {
		this.name = powerStrings.DESCRIPTIONS[this.color.ordinal() + 1];
	}

	@Override
	public boolean onReceivePower(AbstractPower other, AbstractCreature target, AbstractCreature source) {
		if (!(other instanceof StampPower))
			return true;

		StampPower stamp = (StampPower) other;
		if (stamp.color == this.color) {
			Areshkagal areshkagal = (Areshkagal) AbstractDungeon.getMonsters().getMonster(Areshkagal.ID);

			if (areshkagal != null) {
//				if (areshkagal.hasPower(FourColorTheoremPower.POWER_ID))
//					areshkagal.getPower(FourColorTheoremPower.POWER_ID).flash();

				if (areshkagal.intent != AbstractMonster.Intent.NONE) {
					this.addToBot(new ShowMoveNameAction(areshkagal));
					this.addToBot(new IntentFlashAction(areshkagal));
				}

				areshkagal.takeTurn(); // 我说谁来都打不过
				this.addToBot(new AnonymousAction(areshkagal::createIntent));
				this.addToBot(new AnonymousAction(() -> {
					areshkagal.intentAlphaTarget = 1.0F;
				}));
			}
		}
		else if (this.color == ColorificStampModifier.ColorEnum.R || this.color == ColorificStampModifier.ColorEnum.G) {
			if (Arrays.stream(ColorificStampModifier.ColorEnum.values()) // 四种不同颜色，全都减一层
					.allMatch(c -> AbstractDungeon.player.hasPower(POWER_ID + "_" + c.name()) ||
							((StampPower) other).color == c)) {
				if (this.color == ColorificStampModifier.ColorEnum.G &&
						((StampPower) other).color != ColorificStampModifier.ColorEnum.R)
					return true; // 只有 other 是红色时，自己是绿色的情况才能触发

				Arrays.stream(ColorificStampModifier.ColorEnum.values())
						.map(c -> POWER_ID + "_" + c.name())
						.forEach(id -> AbstractDungeon.actionManager.addToBottom(
								new ReducePowerAction(AbstractDungeon.player,
										AbstractDungeon.player, id, 1)));
			}
		}

		return true;
	}

	private static final float FADE_DUR = 0.5F;
	private static final float[] ANGLE = { -25.0F, 25.0F, -155.0F, 155.0F };
	private static float[] timer;
	private static Texture[] textures;

	public static void initialize() {
		textures = new Texture[ColorificStampModifier.ColorEnum.values().length + 1];
		for (int i = 0; i < ColorificStampModifier.ColorEnum.values().length; i++) {
			textures[i] = new Texture("cthughaResources/img/stamp/nail_" +
					ColorificStampModifier.ColorEnum.values()[i].name().toLowerCase() + ".png");
		}

		textures[textures.length - 1] = new Texture("cthughaResources/img/stamp/nail_w.png");

		timer = new float[ColorificStampModifier.ColorEnum.values().length];
		Arrays.fill(timer, 0.0F);
	}

	public static void clearTimer() {
		Arrays.fill(timer, 0.0F);
	}

	public static void update() {
		for (int i = 0; i < ColorificStampModifier.ColorEnum.values().length; i++) {
			if (AbstractDungeon.player.hasPower(POWER_ID + "_" +
					ColorificStampModifier.ColorEnum.values()[i].name()))
				timer[i] = Math.min(FADE_DUR, timer[i] + Gdx.graphics.getDeltaTime());
			else
				timer[i] = Math.max(0.0F, timer[i] - Gdx.graphics.getDeltaTime());
		}
	}

	private static float getOffsetX(int i) {
		return (i / 2 == 0 ? -300.0F : 300.0F) * 1.3F;
	}

	private static float getOffsetY(int i) {
		return (i % 2 == 0 ? 170.0F : -120.0F) * 1.3F;
	}

	private static void draw(SpriteBatch sb, Texture texture, float transparency, int i) {
		Color color = Color.WHITE.cpy();
		color.a = transparency;
		sb.setColor(color);

		sb.draw(texture,
				AbstractDungeon.player.hb.cX + getOffsetX(i) - texture.getWidth() / 2.0F,
				AbstractDungeon.player.hb.cY + getOffsetY(i) - texture.getHeight() / 2.0F,
				texture.getWidth() / 2.0F, texture.getHeight() / 2.0F,
				texture.getWidth(), texture.getHeight(),
				Settings.scale, Settings.scale,
				ANGLE[i], 0, 0,
				texture.getWidth(), texture.getHeight(), false, false);
	}

	public static void render(SpriteBatch sb) {
		for (int i = 0; i < ColorificStampModifier.ColorEnum.values().length; i++) {

			float transparency = timer[i] / FADE_DUR;

			if (transparency > 0.0F)
				draw(sb, textures[i], transparency, i);

			if (transparency < 1.0F)
				draw(sb, textures[textures.length - 1], 1.0F - transparency, i);

			AbstractPower power = AbstractDungeon.player.getPower(POWER_ID + "_" +
					ColorificStampModifier.ColorEnum.values()[i].name());
			if (power != null) {
				BitmapFont[] fonts = new BitmapFont[]{
						FontHelper.energyNumFontRed,
						FontHelper.energyNumFontGreen,
						FontHelper.energyNumFontBlue,
						FontHelper.energyNumFontPurple
				};

				int amount = power.amount;
				if (amount > 1)
					FontHelper.renderFontCentered(sb, fonts[i], Integer.toString(amount),
							AbstractDungeon.player.hb.cX + getOffsetX(i) * 1.22F,
							AbstractDungeon.player.hb.cY + getOffsetY(i) * 1.25F,
							Color.WHITE.cpy());
			}
		}
	}
}
