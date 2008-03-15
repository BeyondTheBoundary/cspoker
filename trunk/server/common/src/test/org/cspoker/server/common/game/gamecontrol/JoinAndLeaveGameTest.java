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
package org.cspoker.server.common.game.gamecontrol;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.elements.table.GameTable;
import org.cspoker.server.common.game.elements.table.PlayerListFullException;
import org.cspoker.server.common.game.gamecontrol.rounds.PreFlopRound;
import org.cspoker.server.common.game.gamecontrol.rounds.WaitingRound;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.player.PlayerFactory;

import junit.framework.TestCase;

public class JoinAndLeaveGameTest extends TestCase {
	
	static {
		Log4JPropertiesLoader
		.load("org/cspoker/server/common/logging/log4j.properties");
	}
	
	private static Logger logger = Logger.getLogger(GameFlowTest.class);

	private GamePlayer kenzo;

	private GamePlayer cedric;

	private GamePlayer guy;

	private GameTable table;

	private GameMediator gameMediator;

	private PlayerFactory playerFactory;

	@Override
	protected void setUp() {
		
		playerFactory = new TestPlayerFactory();
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);
			guy = playerFactory.createNewPlayer("Guy", 500);
			
			TableId id = new TableId(0);
			gameMediator = new GameMediator(id);
			table = new GameTable(id, new GameProperty(10));
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test Settings:
	 * > 2 players
	 * > on-turn player leaves the table.
	 */
	public void testLeaveTable1(){
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		}catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		
		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(kenzo);
			assertEquals(0,kenzo.getBetChips().getValue());
			assertEquals(WaitingRound.class, gameControl.getRound().getClass());
			gameControl.joinGame(null, kenzo);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test Settings:
	 * > 2 players
	 * > other player (big blind player) leaves the table.
	 */
	public void testLeaveTable2(){
		try {
			kenzo = playerFactory.createNewPlayer("Kenzo", 500);
			cedric = playerFactory.createNewPlayer("Cedric", 500);

			table = new GameTable(new TableId(0), new GameProperty());
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		
		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(cedric);
			assertEquals(0,cedric.getBetChips().getValue());
			assertEquals(WaitingRound.class, gameControl.getRound().getClass());
			gameControl.joinGame(null, cedric);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test Settings:
	 * > 3 players
	 * > other player (big blind player) leaves the table.
	 */
	public void testLeaveTable3(){
		try {
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		
		GameControl gameControl = new GameControl(gameMediator, table, kenzo);
		
		try {
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.leaveGame(kenzo);
			assertFalse(gameControl.getGame().hasAsActivePlayer(kenzo));
			assertFalse(gameControl.getGame().getTable().hasAsPlayer(kenzo));
			assertEquals(0,kenzo.getBetChips().getValue());
			gameControl.fold(cedric);
			gameControl.joinGame(null, kenzo);
			assertTrue(gameControl.getGame().getTable().hasAsPlayer(kenzo));
			assertFalse(gameControl.getGame().hasAsActivePlayer(kenzo));
			gameControl.fold(cedric);
			assertTrue(gameControl.getGame().hasAsActivePlayer(kenzo));
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

}