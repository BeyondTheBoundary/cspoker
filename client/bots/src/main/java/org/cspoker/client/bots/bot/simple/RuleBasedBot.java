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
package org.cspoker.client.bots.bot.simple;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Rounds;
import org.cspoker.common.elements.table.TableId;

public class RuleBasedBot
		extends AbstractBot {
	
	private final static Logger logger = Logger.getLogger(RuleBasedBot.class);
	Random random = new Random();

	public RuleBasedBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, executor, botListeners);
	}
	
	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			
			public void run() {
				try {
					if (tableContext.getCurrentRound().equals(Rounds.PREFLOP)) {
						playerContext.checkOrCall();
					} else {
						float betProbability;
						if (playerContext.haveA(Rank.ACE) || playerContext.havePocketPair()) {
							betProbability = 0.99F;
						} else {
							betProbability = 0.01F;
						}
						if (random.nextFloat() < betProbability) {
							playerContext.raiseMaxBetWith(lobbyContext.getHoldemTableInformation(tableID)
									.getGameProperty().getBigBlind());
						} else {
							playerContext.raiseMaxBetWith(0);
						}
					}
				} catch (IllegalActionException e) {
					logger.error(e);
					throw new IllegalStateException("Action was not allowed.",e);
				}catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Action failed.",e);

				}
			}
		});
	}
	
}