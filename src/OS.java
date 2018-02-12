import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
OS Class is the where the algorithms are stored, CPU and IO Devices are managed
 */

public class OS
{
    private int processCounter;
    private Memory systemMemory;
    private Queue<PCB> Ready_Queue;
    private Queue<PCB> Wait_Queue;
    private Queue<PCB> TerminateQueue;

    public OS()
    {
        Ready_Queue = new LinkedList<PCB>();
        Wait_Queue = new LinkedList<PCB>();
        TerminateQueue = new LinkedList<PCB>();

        systemMemory = new Memory();

        processCounter = 0;

        readFile();
        algorithm_FCFS();
    }

    /*
    FCFS Algorithm simple goes through all of the processes in queue and processes all of them in the order
    they appeared in the queue
     */

    private void algorithm_FCFS()
    {
        CPU cpu = new CPU(systemMemory);
        DMA dma = new DMA(systemMemory);

        PCB process = null; //CPU is idle, process is IO instruction, move to DMA

        while (processCounter > TerminateQueue.size())
        {
            if (!cpu.CPUisBusy()) //Proceeds if the CPU is not busy
            {
                if (cpu.getProcess() == null) //CPU is idle, so new process is added
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //CPU has a process on it
                {
                    if (cpu.getProcess().isComplete()) //Process is complete, move to Terminated Queue
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        TerminateQueue.add(process);
                        cpu.setProcess(null); //CPU is idle again
                        cpu.resetIOReady(0);
                    }
                    else if (cpu.isIOReady() == 1) //CPU is done with process, but the process needs to go to IO (DMA)
                    {
                        process = cpu.getProcess();
                        //systemMemory.writeDMA(process.getDataPointer());
                        process.setState("WAITING");
                        Wait_Queue.add(process);
                        cpu.setProcess(null);
                        cpu.resetIOReady(0);
                    }
                }
            }

            if (!dma.dmaFlag()) //Proceeds if the IODevice is not busy
            {
                PCB temp = null;
                if (!Wait_Queue.isEmpty()) //Checks to see if the Wait Queue is empty
                {
                    temp = Wait_Queue.peek();
                    dma.run();
                }
                if (dma.complete() == 0 && !dma.dmaFlag() && !Wait_Queue.isEmpty()) //complete
                {
                    Wait_Queue.poll();
                    temp.setState("READY"); //Process is added back to Ready Queue for CPU
                    Ready_Queue.add(temp);
                }

            }
        }
    }

    /*
    Reads info from file and creates PCBs, places data into Memory
     */

    private void readFile()
    {
    	System.out.println("\n***File Data***\n");
        String data = "src/Data.csv";

        String processFromFile = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(data))) {

            while ((processFromFile = br.readLine()) != null)
            {
                String[] ProcessData = new String[4];
                
                String[] tags = {"ID: ", "Arrival Order: ", "Instructions: ", "Data: "};
                
                ProcessData = processFromFile.split(cvsSplitBy);

                int ID = Integer.parseInt(ProcessData[0]);
                int ArrivalOrder = Integer.parseInt(ProcessData[1]);
                int Data = Integer.parseInt(ProcessData[3]);

                String tempInstructions = ProcessData[2];
                int[] Instructions = new int[tempInstructions.length()];

                for(int i = 0; i < 4; i++){
                	System.out.println(tags[i] + ProcessData[i]);
                }
                System.out.println("\n");
                
                for (int i = 0; i < tempInstructions.length(); i++)
                {
                    Instructions[i] = Character.getNumericValue(tempInstructions.charAt(i));
                }

                //determine logical address for process data start
                int[] PC = new int[2];
                int memCounter = systemMemory.getMemoryCounter();
                PC[0] = memCounter / 4;
                PC[1] = memCounter % 4;

                for (int i = 0; i < Instructions.length; i++)
                {
                    systemMemory.addDataToMemory(Instructions[i]);
                }

                systemMemory.addDataToMemory(Data);

                //determine logical address for file data
                int[] dataPointer = new int [2];
                memCounter = systemMemory.getMemoryCounter() - 1;
                dataPointer[0] = memCounter / 4;
                dataPointer[1] = memCounter % 4;

                int memoryFiller = 4 - (memCounter % 4);

                for (int i = 0; i < memoryFiller; i++)
                {
                    systemMemory.addDataToMemory(9); //9 is used to represent an empty physical memory location
                }

                memCounter = systemMemory.getMemoryCounter();
                int Pages = memCounter / 4;

                //get first logical address
                //calculate number of pages
                //add null filler
                //add data to memory

                PCB process = new PCB("READY", ID, ArrivalOrder, Pages, PC, dataPointer);
                Ready_Queue.add(process);
                processCounter++;

            }

            systemMemory.fillMemory();
            System.out.println("***End of file data***\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}