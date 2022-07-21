package models;

import java.util.ArrayList;
import java.util.Collections;

import exceptions.RepeatedNameException;

public class Manager {

	private ArrayList<Partition> partitions;
	private ArrayList<MyProcess> processes;
	private ArrayList<MyProcess> processesTermined;
	private Queue<MyProcess> processQueueReady;
	private int positionName;
	private ArrayList<Partition> partitionsNews;
	private ArrayList<String> joinsReports;

	public Manager() {
		this.processQueueReady = new Queue<>();
		partitions = new ArrayList<>();
		this.processes = new ArrayList<MyProcess>();
		partitionsNews = new ArrayList<>();
		joinsReports = new ArrayList<>();
	}

	public void addPartition(String name, long size) {
		partitions.add(new Partition(name, size, this.processQueueReady, this.processes));
	}

	public Partition searchPartition(String name) {
		for (Partition partition : partitions) {
			if (partition.getName().equalsIgnoreCase(name)) {
				return partition;
			}
		}
		return null;
	}

	public void verifyProcessName(String processName) throws RepeatedNameException {
		for (MyProcess process : processes) {
			if (processName.equals(process.getName())) {
				throw new RepeatedNameException(processName);
			}
		}
	}

	public boolean deletePartition(String name) {
		Partition partition = searchPartition(name);
		if (partition != null) {
			partitions.remove(partition);
			return true;
		} else {
			return false;
		}
	}

	public boolean addProcess(MyProcess myProcess) {
		if (search(myProcess.getName()) == null) {
			processes.add(
					new MyProcess(myProcess.getName(), myProcess.getTime(), myProcess.getSize(), myProcess.isLocked()));
			processQueueReady.push(myProcess);
			return true;
		}
		return false;
	}

	private MyProcess searchInList(String name, ArrayList<MyProcess> myProcesses) {
		for (MyProcess myProcess : myProcesses) {
			if (name.equalsIgnoreCase(myProcess.getName())) {
				return myProcess;
			}
		}
		return null;
	}

	public MyProcess search(String name) {
		Node<MyProcess> temp = processQueueReady.peek();
		while (temp != null) {
			if (temp.getData().getName().equals(name)) {
				return temp.getData();
			} else {
				temp = temp.getNext();
			}
		}
		return null;
	}

	public void editProcess(String actualName, String name, long time, long size, boolean lockedStatus) {
		edit(search(actualName), name, time, size, lockedStatus);
		edit(searchInList(actualName, processes), name, time, size, lockedStatus);
	}

	private void edit(MyProcess myProcess, String name, long time, long size, boolean lockedStatus) {
		myProcess.setName(name);
		myProcess.updateTime(time);
		myProcess.setSize(size);
		myProcess.setLocked(lockedStatus);
	}

	public boolean deleteProccess(String name) {
		boolean isDelete = false;
		Node<MyProcess> temp = processQueueReady.peek();
		processes.remove(searchInList(name, processes));
		if (temp.getData().getName().equals(name)) {
			processQueueReady.pop();
			isDelete = true;
		} else {
			isDelete = deleteProcess(name, isDelete, temp);
		}
		return isDelete;
	}

	private boolean deleteProcess(String name, boolean isDelete, Node<MyProcess> temp) {
		while (temp.getNext() != null) {
			if (temp.getNext().getData().getName().equals(name)) {
				temp.setNext(temp.getNext().getNext());
				isDelete = true;
			} else {
				temp = temp.getNext();
			}
		}
		return isDelete;
	}

	public void initSimulation() {
		createNewsPartitions();
		lisTermitatedOrder();
		int count = 0;
		positionName = partitions.size();
		while (!processQueueReady.isEmpty()) {
			MyProcess process = processQueueReady.peek().getData();
			if (count >= partitions.size()) {
				count = 0;
			}
			if (!partitions.get(count).isFinished()) {
				partitions.get(count).valideSystemTimer(process);
			}

			if (partitions.get(count).isFinished()) {
				updatePartition(partitions.get(count));
			}
			checkJoin();
			count++;
		}
	}

	private void lisTermitatedOrder() {
		processesTermined = new ArrayList<>(processes);
		Collections.sort(processesTermined);
	}

	private void checkJoin() {
		boolean join = true;
		while (join) {
			join = join() > 1;
			for (int i = 0; i < partitionsNews.size(); i++) {
				if (partitionsNews.get(i).isFinished()) {
					if (i + 1 < partitionsNews.size() && partitionsNews.get(i + 1).isFinished()) {
						updateNewPartition(partitionsNews.get(i), partitionsNews.get(i + 1));
					}
				}
			}
		}
	}

	private void updateNewPartition(Partition partition, Partition partition2) {
		positionName++;
		long size = partition.getSize() + partition2.getSize();
		String report = partition.getName() + " se une con " + partition2.getName() + " y forman " + "PAR"
				+ positionName + " con un tamaño de :" + size;
		System.out.println(report);
		joinsReports.add(report);
		partition.setName("PAR" + positionName);
		partition.setSize(size);
		partition.setFinished(true);
		partitionsNews.remove(partition2);
	}

