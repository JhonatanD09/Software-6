package models;

import exceptions.RepeatedNameException;

import java.util.ArrayList;


public class Partition implements Comparable<Partition>{

	private String name;
	private long size;
        private long time;
	private Queue<MyProcess> processQueueReady;
	private ArrayList<MyProcess> processesQueue;
	private ArrayList<MyProcess> readyAndDespachado;
	private ArrayList<MyProcess> lockedAndWakeUp;
	private ArrayList<MyProcess> executing;
	private ArrayList<MyProcess> expired;
	private ArrayList<MyProcess> processTerminated;
	private boolean isFinished;
	private boolean isJoin;

	public Partition(String name, long size,Queue<MyProcess> processQueueReady, ArrayList<MyProcess> processes) {
		
		this.name = name;
		this.size = size;
                this.time = 0;
		this.processesQueue = processes;
		this.processQueueReady = processQueueReady;
		this.lockedAndWakeUp = new ArrayList<>();
		this.processTerminated = new ArrayList<>();
		executing = new ArrayList<>();
		expired = new ArrayList<>();
		readyAndDespachado = new ArrayList<>();
		this.isFinished = false;
		this.isJoin = false;
	}

//	public boolean addProcess(MyProcess myProcess) {
//		if (search(myProcess.getName()) == null) {
//			if(myProcess.getSize() <= this.size) {				
//				readyAndDespachado.add(new MyProcess(myProcess.getName(), myProcess.getTime(),myProcess.getSize(), myProcess.isLocked()));
//				processQueueReady.push(myProcess);
//			}else {
//				overSize.add(new MyProcess(myProcess.getName(), myProcess.getTime(), myProcess.getSize(), myProcess.isLocked()));
//			}
//			return true;
//		}
//		return false;
//	}

	public Partition() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Me avisa si no funciona, xd
	 * @param actualName
	 * @param name
	 * @param time
	 * @param lockedStatus
	 */
//	public void editProcess(String actualName, String name, int time,int size, boolean lockedStatus) {
//		edit(search(actualName), name, time,size, lockedStatus);
//		edit(searchInList(actualName, processesQueue), name, time,size, lockedStatus);
//	}
//	
//	private void edit(MyProcess myProcess, String name, int time,int size, boolean lockedStatus) {
//		myProcess.setName(name);
//		myProcess.updateTime(time);
//		myProcess.setSize(size);
//		myProcess.setLocked(lockedStatus);
//	}
	
//	/**
//	 * Eliminar de la cola y de la lista de listos
//	 * @param name
//	 * @return
//	 */
//	public boolean deleteProccess(String name) {
//		boolean isDelete = false;
//		Node<MyProcess> temp = processQueueReady.peek();
//		processesQueue.remove(searchInList(name, processesQueue));
//		if (temp.getData().getName().equals(name)) {
//			processQueueReady.pop();
//			isDelete = true;
//		}else {
//			isDelete = deleteProcess(name, isDelete, temp);
//		}	
//		return isDelete;
//	}
//
//	private boolean deleteProcess(String name, boolean isDelete, Node<MyProcess> temp) {
//		while (temp.getNext() != null) {
//			if (temp.getNext().getData().getName().equals(name)) {
//				temp.setNext(temp.getNext().getNext());
//				isDelete = true;
//			} else {
//				temp = temp.getNext();
//			}
//		}
//		return isDelete;
//	}

//	private MyProcess searchInList(String name, ArrayList<MyProcess> myProcesses) {
//		for (MyProcess myProcess : myProcesses) {
//			if (name.equals(myProcess.getName())) {
//				return myProcess;
//			}
//		}
//		return null;
//	}
//	
//	
//	public MyProcess search(String name) {
//		for (MyProcess myProcess : processesQueue) {
//			if(myProcess.getName().equalsIgnoreCase(name)) {
//				return myProcess;
//			}
//		}
//		return null;
//	}

//	public void startSimulation() {
//		while (!processQueueReady.isEmpty()) {
//			MyProcess process = processQueueReady.peek().getData();
////			if(process.getSize() <= this.size) {				
//				valideSystemTimer(process);
////			}else {
////				overSize.add(new MyProcess(process.getName(), process.getTime(), process.getSize(), process.isLocked()));
////				processQueueReady.pop();
////			}
//		}
//	}

	public void valideSystemTimer(MyProcess process) {
		if(readyAndDespachado!= null) {
			readyAndDespachado.add(new MyProcess(process.getName(), (process.getTime()),process.getSize(), process.isLocked()));
			executing.add(new MyProcess(process.getName(), (process.getTime()-5< 0 ? 0:process.getTime()-5),process.getSize(), process.isLocked()));
			if ((process.getTime() - 5) > 0) {
				proccessTimeDiscount(process);
			} else {
				MyProcess myProcess = processQueueReady.pop();
				myProcess.setTime((int)myProcess.getTime());
				processTerminated.add(myProcess);
				isFinished = true;
				isJoin = true;
			}
		}
	}

	private void proccessTimeDiscount(MyProcess process) {
		process.setTime(5);
		valideLocked(process);
		processesQueue.add(new MyProcess(process.getName(), process.getTime(),process.getSize(), process.isLocked()));
//		readyAndDespachado.add(new MyProcess(process.getName(), process.getTime(),process.getSize(), process.isLocked()));
		processQueueReady.push(processQueueReady.pop());
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
	public Queue<MyProcess> getProcessQueue() {
		return processQueueReady;
	}

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

    @Override
    public int compareTo(Partition o) {
        return (int)(getTime()-o.getTime());
    }
}
