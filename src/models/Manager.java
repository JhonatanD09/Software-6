package models;

import java.util.ArrayList;
import java.util.Collections;

import exceptions.RepeatedNameException;

public class Manager {

	private ArrayList<Partition> partitions;
	private ArrayList<MyProcess> processes;
	private ArrayList<MyProcess> processesTermined;
	private Queue<MyProcess> processQueueReady;
	private ArrayList<Partition> allPartitions; // esta se usa
	private ArrayList<Partition> partitionsTerminated; // esta si se usa
	private ArrayList<String> joinsReports; // esta si se usa
	private int memorySize;
	private int count;

	public Manager() {
		this.processQueueReady = new Queue<>();
		this.partitions = new ArrayList<>();
		this.processes = new ArrayList<MyProcess>();
		this.partitionsTerminated = new ArrayList<>();
		this.joinsReports = new ArrayList<>();
		this.allPartitions = new ArrayList<>();
		processesTermined = new ArrayList<>();
		this.memorySize = 0;
		this.count = 1;
	}

	public void addPartition(String name, long size) {
		partitions.add(new Partition(name, size));
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
		createPartitions();
		int i = 0;
		while (!processQueueReady.isEmpty()) {
			if (i == partitions.size()) {
				i = 0;
			}

			partitions.get(i).valideSystemTimer();

			if (partitions.get(i).isFinished()) {
				if (i + 1 < partitions.size() && i - 1 >= 0) {
					if (partitions.get(i - 1).isFinished() && partitions.get(i + 1).isFinished()) {
						dobleCondesacion(i);
					} else if (partitions.get(i - 1).isFinished()) {
						condesacion(i, (i - 1));
					} else if (partitions.get(i + 1).isFinished()) {
						condesacion(i, (i + 1));
					}
				}
				nextProccess();
			}

			i++;
		}

		int size = 0;
		String finalReportString = "Condesacion de :";
		for (Partition partition : partitions) {
			size += partition.getSize();
			finalReportString += partition.getName() + ", ";
		}
		finalReportString += " y formaron " + ("PAR" + count) + " con un tamaño de " + size;
		joinsReports.add(finalReportString);
		allPartitions.add(new Partition("PAR" + count, size, 0,"Condensacion"));
	}

	private void dobleCondesacion(int i) {
		Partition originalPartition = partitions.get(i);
		long size = partitions.get(i + 1).getSize() + originalPartition.getSize();
		String report = "Condensacion de :" + originalPartition.getName() + " con " + partitions.get(i + 1).getName()
				+ " y formaron " + ("PAR" + count) + " con un tamaño de " + size;
		joinsReports.add(report);
		originalPartition.setName("PAR" + count);
		originalPartition.setSize(size);
		allPartitions.add(new Partition(originalPartition.getName(), originalPartition.getSize(),0,"Condensacion"));
		partitions.remove(i + 1);
		count++;

		size = partitions.get(i - 1).getSize() + originalPartition.getSize();
		String repoert2 = "Condensacion de :" + originalPartition.getName() + " con " + partitions.get(i - 1).getName()
				+ " y formaron " + ("PAR" + count) + " con un tamaño de " + size;
		joinsReports.add(repoert2);
		originalPartition.setName("PAR" + count);
		originalPartition.setSize(size);
		allPartitions.add(new Partition(originalPartition.getName(), originalPartition.getSize(),0,"Condensacion"));
		partitions.remove(i - 1);
		count++;
	}

	private void condesacion(int index, int i) {
		Partition originalPartition = partitions.get(index);
		long size = partitions.get(i).getSize() + originalPartition.getSize();
		String report = "Condensacion de :" + originalPartition.getName() + " con " + partitions.get(i).getName()
				+ " y formaron " + ("PAR" + count) + " con un tamaño de " + size;
		joinsReports.add(report);
		originalPartition.setName("PAR" + count);
		originalPartition.setSize(size);
		allPartitions.add(new Partition(originalPartition.getName(), originalPartition.getSize(),0,"Condensacion"));
		partitions.remove(i);
		count++;
	}

