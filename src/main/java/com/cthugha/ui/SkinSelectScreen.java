package com.cthugha.ui;

import basemod.BaseMod;
import basemod.Pair;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import basemod.interfaces.ISubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.cthugha.characters.Cthugha;
import com.cthugha.enums.MyPlayerClassEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import java.util.ArrayList;

public class SkinSelectScreen implements ISubscriber, CustomSavable<Integer> {
	private static final String[] TEXT;

	private static final ArrayList<Skin> SKINS = new ArrayList<>();

	public static SkinSelectScreen Inst;

	public static boolean shouldUpdateBackground = false;

	public Hitbox leftHb;

	public Hitbox rightHb;

	public TextureAtlas atlas;

	public Skeleton skeleton;

	public AnimationStateData stateData;

	public AnimationState state;

	public String curName = "";

	public String nextName = "";

	public int index;

	static {
		final String ID = CthughaHelper.makeID(SkinSelectScreen.class.getSimpleName());
		TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
		SKINS.add(new Skin(0, "default"));
		SKINS.add(new Skin(1, "mari")); // 红艳煞-以实玛利
		SKINS.add(new Skin(2, "god")); // 神皮使徒
		SKINS.add(new Skin(3, "dianhuo")); // “癫火之王”米德拉
		SKINS.add(new Skin(4, "cirno")); // 火焰琪露诺
		SKINS.add(new Skin(5, "mcdonald")); // 麦当劳薯条
		SKINS.add(new Skin(6, "stardust")); // 星尘
		SKINS.add(new Skin(7, "kagari")); // 闪刀姬-燎里
		SKINS.add(new Skin(8, "helia")); // 豪赌客-海拉
		SKINS.add(new Skin(9, "rin")); // 火焰猫燐

		Inst = new SkinSelectScreen();
	}

	public static Pair<String, Color> getFireVampireSkin() {
		int index = Inst.index;

		if (index == 2) // god
			return new Pair<>(null, Color.DARK_GRAY);
		else if (index == 3) // dianhuo
			return new Pair<>(null, Color.GOLD);
		else if (index == 5) // mcdonald
			return new Pair<>("cthughaResources/img/orbs/mcdonald.png", null);
		else if (index == 6) // stardust
			return new Pair<>("cthughaResources/img/orbs/stardust.png", null);
		else if (index == 9) // rin
			return new Pair<>(null, Color.ROYAL);

		// default
		return new Pair<>(null, Color.SCARLET);
	}

	public SkinSelectScreen() {
		this.index = 0;
		refresh();
		this.leftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.rightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		BaseMod.subscribe(this);
		BaseMod.addSaveField(CthughaHelper.makeID("skin"), (CustomSavableRaw) this);
	}

	public static Skin getSkin() {
		return SKINS.get(Inst.index);
	}

	public void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
		this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
		SkeletonJson json = new SkeletonJson(this.atlas);
		json.setScale(Settings.renderScale / scale);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
		this.skeleton = new Skeleton(skeletonData);
		this.skeleton.setColor(Color.WHITE);
		this.stateData = new AnimationStateData(skeletonData);
		this.state = new AnimationState(this.stateData);
		AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
		e.setTimeScale(1.2F);
	}

	public void refresh() {
		Skin skin = SKINS.get(this.index);
		this.curName = skin.name;
		// loadAnimation(skin.charPath + ".atlas", skin.charPath + ".json", 1.5F);
		this.nextName = ((Skin) SKINS.get(nextIndex())).name;

		shouldUpdateBackground = true;

		if (AbstractDungeon.player instanceof Cthugha) {
			Cthugha k = (Cthugha) AbstractDungeon.player;
			k.refreshSkin();
		}
	}

	public int prevIndex() {
		return (this.index - 1 < 0) ? (SKINS.size() - 1) : (this.index - 1);
	}

	public int nextIndex() {
		return (this.index + 1 > SKINS.size() - 1) ? 0 : (this.index + 1);
	}

	public void update() {
		float centerX = Settings.WIDTH * 0.86F;
		float centerY = Settings.HEIGHT * 0.55F;

		this.leftHb.move(centerX - 200.0F * Settings.scale, centerY);
		this.rightHb.move(centerX + 200.0F * Settings.scale, centerY);
		updateInput();
	}

	private void updateInput() {
		if (CardCrawlGame.chosenCharacter == MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS) {
			this.leftHb.update();
			this.rightHb.update();
			if (this.leftHb.clicked) {
				this.leftHb.clicked = false;
				CardCrawlGame.sound.play("UI_CLICK_1");
				this.index = prevIndex();
				refresh();
			}
			if (this.rightHb.clicked) {
				this.rightHb.clicked = false;
				CardCrawlGame.sound.play("UI_CLICK_1");
				this.index = nextIndex();
				refresh();
			}
			if (InputHelper.justClickedLeft) {
				if (this.leftHb.hovered)
					this.leftHb.clickStarted = true;
				if (this.rightHb.hovered)
					this.rightHb.clickStarted = true;
			}
		}
	}

	public Texture getPortrait() {
		return SKINS.get(this.index).portrait_IMG;
	}

