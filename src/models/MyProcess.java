package models;

import java.math.BigInteger;

public class MyProcess implements Comparable<MyProcess>{

	private String name;
	private long size;
	private long time;
	private boolean locked;

	public MyProcess(String name, long time,long size, boolean locked ) {
		super();
		this.name = name;
		this.time = time;
		this.size = size;
		this.locked = locked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = (this.time - time);
	}

	public void updateTime(long time){
		this.time = time;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}

	public boolean isLocked() {
		return locked;
	}
		
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

    @Override
    public int compareTo(MyProcess o) {
        return (int) (getTime() - o.getTime());
    }
	
}
