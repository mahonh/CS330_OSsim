import java.util.Random;

/*
CPU class acts like a CPU and accepts bursts from the OS
 */

public class CPU implements Runnable
{
    private Memory SystemMemory;

    private boolean BusyOrNot;
    private int done; //0 for cpu task, 1 for IO task

    private int[] PC;
    private int instruction;

    private int processCount;
    private PCB process;

    public CPU(Memory SystemMemory)
    {
        this.SystemMemory = SystemMemory;
        BusyOrNot = false;
        done = 0; //CPU task
    }

    public void run() //Thread is used for CPU so it does not interfere with Main Thread
    {
        while (done == 0)
        {
            BusyOrNot = true;
            readyMemory();
            execute(instruction);
        }
        BusyOrNot = false;
    }

    //reads instruction from memory
    private void readyMemory()
    {
        PC = process.getPC();
        int physicalMemLocation = SystemMemory.getPage(PC[0]) + PC[1];
        instruction = SystemMemory.access(physicalMemLocation);
    }

    //updates and moves the PC forward one
    private void updatePC()
    {
        PC[1] = PC[1] + 1;
        process.setPC(PC);

        PC = process.getPC();

        int temp = PC[1];
        if (temp == 4)
        {
            PC[0] = PC[0] + 1;
            PC[1] = 0;
            process.setPC(PC);
        }
    }

    private void execute(int instruction)
    {
        if (instruction == 1)
        {
            BubbleSort();
            updatePC();
        }

        else if (instruction == 0) //IO Instruction, moves PC forward one
        {
            done = 1;

            SystemMemory.writeDMA(process.getDataPointer());

            updatePC();
        }

        else if (process.isComplete())
        {
            done = 2;
        }
    }

    private void BubbleSort()
    {
        int[] bubbleSort = new int[1000];
        Random random = new Random();
        int x;
        //Creates the random 1,000 integers
        for (int i = 0; i < 1000; i++) {
            x = random.nextInt(10000) + 1;
            bubbleSort[i] = x;
        }
        //Bubble sort algorithm
        int temp;
        for (int i = 0; i < bubbleSort.length; i++){
            for (int j = 1; j < bubbleSort.length; j++){
                if (bubbleSort[j-1] > bubbleSort[j]){
                    temp = bubbleSort[j-1];
                    bubbleSort[j-1] = bubbleSort[j];
                    bubbleSort[j] = temp;
                }
            }
        }
    }

    public Boolean CPUisBusy()
    {
        return BusyOrNot;
    }

    public int isIOReady()
    {
        return done;
    }

    public void resetIOReady(int done)
    {
        this.done = done;
    }

    public void setPCB(PCB pcb)
    {
        process = pcb;
    }

    public void setProcess(PCB process)
    {
        this.process = process;
    }

    public PCB getProcess()
    {
        return process;
    }
}