//	public void renderPortrait(SpriteBatch sb) {
//		Skin skin = SKINS.get(this.index);
//
//		if (Settings.isSixteenByTen) {
//			sb.draw(skin.portrait_IMG, Settings.WIDTH / 2.0F - 960.0F, Settings.HEIGHT / 2.0F - 600.0F, 960.0F, 600.0F,
//					1920.0F, 1200.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1920, 1080, false, false);
//		} else if (Settings.isFourByThree) {
//			sb.draw(skin.portrait_IMG, Settings.WIDTH / 2.0F - 960.0F, Settings.HEIGHT / 2.0F - 600.0F + 0.0F, 960.0F, 600.0F,
//					1920.0F, 1200.0F, Settings.yScale, Settings.yScale, 0.0F, 0, 0, 1920, 1080, false, false);
//		} else if (Settings.isLetterbox) {
//			sb.draw(skin.portrait_IMG, Settings.WIDTH / 2.0F - 960.0F, Settings.HEIGHT / 2.0F - 600.0F + 0.0F, 960.0F, 600.0F,
//					1920.0F, 1200.0F, Settings.xScale, Settings.xScale, 0.0F, 0, 0, 1920, 1080, false, false);
//		} else {
//			sb.draw(skin.portrait_IMG, Settings.WIDTH / 2.0F - 960.0F, Settings.HEIGHT / 2.0F - 600.0F + 0.0F, 960.0F, 600.0F,
//					1920.0F, 1200.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1920, 1080, false, false);
//		}
//
//	}

	public void render(SpriteBatch sb) {
		float centerX = Settings.WIDTH * 0.86F;
		float centerY = Settings.HEIGHT * 0.55F;
//		renderSkin(sb, centerX, centerY);
		FontHelper.renderFontCentered(
				sb,
				FontHelper.cardTitleFont,
				TEXT[0],
				centerX,
				centerY + 150.0F * Settings.scale,
				Color.WHITE,
				1.25F
		);

		Texture img = getSkin().char_IMG;
		sb.draw(img, centerX - img.getWidth() / 2.0F, centerY - img.getHeight() / 2.0F);

		Color color = Settings.GOLD_COLOR.cpy();
		color.a /= 2.0F;
		// float dist = 100.0F * Settings.scale;
		FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.curName, centerX, centerY - 230.0F, Settings.GOLD_COLOR, 1.1F);
		// FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, this.nextName,
		// centerX + dist * 1.5F, centerY - dist, color);
		if (this.leftHb.hovered) {
			sb.setColor(Color.LIGHT_GRAY);
		} else {
			sb.setColor(Color.WHITE);
		}
		sb.draw(ImageMaster.CF_LEFT_ARROW, this.leftHb.cX - 24.0F, this.leftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
				Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
		if (this.rightHb.hovered) {
			sb.setColor(Color.LIGHT_GRAY);
		} else {
			sb.setColor(Color.WHITE);
		}
		sb.draw(ImageMaster.CF_RIGHT_ARROW, this.rightHb.cX - 24.0F, this.rightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
				Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
		this.rightHb.render(sb);
		this.leftHb.render(sb);
	}

	public void renderSkin(SpriteBatch sb, float x, float y) {
		if (this.atlas == null)
			return;
		this.state.update(Gdx.graphics.getDeltaTime());
		this.state.apply(this.skeleton);
		this.skeleton.updateWorldTransform();
		this.skeleton.setPosition(x, y);
		sb.end();
		CardCrawlGame.psb.begin();
		AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
		CardCrawlGame.psb.end();
		sb.begin();
	}

	public static class Skin {
		public String charPath;

		public Texture char_IMG, portrait_IMG;

		public String name;

		public Skin(int index, String charPath) {
			this.charPath = "cthughaResources/img/char/cthugha_" + charPath + ".png";
			this.char_IMG = ImageMaster.loadImage(this.charPath);
			this.portrait_IMG = ImageMaster.loadImage("cthughaResources/img/char/cthugha_" + charPath + "_portrait" + ".png");
			// this.monsterPath = "KaltsitResources/img/char/token_10002_kalts_mon3tr" +
			// monsterPath;
			// this.calcitePath = "KaltsitResources/img/char/calcite" + calcitePath;
			// this.shoulder = "KaltsitResources/img/char/shoulder.png";
			this.name = SkinSelectScreen.TEXT[index + 1];
		}
	}

	public void onLoad(Integer arg0) {
		if (arg0 != null) {
			this.index = arg0.intValue();
		}
		refresh();
	}

	public Integer onSave() {
		return Integer.valueOf(this.index);
	}
}
