package org.usfirst.frc.team138.robot;

/**
 * 
 */

import java.net.DatagramSocket;
import java.math.BigDecimal;
import java.net.DatagramPacket;

import org.json.JSONObject;
import org.json.JSONArray;


/**
 * This is a class to be used for listening for incoming UDP Datagram Packets.
 * In this particular case, it is looking for the JSON data being sent by a 
 * Raspberry Pi running OpenCV vision software.
 * It uses the Singleton Design Pattern
 * @author Al Landeck
 *
 */
public class VisionDataListener implements Runnable {
	/**
	 * @param args
	 */
	
	protected final int PACKETSIZE = 300 ;
	protected final int PORT = 5002;

	// Mapped Vision Attributes
	protected BigDecimal v_dist;
	protected long v_collectTime;
	protected JSONArray v_aim;
	protected long v_sendTime;
	protected JSONArray v_resolution;
	protected String v_id;
	protected boolean v_match;
	
	protected DatagramSocket socket;
	protected boolean listenerBreak = false;
	
	private static VisionDataListener instance = null;
	
	/**
	 * Make this a singleton, this method cannot be called by any external objects
	 */
	protected VisionDataListener() {
		// Exists only to defeat instantiation.
	}
		
	/**
	 * This is called to return the pointer to the single listener.
	 * If it doesn't currently exist, it is constructed, but only the first time.
	 * 
	 * @return VisionDataListener
	 */
	public static VisionDataListener getInstance() {
		if(instance == null) {
			instance = new VisionDataListener();
		}
		return instance;
	}

	/**
	 * Called once to setup the port.  Needed prior to run() (or start of a Thread) 
	 */
    public void initialize() {
        try
        {
        	socket = new DatagramSocket( PORT ) ;
        }
        catch( Exception e )
        {
           System.out.println( e ) ;
           return;
        }
    }

	/**
	 * This method is needed to implement the Runnable class which enables this class to 
	 * be spawned off as a separate thread
	 * Called and starts a loop until told to stop, implements the Thread
	 * 
	 * @see java.lang.Runnable
	 * @see java.lang.Thread
	 */

    public void run() {	
		listenerBreak = false;
		
        try
        {
           
           while (isFinished())
           {
              // Create a blank receiving packet
              DatagramPacket receivePacket = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;

              // Receive into our initialized packet (blocking)
              socket.receive( receivePacket ) ;
              
              String strData = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
              
              JSONObject json_package = new JSONObject (strData);
              
              v_dist = json_package.getBigDecimal("distance");
              v_collectTime = json_package.getLong("collectTime");
              v_aim = json_package.getJSONArray("aim");
              v_sendTime = json_package.getLong("sendTime");
              v_resolution = json_package.getJSONArray("resolution");
              v_id = json_package.getString("id");
              v_match = json_package.getBoolean("match");
              
              // Return the packet to the sender
              socket.send( receivePacket ) ;
           }  
       }
       catch( Exception e )
       {
          System.out.println( e ) ;
       }
    }


    
	/**
	 * Make this return true when this Command no longer needs to run execute()
	 * 
	 * @return A boolean showing <code>true</code> if the terminateListener() has been called.  
	 * Otherwise, it returns <code>false</code>
	 */
    public boolean isFinished() {
        return listenerBreak;
    }


	/**
	 * Called once to clean up the socket if needed after the thread is down
	 */
    public void end() { 
        socket.close();
    }

	/**
	 * Called to instruct the listner thread to exit gracefully
	 */
    protected void terminateListener() {
    	listenerBreak = true;
    }


	/**
	 * Getter for the "distance" attribute of the JSON vision message packet
	 * 
	 * @return A <code>BigDecimal</code> representing the distance determined from the
	 * Vision system.  (Currently hard coded at 10.0 on the Raspberry PI)
	 */
    public BigDecimal get_distance() {
		return v_dist;
	}

	/**
	 * Getter for the "collectTime" attribute of the JSON vision message packet
	 * 
	 * @return A <code>long</code> representing a time stamp from the vision system.  This value should grow larger 
	 * in subsequent vision packets, and so can be used to determine if new data is present.
	 */
    public long get_collectTime() {
		return v_collectTime;
	}

	/**
	 * Getter for the "aim" attribute of the JSON vision message packet
	 * 
	 * @return A <code>JSONArray</code> a pair of [x,y] offset values for the location of the target.  
	 */
    public JSONArray get_aim() {
		return v_aim;
	}

	/**
	 * Getter for the "sendTime" attribute of the JSON vision message packet
	 * 
	 * @return A <code>long</code> representing a time stamp (as a long)  for the send time from the vision system. 
	 */
    public long get_sendTime() {
		return v_sendTime;
	}

	/**
	 * Getter for the "resolution" attribute of the JSON vision message packet
	 * 
	 * @return A <code>JSONArray</code> a pair of [x,y] screen resolution of a video capture frame
	 */
    public JSONArray get_resolution() {
		return v_resolution;
	}

	/**
	 * Getter for the "id" attribute of the JSON vision message packet
	 * 
	 * @return A <code>String</code> label of the vision package
	 */
    public String get_id() {
		return v_id;
	}

	/**
	 * Getter for the "match" attribute of the JSON vision message packet
	 * 
	 * @return A <code>boolean</code> a true false if there is a match
	 */
    public boolean get_match() {
		return v_match;
	}
}
