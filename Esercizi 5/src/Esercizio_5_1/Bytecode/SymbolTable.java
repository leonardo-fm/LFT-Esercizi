package Esercizio_5_1.Bytecode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    Map<String, Integer> OffsetMap = new HashMap<String,Integer>();

    /**
     * Given the Id and the address create a unique reference to the variable
     * @param s
     * @param address
     */
    public void insert( String s, int address ) {
        if( !OffsetMap.containsValue(address) )
            OffsetMap.put(s,address);
        else
            throw new IllegalArgumentException("Riferimento ad una locazione di memoria gia‘ occupata da un’altra variabile");
    }
    public int lookupAddress ( String s ) {
        if( OffsetMap.containsKey(s) )
            return OffsetMap.get(s);
        else
            return -1;
    }

}
