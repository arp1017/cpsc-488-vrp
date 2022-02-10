package Zeus;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: Interface class giving basic structure for any Operation class
 * using Tabu</p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public interface Operation {
    public Tabu.TabuOperation getFirstTabuOperation();

    public Tabu.TabuOperation getSecondTabuOperation();

    public void setFirstTabuOperation(Tabu.TabuOperation firstTabu);

    public void setSecondTabuOperation(Tabu.TabuOperation secondTabu);
}
