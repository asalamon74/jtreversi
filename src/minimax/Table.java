package minimax;

public interface Table {
    public Move getEmptyMove();
    public Table copyFrom(Table table);
}
