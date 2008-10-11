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
package org.cspoker.common.api.lobby.holdemtable.context;

import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class ForwardingRemoteHoldemTableContext implements RemoteHoldemTableContext{

	private RemoteHoldemTableContext holdemTableContext;

	public ForwardingRemoteHoldemTableContext(RemoteHoldemTableContext holdemTableContext) throws RemoteException {
		this.holdemTableContext  = holdemTableContext;
	}

	public void leaveTable() throws RemoteException, IllegalActionException {
		holdemTableContext.leaveTable();
	}

	public RemoteHoldemPlayerContext sitIn(long seatId, int amount, HoldemPlayerListener holdemPlayerListener)
			throws RemoteException, IllegalActionException {
		return holdemTableContext.sitIn(seatId, amount, holdemPlayerListener);
	}
	
}
