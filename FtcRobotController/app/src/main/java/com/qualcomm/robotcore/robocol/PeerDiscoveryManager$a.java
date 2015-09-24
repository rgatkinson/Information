package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;

private class a implements Runnable
{
    @Override
    public void run() {
        try {
            RobotLog.v("Sending peer discovery packet");
            final RobocolDatagram robocolDatagram = new RobocolDatagram(PeerDiscoveryManager.a(PeerDiscoveryManager.this));
            if (PeerDiscoveryManager.b(PeerDiscoveryManager.this).getInetAddress() == null) {
                robocolDatagram.setAddress(PeerDiscoveryManager.c(PeerDiscoveryManager.this));
            }
            PeerDiscoveryManager.b(PeerDiscoveryManager.this).send(robocolDatagram);
        }
        catch (RobotCoreException ex) {
            RobotLog.d("Unable to send peer discovery packet: " + ex.toString());
        }
    }
}