	private void nextProccess() {
		Node<MyProcess> node = processQueueReady.peek();
		while (node != null) {
			int index = valideJoinProcess(node);
			if (index > -1) {
				joinProcess(node, index);
				node = null;
			} else {
				node = node.getNext();
			}
		}
	}

	private void joinProcess(Node<MyProcess> node, int i) {
		Partition partitionN = new Partition("PAR" + count, node.getData().getSize(),node.getData().getTime(),node.getData().getName());
		long size = partitions.get(i).getSize() - node.getData().getSize();
		partitions.get(i).setName("PAR" + count);
		partitions.get(i).setSize(node.getData().getSize());
		partitions.get(i).setMyProcess(node.getData());
		partitions.get(i).setFinished(false);
		partitionsTerminated
				.add(new Partition(partitions.get(i).getName(), partitions.get(i).getSize(), node.getData().getTime(),node.getData().getName()));
		processesTermined.add(new MyProcess(node.getData().getName(), node.getData().getTime(),
				node.getData().getSize(), node.getData().isLocked()));
		allPartitions.add(partitionN);
		count++;
		if (size > 0) {
			Partition partitionNL = new Partition("PAR" + count, size, 0,"libre");
			allPartitions.add(partitionNL);
			partitions.add(i + 1, partitionNL);
			count++;
		}
		processQueueReady.delete(node);
	}

	private int valideJoinProcess(Node<MyProcess> proccess) {
		for (int i = 0; i < partitions.size(); i++) {
			if (partitions.get(i).getSize() >= proccess.getData().getSize() && partitions.get(i).isFinished()) {
				return i;
			}
		}
		return -1;
	}

	private void createPartitions() {
		Node<MyProcess> process = processQueueReady.peek();
		int sizeTemp = 0;
		while (process != null) {
			MyProcess temp = process.getData();
			sizeTemp += temp.getSize();
			if (sizeTemp > memorySize) {
				process = null;
			} else {
				process = process.getNext();
				Partition partition = new Partition("PAR" + count, temp.getSize());
				MyProcess pMyProcess = processQueueReady.pop();
				partition.setMyProcess(pMyProcess);
				partitions.add(partition);
				allPartitions.add(new Partition(partition.getName(), partition.getSize(), pMyProcess.getTime(),pMyProcess.getName()));
				partitionsTerminated.add(new Partition(partition.getName(), partition.getSize(), pMyProcess.getTime(),""));
				processesTermined.add(new MyProcess(pMyProcess.getName(), pMyProcess.getTime(), pMyProcess.getSize(),
						pMyProcess.isLocked()));
				count++;
			}
		}
		Collections.sort(partitionsTerminated);
		Collections.sort(processesTermined);
	}

	public ArrayList<Partition> getPartitions() {
		return partitions;
	}

