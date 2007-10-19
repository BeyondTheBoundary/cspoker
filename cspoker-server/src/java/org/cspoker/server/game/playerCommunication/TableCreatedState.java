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
package org.cspoker.server.game.playerCommunication;

import org.apache.log4j.Logger;
import org.cspoker.common.game.player.PlayerId;
import org.cspoker.server.game.GameManager;
import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.TableManager;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.server.game.gameControl.GameControl;
import org.cspoker.server.game.gameControl.IllegalActionException;

/**
 * A player who has created the table goes to the table created state.
 * 
 * Only this player can change game settings or start the game.
 * 
 * 
 * 
 * InitialState --------------------------------> TableCreatedState /\
 * createTable() | | |__________________________________________________| |
 * startGame() leaveTable() && only player at table. | | \/ PlayingState (for
 * all players at the table)
 * 
 * @author Kenzo
 * 
 */
class TableCreatedState extends WaitingAtTableState {
    private static Logger logger = Logger.getLogger(TableCreatedState.class);

    /**
     * Construct a new table created state with given player communication and
     * table.
     * 
     * @param playerCommunication
     *                The playerCommunication of the player.
     * @param table
     *                The created table.
     */
    public TableCreatedState(PlayerCommunicationImpl playerCommunication,
	    Table table) {
	super(playerCommunication, table);
    }

    @Override
    public void startGame() throws IllegalActionException {

	/**
	 * The table should be locked so while constructing the new game no
	 * player can exit or enter the table.
	 * 
	 * Important for not having a dead-lock: only one player can call
	 * startGame(), as it is guaranteed.
	 */
	synchronized (table) {
	    GameMediator gameMediator = new GameMediator();
	    for (PlayerId id : table.getPlayerIds()) {
		PlayerCommunicationImpl comm = PlayerCommunicationManager
			.getPlayerCommunication(id);
		comm.setPlayerCommunicationState(new PlayingState(comm,
			gameMediator));
	    }
	    new GameControl(gameMediator, table);
	    GameManager.addGame(table.getId(), gameMediator);
	}

	TableCreatedState.logger.info("Game Started.");
    }

    @Override
    public void leaveTable() throws IllegalActionException {
	synchronized (table) {
	    if (table.getNbPlayers() == 1) {
		TableManager.removeTable(table);
		table.removePlayer(playerCommunication.getPlayer());
		playerCommunication
			.setPlayerCommunicationState(new InitialState(
				playerCommunication));
		GameManager.getServerMediator().publishPlayerLeftEvent(
			new PlayerLeftEvent(playerCommunication.getPlayer()
				.getSavedPlayer(), table.getId()));
	    } else
		throw new IllegalActionException(
			"The owner can only leave if he is the only player at the table.");
	}
    }

    @Override
    protected String getStdErrorMessage() {
	return "You have not yet started the game.";
    }

}
