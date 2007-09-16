/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.game.player;

import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.cspoker.server.game.PlayerId;
import org.cspoker.server.game.elements.cards.deck.Deck.Card;

/**
 * A class of immutable saved players.
 *
 *
 * @author Kenzo
 *
 */
@Immutable
public class SavedPlayer {

	/**
	 * The variable containing the id of the player.
	 */
	private final PlayerId id;

	/**
	 * The name of the player.
	 */
	private final String name;

	/**
	 * The stack of this player.
	 */
	private final int stackValue;

	/**
	 * The chips the player has bet in this round.
	 *
	 */
	private final int betChipsValue;

	/**
	 * The hidden cards.
	 */
	private final List<Card> pocketCards;

	public SavedPlayer(Player player){
		id = player.getId();
		name = player.getName();
		stackValue = player.getStack().getValue();
		betChipsValue = player.getBetChips().getValue();
		pocketCards = Collections.unmodifiableList(player.getPocketCards());
	}

	/**
	 * Returns the id of this saved player.
	 *
	 * @return The id of this saved player.
	 */
	public PlayerId getId(){
		return id;
	}

	/**
	 * Returns the name of this saved player.
	 *
	 * @return The name of this saved player.
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the stack value of this saved player.
	 *
	 * @return The stack value of this saved player.
	 */
	public int getStackValue(){
		return stackValue;
	}

	/**
	 * Returns the bet chips value of this saved player.
	 *
	 * @return The bet chips value of this saved player.
	 */
	public int getBetChipsValue(){
		return betChipsValue;
	}

	/**
	 * The pocket cards of this saved player.
	 *
	 * @return The pocket cards of this saved player.
	 */
	public List<Card> getPocketCards(){
		return pocketCards;
	}

}
