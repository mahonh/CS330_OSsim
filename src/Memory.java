import java.util.LinkedList;
import java.util.Queue;

public class Memory
{
    private int[] RAM; //System Memory
    private Queue<int[]> Memory2; //Memory for DMA, a Queue is used to illustrate the methods. This would probably
    private int[] pageTable;        //be a small cache of memory in a real world situation
    private int pageSize;
    private int memoryCounter;

    public Memory()
    {
        RAM = new int[400];
        pageSize = 4;
        pageTable = new int[RAM.length / pageSize]; //creates page table for the number of pages in RAM
        setupPageTable();                           //determined by the pageSize
        Memory2 = new LinkedList<>();
        memoryCounter = 0;
    }

    //Creates the index for the start of each page in RAM for the page table
    private void setupPageTable()
    {
        int x = 0;
        for (int i = 0; i < RAM.length; i++)
        {
            if (i % 4 == 0)
            {
                pageTable[x] = i;
                x++;
            }
        }
    }

    public int getPage(int page)
    {
        return pageTable[page];
    }

    public int getMemoryCounter()
    {
        return memoryCounter;
    }

    //Add data to position in Memory
    public void addDataToMemory(int data)
    {
        RAM[memoryCounter] = data;
        memoryCounter++;
    }

    //Fills the rest of the RAM up with 9s to show it is empty
    public void fillMemory()
    {
        for (int i = memoryCounter; i < RAM.length; i++)
        {
            RAM[i] = 9;
        }
    }

    //Accesses an item from RAM
    public int access(int position)
    {
        return RAM[position];
    }

    //Writes the physical address in the DMA's memory
    public void writeDMA(int[] address)
    {
        Memory2.add(address);
    }

    //Accesses the first item in the DMA's memory
    public int[] accessDMA()
    {
        int[] position = Memory2.peek();
        return position;
    }

    //Removes the first item in the DMA's memory
    public void removeDMA()
    {
        Memory2.poll();
    }
}
