public class DMA implements Runnable
{
    private int statusRegister = 0; //Indicates if busy or not
    private int controlRegister; //Indicates if data is to print to console
    private boolean dmaFlag; //Used for interrupt

    private int[] memory2Data;
    private int data;

    private Memory SystemMemory;

    public DMA(Memory Memory)
    {
        SystemMemory = Memory;
    }

    @Override
    public void run()
    {
        dmaFlag = true;
        statusRegister = 1;
        getMemoryLocation();
        IOActivity();
    }

    private void IOActivity() //IO activity for instruction data, prints data
    {
        System.out.println(data);
        dmaFlag = false;
        statusRegister = 0;
        data = 0;
    }

    public boolean dmaFlag() //Used to determine if busy or not
    {
        return dmaFlag;
    }

    public int complete()
    {
        return statusRegister;
    }

    private void getMemoryLocation()
    {
        memory2Data = SystemMemory.accessDMA();
        int physicalMemLocation = SystemMemory.getPage(memory2Data[0]) + memory2Data[1];
        data = SystemMemory.access(physicalMemLocation);
        SystemMemory.removeDMA();
    }
}