	public void show() {
//		System.out.println("Coloa general de listos");
//		for (MyProcess p : processes) {
//			System.out.println(p.getName() + " -- " + p.getTime() + " -- " + p.getSize());
//		}
//		System.out.println("----------------------------------------------------");
//		System.out.println("Particiones iniciales");
//		for (Partition p : partitions) {
//			System.out.println(p.getName() + " -- " + p.getSize());
//		}
//
//		System.out.println("----------------------------------------------------");
//		System.out.println("Reportes por particion");
//		for (Partition p : partitions) {
//			System.out.println("-----------------------" + p.getName() + "-----------------------------");
//			System.out.println("Listos y despachados");
//			if (p.getReadyProccess() != null) {
//				for (MyProcess process : p.getReadyProccess()) {
//					System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
//				}
//			}
//			System.out.println("----------------------------------------------------");
//			System.out.println("Ejecucion");
//			for (MyProcess process : p.getExecuting()) {
//				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
//			}
//
//			System.out.println("----------------------------------------------------");
//			System.out.println("Expirados");
//			for (MyProcess process : p.getProcessExpired()) {
//				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
//			}
//
//			System.out.println("----------------------------------------------------");
//			System.out.println("Bloqueo");
//			for (MyProcess process : p.getProcessLocked()) {
//				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
//			}
//
//			System.out.println("----------------------------------------------------");
//			System.out.println("Termiandos");
//			for (MyProcess process : p.getProcessTerminated()) {
//				System.out.println(process.getName() + " -- " + process.getTime() + " -- " + process.getSize());
//			}
//
//		}
////
////
////        System.out.println("----------------------------------------------------");
////        System.out.println("Tiempo de terminacion de las particiones");
////        Collections.sort(partitions);
////        for (Partition p : partitions) {
////            System.out.println(p.getName() + " -- " + p.getTime());
////        }
////
//        System.out.println("----------------------------------------------------");
//        System.out.println("Orden de terminacion de los procesos");
//        Collections.sort(processesTermined);
//        for (MyProcess p : processesTermined) {
//            System.out.println(p.getName() + " -- " + p.getTime());
//        }
	}

	/**
	 *
	 * @return Lista general de procesos o cola general de listos
	 */
	public ArrayList<MyProcess> getProcesses() {
		return processes;
	}

	/**
	 * 
	 * @return reporte de los procesos tterminados en orden
	 */
	public ArrayList<MyProcess> getProcessesTermined() {
		return processesTermined;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	/**
	 * 
	 * @return reportes de uniones
	 */
	public ArrayList<String> getJoinsReports() {
		return joinsReports;
	}

	/**
	 * 
	 * @return reporte de como terminan las particiones en orden
	 */
	public ArrayList<Partition> getPartitionsTerminated() {
		return partitionsTerminated;
	}

	public int getMemorySize() {
		return memorySize;
	}

	/**
	 * 
	 * @return Reporte de todas las partciones creadas
	 */
	public ArrayList<Partition> getAllPartitions() {
		return allPartitions;
	}
//	public ArrayList<Partition> terminatedPartitions(){
//	}

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
	
	public static Object[][] allPartitions(ArrayList<Partition> initialPartitions) {
		Object[][] partitionsInfo = new Object[initialPartitions.size()][3];
		for (int i = 0; i < initialPartitions.size(); i++) {
			partitionsInfo[i][0] = initialPartitions.get(i).getName();
			partitionsInfo[i][1] = initialPartitions.get(i).getData();
			partitionsInfo[i][2] = initialPartitions.get(i).getSize();
		}
		return partitionsInfo;
	}

	public static Object[][] processInfo(ArrayList<MyProcess> processes) {

		Object[][] processInfo = new Object[processes.size()][4];
		for (int i = 0; i < processes.size(); i++) {
			processInfo[i][0] = processes.get(i).getName();
			processInfo[i][1] = processes.get(i).getTime();
			processInfo[i][2] = processes.get(i).getSize();
			processInfo[i][3] = processes.get(i).isLocked();
		}
		return processInfo;
	}

	public ArrayList<Partition> getx() {
		return partitions;
	}

	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.setMemorySize(50);
		manager.addProcess(new MyProcess("P11", 5, 11, false));
		manager.addProcess(new MyProcess("P15", 7, 15, false));
		manager.addProcess(new MyProcess("P18", 8, 18, false));
		manager.addProcess(new MyProcess("P6", 3, 6, false));
		manager.addProcess(new MyProcess("P9", 4, 9, false));
		manager.addProcess(new MyProcess("P13", 6, 13, false));
		manager.addProcess(new MyProcess("P20", 2, 20, false));

		manager.initSimulation();

		manager.show();

		System.out.println("-------------------------------");
		manager.getProcesses();

		for (Partition repor : manager.getAllPartitions()) {
			System.out.println(repor.getData());
			
		}

	}

}
