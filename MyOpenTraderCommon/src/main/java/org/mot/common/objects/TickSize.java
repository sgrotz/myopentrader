package org.mot.common.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TickSize implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7632463700332520014L;
	private String symbol;
	private String field;
	private int size;
	private long timestamp;
	private boolean replay = false;
	
	
	public TickSize(String symbol, String field, int size, Long timestamp) {
		this.symbol = symbol;
		this.field = field;
		this.size = size;
		this.timestamp = timestamp;
	}
	
	
	
	
	public boolean isReplay() {
		return replay;
	}




	public void setReplay(boolean replay) {
		this.replay = replay;
	}




	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	

	public byte[] serialize() {
		// TODO Auto-generated method stub
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        
        try {
        	ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return b.toByteArray();

	}
	
    public static TickSize deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (TickSize)o.readObject();
    }
	
	
	
}
