package com.cthugha.cards.interfaces;

public interface RightClickableCard {
	void onRightClick();

	default Position[] clickablePositions() {
		return new Position[] { Position.HAND };
	}

	enum Position {
		HAND, DRAW_PILE, DISCARD_PILE, EXHAUST_PILE
	}
}