	private int join() {
		int count = 0;
		for (int i = 0; i < partitionsNews.size(); i++) {
			if (partitionsNews.get(i).isFinished()) {
				if (i + 1 < partitionsNews.size() && partitionsNews.get(i + 1).isFinished()) {
					count++;
				}
			}
		}
		return count;
	}

	private void updatePartition(Partition p) {
		for (Partition partition : partitionsNews) {
			if (p.getName().equalsIgnoreCase(partition.getName())) {
				partition.setFinished(true);
			}
		}
	}

	private void createNewsPartitions() {
		Node<MyProcess> temp = processQueueReady.peek();
		int count = 1;
		while (temp != null) {
			MyProcess process = temp.getData();
			Partition partition = new Partition("PAR" + count, process.getSize(), processQueueReady, processes);
			partitionsNews.add(new Partition(partition.getName(), partition.getSize(), processQueueReady, processes));
			partitions.add(partition);
			count++;
			temp = temp.getNext();
		}
	}

	public ArrayList<Partition> getPartitions() {
		return partitions;
	}

	public void show() {
		System.out.println("Coloa general de listos");
		for (MyProcess p : processes) {
			System.out.println(p.getName() + " -- " + p.getTime() + " -- " + p.getSize());
		}
		System.out.println("----------------------------------------------------");
		System.out.println("Particiones iniciales");
		for (Partition p : partitions) {
			System.out.println(p.getName() + " -- " + p.getSize());
		}
//
		System.out.println("----------------------------------------------------");
		System.out.println("Reportes por particion");
		for (Partition p : partitions) {
			System.out.println("-----------------------" + p.getName() + "-----------------------------");
			System.out.println("Listos y despachados");
			if (p.getReadyProccess() != null) {
				for (MyProcess process : p.getReadyProccess()) {
					System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
				}
			}
			System.out.println("----------------------------------------------------");
			System.out.println("Ejecucion");
			for (MyProcess process : p.getExecuting()) {
				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
			}

			System.out.println("----------------------------------------------------");
			System.out.println("Expirados");
			for (MyProcess process : p.getProcessExpired()) {
				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
			}

			System.out.println("----------------------------------------------------");
			System.out.println("Bloqueo");
			for (MyProcess process : p.getProcessLocked()) {
				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
			}

			System.out.println("----------------------------------------------------");
			System.out.println("Termiandos");
			for (MyProcess process : p.getProcessTerminated()) {
				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
			}

		}
//
//
//        System.out.println("----------------------------------------------------");
//        System.out.println("Tiempo de terminacion de las particiones");
//        Collections.sort(partitions);
//        for (Partition p : partitions) {
//            System.out.println(p.getName() + " -- " + p.getTime());
//        }
//
        System.out.println("----------------------------------------------------");
        System.out.println("Orden de terminacion de los procesos");
        Collections.sort(processesTermined);
        for (MyProcess p : processesTermined) {
            System.out.println(p.getName() + " -- " + p.getTime());
        }
	}

	/**
	 *
	 * @return Lista general de procesos o cola general de listos
	 */
	public ArrayList<MyProcess> getProcesses() {
		return processes;
	}

	public ArrayList<MyProcess> getProcessesTermined() {
		return processesTermined;
	}
	
	/**
	 * 
	 * @return reportes de uniones
	 */
	public ArrayList<String> getJoinsReports() {
		return joinsReports;
	}

	public static Object[][] processProcessTermiedInfo(ArrayList<MyProcess> termined) {
		Object[][] processInfo = new Object[termined.size()][2];
		for (int i = 0; i < termined.size(); i++) {
			processInfo[i][0] = termined.get(i).getName();
			processInfo[i][1] = termined.get(i).getTime();
		}
		return processInfo;
	}

	public static String[] processJoinsInfo(ArrayList<String> joinsReports) {
		String[] processInfo = new String[joinsReports.size()];
		for (int i = 0; i < joinsReports.size(); i++) {
			processInfo[i] = joinsReports.get(i);
		}
		return processInfo;
	}

	public static Object[][] processInitialPartitionsInfo(ArrayList<Partition> initialPartitions) {
		Object[][] partitionsInfo = new Object[initialPartitions.size()][2];
		for (int i = 0; i < initialPartitions.size(); i++) {
			partitionsInfo[i][0] = initialPartitions.get(i).getName();
			partitionsInfo[i][1] = initialPartitions.get(i).getSize();
		}
		return partitionsInfo;
	}


	public ArrayList<Partition> getPartitionsNews() {
		return partitionsNews;
	}

	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.addProcess(new MyProcess("P1", 26, 10, false));
		manager.addProcess(new MyProcess("P2", 12, 20, false));
		manager.addProcess(new MyProcess("P3", 23, 30, false));
		manager.addProcess(new MyProcess("P4", 10, 20, false));
		manager.addProcess(new MyProcess("P5", 24, 5, false));
		manager.addProcess(new MyProcess("P6", 15, 15, false));

		manager.initSimulation();

		manager.show();

		for (Partition par : manager.getPartitionsNews()) {
			System.out.println(par.getName() + " " + par.getSize());
		}
	}
}
