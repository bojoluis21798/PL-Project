/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.HashMap;
import java.util.Map;
import readfile.tokenizer.TokenType;

//unused imports
import parser.memory;

/**
 *
 * @author User
 * @param <Integer>
 * @param <String>
 */
public class BiHashMap{

private final Map<Integer, Map<String, memory>> mMap;

public BiHashMap() {
    mMap = new HashMap<Integer, Map<String, memory>>();
}

/**
 * Associates the specified value with the specified keys in this map (optional operation). If the map previously
 * contained a mapping for the key, the old value is replaced by the specified value.
 * 
 * @param key1
 *            the first key
 * @param key2
 *            the second key
 * @param value
 *            the value to be set
 * @return the value previously associated with (key1,key2), or <code>null</code> if none
 * @see Map#put(Object, Object)
 */
public Object put(Integer key1, String key2, memory value) {
    Map<String, memory> map;
    if (mMap.containsKey(key1)) {
        map = mMap.get(key1);
    } else {
        map = new HashMap<String, memory>();
        mMap.put(key1, map);
    }

    return map.put(key2, value);
}

/**
 * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for
 * the key.
 * 
 * @param key1
 *            the first key whose associated value is to be returned
 * @param key2
 *            the second key whose associated value is to be returned
 * @return the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for
 *         the key
 * @see Map#get(Object)
 */
public Object get(int key1, String key2) {
    if (mMap.containsKey(key1)) {
        return mMap.get(key1).get(key2).var;
    } else {
        return null;
    }
}

public TokenType getTokenType(int key1, String key2){
    if (mMap.containsKey(key1)) {
        return mMap.get(key1).get(key2).tok;
    } else {
        return null;
    }
}
public boolean removeKey(int key1){

    boolean retval = false;
    if(mMap.containsKey(key1)){
        mMap.remove(key1);
        retval = true;
    }

    return retval;
}
/**
 * Returns <code>true</code> if this map contains a mapping for the specified key
 * 
 * @param key1
 *            the first key whose presence in this map is to be tested
 * @param key2
 *            the second key whose presence in this map is to be tested
 * @return Returns true if this map contains a mapping for the specified key
 * @see Map#containsKey(Object)
 */
public boolean containsKeys(Integer key1, String key2) {
    return mMap.containsKey(key1) && mMap.get(key1).containsKey(key2);
}

public void clear() {
    mMap.clear();
}

}