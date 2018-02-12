public class PCB
{
    /*
    This class is for the Process Control Block
     */
    private int id;
    private int[] PC; //program counter (logical address)
    private int[] dataPointer; //pointer to process data in memory (logical address)
    private int pages; //number of pages for the process
    private int arrivalOrder;
    private String state;

    public PCB(String state, int id, int arrivalOrder, int pages, int[] PC, int[] dataPointer)
    {
        this.setPC(PC);
        this.setDataPointer(dataPointer);
        this.setPages(pages);
        this.setState(state);
        this.setId(id);
        this.setArrivalOrder(arrivalOrder);
    }

    public boolean isComplete() //Checks to see if all instructions are complete
    {
        return (PC[0] == dataPointer[0] && PC[1] == dataPointer[1]);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public int getArrivalOrder()
    {
        return arrivalOrder;
    }

    public void setArrivalOrder(int arrivalOrder)
    {
        this.arrivalOrder = arrivalOrder;
    }

    public int[] getPC()
    {
        return PC;
    }

    public void setPC(int[] PC)
    {
        this.PC = PC;
    }

    public int[] getDataPointer()
    {
        return dataPointer;
    }

    public void setDataPointer(int[] dataPointer)
    {
        this.dataPointer = dataPointer;
    }

    public int getPages()
    {
        return pages;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }
}
