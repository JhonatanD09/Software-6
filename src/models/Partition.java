package models;

import exceptions.RepeatedNameException;

import java.util.ArrayList;


public class Partition implements Comparable<Partition>{

	private static final int timeSystem = 1;
	private String name;
	private long size;
        private long time;
	private ArrayList<MyProcess> processesQueue;
	private ArrayList<MyProcess> readyAndDespachado;
	private ArrayList<MyProcess> lockedAndWakeUp;
	private ArrayList<MyProcess> executing;
	private ArrayList<MyProcess> expired;
	private ArrayList<MyProcess> processTerminated;
	private MyProcess  process;
	private boolean isFinished;
	private boolean isJoin;
	private boolean isFree;

	public Partition(String name, long size) {
		
		this.name = name;
		this.size = size;
        this.time = 0;
		this.processesQueue = new ArrayList<>();
		this.lockedAndWakeUp = new ArrayList<>();
		this.processTerminated = new ArrayList<>();
		executing = new ArrayList<>();
		expired = new ArrayList<>();
		readyAndDespachado = new ArrayList<>();
		this.isFinished = false;
		this.isJoin = false;
	}
	
	public Partition(String name , long size, long time) {
		this.name = name;
		this.size = size;
		this.isFinished = true;
		this.isFree = true;
		this.process = null;
		this.time = time;
	}

	public void valideSystemTimer() {
		if(isFree== false)
		if(readyAndDespachado!= null) {
			readyAndDespachado.add(new MyProcess(process.getName(), (process.getTime()),process.getSize(), process.isLocked()));
			executing.add(new MyProcess(process.getName(), (process.getTime()-timeSystem< 0 ? 0:process.getTime()-timeSystem),process.getSize(), process.isLocked()));
			if ((process.getTime() - timeSystem) > 0) {
				proccessTimeDiscount(process);
			} else {
				process.setTime((int)process.getTime());
				processTerminated.add(process);
				isFinished = true;
				isJoin = true;
			}
		}
	}

	private void proccessTimeDiscount(MyProcess process) {
		process.setTime(timeSystem);
		valideLocked(process);
		processesQueue.add(new MyProcess(process.getName(), process.getTime(),process.getSize(), process.isLocked()));
//		readyAndDespachado.add(new MyProcess(process.getName(), process.getTime(),process.getSize(), process.isLocked()));
//		processQueueReady.push(processQueueReady.pop());
	}

	private void valideLocked(MyProcess process) {
		if (process.isLocked()) {
			lockedAndWakeUp.add(new MyProcess(process.getName(), process.getTime(),process.getSize(),process.isLocked()));
		} else {
			expired.add(new MyProcess(process.getName(), process.getTime(),process.getSize(), process.isLocked()));
		}
	}

	/**
	 * 
	 * @return Los procesos que se van agregando a la lista, estos toca ir actualizando
	 * cada que se agregan a la interfaz
	 */
//	public Queue<MyProcess> getProcessQueue() {
//		return processQueueReady;
//	}

	public void verifyProcessName(String name) throws RepeatedNameException {
            for (MyProcess myProcess : processesQueue) {
		if (myProcess.getName().equalsIgnoreCase(name)) {
                    throw new RepeatedNameException(name);
		}
            }
	}


	public ArrayList<MyProcess> getProcessQueueLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Procesos terminados
	 */
	public ArrayList<MyProcess> getProcessTerminated() {
		return processTerminated;
	}

	/**
	 * 
	 * @return Lista de los procesos listos
	 */
	public ArrayList<MyProcess> getReadyProccess() {
		return readyAndDespachado;
	}

	/**
	 * 
	 * @return Procesos despachados
	 */
	public ArrayList<MyProcess> getProcessDespachados() {
		return readyAndDespachado;
	}

	/**
	 * 
	 * @return  Processos en ejecucion
	 */
	public ArrayList<MyProcess> getExecuting() {
		return executing;
	}

	/**
	 * 
	 * @return Procesos expirados
	 */
	public ArrayList<MyProcess> getProcessExpired() {
		return expired;
	}

	/**
	 * 
	 * @return Los que pasan a bloqueado
	 */
	public ArrayList<MyProcess> getProcessToLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Porcesos bloqueados
	 */
	public ArrayList<MyProcess> getProcessLocked() {
		return lockedAndWakeUp;
	}

	/**
	 * 
	 * @return Procesos despertados
	 */
	public ArrayList<MyProcess> getProcessWakeUp() {
		return lockedAndWakeUp;
	}
	
	public ArrayList<MyProcess> getProcessesQueue() {
		return processesQueue;
	}
	
	public void setProcessesQueue(ArrayList<MyProcess> processesQueue) {
		this.processesQueue = processesQueue;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}

        public long getTime() {
            return time;    
        }

        public void setTime(long time) {
            this.time += time;
        }
        
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public static Object[][] processInfo(ArrayList<MyProcess> processes){
		
		Object[][] processInfo = new Object[processes.size()][4];
		for (int i = 0; i < processes.size(); i++) {
			processInfo[i][0] = processes.get(i).getName();
			processInfo[i][1] = processes.get(i).getTime();
			processInfo[i][2] = processes.get(i).getSize();
			processInfo[i][3] = processes.get(i).isLocked();
		}
		return processInfo;
	}

	public boolean isFinished() {
		return isFinished;
	}
	
	public boolean isJoin() {
		return isJoin;
	}
	
	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public MyProcess getMyProcess() {
		return process;
	}
	
	public void setMyProcess(MyProcess myProcess) {
		this.process = myProcess;
	}
	
    @Override
    public int compareTo(Partition o) {
        return (int)(getTime()-o.getTime());
    }
}
